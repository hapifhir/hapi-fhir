package ca.uhn.fhir.context.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.HapiLocalizer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

// Created by Claude Opus 5.5
class MissingCodeSystemPolicyEvaluatorTest {

	private final AtomicReference<MissingCodeSystemPolicy> myPolicy =
			new AtomicReference<>(MissingCodeSystemPolicy.REJECT);
	private final MissingCodeSystemPolicyEvaluator myEvaluator =
			new MissingCodeSystemPolicyEvaluator(new HapiLocalizer(), myPolicy::get);

	@Nested
	class IsNamedVersionReplaced {
		private static final String CS_URL = "http://example.org/cs";

		private final SingleVersionValidationSupport mySupport = new SingleVersionValidationSupport(CS_URL, "2");
		private final ValidationSupportContext myContext = new ValidationSupportContext(mySupport);

		@ParameterizedTest
		@EnumSource(
				value = MissingCodeSystemPolicy.class,
				names = {"USE_INSTALLED_VERSION", "ACCEPT_LISTED_CODES"})
		void isNamedVersionReplaced_versionNotSupported_returnsTrue(MissingCodeSystemPolicy thePolicy) {
			myPolicy.set(thePolicy);

			assertThat(myEvaluator.isNamedVersionReplaced(myContext, CS_URL, "1")).isTrue();
		}

		@Test
		void isNamedVersionReplaced_reject_returnsFalse() {
			myPolicy.set(MissingCodeSystemPolicy.REJECT);

			assertThat(myEvaluator.isNamedVersionReplaced(myContext, CS_URL, "1")).isFalse();
		}

		@Test
		void isNamedVersionReplaced_versionSupported_returnsFalse() {
			myPolicy.set(MissingCodeSystemPolicy.USE_INSTALLED_VERSION);

			assertThat(myEvaluator.isNamedVersionReplaced(myContext, CS_URL, "2")).isFalse();
		}

		@ParameterizedTest
		@CsvSource(
				value = {"null, 1", "'', 1", "http://example.org/cs, null", "http://example.org/cs, ''"},
				nullValues = "null")
		void isNamedVersionReplaced_noSystemOrNoVersion_returnsFalse(String theSystem, String theVersion) {
			myPolicy.set(MissingCodeSystemPolicy.USE_INSTALLED_VERSION);

			assertThat(myEvaluator.isNamedVersionReplaced(myContext, theSystem, theVersion))
					.isFalse();
		}
	}

	@Nested
	class IsListedCodesAccepted {

		@Test
		void isListedCodesAccepted_policyChangedAfterConstruction_usesCurrentPolicy() {
			myPolicy.set(MissingCodeSystemPolicy.REJECT);
			assertThat(myEvaluator.isListedCodesAccepted()).isFalse();

			myPolicy.set(MissingCodeSystemPolicy.ACCEPT_LISTED_CODES);

			assertThat(myEvaluator.isListedCodesAccepted()).isTrue();
		}
	}

	@Nested
	class IsListedCodesTakenAsListed {

		@ParameterizedTest
		@CsvSource({
			"REJECT,                false, true,  false",
			"REJECT,                true,  true,  true",
			"REJECT,                false, false, true",
			"USE_INSTALLED_VERSION, false, true,  false",
			"ACCEPT_LISTED_CODES,   false, true,  true"
		})
		void isListedCodesTakenAsListed_policyAndInclude_returnsExpected(
				MissingCodeSystemPolicy thePolicy,
				boolean theCodeSystemNotPresent,
				boolean theFailOnMissingCodeSystem,
				boolean theExpected) {
			myPolicy.set(thePolicy);

			assertThat(myEvaluator.isListedCodesTakenAsListed(theCodeSystemNotPresent, theFailOnMissingCodeSystem))
					.isEqualTo(theExpected);
		}
	}

	@Nested
	class IsListedCodesAcceptedUnchecked {

		@ParameterizedTest
		@CsvSource({
			"ACCEPT_LISTED_CODES,   true,  true,  true",
			"ACCEPT_LISTED_CODES,   false, true,  false",
			"ACCEPT_LISTED_CODES,   true,  false, false",
			"USE_INSTALLED_VERSION, true,  true,  false",
			"REJECT,                true,  true,  false"
		})
		void isListedCodesAcceptedUnchecked_policyAndInclude_returnsExpected(
				MissingCodeSystemPolicy thePolicy,
				boolean theHasListedCodes,
				boolean theCodeSystemCannotBeChecked,
				boolean theExpected) {
			myPolicy.set(thePolicy);

			assertThat(myEvaluator.isListedCodesAcceptedUnchecked(theHasListedCodes, () -> theCodeSystemCannotBeChecked))
					.isEqualTo(theExpected);
		}

		@Test
		void isListedCodesAcceptedUnchecked_reject_doesNotAskWhetherTheCodeSystemCanBeChecked() {
			myPolicy.set(MissingCodeSystemPolicy.REJECT);

			boolean result = myEvaluator.isListedCodesAcceptedUnchecked(true, () -> {
				throw new AssertionError("The code system check is not needed under REJECT");
			});

			assertThat(result).isFalse();
		}
	}

	@Nested
	class Messages {

		@Test
		void createVersionReplacedMessage_usedVersion_namesBothVersions() {
			assertThat(myEvaluator.createVersionReplacedMessage("http://example.org/cs", "1", "2"))
					.isEqualTo(
							"CodeSystem 'http://example.org/cs' version '1' is not installed, so version '2' was used instead");
		}

		@ParameterizedTest
		@CsvSource(value = {"null", "''"}, nullValues = "null")
		void createVersionReplacedMessage_noUsedVersion_namesTheInstalledVersion(String theUsedVersion) {
			assertThat(myEvaluator.createVersionReplacedMessage("http://example.org/cs", "1", theUsedVersion))
					.isEqualTo(
							"CodeSystem 'http://example.org/cs' version '1' is not installed, so the installed version was used instead");
		}

		@Test
		void createListedCodesUncheckedMessage_codeSystem_namesIt() {
			assertThat(myEvaluator.createListedCodesUncheckedMessage("http://example.org/cs|1"))
					.isEqualTo("CodeSystem 'http://example.org/cs|1' is not available, so the codes the ValueSet lists"
							+ " from it were accepted without being checked");
		}
	}

	/**
	 * Supports one code system at one version only
	 */
	private static class SingleVersionValidationSupport implements IValidationSupport {
		private final String myCodeSystemUrl;
		private final String myVersion;

		private SingleVersionValidationSupport(String theCodeSystemUrl, String theVersion) {
			myCodeSystemUrl = theCodeSystemUrl;
			myVersion = theVersion;
		}

		@Override
		public FhirContext getFhirContext() {
			return null;
		}

		@Override
		public boolean isCodeSystemSupported(
				ValidationSupportContext theValidationSupportContext, String theSystem, String theVersion) {
			return myCodeSystemUrl.equals(theSystem) && myVersion.equals(theVersion);
		}
	}
}
