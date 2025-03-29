package ca.uhn.fhir.rest.client.impl;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClientProvider;
import ca.uhn.fhir.rest.client.api.IGenericClient;

public class FhirContextGenericClientProvider implements IGenericClientProvider {

	private final FhirContext myFhirContext;

	public FhirContextGenericClientProvider(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	@Override
	public IGenericClient newRestfulGenericClient(String theBaseUrl) {
		return myFhirContext.newRestfulGenericClient(theBaseUrl);
	}
}
