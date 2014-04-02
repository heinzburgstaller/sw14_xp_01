package com.appsrox.messenger.server;
import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class InitServlet extends HttpServlet {
	
	private static final Logger logger = Logger.getLogger(InitServlet.class.getCanonicalName());

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String apiKey = req.getParameter("api_key");
		
		String response = "";
		if (apiKey == null || "".equals(apiKey.trim())) {
			response = "Please provide an API key";
			
		} else if (!"".equals(Constants.API_KEY)) {
			response = "Sorry API key is already set!";
			
		} else {
			Constants.API_KEY = apiKey.trim();
			response = "Congratulations! API key successfully set to "+apiKey.trim()+". For security reasons API key cannot be set again. If required please redeploy the application.";
		}
		
		resp.setContentType("text/plain");
		resp.getWriter().println(response);
	}	
	
}
