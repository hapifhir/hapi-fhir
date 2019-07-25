package ca.uhn.fhir.example.ex4;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import ca.uhn.fhir.rest.server.RestfulServer;

@WebServlet("/example04/*")
public class Example04_SimpleRestfulServer extends RestfulServer {

	private static final long serialVersionUID = 1L;

	@Override
	protected void initialize() throws ServletException {
		setResourceProviders(new Example04_PatientResourceProvider());
	}
	
	
}
