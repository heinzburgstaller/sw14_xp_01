package at.tugraz.sw.hoi.messenger.test;

import at.tugraz.sw.hoi.messenger.R;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.UiThreadTest;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import at.tugraz.sw.hoi.messenger.ChatActivity;
import at.tugraz.sw.hoi.messenger.MainActivity;
import at.tugraz.sw.hoi.messenger.MainActivity.SectionsPagerAdapter;

import junit.framework.TestCase;



public class ChatActivityTest extends ActivityInstrumentationTestCase2<ChatActivity> {

  private ChatActivity mActivity;

  private static final int TIMEOUT_IN_MS = 5000;
  private static final String TEST_MESSAGE = "Hello Receiver";
  
  public ChatActivityTest() {
    super(ChatActivity.class);
  }

  public void setUp() throws Exception {
    super.setUp();
    mActivity = getActivity();
    setActivityInitialTouchMode(true);
  }
  
  protected void tearDown() throws Exception {
    super.tearDown();
  }
  
  public void testPreconditions() 
  {
    assertNotNull("mActivity is null", mActivity);
  }

  public void SendButtonTest() {

    final Button sendButton = (Button) mActivity.findViewById(R.id.btSend);
    final EditText senderMessageEditText = (EditText) mActivity.findViewById(R.id.etText);

    Instrumentation.ActivityMonitor receiverActivityMonitor = getInstrumentation()
            .addMonitor(ChatActivity.class.getName(), null, false);

    //Request focus on the EditText field. This must be done on the UiThread because
    getInstrumentation().runOnMainSync(new Runnable() {
        @Override
        public void run() {
            senderMessageEditText.requestFocus();
        }
    });
    //Wait until all events from the MainHandler's queue are processed
    getInstrumentation().waitForIdleSync();

    //Send the text message
    getInstrumentation().sendStringSync(TEST_MESSAGE);
    getInstrumentation().waitForIdleSync();

    //Click on the sendToReceiverButton to send the message to ReceiverActivity
    TouchUtils.clickView(this, sendButton);

    
    //Read the message received by ReceiverActivity
    //Verify that received message is correct
    assertNotNull(senderMessageEditText);
    assertEquals("Wrong received message", TEST_MESSAGE, senderMessageEditText.getText().toString());

    //Unregister monitor for ReceiverActivity
    getInstrumentation().removeMonitor(receiverActivityMonitor);

      
  }
}