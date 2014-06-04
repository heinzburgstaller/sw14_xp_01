package at.tugraz.sw.hoi.messenger;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import at.tugraz.sw.hoi.messenger.remote.Configuration;
import at.tugraz.sw.hoi.messenger.util.DataProvider;

public class AddContactDialog extends DialogFragment implements OnClickListener {
  private EditText et_name;
  private EditText et_email;
  private String email;
  private boolean updateContact;

  public static AddContactDialog newInstance() {
    AddContactDialog fragment = new AddContactDialog();
    fragment.updateContact = false;
    fragment.email = "";
    return fragment;
  }

  public static AddContactDialog newInstance(boolean update, String email) {
    AddContactDialog fragment = new AddContactDialog();
    fragment.updateContact = update;
    fragment.email = email;
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.dialog_contact, container);

    this.et_name = (EditText) view.findViewById(R.id.et_name);
    this.et_email = (EditText) view.findViewById(R.id.et_email);
    if (email.equals("")) {
      et_email.setHint("abc@example.com");
      et_name.setHint("<Name>");
    } else {
      et_email.setFocusable(false);
      et_email.setText(email);
      String name = "";
      if (updateContact) {
        Cursor c = getActivity().getContentResolver().query(DataProvider.CONTENT_URI_PROFILE,
            new String[] { DataProvider.COL_ID, DataProvider.COL_NAME }, DataProvider.COL_EMAIL + "=?",
            new String[] { "" + email }, DataProvider.COL_ID);
        c.moveToFirst();
        name = c.getString(c.getColumnIndex(DataProvider.COL_NAME));
      } else {
        name = email.substring(0, email.indexOf('@'));
      }
      et_name.setHint(name);
    }

    getDialog().setTitle("Add Contact");
    view.findViewById(R.id.ok).setOnClickListener(this);
    view.findViewById(R.id.cancel).setOnClickListener(this);

    return view;
  }

  private boolean isEmailValid(CharSequence email) {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
    case R.id.ok:
      String input_email = et_email.getText().toString();
      if (!isEmailValid(input_email)) {
        et_email.setError("Invalid email!");
        return;
      }
      try {
        String name = et_name.getText().toString();

        if (updateContact) {
          Cursor c = getActivity().getContentResolver().query(DataProvider.CONTENT_URI_PROFILE,
              new String[] { DataProvider.COL_ID, DataProvider.COL_NAME }, DataProvider.COL_EMAIL + "=?",
              new String[] { "" + email }, DataProvider.COL_ID);
          c.moveToFirst();
          String id = String.valueOf(c.getInt(c.getColumnIndex(DataProvider.COL_ID)));

          ContentValues values = new ContentValues(1);
          values.put(DataProvider.COL_NAME, (name.equals("")) ? input_email.substring(0, input_email.indexOf('@'))
              : name);
          getActivity().getContentResolver().update(Uri.withAppendedPath(DataProvider.CONTENT_URI_PROFILE, id), values,
              null, null);
        } else {
          ContentValues values = new ContentValues(2);
          values.put(DataProvider.COL_NAME, (name.equals("")) ? input_email.substring(0, input_email.indexOf('@'))
              : name);
          values.put(DataProvider.COL_EMAIL, input_email);

          getActivity().getContentResolver().insert(DataProvider.CONTENT_URI_PROFILE, values);
          Cursor c = getActivity().getContentResolver().query(DataProvider.CONTENT_URI_PROFILE,
              new String[] { DataProvider.COL_ID, DataProvider.COL_NAME }, DataProvider.COL_EMAIL + "=?",
              new String[] { "" + input_email }, DataProvider.COL_ID);
          c.moveToFirst();
          String id = String.valueOf(c.getInt(c.getColumnIndex(DataProvider.COL_ID)));
          Intent intent = new Intent(getActivity(), ChatActivity.class);
          intent.putExtra(Configuration.PROFILE_ID, id);
          startActivity(intent);
        }
      } catch (SQLException sqle) {
      }

      break;

    default:
      break;
    }

    this.dismiss();
  }

}