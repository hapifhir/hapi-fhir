package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.validation.JpaValidationSupportChain;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Questionnaire;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Regression test checking that a CodeSystem stored in the database with a canonical URL that matches a built-in definition
 * (e.g. {@code http://hl7.org/fhir/item-type}) should not take precedence during instance validation or during ValueSet pre-expansion when {@link JpaStorageSettings#isAllowDatabaseValidationOverride()} is {@code false}.
 * <p>
 * These tests assert that the built-in R4 terminology keeps winning when the setting is {@code false} and gives way to the database stored terminology wins when the setting is {@code true}
 */
class CoreTerminologyOverrideTest extends BaseJpaR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(CoreTerminologyOverrideTest.class);

	private static final String CORE_ITEM_TYPE_SYSTEM = "http://hl7.org/fhir/item-type";
	private static final String MY_ITEM_TYPE_VS = "http://example.org/fhir/ValueSet/my-item-type";

	@Autowired
	private IValidationSupport myJpaValidationSupportChain;

	@AfterEach
	public void after() {
		JpaStorageSettings defaults = new JpaStorageSettings();
		myStorageSettings.setAllowDatabaseValidationOverride(defaults.isAllowDatabaseValidationOverride());
		myStorageSettings.setPreExpandValueSets(defaults.isPreExpandValueSets());
		((JpaValidationSupportChain) myJpaValidationSupportChain).rebuildChainForUnitTest();
	}

	@Test
	void instanceValidation_codeSystemAtCoreUrl_doesNotShadowBuiltInDefinitions_whenOverrideDisabled() {
		// Setup
		myStorageSettings.setAllowDatabaseValidationOverride(false);
		createR5ItemTypeBackportCodeSystem();
		// Make sure no previously cached validation results mask the behavior under test
		((JpaValidationSupportChain) myJpaValidationSupportChain).rebuildChainForUnitTest();

		Questionnaire questionnaire = new Questionnaire();
		questionnaire.setStatus(Enumerations.PublicationStatus.ACTIVE);
		questionnaire.addItem()
			.setLinkId("1")
			.setText("Pick one")
			.setType(Questionnaire.QuestionnaireItemType.CHOICE);

		// Execute
		OperationOutcome outcome = (OperationOutcome) myQuestionnaireDao
			.validate(questionnaire, null, null, null, null, null, mySrd)
			.getOperationOutcome();
		ourLog.info("Validation outcome:\n{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));

		// Verify: "choice" is a valid R4 item-type code, so the built-in definition must win over
		// the R5 backport stored in the database while allowDatabaseValidationOverride=false
		List<String> errors = outcome.getIssue().stream()
			.filter(t -> t.getSeverity() == OperationOutcome.IssueSeverity.ERROR || t.getSeverity() == OperationOutcome.IssueSeverity.FATAL)
			.map(OperationOutcome.OperationOutcomeIssueComponent::getDiagnostics)
			.collect(Collectors.toList());
		assertThat(errors).isEmpty();
	}

	@Test
	void preExpansion_valueSetIncludingCoreSystem_expandsAgainstBuiltInDefinitions_whenOverrideDisabled() {
		// Setup
		myStorageSettings.setAllowDatabaseValidationOverride(false);
		myStorageSettings.setPreExpandValueSets(true);
		createR5ItemTypeBackportCodeSystem();

		ValueSet valueSet = new ValueSet();
		valueSet.setUrl(MY_ITEM_TYPE_VS);
		valueSet.setStatus(Enumerations.PublicationStatus.ACTIVE);
		valueSet.getCompose().addInclude().setSystem(CORE_ITEM_TYPE_SYSTEM);
		myValueSetDao.create(valueSet, mySrd);
		myBatch2JobHelper.awaitNoJobsRunning();

		// Confirm we pre-expanded successfully
		runInTransaction(() -> {
			List<TermValueSet> valueSets = myTermValueSetDao.findTermValueSetByUrl(Pageable.unpaged(), MY_ITEM_TYPE_VS);
			assertEquals(1, valueSets.size());
			assertEquals(TermValueSetPreExpansionStatusEnum.EXPANDED, valueSets.get(0).getExpansionStatus());
		});

		// Execute
		ValueSet expanded = myTermSvc.expandValueSet(new ValueSetExpansionOptions(), valueSet);
		List<String> codes = expanded.getExpansion().getContains().stream()
			.map(ValueSet.ValueSetExpansionContainsComponent::getCode)
			.collect(Collectors.toList());
		ourLog.info("Expanded ValueSet contains {} codes: {}", codes.size(), codes);

		// Verify: the expansion must reflect the built-in R4 item-type definition (17 codes incl. "choice"), not the override stored in the database (3 codes, no "choice")
		assertThat(codes).hasSize(17);
		assertThat(codes).contains("choice");
	}

	@Test
	void instanceValidation_codeSystemAtCoreUrl_shadowsBuiltInDefinitions_whenOverrideEnabled() {
		// Setup
		myStorageSettings.setAllowDatabaseValidationOverride(true);
		createR5ItemTypeBackportCodeSystem();
		// Rebuild so the chain reflects the flipped setting and no cached validation results interfere
		((JpaValidationSupportChain) myJpaValidationSupportChain).rebuildChainForUnitTest();

		Questionnaire questionnaire = new Questionnaire();
		questionnaire.setStatus(Enumerations.PublicationStatus.ACTIVE);
		questionnaire.addItem()
			.setLinkId("1")
			.setText("Pick one")
			.setType(Questionnaire.QuestionnaireItemType.CHOICE);

		// Execute
		OperationOutcome outcome = (OperationOutcome) myQuestionnaireDao
			.validate(questionnaire, null, null, null, null, null, mySrd)
			.getOperationOutcome();
		ourLog.info("Validation outcome:\n{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));

		// Verify: with allowDatabaseValidationOverride=true the database-stored R5 backport is authoritative, so "choice" (absent from it) must be rejected
		List<String> errors = outcome.getIssue().stream()
			.filter(t -> t.getSeverity() == OperationOutcome.IssueSeverity.ERROR || t.getSeverity() == OperationOutcome.IssueSeverity.FATAL)
			.map(OperationOutcome.OperationOutcomeIssueComponent::getDiagnostics)
			.collect(Collectors.toList());
		assertThat(errors).anySatisfy(t -> assertThat(t).contains("Code is not found in CodeSystem: " + CORE_ITEM_TYPE_SYSTEM));
	}

	@Test
	void preExpansion_valueSetIncludingCoreSystem_expandsAgainstDatabaseCodeSystem_whenOverrideEnabled() {
		// Setup
		myStorageSettings.setAllowDatabaseValidationOverride(true);
		myStorageSettings.setPreExpandValueSets(true);
		((JpaValidationSupportChain) myJpaValidationSupportChain).rebuildChainForUnitTest();
		createR5ItemTypeBackportCodeSystem();

		ValueSet valueSet = new ValueSet();
		valueSet.setUrl(MY_ITEM_TYPE_VS);
		valueSet.setStatus(Enumerations.PublicationStatus.ACTIVE);
		valueSet.getCompose().addInclude().setSystem(CORE_ITEM_TYPE_SYSTEM);
		myValueSetDao.create(valueSet, mySrd);
		myBatch2JobHelper.awaitNoJobsRunning();

		// Confirm we pre-expanded successfully
		runInTransaction(() -> {
			List<TermValueSet> valueSets = myTermValueSetDao.findTermValueSetByUrl(Pageable.unpaged(), MY_ITEM_TYPE_VS);
			assertEquals(1, valueSets.size());
			assertEquals(TermValueSetPreExpansionStatusEnum.EXPANDED, valueSets.get(0).getExpansionStatus());
		});

		// Execute
		ValueSet expanded = myTermSvc.expandValueSet(new ValueSetExpansionOptions(), valueSet);
		List<String> codes = expanded.getExpansion().getContains().stream()
			.map(ValueSet.ValueSetExpansionContainsComponent::getCode)
			.collect(Collectors.toList());
		ourLog.info("Expanded ValueSet contains {} codes: {}", codes.size(), codes);

		// Verify: with allowDatabaseValidationOverride=true the expansion must reflect the database-stored R5 backport (3 codes), not the built-in R4 definition (17 codes)
		assertThat(codes).containsExactlyInAnyOrder("group", "display", "question");
	}

	/**
	 * Mirrors the {@code item-type} CodeSystem shipped by {@code hl7.fhir.uv.xver-r5.r4#0.1.0}: same
	 * canonical URL as the built-in R4 definition, but with the R5 code set, where {@code choice} no
	 * longer exists.
	 */
	private void createR5ItemTypeBackportCodeSystem() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(CORE_ITEM_TYPE_SYSTEM);
		codeSystem.setVersion("5.0.0");
		codeSystem.setName("QuestionnaireItemTypeR5Backport");
		codeSystem.setStatus(Enumerations.PublicationStatus.ACTIVE);
		codeSystem.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		codeSystem.addConcept().setCode("group").setDisplay("Group");
		codeSystem.addConcept().setCode("display").setDisplay("Display");
		codeSystem.addConcept().setCode("question").setDisplay("Question");
		myCodeSystemDao.create(codeSystem, mySrd);
		myTerminologyDeferredStorageSvc.saveAllDeferred();
	}
}
