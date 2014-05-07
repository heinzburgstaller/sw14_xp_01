package at.tugraz.sw.hoi.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.appengine.api.datastore.dev.LocalDatastoreService;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class TestChatServlet {
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig());

	@SuppressWarnings("static-access")
	@Before
	public void setUp() {
		helper.setUp();
		LocalDatastoreService dsService = (LocalDatastoreService) helper
				.getLocalService(LocalDatastoreService.PACKAGE);
		dsService.setNoStorage(true);
	}

	@After
	public void tearDown() {
		helper.tearDown();
	}

	@Test
	public void testChatFail() throws IOException {
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
		StringWriter sr = new StringWriter();
		PrintWriter writer = new PrintWriter(sr);
		Mockito.when(response.getWriter()).thenReturn(writer);

		Mockito.when(request.getParameter(Configuration.FROM)).thenReturn(
				"sender@gmail.com");
		Mockito.when(request.getParameter(Configuration.TO)).thenReturn(
				"receiver@gmail.com");
		Mockito.when(request.getParameter(Configuration.MSG)).thenReturn(
				"This is a message.");

		new ChatServlet().doPost(request, response);
		writer.flush();
		writer.close();

		Assert.assertTrue(sr.toString().startsWith(Configuration.FAILURE));
	}
}
