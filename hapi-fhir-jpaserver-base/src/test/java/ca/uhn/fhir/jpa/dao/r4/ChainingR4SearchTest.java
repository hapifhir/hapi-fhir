package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.ResourceSearch;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Device;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class ChainingR4SearchTest extends BaseJpaR4Test {

	@Autowired
	MatchUrlService myMatchUrlService;

	@AfterEach
	public void after() throws Exception {

		myDaoConfig.setAllowMultipleDelete(new DaoConfig().isAllowMultipleDelete());
		myDaoConfig.setAllowExternalReferences(new DaoConfig().isAllowExternalReferences());
		myDaoConfig.setReuseCachedSearchResultsForMillis(new DaoConfig().getReuseCachedSearchResultsForMillis());
		myDaoConfig.setCountSearchResultsUpTo(new DaoConfig().getCountSearchResultsUpTo());
		myDaoConfig.setSearchPreFetchThresholds(new DaoConfig().getSearchPreFetchThresholds());
		myDaoConfig.setAllowContainsSearches(new DaoConfig().isAllowContainsSearches());
		myDaoConfig.setIndexMissingFields(new DaoConfig().getIndexMissingFields());

		myModelConfig.setIndexOnContainedResources(false);
		myModelConfig.setIndexOnContainedResources(new ModelConfig().isIndexOnContainedResources());
		myModelConfig.setIndexOnContainedResourcesRecursively(new ModelConfig().isIndexOnContainedResourcesRecursively());
	}

	@BeforeEach
	public void before() throws Exception {
		myFhirCtx.setParserErrorHandler(new StrictErrorHandler());

		myDaoConfig.setAllowMultipleDelete(true);
		myDaoConfig.setSearchPreFetchThresholds(new DaoConfig().getSearchPreFetchThresholds());
		myModelConfig.setIndexOnContainedResources(true);
		myDaoConfig.setReuseCachedSearchResultsForMillis(null);
	}

	@Test
	public void testShouldResolveATwoLinkChainWithStandAloneResources() throws Exception {

		// setup
		IIdType oid1;

		{
			Patient p = new Patient();
			p.setId(IdType.newRandomUuid());
			p.addName().setFamily("Smith").addGiven("John");
			myPatientDao.create(p, mySrd);

			Observation obs = new Observation();
			obs.getCode().setText("Observation 1");
			obs.getSubject().setReference(p.getId());

			oid1 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();
		}

		String url = "/Observation?subject.name=Smith";

		// execute
		List<String> oids = searchAndReturnUnqualifiedVersionlessIdValues(url);

		// validate
		assertEquals(1L, oids.size());
		assertThat(oids, contains(oid1.getIdPart()));
	}

	@Test
	public void testShouldResolveATwoLinkChainWithAContainedResource() throws Exception {
		// setup
		IIdType oid1;

		{
			Patient p = new Patient();
			p.setId("pat");
			p.addName().setFamily("Smith").addGiven("John");

			Observation obs = new Observation();
			obs.getContained().add(p);
			obs.getCode().setText("Observation 1");
			obs.setValue(new StringType("Test"));
			obs.getSubject().setReference("#pat");

			oid1 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();
		}

		String url = "/Observation?subject.name=Smith";

		// execute
		List<String> oids = searchAndReturnUnqualifiedVersionlessIdValues(url);

		// validate
		assertEquals(1L, oids.size());
		assertThat(oids, contains(oid1.getIdPart()));
	}

	@Test
	public void testShouldResolveATwoLinkChainToAContainedReference() throws Exception {
		// Adding support for this case in SMILE-3151

		// setup
		IIdType oid1;
		IIdType orgId;

		{
			Organization org = new Organization();
			org.setId(IdType.newRandomUuid());
			org.setName("HealthCo");
			orgId = myOrganizationDao.create(org, mySrd).getId();

			Patient p = new Patient();
			p.setId("pat");
			p.addName().setFamily("Smith").addGiven("John");
			p.getManagingOrganization().setReference(org.getId());

			Observation obs = new Observation();
			obs.getContained().add(p);
			obs.getCode().setText("Observation 1");
			obs.getSubject().setReference("#pat");

			oid1 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();
		}

		String url = "/Observation?subject.organization=" + orgId.getValueAsString();

		// execute
		List<String> oids = searchAndReturnUnqualifiedVersionlessIdValues(url);

		// validate
		assertEquals(1L, oids.size());
		assertThat(oids, contains(oid1.getIdPart()));
	}

	@Test
	public void testShouldResolveAThreeLinkChainWhereAllResourcesStandAlone() throws Exception {

		// setup
		IIdType oid1;

		{
			Organization org = new Organization();
			org.setId(IdType.newRandomUuid());
			org.setName("HealthCo");
			myOrganizationDao.create(org, mySrd);

			Patient p = new Patient();
			p.setId(IdType.newRandomUuid());
			p.addName().setFamily("Smith").addGiven("John");
			p.getManagingOrganization().setReference(org.getId());
			myPatientDao.create(p, mySrd);

			Observation obs = new Observation();
			obs.getCode().setText("Observation 1");
			obs.getSubject().setReference(p.getId());

			oid1 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();
		}

		String url = "/Observation?subject.organization.name=HealthCo";

		// execute
		List<String> oids = searchAndReturnUnqualifiedVersionlessIdValues(url);

		// validate
		assertEquals(1L, oids.size());
		assertThat(oids, contains(oid1.getIdPart()));
	}

	@Test
	public void testShouldResolveAThreeLinkChainWithAContainedResourceAtTheEndOfTheChain() throws Exception {
		// This is the case that is most relevant to SMILE-2899

		// setup
		IIdType oid1;

		{
			Organization org = new Organization();
			org.setId("org");
			org.setName("HealthCo");

			Patient p = new Patient();
			p.setId(IdType.newRandomUuid());
			p.getContained().add(org);
			p.addName().setFamily("Smith").addGiven("John");
			p.getManagingOrganization().setReference("#org");
			myPatientDao.create(p, mySrd);

			Observation obs = new Observation();
			obs.getCode().setText("Observation 1");
			obs.getSubject().setReference(p.getId());

			oid1 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();
		}

		String url = "/Observation?subject.organization.name=HealthCo";

		// execute
		List<String> oids = searchAndReturnUnqualifiedVersionlessIdValues(url);

		// validate
		assertEquals(1L, oids.size());
		assertThat(oids, contains(oid1.getIdPart()));
	}

	@Test
	public void testShouldResolveAThreeLinkChainWithAContainedResourceAtTheBeginningOfTheChain() throws Exception {
		// Adding support for this case in SMILE-3151

		// setup
		IIdType oid1;

		{
			Organization org = new Organization();
			org.setId(IdType.newRandomUuid());
			org.setName("HealthCo");
			myOrganizationDao.create(org, mySrd);

			Patient p = new Patient();
			p.setId("pat");
			p.addName().setFamily("Smith").addGiven("John");
			p.getManagingOrganization().setReference(org.getId());

			Observation obs = new Observation();
			obs.getContained().add(p);
			obs.getCode().setText("Observation 1");
			obs.getSubject().setReference("#pat");

			oid1 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();
		}

		String url = "/Observation?subject.organization.name=HealthCo";

		// execute
		List<String> oids = searchAndReturnUnqualifiedVersionlessIdValues(url);

		// validate
		assertEquals(1L, oids.size());
		assertThat(oids, contains(oid1.getIdPart()));
	}

	@Test
	public void testShouldResolveAThreeLinkChainWithAllContainedResources() throws Exception {

		// setup
		myModelConfig.setIndexOnContainedResourcesRecursively(true);

		IIdType oid1;

		{
			Organization org = new Organization();
			org.setId("org");
			org.setName("HealthCo");

			Patient p = new Patient();
			p.setId("pat");
			p.addName().setFamily("Smith").addGiven("John");
			p.getManagingOrganization().setReference("#org");

			Observation obs = new Observation();
			obs.getContained().add(p);
			obs.getContained().add(org);
			obs.getCode().setText("Observation 1");
			obs.getSubject().setReference("#pat");

			oid1 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();
		}

		String url = "/Observation?subject.organization.name=HealthCo";

		// execute
		myCaptureQueriesListener.clear();
		List<String> oids = searchAndReturnUnqualifiedVersionlessIdValues(url);
		myCaptureQueriesListener.logSelectQueries();

		// validate
		assertEquals(1L, oids.size());
		assertThat(oids, contains(oid1.getIdPart()));
	}

	@Test
	public void testShouldResolveAThreeLinkChainWithQualifiersWhereAllResourcesStandAlone() throws Exception {

		// setup
		IIdType oid1;

		{
			Organization org = new Organization();
			org.setId(IdType.newRandomUuid());
			org.setName("HealthCo");
			myOrganizationDao.create(org, mySrd);

			Patient p = new Patient();
			p.setId(IdType.newRandomUuid());
			p.addName().setFamily("Smith").addGiven("John");
			p.getManagingOrganization().setReference(org.getId());
			myPatientDao.create(p, mySrd);

			Device d = new Device();
			d.setId(IdType.newRandomUuid());
			d.getOwner().setReference(org.getId());
			myDeviceDao.create(d, mySrd);

			Observation obs1 = new Observation();
			obs1.getCode().setText("Observation 1");
			obs1.getSubject().setReference(p.getId());

			Observation obs2 = new Observation();
			obs2.getCode().setText("Observation 2");
			obs2.getSubject().setReference(d.getId());

			oid1 = myObservationDao.create(obs1, mySrd).getId().toUnqualifiedVersionless();
			myObservationDao.create(obs2, mySrd);
		}

		String url = "/Observation?subject:Patient.organization:Organization.name=HealthCo";

		// execute
		List<String> oids = searchAndReturnUnqualifiedVersionlessIdValues(url);

		// validate
		assertEquals(1L, oids.size());
		assertThat(oids, contains(oid1.getIdPart()));
	}

	@Test
	public void testShouldResolveAThreeLinkChainWithQualifiersWithAContainedResourceAtTheEndOfTheChain() throws Exception {
		// This is the case that is most relevant to SMILE-2899

		// setup
		IIdType oid1;

		{
			Organization org = new Organization();
			org.setId("org");
			org.setName("HealthCo");

			Patient p = new Patient();
			p.setId(IdType.newRandomUuid());
			p.getContained().add(org);
			p.addName().setFamily("Smith").addGiven("John");
			p.getManagingOrganization().setReference("#org");
			myPatientDao.create(p, mySrd);

			Organization org2 = new Organization();
			org2.setId("org");
			org2.setName("HealthCo");

			Device d = new Device();
			d.setId(IdType.newRandomUuid());
			d.getContained().add(org2);
			d.getOwner().setReference("#org");
			myDeviceDao.create(d, mySrd);

			Observation obs = new Observation();
			obs.getCode().setText("Observation 1");
			obs.getSubject().setReference(p.getId());

			oid1 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			Observation obs2 = new Observation();
			obs2.getCode().setText("Observation 2");
			obs2.getSubject().setReference(d.getId());
			myObservationDao.create(obs2, mySrd);
		}

		String url = "/Observation?subject:Patient.organization:Organization.name=HealthCo";

		// execute
		List<String> oids = searchAndReturnUnqualifiedVersionlessIdValues(url);

		// validate
		assertEquals(1L, oids.size());
		assertThat(oids, contains(oid1.getIdPart()));
	}

	@Test
	public void testShouldResolveAThreeLinkChainWithQualifiersWithAContainedResourceAtTheBeginning() throws Exception {
		// Adding support for this case in SMILE-3151

		// setup
		IIdType oid1;

		{
			Organization org = new Organization();
			org.setId(IdType.newRandomUuid());
			org.setName("HealthCo");
			myOrganizationDao.create(org, mySrd);

			Patient p = new Patient();
			p.setId("pat");
			p.addName().setFamily("Smith").addGiven("John");
			p.getManagingOrganization().setReference(org.getId());

			Observation obs = new Observation();
			obs.getContained().add(p);
			obs.getCode().setText("Observation 1");
			obs.getSubject().setReference("#pat");

			oid1 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			Device d = new Device();
			d.setId("dev");
			d.getOwner().setReference(org.getId());

			Observation obs2 = new Observation();
			obs2.getContained().add(d);
			obs2.getCode().setText("Observation 2");
			obs2.getSubject().setReference("#dev");

			myObservationDao.create(obs2, mySrd);
		}

		String url = "/Observation?subject:Patient.organization:Organization.name=HealthCo";

		// execute
		myCaptureQueriesListener.clear();
		List<String> oids = searchAndReturnUnqualifiedVersionlessIdValues(url);
		myCaptureQueriesListener.logSelectQueries();

		// validate
		assertEquals(1L, oids.size());
		assertThat(oids, contains(oid1.getIdPart()));
	}

	@Test
	public void testShouldResolveAFourLinkChainWhereAllResourcesStandAlone() throws Exception {

		// setup
		IIdType oid1;

		{
			Organization org = new Organization();
			org.setId(IdType.newRandomUuid());
			org.setName("HealthCo");
			myOrganizationDao.create(org, mySrd);

			Organization partOfOrg = new Organization();
			partOfOrg.setId(IdType.newRandomUuid());
			partOfOrg.getPartOf().setReference(org.getId());
			myOrganizationDao.create(partOfOrg, mySrd);

			Patient p = new Patient();
			p.setId(IdType.newRandomUuid());
			p.addName().setFamily("Smith").addGiven("John");
			p.getManagingOrganization().setReference(partOfOrg.getId());
			myPatientDao.create(p, mySrd);

			Observation obs = new Observation();
			obs.getCode().setText("Observation 1");
			obs.getSubject().setReference(p.getId());

			oid1 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();
		}

		String url = "/Observation?subject.organization.partof.name=HealthCo";

		// execute
		List<String> oids = searchAndReturnUnqualifiedVersionlessIdValues(url);

		// validate
		assertEquals(1L, oids.size());
		assertThat(oids, contains(oid1.getIdPart()));
	}

	@Test
	public void testShouldResolveAFourLinkChainWhereTheLastReferenceIsContained() throws Exception {

		// setup
		IIdType oid1;

		{
			Organization org = new Organization();
			org.setId("parent");
			org.setName("HealthCo");

			Organization partOfOrg = new Organization();
			partOfOrg.setId(IdType.newRandomUuid());
			partOfOrg.getContained().add(org);
			partOfOrg.getPartOf().setReference("#parent");
			myOrganizationDao.create(partOfOrg, mySrd);

			Patient p = new Patient();
			p.setId(IdType.newRandomUuid());
			p.addName().setFamily("Smith").addGiven("John");
			p.getManagingOrganization().setReference(partOfOrg.getId());
			myPatientDao.create(p, mySrd);

			Observation obs = new Observation();
			obs.getCode().setText("Observation 1");
			obs.getSubject().setReference(p.getId());

			oid1 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();
		}

		String url = "/Observation?subject.organization.partof.name=HealthCo";

		// execute
		List<String> oids = searchAndReturnUnqualifiedVersionlessIdValues(url);

		// validate
		assertEquals(1L, oids.size());
		assertThat(oids, contains(oid1.getIdPart()));
	}

	@Test
	public void testShouldResolveAFourLinkChainWithAContainedResourceInTheMiddle() throws Exception {

		// setup
		IIdType oid1;

		{
			myCaptureQueriesListener.clear();

			Organization org = new Organization();
			org.setId(IdType.newRandomUuid());
			org.setName("HealthCo");
			myOrganizationDao.create(org, mySrd);

			Organization partOfOrg = new Organization();
			partOfOrg.setId("org");
			partOfOrg.getPartOf().setReference(org.getId());

			Patient p = new Patient();
			p.setId(IdType.newRandomUuid());
			p.addName().setFamily("Smith").addGiven("John");
			p.getContained().add(partOfOrg);
			p.getManagingOrganization().setReference("#org");
			myPatientDao.create(p, mySrd);

			Observation obs = new Observation();
			obs.getCode().setText("Observation 1");
			obs.getSubject().setReference(p.getId());

			oid1 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			myCaptureQueriesListener.logInsertQueries();
		}

		String url = "/Observation?subject.organization.partof.name=HealthCo";

		// execute
		myCaptureQueriesListener.clear();
		List<String> oids = searchAndReturnUnqualifiedVersionlessIdValues(url);
		myCaptureQueriesListener.logSelectQueries();

		// validate
		assertEquals(1L, oids.size());
		assertThat(oids, contains(oid1.getIdPart()));
	}

	private List<String> searchAndReturnUnqualifiedVersionlessIdValues(String theUrl) throws IOException {
		List<String> ids = new ArrayList<>();

		ResourceSearch search = myMatchUrlService.getResourceSearch(theUrl);
		SearchParameterMap map = search.getSearchParameterMap();
		map.setLoadSynchronous(true);
		IBundleProvider result = myObservationDao.search(map);
		return result.getAllResourceIds();
	}

}
