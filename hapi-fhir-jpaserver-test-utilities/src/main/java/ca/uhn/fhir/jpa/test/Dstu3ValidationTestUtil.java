/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
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
