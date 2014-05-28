package at.tugraz.sw.hoi.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import at.tugraz.sw.hoi.model.Contact;
import at.tugraz.sw.hoi.model.EMFService;

import com.google.appengine.api.datastore.dev.LocalDatastoreService;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class TestContactServlet {

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig());

	private static final String EMAIL = "somestuff@gmail.com";

	@SuppressWarnings("static-access")
	@Before
	public void setUp() {
		helper.setUp();
		LocalDatastoreService dsService = (LocalDatastoreService) helper
				.getLocalService(LocalDatastoreService.PACKAGE);
		dsService.setNoStorage(true);
		EntityManager em = EMFService.get().createEntityManager();
		em.persist(new Contact(EMAIL, "XXX"));
		em.close();
	}

	@After
	public void tearDown() {
		helper.tearDown();
	}

	@Test
	public void testContactSuccess() throws IOException {
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
		StringWriter sr = new StringWriter();
		PrintWriter writer = new PrintWriter(sr);
		Mockito.when(response.getWriter()).thenReturn(writer);

		Mockito.when(request.getParameter(Configuration.EMAIL)).thenReturn(
				EMAIL);


		new ContactServlet().doPost(request, response);
		writer.flush();
		writer.close();
		Assert.assertTrue(sr.toString().equals(Configuration.SUCCESS));

		EntityManager em = EMFService.get().createEntityManager();
		Contact contact = Contact.findByEmail(EMAIL, em);
		em.close();
		Assert.assertNotNull(contact);
	}

	@Test
	public void testContactFailure() throws IOException {
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
		StringWriter sr = new StringWriter();
		PrintWriter writer = new PrintWriter(sr);
		Mockito.when(response.getWriter()).thenReturn(writer);

		Mockito.when(request.getParameter(Configuration.EMAIL)).thenReturn(
				"JunitSomestuff@gmail.com");


		new ContactServlet().doPost(request, response);
		writer.flush();
		writer.close();
		Assert.assertFalse(sr.toString().equals(Configuration.SUCCESS));

		EntityManager em = EMFService.get().createEntityManager();
		Contact contact = Contact.findByEmail("JunitSomestuff@gmail.com", em);
		em.close();
		Assert.assertNull(contact);
	}
}
