package at.tugraz.sw.hoi.messenger.test.otr;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import net.java.otr4j.OtrKeyManager;
import net.java.otr4j.OtrKeyManagerStore;
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
import at.tugraz.sw.hoi.messenger.otr.HoiOtrKeyManager;
import at.tugraz.sw.hoi.messenger.otr.HoiOtrKeyManagerStore;

public class OtrKeyManagerTest extends AndroidTestCase {

	private Context context;
	private SharedPreferences prefs;
	private static final String ALICE = "alice@differentmail.com";
	private static final String BOB = "bob@anotherthing.com";
	private static final String TEST_DOMAIN = "Lorem ipsum dolor sit amet";

	protected void setUp() throws Exception {
		super.setUp();
		context = getContext();
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		// Editor editor = prefs.edit();
		//
		// // editor.putString(Configuration.PROPERTY_REG_ID, "");
		// editor.commit();
	}

	public void testKeyGenerator() {
		OtrKeyManager km = new HoiOtrKeyManager(prefs);
		SessionID session = new SessionID(ALICE, BOB, TEST_DOMAIN);

		assertNull(km.loadLocalKeyPair(session));

		km.generateLocalKeyPair(session);

		assertNotNull(km.loadLocalKeyPair(session));

		assertFalse(km.isVerified(session));

		km.verify(session);

		assertTrue(km.isVerified(session));

	}

	public void testLoadKey() throws NoSuchAlgorithmException {
		SessionID session = new SessionID(ALICE, BOB, TEST_DOMAIN);
		HoiOtrKeyManagerStore store = new HoiOtrKeyManagerStore(prefs);
		KeyPairGenerator kg;

		kg = KeyPairGenerator.getInstance("DSA");

		KeyPair kp = kg.genKeyPair();
		PublicKey pubKey = kp.getPublic();
	    X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(pubKey.getEncoded());

	    store.setProperty(session.getAccountID() + ".publicKey", x509EncodedKeySpec.getEncoded());
	    
	    PrivateKey privKey = kp.getPrivate();
	    PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privKey.getEncoded());

	    store.setProperty(session.getAccountID() + ".privateKey", pkcs8EncodedKeySpec.getEncoded());

		HoiOtrKeyManager km = new HoiOtrKeyManager(prefs);
		
		KeyPair loadedKeys = km.loadLocalKeyPair(session);
		assertNotNull(loadedKeys);
		assertTrue(kp.getPrivate().equals(loadedKeys.getPrivate()));
		assertTrue(kp.getPublic().equals(loadedKeys.getPublic()));
	}

}
