package at.tugraz.sw.hoi.messenger.remote;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import at.tugraz.sw.hoi.messenger.MainActivity;
import at.tugraz.sw.hoi.messenger.R;
//import at.tugraz.sw14_xp_01.Common;
//import at.tugraz.sw14_xp_01.DataProvider;
//import at.tugraz.sw14_xp_01.DataProvider.MessageType;
//import at.tugraz.sw14_xp_01.MainActivity;
//import at.tugraz.sw14_xp_01.R;
import at.tugraz.sw.hoi.messenger.util.DataProvider;
import at.tugraz.sw.hoi.messenger.util.DataProvider.MessageType;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmBroadcastReceiver extends BroadcastReceiver {

  private static final String TAG = "GcmBroadcastReceiver";
  private Context ctx;
  private SharedPreferences prefs;

  public GcmBroadcastReceiver() {
    super();
    prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    ctx = context;
    PowerManager mPowerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
    WakeLock mWakeLock = mPowerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
    mWakeLock.acquire();
    try {
      GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
      String messageType = gcm.getMessageType(intent);
      if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
        sendNotification(Configuration.MESSAGE_SEND_ERROR, false);
      } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
        sendNotification(Configuration.MESSAGE_DELETED_ERROR, false);
      } else {
        String msg = intent.getStringExtra(DataProvider.COL_MESSAGE);
        String senderEmail = intent.getStringExtra(DataProvider.COL_SENDER_EMAIL);
        String receiverEmail = intent.getStringExtra(DataProvider.COL_RECEIVER_EMAIL);
        ContentValues values = new ContentValues(2);
        values.put(DataProvider.COL_TYPE, MessageType.INCOMING.ordinal());
        values.put(DataProvider.COL_MESSAGE, msg);
        values.put(DataProvider.COL_SENDER_EMAIL, senderEmail);
        values.put(DataProvider.COL_RECEIVER_EMAIL, receiverEmail);
        context.getContentResolver().insert(DataProvider.CONTENT_URI_MESSAGES, values);

        if (prefs.getBoolean(Configuration.PROPERTY_NEW_NOTIFICATION, true)) {
          sendNotification(Configuration.MESSAGE_NEW, true);
        }
      }
      setResultCode(Activity.RESULT_OK);
    } finally {
      mWakeLock.release();
    }
  }

  private void sendNotification(String text, boolean launchApp) {
    NotificationManager mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
    NotificationCompat.Builder notification = new NotificationCompat.Builder(ctx);
    notification.setContentTitle(ctx.getString(R.string.app_name));
    notification.setContentText(text);
    notification.setAutoCancel(true);
    notification.setSmallIcon(R.drawable.ic_launcher);

    String ringtone = prefs.getString(Configuration.PROPERTY_NEW_NOTIFICATION_RINGTONE,
        android.provider.Settings.System.DEFAULT_NOTIFICATION_URI.toString());
    if (!TextUtils.isEmpty(ringtone)) {
      notification.setSound(Uri.parse(ringtone));
    }

    if (launchApp) {
      Intent intent = new Intent(ctx, MainActivity.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
      PendingIntent pi = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
      notification.setContentIntent(pi);
    }

    mNotificationManager.notify(1, notification.build());
  }

}