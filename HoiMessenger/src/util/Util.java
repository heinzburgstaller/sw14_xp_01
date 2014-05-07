package util;

import java.util.ArrayList;
import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Patterns;

public class Util extends Application {

  public static SharedPreferences prefs;
  public static String[] email_arr;
  public static final String ACTION_REGISTER = "at.tugraz.sw14_xp_01.REGISTER";
  public static final int STATUS_SUCCESS = 1;
  public static final int STATUS_FAILED = 0;
  public static final String EXTRA_STATUS = "status";
  public static final String CHAT_EMAIL_ID = "chat_email_id";
  public static final String CHAT_SENDER_ID = "sender_id_pref";

  @Override
  public void onCreate() {
    super.onCreate();
    prefs = PreferenceManager.getDefaultSharedPreferences(this);
    email_arr = getEmailList();
  }

  public static String getRingtone() {
    return prefs.getString("notifications_new_message_ringtone",
        android.provider.Settings.System.DEFAULT_NOTIFICATION_URI.toString());
  }

  public String[] getEmailList() {
    List<String> lst = new ArrayList<String>();
    Account[] accounts = AccountManager.get(this).getAccounts();
    for (Account account : accounts) {
      if (Patterns.EMAIL_ADDRESS.matcher(account.name).matches()) {
        lst.add(account.name);
      }
    }
    return lst.toArray(new String[lst.size()]);

  }

  public static String getPreferredEmail() {
    return prefs.getString(CHAT_EMAIL_ID, email_arr.length == 0 ? "" : email_arr[0]);
  }

  public static String getSenderId() {
    return prefs.getString(CHAT_SENDER_ID, Constants.SENDER_ID);
  }

  public static String getServerUrl() {
    return prefs.getString("server_url_pref", Constants.SERVER_URL);
  }

}