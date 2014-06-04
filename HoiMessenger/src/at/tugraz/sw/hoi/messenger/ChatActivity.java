package at.tugraz.sw.hoi.messenger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import at.tugraz.sw.hoi.messenger.remote.Configuration;
import at.tugraz.sw.hoi.messenger.remote.GcmUtil;
import at.tugraz.sw.hoi.messenger.remote.ServletResponse;
import at.tugraz.sw.hoi.messenger.remote.ServletUtil;
import at.tugraz.sw.hoi.messenger.util.DataProvider;
import at.tugraz.sw.hoi.messenger.util.DataProvider.MessageType;

public class ChatActivity extends ActionBarActivity implements MessagesFragment.OnFragmentInteractionListener,
    EditContactDialog.OnFragmentInteractionListener, OnClickListener {

  private SharedPreferences prefs;
  private EditText msgEdit;
  private ImageButton sendBtn;
  private String profileId;
  private String profileName;
  private String profileEmail;
  private GcmUtil gcmUtil;

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.chat_activity);
    prefs = PreferenceManager.getDefaultSharedPreferences(this);
    profileId = getIntent().getStringExtra(Configuration.PROFILE_ID);
    Log.d("DEBUG", "profilid  " + profileId);
    msgEdit = (EditText) findViewById(R.id.etText);
    sendBtn = (ImageButton) findViewById(R.id.btSend);
    sendBtn.setOnClickListener(this);

    ActionBar actionBar = getSupportActionBar();
    actionBar.setHomeButtonEnabled(true);
    actionBar.setDisplayHomeAsUpEnabled(true);
    Cursor c = getContentResolver().query(Uri.withAppendedPath(DataProvider.CONTENT_URI_PROFILE, profileId), null,
        null, null, null);
    if (c.moveToFirst()) {
      profileName = c.getString(c.getColumnIndex(DataProvider.COL_NAME));
      profileEmail = c.getString(c.getColumnIndex(DataProvider.COL_EMAIL));
      actionBar.setTitle(profileName);
    }
    actionBar.setSubtitle("connecting ...");

    registerReceiver(registrationStatusReceiver, new IntentFilter(Configuration.ACTION_REGISTER));
    gcmUtil = new GcmUtil(getApplicationContext());
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    // getMenuInflater().inflate(R.menu.chat, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case android.R.id.home:
      Intent intent = new Intent(this, MainActivity.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      startActivity(intent);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onBackPressed() {
    Intent intent = new Intent(this, MainActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(intent);
  }

  @Override
  public void onEditContact(String name) {
    getSupportActionBar().setTitle(name);
  }

  @Override
  public String getProfileEmail() {
    return profileEmail;
  }

  private void send(final String txt) {
    new AsyncTask<Void, Void, String>() {
      @Override
      protected String doInBackground(Void... params) {
        String msg = "";

        String senderEmail = prefs.getString(Configuration.CHAT_EMAIL_ID, "");
        ServletResponse response = ServletUtil.chat(txt, senderEmail, profileEmail);

        if (ServletResponse.Status.SUCCESS.equals(response.getStatus())) {
          ContentValues values = new ContentValues(2);
          values.put(DataProvider.COL_TYPE, MessageType.OUTGOING.ordinal());
          values.put(DataProvider.COL_MESSAGE, txt);
          values.put(DataProvider.COL_RECEIVER_EMAIL, profileEmail);
          values.put(DataProvider.COL_SENDER_EMAIL, senderEmail);
          Calendar calDt = Calendar.getInstance(TimeZone.getDefault());
          calDt.setTime(new Date());
          values.put(DataProvider.COL_TIME, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calDt.getTime()));
          getContentResolver().insert(DataProvider.CONTENT_URI_MESSAGES, values);
        } else {
          Log.d("ServletResponse", response.getMessage());
        }

        return msg;
      }

      @Override
      protected void onPostExecute(String msg) {
        if (!TextUtils.isEmpty(msg)) {
          Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        }
      }
    }.execute(null, null, null);
  }

  @Override
  protected void onPause() {
    ContentValues values = new ContentValues(1);
    values.put(DataProvider.COL_COUNT, 0);
    getContentResolver().update(Uri.withAppendedPath(DataProvider.CONTENT_URI_PROFILE, profileId), values, null, null);
    super.onPause();
  }

  @Override
  protected void onDestroy() {
    unregisterReceiver(registrationStatusReceiver);
    gcmUtil.cleanup();
    super.onDestroy();
  }

  private BroadcastReceiver registrationStatusReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      if (intent != null && Configuration.ACTION_REGISTER.equals(intent.getAction())) {
        switch (intent.getIntExtra(Configuration.EXTRA_STATUS, 100)) {
        case Configuration.STATUS_SUCCESS:
          getSupportActionBar().setSubtitle("online");
          sendBtn.setEnabled(true);
          break;

        case Configuration.STATUS_FAILED:
          getSupportActionBar().setSubtitle("offline");
          break;
        }
      }
    }
  };

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
    case R.id.btSend:
      String text = msgEdit.getText().toString();
      if (text != null && text.trim().length() > 0) {
        send(msgEdit.getText().toString());
        msgEdit.setText("");
      }
      break;
    }
  }

}