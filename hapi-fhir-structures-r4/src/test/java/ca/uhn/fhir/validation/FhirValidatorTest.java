package ca.uhn.fhir.validation;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FhirValidatorTest {
	FhirContext ourFhirContext = FhirContext.forR4Cached();

	@Test
	public void testWithoutContainedResourcesDoesNotAlter() {
		// setup
		FhirValidator validator = new FhirValidator(ourFhirContext);
		Patient patient = new Patient();
		patient.addName().addGiven("bob");
		Observation obs = new Observation();
		obs.setValue(new StringType("heavy"));
		patient.getContained().add(obs);

		// run
		Patient patientWithoutContained = (Patient)validator.withoutContainedResources(patient);

		// check
		assertThat(patientWithoutContained.getNameFirstRep().getGivenAsSingleString()).isEqualTo("bob");
		assertThat(patientWithoutContained.getContained()).hasSize(0);

		assertThat(patient.getNameFirstRep().getGivenAsSingleString()).isEqualTo("bob");
		assertThat(patient.getContained()).hasSize(1);
		assertThat(((Observation) patient.getContained().get(0)).getValue().toString()).isEqualTo("heavy");
	}
}
