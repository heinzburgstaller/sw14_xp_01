package at.tugraz.sw.hoi.messenger.remote;

import java.util.concurrent.ExecutionException;

import android.os.AsyncTask;
import android.util.Log;

public class OtrHandshakeTask extends AsyncTask<Void, Void, Boolean> {

  private String fromEmail;
  private String toEmail;
  private String lastMessage;
  private String state;

  private String TAG = "OtrHandshake";
  private static final int MAX_ATTEMPTS = 5;

  private boolean isFinished = false;
  private boolean contactExists = false;

  public OtrHandshakeTask(String fromEmail, String toEmail, String lastMessage, String state) {
    super();
    this.fromEmail = fromEmail;
    this.toEmail = toEmail;
    this.lastMessage = lastMessage;
    this.state = state;

  }

  protected Boolean doInBackground(Void... params) {
    for (int i = 1; i <= MAX_ATTEMPTS; i++) {
      Log.d(TAG, "Attempt #" + i + " to register");
      ServletResponse response = ServletUtil.startHandshake(fromEmail, toEmail, lastMessage, state);
      if (response.getStatus().equals(ServletResponse.Status.SUCCESS)) {
        return Boolean.TRUE;
      } else {
        continue;
      }
    }
    return Boolean.FALSE;
  }

  protected void onPostExecute(Boolean status) {
    this.isFinished = true;
    this.contactExists = status;
  }

  public Boolean getResult() {
    try {
      return this.get();
    } catch (InterruptedException e) {
      Log.w(TAG, e.toString());
      e.printStackTrace();
    } catch (ExecutionException e) {
      Log.w(TAG, e.toString());
      e.printStackTrace();
    }
    return Boolean.FALSE;

  }
}
