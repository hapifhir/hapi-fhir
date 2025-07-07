package ca.uhn.fhir.repository;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.repository.impl.InMemoryFhirRepository;

public class Repositories {
	IRepository emptyInMemoryRepository(FhirContext theFhirContext) {
		return InMemoryFhirRepository.emptyRepository(theFhirContext);
	}
}
