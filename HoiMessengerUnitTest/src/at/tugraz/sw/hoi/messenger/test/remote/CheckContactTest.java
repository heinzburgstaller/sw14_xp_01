package at.tugraz.sw.hoi.messenger.test.remote;

import android.test.AndroidTestCase;
import android.util.Log;
import at.tugraz.sw.hoi.messenger.remote.CheckContactTask;

public class CheckContactTest extends AndroidTestCase {

	private static final String EMAIL = "heinz.burgstaller@gmail.com";
	@SuppressWarnings("unused")
	private static final String REG_ID = "APA91bH8D8DBBUgHQqJug5Zzn-IqTs1DTs4AL9HnlIDXRRiD27gfrKRLg4gbDKm4ex6vM3diIcbpheGysBAI4P2BKEJ5vLc--9u062upSSMatq4I5plTNPkKjr1WXPFvQj8KqDP8qPy2FI6n-TmFbP33I8QEJbmaHH6CNLaNg9Yv4THGVK3gG78";

	private static final String TAG = "JUnit - checkContact";

	public void testCheckContact() {
		CheckContactTask ccontact = new CheckContactTask(EMAIL);
		ccontact.execute(null, null, null);

		Boolean result = ccontact.getResult();

		Log.d(TAG, result.toString());
		assertTrue(result);
	}

}
