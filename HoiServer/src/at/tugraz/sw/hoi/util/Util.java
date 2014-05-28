package at.tugraz.sw.hoi.util;

import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

public class Util {

	public static boolean isEmpty(String s) {
		if (s == null)
			return true;

		if (s.trim().length() == 0)
			return true;

		return false;
	}

	public static String encrypt(String input, String b64PubKey) throws Exception {
		byte[] inArr = input.getBytes();
		byte[] pubKey = Base64.decodeBase64(b64PubKey);
		Key publicKey = KeyFactory.getInstance("RSA").generatePublic(
				new X509EncodedKeySpec(pubKey));
		Cipher c = Cipher.getInstance("RSA");
		c.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] decodedBytes = c.doFinal(inArr);
		String output = new String(decodedBytes,"UTF8");
		
		return output;
		
	}

	public static String decrypt(String input, String b64key) throws Exception {
		byte[] inArr = Base64.decodeBase64(input);
		byte[] pubKey = Base64.decodeBase64(b64key);
		Key publicKey = KeyFactory.getInstance("RSA").generatePublic(
				new X509EncodedKeySpec(pubKey));
		Cipher c = Cipher.getInstance("RSA");
		c.init(Cipher.DECRYPT_MODE, publicKey);
		byte[] decodedBytes = c.doFinal(inArr);
		String output = new String(decodedBytes,"UTF8");
		
		return output;
	}

}
