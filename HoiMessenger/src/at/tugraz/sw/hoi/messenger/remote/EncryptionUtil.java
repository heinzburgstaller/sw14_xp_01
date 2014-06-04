package at.tugraz.sw.hoi.messenger.remote;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class EncryptionUtil {
  private Context ctx;
  private SharedPreferences prefs;

  public EncryptionUtil(Context applicationContext) {
    ctx = applicationContext;
    prefs = PreferenceManager.getDefaultSharedPreferences(ctx);

    if (prefs.getString(Configuration.PRIVATE_KEY, "").equals("")
        || prefs.getString(Configuration.PUBLIC_KEY, "").equals("")) {

    }
  }

  @SuppressLint("TrulyRandom")
  public static String encrypt(String input, Key key) throws Exception {
    byte[] inArr = input.getBytes("UTF-8");
    Cipher c = Cipher.getInstance("RSA");
    c.init(Cipher.ENCRYPT_MODE, key);
    byte[] decodedBytes = c.doFinal(inArr);
    String output = new String(decodedBytes, "UTF-8");
    return output;
  }

  public static String decrypt(String input, Key key) throws Exception {
    byte[] inArr = input.getBytes("UTF-8");
    Cipher c = Cipher.getInstance("RSA");
    c.init(Cipher.DECRYPT_MODE, key);
    byte[] decodedBytes = c.doFinal(inArr);
    return new String(decodedBytes, "UTF-8");
  }

  public static KeyPair generateRsaKeyPair() {
    KeyPairGenerator kpg = null;
    try {
      kpg = KeyPairGenerator.getInstance("RSA");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    kpg.initialize(1024);
    KeyPair kp = kpg.genKeyPair();
    return kp;
  }
}
