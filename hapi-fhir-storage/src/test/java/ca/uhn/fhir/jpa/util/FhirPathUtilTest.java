package ca.uhn.fhir.jpa.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FhirPathUtilTest {

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

	@ParameterizedTest
	@CsvSource("""
	    "Appointment.participant.actor.reference.where(startsWith('Patient'))",true
  		"Appointment.participant.actor.reference.where(startsWith('Patient')",false
		""")
	public void hasBalancedBraces_coverage(String theStr, boolean theExpectedValue) {
		boolean output = FhirPathUtils.hasBalancedBraces(theStr);

		assertEquals(theExpectedValue, output);
	}
}
