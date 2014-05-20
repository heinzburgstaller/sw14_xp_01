package at.tugraz.sw.hoi.messenger;

import android.content.Context;
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
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import at.tugraz.sw.hoi.messenger.util.DataProvider;

public class ConversationsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

  private ConversationtCursorAdapter conversationCursorAdapter;
  /**
   * The fragment argument representing the section number for this fragment.
   */
  private static final String ARG_SECTION_NUMBER = "section_number";

  /**
   * Returns a new instance of this fragment for the given section number.
   */
  public static ConversationsFragment newInstance(int sectionNumber) {
    ConversationsFragment fragment = new ConversationsFragment();
    Bundle args = new Bundle();
    args.putInt(ARG_SECTION_NUMBER, sectionNumber);
    fragment.setArguments(args);
    return fragment;
  }

  public ConversationsFragment() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_conversations, container, false);

    ListView conversationList = (ListView) rootView.findViewById(R.id.lvConversation);

    // conversationCursorAdapter = new
    // SimpleCursorAdapter(getActivity().getApplicationContext(),
    // R.layout.conversation_list_item, null, new String[] {
    // DataProvider.COL_NAME, DataProvider.COL_EMAIL },
    // new int[] { R.id.tvName, R.id.tvLastMessage }, 0);

    conversationCursorAdapter = new ConversationtCursorAdapter(getActivity().getApplicationContext(), null);
    conversationList.setAdapter(conversationCursorAdapter);

    // Prepare the loader. Either re-connect with an existing one,
    // or start a new one.
    getLoaderManager().initLoader(0, null, this);

    return rootView;
  }

  class ConversationtCursorAdapter extends CursorAdapter {

    private LayoutInflater mInflater;

    public ConversationtCursorAdapter(Context context, Cursor c) {
      super(context, c, 0);
      Log.d("DEBUG", "constructerContactCursorAdapter ");
      this.mInflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(
          Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
      ViewHolder holder = (ViewHolder) view.getTag();
      holder.tvName.setText(cursor.getString(cursor.getColumnIndex(DataProvider.COL_NAME)));

      holder.tvLastMessage.setText(cursor.getString(cursor.getColumnIndex(DataProvider.COL_EMAIL)));

      int count = cursor.getInt(cursor.getColumnIndex(DataProvider.COL_COUNT));
      if (count > 0) {
        holder.tvTimeLastMessage.setVisibility(View.VISIBLE);
        holder.tvTimeLastMessage.setText(String.format("%d new message%s", count, count == 1 ? "" : "s"));
      } else
        holder.tvTimeLastMessage.setVisibility(View.GONE);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
      View itemLayout = mInflater.inflate(R.layout.conversation_list_item, parent, false);
      ViewHolder holder = new ViewHolder();
      itemLayout.setTag(holder);
      holder.tvName = (TextView) itemLayout.findViewById(R.id.tvName);
      holder.tvLastMessage = (TextView) itemLayout.findViewById(R.id.tvLastMessage);
      holder.tvTimeLastMessage = (TextView) itemLayout.findViewById(R.id.tvTimeLastMessage);
      // holder.avatar = (ImageView) itemLayout.findViewById(R.id.avatar);
      return itemLayout;
    }
  }

  private static class ViewHolder {
    TextView tvName;
    TextView tvLastMessage;
    TextView tvTimeLastMessage;

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
    conversationCursorAdapter.changeCursor(cursor);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> arg0) {
    Log.d("DEBUG", "onLoaderReset");
    conversationCursorAdapter.changeCursor(null);

  }
}
