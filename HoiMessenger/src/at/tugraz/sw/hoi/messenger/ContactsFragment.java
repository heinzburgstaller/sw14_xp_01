package at.tugraz.sw.hoi.messenger;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import at.tugraz.sw.hoi.messenger.remote.Configuration;
import at.tugraz.sw.hoi.messenger.util.DataProvider;

public class ContactsFragment extends Fragment implements OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

  private ImageButton btAddContacts;
  private ContactCursorAdapter contactCursorAdapter;
  private static final String ARG_SECTION_NUMBER = "section_number";

  public static ContactsFragment newInstance(int sectionNumber) {
    ContactsFragment fragment = new ContactsFragment();
    Bundle args = new Bundle();
    args.putInt(ARG_SECTION_NUMBER, sectionNumber);
    fragment.setArguments(args);
    return fragment;
  }

  public ContactsFragment() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);

    ListView conversationList = (ListView) rootView.findViewById(R.id.lvContacts);
    // contactCursorAdapter = new
    // SimpleCursorAdapter(getActivity().getApplicationContext(),
    // R.layout.contact_list_item,
    // null, new String[] { DataProvider.COL_NAME, DataProvider.COL_EMAIL },
    // new int[] { R.id.tvName,
    // R.id.tvLastMessage }, 0);

    contactCursorAdapter = new ContactCursorAdapter(getActivity().getApplicationContext(), null);
    conversationList.setAdapter(contactCursorAdapter);

    // Prepare the loader. Either re-connect with an existing one,
    // or start a new one.
    getLoaderManager().initLoader(0, null, this);

    this.btAddContacts = (ImageButton) rootView.findViewById(R.id.btAddContact);
    this.btAddContacts.setOnClickListener(this);

    return rootView;
  }

  class ContactCursorAdapter extends CursorAdapter implements OnClickListener {

    private LayoutInflater mInflater;

    public ContactCursorAdapter(Context context, Cursor c) {
      super(context, c, 0);
      Log.d("DEBUG", "constructerContactCursorAdapter ");
      this.mInflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(
          Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public void onClick(View v) {
      Intent intent = new Intent(getActivity(), ChatActivity.class);
      intent.putExtra(Configuration.PROFILE_ID, (String) (v.findViewById(R.id.tvId)).getTag());
      startActivity(intent);
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
      ViewHolder holder = (ViewHolder) view.getTag();
      holder.tvName.setText(cursor.getString(cursor.getColumnIndex(DataProvider.COL_NAME)));
      holder.tvId.setTag(String.valueOf(cursor.getInt(cursor.getColumnIndex(DataProvider.COL_ID))));

      view.setOnClickListener(this);
      // holder.tvOnlineStatus = "online";
      /*
       * 
       * holder.tvTimeLastMessage.setText(cursor.getString(cursor.getColumnIndex
       * (DataProvider.COL_EMAIL))); int count =
       * cursor.getInt(cursor.getColumnIndex(DataProvider.COL_COUNT)); if (count
       * > 0) { holder.tvLastMessage.setVisibility(View.VISIBLE);
       * holder.tvLastMessage.setText(String.format("%d new message%s", count,
       * count == 1 ? "" : "s")); } else
       * holder.tvLastMessage.setVisibility(View.GONE);
       */
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
      View itemLayout = mInflater.inflate(R.layout.contact_list_item, parent, false);
      ViewHolder holder = new ViewHolder();
      itemLayout.setTag(holder);
      holder.tvName = (TextView) itemLayout.findViewById(R.id.tvName);
      holder.tvOnlineStatus = (TextView) itemLayout.findViewById(R.id.tvOnlineStatus);
      holder.tvId = (TextView) itemLayout.findViewById(R.id.tvId);
      // holder.avatar = (ImageView) itemLayout.findViewById(R.id.avatar);
      return itemLayout;
    }

  }

  private static class ViewHolder {
    TextView tvName;
    TextView tvOnlineStatus;
    TextView tvId;

  }

  @Override
  public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
    CursorLoader loader = new CursorLoader(getActivity().getApplicationContext(), DataProvider.CONTENT_URI_PROFILE,
        new String[] { DataProvider.COL_ID, DataProvider.COL_NAME, DataProvider.COL_EMAIL, DataProvider.COL_COUNT },
        null, null, DataProvider.COL_ID + " DESC");
    return loader;
  }

  @Override
  public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
    Log.d("DEBUG", "onLoadFinished");
    contactCursorAdapter.changeCursor(cursor);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> arg0) {
    Log.d("DEBUG", "onLoaderReset");
    contactCursorAdapter.changeCursor(null);

  }

  @Override
  public void onClick(View v) {
    AddContactDialog newFragment = AddContactDialog.newInstance();
    newFragment.show(getActivity().getSupportFragmentManager(), "AddContactDialog");
  }
}
