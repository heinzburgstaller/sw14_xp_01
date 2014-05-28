package at.tugraz.sw.hoi.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

import org.apache.commons.codec.binary.Base64;

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

		String pubKey = req.getParameter(Configuration.PUBLIC_KEY);
		

		if (Util.isEmpty(to) || Util.isEmpty(from) || Util.isEmpty(msg)) {
			resp.getWriter().print(Configuration.FAILURE);
			return;
		}

		if (!Util.isEmpty(pubKey)) {
			try {
				byte[] keyb = Base64.decodeBase64(pubKey);
				byte[] msgb = Base64.decodeBase64(msg);
				byte[] tob = Base64.decodeBase64(to);
				byte[] fromb = Base64.decodeBase64(from);
				System.out.println(pubKey);
				System.out.println(keyb);
				Key publicKey = KeyFactory.getInstance("RSA").generatePublic(
						new X509EncodedKeySpec(keyb));
				System.out.println(publicKey);
				Cipher c = Cipher.getInstance("RSA");
				c.init(Cipher.DECRYPT_MODE, publicKey);
				byte[] decodedBytes = c.doFinal(msgb);
				msg = new String(decodedBytes,"UTF8");
				
				decodedBytes = c.doFinal(tob);
				to = new String(decodedBytes,"UTF8");
				
				decodedBytes = c.doFinal(fromb);
				from = new String(decodedBytes,"UTF8");
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				resp.getWriter().print(Configuration.FAILURE);
				return;
			}
		}
		System.out.println(msg);
		System.out.println(to);
		System.out.println(from);
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
			if (result.getErrorCodeName() != null) {
				logger.log(Level.WARNING, result.getErrorCodeName());
				resp.getWriter().print(Configuration.FAILURE);
				resp.getWriter().print(result.getErrorCodeName());
				return;
			}
		} catch (IOException e) {
			logger.log(Level.WARNING, e.getMessage());
			resp.getWriter().print(Configuration.FAILURE);
			return;
		}

		resp.getWriter().print(Configuration.SUCCESS);
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/html");
		PrintWriter writer = resp.getWriter();
		writer.println("<html>\n<body>");
		writer.println("<form action=\"\" method=\"post\"><table>");
		writer.println("<tr><td>TO</td><td><input type=\"text\" name=\""
				+ Configuration.TO + "\" /></td></tr>");
		writer.println("<tr><td>FROM</td><td><input type=\"text\" name=\""
				+ Configuration.FROM + "\" /></td></tr>");
		writer.println("<tr><td>MSG</td><td><input type=\"text\" name=\""
				+ Configuration.MSG + "\" /></td></tr>");
		writer.println("<tr><td colspan=2><input type=\"submit\" /></td></tr>");
		writer.println("</table></form>");
		writer.println("</body>");
		writer.println("</html>");
		writer.close();

	}
}
