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
		"Patient.gender",
		"Patient.birthDate",
		"Observation.status"
	})
	void testIsPathSingleValued_singleCardinalityPaths_returnsTrue(String thePath) {
		assertThat(FhirPathUtils.isPathSingleValued(ourFhirContext, thePath)).isTrue();
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"Patient.generalPractitioner",
		"AuditEvent.entity",
		"Observation.performer"
	})
	void testIsPathSingleValued_multiCardinalityPaths_returnsFalse(String thePath) {
		assertThat(FhirPathUtils.isPathSingleValued(ourFhirContext, thePath)).isFalse();
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"Patient.name.family",
		"AuditEvent.entity.what"
	})
	void testIsPathSingleValued_multiValuedIntermediateSegment_returnsFalse(String thePath) {
		// intermediate elem is 0..* so any deeper path through it is multi-valued
		assertThat(FhirPathUtils.isPathSingleValued(ourFhirContext, thePath)).isFalse();
	}

	@Test
	void testIsPathSingleValued_singleValuedIntermediateSegment_returnsTrue() {
		// Coverage.subscriber is 0..1 Reference; Reference.identifier is 0..1 Identifier
		assertThat(FhirPathUtils.isPathSingleValued(ourFhirContext, "Coverage.subscriber.identifier")).isTrue();
	}

	@Test
	void testIsPathSingleValued_choiceTypeElement_returnsTrue() {
		// Observation.value[x] is a choice type but single-cardinality; path uses bare "value"
		assertThat(FhirPathUtils.isPathSingleValued(ourFhirContext, "Observation.value")).isTrue();
	}
}
