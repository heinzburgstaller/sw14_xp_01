package at.tugraz.sw.hoi.messenger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import util.DataProvider;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import at.tugraz.sw.hoi.messenger.MainActivity.ConversationsFragment.ContactsFragment;

public class MainActivity extends ActionBarActivity {

  /**
   * The {@link android.support.v4.view.PagerAdapter} that will provide
   * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
   * derivative, which will keep every loaded fragment in memory. If this
   * becomes too memory intensive, it may be best to switch to a
   * {@link android.support.v4.app.FragmentStatePagerAdapter}.
   */
  private SectionsPagerAdapter mSectionsPagerAdapter;

  /**
   * The {@link ViewPager} that will host the section contents.
   */
  private ViewPager mViewPager;
  private static int CONVERSATIONS_FRAGMENT_INDEX = 0;
  private static int CONTACTS_FRAGMENT_INDEX = 1;
  private static int MORE_FRAGMENT_INDEX = 2;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    List<Fragment> fragments = getFragments();
    // Create the adapter that will return a fragment for each of the three
    // primary sections of the activity.
    mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), fragments);

    // Set up the ViewPager with the sections adapter.
    mViewPager = (ViewPager) findViewById(R.id.pager);
    mViewPager.setAdapter(mSectionsPagerAdapter);

    final ActionBar ab = getSupportActionBar();
    ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

    // Create a tab listener that is called when the user changes tabs.
    ActionBar.TabListener tabListener = new ActionBar.TabListener() {
      public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        // When the tab is selected, switch to the
        // corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
      }

      @Override
      public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
      }

      @Override
      public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
      }
    };

    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrollStateChanged(int state) {
      }

      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
      }

      @Override
      public void onPageSelected(int position) {
        ab.setSelectedNavigationItem(position);
      }
    };
    mViewPager.setOnPageChangeListener(pageChangeListener);
    ab.addTab(
        ab.newTab().setText(mSectionsPagerAdapter.getPageTitle(CONVERSATIONS_FRAGMENT_INDEX))
            .setTabListener(tabListener), CONVERSATIONS_FRAGMENT_INDEX);
    ab.addTab(
        ab.newTab().setText(mSectionsPagerAdapter.getPageTitle(CONTACTS_FRAGMENT_INDEX)).setTabListener(tabListener),
        CONTACTS_FRAGMENT_INDEX);
    ab.addTab(ab.newTab().setText(mSectionsPagerAdapter.getPageTitle(MORE_FRAGMENT_INDEX)).setTabListener(tabListener),
        MORE_FRAGMENT_INDEX);

  }

  private List<Fragment> getFragments() {
    List<Fragment> fList = new ArrayList<Fragment>();
    fList.add(ConversationsFragment.newInstance(CONVERSATIONS_FRAGMENT_INDEX));
    fList.add(ContactsFragment.newInstance(CONTACTS_FRAGMENT_INDEX));
    fList.add(MoreFragment.newInstance(MORE_FRAGMENT_INDEX));
    return fList;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    if (id == R.id.action_settings) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  /**
   * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one
   * of the sections/tabs/pages.
   */
  public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;

    public SectionsPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
      super(fm);
      this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
      return fragments.get(position);
    }

    @Override
    public int getCount() {
      return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
      Locale l = Locale.getDefault();
      switch (position) {
      case 0:
        return getString(R.string.title_conversations).toUpperCase(l);
      case 1:
        return getString(R.string.title_contacts).toUpperCase(l);
      case 2:
        return getString(R.string.title_more).toUpperCase(l);
      }
      return null;
    }
  }

  /**
   * A placeholder fragment containing a simple view.
   */
  public static class MoreFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section number.
     */
    public static MoreFragment newInstance(int sectionNumber) {
      MoreFragment fragment = new MoreFragment();
      Bundle args = new Bundle();
      args.putInt(ARG_SECTION_NUMBER, sectionNumber);
      fragment.setArguments(args);
      return fragment;
    }

    public MoreFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View rootView = inflater.inflate(R.layout.fragment_more, container, false);

      return rootView;
    }
  }

  /**
   * A placeholder fragment containing a simple view.
   */
  public static class ConversationsFragment extends Fragment implements OnItemClickListener,
      LoaderManager.LoaderCallbacks<Cursor> {

    private SimpleCursorAdapter contactCursorAdapter;
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
      // conversationList.setOnItemClickListener(this);

      getLoaderManager().initLoader(0, null, this);

      Log.d("DEBUG", "after initloader");
      contactCursorAdapter = new SimpleCursorAdapter(getActivity().getApplicationContext(), R.layout.main_list_item,
          null, new String[] { DataProvider.COL_NAME, DataProvider.COL_EMAIL }, new int[] { R.id.tvName,
              R.id.tvLastMessage }, 0);

      Log.d("DEBUG", "after simplecursorAdapter");
      conversationList.setAdapter(contactCursorAdapter);
      Log.d("DEBUG", "setadapter");
      return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
      // Intent intent = new Intent(this, chat_activity.class);
      // intent.putExtra(Util.PROFILE_ID, String.valueOf(arg3));
      // startActivity(intent);

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class ContactsFragment extends Fragment {
      /**
       * The fragment argument representing the section number for this
       * fragment.
       */
      private static final String ARG_SECTION_NUMBER = "section_number";

      /**
       * Returns a new instance of this fragment for the given section number.
       */
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

        return rootView;
      }

      public class ContactCursorAdapter extends CursorAdapter {

        private LayoutInflater mInflater;

        public ContactCursorAdapter(Context context, Cursor c) {
          super(context, c, 0);
          Log.d("DEBUG", "constructerContactCursorAdapter ");
          this.mInflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(
              Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
          ViewHolder holder = (ViewHolder) view.getTag();
          Log.d("DEBUG", "bindview");
          holder.tvName.setText(cursor.getString(cursor.getColumnIndex(DataProvider.COL_NAME)));
          holder.tvTimeLastMessage.setText(cursor.getString(cursor.getColumnIndex(DataProvider.COL_EMAIL)));
          int count = cursor.getInt(cursor.getColumnIndex(DataProvider.COL_COUNT));
          if (count > 0) {
            holder.tvLastMessage.setVisibility(View.VISIBLE);
            holder.tvLastMessage.setText(String.format("%d new message%s", count, count == 1 ? "" : "s"));
          } else
            holder.tvLastMessage.setVisibility(View.GONE);

          // photoCache.DisplayBitmap(requestPhoto(cursor.getString(cursor
          // .getColumnIndex(DataProvider.COL_EMAIL))), holder.avatar);

        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
          Log.d("DEBUG", "newview");
          View itemLayout = mInflater.inflate(R.layout.main_list_item, parent, false);
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

    }

    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
      // Log.d("DEBUG", "oncreateLoader");
      // CursorLoader loader = new
      // CursorLoader(getActivity().getApplicationContext(),
      // DataProvider.CONTENT_URI_PROFILE,
      // new String[] { DataProvider.COL_NAME, DataProvider.COL_EMAIL,
      // DataProvider.COL_COUNT }, null, null, " DESC");
      //
      // Log.d("DEBUG", "endeLoader");
      // Log.d("DEBUG", loader.toString());
      // Log.d("DEBUG", loader.getSelection().toString());
      // Log.d("DEBUG", loader.getSelection().toString());
      //
      // return loader;
      return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
      Log.d("DEBUG", "onLoadFinished");
      contactCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
      Log.d("DEBUG", "onLoaderReset");
      contactCursorAdapter.swapCursor(null);

    }
  }
}
