package org.hl7.fhir.common.hapi.validation.validator;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.IValidationContext;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationContext;
import ca.uhn.fhir.validation.ValidationOptions;
import ca.uhn.fhir.validation.ValidationResult;
import jakarta.annotation.Nullable;
import org.hl7.fhir.common.hapi.validation.support.PrePopulatedValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r5.fhirpath.FHIRPathEngine;
import org.hl7.fhir.r5.model.ElementDefinition;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.Practitioner;
import org.hl7.fhir.r5.model.Reference;
import org.hl7.fhir.r5.model.StructureDefinition;
import org.hl7.fhir.r5.utils.validation.constants.BestPracticeWarningLevel;
import org.hl7.fhir.utilities.validation.ValidationMessage;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class HostApplicationServicesTest {

	private static final String PROFILE_URL = "http://example.org/fhir/StructureDefinition/HostAwarePatient";

	private final FhirContext myCtx = FhirContext.forR5Cached();
	private final DefaultProfileValidationSupport myDefaultSupport = new DefaultProfileValidationSupport(myCtx);

	@Test
	void testFhirInstanceValidatorUsesHostApplicationServices() {
		StructureDefinition profile = createHostAwarePatientProfile();
		ValidationSupportChain validationSupport = buildValidationSupport(profile);
		Patient patient = newPatientWithProfile();

		ValidationResult withoutHost = runValidationWithHost(validationSupport, patient, null);
		assertThat(withoutHost.isSuccessful()).isFalse();
		assertThat(extractMessages(withoutHost)).anyMatch(msg -> msg.contains("host-1") || msg.contains("resolve"));

		RecordingHostServices hostServices = new RecordingHostServices();
		ValidationResult withHost = runValidationWithHost(validationSupport, patient, hostServices);

		assertThat(hostServices.wasResolveReferenceCalled()).isTrue();
		assertThat(withHost.isSuccessful()).isTrue();
	}

	@Test
	void testValidatorWrapperAcceptsHostApplicationServices() {
		StructureDefinition profile = createHostAwarePatientProfile();
		ValidationSupportChain validationSupport = buildValidationSupport(profile);
		Patient patient = newPatientWithProfile();

		WorkerContextValidationSupportAdapter workerContext =
			WorkerContextValidationSupportAdapter.newVersionSpecificWorkerContextWrapper(validationSupport);
		workerContext.setExpansionParameters(new org.hl7.fhir.r5.model.Parameters());

		RecordingHostServices hostServices = new RecordingHostServices();
		ValidatorWrapper validatorWrapper = new ValidatorWrapper()
			.setAnyExtensionsAllowed(true)
			.setErrorForUnknownProfiles(true)
			.setExtensionDomains(Collections.emptyList())
			.setValidationPolicyAdvisor(new FhirDefaultPolicyAdvisor())
			.setBestPracticeWarningLevel(BestPracticeWarningLevel.Hint)
			.setHostApplicationServices(hostServices);

		IValidationContext<IBaseResource> validationContext =
			ValidationContext.forResource(myCtx, patient, ValidationOptions.empty());

		List<ValidationMessage> messages = validatorWrapper.validate(workerContext, validationContext);

		assertThat(hostServices.wasResolveReferenceCalled()).isTrue();
		assertThat(messages)
			.noneMatch(message -> message.getLevel() == ValidationMessage.IssueSeverity.ERROR
				|| message.getLevel() == ValidationMessage.IssueSeverity.FATAL);
	}

	private ValidationResult runValidationWithHost(
		ValidationSupportChain validationSupport,
		Patient patient,
		@Nullable RecordingHostServices hostServices) {
		FhirInstanceValidator module = new FhirInstanceValidator(validationSupport);
		if (hostServices != null) {
			module.setHostApplicationServices(hostServices);
		}

		FhirValidator validator = myCtx.newValidator();
		validator.setValidateAgainstStandardSchema(false);
		validator.setValidateAgainstStandardSchematron(false);
		validator.registerValidatorModule(module);
		return validator.validateWithResult(patient);
	}

	private Patient newPatientWithProfile() {
		Patient patient = new Patient();
		patient.getMeta().addProfile(PROFILE_URL);
		patient.addGeneralPractitioner(new Reference("Practitioner/hosted"));
		return patient;
	}

	private ValidationSupportChain buildValidationSupport(StructureDefinition profile) {
		PrePopulatedValidationSupport prePopulatedValidationSupport = new PrePopulatedValidationSupport(myCtx);
		prePopulatedValidationSupport.addStructureDefinition(profile);

		return new ValidationSupportChain(prePopulatedValidationSupport, myDefaultSupport);
	}

	private StructureDefinition createHostAwarePatientProfile() {
		StructureDefinition basePatient =
			(StructureDefinition) myDefaultSupport.fetchStructureDefinition("http://hl7.org/fhir/StructureDefinition/Patient");

		StructureDefinition profile = basePatient.copy();
		profile.setId("HostAwarePatient");
		profile.setUrl(PROFILE_URL);
		profile.setName("HostAwarePatient");
		profile.setTitle("HostAwarePatient");
		profile.setVersion("1.0.0");
		profile.setStatus(Enumerations.PublicationStatus.ACTIVE);
		profile.setKind(StructureDefinition.StructureDefinitionKind.RESOURCE);
		profile.setBaseDefinition(basePatient.getUrl());
		profile.setDerivation(StructureDefinition.TypeDerivationRule.CONSTRAINT);

		ElementDefinition.ElementDefinitionConstraintComponent constraint =
			new ElementDefinition.ElementDefinitionConstraintComponent();
		constraint.setKey("host-1");
		constraint.setSeverity(ElementDefinition.ConstraintSeverity.ERROR);
		constraint.setHuman("General practitioner must resolve through host services");
		constraint.setExpression("generalPractitioner.resolve().exists()");

		profile.getSnapshot().getElementFirstRep().addConstraint(constraint);
		profile.getDifferential().getElementFirstRep().addConstraint(constraint.copy());

		return profile;
	}

	private List<String> extractMessages(ValidationResult validationResult) {
		return validationResult.getMessages().stream()
			.map(SingleValidationMessage::getMessage)
			.collect(Collectors.toList());
	}

	private static class RecordingHostServices extends FhirInstanceValidator.NullEvaluationContext {
		private final AtomicBoolean myResolveCalled = new AtomicBoolean(false);

		@Override
		public org.hl7.fhir.r5.model.Base resolveReference(
			FHIRPathEngine engine, Object appContext, String url, org.hl7.fhir.r5.model.Base refContext)
			throws FHIRException {
			myResolveCalled.set(true);
			return new Practitioner().setId(url);
		}

		boolean wasResolveReferenceCalled() {
			return myResolveCalled.get();
		}
	}
}

