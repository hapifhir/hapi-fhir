package ca.uhn.fhir.example.ex3;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import ca.uhn.fhir.rest.server.RestfulServer;

@WebServlet("/example03/*")
public class Example03_SimpleRestfulServer extends RestfulServer {

	private static final long serialVersionUID = 1L;

	@Override
	protected void initialize() throws ServletException {
		setResourceProviders(new Example03_PatientResourceProvider());
	}
	
	
}
