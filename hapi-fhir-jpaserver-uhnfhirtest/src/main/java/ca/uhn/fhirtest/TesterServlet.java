package ca.uhn.fhirtest;

import ca.uhn.fhir.rest.server.tester.RestfulTesterServlet;

public class TesterServlet extends RestfulTesterServlet {

	private static final long serialVersionUID = 1L;

	public TesterServlet() {
		String baseUrl = System.getProperty("fhir.baseurl");
		setServerBase(baseUrl);
	}
	
}
