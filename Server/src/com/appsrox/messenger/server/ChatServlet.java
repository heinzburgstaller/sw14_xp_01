package com.appsrox.messenger.server;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appsrox.messenger.model.Contact;
import com.appsrox.messenger.model.EMFService;
import com.appsrox.messenger.model.Group;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

@SuppressWarnings("serial")
public class ChatServlet extends HttpServlet {
	
	private static final Logger logger = Logger.getLogger(ChatServlet.class.getCanonicalName());
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Sends a message to the GCM server.");		
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String msg = req.getParameter(Constants.MSG);
		String from = req.getParameter(Constants.FROM);
		String to = req.getParameter(Constants.TO);
		//logger.log(Level.WARNING, "ChatServlet: msg=" + msg+", from="+from+", to="+to);
		
		List<String> regIds = new ArrayList<String>();
		EntityManager em = EMFService.get().createEntityManager();
		
		try {
			if (to.length() == 2) {//group
				Group group = Group.find(to, em);
				if (group == null) {
					err(resp, "Group "+to+" not found");
					return;
				}
				
				List<String> members = Util.strToList(group.getMembers());
				
//				Contact fromContact = Contact.find(from, em);
				boolean isMember = members.remove(from);
				
				for(String member : members) {
					regIds.add(Contact.find(member, em).getRegId());
				}
				
				if (!isMember) {//join group
					members.add(from);
					group.setMembers(Util.listToStr(members));
					em.persist(group);
					//logger.log(Level.WARNING, to + " new member: " + from);
				}				
				
			} else if (to.length() == 3) {//contact
				Contact toContact = Contact.find(to, em);
				if (toContact == null) {
					err(resp, "Contact "+to+" not found");
					return;
				}
				
				regIds.add(toContact.getRegId());
			}
		} finally {
			em.close();
		}
		
		if ("".equals(Constants.API_KEY)) {
			err(resp, "Server not initialized with API key");
			return;
		}
		
		Sender sender = new Sender(Constants.API_KEY);
		Message message = new Message.Builder()
//			.delayWhileIdle(true)
			.addData(Constants.TO, to).addData(Constants.FROM, from).addData(Constants.MSG, msg)
			.build();
		
		try {
			if (regIds.size() == 1) {
				Result result = sender.send(message, regIds.get(0), 5);
				//logger.log(Level.WARNING, "Result: " + result.toString());
			} else if (regIds.size() > 1) {
				MulticastResult result = sender.send(message, regIds, 5);
				//logger.log(Level.WARNING, "MulticastResult: " + result.toString());
			} else {
				err(resp, "No recipients for message");
				return;
			}
		} catch (IOException e) {
			//logger.log(Level.SEVERE, e.getMessage());
			err(resp, e.getMessage());
			return;
		}
	}
	
	private void err(HttpServletResponse resp, String msg) throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Err: "+msg);
	}

}
