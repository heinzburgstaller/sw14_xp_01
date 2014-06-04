package at.tugraz.sw.hoi.messenger.test;
import com.robotium.solo.Solo;

import android.app.Fragment;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import at.tugraz.sw.hoi.messenger.AddContactDialog;
import at.tugraz.sw.hoi.messenger.MainActivity;
import at.tugraz.sw.hoi.messenger.MainActivity.SectionsPagerAdapter;
import at.tugraz.sw.hoi.messenger.R;
import junit.framework.TestCase;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

	private MainActivity mActivity;
	private SectionsPagerAdapter mSectionsPagerAdapter;
	
	public MainActivityTest() {
		super(MainActivity.class);
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
    assertNotNull("Fragment Manager ist null",mActivity.getSupportFragmentManager());
  }
  
  public void testOnCreateBundle() 
  { 
}
  public void editContactDialogTest() {

   // final Button addButton = (Button) mActivity.getActionBar().findViewById(R.);

    AddContactDialog newFragment = AddContactDialog.newInstance();
    newFragment.show(getActivity().getSupportFragmentManager(), "AddContactDialog");

  //  TouchUtils.clickView(this, addButton);

    assertTrue(newFragment.getDialog().isShowing());

    newFragment.getDialog().dismiss();
  }



  
  
  public void testOnCreateOptionsMenuMenu() {
    fail("Not yet implemented");
  }

  public void testOnOptionsItemSelectedMenuItem() {
    fail("Not yet implemented");
  }

  public void testReRegisterUser() {
    fail("Not yet implemented");
  }

}
