package at.tugraz.sw.hoi.messenger.remote;

import android.os.AsyncTask;
import android.util.Log;

public class CheckContactTask extends AsyncTask<Void, Void, Boolean> {

  private String email;

  private String TAG = "checkContact";
  private static final int MAX_ATTEMPTS = 5;

  private boolean isFinished = false;
  private boolean contactExists = false;

  public CheckContactTask(String email) {
    super();
    this.email = email;
  }

  protected Boolean doInBackground(Void... params) {
    for (int i = 1; i <= MAX_ATTEMPTS; i++) {
      Log.d(TAG, "Attempt #" + i + " to register");
      ServletResponse response = ServletUtil.checkContact(email);

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

}
