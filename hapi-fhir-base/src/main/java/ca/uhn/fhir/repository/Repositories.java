package ca.uhn.fhir.repository;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.repository.impl.GenericClientRepository;
import ca.uhn.fhir.repository.impl.InMemoryFhirRepository;
import ca.uhn.fhir.rest.client.api.IGenericClient;

public class Repositories {
	public IRepository emptyInMemoryRepository(FhirContext theFhirContext) {
		return InMemoryFhirRepository.emptyRepository(theFhirContext);
	}

	public IRepository restClientRepository(IGenericClient theGenericClient) {
		return new GenericClientRepository(theGenericClient);
	}
}
