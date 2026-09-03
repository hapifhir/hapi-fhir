package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhir.validation.ValidationResult;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Questionnaire;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Reproducer for: a CodeSystem stored in the terminology tables at a URL that core R4 also
 * defines (e.g. http://hl7.org/fhir/item-type) shadows the built-in definition during instance
 * ($validate) validation, even though {@link JpaStorageSettings#isAllowDatabaseValidationOverride()}
 * defaults to {@code false}.
 * <p>
 * Root cause: {@code TermReadSvcImpl#isCodeSystemSupported} claims any system with a current
 * {@code TermCodeSystemVersion} regardless of the override setting, and
 * {@code ValidationSupportChain#validateCode} only stops walking the chain once a module returns a
 * non-null result - {@code DefaultProfileValidationSupport} never implements {@code validateCode},
 * so its null result falls through to {@code TermReadSvcImpl} even when it is queried first.
 */
public class CodeSystemDatabaseShadowingR4Test extends BaseTermR4Test {

	private static final String CORE_ITEM_TYPE_URL = "http://hl7.org/fhir/item-type";

	@Test
	public void testDatabaseCodeSystemAtCoreUrlDoesNotShadowBuiltInDuringInstanceValidation() {
		// Precondition: this is the default, and is what the bug report is about.
		assertThat(myStorageSettings.isAllowDatabaseValidationOverride()).isFalse();

		// A CodeSystem that diverges from the R4 core "item-type" CodeSystem (missing "choice"),
		// stored at the exact same canonical URL but a different version. This mirrors what
		// installing hl7.fhir.uv.xver-r5.r4#0.1.0 does: it ships R5 terminology backports at
		// R4-core URLs, and R5 removed "choice" from item-type.
		CodeSystem divergingCodeSystem = new CodeSystem();
		divergingCodeSystem.setUrl(CORE_ITEM_TYPE_URL);
		divergingCodeSystem.setVersion("5.0.0");
		divergingCodeSystem.setStatus(Enumerations.PublicationStatus.ACTIVE);
		divergingCodeSystem.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		divergingCodeSystem.addConcept().setCode("group");
		divergingCodeSystem.addConcept().setCode("display");
		divergingCodeSystem.addConcept().setCode("question");
		myCodeSystemDao.create(divergingCodeSystem, mySrd);

		Questionnaire questionnaire = new Questionnaire();
		questionnaire.setStatus(Enumerations.PublicationStatus.ACTIVE);
		questionnaire
			.addItem()
			.setLinkId("1")
			.setText("Pick one")
			.setType(Questionnaire.QuestionnaireItemType.CHOICE);

		ValidationResult result = validateWithResult(questionnaire);

		boolean codeNotFoundInBuiltInCodeSystem = result.getMessages().stream()
			.anyMatch(m -> m.getSeverity() == ResultSeverityEnum.ERROR
				&& m.getMessage() != null
				&& m.getMessage().contains("Code is not found in CodeSystem")
				&& m.getMessage().contains(CORE_ITEM_TYPE_URL));

		assertThat(codeNotFoundInBuiltInCodeSystem)
			.as("The built-in R4 item-type code 'choice' should validate successfully even though a "
				+ "diverging CodeSystem was stored in the database at the same core URL, since "
				+ "allowDatabaseValidationOverride defaults to false. Messages: " + result.getMessages())
			.isFalse();
	}
}
