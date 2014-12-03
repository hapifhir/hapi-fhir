package ca.uhn.fhir.example.ex3;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import ca.uhn.fhir.rest.server.RestfulServer;

@WebServlet("/example03/*")
public class Example03_SimpleRestfulServer extends RestfulServer {

	private static final long serialVersionUID = 1L;

	@Override
	protected void initialize() throws ServletException {

		// Set the resource providers used by this server 
		setResourceProviders(new Example03_PatientResourceProvider());
		
		/* This just means to use Content-Types which are not technically
		 * FHIR compliant if a browser is detected (so that they display
		 * nicely for testing) */
		setUseBrowserFriendlyContentTypes(true);
		
	}
	
	
}
