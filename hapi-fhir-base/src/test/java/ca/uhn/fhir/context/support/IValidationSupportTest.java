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

	@Test
	void copy_resultWithEveryFieldSet_copiesEveryField() {
		IValidationSupport.CodeValidationResult original = newResultWithEveryFieldSet();

		IValidationSupport.CodeValidationResult copy = original.copy();

		assertThat(copy).isNotSameAs(original).usingRecursiveComparison().isEqualTo(original);
	}

	@Test
	void copy_listsOfTheCopyChanged_leavesTheOriginalUnchanged() {
		IValidationSupport.CodeValidationResult original = newResultWithEveryFieldSet();

		IValidationSupport.CodeValidationResult copy = original.copy();
		copy.addIssue(new IValidationSupport.CodeValidationIssue(
				"added", IValidationSupport.IssueSeverity.WARNING, "processing"));
		copy.getProperties().add(new IValidationSupport.StringConceptProperty("added", "value"));
		copy.addUnknownSystem("http://example.org/cs/added");

		assertThat(original.getIssues()).hasSize(1);
		assertThat(original.getProperties()).hasSize(1);
		assertThat(original.getUnknownSystems()).containsExactly(SYSTEM + "|1.0");
	}

	private static IValidationSupport.CodeValidationResult newResultWithEveryFieldSet() {
		IValidationSupport.CodeValidationResult result = new IValidationSupport.CodeValidationResult()
				.setCode("code")
				.setDisplay("display")
				.setCodeSystemName("name")
				.setCodeSystemVersion("1.0")
				.setMessage("message")
				.setSeverity(IValidationSupport.IssueSeverity.ERROR)
				.setSourceDetails("source")
				.addIssue(new IValidationSupport.CodeValidationIssue(
						"diagnostics", IValidationSupport.IssueSeverity.ERROR, "not-found"))
				.addUnknownSystem(SYSTEM + "|1.0");
		result.setProperties(new ArrayList<>(List.of(new IValidationSupport.StringConceptProperty("prop", "value"))));
		return result;
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
