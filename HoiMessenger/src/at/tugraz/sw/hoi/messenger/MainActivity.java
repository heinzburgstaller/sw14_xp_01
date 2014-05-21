package at.tugraz.sw.hoi.messenger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import at.tugraz.sw.hoi.messenger.remote.Configuration;
import at.tugraz.sw.hoi.messenger.remote.GcmUtil;

public class MainActivity extends ActionBarActivity {

  /**
   * The {@link android.support.v4.view.PagerAdapter} that will provide
   * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
   * derivative, which will keep every loaded fragment in memory. If this
   * becomes too memory intensive, it may be best to switch to a
   * {@link android.support.v4.app.FragmentStatePagerAdapter}.
   */
  private SectionsPagerAdapter mSectionsPagerAdapter;
  private GcmUtil gcm;

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

    if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
        .getString(Configuration.CHAT_EMAIL_ID, "").equals("")) {
      EditEmailDialog newFragment = EditEmailDialog.newInstance();
      newFragment.show(getSupportFragmentManager(), "EditEmailDialog");
    }
    gcm = new GcmUtil(this);
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

  public void reRegisterUser(String email) {
    gcm.reRegister(getApplicationContext(), email);
  }
}
