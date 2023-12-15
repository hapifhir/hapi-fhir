package ca.uhn.fhir.jpa.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.interceptor.validation.IRepositoryValidatingRule;
import ca.uhn.fhir.jpa.interceptor.validation.RepositoryValidatingInterceptor;
import ca.uhn.fhir.jpa.interceptor.validation.RepositoryValidatingRuleBuilder;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;
import org.hl7.fhir.common.hapi.validation.support.CachingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.PrePopulatedValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.SnapshotGeneratingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r5.utils.validation.constants.BestPracticeWarningLevel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ResourceValidationR4Test extends BaseJpaR4Test {
	private static final FhirContext ourCtx = FhirContext.forR4();
	private static FhirValidator myFhirValidator;
	private static PrePopulatedValidationSupport ourValidationSupport;

	private static final String myResourceType = "Patient";
	private static final String myResourceTypeDefinition = "http://hl7.org/fhir/StructureDefinition/Patient";
	private static final String PATIENT_STRUCTURE_DEFINITION_URL = "http://example.org/fhir/StructureDefinition/TestPatient";

	private final RepositoryValidatingInterceptor myRepositoryValidatingInterceptor = new RepositoryValidatingInterceptor(ourCtx, Collections.emptyList());
	@Autowired
	private ApplicationContext myApplicationContext;

	@BeforeAll
	public static void setup() {
		myFhirValidator = ourCtx.newValidator();
		myFhirValidator.setValidateAgainstStandardSchema(false);
		myFhirValidator.setValidateAgainstStandardSchematron(false);

		ourValidationSupport = new PrePopulatedValidationSupport(ourCtx);

		ValidationSupportChain chain = new ValidationSupportChain(
			new DefaultProfileValidationSupport(ourCtx),
			new SnapshotGeneratingValidationSupport(ourCtx),
			new CommonCodeSystemsTerminologyService(ourCtx),
			new InMemoryTerminologyServerValidationSupport(ourCtx),
			ourValidationSupport);
		CachingValidationSupport myValidationSupport = new CachingValidationSupport(chain, true);
		FhirInstanceValidator myInstanceVal = new FhirInstanceValidator(myValidationSupport);
		myFhirValidator.registerValidatorModule(myInstanceVal);
	}

	@BeforeEach
	public void before() {
		myInterceptorRegistry.registerInterceptor(myRepositoryValidatingInterceptor);
	}

	@AfterEach
	public void after() {
		myInterceptorRegistry.unregisterInterceptor(myRepositoryValidatingInterceptor);
	}

	@Test
	public void testCreateStructureDefinition_createMultipleWithWithSameUrl_isStoredSuccessfully() {
		createPatientStructureDefinitionWithMandatoryField(PATIENT_STRUCTURE_DEFINITION_URL, "1", "Patient.identifier");
		createPatientStructureDefinitionWithMandatoryField(PATIENT_STRUCTURE_DEFINITION_URL, "2", "Patient.name");
		createPatientStructureDefinitionWithMandatoryField(PATIENT_STRUCTURE_DEFINITION_URL, "3", "Patient.birthdate");
	}

	@Test
	public void testValidatePatient_withProfileIncludingVersion_shouldUseSpecifiedVersion() {
		createPatientStructureDefinitionWithMandatoryField(PATIENT_STRUCTURE_DEFINITION_URL, "1", "Patient.identifier");
		createPatientStructureDefinitionWithMandatoryField(PATIENT_STRUCTURE_DEFINITION_URL, "2", "Patient.name");

		Patient patient = new Patient();
		patient.getMeta().addProfile(PATIENT_STRUCTURE_DEFINITION_URL + "|1");
		myPatientDao.create(patient, mySrd);

		ValidationResult validationResult = myFhirValidator.validateWithResult(patient);
		assertFalse(validationResult.isSuccessful());
		assertEquals(1, validationResult.getMessages().size());

		SingleValidationMessage message = validationResult.getMessages().iterator().next();
		assertEquals("Patient.identifier: minimum required = 1, but only found 0 (from " + PATIENT_STRUCTURE_DEFINITION_URL + "|1)", message.getMessage());
	}

	@Test
	public void testValidatePatient_withProfileNoVersion_shouldUsesLatestVersion() {
		createPatientStructureDefinitionWithMandatoryField(PATIENT_STRUCTURE_DEFINITION_URL, "1", "Patient.identifier");
		createPatientStructureDefinitionWithMandatoryField(PATIENT_STRUCTURE_DEFINITION_URL, "2", "Patient.name");

		Patient patient = new Patient();
		patient.getMeta().addProfile(PATIENT_STRUCTURE_DEFINITION_URL);
		myPatientDao.create(patient, mySrd);

		ValidationResult validationResult = myFhirValidator.validateWithResult(patient);
		assertFalse(validationResult.isSuccessful());
		assertEquals(1, validationResult.getMessages().size());

		SingleValidationMessage message = validationResult.getMessages().iterator().next();
		assertEquals("Patient.name: minimum required = 1, but only found 0 (from " + PATIENT_STRUCTURE_DEFINITION_URL + "|2)", message.getMessage());
	}

	@Test
	public void testValidatePatient_withMultipleProfilesDifferentUrls_validatesAgainstAllProfiles() {
		final String sdIdentifier = PATIENT_STRUCTURE_DEFINITION_URL + "-identifier";
		final String sdName = PATIENT_STRUCTURE_DEFINITION_URL + "-name";
		createPatientStructureDefinitionWithMandatoryField(sdIdentifier, "1", "Patient.identifier");
		createPatientStructureDefinitionWithMandatoryField(sdName, "1", "Patient.name");

		Patient patient = new Patient();
		patient.getMeta().addProfile(sdIdentifier).addProfile(sdName);
		myPatientDao.create(patient, mySrd);

		ValidationResult validationResult = myFhirValidator.validateWithResult(patient);
		assertFalse(validationResult.isSuccessful());
		assertEquals(2, validationResult.getMessages().size());

		Iterator<SingleValidationMessage> messageIterator = validationResult.getMessages().iterator();
		assertEquals("Patient.identifier: minimum required = 1, but only found 0 (from " + PATIENT_STRUCTURE_DEFINITION_URL + "-identifier|1)", messageIterator.next().getMessage());
		assertEquals("Patient.name: minimum required = 1, but only found 0 (from " + PATIENT_STRUCTURE_DEFINITION_URL + "-name|1)", messageIterator.next().getMessage());
	}

	@Test
	public void testStructureDefinition_createResourceWithMultipleProfilesSameStructureDefinition_usesFirstVersion() {
		createPatientStructureDefinitionWithMandatoryField(PATIENT_STRUCTURE_DEFINITION_URL, "1", "Patient.identifier");
		createPatientStructureDefinitionWithMandatoryField(PATIENT_STRUCTURE_DEFINITION_URL, "2", "Patient.name");
		createPatientStructureDefinitionWithMandatoryField(PATIENT_STRUCTURE_DEFINITION_URL, "3", "Patient.birthDate");

		Patient patient = new Patient();
		patient.getMeta()
			.addProfile(PATIENT_STRUCTURE_DEFINITION_URL + "|2")
			.addProfile(PATIENT_STRUCTURE_DEFINITION_URL + "|1")
			.addProfile(PATIENT_STRUCTURE_DEFINITION_URL + "|3");
		myPatientDao.create(patient, mySrd);

		ValidationResult validationResult = myFhirValidator.validateWithResult(patient);
		assertFalse(validationResult.isSuccessful());
		assertEquals(1, validationResult.getMessages().size());

		SingleValidationMessage message = validationResult.getMessages().iterator().next();
		assertEquals("Patient.identifier: minimum required = 1, but only found 0 (from " + PATIENT_STRUCTURE_DEFINITION_URL + "|1)", message.getMessage());
	}

	@Test
	public void testCreatePatient_withRepositoryValidationRuleNoVersionProfileNoVersion_validatesAgainstFirstVersion() {
		createPatientStructureDefinitionWithMandatoryField(PATIENT_STRUCTURE_DEFINITION_URL, "1", "Patient.identifier");
		createPatientStructureDefinitionWithMandatoryField(PATIENT_STRUCTURE_DEFINITION_URL, "2", "Patient.name");

		setupRepositoryValidationRules(PATIENT_STRUCTURE_DEFINITION_URL);

		Patient patient = new Patient();
		patient.getMeta().addProfile(PATIENT_STRUCTURE_DEFINITION_URL);
		try {
			myPatientDao.create(patient, mySrd);
			fail();
		} catch (PreconditionFailedException e) {
			ourLog.info(e.getMessage());
			assertEquals(Msg.code(574) + "Patient.identifier: minimum required = 1, but only found 0 (from " + PATIENT_STRUCTURE_DEFINITION_URL + "|1)", e.getMessage());
		}
	}

	@Test
	public void testCreatePatient_withRepositoryValidationRuleWithVersionProfileWithVersion_unknownProfile() {
		createPatientStructureDefinitionWithMandatoryField(PATIENT_STRUCTURE_DEFINITION_URL, "1", "Patient.identifier");
		createPatientStructureDefinitionWithMandatoryField(PATIENT_STRUCTURE_DEFINITION_URL, "2", "Patient.name");

		setupRepositoryValidationRules(PATIENT_STRUCTURE_DEFINITION_URL + "|1");

		Patient patient = new Patient();
		patient.getMeta().addProfile(PATIENT_STRUCTURE_DEFINITION_URL + "|1");
		try {
			myPatientDao.create(patient, mySrd);
			fail();
		} catch (PreconditionFailedException e) {
			assertEquals(Msg.code(574) + "Profile reference '" + PATIENT_STRUCTURE_DEFINITION_URL + "|1' has not been checked because it is unknown, and the validator is set to not fetch unknown profiles", e.getMessage());
		}
	}

	@Test
	public void testCreatePatient_withRepositoryValidationRuleNoVersionProfileWithVersion_doesNotConform() {
		createPatientStructureDefinitionWithMandatoryField(PATIENT_STRUCTURE_DEFINITION_URL, "1", "Patient.identifier");
		createPatientStructureDefinitionWithMandatoryField(PATIENT_STRUCTURE_DEFINITION_URL, "2", "Patient.name");

		setupRepositoryValidationRules(PATIENT_STRUCTURE_DEFINITION_URL);

		Patient patient = new Patient();
		patient.getMeta().addProfile(PATIENT_STRUCTURE_DEFINITION_URL + "|1");
		try {
			myPatientDao.create(patient, mySrd);
			fail();
		} catch (PreconditionFailedException e) {
			assertEquals(Msg.code(575) + "Resource of type \"" + myResourceType + "\" does not declare conformance to profile from: [" + PATIENT_STRUCTURE_DEFINITION_URL + "]", e.getMessage());
		}
	}

	@Test
	public void testCreatePatient_withRepositoryValidationRuleWithVersionProfileWithoutVersion_doesNotConform() {
		createPatientStructureDefinitionWithMandatoryField(PATIENT_STRUCTURE_DEFINITION_URL, "1", "Patient.identifier");
		createPatientStructureDefinitionWithMandatoryField(PATIENT_STRUCTURE_DEFINITION_URL, "2", "Patient.name");

		setupRepositoryValidationRules(PATIENT_STRUCTURE_DEFINITION_URL + "|1");

		Patient patient = new Patient();
		patient.getMeta().addProfile(PATIENT_STRUCTURE_DEFINITION_URL);
		try {
			myPatientDao.create(patient, mySrd);
			fail();
		} catch (PreconditionFailedException e) {
			assertEquals(Msg.code(575) + "Resource of type \"" + myResourceType + "\" does not declare conformance to profile from: [" + PATIENT_STRUCTURE_DEFINITION_URL + "|1]", e.getMessage());
		}
	}

	private void createPatientStructureDefinitionWithMandatoryField(String theUrl, String theVersion, String thePath) {
		StructureDefinition sd = new StructureDefinition()
			.setUrl(theUrl).setVersion(theVersion)
			.setBaseDefinition(myResourceTypeDefinition)
			.setType(myResourceType)
			.setDerivation(StructureDefinition.TypeDerivationRule.CONSTRAINT);
		sd.getDifferential().addElement()
			.setPath(thePath)
			.setMin(1)
			.setId(thePath);

		DaoMethodOutcome outcome = myStructureDefinitionDao.create(sd, mySrd);
		assertTrue(outcome.getCreated());
		ourValidationSupport.addStructureDefinition(sd);
	}

	private void setupRepositoryValidationRules(String... theProfiles) {
		List<IRepositoryValidatingRule> rules = myApplicationContext.getBean(RepositoryValidatingRuleBuilder.REPOSITORY_VALIDATING_RULE_BUILDER, RepositoryValidatingRuleBuilder.class)
			.forResourcesOfType(myResourceType)
			.requireAtLeastOneProfileOf(theProfiles)
			.and()
			.requireValidationToDeclaredProfiles()
			.withBestPracticeWarningLevel(BestPracticeWarningLevel.Ignore)
			.rejectOnSeverity("error")
			.build();

		myRepositoryValidatingInterceptor.setRules(rules);
	}
}
