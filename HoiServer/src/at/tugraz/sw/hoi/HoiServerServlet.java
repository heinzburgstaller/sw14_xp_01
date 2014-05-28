package at.tugraz.sw.hoi;

import java.io.IOException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class HoiServerServlet extends HttpServlet {

	public static final String PARAM_NUMBER_1 = "number1";
	public static final String PARAM_NUMBER_2 = "number2";

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		int number1 = Integer.parseInt(req.getParameter(PARAM_NUMBER_1));
		int number2 = Integer.parseInt(req.getParameter(PARAM_NUMBER_2));
		resp.getWriter().print(Integer.toString(number1 + number2));
	}
}
