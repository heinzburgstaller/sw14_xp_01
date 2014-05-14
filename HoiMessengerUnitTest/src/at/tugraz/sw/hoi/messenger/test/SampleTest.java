package at.tugraz.sw.hoi.messenger.test;

import android.test.ActivityInstrumentationTestCase2;
import at.tugraz.sw.hoi.messenger.MainActivity;
import at.tugraz.sw.hoi.messenger.R;

import com.robotium.solo.Solo;

public class SampleTest extends ActivityInstrumentationTestCase2<MainActivity> {

	private Solo solo;

	public SampleTest() {
		super(MainActivity.class);
	}

	public void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
	} 

	public void testButton() throws Exception {
		solo.clickOnView(solo.getView(R.id.btSendMessage));
	} 

	@Override
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}
}