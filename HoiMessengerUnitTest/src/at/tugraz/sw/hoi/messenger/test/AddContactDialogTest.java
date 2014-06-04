package at.tugraz.sw.hoi.messenger.test;

import at.tugraz.sw.hoi.messenger.*;

import android.app.Instrumentation;
import android.support.v4.app.FragmentActivity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.view.KeyEvent;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import junit.framework.Assert;
import junit.framework.TestCase;

public class AddContactDialogTest extends ActivityInstrumentationTestCase2<ChatActivity> {
    
  private FragmentActivity mActivity;
  private TextView mFirstTestText;

  public AddContactDialogTest() 
  {
	  super(ChatActivity.class);
  }

  @Override
  protected void setUp() throws Exception {
      super.setUp();
      mActivity = getActivity();
  }
 protected void tearDown() throws Exception 
  {
  }

  public void testNewInstance() {
   
    }

  public void testOnCreateBundle() {
	  Assert.fail("Not yet implemented");
	   }

  public void testOnCreateDialogBundle() {
	  Assert.fail("Not yet implemented");
	 }
}
