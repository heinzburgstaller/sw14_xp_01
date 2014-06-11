package at.tugraz.sw.hoi.messenger.test.otr;

import net.java.otr4j.OtrKeyManagerStore;
import net.java.otr4j.OtrPolicy;
import net.java.otr4j.OtrPolicyImpl;
import net.java.otr4j.session.SessionID;
import net.java.otr4j.session.SessionStatus;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.test.AndroidTestCase;
import at.tugraz.sw.hoi.messenger.otr.HoiOtrEngine;
import at.tugraz.sw.hoi.messenger.otr.HoiOtrEngineHost;
import at.tugraz.sw.hoi.messenger.otr.HoiOtrKeyManagerStore;

public class OtrStoreTest extends AndroidTestCase {

	private Context context;
	private SharedPreferences prefs;
	private static final String TEST_KEY = "some@differentmail.com";
	private static final String TEST_VALUE = "Lorem ipsum dolor sit amet";
	private static final boolean TEST_BOOL = true;
	protected void setUp() throws Exception {
		super.setUp();
		context = getContext();
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
//		Editor editor = prefs.edit();
//
//		// editor.putString(Configuration.PROPERTY_REG_ID, "");
//		editor.commit();
	}

	public void testStoreManager() {
		OtrKeyManagerStore store = new HoiOtrKeyManagerStore(prefs);
		
		store.setProperty(TEST_KEY, TEST_VALUE.getBytes());
		store.setProperty(TEST_KEY+".bool",TEST_BOOL);
		
		String prop = new String(store.getPropertyBytes(TEST_KEY));
		
		assertEquals(TEST_VALUE, prop);
		assertEquals(TEST_BOOL, store.getPropertyBoolean(TEST_KEY+".bool", false));
		
		
		
		
		
	}
	
}
