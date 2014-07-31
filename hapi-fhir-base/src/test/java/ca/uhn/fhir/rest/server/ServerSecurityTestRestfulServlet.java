package ca.uhn.fhir.rest.server;

import javax.servlet.ServletException;

import ca.uhn.fhir.rest.server.ServerSecurityTest.DummyPatientResourceProvider;

public class ServerSecurityTestRestfulServlet extends RestfulServer
{

	@Override
	protected void initialize() throws ServletException {
		setResourceProviders(new DummyPatientResourceProvider());
	}
	
}