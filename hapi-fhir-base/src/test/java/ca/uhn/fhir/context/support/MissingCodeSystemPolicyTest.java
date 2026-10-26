package ca.uhn.fhir.context.support;

import ca.uhn.fhir.context.FhirContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

// Created by Claude Opus 5.5
class MissingCodeSystemPolicyTest {

	private static final String SYSTEM = "http://example.org/cs";

	private final SingleVersionValidationSupport mySupport = new SingleVersionValidationSupport("2");
	private final ValidationSupportContext myContext = new ValidationSupportContext(mySupport);

	@ParameterizedTest
	@CsvSource({"REJECT, false", "USE_INSTALLED_VERSION, false", "ACCEPT_LISTED_CODES, true"})
	void isListedCodesAccepted_eachPolicy_isTrueOnlyForAcceptListedCodes(
			MissingCodeSystemPolicy thePolicy, boolean theExpected) {
		assertThat(thePolicy.isListedCodesAccepted()).isEqualTo(theExpected);
	}

	@ParameterizedTest
	@EnumSource(
			value = MissingCodeSystemPolicy.class,
			names = {"USE_INSTALLED_VERSION", "ACCEPT_LISTED_CODES"})
	void isNamedVersionReplaced_versionNotSupported_returnsTrue(MissingCodeSystemPolicy thePolicy) {
		assertThat(thePolicy.isNamedVersionReplaced(myContext, SYSTEM, "1")).isTrue();
	}

	@Test
	void isNamedVersionReplaced_reject_returnsFalse() {
		assertThat(MissingCodeSystemPolicy.REJECT.isNamedVersionReplaced(myContext, SYSTEM, "1"))
				.isFalse();
	}

	@Test
	void isNamedVersionReplaced_versionSupported_returnsFalse() {
		assertThat(MissingCodeSystemPolicy.USE_INSTALLED_VERSION.isNamedVersionReplaced(myContext, SYSTEM, "2"))
				.isFalse();
	}

	@ParameterizedTest
	@CsvSource(
			value = {"null, 1", "'', 1", "http://example.org/cs, null", "http://example.org/cs, ''"},
			nullValues = "null")
	void isNamedVersionReplaced_noSystemOrNoVersion_returnsFalse(String theSystem, String theVersion) {
		assertThat(MissingCodeSystemPolicy.USE_INSTALLED_VERSION.isNamedVersionReplaced(myContext, theSystem, theVersion))
				.isFalse();
	}

	/**
	 * Supports {@link #SYSTEM} at one version only
	 */
	private static class SingleVersionValidationSupport implements IValidationSupport {
		private final String myVersion;

		private SingleVersionValidationSupport(String theVersion) {
			myVersion = theVersion;
		}

		@Override
		public FhirContext getFhirContext() {
			return null;
		}

		@Override
		public boolean isCodeSystemSupported(
				ValidationSupportContext theValidationSupportContext, String theSystem, String theVersion) {
			return SYSTEM.equals(theSystem) && myVersion.equals(theVersion);
		}
	}
}
