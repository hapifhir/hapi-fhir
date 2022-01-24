package ca.uhn.fhir.validation;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
		assertEquals("bob", patientWithoutContained.getNameFirstRep().getGivenAsSingleString());
		assertThat(patientWithoutContained.getContained(), hasSize(0));

		assertEquals("bob", patient.getNameFirstRep().getGivenAsSingleString());
		assertThat(patient.getContained(), hasSize(1));
		assertEquals("heavy", ((Observation)patient.getContained().get(0)).getValue().toString());
	}
}
