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

@SuppressWarnings("serial")
public class RegisterServlet extends HttpServlet {

	private static final Logger logger = Logger.getLogger(RegisterServlet.class
			.getCanonicalName());

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");

		String email = req.getParameter(Configuration.EMAIL);
		String regId = req.getParameter(Configuration.REG_ID);

		if (Util.isEmpty(email) || Util.isEmpty(regId)) {
			resp.getWriter().print(Configuration.FAILURE);
			resp.getWriter().print("Email and regId have to specified...");
			return;
		}

		EntityManager em = EMFService.get().createEntityManager();
		Contact contact = Contact.findByEmail(email, em);

		if (contact == null) {
			contact = new Contact(email, regId);
			em.persist(contact);

			logger.log(Level.INFO,
					"Contact registered with email={0} and regId={1}",
					new Object[] { email, regId });
		} else {
			contact.setRegId(regId);
			contact = em.merge(contact);

			logger.log(Level.INFO,
					"Contact updated with email={0} and regId={1}",
					new Object[] { email, regId });
		}

		em.close();
		resp.getWriter().print(Configuration.SUCCESS);
	}

}
