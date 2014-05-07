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
public class ChatServlet extends HttpServlet {

	private static final Logger logger = Logger.getLogger(RegisterServlet.class
			.getCanonicalName());

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");

		String to = req.getParameter(Configuration.TO);
		String from = req.getParameter(Configuration.FROM);
		String msg = req.getParameter(Configuration.MSG);

		if (Util.isEmpty(to) || Util.isEmpty(from) || Util.isEmpty(msg)) {
			resp.getWriter().print(Configuration.FAILURE);
			return;
		}

		EntityManager em = EMFService.get().createEntityManager();

		Contact toContact = Contact.findByEmail(to, em);
		Contact fromContact = Contact.findByEmail(from, em);

		if (toContact == null || fromContact == null) {
			resp.getWriter().print(Configuration.FAILURE);
			return;
		}

		Sender sender = new Sender(Configuration.API_KEY);
		Message message = new Message.Builder()
				// .delayWhileIdle(true)
				.addData(Configuration.TO, to)
				.addData(Configuration.FROM, from)
				.addData(Configuration.MSG, msg).build();
		try {
			Result result = sender.send(message, toContact.getRegId(), 5);
		} catch (IOException e) {
			logger.log(Level.WARNING, e.getMessage());
			resp.getWriter().print(Configuration.FAILURE);
			return;
		}

		resp.getWriter().print(Configuration.SUCCESS);
	}

}
