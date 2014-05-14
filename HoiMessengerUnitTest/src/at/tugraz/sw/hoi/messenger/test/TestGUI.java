package at.tugraz.sw.hoi.messenger.test;
import com.robotium.solo.Solo;

import android.graphics.Point;
import android.test.ActivityInstrumentationTestCase2;
import android.view.Display;
import at.tugraz.sw.hoi.messenger.*;


public class TestGUI extends ActivityInstrumentationTestCase2<MainActivity> {

	private Solo solo;
	
	public TestGUI() {
		super(MainActivity.class);
	}
	
	private void swipeToLeft(int stepCount) {
	    Display display = solo.getCurrentActivity().getWindowManager().getDefaultDisplay();
	    Point p = new Point();
	    display.getSize(p);
	    int width = p.x;
	    int height = p.y; 
	    float xStart = width - 10 ;
	    float xEnd = 10;
	    solo.drag(xStart, xEnd, height / 2, height / 2, stepCount);
	}

	private void swipeToRight(int stepCount) {
	    Display display = solo.getCurrentActivity().getWindowManager().getDefaultDisplay();
	    Point p = new Point();
	    display.getSize(p);
	    int width = p.x;
	    int height = p.y; 
	    float xStart = 10 ;
	    float xEnd = width - 10;
	    solo.drag(xStart, xEnd, height / 2, height / 2, stepCount);
	}
	
	public void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
	}
	
	public void testActivityOpenedAtConversationsTab() throws Exception {
		// check if MainActivity started
		solo.assertCurrentActivity("Wrong Activity! - expected MainActivity", MainActivity.class);
		
		// check if selected Tab when opening is the conversation Tab
		assertEquals(solo.getCurrentActivity().getActionBar().getSelectedTab().getText(),
						solo.getString(at.tugraz.sw.hoi.messenger.R.string.title_conversations));
		
		// check if the tabs are in the right order
		assertEquals(solo.getCurrentActivity().getActionBar().getTabAt(0).getText(), 
						solo.getString(at.tugraz.sw.hoi.messenger.R.string.title_conversations));
		assertEquals(solo.getCurrentActivity().getActionBar().getTabAt(1).getText(),
						solo.getString(at.tugraz.sw.hoi.messenger.R.string.title_contacts));
		assertEquals(solo.getCurrentActivity().getActionBar().getTabAt(2).getText(), 
						solo.getString(at.tugraz.sw.hoi.messenger.R.string.title_more));
		
		// check if there exists a list of conversations
		assertNotNull(solo.getView(at.tugraz.sw.hoi.messenger.R.id.lvConversation));
		
		//check if there is a Button for sending
		assertNotNull(solo.getView(at.tugraz.sw.hoi.messenger.R.id.btSendMessage));
		
	}
	
	public void testTabsClickable() throws Exception {
		// check if MainActivity started
		solo.assertCurrentActivity("Wrong Activity! - expected MainActivity", MainActivity.class);
		
		// Click through tabs and check if the selected tab has changed the right way
		solo.clickOnText(solo.getString(at.tugraz.sw.hoi.messenger.R.string.title_contacts));
		solo.sleep(500);
		assertEquals(solo.getString(at.tugraz.sw.hoi.messenger.R.string.title_contacts), 
						solo.getCurrentActivity().getActionBar().getSelectedTab().getText());
	
		solo.clickOnText(solo.getString(at.tugraz.sw.hoi.messenger.R.string.title_more));
		solo.sleep(500);
		assertEquals(solo.getString(at.tugraz.sw.hoi.messenger.R.string.title_more), 
						solo.getCurrentActivity().getActionBar().getSelectedTab().getText());
		
		solo.clickOnText(solo.getString(at.tugraz.sw.hoi.messenger.R.string.title_conversations));
		solo.sleep(500);
		assertEquals(solo.getString(at.tugraz.sw.hoi.messenger.R.string.title_conversations), 
						solo.getCurrentActivity().getActionBar().getSelectedTab().getText());
	}
	
	
	public void testContactsTabContent() throws Exception {
		// check if MainActivity started
		solo.assertCurrentActivity("Wrong Activity! - expected MainActivity", MainActivity.class);
		
		// Go to contacts tab
		solo.clickOnText(solo.getString(at.tugraz.sw.hoi.messenger.R.string.title_contacts));
		solo.sleep(500);
		// check if it opened
		assertEquals(solo.getString(at.tugraz.sw.hoi.messenger.R.string.title_contacts), 
						solo.getCurrentActivity().getActionBar().getSelectedTab().getText());
		
		// check if there exists a list of contacts
		assertNotNull(solo.getView(at.tugraz.sw.hoi.messenger.R.id.lvContacts));
		
		//check if there is a Button for adding contacts
		assertNotNull(solo.getView(at.tugraz.sw.hoi.messenger.R.id.btAddContact));
		
	}
	
	public void testTabsSwipeable() {	
		// check if MainActivity started
		solo.assertCurrentActivity("Wrong Activity! - expected MainActivity", MainActivity.class);
		
		// Click through tabs and check if the selected tab has changed the right way
		swipeToLeft(1);
		solo.sleep(500);
		assertEquals(solo.getString(at.tugraz.sw.hoi.messenger.R.string.title_contacts), 
						solo.getCurrentActivity().getActionBar().getSelectedTab().getText());
	
		swipeToLeft(1);
		solo.sleep(500);
		assertEquals(solo.getString(at.tugraz.sw.hoi.messenger.R.string.title_more), 
						solo.getCurrentActivity().getActionBar().getSelectedTab().getText());
		
		swipeToRight(1);
		solo.sleep(500);
		assertEquals(solo.getString(at.tugraz.sw.hoi.messenger.R.string.title_contacts), 
						solo.getCurrentActivity().getActionBar().getSelectedTab().getText());
		
		swipeToRight(1);
		solo.sleep(500);
		assertEquals(solo.getString(at.tugraz.sw.hoi.messenger.R.string.title_conversations), 
						solo.getCurrentActivity().getActionBar().getSelectedTab().getText());
	}
	
	@Override
	public void tearDown() throws Exception {
	    solo.finishOpenedActivities();
	}
}