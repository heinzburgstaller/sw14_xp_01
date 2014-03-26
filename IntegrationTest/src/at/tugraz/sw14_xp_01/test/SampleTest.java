package at.tugraz.sw14_xp_01.test;

import android.test.ActivityInstrumentationTestCase2;
import at.tugraz.sw14_xp_01.MainActivity;
import at.tugraz.sw14_xp_01.R;

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
		solo.clickOnView(solo.getView(R.id.button1));
	}

	@Override
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}
}