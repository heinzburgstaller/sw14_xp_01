package at.tugraz.sw.hoi.messenger.test.remote;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.test.AndroidTestCase;
import at.tugraz.sw.hoi.messenger.remote.Configuration;
import at.tugraz.sw.hoi.messenger.remote.GcmUtil;
import at.tugraz.sw.hoi.messenger.remote.ServletResponse;
import at.tugraz.sw.hoi.messenger.remote.ServletUtil;
import at.tugraz.sw.hoi.messenger.remote.ServletResponse.Status;

public class GcmTest extends AndroidTestCase {
	
	public void testWorkflow() throws Exception {
		Context context = this.getContext();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();
		
		String rand_mail = RandomStringGenerator.generateRandomString(8, RandomStringGenerator.Mode.ALPHA);
		String test_mail = rand_mail + "@gmail.com";
		
		editor.putString(Configuration.PROPERTY_REG_ID, "");
		editor.putString(Configuration.CHAT_EMAIL_ID,
				test_mail);
		
		editor.commit();
		GcmUtil gcm = new GcmUtil(context);
		
		assertEquals(test_mail, gcm.getPreferredEmail());
		wait(2000);
		assertNotSame("No RegID", "", prefs.getString(Configuration.PROPERTY_REG_ID, ""));
		
		String testMsg = "Lorem ipsum usw. 123456\n";
		
		ServletResponse resp = ServletUtil.chat(testMsg, test_mail, test_mail);
		assertTrue(ServletResponse.Status.SUCCESS.equals(resp.getStatus()));
		
		resp = ServletUtil.unregister(test_mail);
		assertTrue(ServletResponse.Status.SUCCESS.equals(resp.getStatus()));
		
		
		
		
		
	}
	
	

}
