package at.tugraz.sw.hoi.messenger.remote;

import java.security.Key;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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
}
