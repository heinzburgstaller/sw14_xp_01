package at.tugraz.sw.hoi.messenger.test.remote;

import android.test.AndroidTestCase;
import android.util.Log;
import at.tugraz.sw.hoi.messenger.remote.CheckContactTask;
import at.tugraz.sw.hoi.messenger.remote.ServletResponse;
import at.tugraz.sw.hoi.messenger.remote.ServletResponse.Status;
import at.tugraz.sw.hoi.messenger.remote.ServletUtil;
import junit.framework.TestCase;

public class checkContactTest extends AndroidTestCase {

	private static final String EMAIL = "heinz.burgstaller@gmail.com";
	private static final String REG_ID = "APA91bHlMWAR8404lQvJsFMxDxNn6JjdTYGM7NOVAP3ENHqaYfGfmDatew4G7i03U_QUc5YlMZ1VJl3ZemXVyTHU3fbxHVDS_iIw6mU8GgsiQRC-33sBaMsr97RFqfkgdxFMmfAw5YZf2NA6qXZBH2K09dgWCTxv3A";
	
	private static final String TAG = "JUnit - checkContact";
	
	public void testCheckContact() {
		CheckContactTask ccontact = new CheckContactTask(EMAIL);
		ccontact.execute(null,null,null);
		
		Boolean result = ccontact.get();
		
		Log.d(TAG, result);
		assertTrue(result);
	}
	

}
