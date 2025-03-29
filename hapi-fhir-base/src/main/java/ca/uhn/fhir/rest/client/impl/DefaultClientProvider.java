package ca.uhn.fhir.rest.client.impl;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IClientProvider;
import ca.uhn.fhir.rest.client.api.IGenericClient;

public class DefaultClientProvider implements IClientProvider {

	private final FhirContext myFhirContext;

	public DefaultClientProvider(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	@Override
	public IGenericClient newRestfulGenericClient(String theBaseUrl) {
		return myFhirContext.newRestfulGenericClient(theBaseUrl);
	}
}
