package at.tugraz.sw.hoi.messenger.test.otr;

import net.java.otr4j.OtrPolicy;
import net.java.otr4j.OtrPolicyImpl;
import net.java.otr4j.session.SessionID;
import net.java.otr4j.session.SessionStatus;
import at.tugraz.sw.hoi.messenger.otr.HoiOtrEngine;
import at.tugraz.sw.hoi.messenger.otr.HoiOtrEngineHost;

public class OtrTest extends junit.framework.TestCase {

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
		return new HoiOtrEngineHost(new OtrPolicyImpl(OtrPolicy.ALLOW_V2
				| OtrPolicy.ERROR_START_AKE));
	}

	private HoiOtrEngine getUsAlice(HoiOtrEngineHost host) {
		return new HoiOtrEngine(host);
	}

	public void testSession() throws Exception {
		this.startSession();
		this.exchageMessages();
		this.endSession();
	}

	private at.tugraz.sw.hoi.messenger.otr.HoiOtrEngine usAlice;
	private HoiOtrEngine usBob;
	private HoiOtrEngineHost hostAlice;
	private HoiOtrEngineHost hostBob;

	private void startSession() {
		hostAlice = getHostAlice();
		hostBob = new HoiOtrEngineHost(new OtrPolicyImpl(OtrPolicy.ALLOW_V2
				| OtrPolicy.ERROR_START_AKE));

		usAlice = new HoiOtrEngine(hostAlice);
		usBob = new HoiOtrEngine(hostBob);

		usAlice.startSession(getAliceSessionID());

		// Bob receives query, sends D-H commit.
		usBob.transformReceiving(getBobSessionID(),
				hostAlice.lastInjectedMessage);

		// Alice received D-H Commit, sends D-H key.
		hostAlice = getHostAlice();
		usAlice = getUsAlice(hostAlice); // = new HoiOtrEngine(hostAlice);
		usAlice.transformReceiving(getAliceSessionID(),
				hostBob.lastInjectedMessage);

		// Bob receives D-H Key, sends reveal signature.
		usBob.transformReceiving(getBobSessionID(),
				hostAlice.lastInjectedMessage);

		// Alice receives Reveal Signature, sends signature and goes secure.
		usAlice.transformReceiving(getAliceSessionID(),
				hostBob.lastInjectedMessage);

		// Bobs receives Signature, goes secure.
		usBob.transformReceiving(getBobSessionID(),
				hostAlice.lastInjectedMessage);

		if (usBob.getSessionStatus(getBobSessionID()) != SessionStatus.ENCRYPTED
				|| usAlice.getSessionStatus(getAliceSessionID()) != SessionStatus.ENCRYPTED)
			fail("Could not establish a secure session.");
	}

	private void exchageMessages() {
		// We are both secure, send encrypted message.
		String clearTextMessage = "Hello Bob, this new IM software you installed on my PC the other day says we are talking Off-the-Record, what is that supposed to mean?";
		String sentMessage = usAlice.transformSending(getAliceSessionID(),
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
		receivedMessage = usAlice.transformReceiving(getAliceSessionID(),
				sentMessage);

		if (!clearTextMessage.equals(receivedMessage))
			fail();

		// Send encrypted message.
		clearTextMessage = "Oh, is that all?";
		sentMessage = usAlice.transformSending(getAliceSessionID(),
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
		receivedMessage = usAlice.transformReceiving(getAliceSessionID(),
				sentMessage);

		if (!clearTextMessage.equals(receivedMessage))
			fail();

		// Send encrypted message. Test UTF-8 space characters.
		clearTextMessage = "Oh really?! pouvons-nous parler en français?";

		sentMessage = usAlice.transformSending(getAliceSessionID(),
				clearTextMessage);

		// Receive encrypted message.
		receivedMessage = usBob.transformReceiving(getBobSessionID(),
				sentMessage);
		if (!clearTextMessage.equals(receivedMessage))
			fail();
	}

	private void endSession() {
		usBob.endSession(getBobSessionID());
		usAlice.endSession(getAliceSessionID());

		if (usBob.getSessionStatus(getBobSessionID()) != SessionStatus.PLAINTEXT
				|| usAlice.getSessionStatus(getAliceSessionID()) != SessionStatus.PLAINTEXT)
			fail("Failed to end session.");
	}
}
