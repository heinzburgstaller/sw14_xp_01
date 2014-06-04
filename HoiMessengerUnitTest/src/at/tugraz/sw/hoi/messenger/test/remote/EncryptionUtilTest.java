package at.tugraz.sw.hoi.messenger.test.remote;

import java.security.KeyPair;

import at.tugraz.sw.hoi.messenger.remote.EncryptionUtil;
import junit.framework.Assert;
import junit.framework.TestCase;

public class EncryptionUtilTest extends TestCase {

	public void testDeEncryption() {
		KeyPair keyPair = EncryptionUtil.generateRsaKeyPair();
		String text = "Hallo Welt!";
		String textEncrypted = null;
		String textDecrypted = null;

		try {
			textEncrypted = EncryptionUtil.encrypt(text, keyPair.getPrivate());
			textDecrypted = EncryptionUtil.decrypt(textEncrypted,
					keyPair.getPublic());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		Assert.assertEquals(text, textDecrypted);
	}

}
