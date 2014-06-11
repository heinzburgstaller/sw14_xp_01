package at.tugraz.sw.hoi.messenger.test;
import com.robotium.solo.Solo;

import android.content.ContentResolver;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;
import at.tugraz.sw.hoi.messenger.*;
import at.tugraz.sw.hoi.messenger.MainActivity.SectionsPagerAdapter;
import at.tugraz.sw.hoi.messenger.util.*;


public class ContactsFragmentTest extends ActivityInstrumentationTestCase2<MainActivity> {

	private MainActivity mActivity;
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private Solo solo;
	private Context context;
	
	private ContentResolver contentResolver; 
	
	public ContactsFragmentTest() {
		super(MainActivity.class);
	}

	public void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
		mActivity = getActivity();
		contentResolver = getActivity().getContentResolver();
		contentResolver.delete(DataProvider.CONTENT_URI_MESSAGES, null, null);
		contentResolver.delete(DataProvider.CONTENT_URI_PROFILE, null, null);
	}
	
  protected void tearDown() throws Exception {
    super.tearDown();
  }
  
  public void testPreconditions() 
  {
    assertNotNull("mActivity is null", mActivity);
  }
  
  
  public void testAddContact() throws Exception {
	solo.clickOnText(solo.getString(at.tugraz.sw.hoi.messenger.R.string.title_contacts));
	solo.sleep(500);
	assertEquals(solo.getString(at.tugraz.sw.hoi.messenger.R.string.title_contacts), 
						solo.getCurrentActivity().getActionBar().getSelectedTab().getText());
		
	solo.clickOnImageButton(0);
    solo.enterText(0, "martin.erb");
    solo.enterText(1, "martin.erb91@gmail.com");
    solo.clickOnButton(0);
    solo.sleep(500);
    assertEquals(solo.getCurrentActivity() instanceof ChatActivity, true);
    assertEquals(solo.searchText("martin.erb"), true); // check if chatactivty of user is opening
    solo.goBack(); // go Back from Activity
    //solo.goBack();
    solo.sleep(500);
    
	solo.clickOnText(solo.getString(at.tugraz.sw.hoi.messenger.R.string.title_contacts));
	solo.sleep(500);
	assertEquals(solo.searchText("martin.erb"), true); // check if it is in contactslist
	
	contentResolver.delete(DataProvider.CONTENT_URI_MESSAGES, null, null);
	contentResolver.delete(DataProvider.CONTENT_URI_PROFILE, null, null);
  }
  
/*
  public void testNewInstance() {
    fail("Not yet implemented");
  }

  public void testContactsFragment() {
    fail("Not yet implemented");
  }

  public void testOnCreateViewLayoutInflaterViewGroupBundle() {
    fail("Not yet implemented");
  }

  public void testOnCreateContextMenuContextMenuViewContextMenuInfo() {
    fail("Not yet implemented");
  }

  public void testOnContextItemSelectedMenuItem() {
    fail("Not yet implemented");
  }

  public void testOnCreateLoader() {
    fail("Not yet implemented");
  }

  public void testOnLoadFinished() {
    fail("Not yet implemented");
  }

  public void testOnLoaderReset() {
    fail("Not yet implemented");
  }

  public void testOnClick() {
    fail("Not yet implemented");
  }*/

}
