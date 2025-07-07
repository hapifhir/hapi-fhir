package ca.uhn.fhir.repository.impl;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.repository.IRepositoryTest;

public class InMemoryRepositoryTest implements IRepositoryTest {
	FhirContext myFhirContext = FhirContext.forR4();
	InMemoryFhirRepository myRepository = InMemoryFhirRepository.emptyRepository(myFhirContext);

	@Override
	public RepositoryTestSupport getRepositoryTestSupport() {
		return new RepositoryTestSupport(myRepository);
	}

	@Override
	public boolean isPatchSupported() {
		return false;
	}

}
