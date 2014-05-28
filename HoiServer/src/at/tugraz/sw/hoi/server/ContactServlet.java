package at.tugraz.sw.hoi.server;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

import at.tugraz.sw.hoi.model.Contact;
import at.tugraz.sw.hoi.model.EMFService;
import at.tugraz.sw.hoi.util.Util;

@SuppressWarnings("serial")
public class ContactServlet extends HttpServlet {

	private static final Logger logger = Logger.getLogger(RegisterServlet.class
			.getCanonicalName());

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");

		String email = req.getParameter(Configuration.EMAIL);

		if (Util.isEmpty(email)) {
			resp.getWriter().print(Configuration.FAILURE);
			return;
		}

		EntityManager em = EMFService.get().createEntityManager();

		Contact emailContact = Contact.findByEmail(email, em);

		if (emailContact == null) {
			resp.getWriter().print(Configuration.FAILURE);
			return;
		}

		
		em.close();
		resp.getWriter().print(Configuration.SUCCESS);
	}

}
