package at.tugraz.sw.hoi;

import java.io.IOException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class HoiServerServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, world");
	}
}
