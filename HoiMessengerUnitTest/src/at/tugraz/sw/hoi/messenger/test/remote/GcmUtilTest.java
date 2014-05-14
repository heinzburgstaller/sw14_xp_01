package at.tugraz.sw.hoi.messenger.test.remote;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.test.InstrumentationTestCase;
import at.tugraz.sw.hoi.messenger.remote.Configuration;
import at.tugraz.sw.hoi.messenger.remote.GcmUtil;

public class GcmUtilTest extends InstrumentationTestCase {

	public Context context;
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		context = getInstrumentation().getContext();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();
		
		editor.putString(Configuration.PROPERTY_REG_ID, "");
		editor.putString(Configuration.CHAT_EMAIL_ID, "heinz.burgstaller@gmail.com");
		editor.commit();
		
		
	}
	public void testGetPrefered() {
		GcmUtil gcmutil = new GcmUtil(context);
		assertEquals("heinz.burgstaller@gmail.com",gcmutil.getPreferredEmail());
		
	}
}
