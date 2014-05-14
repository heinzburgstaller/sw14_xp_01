package at.tugraz.sw.hoi.messenger.test;

import junit.framework.TestCase;
import util.ServerUtilities;

public class TestGcm extends TestCase {

	public void testRegister() {
		ServerUtilities.register("heinz.burgstaller@gmail.com", "");
	}

}
