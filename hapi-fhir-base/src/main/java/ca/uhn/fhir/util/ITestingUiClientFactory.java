package ca.uhn.fhir.util;

import javax.servlet.http.HttpServletRequest;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.IGenericClient;

/**
 * This interface isn't used by hapi-fhir-base, but is used by the 
 * <a href="http://jamesagnew.github.io/hapi-fhir/doc_server_tester.html">Web Testing UI</a>
 */
public interface ITestingUiClientFactory {

	/**
	 * Instantiate a new client
	 */
	IGenericClient newClient(FhirContext theFhirContext, HttpServletRequest theRequest, String theServerBaseUrl);
	
}
