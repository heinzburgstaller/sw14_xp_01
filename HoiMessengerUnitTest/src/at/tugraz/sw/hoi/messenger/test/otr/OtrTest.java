package at.tugraz.sw.hoi.messenger.test.otr;

import net.java.otr4j.OtrPolicy;
import net.java.otr4j.OtrPolicyImpl;
import net.java.otr4j.session.SessionID;
import net.java.otr4j.session.SessionStatus;
import at.tugraz.sw.hoi.messenger.otr.HoiOtrEngine;
import at.tugraz.sw.hoi.messenger.otr.HoiOtrEngineHost;

public class OtrTest extends junit.framework.TestCase {

	private SessionID aliceSessionID = new SessionID("Alice@Wonderland",
			"Bob@Wonderland", "Scytale");

	private SessionID bobSessionID = new SessionID("Bob@Wonderland",
			"Alice@Wonderland", "Scytale");

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
		hostAlice = new HoiOtrEngineHost(new OtrPolicyImpl(OtrPolicy.ALLOW_V2
				| OtrPolicy.ERROR_START_AKE));
		hostBob = new HoiOtrEngineHost(new OtrPolicyImpl(OtrPolicy.ALLOW_V2
				| OtrPolicy.ERROR_START_AKE));

		usAlice = new HoiOtrEngine(hostAlice);
		usBob = new HoiOtrEngine(hostBob);

		usAlice.startSession(aliceSessionID);

		// Bob receives query, sends D-H commit.
		usBob.transformReceiving(bobSessionID, hostAlice.lastInjectedMessage);

		// Alice received D-H Commit, sends D-H key.
		usAlice.transformReceiving(aliceSessionID, hostBob.lastInjectedMessage);

		// Bob receives D-H Key, sends reveal signature.
		usBob.transformReceiving(bobSessionID, hostAlice.lastInjectedMessage);

		// Alice receives Reveal Signature, sends signature and goes secure.
		usAlice.transformReceiving(aliceSessionID, hostBob.lastInjectedMessage);

		// Bobs receives Signature, goes secure.
		usBob.transformReceiving(bobSessionID, hostAlice.lastInjectedMessage);

		if (usBob.getSessionStatus(bobSessionID) != SessionStatus.ENCRYPTED
				|| usAlice.getSessionStatus(aliceSessionID) != SessionStatus.ENCRYPTED)
			fail("Could not establish a secure session.");
	}

	private void exchageMessages() {
		// We are both secure, send encrypted message.
		String clearTextMessage = "Hello Bob, this new IM software you installed on my PC the other day says we are talking Off-the-Record, what is that supposed to mean?";
		String sentMessage = usAlice.transformSending(aliceSessionID,
				clearTextMessage);

		// Receive encrypted message.
		String receivedMessage = usBob.transformReceiving(bobSessionID,
				sentMessage);

		if (!clearTextMessage.equals(receivedMessage))
			fail();

		// Send encrypted message.
		clearTextMessage = "Hey Alice, it means that our communication is encrypted and authenticated.";
		sentMessage = usBob.transformSending(bobSessionID, clearTextMessage);

		// Receive encrypted message.
		receivedMessage = usAlice.transformReceiving(aliceSessionID,
				sentMessage);

		if (!clearTextMessage.equals(receivedMessage))
			fail();

		// Send encrypted message.
		clearTextMessage = "Oh, is that all?";
		sentMessage = usAlice
				.transformSending(aliceSessionID, clearTextMessage);

		// Receive encrypted message.
		receivedMessage = usBob.transformReceiving(bobSessionID, sentMessage);
		if (!clearTextMessage.equals(receivedMessage))
			fail();

		// Send encrypted message.
		clearTextMessage = "Actually no, our communication has the properties of perfect forward secrecy and deniable authentication.";
		sentMessage = usBob.transformSending(bobSessionID, clearTextMessage);

		// Receive encrypted message.
		receivedMessage = usAlice.transformReceiving(aliceSessionID,
				sentMessage);

		if (!clearTextMessage.equals(receivedMessage))
			fail();

		// Send encrypted message. Test UTF-8 space characters.
		clearTextMessage = "Oh really?! pouvons-nous parler en français?";

		sentMessage = usAlice
				.transformSending(aliceSessionID, clearTextMessage);

		// Receive encrypted message.
		receivedMessage = usBob.transformReceiving(bobSessionID, sentMessage);
		if (!clearTextMessage.equals(receivedMessage))
			fail();
	}

	private void endSession() {
		usBob.endSession(bobSessionID);
		usAlice.endSession(aliceSessionID);

		if (usBob.getSessionStatus(bobSessionID) != SessionStatus.PLAINTEXT
				|| usAlice.getSessionStatus(aliceSessionID) != SessionStatus.PLAINTEXT)
			fail("Failed to end session.");
	}
}
