package ca.uhn.fhir.rest.server.security;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

public class DummyOpenIdServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(DummyOpenIdServlet.class);

	@Override
	protected void doGet(HttpServletRequest theReq, HttpServletResponse theResp) throws ServletException, IOException {
		ourLog.info("Request: {}", theReq.getRequestURI());
		
		if (theReq.getRequestURI().equals("/openid/jwk")) {
			
			theResp.setStatus(200);
			InputStream is = DummyOpenIdServlet.class.getResourceAsStream("/svr_keystore.jwks");
			IOUtils.copy(is, theResp.getOutputStream());
			IOUtils.closeQuietly(theResp.getOutputStream());
			return;
		}
		
		
		super.doGet(theReq, theResp);
	}

}
