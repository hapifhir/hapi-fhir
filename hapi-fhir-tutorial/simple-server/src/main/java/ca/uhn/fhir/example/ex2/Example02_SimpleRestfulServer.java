package ca.uhn.fhir.example.ex2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import ca.uhn.fhir.rest.server.RestfulServer;

@WebServlet("/example02/*")
public class Example02_SimpleRestfulServer extends RestfulServer {

	private static final long serialVersionUID = 1L;

	@Override
	protected void initialize() throws ServletException {

		// Set the resource providers used by this server 
		setResourceProviders(new Example02_PatientResourceProvider());
		
		/* This just means to use Content-Types which are not technically
		 * FHIR compliant if a browser is detected (so that they display
		 * nicely for testing) */
		setUseBrowserFriendlyContentTypes(true);
		
	}
	
	
}
