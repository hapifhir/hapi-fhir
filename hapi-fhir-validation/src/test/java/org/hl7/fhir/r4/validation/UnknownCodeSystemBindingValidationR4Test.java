package org.hl7.fhir.r4.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.PrePopulatedValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.SnapshotGeneratingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.r4.model.ElementDefinition;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Instance validation of a code bound to a ValueSet that enumerates codes from a CodeSystem no module knows. The
 * terminology layer reports the code system as not found (#8415); the HL7 validator then grades that finding by
 * the binding strength, and it must be reported once, not alongside a second "not in the value set" finding.
 */
// Created by Claude Opus 5.5
class UnknownCodeSystemBindingValidationR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(UnknownCodeSystemBindingValidationR4Test.class);
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private static final String PROFILE_URL = "http://example.com/StructureDefinition/observation-unknown-cs";
	private static final String VS_URL = "http://example.com/ValueSet/unknown-cs-codes";
	private static final String UNKNOWN_CS_URL = "http://example.com/CodeSystem/unknown";

	@ParameterizedTest
	@CsvSource({"REQUIRED, ERROR", "EXTENSIBLE, WARNING"})
	void validate_codeFromAnUnknownCodeSystem_isReportedOnceAtTheSeverityTheBindingStrengthGives(
			Enumerations.BindingStrength theStrength, ResultSeverityEnum theExpectedSeverity) {
		// Setup
		FhirValidator validator = newValidator(theStrength);
		Observation observation = new Observation();
		observation.getMeta().addProfile(PROFILE_URL);
		observation.setStatus(Observation.ObservationStatus.FINAL);
		observation.getCode().addCoding().setSystem(UNKNOWN_CS_URL).setCode("code1");

		// Test
		ValidationResult result = validator.validateWithResult(observation);

		// Verify
		List<SingleValidationMessage> codeMessages = result.getMessages().stream()
				.peek(t -> ourLog.info("{} - {} - {}", t.getSeverity(), t.getLocationString(), t.getMessage()))
				.filter(t -> t.getLocationString().startsWith("Observation.code"))
				.filter(t -> t.getSeverity().ordinal() >= ResultSeverityEnum.WARNING.ordinal())
				.toList();
		assertThat(codeMessages).hasSize(1);
		assertThat(codeMessages.get(0).getSeverity()).isEqualTo(theExpectedSeverity);
		assertThat(codeMessages.get(0).getMessage()).contains(UNKNOWN_CS_URL);
	}

	private static FhirValidator newValidator(Enumerations.BindingStrength theStrength) {
		ValueSet vs = new ValueSet();
		vs.setUrl(VS_URL);
		vs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		vs.getCompose().addInclude().setSystem(UNKNOWN_CS_URL).addConcept().setCode("code1");

		StructureDefinition profile = new StructureDefinition();
		profile.setUrl(PROFILE_URL);
		profile.setName("ObservationUnknownCs");
		profile.setStatus(Enumerations.PublicationStatus.ACTIVE);
		profile.setKind(StructureDefinition.StructureDefinitionKind.RESOURCE);
		profile.setAbstract(false);
		profile.setType("Observation");
		profile.setBaseDefinition("http://hl7.org/fhir/StructureDefinition/Observation");
		profile.setDerivation(StructureDefinition.TypeDerivationRule.CONSTRAINT);
		ElementDefinition code = profile.getDifferential().addElement().setPath("Observation.code");
		code.setId("Observation.code");
		code.getBinding().setStrength(theStrength).setValueSet(VS_URL);

		PrePopulatedValidationSupport prePopulated = new PrePopulatedValidationSupport(ourCtx);
		prePopulated.addValueSet(vs);
		prePopulated.addStructureDefinition(profile);

		ValidationSupportChain chain = new ValidationSupportChain(
				new DefaultProfileValidationSupport(ourCtx),
				prePopulated,
				new CommonCodeSystemsTerminologyService(ourCtx),
				new InMemoryTerminologyServerValidationSupport(ourCtx),
				new SnapshotGeneratingValidationSupport(ourCtx));

		FhirValidator validator = ourCtx.newValidator();
		validator.registerValidatorModule(new FhirInstanceValidator(chain));
		return validator;
	}
}
