package at.tugraz.sw.hoi.messenger.test.otr;

import net.java.otr4j.OtrPolicy;
import net.java.otr4j.OtrPolicyImpl;
import net.java.otr4j.session.SessionID;
import net.java.otr4j.session.SessionStatus;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.test.AndroidTestCase;
import at.tugraz.sw.hoi.messenger.otr.HoiOtrEngine;
import at.tugraz.sw.hoi.messenger.otr.HoiOtrEngineHost;

public class OtrTest extends AndroidTestCase {

	private Context context;
	private SharedPreferences prefs;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		context = getContext();
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();

		// editor.putString(Configuration.PROPERTY_REG_ID, "");
		editor.commit();
	}

	public void testSessionIdEquality() throws Exception {
		SessionID aliceSessionID2 = getAliceSessionID();
		assertTrue(getAliceSessionID().equals(aliceSessionID2));
	}

	private SessionID getAliceSessionID() {
		return new SessionID("Alice@Wonderland", "Bob@Wonderland", "Scytale");
	}

	private SessionID getBobSessionID() {
		return new SessionID("Bob@Wonderland", "Alice@Wonderland", "Scytale");
	}

	private HoiOtrEngineHost getHostAlice() {
		if (hostAlice_ == null) {
			hostAlice_ = new HoiOtrEngineHost(new OtrPolicyImpl(
					OtrPolicy.ALLOW_V2 | OtrPolicy.ERROR_START_AKE), prefs);
		}

		return hostAlice_;
	}

	private HoiOtrEngine getUsAlice() {
		if (usAlice_ == null) {
			usAlice_ = new HoiOtrEngine(getHostAlice());
		}
		return usAlice_;
	}

	public void testSession() throws Exception {
		this.startSession();
		this.exchageMessages();
		this.endSession();
	}

	private HoiOtrEngine usAlice_;
	private HoiOtrEngine usBob;
	private HoiOtrEngineHost hostAlice_;
	private HoiOtrEngineHost hostBob;

	private void startSession() {
		hostBob = new HoiOtrEngineHost(new OtrPolicyImpl(OtrPolicy.ALLOW_V2
				| OtrPolicy.ERROR_START_AKE), prefs);

		usBob = new HoiOtrEngine(hostBob);

		getUsAlice().startSession(getAliceSessionID());

		// Bob receives query, sends D-H commit.
		usBob.transformReceiving(getBobSessionID(),
				getHostAlice().lastInjectedMessage);

		// Alice received D-H Commit, sends D-H key.
		getUsAlice().transformReceiving(getAliceSessionID(),
				hostBob.lastInjectedMessage);

		// Bob receives D-H Key, sends reveal signature.
		usBob.transformReceiving(getBobSessionID(),
				getHostAlice().lastInjectedMessage);

		// Alice receives Reveal Signature, sends signature and goes secure.
		destroyAlice();
		getUsAlice().transformReceiving(getAliceSessionID(),
				hostBob.lastInjectedMessage);

		// Bobs receives Signature, goes secure.
		usBob.transformReceiving(getBobSessionID(),
				getHostAlice().lastInjectedMessage);

		if (usBob.getSessionStatus(getBobSessionID()) != SessionStatus.ENCRYPTED
				|| getUsAlice().getSessionStatus(getAliceSessionID()) != SessionStatus.ENCRYPTED)
			fail("Could not establish a secure session.");
	}

	private void destroyAlice() {
		usAlice_ = null;
		hostAlice_ = null;
	}

	private void exchageMessages() {
		// We are both secure, send encrypted message.
		String clearTextMessage = "Hello Bob, this new IM software you installed on my PC the other day says we are talking Off-the-Record, what is that supposed to mean?";
		String sentMessage = getUsAlice().transformSending(getAliceSessionID(),
				clearTextMessage);

		// Receive encrypted message.
		String receivedMessage = usBob.transformReceiving(getBobSessionID(),
				sentMessage);

		if (!clearTextMessage.equals(receivedMessage))
			fail();

		// Send encrypted message.
		clearTextMessage = "Hey Alice, it means that our communication is encrypted and authenticated.";
		sentMessage = usBob.transformSending(getBobSessionID(),
				clearTextMessage);

		// Receive encrypted message.
		receivedMessage = getUsAlice().transformReceiving(getAliceSessionID(),
				sentMessage);

		if (!clearTextMessage.equals(receivedMessage))
			fail();

		// Send encrypted message.
		clearTextMessage = "Oh, is that all?";
		sentMessage = getUsAlice().transformSending(getAliceSessionID(),
				clearTextMessage);

		// Receive encrypted message.
		receivedMessage = usBob.transformReceiving(getBobSessionID(),
				sentMessage);
		if (!clearTextMessage.equals(receivedMessage))
			fail();

		// Send encrypted message.
		clearTextMessage = "Actually no, our communication has the properties of perfect forward secrecy and deniable authentication.";
		sentMessage = usBob.transformSending(getBobSessionID(),
				clearTextMessage);

		// Receive encrypted message.
		receivedMessage = getUsAlice().transformReceiving(getAliceSessionID(),
				sentMessage);

		if (!clearTextMessage.equals(receivedMessage))
			fail();

		// Send encrypted message. Test UTF-8 space characters.
		clearTextMessage = "Oh really?! pouvons-nous parler en français?";

		sentMessage = getUsAlice().transformSending(getAliceSessionID(),
				clearTextMessage);

		// Receive encrypted message.
		receivedMessage = usBob.transformReceiving(getBobSessionID(),
				sentMessage);
		if (!clearTextMessage.equals(receivedMessage))
			fail();
	}

	private void endSession() {
		usBob.endSession(getBobSessionID());
		getUsAlice().endSession(getAliceSessionID());

		if (usBob.getSessionStatus(getBobSessionID()) != SessionStatus.PLAINTEXT
				|| getUsAlice().getSessionStatus(getAliceSessionID()) != SessionStatus.PLAINTEXT)
			fail("Failed to end session.");
	}
}
