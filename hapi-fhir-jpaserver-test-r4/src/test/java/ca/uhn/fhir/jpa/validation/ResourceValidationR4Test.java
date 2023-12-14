package ca.uhn.fhir.jpa.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResourceValidationR4Test extends BaseJpaR4Test {
	private static final FhirContext ourCtx = FhirContext.forR4();
	private static FhirValidator myFhirValidator;
	private static PrePopulatedValidationSupport ourValidationSupport;
	private static final String PATIENT_STRUCTURE_DEFINITION_URL = "http://example.org/fhir/StructureDefinition/TestPatient";

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
		assertTrue(message.getMessage().contains( PATIENT_STRUCTURE_DEFINITION_URL + "|1"));
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
		assertTrue(message.getMessage().contains(PATIENT_STRUCTURE_DEFINITION_URL + "|2"));
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
		assertTrue(message.getMessage().contains(PATIENT_STRUCTURE_DEFINITION_URL + "|1"));
	}

	private void createPatientStructureDefinitionWithMandatoryField(String theUrl, String theVersion, String thePath) {
		StructureDefinition sd = new StructureDefinition()
			.setUrl(theUrl).setVersion(theVersion)
			.setBaseDefinition("http://hl7.org/fhir/StructureDefinition/Patient")
			.setType("Patient")
			.setDerivation(StructureDefinition.TypeDerivationRule.CONSTRAINT);
		sd.getDifferential().addElement()
			.setPath(thePath)
			.setMin(1)
			.setId(thePath);

		DaoMethodOutcome outcome = myStructureDefinitionDao.create(sd, mySrd);
		assertTrue(outcome.getCreated());
		ourValidationSupport.addStructureDefinition(sd);
	}
}
