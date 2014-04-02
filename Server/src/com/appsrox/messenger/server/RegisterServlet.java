package com.appsrox.messenger.server;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appsrox.messenger.model.Contact;
import com.appsrox.messenger.model.EMFService;

@SuppressWarnings("serial")
public class RegisterServlet extends HttpServlet {
	
	private static final Logger logger = Logger.getLogger(RegisterServlet.class.getCanonicalName());
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Registers a device with the Demo server.");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String chatId = req.getParameter(Constants.FROM);
		String regId = req.getParameter(Constants.REG_ID);
		//logger.log(Level.WARNING, "RegisterServlet: chatId=" + chatId+", regId="+regId);
		
		EntityManager em = EMFService.get().createEntityManager();
		Contact contact;
		
		try {
			//generate chat ID if not passed
			if ("".equals(chatId)) {
				do {
					chatId = Util.generateUID(3);
					contact = Contact.find(chatId, em);
				} while(contact != null);
			} else {
				contact = Contact.find(chatId, em);
			}			
			
			//create or update contact with GCM registration ID
			if (contact == null) {
				contact = new Contact(chatId, regId);
			} else {
				contact.setRegId(regId);
			}
			
			em.persist(contact);
			//logger.log(Level.WARNING, "Registered: " + chatId);
		} finally {
			em.close();
		}
		
		resp.setContentType("text/plain");
		resp.getWriter().println(chatId);
	}	
	
}
