package ca.uhn.fhir.jpa.validation;

import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.interceptor.RequestValidatingInterceptor;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import org.hl7.fhir.common.hapi.validation.support.CachingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.PrePopulatedValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.SnapshotGeneratingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Narrative;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

public class EndpointResourceValidationR4Test extends BaseResourceProviderR4Test {
	private static final String myResourceType = "Patient";
	private final String myProfile = "http://example.org/fhir/StructureDefinition/TestPatient";
	private PrePopulatedValidationSupport myPrePopulatedValidationSupport;

	@BeforeEach
	public void before() {
		CachingValidationSupport myValidationSupport = createCachingValidationSupport();
		FhirInstanceValidator fhirInstanceValidator = new FhirInstanceValidator(myValidationSupport);
		RequestValidatingInterceptor interceptor = new RequestValidatingInterceptor();
		interceptor.addValidatorModule(fhirInstanceValidator);
		interceptor.setFailOnSeverity(ResultSeverityEnum.ERROR);
		interceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
		myServer.registerInterceptor(interceptor);
	}

	@Nonnull
	private CachingValidationSupport createCachingValidationSupport() {
		myPrePopulatedValidationSupport = new PrePopulatedValidationSupport(myFhirContext);
		ValidationSupportChain chain = new ValidationSupportChain(
			new DefaultProfileValidationSupport(myFhirContext),
			new SnapshotGeneratingValidationSupport(myFhirContext),
			new CommonCodeSystemsTerminologyService(myFhirContext),
			new InMemoryTerminologyServerValidationSupport(myFhirContext),
			myPrePopulatedValidationSupport);
		return new CachingValidationSupport(chain, true);
	}

	@Test
	public void testCreatePatientRequest_withProfileNotRegistered_profileCouldNotBeFound() {
		createProfile(myProfile, "1", "Patient.identifier");

		// add narrative to remove best practice info validation message
		Narrative narrative = new Narrative();
		narrative.setDivAsString("<div>Some Text</div>");
		narrative.setStatus(Narrative.NarrativeStatus.GENERATED);

		final Patient patient = new Patient();
		patient.setText(narrative);
		patient.setMeta(new Meta().addProfile(myProfile));

		try {
			myClient.create().resource(patient).execute();
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals("HTTP 422 Unprocessable Entity: Profile reference '" + myProfile + "' has not been checked because it could not be found", e.getMessage());
		}
	}

	@Test
	public void testCreatePatientRequest_withProfileNoVersion_throwsExceptionWithLatestVersion() {
		createAndRegisterProfile("1", "Patient.identifier");
		createAndRegisterProfile("2", "Patient.name");

		final Patient patient = new Patient();
		patient.setMeta(new Meta().addProfile(myProfile));

		try {
			myClient.create().resource(patient).execute();
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals("HTTP 422 Unprocessable Entity: Patient.name: minimum required = 1, but only found 0 (from " + myProfile + "|2)", e.getMessage());
		}
	}

	@Test
	public void testCreatePatientRequest_withProfileWithVersion_throwsExceptionWithLatestVersionOfProfile() {
		createAndRegisterProfile("1", "Patient.identifier");
		createAndRegisterProfile("2", "Patient.name");

		final Patient patient = new Patient();
		patient.setMeta(new Meta().addProfile(myProfile + "|1"));

		try {
			myClient.create().resource(patient).execute();
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals("HTTP 422 Unprocessable Entity: Patient.name: minimum required = 1, but only found 0 (from " + myProfile + "|2)", e.getMessage());
		}
	}

	@Test
	public void testCreatePatientRequest_withProfileWithVersion_onlyValidatesUsingLatestProfile() {
		createAndRegisterProfile("1", "Patient.identifier");
		createAndRegisterProfile("2", "Patient.name");

		final Patient patient = new Patient();
		patient.getNameFirstRep().addGiven("John").setFamily("Smith");
		patient.setMeta(new Meta().addProfile(myProfile + "|1"));

		// patient.identifier is not validated from profile 1
		myClient.create().resource(patient).execute();
	}

	@Test
	public void testCreatePatientRequest_withMultipleProfiles_throwsExceptionWithFirstDeclaredProfile() {
		final String sdIdentifier = myProfile + "-identifier";
		final String sdName = myProfile + "-name";

		createAndRegisterProfile(sdIdentifier, "1", "Patient.identifier");
		createAndRegisterProfile(sdName, "1", "Patient.name");

		final Patient patient = new Patient();
		patient.setMeta(new Meta().addProfile(sdIdentifier).addProfile(sdName));

		try {
			myClient.create().resource(patient).execute();
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals("HTTP 422 Unprocessable Entity: Patient.identifier: minimum required = 1, but only found 0 (from " + sdIdentifier + "|1)", e.getMessage());
		}
	}

	@Test
	public void testCreatePatientRequest_withMultipleVersions_throwsExceptionWithLatestProfile() {
		createAndRegisterProfile(myProfile, "1", "Patient.identifier");
		createAndRegisterProfile(myProfile, "2", "Patient.name");
		createAndRegisterProfile(myProfile, "3", "Patient.birthDate");

		final Patient patient = new Patient();
		patient.setMeta(new Meta()
			.addProfile(myProfile + "|2")
			.addProfile(myProfile + "|3")
			.addProfile(myProfile + "|1"));

		try {
			myClient.create().resource(patient).execute();
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals("HTTP 422 Unprocessable Entity: Patient.birthDate: minimum required = 1, but only found 0 (from " + myProfile + "|3)", e.getMessage());
		}

		// only populated field from version 3 profile
		patient.setBirthDate(new Date());
		// patient.identifier or patient.name are not validated from the version 1 or 2 profile
		myClient.create().resource(patient).execute();
	}

	private void createAndRegisterProfile(String theVersion, String thePath) {
		createAndRegisterProfile(myProfile, theVersion, thePath);
	}

	private void createAndRegisterProfile(String theUrl, String theVersion, String thePath) {
		StructureDefinition sd = createProfile(theUrl, theVersion, thePath);
		myPrePopulatedValidationSupport.addStructureDefinition(sd);
	}

	private StructureDefinition createProfile(String theUrl, String theVersion, String thePath) {
		final String baseProfile = "http://hl7.org/fhir/StructureDefinition/Patient";
		final String profileId = "TestProfile";

		StructureDefinition sd = new StructureDefinition()
			.setUrl(theUrl).setVersion(theVersion)
			.setBaseDefinition(baseProfile)
			.setType(myResourceType)
			.setDerivation(StructureDefinition.TypeDerivationRule.CONSTRAINT);
		sd.setId(profileId);
		sd.getDifferential().addElement()
			.setPath(thePath)
			.setMin(1)
			.setId(thePath);

		DaoMethodOutcome outcome = myStructureDefinitionDao.update(sd, new SystemRequestDetails());
		assertNotNull(outcome.getResource());
		return (StructureDefinition) outcome.getResource();
	}
}
