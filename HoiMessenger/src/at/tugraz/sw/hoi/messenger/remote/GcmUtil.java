package at.tugraz.sw.hoi.messenger.remote;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Patterns;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmUtil {

  private static final String TAG = "GcmUtil";

  private static final int MAX_ATTEMPTS = 5;
  private static final int BACKOFF_MILLI_SECONDS = 2000;
  private static final Random random = new Random();

  private Context ctx;
  private SharedPreferences prefs;
  private GoogleCloudMessaging gcm;
  private AsyncTask<Void, Void, Boolean> registrationTask;
  private AsyncTask<Void, Void, Boolean> unRegisterTask;
  private CountDownLatch signal = null;
  private String new_mail = "";

  public CountDownLatch getSignal() {
    return signal;
  }

  public GcmUtil(Context applicationContext) {
    super();
    init(applicationContext);
  }

  private void init(Context applicationContext) {
    ctx = applicationContext;
    prefs = PreferenceManager.getDefaultSharedPreferences(ctx);

    String regid = getRegistrationId();
    if (regid.length() == 0) {
      registerBackground();
    } else {
      broadcastStatus(true);
    }
    gcm = GoogleCloudMessaging.getInstance(ctx);
  }

  public void reRegister(Context applicationContext, String newEmail) {
    new_mail = newEmail;
    reRegisterBackground();
  }

  public GcmUtil(Context applicationContext, CountDownLatch signal) {
    this.signal = signal;
    init(applicationContext);
  }

  /**
   * Gets the current registration id for application on GCM service.
   * <p>
   * If result is empty, the registration has failed.
   * 
   * @return registration id, or empty string if the registration is not
   *         complete.
   */
  public String getRegistrationId() {
    String registrationId = prefs.getString(Configuration.PROPERTY_REG_ID, "");
    if (registrationId.length() == 0) {
      Log.v(TAG, "Registration not found.");
      return "";
    }

    return registrationId;
  }

  /**
   * Stores the registration id, app versionCode, and expiration time in the
   * application's {@code SharedPreferences}.
   * 
   * @param regId
   *          registration id
   */
  private void setRegistrationId(String regId) {

    // Log.v(TAG, "Saving regId on app version " + appVersion);
    SharedPreferences.Editor editor = prefs.edit();
    editor.putString(Configuration.PROPERTY_REG_ID, regId);

    editor.commit();
  }

  /**
   * Unregistering
   * 
   * @param newEmail
   */
  private void reRegisterBackground() {
    unRegisterTask = new AsyncTask<Void, Void, Boolean>() {
      @Override
      protected Boolean doInBackground(Void... params) {
        long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
        for (int i = 1; i <= MAX_ATTEMPTS; i++) {
          // Log.d(TAG, "Attempt #" + i + " to register");
          try {

            ServletResponse responseu = ServletUtil.unregister(prefs.getString(Configuration.CHAT_EMAIL_ID, ""));

            if (responseu.getStatus() == ServletResponse.Status.FAILURE) {
              continue;
            }

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(Configuration.REG_ID, "");
            editor.putString(Configuration.CHAT_EMAIL_ID, new_mail);
            editor.commit();

            if (gcm == null) {
              gcm = GoogleCloudMessaging.getInstance(ctx);
            }
            String regid = gcm.register(Configuration.SENDER_ID);

            // You should send the registration ID to your server over HTTP,
            // so it can use GCM/HTTP or CCS to send messages to your app.
            ServletResponse response = ServletUtil.register(getPreferredEmail(), regid);

            if (response.getStatus() == ServletResponse.Status.FAILURE) {
              continue;
            }

            // Save the regid - no need to register again.
            setRegistrationId(regid);
            return Boolean.TRUE;

          } catch (IOException ex) {
            // Log.e(TAG, "Failed to register on attempt " + i + ":" + ex);
            if (i == MAX_ATTEMPTS) {
              break;
            }
            try {
              // Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
              Thread.sleep(backoff);
            } catch (InterruptedException e1) {
              // Activity finished before we complete - exit.
              // Log.d(TAG, "Thread interrupted: abort remaining retries!");
              Thread.currentThread().interrupt();
            }
            // increase backoff exponentially
            backoff *= 2;
          }
        }
        return Boolean.FALSE;
      }

      @Override
      protected void onPostExecute(Boolean status) {
        broadcastStatus(status);
        if (signal != null) {
          signal.countDown();
        }
      }
    }.execute(null, null, null);
  }

  /**
   * Registers the application with GCM servers asynchronously.
   * <p>
   * Stores the registration id, app versionCode, and expiration time in the
   * application's shared preferences.
   */
  private void registerBackground() {
    registrationTask = new AsyncTask<Void, Void, Boolean>() {
      @Override
      protected Boolean doInBackground(Void... params) {
        long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
        for (int i = 1; i <= MAX_ATTEMPTS; i++) {
          // Log.d(TAG, "Attempt #" + i + " to register");
          try {
            if (gcm == null) {
              gcm = GoogleCloudMessaging.getInstance(ctx);
            }
            String regid = gcm.register(Configuration.SENDER_ID);

            // You should send the registration ID to your server over HTTP,
            // so it can use GCM/HTTP or CCS to send messages to your app.
            ServletResponse response = ServletUtil.register(getPreferredEmail(), regid);

            if (response.getStatus() == ServletResponse.Status.FAILURE) {
              return Boolean.FALSE;
            }

            if (!getPreferredEmail().equals(prefs.getString(Configuration.CHAT_EMAIL_ID, ""))) {
              SharedPreferences.Editor editor = prefs.edit();
              editor.putString(Configuration.CHAT_EMAIL_ID, getPreferredEmail());

              editor.commit();
            }

            // Save the regid - no need to register again.
            setRegistrationId(regid);
            return Boolean.TRUE;

          } catch (IOException ex) {
            // Log.e(TAG, "Failed to register on attempt " + i + ":" + ex);
            if (i == MAX_ATTEMPTS) {
              break;
            }
            try {
              // Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
              Thread.sleep(backoff);
            } catch (InterruptedException e1) {
              // Activity finished before we complete - exit.
              // Log.d(TAG, "Thread interrupted: abort remaining retries!");
              Thread.currentThread().interrupt();
            }
            // increase backoff exponentially
            backoff *= 2;
          }
        }
        return Boolean.FALSE;
      }

      @Override
      protected void onPostExecute(Boolean status) {
        broadcastStatus(status);
        if (signal != null) {
          signal.countDown();
        }
      }
    }.execute(null, null, null);
  }

  private void broadcastStatus(boolean status) {
    Intent intent = new Intent(Configuration.ACTION_REGISTER);
    intent.putExtra(Configuration.EXTRA_STATUS, status ? Configuration.STATUS_SUCCESS : Configuration.STATUS_FAILED);
    ctx.sendBroadcast(intent);
  }

  public void cleanup() {
    if (registrationTask != null) {
      registrationTask.cancel(true);
    }
    if (unRegisterTask != null) {
      unRegisterTask.cancel(true);
    }
    if (gcm != null) {
      gcm.close();
    }
  }

  public String getPreferredEmail() {

    List<String> emailList = new ArrayList<String>();
    Account[] accounts = AccountManager.get(ctx).getAccounts();
    for (Account account : accounts) {
      if (Patterns.EMAIL_ADDRESS.matcher(account.name).matches()) {
        emailList.add(account.name);
      }
    }

    return prefs.getString(Configuration.CHAT_EMAIL_ID, emailList.size() == 0 ? "" : emailList.get(0));

  }

}
