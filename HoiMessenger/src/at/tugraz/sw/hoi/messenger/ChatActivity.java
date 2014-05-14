package at.tugraz.sw.hoi.messenger;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import at.tugraz.sw.hoi.messenger.util.DataProvider;
import at.tugraz.sw.hoi.messenger.util.DataProvider.MessageType;
import at.tugraz.sw.hoi.messenger.util.GcmUtil;
import at.tugraz.sw.hoi.messenger.util.Util;

public class ChatActivity extends ActionBarActivity implements MessagesFragment.OnFragmentInteractionListener,
    EditContactDialog.OnFragmentInteractionListener {

  private EditText msgEdit;
  private ImageButton sendBtn;
  private String profileId;
  private String profileName;
  private String profileEmail;
  private GcmUtil gcmUtil;

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.chat_activity);

    profileId = getIntent().getStringExtra(Util.PROFILE_ID);
    Log.d("DEBUG", "profilid  " + profileId);
    msgEdit = (EditText) findViewById(R.id.etText);
    sendBtn = (ImageButton) findViewById(R.id.btSendMessage);
    Log.d("DEBUG", "++++++++++++++0.5++++++++++");

    // sendBtn.setOnClickListener(new OnClickListener() {
    //
    // @Override
    // public void onClick(View v) {
    // switch (v.getId()) {
    // case R.id.btSendMessage:
    // send(msgEdit.getText().toString());
    // msgEdit.setText(null);
    // break;
    // }
    // }
    // });

    Log.d("DEBUG", "++++++++++++++1++++++++++");
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

    registerReceiver(registrationStatusReceiver, new IntentFilter(Util.ACTION_REGISTER));
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
    /*
     * case R.id.action_edit: EditContactDialog dialog = new
     * EditContactDialog(); Bundle args = new Bundle();
     * args.putString(Common.PROFILE_ID, profileId);
     * args.putString(DataProvider.COL_NAME, profileName);
     * dialog.setArguments(args); dialog.show(getSupportFragmentManager(),
     * "EditContactDialog"); return true;
     */
    case android.R.id.home:
      Intent intent = new Intent(this, MainActivity.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      startActivity(intent);
      return true;
    }
    return super.onOptionsItemSelected(item);
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

        // ServerUtilities.send(txt, profileEmail); //TODO replace send with
        // post mb
        ContentValues values = new ContentValues(2);
        values.put(DataProvider.COL_TYPE, MessageType.OUTGOING.ordinal());
        values.put(DataProvider.COL_MESSAGE, txt);
        values.put(DataProvider.COL_RECEIVER_EMAIL, profileEmail);
        values.put(DataProvider.COL_SENDER_EMAIL, Util.getPreferredEmail());
        getContentResolver().insert(DataProvider.CONTENT_URI_MESSAGES, values);

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
      if (intent != null && Util.ACTION_REGISTER.equals(intent.getAction())) {
        switch (intent.getIntExtra(Util.EXTRA_STATUS, 100)) {
        case Util.STATUS_SUCCESS:
          getSupportActionBar().setSubtitle("online");
          sendBtn.setEnabled(true);
          break;

        case Util.STATUS_FAILED:
          getSupportActionBar().setSubtitle("offline");
          break;
        }
      }
    }
  };

}