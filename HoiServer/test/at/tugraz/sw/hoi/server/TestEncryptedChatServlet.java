package at.tugraz.sw.hoi.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import org.apache.commons.codec.binary.Base64;

import at.tugraz.sw.hoi.TestConstants;
import at.tugraz.sw.hoi.model.Contact;
import at.tugraz.sw.hoi.model.EMFService;

import com.google.appengine.api.datastore.dev.LocalDatastoreService;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class TestEncryptedChatServlet {
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig());

	private Contact to;
	private Contact from;

	private Key publicKey = null;
	private Key privateKey = null;

	@SuppressWarnings("static-access")
	@Before
	public void setUp() {
		helper.setUp();
		LocalDatastoreService dsService = (LocalDatastoreService) helper
				.getLocalService(LocalDatastoreService.PACKAGE);
		dsService.setNoStorage(true);

		EntityManager em = EMFService.get().createEntityManager();

		to = new Contact(TestConstants.EMAIL, TestConstants.REG_ID);
		from = new Contact("receiver@gmail.com", "receiver");

		em.persist(to);
		em.close();

		em = EMFService.get().createEntityManager();
		em.persist(from);
		em.close();

		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(1024);
			KeyPair kp = kpg.genKeyPair();
			publicKey = kp.getPublic();
			privateKey = kp.getPrivate();
		} catch (Exception e) {
			// Log.e(TAG, "RSA key pair error");
			e.printStackTrace();
		}
	}

	@After
	public void tearDown() {
		helper.tearDown();
	}

	@Test
	public void testRSAChat() throws Exception {

		byte[] encodedMsg = null;
		byte[] encodedTo = null;
//		byte[] encodedFrom = n
		String msgText = "This is a test message. Please ignore.";

		Cipher c = Cipher.getInstance("RSA");
		c.init(Cipher.ENCRYPT_MODE, privateKey);
		encodedMsg = c.doFinal(msgText.getBytes());
		encodedTo = c.doFinal(to.getEmail().getBytes());

		String sPubKey = Base64.encodeBase64String(publicKey.getEncoded());
		String sMsg = Base64.encodeBase64String(encodedMsg);
		String sTo = Base64.encodeBase64String(encodedTo);

		System.out.println(publicKey);

		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
		StringWriter sr = new StringWriter();
		PrintWriter writer = new PrintWriter(sr);
		Mockito.when(response.getWriter()).thenReturn(writer);

		Mockito.when(request.getParameter(Configuration.FROM)).thenReturn(
				sTo);
		Mockito.when(request.getParameter(Configuration.TO)).thenReturn(
				sTo);
		Mockito.when(request.getParameter(Configuration.MSG)).thenReturn(sMsg);
		Mockito.when(request.getParameter(Configuration.PUBLIC_KEY))
				.thenReturn(sPubKey);
		System.out.println(sPubKey);

		new ChatServlet().doPost(request, response);
		writer.flush();
		writer.close();
		System.out.println(sr.toString());
		Assert.assertTrue(sr.toString().startsWith(Configuration.SUCCESS));
	}

}
