package ca.uhn.fhir.jpa.interceptor.validation;

import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class RepositoryValidatingInterceptorR4Test extends BaseJpaR4Test {

	private RepositoryValidatingInterceptor myValInterceptor;

	@BeforeEach
	public void before() {
		myValInterceptor = new RepositoryValidatingInterceptor();
	}

	@AfterEach
	public void after() {
		myInterceptorRegistry.unregisterInterceptorsIf(t -> t instanceof RepositoryValidatingInterceptor);
	}

	@Test
	public void testRequireAtLeastOneProfileOf_Allowed() {

		List<IRepositoryValidatingRule> rules = RepositoryValidatingRuleBuilder
			.newInstance(myFhirCtx)
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

		List<IRepositoryValidatingRule> rules = RepositoryValidatingRuleBuilder
			.newInstance(myFhirCtx)
			.forResourcesOfType("Patient")
			.requireAtLeastOneProfileOf("http://foo/Profile1", "http://foo/Profile2")
			.build();
		myValInterceptor.setRules(rules);

		// Disallowed profile blocked
		try {
			Patient patient = new Patient();
			patient.getMeta().addProfile("http://foo/Profile3");
			myPatientDao.create(patient).getId();
			fail();
		} catch (PreconditionFailedException e) {
			assertEquals("", e.getMessage());
		}

		// No profile blocked
		try {
			Patient patient = new Patient();
			patient.setActive(true);
			myPatientDao.create(patient);
			fail();
		} catch (PreconditionFailedException e) {
			assertEquals("", e.getMessage());
		}

	}

	@Test
	public void testRequireAtLeastOneProfileOf_UpdateBlocked() {

		List<IRepositoryValidatingRule> rules = RepositoryValidatingRuleBuilder
			.newInstance(myFhirCtx)
			.forResourcesOfType("Patient")
			.requireAtLeastOneProfileOf("http://foo/Profile1", "http://foo/Profile2")
			.build();
		myValInterceptor.setRules(rules);

		// Create a resource with an allowable profile
		Patient patient = new Patient();
		patient.getMeta().addProfile("http://foo/Profile1");
		IIdType id = myPatientDao.create(patient).getId();

		// Update with no profile declaration should preserve the old one so this is ok
		patient = new Patient();
		patient.setId(id);
		patient.setActive(true);
		id = myPatientDao.update(patient).getId().toUnqualifiedVersionless();

		// Explicitly dropping the profile is blocked
		try {
			Meta metaDel = new Meta();
			metaDel.addProfile("http://foo/Profile1");
			myPatientDao.metaDeleteOperation(id, metaDel, mySrd);
			fail();
		} catch (PreconditionFailedException e) {
			assertEquals("", e.getMessage());
		}

	}


}
