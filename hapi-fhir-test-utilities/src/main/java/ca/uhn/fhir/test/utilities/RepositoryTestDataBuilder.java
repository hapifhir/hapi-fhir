package ca.uhn.fhir.test.utilities;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.repository.IRepository;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

public class RepositoryTestDataBuilder implements ITestDataBuilder {

	private final IRepository myRepository;

	RepositoryTestDataBuilder(IRepository theIgRepository) {
		myRepository = theIgRepository;
	}

	public static RepositoryTestDataBuilder forRepository(IRepository theRepository) {
		return new RepositoryTestDataBuilder(theRepository);
	}

	@Override
	public IIdType doCreateResource(IBaseResource theResource) {
		return myRepository.create(theResource).getId();
	}

	@Override
	public IIdType doUpdateResource(IBaseResource theResource) {
		return myRepository.update(theResource).getId();
	}

	@Override
	public FhirContext getFhirContext() {
		return myRepository.fhirContext();
	}
}
