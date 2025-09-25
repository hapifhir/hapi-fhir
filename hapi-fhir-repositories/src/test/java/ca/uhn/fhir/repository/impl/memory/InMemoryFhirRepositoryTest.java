package ca.uhn.fhir.repository.impl.memory;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.repository.IRepositoryTest;
import ca.uhn.fhir.util.BundleBuilder;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryFhirRepositoryTest implements IRepositoryTest {
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

	@Test
	void testCreateFromBundle() {
	    // given
		BundleBuilder bundleBuilder = new BundleBuilder(myFhirContext);
		bundleBuilder.addDocumentEntry(new Patient());
		bundleBuilder.addDocumentEntry(new Observation());
		IBaseBundle b = bundleBuilder.getBundle();

	    // when
		myRepository = InMemoryFhirRepository.fromBundleContents(myFhirContext,b);

	    // then
	    assertThat(myRepository.getResourceStorage().getAllOfType("Patient")).hasSize(1);
	}

}
