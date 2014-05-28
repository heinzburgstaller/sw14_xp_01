package at.tugraz.sw.hoi.messenger.test.remote;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.test.AndroidTestCase;
import at.tugraz.sw.hoi.messenger.remote.Configuration;
import at.tugraz.sw.hoi.messenger.remote.GcmUtil;

public class GcmUtilTest extends AndroidTestCase {

	private Context context;
	private SharedPreferences prefs;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		context = getContext();
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();

		editor.putString(Configuration.PROPERTY_REG_ID, "");
		editor.putString(Configuration.CHAT_EMAIL_ID,
				"heinz.burgstaller@gmail.com");
		editor.commit();
	}

	public void testGetPrefered() {
		GcmUtil gcmutil = new GcmUtil(context);
		String regId = prefs.getString(Configuration.PROPERTY_REG_ID, "Fail");

		android.util.Log.d("JUnit", regId);
		assertEquals("heinz.burgstaller@gmail.com", gcmutil.getPreferredEmail());
		assertNotSame("Fail", regId);
		assertEquals("heinz.burgstaller@gmail.com", gcmutil.getPreferredEmail());
	}
}
