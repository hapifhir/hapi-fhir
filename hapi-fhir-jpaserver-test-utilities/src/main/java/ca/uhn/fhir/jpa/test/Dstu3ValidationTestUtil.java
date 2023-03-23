package ca.uhn.fhir.jpa.test;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.dstu3.model.OperationOutcome;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class Dstu3ValidationTestUtil {
	private static final FhirContext ourFhirContext = FhirContext.forR4Cached();

	private Dstu3ValidationTestUtil() {
	}

	public static void assertHasErrors(OperationOutcome theOperationOutcome) {
		assertTrue(hasValidationIssuesWithSeverity(theOperationOutcome, OperationOutcome.IssueSeverity.ERROR), "Expected validation errors, found none");
	}

	public static void assertHasWarnings(OperationOutcome theOperationOutcome) {
		assertTrue(hasValidationIssuesWithSeverity(theOperationOutcome, OperationOutcome.IssueSeverity.WARNING), "Expected validation warnings, found none");
	}

	public static void assertHasNoErrors(OperationOutcome theOperationOutcome) {
		assertFalse(hasValidationIssuesWithSeverity(theOperationOutcome, OperationOutcome.IssueSeverity.ERROR), "Expected no validation errors, found some");
	}

	// TODO KHS use this in places that call assertHasErrors to strengthen the assert (today many of those tests just assert a string is somewhere in the OperationOutcome,
	// when it would be stronger to assert the string is in the diagnostics of an error)
	private static boolean hasValidationIssuesWithSeverity(OperationOutcome theOperationOutcome, OperationOutcome.IssueSeverity theSeverity) {
		return theOperationOutcome.getIssue().stream().anyMatch(t -> t.getSeverity() == theSeverity);
	}

	public static String toString(OperationOutcome theOperationOutcome) {
		return ourFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(theOperationOutcome);
	}

	public static void assertErrorDiagnosticContainsString(OperationOutcome theOo, String theExpectedDiagnosticSubstring) {
		assertTrue(theOo.getIssue().stream().anyMatch(t -> t.getSeverity() == OperationOutcome.IssueSeverity.ERROR && t.getDiagnostics().contains(theExpectedDiagnosticSubstring)), "Expected a validation error with diagnostic containing '" + theExpectedDiagnosticSubstring+ "', found none");
	}
}
