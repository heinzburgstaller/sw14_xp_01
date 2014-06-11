package at.tugraz.sw.hoi.messenger.test;
import android.test.ActivityInstrumentationTestCase2;
import at.tugraz.sw.hoi.messenger.AddContactDialog;
import at.tugraz.sw.hoi.messenger.MainActivity;
import at.tugraz.sw.hoi.messenger.MainActivity.SectionsPagerAdapter;

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

}
