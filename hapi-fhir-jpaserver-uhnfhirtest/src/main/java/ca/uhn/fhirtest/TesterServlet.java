package ca.uhn.fhirtest;

import ca.uhn.fhir.rest.server.tester.RestfulTesterServlet;

public class TesterServlet extends RestfulTesterServlet {

	private static final long serialVersionUID = 1L;

	public TesterServlet() {
		String baseUrl = System.getProperty("fhir.baseurl");
		
		addServerBase("fhirtest", "UHN/HAPI Test Server", baseUrl);
		addServerBase("hi", "Health Intersections Ref Server", "http://fhir.healthintersections.com.au/open");
		addServerBase("furore", "Spark - Furore Ref Server", "http://spark.furore.com/fhir");
		addServerBase("blaze", "Blaze (Orion Health)", "https://his-medicomp-gateway.orionhealth.com/blaze/fhir");
	}
	
}
