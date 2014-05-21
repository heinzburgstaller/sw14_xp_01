package at.tugraz.sw.hoi.messenger;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import at.tugraz.sw.hoi.messenger.remote.Configuration;

public class EditEmailDialog extends DialogFragment {
  private AlertDialog alertDialog;
  private EditText et;
  private SharedPreferences prefs;
  public String oldemail_;
  public String newemail_;

  public static EditEmailDialog newInstance() {
    EditEmailDialog fragment = new EditEmailDialog();
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    prefs = PreferenceManager.getDefaultSharedPreferences(this.getActivity().getApplicationContext());
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    et = new EditText(getActivity());
    et.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
    et.setHint((prefs.getString(Configuration.CHAT_EMAIL_ID, "")));
    alertDialog = new AlertDialog.Builder(getActivity()).setTitle("Edit Email").setMessage("Edit Email")
        .setPositiveButton(android.R.string.ok, null).setNegativeButton(android.R.string.cancel, null).setView(et)
        .create();
    alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
      @Override
      public void onShow(DialogInterface dialog) {
        Button okBtn = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        okBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

            String email = et.getText().toString();

            if (!isEmailValid(email)) {
              et.setError("Invalid email!");
              return;
            }
            if (email != prefs.getString(Configuration.CHAT_EMAIL_ID, "")) {
              newemail_ = email;
              ((MainActivity) getActivity()).reRegisterUser(email);
              oldemail_ = prefs.getString(Configuration.CHAT_EMAIL_ID, "");

            }

            alertDialog.dismiss();
          }
        });
      }
    });
    return alertDialog;
  }

  private boolean isEmailValid(CharSequence email) {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
  }
}