package at.tugraz.sw.hoi.messenger.remote;

import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;

public class EncryptionUtil {
  private Context ctx;
  private SharedPreferences prefs;
  private Key privateKey;
  private Key publicKey;

  public EncryptionUtil(Context applicationContext) {
    ctx = applicationContext;
    prefs = PreferenceManager.getDefaultSharedPreferences(ctx);

    if (prefs.getString(Configuration.PRIVATE_KEY, "").equals("")
        || prefs.getString(Configuration.PUBLIC_KEY, "").equals("")) {

    }
  }

  @SuppressLint("TrulyRandom")
  public static String encrypt(String input, String b64PubKey) throws Exception {
    byte[] inArr = input.getBytes();
    byte[] pubKey = Base64.decode(b64PubKey, Base64.DEFAULT);
    Key publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(pubKey));
    Cipher c = Cipher.getInstance("RSA");
    c.init(Cipher.ENCRYPT_MODE, publicKey);
    byte[] decodedBytes = c.doFinal(inArr);
    String output = new String(decodedBytes, "UTF8");

    return output;

  }

  public static String decrypt(String input, String b64key) throws Exception {
    byte[] inArr = Base64.decode(input, Base64.DEFAULT);
    byte[] pubKey = Base64.decode(b64key, Base64.DEFAULT);
    Key publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(pubKey));
    Cipher c = Cipher.getInstance("RSA");
    c.init(Cipher.DECRYPT_MODE, publicKey);
    byte[] decodedBytes = c.doFinal(inArr);
    String output = new String(decodedBytes, "UTF8");

    return output;
  }
}
