package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport.CodeValidationResult;
import ca.uhn.fhir.context.support.ValidateCodeRequest;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

// Created by Claude Opus 5.5
@SuppressWarnings("deprecation")
class UnknownCodeSystemWarningValidationSupportTest {

	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private static final String CS_URL = "http://example.org/cs";
	private static final String UNKNOWN_CS_URL = "http://example.org/unknown-cs";
	private static final String VS_URL = "http://example.org/vs";

	private UnknownCodeSystemWarningValidationSupport mySvc;
	private ValidationSupportChain myChain;

	@BeforeEach
	void beforeEach() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(CS_URL);
		codeSystem.setVersion("1.0");
		codeSystem.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		codeSystem.setStatus(Enumerations.PublicationStatus.ACTIVE);
		codeSystem.addConcept().setCode("a");
		PrePopulatedValidationSupport prePopulated = new PrePopulatedValidationSupport(ourCtx);
		prePopulated.addCodeSystem(codeSystem);

		ValueSet valueSet = new ValueSet();
		valueSet.setUrl(VS_URL);
		valueSet.setStatus(Enumerations.PublicationStatus.ACTIVE);
		valueSet.getCompose().addInclude().setSystem(CS_URL).setVersion("2.0").addConcept().setCode("a");
		prePopulated.addValueSet(valueSet);

		mySvc = new UnknownCodeSystemWarningValidationSupport(ourCtx);
		mySvc.setNonExistentCodeSystemSeverity(IValidationSupport.IssueSeverity.WARNING);
		myChain = new ValidationSupportChain(
				prePopulated, new InMemoryTerminologyServerValidationSupport(ourCtx), mySvc);
	}

	@Test
	void validateCode_unknownCodeSystem_warns() {
		CodeValidationResult result = validateCode(UNKNOWN_CS_URL, null);

		assertThat(result.getSeverity()).isEqualTo(IValidationSupport.IssueSeverity.WARNING);
		assertThat(result.getMessage()).startsWith("CodeSystem is unknown and can't be validated: " + UNKNOWN_CS_URL);
	}

	@Test
	void validateCode_versionNotStoredOfAKnownCodeSystem_reportsTheVersionAsAnError() {
		CodeValidationResult result = validateCode(CS_URL, "2.0");

		assertThat(result.isOk()).isFalse();
		assertThat(result.getSeverity()).isEqualTo(IValidationSupport.IssueSeverity.ERROR);
		assertThat(result.getMessage())
				.isEqualTo("A definition for CodeSystem '" + CS_URL
						+ "' version '2.0' could not be found, so the code cannot be validated");
	}

	@Test
	void validateCode_versionNotStoredOfAKnownCodeSystemPackedInTheUrl_reportsTheVersionAsAnError() {
		CodeValidationResult result = myChain.validateCode(
				new ValidationSupportContext(myChain), new ConceptValidationOptions(), CS_URL + "|2.0", "a", null, null);

		assertThat(result.isOk()).isFalse();
		assertThat(result.getSeverity()).isEqualTo(IValidationSupport.IssueSeverity.ERROR);
		assertThat(result.getMessage()).contains("version '2.0' could not be found");
	}

	@Test
	void isCodeSystemSupported_versionNotStoredOfAKnownCodeSystem_isFalse() {
		ValidationSupportContext context = new ValidationSupportContext(myChain);

		assertThat(mySvc.isCodeSystemSupported(context, CS_URL, "2.0")).isFalse();
		assertThat(mySvc.isCodeSystemSupported(context, UNKNOWN_CS_URL, "2.0")).isTrue();
	}

	@Test
	void validateCode_valueSetIncludingAVersionNotStoredOfAKnownCodeSystem_rejectsAndNamesTheVersion() {
		CodeValidationResult result = myChain.validateCode(
				new ValidationSupportContext(myChain),
				new ConceptValidationOptions(),
				new ValidateCodeRequest(CS_URL, null, "a", null, VS_URL));

		assertThat(result.isOk()).isFalse();
		assertThat(result.getUnknownSystems()).containsExactly(CS_URL + "|2.0");
	}

	@Test
	void validateCode_storedVersion_accepts() {
		CodeValidationResult result = validateCode(CS_URL, "1.0");

		assertThat(result.isOk()).isTrue();
	}

	private CodeValidationResult validateCode(String theSystem, String theVersion) {
		return myChain.validateCode(
				new ValidationSupportContext(myChain),
				new ConceptValidationOptions(),
				new ValidateCodeRequest(theSystem, theVersion, "a", null, null));
	}
}
