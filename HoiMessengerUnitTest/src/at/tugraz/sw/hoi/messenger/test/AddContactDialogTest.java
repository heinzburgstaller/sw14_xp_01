package at.tugraz.sw.hoi.messenger.test;

import android.support.v4.app.FragmentActivity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;
import at.tugraz.sw.hoi.messenger.ChatActivity;

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

}
