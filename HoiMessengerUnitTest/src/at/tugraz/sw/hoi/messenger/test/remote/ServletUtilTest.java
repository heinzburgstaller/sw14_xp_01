package at.tugraz.sw.hoi.messenger.test.remote;

import android.util.Log;
import at.tugraz.sw.hoi.messenger.remote.ServletResponse;
import at.tugraz.sw.hoi.messenger.remote.ServletResponse.Status;
import at.tugraz.sw.hoi.messenger.remote.ServletUtil;
import junit.framework.TestCase;

public class ServletUtilTest extends TestCase {

	private static final String EMAIL = "heinz.burgstaller@gmail.com";
	private static final String REG_ID = "APA91bHlMWAR8404lQvJsFMxDxNn6JjdTYGM7NOVAP3ENHqaYfGfmDatew4G7i03U_QUc5YlMZ1VJl3ZemXVyTHU3fbxHVDS_iIw6mU8GgsiQRC-33sBaMsr97RFqfkgdxFMmfAw5YZf2NA6qXZBH2K09dgWCTxv3A";

	public void testRegister() {
		ServletResponse response = ServletUtil.register(EMAIL, REG_ID);
		Log.d("JUnit", response.toString());
		assertTrue(Status.SUCCESS.equals(response.getStatus()));
	}

}
