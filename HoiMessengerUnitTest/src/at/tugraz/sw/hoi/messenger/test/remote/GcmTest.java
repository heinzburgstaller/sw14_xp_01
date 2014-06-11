package at.tugraz.sw.hoi.messenger.test.remote;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.test.InstrumentationTestCase;
import android.test.IsolatedContext;
import at.tugraz.sw.hoi.messenger.remote.Configuration;
import at.tugraz.sw.hoi.messenger.remote.GcmUtil;
import at.tugraz.sw.hoi.messenger.remote.ServletResponse;
import at.tugraz.sw.hoi.messenger.remote.ServletUtil;

public class GcmTest extends InstrumentationTestCase {

	public Context context;
	public GcmUtil gcm;
	public final CountDownLatch signal = new CountDownLatch(1);
	
	protected void setUp() throws Exception {
		 
	    // isolated context so that we can't bind to the remote service,
	    // but don't isolated from system services because we need them
	    context = new IsolatedContext(null, getInstrumentation().getContext()) {
	        @Override
	        public Object getSystemService(final String pName) {
	            return getInstrumentation().getContext().getSystemService(pName);
	        }
	    };
	}

	public void testWorkflow() throws Throwable {

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();

		String rand_mail = RandomStringGenerator.generateRandomString(8,
				RandomStringGenerator.Mode.ALPHA);
		String test_mail = rand_mail + "@gmail.com";

		//editor.putString(Configuration.PROPERTY_REG_ID, "");
		editor.putString(Configuration.CHAT_EMAIL_ID, test_mail);

		editor.commit();

		runTestOnUiThread(new Runnable() {

			@Override
			public void run() {
				gcm = new GcmUtil(context, signal);
			}
		});

		/*
		 * The testing thread will wait here until the UI thread releases it
		 * above with the countDown() or 30 seconds passes and it times out.
		 */
		signal.await(30, TimeUnit.SECONDS);

		//assertEquals(test_mail, gcm.getPreferredEmail());
		String regId = gcm.getRegistrationId();
		assertNotSame("No RegID", regId);

		String testMsg = "Lorem ipsum usw. 123456\n";

		ServletResponse resp = ServletUtil.chat(testMsg, test_mail, test_mail);
		assertTrue(ServletResponse.Status.SUCCESS.equals(resp.getStatus()));

		resp = ServletUtil.unregister(test_mail);
		assertTrue(ServletResponse.Status.SUCCESS.equals(resp.getStatus()));

	}

}
