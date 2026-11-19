package ca.uhn.fhir.jpa.validation;

import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ElementDefinition;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Each test caches a validation answer, writes a {@literal ValueSet} or {@literal CodeSystem}, then asks again.
 * Tests here turn pre-expansion off, because the pre-expansion job clears the cache when it finishes and could
 * hide a stale cached answer.
 */
// Created by Claude Opus 5.5
public class ValidateAfterConformanceResourceWriteR4Test extends BaseJpaR4Test {

	private static final String CS_URL = "http://example.org/fhir/CodeSystem/colour";
	private static final String VS_URL = "http://example.org/fhir/ValueSet/colour";
	private static final String PROFILE_URL = "http://example.org/fhir/StructureDefinition/ColourObservation";
	private static final String CS_ID = "colour-cs";
	private static final String VS_ID = "colour-vs";

	@BeforeEach
	void beforeDisablePreExpansion() {
		myStorageSettings.setPreExpandValueSets(false);
	}

	@AfterEach
	void afterRestorePreExpansion() {
		myStorageSettings.setPreExpandValueSets(new JpaStorageSettings().isPreExpandValueSets());
	}

	@Test
	void validateCode_valueSetUpdatedToSwapItsCode_answersFromTheNewContent() {
		// Setup
		storeCodeSystem("a", "b");
		storeValueSetIncluding("a");
		assertThat(isValidInValueSet("a")).isTrue();
		assertThat(isValidInValueSet("b")).isFalse();

		// Test
		storeValueSetIncluding("b");

		// Verify
		assertThat(isValidInValueSet("a")).isFalse();
		assertThat(isValidInValueSet("b")).isTrue();
	}

	@Test
	void validateCode_valueSetUpdatedInATransaction_answersFromTheNewContent() {
		// Setup
		storeCodeSystem("a", "b");
		storeValueSetIncluding("a");
		assertThat(isValidInValueSet("a")).isTrue();
		assertThat(isValidInValueSet("b")).isFalse();

		// Test
		Bundle transaction = new Bundle().setType(Bundle.BundleType.TRANSACTION);
		transaction
				.addEntry()
				.setResource(valueSetIncluding("b"))
				.getRequest()
				.setMethod(Bundle.HTTPVerb.PUT)
				.setUrl("ValueSet/" + VS_ID);
		mySystemDao.transaction(mySrd, transaction);
		myTerminologyDeferredStorageSvc.saveAllDeferred();

		// Verify
		assertThat(isValidInValueSet("a")).isFalse();
		assertThat(isValidInValueSet("b")).isTrue();
	}

	@Test
	void validateCode_valueSetPatchedToSwapItsCode_answersFromTheNewContent() {
		// Setup
		storeCodeSystem("a", "b");
		storeValueSetIncluding("a");
		assertThat(isValidInValueSet("a")).isTrue();
		assertThat(isValidInValueSet("b")).isFalse();

		// Test
		String patch = "[{\"op\":\"replace\",\"path\":\"/compose/include/0/concept/0/code\",\"value\":\"b\"}]";
		myValueSetDao.patch(new IdType("ValueSet", VS_ID), null, PatchTypeEnum.JSON_PATCH, patch, null, mySrd);
		myTerminologyDeferredStorageSvc.saveAllDeferred();

		// Verify
		assertThat(isValidInValueSet("a")).isFalse();
		assertThat(isValidInValueSet("b")).isTrue();
	}

	@Test
	void validateCode_valueSetCreatedAfterItsUrlWasAskedFor_answersFromTheNewValueSet() {
		// Setup: the pre-expansion job start looks the new ValueSet up, so a cached "not found" fails the create
		myStorageSettings.setPreExpandValueSets(true);
		storeCodeSystem("a", "b");
		assertThat(isValidInValueSet("a")).isFalse();

		// Test
		storeValueSetIncluding("a");

		// Verify
		assertThat(isValidInValueSet("a")).isTrue();
	}

	@Test
	void validateCode_valueSetDeleted_isNotValid() {
		// Setup
		storeCodeSystem("a", "b");
		storeValueSetIncluding("a");
		assertThat(isValidInValueSet("a")).isTrue();

		// Test
		myValueSetDao.delete(new ValueSet().setId(VS_ID).getIdElement(), mySrd);

		// Verify
		assertThat(isValidInValueSet("a")).isFalse();
	}

	@Test
	void validateCode_codeSystemCreatedAfterItsUrlWasAskedFor_answersFromTheNewCodeSystem() {
		// Setup: the CodeSystem is not stored yet
		storeValueSetIncludingTheWholeCodeSystem();
		assertThat(isValidInValueSet("a")).isFalse();

		// Test
		storeCodeSystem("a", "b");

		// Verify
		assertThat(isValidInValueSet("a")).isTrue();
	}

	@Test
	void validateCode_codeSystemUpdatedToSwapACode_answersFromTheNewContent() {
		// Setup
		storeCodeSystem("a", "b");
		storeValueSetIncludingTheWholeCodeSystem();
		assertThat(isValidInValueSet("a")).isTrue();
		assertThat(isValidInValueSet("c")).isFalse();

		// Test
		storeCodeSystem("b", "c");

		// Verify
		assertThat(isValidInValueSet("a")).isFalse();
		assertThat(isValidInValueSet("c")).isTrue();
	}

	@Test
	void validateCode_codeSystemDeleted_isNotValid() {
		// Setup
		storeCodeSystem("a", "b");
		storeValueSetIncludingTheWholeCodeSystem();
		assertThat(isValidInValueSet("a")).isTrue();

		// Test
		myCodeSystemDao.delete(new CodeSystem().setId(CS_ID).getIdElement(), mySrd);
		myTerminologyDeferredStorageSvc.saveAllDeferred();

		// Verify
		assertThat(isValidInValueSet("a")).isFalse();
	}

	@Test
	void validate_valueSetOfARequiredBindingUpdatedToSwapItsCode_answersFromTheNewContent() {
		// Setup
		storeCodeSystem("a", "b");
		storeValueSetIncluding("a");
		createProfileBoundTo(VS_URL);
		assertHasNoErrors(validateObservationWithCode("a"));
		assertHasErrors(validateObservationWithCode("b"));

		// Test
		storeValueSetIncluding("b");

		// Verify
		assertHasErrors(validateObservationWithCode("a"));
		assertHasNoErrors(validateObservationWithCode("b"));
	}

	private void storeCodeSystem(String... theCodes) {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setId(CS_ID);
		codeSystem.setUrl(CS_URL);
		codeSystem.setStatus(Enumerations.PublicationStatus.ACTIVE);
		codeSystem.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		for (String code : theCodes) {
			codeSystem.addConcept().setCode(code).setDisplay(code);
		}
		myCodeSystemDao.update(codeSystem, mySrd);
		myTerminologyDeferredStorageSvc.saveAllDeferred();
	}

	private void storeValueSetIncluding(String theCode) {
		myValueSetDao.update(valueSetIncluding(theCode), mySrd);
		myTerminologyDeferredStorageSvc.saveAllDeferred();
	}

	private void storeValueSetIncludingTheWholeCodeSystem() {
		ValueSet valueSet = newValueSet();
		valueSet.getCompose().addInclude().setSystem(CS_URL);
		myValueSetDao.update(valueSet, mySrd);
		myTerminologyDeferredStorageSvc.saveAllDeferred();
	}

	private ValueSet valueSetIncluding(String theCode) {
		ValueSet valueSet = newValueSet();
		valueSet.getCompose().addInclude().setSystem(CS_URL).addConcept().setCode(theCode);
		return valueSet;
	}

	private ValueSet newValueSet() {
		ValueSet valueSet = new ValueSet();
		valueSet.setId(VS_ID);
		valueSet.setUrl(VS_URL);
		valueSet.setStatus(Enumerations.PublicationStatus.ACTIVE);
		return valueSet;
	}

	private boolean isValidInValueSet(String theCode) {
		IValidationSupport.CodeValidationResult result = myValidationSupport.validateCode(
				new ValidationSupportContext(myValidationSupport),
				new ConceptValidationOptions(),
				CS_URL,
				theCode,
				null,
				VS_URL);
		return result != null && result.isOk();
	}

	private void createProfileBoundTo(String theValueSetUrl) {
		StructureDefinition profile = new StructureDefinition();
		profile.setUrl(PROFILE_URL);
		profile.setName("ColourObservation");
		profile.setStatus(Enumerations.PublicationStatus.ACTIVE);
		profile.setFhirVersion(Enumerations.FHIRVersion._4_0_1);
		profile.setKind(StructureDefinition.StructureDefinitionKind.RESOURCE);
		profile.setAbstract(false);
		profile.setType("Observation");
		profile.setBaseDefinition("http://hl7.org/fhir/StructureDefinition/Observation");
		profile.setDerivation(StructureDefinition.TypeDerivationRule.CONSTRAINT);

		ElementDefinition root = profile.getDifferential().addElement();
		root.setId("Observation");
		root.setPath("Observation");

		ElementDefinition code = profile.getDifferential().addElement();
		code.setId("Observation.code");
		code.setPath("Observation.code");
		code.getBinding().setStrength(Enumerations.BindingStrength.REQUIRED).setValueSet(theValueSetUrl);

		myStructureDefinitionDao.create(profile, mySrd);
	}

	private OperationOutcome validateObservationWithCode(String theCode) {
		Observation observation = new Observation();
		observation.getMeta().addProfile(PROFILE_URL);
		observation.setStatus(Observation.ObservationStatus.FINAL);
		observation.getCode().addCoding().setSystem(CS_URL).setCode(theCode).setDisplay(theCode);
		try {
			MethodOutcome outcome =
					myObservationDao.validate(observation, null, null, null, ValidationModeEnum.CREATE, null, mySrd);
			return (OperationOutcome) outcome.getOperationOutcome();
		} catch (PreconditionFailedException e) {
			return (OperationOutcome) e.getOperationOutcome();
		}
	}
}
