package ca.uhn.fhir.context.support;

import ca.uhn.fhir.context.FhirContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// Created by Claude Opus 5.5
class IValidationSupportTest {

	private static final String SYSTEM = "http://example.org/cs";

	@ParameterizedTest
	@CsvSource(
			value = {"null, null", "null, 1.0", "'', 1.0"},
			nullValues = "null")
	void isCodeSystemSupported_noSystem_answersFalseWithoutAskingTheUrlOnlyForm(String theSystem, String theVersion) {
		ExactUrlValidationSupport support = new ExactUrlValidationSupport(SYSTEM);

		boolean supported =
				support.isCodeSystemSupported(new ValidationSupportContext(support), theSystem, theVersion);

		assertThat(supported).isFalse();
		assertThat(support.myAskedUrls).isEmpty();
	}

	@Test
	void isCodeSystemSupported_moduleRecognisingOnlyThePlainUrl_asksVersionedThenPlain() {
		ExactUrlValidationSupport support = new ExactUrlValidationSupport(SYSTEM);

		boolean supported = support.isCodeSystemSupported(new ValidationSupportContext(support), SYSTEM, "1.0");

		assertThat(supported).isTrue();
		assertThat(support.myAskedUrls).containsExactly(SYSTEM + "|1.0", SYSTEM);
	}

	/**
	 * A module written against the URL-only form, which compares the URL exactly and assumes it is present.
	 */
	private static class ExactUrlValidationSupport implements IValidationSupport {
		private final String mySystem;
		private final List<String> myAskedUrls = new ArrayList<>();

		private ExactUrlValidationSupport(String theSystem) {
			mySystem = theSystem;
		}

		@Override
		public FhirContext getFhirContext() {
			return null;
		}

		@Override
		public boolean isCodeSystemSupported(ValidationSupportContext theValidationSupportContext, String theSystem) {
			myAskedUrls.add(theSystem);
			return theSystem.equals(mySystem);
		}
	}
}
