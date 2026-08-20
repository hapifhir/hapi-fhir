package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.context.FhirContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FhirPathUtilTest {

	private static final FhirContext ourFhirContext = FhirContext.forR4Cached();

	@ParameterizedTest
	@ValueSource(strings = {
		"Appointment.participant.actor.reference.where(startsWith('Patient')",
		"Appointment.participant.actor.reference.where((startsWith('Patient'))",
		"Appointment.participant.actor.reference.where((contains('('))"
	})
	public void cleansePath_errorCases_throws(String theStr) {
		try {
			FhirPathUtils.cleansePath(theStr);
		} catch (IllegalArgumentException ex) {
			assertTrue(ex.getLocalizedMessage().contains("Cannot cleanse path"));
		}
	}

	@Test
	void testIsPathSingleValued_singleSegmentPath_returnsFalse() {
		assertThat(FhirPathUtils.isPathSingleValued(ourFhirContext, "Patient")).isFalse();
	}

	@Test
	void testIsPathSingleValued_unknownResourceType_returnsFalse() {
		assertThat(FhirPathUtils.isPathSingleValued(ourFhirContext, "UnknownResource.field")).isFalse();
	}

	@Test
	void testIsPathSingleValued_unknownChildElement_returnsFalse() {
		assertThat(FhirPathUtils.isPathSingleValued(ourFhirContext, "Patient.unknownField")).isFalse();
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"Coverage.beneficiary",
		"Coverage.subscriber.identifier",
		"Patient.birthDate"
	})
	void testIsPathSingleValued_singleCardinalityPaths_returnsTrue(String thePath) {
		assertThat(FhirPathUtils.isPathSingleValued(ourFhirContext, thePath)).isTrue();
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"Observation.value",
		"MedicationAdministration.medication"
	})
	void testIsPathSingleValued_choiceTypeElement_returnsTrue(String thePath) {
		assertThat(FhirPathUtils.isPathSingleValued(ourFhirContext, thePath)).isTrue();
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"MedicationAdministration.medication.as(Reference)",
		"Observation.value.ofType(CodeableConcept).text",
		"Observation.value.ofType(Quantity).value"
	})
	void testIsPathSingleValued_pathWithFunctionCall_returnsTrue(String thePath) {
		assertThat(FhirPathUtils.isPathSingleValued(ourFhirContext, thePath)).isTrue();
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"Patient.generalPractitioner",
		"Observation.performer",
		"AuditEvent.entity",
		"AuditEvent.entity.what"
	})
	void testIsPathSingleValued_multiCardinalityPaths_returnsFalse(String thePath) {
		assertThat(FhirPathUtils.isPathSingleValued(ourFhirContext, thePath)).isFalse();
	}
}
