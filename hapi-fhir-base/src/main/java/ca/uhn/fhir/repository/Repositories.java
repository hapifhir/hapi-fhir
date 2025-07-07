package ca.uhn.fhir.repository;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.repository.impl.GenericClientRepository;
import ca.uhn.fhir.repository.impl.InMemoryFhirRepository;
import ca.uhn.fhir.rest.client.api.IGenericClient;

/**
 * Static factory methods for creating instances of {@link IRepository}.
 */
public class Repositories {
	public static IRepository emptyInMemoryRepository(FhirContext theFhirContext) {
		return InMemoryFhirRepository.emptyRepository(theFhirContext);
	}

	public static IRepository restClientRepository(IGenericClient theGenericClient) {
		return new GenericClientRepository(theGenericClient);
	}

	/**
	 * Private constructor to prevent instantiation.
	 */
	Repositories() {}
}
