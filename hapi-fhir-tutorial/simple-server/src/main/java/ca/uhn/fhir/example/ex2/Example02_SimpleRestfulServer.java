package ca.uhn.fhir.example.ex2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import ca.uhn.fhir.rest.server.RestfulServer;

@WebServlet("/example02/*")
public class Example02_SimpleRestfulServer extends RestfulServer {

	private static final long serialVersionUID = 1L;

	@Override
	protected void initialize() throws ServletException {
		setResourceProviders(new Example02_PatientResourceProvider());
	}
	
	
}
