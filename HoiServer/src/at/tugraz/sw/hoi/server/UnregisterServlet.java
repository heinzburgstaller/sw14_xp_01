package at.tugraz.sw.hoi.server;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.tugraz.sw.hoi.model.Contact;
import at.tugraz.sw.hoi.model.EMFService;
import at.tugraz.sw.hoi.util.Util;

public class UnregisterServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(RegisterServlet.class
			.getCanonicalName());

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");

		String email = req.getParameter(Configuration.EMAIL);

		if (Util.isEmpty(email)) {
			resp.getWriter().print(Configuration.FAILURE);
			resp.getWriter().print("Email has to specified...");
			return;
		}

		EntityManager em = EMFService.get().createEntityManager();
		Contact contact = Contact.findByEmail(email, em);

		if (contact == null) {
			logger.log(
					Level.INFO,
					"Contact with email={0} already unregistered - nothing to do!",
					email);
		} else {
			em.remove(contact);
			logger.log(Level.INFO, "Contact with email={0} unregistered", email);
		}

		em.close();
		resp.getWriter().print(Configuration.SUCCESS);
	}
}
