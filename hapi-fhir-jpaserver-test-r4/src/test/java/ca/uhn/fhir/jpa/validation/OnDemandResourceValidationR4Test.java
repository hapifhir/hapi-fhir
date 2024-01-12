package ca.uhn.fhir.jpa.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
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
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OnDemandResourceValidationR4Test extends BaseResourceProviderR4Test {
	private static final FhirContext ourCtx = FhirContext.forR4();
	private static final String myResourceType = "Patient";
	private final String myProfile = "http://example.org/fhir/StructureDefinition/TestPatient";

	@Test
	public void testCreateStructureDefinition_createMultipleWithWithSameUrl_isStoredSuccessfully() {
		createProfile("1", "Patient.identifier");
		createProfile("2", "Patient.name");
		createProfile("3", "Patient.birthdate");
	}

	private static FhirValidator myFhirValidator;
	private static PrePopulatedValidationSupport ourValidationSupport;

	@BeforeEach
	public void before() {
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
	public void testValidatePatient_withProfileNotRegistered_unknownProfile() {
		createProfile(myProfile, "1", "Patient.identifier");

		IIdType idType = createPatient(withProfile(myProfile));
		Patient patient = myPatientDao.read(idType, mySrd);

		ValidationResult validationResult = myFhirValidator.validateWithResult(patient);
		assertFalse(validationResult.isSuccessful());
		assertEquals(1, validationResult.getMessages().size());

		SingleValidationMessage message = validationResult.getMessages().iterator().next();
		assertEquals("Profile reference '" + myProfile + "' has not been checked because it is unknown", message.getMessage());
	}

	@Test
	public void testValidatePatient_withProfileNoVersion_shouldUsesLatestVersion() {
		createAndRegisterProfile(myProfile, "1", "Patient.identifier");
		createAndRegisterProfile(myProfile, "2", "Patient.name");

		IIdType idType = createPatient(withProfile(myProfile));
		Patient patient = myPatientDao.read(idType, mySrd);

		ValidationResult validationResult = myFhirValidator.validateWithResult(patient);
		assertFalse(validationResult.isSuccessful());
		assertEquals(1, validationResult.getMessages().size());

		SingleValidationMessage message = validationResult.getMessages().iterator().next();
		assertEquals("Patient.name: minimum required = 1, but only found 0 (from " + myProfile + "|2)", message.getMessage());
	}

	@Test
	public void testValidatePatient_withProfileWithVersion_shouldUseSpecifiedVersion() {

		createAndRegisterProfile(myProfile, "1", "Patient.identifier");
		createAndRegisterProfile(myProfile, "2", "Patient.name");

		IIdType idType = createPatient(withProfile(myProfile + "|1"));
		Patient patient = myPatientDao.read(idType, mySrd);

		ValidationResult validationResult = myFhirValidator.validateWithResult(patient);
		assertFalse(validationResult.isSuccessful());
		assertEquals(1, validationResult.getMessages().size());

		SingleValidationMessage message = validationResult.getMessages().iterator().next();
		assertEquals("Patient.identifier: minimum required = 1, but only found 0 (from " + myProfile + "|1)", message.getMessage());
	}

	@Test
	public void testValidatePatient_withMultipleProfiles_validatesAgainstAllProfiles() {
		final String sdIdentifier = myProfile + "-identifier";
		final String sdName = myProfile + "-name";

		createAndRegisterProfile(sdIdentifier, "1", "Patient.identifier");
		createAndRegisterProfile(sdName, "1", "Patient.name");

		IIdType idType = createPatient(withProfile(sdIdentifier), withProfile(sdName));
		Patient patient = myPatientDao.read(idType, mySrd);

		ValidationResult validationResult = myFhirValidator.validateWithResult(patient);
		assertFalse(validationResult.isSuccessful());
		assertEquals(2, validationResult.getMessages().size());

		Iterator<SingleValidationMessage> messageIterator = validationResult.getMessages().iterator();
		assertEquals("Patient.identifier: minimum required = 1, but only found 0 (from " + myProfile + "-identifier|1)", messageIterator.next().getMessage());
		assertEquals("Patient.name: minimum required = 1, but only found 0 (from " + myProfile + "-name|1)", messageIterator.next().getMessage());
	}

	@Test
	public void testValidatePatient_withMultipleVersions_validatesAgainstFirstVersion() {
		createAndRegisterProfile(myProfile, "1", "Patient.identifier");
		createAndRegisterProfile(myProfile, "2", "Patient.name");
		createAndRegisterProfile(myProfile, "3", "Patient.birthDate");

		IIdType idType = createPatient(
			withProfile(myProfile + "|2"),
			withProfile(myProfile + "|1"),
			withProfile(myProfile + "|3"));
		Patient patient = myPatientDao.read(idType, mySrd);

		ValidationResult validationResult = myFhirValidator.validateWithResult(patient);
		assertFalse(validationResult.isSuccessful());
		assertEquals(1, validationResult.getMessages().size());

		SingleValidationMessage message = validationResult.getMessages().iterator().next();
		assertEquals("Patient.identifier: minimum required = 1, but only found 0 (from " + myProfile + "|1)", message.getMessage());
	}

	private void createAndRegisterProfile(String theUrl, String theVersion, String thePath) {
		StructureDefinition sd = createProfile(theUrl, theVersion, thePath);
		ourValidationSupport.addStructureDefinition(sd);
	}

	private void createProfile(String theVersion, String theRequiredPath) {
		createProfile(myProfile, theVersion, theRequiredPath);
	}

	private StructureDefinition createProfile(String theUrl, String theVersion, String theRequiredPath) {
		final String myResourceTypeDefinition = "http://hl7.org/fhir/StructureDefinition/Patient";
		final String myProfileId = "TestProfile";

		StructureDefinition sd = new StructureDefinition()
			.setUrl(theUrl).setVersion(theVersion)
			.setBaseDefinition(myResourceTypeDefinition)
			.setType(myResourceType)
			.setDerivation(StructureDefinition.TypeDerivationRule.CONSTRAINT);
		sd.setId(myProfileId);
		sd.getDifferential().addElement()
			.setPath(theRequiredPath)
			.setMin(1)
			.setId(theRequiredPath);

		DaoMethodOutcome outcome = myStructureDefinitionDao.update(sd, mySrd);
		assertNotNull(outcome.getResource());
		return (StructureDefinition) outcome.getResource();
	}
}
