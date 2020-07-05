package ca.uhn.fhir.jpa.dao.dstu3;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class FhirResourceDaoDstu3ExternalReferenceTest extends BaseJpaDstu3Test {

	@BeforeEach
	public void beforeDisableResultReuse() {
		myDaoConfig.setReuseCachedSearchResultsForMillis(null);
	}

	@BeforeEach
	@AfterEach
	public void resetDefaultBehaviour() {
		// Reset to default
		myDaoConfig.setAllowExternalReferences(new DaoConfig().isAllowExternalReferences());
		myDaoConfig.setTreatBaseUrlsAsLocal(null);
	}

	@Test
	public void testInternalReferenceBlockedByDefault() {
		Patient p = new Patient();
		p.getManagingOrganization().setReference("Organization/FOO");
		try {
			myPatientDao.create(p, mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Resource Organization/FOO not found, specified in path: Patient.managingOrganization", e.getMessage());
		}
	}

	@Test
	public void testExternalReferenceBlockedByDefault() {
		Organization org = new Organization();
		org.setId("FOO");
		org.setName("Org Name");
		myOrganizationDao.update(org, mySrd);

		Patient p = new Patient();
		p.getManagingOrganization().setReference("http://example.com/base/Organization/FOO");
		try {
			myPatientDao.create(p, mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Resource contains external reference to URL \"http://example.com/base/Organization/FOO\" but this server is not configured to allow external references", e.getMessage());
		}
	}

	@Test
	public void testExternalReferenceAllowed() {
		Organization org = new Organization();
		org.setId("FOO");
		org.setName("Org Name");
		myOrganizationDao.update(org, mySrd);

		myDaoConfig.setAllowExternalReferences(true);

		Patient p = new Patient();
		p.getManagingOrganization().setReference("http://example.com/base/Organization/FOO");
		IIdType pid = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap map = new SearchParameterMap();
		map.add(Patient.SP_ORGANIZATION, new ReferenceParam("http://example.com/base/Organization/FOO"));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map)), contains(pid.getValue()));

		map = new SearchParameterMap();
		map.add(Patient.SP_ORGANIZATION, new ReferenceParam("http://example2.com/base/Organization/FOO"));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map)), empty());
	}

	@Test
	public void testExternalReferenceReplaced() {
		Organization org = new Organization();
		org.setId("FOO");
		org.setName("Org Name");
		org.getPartOf().setDisplay("Parent"); // <-- no reference, make sure this works
		myOrganizationDao.update(org, mySrd);

		Set<String> urls = new HashSet<String>();
		urls.add("http://example.com/base/");
		myDaoConfig.setTreatBaseUrlsAsLocal(urls);

		Patient p = new Patient();
		p.getManagingOrganization().setReference("http://example.com/base/Organization/FOO");
		IIdType pid = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		p = myPatientDao.read(pid, mySrd);
		assertEquals("Organization/FOO", p.getManagingOrganization().getReference());

		SearchParameterMap map;

		map = new SearchParameterMap();
		map.add(Patient.SP_ORGANIZATION, new ReferenceParam("http://example.com/base/Organization/FOO"));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map)), contains(pid.getValue()));
	}

	@Test
	public void testSearchForInvalidLocalReference() {
		SearchParameterMap map;

		map = new SearchParameterMap();
		map.add(Patient.SP_ORGANIZATION, new ReferenceParam("Organization/FOO"));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map)), empty());

		map = new SearchParameterMap();
		map.add(Patient.SP_ORGANIZATION, new ReferenceParam("Organization/9999999999"));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map)), empty());
	}

	@Test
	public void testExternalReferenceReplacedWrongDoesntMatch() {
		Organization org = new Organization();
		org.setId("FOO");
		org.setName("Org Name");
		org.getPartOf().setDisplay("Parent"); // <-- no reference, make sure this works
		myOrganizationDao.update(org, mySrd);

		Set<String> urls = new HashSet<String>();
		urls.add("http://example.com/base/");
		myDaoConfig.setTreatBaseUrlsAsLocal(urls);

		Patient p = new Patient();
		p.getManagingOrganization().setReference("http://example.com/base/Organization/FOO");
		IIdType pid = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		p = myPatientDao.read(pid, mySrd);
		assertEquals("Organization/FOO", p.getManagingOrganization().getReference());

		SearchParameterMap map;

		// Different base
		map = new SearchParameterMap();
		map.add(Patient.SP_ORGANIZATION, new ReferenceParam("http://foo.com/base/Organization/FOO"));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map)), empty());
	}

}
