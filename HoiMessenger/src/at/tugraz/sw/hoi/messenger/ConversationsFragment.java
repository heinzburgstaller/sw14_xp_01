package at.tugraz.sw.hoi.messenger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

  private ListView conversationList;
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

    conversationList = (ListView) rootView.findViewById(R.id.lvConversation);
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
      this.mInflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(
          Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
      ViewHolder holder = (ViewHolder) view.getTag();

      holder.tvName.setText(cursor.getString(cursor.getColumnIndex(DataProvider.COL_EMAIL)));

      holder.tvLastMessage.setText(cursor.getString(cursor.getColumnIndex(DataProvider.COL_MESSAGE)));

      String dateString = cursor.getString(cursor.getColumnIndex(DataProvider.COL_TIME));
      Date date;
      try {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = sdf.parse(dateString);
        holder.tvTimeLastMessage.setText(new SimpleDateFormat("HH:mm").format(date));
      } catch (ParseException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
      View itemLayout = mInflater.inflate(R.layout.conversation_list_item, parent, false);
      ViewHolder holder = new ViewHolder();
      itemLayout.setTag(holder);
      holder.tvName = (TextView) itemLayout.findViewById(R.id.tvName);
      holder.tvLastMessage = (TextView) itemLayout.findViewById(R.id.tvLastMessage);
      holder.tvTimeLastMessage = (TextView) itemLayout.findViewById(R.id.tvTimeLastMessage);
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
    CursorLoader loader = new CursorLoader(getActivity(), DataProvider.CONTENT_URI_CONVERSATIONS, null, null, null,
        DataProvider.COL_TIME + " ASC");
    return loader;
  }

  @Override
  public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
    conversationCursorAdapter.changeCursor(cursor);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> arg0) {
    Log.d("onloadreset", "resetted");
    conversationCursorAdapter.changeCursor(null);
  }
}
