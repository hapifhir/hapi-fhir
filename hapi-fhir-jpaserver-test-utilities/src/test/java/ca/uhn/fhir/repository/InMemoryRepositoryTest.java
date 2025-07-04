package ca.uhn.fhir.repository;

import ca.uhn.fhir.context.FhirContext;

public class InMemoryRepositoryTest implements IRepositoryTest {
	FhirContext myFhirContext = FhirContext.forR4();
	InMemoryFhirRepository myRepository = InMemoryFhirRepository.emptyRepository(myFhirContext);

	@Override
	public RepositoryTestSupport getRepositoryTestSupport() {
		return new RepositoryTestSupport(myRepository);
	}
}
