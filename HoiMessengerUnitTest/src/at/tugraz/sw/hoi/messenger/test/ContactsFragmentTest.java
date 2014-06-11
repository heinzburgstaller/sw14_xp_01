package at.tugraz.sw.hoi.messenger.test;
import android.content.ContentResolver;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import at.tugraz.sw.hoi.messenger.ChatActivity;
import at.tugraz.sw.hoi.messenger.MainActivity;
import at.tugraz.sw.hoi.messenger.MainActivity.SectionsPagerAdapter;
import at.tugraz.sw.hoi.messenger.util.DataProvider;

import com.robotium.solo.Solo;


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
//    solo.finishOpenedActivities();
  }
  
  public void testAddContact() throws Exception {
	solo.clickOnText(solo.getString(at.tugraz.sw.hoi.messenger.R.string.title_contacts));
	solo.sleep(500);
	assertEquals(solo.getString(at.tugraz.sw.hoi.messenger.R.string.title_contacts), 
						solo.getCurrentActivity().getActionBar().getSelectedTab().getText());
		
	solo.clickOnImageButton(0);
    solo.enterText(0, "martin.erb");
    solo.enterText(1, "martin.erb@gmail.com");
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
	solo.finishOpenedActivities();
  }
}
