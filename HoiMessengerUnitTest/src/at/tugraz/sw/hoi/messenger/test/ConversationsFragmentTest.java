package at.tugraz.sw.hoi.messenger.test;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.view.KeyEvent;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import at.tugraz.sw.hoi.messenger.ChatActivity;
import at.tugraz.sw.hoi.messenger.MainActivity.SectionsPagerAdapter;

import junit.framework.TestCase;


public class ConversationsFragmentTest extends ActivityInstrumentationTestCase2<ChatActivity> {

	private ChatActivity mActivity;
	private SectionsPagerAdapter mSectionsPagerAdapter;
	
	public ConversationsFragmentTest() {
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

  public void testConversationsFragment() {
    fail("Not yet implemented");
  }

  public void testOnCreateViewLayoutInflaterViewGroupBundle() {
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

}
