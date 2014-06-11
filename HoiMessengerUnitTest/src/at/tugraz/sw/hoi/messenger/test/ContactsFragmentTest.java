package at.tugraz.sw.hoi.messenger.test;
import android.test.ActivityInstrumentationTestCase2;
import at.tugraz.sw.hoi.messenger.ChatActivity;
import at.tugraz.sw.hoi.messenger.MainActivity.SectionsPagerAdapter;


public class ContactsFragmentTest extends ActivityInstrumentationTestCase2<ChatActivity> {

	private ChatActivity mActivity;
	private SectionsPagerAdapter mSectionsPagerAdapter;
	
	public ContactsFragmentTest() {
		super(ChatActivity.class);
	}

	public void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
	}
	
  protected void tearDown() throws Exception {
    super.tearDown();
  }
  
  public void testPreconditions() 
  {
    assertNotNull("mActivity is null", mActivity);
  }
  

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
  }

}
