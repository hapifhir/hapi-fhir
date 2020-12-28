package ca.uhn.fhir.jpa.interceptor.validation;

import ca.uhn.fhir.jpa.config.BaseConfig;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class RepositoryValidatingInterceptorR4Test extends BaseJpaR4Test {

	private RepositoryValidatingInterceptor myValInterceptor;
	@Autowired
	private ApplicationContext myApplicationContext;

	@BeforeEach
	public void before() {
		myValInterceptor = new RepositoryValidatingInterceptor();
		myValInterceptor.setFhirContext(myFhirCtx);
		myInterceptorRegistry.registerInterceptor(myValInterceptor);
	}

	@AfterEach
	public void after() {
		myInterceptorRegistry.unregisterInterceptorsIf(t -> t instanceof RepositoryValidatingInterceptor);
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
			assertEquals("Resource of type \"Patient\" does not declare conformance to profile from: [http://foo/Profile1, http://foo/Profile2]", e.getMessage());
		}

		// No profile blocked
		try {
			Patient patient = new Patient();
			patient.setActive(true);
			myPatientDao.create(patient);
			fail();
		} catch (PreconditionFailedException e) {
			assertEquals("Resource of type \"Patient\" does not declare conformance to profile from: [http://foo/Profile1, http://foo/Profile2]", e.getMessage());
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
			assertEquals("Resource of type \"Patient\" does not declare conformance to profile from: [http://foo/Profile1, http://foo/Profile2]", e.getMessage());
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
			assertEquals("Resource of type \"Patient\" must not declare conformance to profile: http://profile-bad", e.getMessage());
		}

	}

	@Test
	public void testDisallowProfile_UpdateBlocked() {

		List<IRepositoryValidatingRule> rules = newRuleBuilder()
			.forResourcesOfType("Patient")
			.disallowProfile("http://profile-bad")
			.build();
		myValInterceptor.setRules(rules);

		// Create a resource with an allowable profile
		Patient patient = new Patient();
		patient.getMeta().addProfile("http://foo/Profile1");
		IIdType id = myPatientDao.create(patient).getId();

		// Explicitly adding the profile is blocked
		try {
			Meta metaDel = new Meta();
			metaDel.addProfile("http://profile-bad");
			myPatientDao.metaAddOperation(id, metaDel, mySrd);
			fail();
		} catch (PreconditionFailedException e) {
			assertEquals("Resource of type \"Patient\" must not declare conformance to profile: http://profile-bad", e.getMessage());
		}

		patient = myPatientDao.read(id);
		assertThat(patient.getMeta().getProfile().stream().map(t -> t.getValue()).collect(Collectors.toList()), containsInAnyOrder("http://foo/Profile1"));

	}

	@Test
	public void testRequireValidation_Allowed() {
		List<IRepositoryValidatingRule> rules = newRuleBuilder()
			.forResourcesOfType("Observation")
			.requireValidation()
			.withBestPracticeWarningLevel("IGNORE")
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
			fail(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(e.getOperationOutcome()));
		}
	}

	@Test
	public void testRequireValidation_Blocked() {
		List<IRepositoryValidatingRule> rules = newRuleBuilder()
			.forResourcesOfType("Observation")
			.requireValidation()
			.withBestPracticeWarningLevel("IGNORE")
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

	private RepositoryValidatingRuleBuilder newRuleBuilder() {
		return myApplicationContext.getBean(BaseConfig.REPOSITORY_VALIDATING_RULE_BUILDER, RepositoryValidatingRuleBuilder.class);
	}

}
