package ca.uhn.fhir.to.client;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import javax.servlet.http.HttpServletRequest;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.BearerTokenAuthInterceptor;
import ca.uhn.fhir.rest.server.util.ITestingUiClientFactory;

public class BearerTokenClientFactory implements ITestingUiClientFactory {

	@Override
	public IGenericClient newClient(FhirContext theFhirContext, HttpServletRequest theRequest, String theServerBaseUrl) {
		// Create a client
		IGenericClient client = theFhirContext.newRestfulGenericClient(theServerBaseUrl);

		String apiKey = theRequest.getParameter("apiKey");
		if (isNotBlank(apiKey)) {
			client.registerInterceptor(new BearerTokenAuthInterceptor(apiKey));
		}

		return client;
	}

}
