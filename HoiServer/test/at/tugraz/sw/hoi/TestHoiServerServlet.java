package at.tugraz.sw.hoi;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class TestHoiServerServlet {

	public static final Integer NUMBER_1 = 7;
	public static final Integer NUMBER_2 = 10;

	@Test
	public void testJUnit() throws IOException {
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
		StringWriter sr = new StringWriter();
		PrintWriter writer = new PrintWriter(sr);
		Mockito.when(response.getWriter()).thenReturn(writer);
		Mockito.when(request.getParameter(HoiServerServlet.PARAM_NUMBER_1))
				.thenReturn(NUMBER_1.toString());
		Mockito.when(request.getParameter(HoiServerServlet.PARAM_NUMBER_2))
				.thenReturn(NUMBER_2.toString());

		new HoiServerServlet().doGet(request, response);
		writer.flush();
		writer.close();
		Assert.assertTrue(sr.toString().equals(
				Integer.toString(NUMBER_1 + NUMBER_2)));
	}

}
