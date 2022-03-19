package ca.uhn.fhir.jpa.interceptor.validation;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.PractitionerRole;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r5.utils.validation.constants.BestPracticeWarningLevel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class RepositoryValidatingInterceptorR4Test extends BaseJpaR4Test {

	private RepositoryValidatingInterceptor myValInterceptor;
	@Autowired
	private ApplicationContext myApplicationContext;

	@BeforeEach
	public void before() {
		myValInterceptor = new RepositoryValidatingInterceptor();
		myValInterceptor.setFhirContext(myFhirContext);
		myInterceptorRegistry.registerInterceptor(myValInterceptor);
	}

	@AfterEach
	public void after() {
		myInterceptorRegistry.unregisterInterceptorsIf(t -> t instanceof RepositoryValidatingInterceptor);
	}

	@Test
	public void testRequireAtLeastProfile_Allowed() {

		List<IRepositoryValidatingRule> rules = newRuleBuilder()
			.forResourcesOfType("Patient")
			.requireAtLeastProfile("http://foo/Profile1")
			.build();
		myValInterceptor.setRules(rules);

		// Create with correct profile allowed
		Patient patient = new Patient();
		patient.getMeta().addProfile("http://foo/Profile1");
		IIdType id = myPatientDao.create(patient).getId();
		assertEquals("1", id.getVersionIdPart());
	}

	@Test
	public void testRequireAtLeastOneProfileOf_Allowed() {

		List<IRepositoryValidatingRule> rules = newRuleBuilder()
			.forResourcesOfType("Patient")
			.requireAtLeastOneProfileOf("http://foo/Profile1", "http://foo/Profile2")
			.build();
		myValInterceptor.setRules(rules);

		// Create with correct profile allowed
		Patient patient = new Patient();
		patient.getMeta().addProfile("http://foo/Profile1");
		IIdType id = myPatientDao.create(patient).getId();

		// Update with correct profile allowed
		patient = new Patient();
		patient.setId(id);
		patient.getMeta().addProfile("http://foo/Profile2");
		patient.setActive(true);
		id = myPatientDao.update(patient).getId();
		assertEquals("2", id.getVersionIdPart());

		// Create with other resource type allowed
		Observation obs = new Observation();
		obs.setStatus(Observation.ObservationStatus.AMENDED);
		myObservationDao.create(obs);

		// Create with correct profile and other profile allowed
		patient = new Patient();
		patient.getMeta().addProfile("http://foo/Profile1");
		patient.getMeta().addProfile("http://foo/Profile9999");
		myPatientDao.create(patient);
	}

	@Test
	public void testRequireAtLeastOneProfileOf_CreateBlocked() {

		List<IRepositoryValidatingRule> rules = newRuleBuilder()
			.forResourcesOfType("Patient")
			.requireAtLeastOneProfileOf("http://foo/Profile1", "http://foo/Profile2")
			.build();
		myValInterceptor.setRules(rules);

		// Disallowed profile blocked
		try {
			Patient patient = new Patient();
			patient.getMeta().addProfile("http://foo/Profile3");
			myPatientDao.create(patient);
			fail();
		} catch (PreconditionFailedException e) {
			assertEquals(Msg.code(575) + "Resource of type \"Patient\" does not declare conformance to profile from: [http://foo/Profile1, http://foo/Profile2]", e.getMessage());
		}

		// No profile blocked
		try {
			Patient patient = new Patient();
			patient.setActive(true);
			myPatientDao.create(patient);
			fail();
		} catch (PreconditionFailedException e) {
			assertEquals(Msg.code(575) + "Resource of type \"Patient\" does not declare conformance to profile from: [http://foo/Profile1, http://foo/Profile2]", e.getMessage());
		}

	}

	@Test
	public void testRequireAtLeastOneProfileOf_UpdateBlocked() {

		List<IRepositoryValidatingRule> rules = newRuleBuilder()
			.forResourcesOfType("Patient")
			.requireAtLeastOneProfileOf("http://foo/Profile1", "http://foo/Profile2")
			.build();
		myValInterceptor.setRules(rules);

		// Create a resource with an allowable profile
		Patient patient = new Patient();
		patient.getMeta().addProfile("http://foo/Profile1");
		IIdType id = myPatientDao.create(patient).getId();

		// Explicitly dropping the profile is blocked
		try {
			Meta metaDel = new Meta();
			metaDel.addProfile("http://foo/Profile1");
			myPatientDao.metaDeleteOperation(id, metaDel, mySrd);
			fail();
		} catch (PreconditionFailedException e) {
			assertEquals(Msg.code(575) + "Resource of type \"Patient\" does not declare conformance to profile from: [http://foo/Profile1, http://foo/Profile2]", e.getMessage());
		}

		patient = myPatientDao.read(id);
		assertThat(patient.getMeta().getProfile().stream().map(t -> t.getValue()).collect(Collectors.toList()), containsInAnyOrder("http://foo/Profile1"));

	}

	@Test
	public void testDisallowProfile_CreateBlocked() {

		List<IRepositoryValidatingRule> rules = newRuleBuilder()
			.forResourcesOfType("Patient")
			.disallowProfile("http://profile-bad")
			.build();
		myValInterceptor.setRules(rules);

		// Disallowed profile blocked
		try {
			Patient patient = new Patient();
			patient.getMeta().addProfile("http://profile-bad");
			myPatientDao.create(patient);
			fail();
		} catch (PreconditionFailedException e) {
			assertEquals(Msg.code(575) + "Resource of type \"Patient\" must not declare conformance to profile: http://profile-bad", e.getMessage());
		}

	}

	@Test
	public void testDisallowProfile_UpdateBlocked() {

		List<IRepositoryValidatingRule> rules = newRuleBuilder()
			.forResourcesOfType("Patient")
			.disallowProfiles("http://profile-bad", "http://profile-bad2")
			.build();
		myValInterceptor.setRules(rules);

		// Create a resource with an allowable profile
		Patient patient = new Patient();
		patient.getMeta().addProfile("http://foo/Profile1");
		IIdType id = myPatientDao.create(patient).getId();

		// Explicitly adding the profile is blocked using $meta-add
		try {
			Meta metaDel = new Meta();
			metaDel.addProfile("http://profile-bad");
			myPatientDao.metaAddOperation(id, metaDel, mySrd);
			fail();
		} catch (PreconditionFailedException e) {
			assertEquals(Msg.code(575) + "Resource of type \"Patient\" must not declare conformance to profile: http://profile-bad", e.getMessage());
		}

		// Explicitly adding the profile is blocked using patch
		try {
			Parameters patch = new Parameters();
			Parameters.ParametersParameterComponent operation = patch.addParameter();
			operation.setName("operation");
			operation
				.addPart()
				.setName("type")
				.setValue(new CodeType("insert"));
			operation
				.addPart()
				.setName("path")
				.setValue(new StringType("Patient.meta.profile"));
			operation
				.addPart()
				.setName("index")
				.setValue(new IntegerType(0));
			operation
				.addPart()
				.setName("value")
				.setValue(new CanonicalType("http://profile-bad2"));
			myCaptureQueriesListener.clear();
			myPatientDao.patch(id, null, PatchTypeEnum.FHIR_PATCH_JSON, null, patch, mySrd);
			fail();
		} catch (PreconditionFailedException e) {
			assertEquals(Msg.code(575) + "Resource of type \"Patient\" must not declare conformance to profile: http://profile-bad2", e.getMessage());
		} catch (Exception e) {
			myCaptureQueriesListener.logAllQueriesForCurrentThread();
			fail(e.toString());
		}

		patient = myPatientDao.read(id);
		assertThat(patient.getMeta().getProfile().stream().map(t -> t.getValue()).collect(Collectors.toList()), containsInAnyOrder("http://foo/Profile1"));

	}

	@Test
	public void testRequireValidationDoesNotApplyToPlaceholders() {

		//Given
		myDaoConfig.setAutoCreatePlaceholderReferenceTargets(true);
		List<IRepositoryValidatingRule> rules = newRuleBuilder()
			.forResourcesOfType("Organization")
			.requireValidationToDeclaredProfiles()
			.build();
		myValInterceptor.setRules(rules);

		//When
		PractitionerRole pr = new PractitionerRole();
		pr.setOrganization(new Reference("Organization/400-40343834-7383-54b4-abfe-95281da21062-ProviderOrganiz"));

		//Then
		try {
			IIdType id = myPractitionerRoleDao.create(pr).getId();
			assertEquals("1", id.getVersionIdPart());
		} catch (PreconditionFailedException e) {
			// should not happen
			fail(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(e.getOperationOutcome()));
		}
	}

	@Test
	public void testRequireAtLeastProfilesDoesNotApplyToPlaceholders() {
		//Given
		myDaoConfig.setAutoCreatePlaceholderReferenceTargets(true);
		List<IRepositoryValidatingRule> rules = newRuleBuilder()
			.forResourcesOfType("Organization")
			.requireAtLeastOneProfileOf("http://example.com/profile1", "http://example.com/profile2")
			.build();
		myValInterceptor.setRules(rules);

		//When
		PractitionerRole pr = new PractitionerRole();
		pr.setOrganization(new Reference("Organization/400-40343834-7383-54b4-abfe-95281da21062-ProviderOrganiz"));

		//Then
		try {
			IIdType id = myPractitionerRoleDao.create(pr).getId();
			assertEquals("1", id.getVersionIdPart());
		} catch (PreconditionFailedException e) {
			// should not happen
			fail(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(e.getOperationOutcome()));
		}
	}


	@Test
	public void testRequireValidation_AdditionalOptions() {
		List<IRepositoryValidatingRule> rules = newRuleBuilder()
			.forResourcesOfType("Observation")
			.requireValidationToDeclaredProfiles()
			.withBestPracticeWarningLevel("IGNORE")
			.allowAnyExtensions()
			.disableTerminologyChecks()
			.errorOnUnknownProfiles()
			.suppressNoBindingMessage()
			.suppressWarningForExtensibleValueSetValidation()
			.build();

		myValInterceptor.setRules(rules);

		Observation obs = new Observation();
		obs.getCode().addCoding().setSystem("http://foo").setCode("123").setDisplay("help im a bug");
		obs.setStatus(Observation.ObservationStatus.AMENDED);
		try {
			IIdType id = myObservationDao.create(obs).getId();
			assertEquals("1", id.getVersionIdPart());
		} catch (PreconditionFailedException e) {
			// should not happen
			fail(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(e.getOperationOutcome()));
		}
	}

	@Test
	public void testRequireValidation_AdditionalOptions_Reject_UnKnown_Extensions() {
		List<IRepositoryValidatingRule> rules = newRuleBuilder()
			.forResourcesOfType("Observation")
			.requireValidationToDeclaredProfiles()
			.withBestPracticeWarningLevel("IGNORE")
			.rejectUnknownExtensions()
			.disableTerminologyChecks()
			.errorOnUnknownProfiles()
			.suppressNoBindingMessage()
			.suppressWarningForExtensibleValueSetValidation()
			.build();

		myValInterceptor.setRules(rules);

		Observation obs = new Observation();
		obs.getCode().addCoding().setSystem("http://foo").setCode("123").setDisplay("help im a bug");
		obs.setStatus(Observation.ObservationStatus.AMENDED);
		try {
			IIdType id = myObservationDao.create(obs).getId();
			assertEquals("1", id.getVersionIdPart());
		} catch (PreconditionFailedException e) {
			// should not happen
			fail(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(e.getOperationOutcome()));
		}
	}

	@Test
	public void testRequireValidation_FailNoRejectAndTag() {
		List<IRepositoryValidatingRule> rules = newRuleBuilder()
			.forResourcesOfType("Observation")
			.requireValidationToDeclaredProfiles()
			.withBestPracticeWarningLevel("IGNORE")
			.neverReject()
			.tagOnSeverity(ResultSeverityEnum.ERROR, "http://foo", "validation-error")
			.build();
		myValInterceptor.setRules(rules);

		Observation obs = new Observation();
		obs.getCode().addCoding().setSystem("http://foo").setCode("123").setDisplay("help im a bug");
		IIdType id = myObservationDao.create(obs).getId();
		assertEquals("1", id.getVersionIdPart());

		obs = myObservationDao.read(id);
		assertTrue(obs.getMeta().hasTag());
		assertTrue(obs.getMeta().getTag("http://foo", "validation-error") != null);
	}

	@Test
	public void testRequireValidation_Blocked() {
		List<IRepositoryValidatingRule> rules = newRuleBuilder()
			.forResourcesOfType("Observation")
			.requireValidationToDeclaredProfiles()
			.withBestPracticeWarningLevel("IGNORE")
			.rejectOnSeverity("error")
			.build();
		myValInterceptor.setRules(rules);

		Observation obs = new Observation();
		obs.getCode().addCoding().setSystem("http://foo").setCode("123").setDisplay("help im a bug");
		obs.addIdentifier().setSystem("system");
		try {
			myObservationDao.create(obs).getId();
			fail();
		} catch (PreconditionFailedException e) {
			OperationOutcome oo = (OperationOutcome) e.getOperationOutcome();
			assertEquals("Observation.status: minimum required = 1, but only found 0 (from http://hl7.org/fhir/StructureDefinition/Observation)", oo.getIssue().get(0).getDiagnostics());
		}

	}


	@Test
	public void testMultipleTypedRules() {

		List<IRepositoryValidatingRule> rules = newRuleBuilder()
			.forResourcesOfType("Observation")
			.requireAtLeastProfile("http://hl7.org/fhir/StructureDefinition/Observation")
			.and()
			.requireValidationToDeclaredProfiles()
			.withBestPracticeWarningLevel(BestPracticeWarningLevel.Ignore)
			.build();
		myValInterceptor.setRules(rules);

		// Create with correct profile allowed
		Observation Observation = new Observation();
		Observation.getMeta().addProfile("http://hl7.org/fhir/StructureDefinition/Observation");
		try {
			myObservationDao.create(Observation);
			fail();
		} catch (PreconditionFailedException e) {
			assertThat(e.getMessage(), containsString("Observation.status: minimum required = 1, but only found 0"));
		}
	}


	private RepositoryValidatingRuleBuilder newRuleBuilder() {
		return myApplicationContext.getBean(RepositoryValidatingRuleBuilder.REPOSITORY_VALIDATING_RULE_BUILDER, RepositoryValidatingRuleBuilder.class);
	}

}
