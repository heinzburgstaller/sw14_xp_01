package at.tugraz.sw.hoi.messenger;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import at.tugraz.sw.hoi.messenger.remote.Configuration;
import at.tugraz.sw.hoi.messenger.util.DataProvider;

public class AddContactDialog extends DialogFragment {
  private AlertDialog alertDialog;
  private EditText et;
  private String email;
  private EditText etname;

  public static AddContactDialog newInstance() {
    AddContactDialog fragment = new AddContactDialog();
    fragment.email = "";
    return fragment;
  }

  public static AddContactDialog newInstance(String email) {
    AddContactDialog fragment = new AddContactDialog();
    fragment.email = email;
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    et = new EditText(getActivity());
    et.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

    if (email.equals("")) {
      et.setHint("abc@example.com");
    } else {
      et.setText(email);
    }
    alertDialog = new AlertDialog.Builder(getActivity()).setTitle("Add Contact").setMessage("Add Contact")
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
            try {
              ContentValues values = new ContentValues(2);
              values.put(DataProvider.COL_NAME, email.substring(0, email.indexOf('@')));
              values.put(DataProvider.COL_EMAIL, email);
              getActivity().getContentResolver().insert(DataProvider.CONTENT_URI_PROFILE, values);

              if (!email.equals("")) {
                Cursor c = getActivity().getContentResolver().query(DataProvider.CONTENT_URI_PROFILE,
                    new String[] { DataProvider.COL_ID, DataProvider.COL_NAME }, DataProvider.COL_EMAIL + "=?",
                    new String[] { "" + email }, DataProvider.COL_ID);
                c.moveToFirst();
                String id = String.valueOf(c.getInt(c.getColumnIndex(DataProvider.COL_ID)));
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra(Configuration.PROFILE_ID, id);
                startActivity(intent);
              }
            } catch (SQLException sqle) {
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