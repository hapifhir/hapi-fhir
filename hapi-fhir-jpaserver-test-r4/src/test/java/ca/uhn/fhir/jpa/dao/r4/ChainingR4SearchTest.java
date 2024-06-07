package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.util.SqlQuery;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.AuditEvent;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Device;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Location;
import org.hl7.fhir.r4.model.MessageHeader;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.countMatches;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


public class ChainingR4SearchTest extends BaseJpaR4Test {

	@AfterEach
	public void after() throws Exception {

		myStorageSettings.setAllowMultipleDelete(new JpaStorageSettings().isAllowMultipleDelete());
		myStorageSettings.setAllowExternalReferences(new JpaStorageSettings().isAllowExternalReferences());
		myStorageSettings.setReuseCachedSearchResultsForMillis(new JpaStorageSettings().getReuseCachedSearchResultsForMillis());
		myStorageSettings.setCountSearchResultsUpTo(new JpaStorageSettings().getCountSearchResultsUpTo());
		myStorageSettings.setSearchPreFetchThresholds(new JpaStorageSettings().getSearchPreFetchThresholds());
		myStorageSettings.setAllowContainsSearches(new JpaStorageSettings().isAllowContainsSearches());
		myStorageSettings.setIndexMissingFields(new JpaStorageSettings().getIndexMissingFields());
		myStorageSettings.setIndexOnContainedResources(new JpaStorageSettings().isIndexOnContainedResources());
		myStorageSettings.setIndexOnContainedResourcesRecursively(new JpaStorageSettings().isIndexOnContainedResourcesRecursively());
	}

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myFhirContext.setParserErrorHandler(new StrictErrorHandler());

		myStorageSettings.setAllowMultipleDelete(true);
		myStorageSettings.setSearchPreFetchThresholds(new JpaStorageSettings().getSearchPreFetchThresholds());
		myStorageSettings.setReuseCachedSearchResultsForMillis(null);
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.DISABLED);
	}

	@Test
	public void testIndexSearchParamPointingToResource() {
		// Setup

		myStorageSettings.setIndexOnContainedResources(true);

		Bundle inputBundle = new Bundle();
		inputBundle.setType(Bundle.BundleType.MESSAGE);

		MessageHeader msgHeader = new MessageHeader();
		msgHeader.setEvent(new Coding("http://foo", "bar", "blah"));
		inputBundle.addEntry().setResource(msgHeader);

		RuntimeSearchParam sp = mySearchParamRegistry.getActiveSearchParam("Bundle", "message");
		assertEquals("Bundle.entry[0].resource", sp.getPath());
		assertThat(sp.getBase()).containsExactly("Bundle");
		assertEquals(RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE, sp.getStatus());

		// Test
		myBundleDao.create(inputBundle, mySrd);

		// Verify - We'll check that the right indexes got written, but the main test is that
		// the create step didn't crash
		runInTransaction(()->{
			assertEquals(0, myResourceIndexedSearchParamStringDao.count());
			assertEquals(1, myResourceIndexedSearchParamTokenDao.count());
			assertEquals(0, myResourceLinkDao.count());
		});
	}


	@Test
	public void testShouldResolveATwoLinkChainWithStandAloneResourcesWithoutContainedResourceIndexing() {

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

			// Create a dummy record so that an unconstrained query doesn't pass the test due to returning the only record
			myObservationDao.create(new Observation(), mySrd);
		}

		String url = "/Observation?subject.name=Smith";

		// execute
		List<String> oids = myTestDaoSearch.searchForIds(url);

		// validate
		assertThat(oids).hasSize(1);
		assertThat(oids).containsExactly(oid1.getIdPart());
	}

	@Test
	public void testShouldResolveATwoLinkChainWithStandAloneResources() {

		// setup
		myStorageSettings.setIndexOnContainedResources(true);

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

			// Create a dummy record so that an unconstrained query doesn't pass the test due to returning the only record
			myObservationDao.create(new Observation(), mySrd);
		}

		String url = "/Observation?subject.name=Smith";

		// execute
		List<String> oids = myTestDaoSearch.searchForIds(url);

		// validate
		assertThat(oids).hasSize(1);
		assertThat(oids).containsExactly(oid1.getIdPart());
	}

	@Test
	public void testShouldResolveATwoLinkChainWithStandAloneResources_CommonReference() {

		// setup
		myStorageSettings.setIndexOnContainedResources(true);

		IIdType oid1;

		{
			Patient p = new Patient();
			p.setId(IdType.newRandomUuid());
			p.addName().setFamily("Smith").addGiven("John");
			myPatientDao.create(p, mySrd);

			Encounter encounter = new Encounter();
			encounter.setId(IdType.newRandomUuid());
			encounter.addIdentifier().setSystem("foo").setValue("bar");
			myEncounterDao.create(encounter, mySrd);

			Observation obs = new Observation();
			obs.getCode().setText("Body Weight");
			obs.getCode().addCoding().setCode("obs2").setSystem("Some System").setDisplay("Body weight as measured by me");
			obs.setStatus(Observation.ObservationStatus.FINAL);
			obs.setValue(new Quantity(81));
			obs.setSubject(new Reference(p.getId()));
			obs.setEncounter(new Reference(encounter.getId()));
			oid1 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			// Create a dummy record so that an unconstrained query doesn't pass the test due to returning the only record
			myObservationDao.create(new Observation(), mySrd);
		}

		String url = "/Observation?encounter.identifier=foo|bar";

		// execute
		myCaptureQueriesListener.clear();
		List<String> oids = myTestDaoSearch.searchForIds(url);
		myCaptureQueriesListener.logSelectQueries();

		// validate
		assertThat(oids).hasSize(1);
		assertThat(oids).containsExactly(oid1.getIdPart());
	}

	@Test
	public void testShouldResolveATwoLinkChainWithStandAloneResources_CompoundReference() {

		// setup
		myStorageSettings.setIndexOnContainedResources(true);

		IIdType oid1;
		IIdType oid2;

		// AuditEvent should match both AuditEvent.agent.who and AuditEvent.entity.what
		{
			Patient p = new Patient();
			p.setId(IdType.newRandomUuid());
			p.addName().setFamily("Smith").addGiven("John");
			myPatientDao.create(p, mySrd);

			AuditEvent auditEvent = new AuditEvent();
			auditEvent.addAgent().setWho(new Reference(p.getId()));
			oid1 = myAuditEventDao.create(auditEvent, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Patient p = new Patient();
			p.setId(IdType.newRandomUuid());
			p.addName().setFamily("Smith").addGiven("John");
			myPatientDao.create(p, mySrd);

			AuditEvent auditEvent = new AuditEvent();
			auditEvent.addEntity().setWhat(new Reference(p.getId()));
			oid2 = myAuditEventDao.create(auditEvent, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			// Create a dummy record so that an unconstrained query doesn't pass the test due to returning all the records
			myAuditEventDao.create(new AuditEvent(), mySrd);
		}

		String url = "/AuditEvent?patient.name=Smith";

		// execute
		myCaptureQueriesListener.clear();
		List<String> oids = myTestDaoSearch.searchForIds(url);
		myCaptureQueriesListener.logSelectQueries();

		// validate
		assertThat(oids).hasSize(2);
		assertThat(oids).containsExactly(oid1.getIdPart(), oid2.getIdPart());
	}

	@Test
	public void testShouldResolveATwoLinkChainWithContainedResources_CompoundReference() {

		// setup
		myStorageSettings.setIndexOnContainedResources(true);

		IIdType oid1;
		IIdType oid2;

		// AuditEvent should match both AuditEvent.agent.who and AuditEvent.entity.what
		{
			Patient p = new Patient();
			p.setId("p1");
			p.addName().setFamily("Smith").addGiven("John");

			AuditEvent auditEvent = new AuditEvent();
			auditEvent.addContained(p);
			auditEvent.addAgent().setWho(new Reference("#p1"));
			oid1 = myAuditEventDao.create(auditEvent, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Patient p = new Patient();
			p.setId("p2");
			p.addName().setFamily("Smith").addGiven("John");

			AuditEvent auditEvent = new AuditEvent();
			auditEvent.addContained(p);
			auditEvent.addEntity().setWhat(new Reference("#p2"));
			oid2 = myAuditEventDao.create(auditEvent, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			// Create a dummy record so that an unconstrained query doesn't pass the test due to returning all the records
			myAuditEventDao.create(new AuditEvent(), mySrd);
		}

		String url = "/AuditEvent?patient.name=Smith";
		logAllStringIndexes();

		// execute
		myCaptureQueriesListener.clear();
		List<String> oids = myTestDaoSearch.searchForIds(url);
		myCaptureQueriesListener.logSelectQueries();

		// validate
		assertThat(oids).hasSize(2);
		assertThat(oids).containsExactly(oid1.getIdPart(), oid2.getIdPart());
	}

	@Test
	public void testShouldResolveATwoLinkChainWithAContainedResource() {
		// setup
		myStorageSettings.setIndexOnContainedResources(true);

		IIdType oid1;

		{
			Patient p = new Patient();
			p.setId("pat");
			p.addName().setFamily("Smith").addGiven("John");

			Observation obs = new Observation();
			obs.getContained().add(p);

			obs.getCode().addCoding().setCode("29463-7").setSystem("http://loinc.org").setDisplay("Body Weight");
			obs.setStatus(Observation.ObservationStatus.FINAL);
			obs.setSubject(new Reference(p.getId()));
			obs.setValue(new Quantity(null, 67.1, "http://unitsofmeasure.org", "kg", "kg"));
			obs.getSubject().setReference("#pat");

			ourLog.debug("Resource: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));

			oid1 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			// Create a dummy record so that an unconstrained query doesn't pass the test due to returning the only record
			myCaptureQueriesListener.clear();
			myObservationDao.create(new Observation(), mySrd);
			myCaptureQueriesListener.logInsertQueries();
		}

		String url = "/Observation?subject.name=Smith";

		// execute
		List<String> oids = myTestDaoSearch.searchForIds(url);

		// validate
		assertThat(oids).hasSize(1);
		assertThat(oids).containsExactly(oid1.getIdPart());
	}

	@Test
	public void testShouldNotResolveATwoLinkChainWithAContainedResourceWhenContainedResourceIndexingIsTurnedOff() {
		// setup

		{
			Patient p = new Patient();
			p.setId("pat");
			p.addName().setFamily("Smith").addGiven("John");

			Observation obs = new Observation();
			obs.getContained().add(p);
			obs.getCode().setText("Observation 1");
			obs.setValue(new StringType("Test"));
			obs.getSubject().setReference("#pat");

            myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

            // Create a dummy record so that an unconstrained query doesn't pass the test due to returning the only record
			myObservationDao.create(new Observation(), mySrd);
		}

		String url = "/Observation?subject.name=Smith";

		// execute
		List<String> oids = myTestDaoSearch.searchForIds(url);

		// validate
		assertThat(oids).hasSize(0);
	}

	@Test
	@Disabled("Known limitation")
	public void testShouldResolveATwoLinkChainWithQualifiersWithAContainedResource() {
		// TODO: This test fails because of a known limitation in qualified searches over contained resources.
		//       Type information for intermediate resources in the chain is not being retained in the indexes.
		// setup
		myStorageSettings.setIndexOnContainedResources(true);

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

			Location loc = new Location();
			loc.setId("loc");
			loc.setName("Smith");

			Observation obs2 = new Observation();
			obs2.getContained().add(loc);
			obs2.getCode().setText("Observation 2");
			obs2.setValue(new StringType("Test"));
			obs2.getSubject().setReference("#loc");

			myObservationDao.create(obs2, mySrd);

			// Create a dummy record so that an unconstrained query doesn't pass the test due to returning the only record
			myObservationDao.create(new Observation(), mySrd);
		}

		String url = "/Observation?subject:Patient.name=Smith";

		// execute
		List<String> oids = myTestDaoSearch.searchForIds(url);

		// validate
		assertThat(oids).hasSize(1);
		assertThat(oids).containsExactly(oid1.getIdPart());
	}

	@Test
	public void testShouldResolveATwoLinkChainToAContainedReference() {
		// Adding support for this case in SMILE-3151

		// setup
		myStorageSettings.setIndexOnContainedResources(true);

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

			// Create a dummy record so that an unconstrained query doesn't pass the test due to returning the only record
			myObservationDao.create(new Observation(), mySrd);
		}

		String url = "/Observation?subject.organization=" + orgId.getValueAsString();

		// execute
		List<String> oids = myTestDaoSearch.searchForIds(url);

		// validate
		assertThat(oids).hasSize(1);
		assertThat(oids).containsExactly(oid1.getIdPart());
	}

	@Test
	public void testShouldResolveATwoLinkChainToAStandAloneReference() {
		// Adding support for this case in SMILE-3151

		// setup
		myStorageSettings.setIndexOnContainedResources(true);

		IIdType oid1;
		IIdType orgId;

		{
			Organization org = new Organization();
			org.setId(IdType.newRandomUuid());
			org.setName("HealthCo");
			orgId = myOrganizationDao.create(org, mySrd).getId();

			Patient p = new Patient();
			p.addName().setFamily("Smith").addGiven("John");
			p.getManagingOrganization().setReference(org.getId());
			myPatientDao.create(p, mySrd);

			Observation obs = new Observation();
			obs.getContained().add(p);
			obs.getCode().setText("Observation 1");
			obs.getSubject().setReference(p.getId());

			oid1 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			// Create a dummy record so that an unconstrained query doesn't pass the test due to returning the only record
			myObservationDao.create(new Observation(), mySrd);
		}

		String url = "/Observation?subject.organization=" + orgId.getValueAsString();

		// execute
		List<String> oids = myTestDaoSearch.searchForIds(url);

		// validate
		assertThat(oids).hasSize(1);
		assertThat(oids).containsExactly(oid1.getIdPart());
	}

	@Test
	public void testShouldResolveATwoLinkChainWithAContainedResource_CommonReference() {

		// setup
		myStorageSettings.setIndexOnContainedResources(true);

		IIdType oid1;

		{
			Encounter encounter = new Encounter();
			encounter.setId("enc");
			encounter.addIdentifier().setSystem("foo").setValue("bar");

			Observation obs = new Observation();
			obs.getCode().setText("Body Weight");
			obs.getCode().addCoding().setCode("obs2").setSystem("Some System").setDisplay("Body weight as measured by me");
			obs.setStatus(Observation.ObservationStatus.FINAL);
			obs.setValue(new Quantity(81));

			obs.addContained(encounter);
			obs.setEncounter(new Reference("#enc"));

			oid1 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			// Create a dummy record so that an unconstrained query doesn't pass the test due to returning the only record
			myObservationDao.create(new Observation(), mySrd);
		}

		String url = "/Observation?encounter.identifier=foo|bar";

		// execute
		myCaptureQueriesListener.clear();
		List<String> oids = myTestDaoSearch.searchForIds(url);
		myCaptureQueriesListener.logSelectQueries();

		// validate
		assertThat(oids).hasSize(1);
		assertThat(oids).containsExactly(oid1.getIdPart());
	}

	@Test
	public void testShouldResolveAThreeLinkChainWhereAllResourcesStandAloneWithoutContainedResourceIndexing() {

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

			// Create a dummy record so that an unconstrained query doesn't pass the test due to returning the only record
			Organization dummyOrg = new Organization();
			dummyOrg.setId(IdType.newRandomUuid());
			dummyOrg.setName("Dummy");
			myOrganizationDao.create(dummyOrg, mySrd);

			Patient dummyPatient = new Patient();
			dummyPatient.setId(IdType.newRandomUuid());
			dummyPatient.addName().setFamily("Jones").addGiven("Jane");
			dummyPatient.getManagingOrganization().setReference(dummyOrg.getId());
			myPatientDao.create(dummyPatient, mySrd);

			Observation dummyObs = new Observation();
			dummyObs.getCode().setText("Observation 2");
			dummyObs.getSubject().setReference(dummyPatient.getId());
			myObservationDao.create(dummyObs, mySrd);
		}

		String url = "/Observation?subject.organization.name=HealthCo";

		// execute
		List<String> oids = myTestDaoSearch.searchForIds(url);

		// validate
		assertThat(oids).hasSize(1);
		assertThat(oids).containsExactly(oid1.getIdPart());
	}

	@Test
	public void testShouldResolveAThreeLinkChainWhereAllResourcesStandAlone() {

		// setup
		myStorageSettings.setIndexOnContainedResources(true);

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

			// Create a dummy record so that an unconstrained query doesn't pass the test due to returning the only record
			Organization dummyOrg = new Organization();
			dummyOrg.setId(IdType.newRandomUuid());
			dummyOrg.setName("Dummy");
			myOrganizationDao.create(dummyOrg, mySrd);

			Patient dummyPatient = new Patient();
			dummyPatient.setId(IdType.newRandomUuid());
			dummyPatient.addName().setFamily("Jones").addGiven("Jane");
			dummyPatient.getManagingOrganization().setReference(dummyOrg.getId());
			myPatientDao.create(dummyPatient, mySrd);

			Observation dummyObs = new Observation();
			dummyObs.getCode().setText("Observation 2");
			dummyObs.getSubject().setReference(dummyPatient.getId());
			myObservationDao.create(dummyObs, mySrd);
		}

		String url = "/Observation?subject.organization.name=HealthCo";

		// execute
		List<String> oids = myTestDaoSearch.searchForIds(url);

		// validate
		assertThat(oids).hasSize(1);
		assertThat(oids).containsExactly(oid1.getIdPart());
	}

	@Test
	public void testShouldResolveAThreeLinkChainWithAContainedResourceAtTheEndOfTheChain() {
		// This is the case that is most relevant to SMILE-2899

		// setup
		myStorageSettings.setIndexOnContainedResources(true);

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

			// Create a dummy record so that an unconstrained query doesn't pass the test due to returning the only record
			myObservationDao.create(new Observation(), mySrd);
		}

		String url = "/Observation?subject.organization.name=HealthCo";

		// execute
		List<String> oids = myTestDaoSearch.searchForIds(url);

		// validate
		assertThat(oids).hasSize(1);
		assertThat(oids).containsExactly(oid1.getIdPart());
	}

	@Test
	public void testShouldResolveAThreeLinkChainWithAContainedResourceAtTheEndOfTheChain_CommonReference() {

		// setup
		myStorageSettings.setIndexOnContainedResources(true);

		IIdType oid1;

		{
			Patient p = new Patient();
			p.setId("pat");
			p.addName().setFamily("Smith").addGiven("John");

			Encounter encounter = new Encounter();
			encounter.addContained(p);
			encounter.setId(IdType.newRandomUuid());
			encounter.addIdentifier().setSystem("foo").setValue("bar");
			encounter.setSubject(new Reference("#pat"));
			myEncounterDao.create(encounter, mySrd);

			Observation obs = new Observation();
			obs.getCode().setText("Body Weight");
			obs.getCode().addCoding().setCode("obs2").setSystem("Some System").setDisplay("Body weight as measured by me");
			obs.setStatus(Observation.ObservationStatus.FINAL);
			obs.setValue(new Quantity(81));
			obs.setSubject(new Reference(p.getId()));
			obs.setEncounter(new Reference(encounter.getId()));
			oid1 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			// Create a dummy record so that an unconstrained query doesn't pass the test due to returning the only record
			myObservationDao.create(new Observation(), mySrd);
		}

		String url = "/Observation?encounter.patient.name=Smith";

		// execute
		myCaptureQueriesListener.clear();
		List<String> oids = myTestDaoSearch.searchForIds(url);
		myCaptureQueriesListener.logSelectQueries();

		// validate
		assertThat(oids).hasSize(1);
		assertThat(oids).containsExactly(oid1.getIdPart());
	}

	@Test
	public void testShouldResolveAThreeLinkChainWithAContainedResourceAtTheBeginningOfTheChain() {
		// Adding support for this case in SMILE-3151

		// setup
		myStorageSettings.setIndexOnContainedResources(true);

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

			// Create a dummy record so that an unconstrained query doesn't pass the test due to returning the only record
			myObservationDao.create(new Observation(), mySrd);
		}

		String url = "/Observation?subject.organization.name=HealthCo";

		// execute
		List<String> oids = myTestDaoSearch.searchForIds(url);

		// validate
		assertThat(oids).hasSize(1);
		assertThat(oids).containsExactly(oid1.getIdPart());
	}

	@Test
	public void testShouldResolveAThreeLinkChainWithAContainedResourceAtTheBeginningOfTheChain_CommonReference() {

		// setup
		myStorageSettings.setIndexOnContainedResources(true);

		IIdType oid1;

		{
			Patient p = new Patient();
			p.setId(IdType.newRandomUuid());
			p.addName().setFamily("Smith").addGiven("John");
			myPatientDao.create(p, mySrd);

			Encounter encounter = new Encounter();
			encounter.setId("enc");
			encounter.addIdentifier().setSystem("foo").setValue("bar");
			encounter.setSubject(new Reference(p.getId()));

			Observation obs = new Observation();
			obs.addContained(encounter);
			obs.getCode().setText("Body Weight");
			obs.getCode().addCoding().setCode("obs2").setSystem("Some System").setDisplay("Body weight as measured by me");
			obs.setStatus(Observation.ObservationStatus.FINAL);
			obs.setValue(new Quantity(81));
			obs.setEncounter(new Reference("#enc"));
			oid1 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			// Create a dummy record so that an unconstrained query doesn't pass the test due to returning the only record
			myObservationDao.create(new Observation(), mySrd);
		}

		String url = "/Observation?encounter.identifier=foo|bar";

		// execute
		myCaptureQueriesListener.clear();
		List<String> oids = myTestDaoSearch.searchForIds(url);
		myCaptureQueriesListener.logSelectQueries();

		// validate
		assertThat(oids).hasSize(1);
		assertThat(oids).containsExactly(oid1.getIdPart());
	}

	@Test
	public void testShouldNotResolveAThreeLinkChainWithAllContainedResourcesWhenRecursiveContainedIndexesAreDisabled() {

		// setup
		myStorageSettings.setIndexOnContainedResources(true);

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

            myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

            // Create a dummy record so that an unconstrained query doesn't pass the test due to returning the only record
			myObservationDao.create(new Observation(), mySrd);
		}

		String url = "/Observation?subject.organization.name=HealthCo";

		// execute
		List<String> oids = myTestDaoSearch.searchForIds(url);

		// validate
		assertThat(oids).hasSize(0);
	}

	@Test
	public void testShouldResolveAThreeLinkChainWithAllContainedResources() {

		// setup
		myStorageSettings.setIndexOnContainedResources(true);
		myStorageSettings.setIndexOnContainedResourcesRecursively(true);

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

			// Create a dummy record so that an unconstrained query doesn't pass the test due to returning the only record
			myObservationDao.create(new Observation(), mySrd);
		}

		String url = "/Observation?subject.organization.name=HealthCo";
		logAllStringIndexes();

		// execute
		myCaptureQueriesListener.clear();
		List<String> oids = myTestDaoSearch.searchForIds(url);
		myCaptureQueriesListener.logSelectQueries();

		// validate
		assertThat(oids).hasSize(1);
		assertThat(oids).containsExactly(oid1.getIdPart());
	}

	@Test
	public void testShouldResolveAThreeLinkChainWithQualifiersWhereAllResourcesStandAlone() {

		// setup
		myStorageSettings.setIndexOnContainedResources(true);

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

			// Create a dummy record so that an unconstrained query doesn't pass the test due to returning the only record
			myObservationDao.create(new Observation(), mySrd);
		}

		String url = "/Observation?subject:Patient.organization:Organization.name=HealthCo";

		// execute
		List<String> oids = myTestDaoSearch.searchForIds(url);

		// validate
		assertThat(oids).hasSize(1);
		assertThat(oids).containsExactly(oid1.getIdPart());
	}

	@Test
	public void testShouldResolveAThreeLinkChainWithQualifiersWithAContainedResourceAtTheEndOfTheChain() {
		// This is the case that is most relevant to SMILE-2899

		// setup
		myStorageSettings.setIndexOnContainedResources(true);

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

			// Create a dummy record so that an unconstrained query doesn't pass the test due to returning the only record
			myObservationDao.create(new Observation(), mySrd);
		}

		String url = "/Observation?subject:Patient.organization:Organization.name=HealthCo";

		// execute
		List<String> oids = myTestDaoSearch.searchForIds(url);

		// validate
		assertThat(oids).hasSize(1);
		assertThat(oids).containsExactly(oid1.getIdPart());
	}

	@Test
	public void testShouldResolveAThreeLinkChainWithQualifiersWithAContainedResourceAtTheBeginning() {
		// Adding support for this case in SMILE-3151

		// setup
		myStorageSettings.setIndexOnContainedResources(true);

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

			// Create a dummy record so that an unconstrained query doesn't pass the test due to returning the only record
			myObservationDao.create(new Observation(), mySrd);
		}

		String url = "/Observation?subject:Patient.organization:Organization.name=HealthCo";

		// execute
		myCaptureQueriesListener.clear();
		List<String> oids = myTestDaoSearch.searchForIds(url);
		myCaptureQueriesListener.logSelectQueries();

		// validate
		assertThat(oids).hasSize(1);
		assertThat(oids).containsExactly(oid1.getIdPart());
	}

	@Test
	@Disabled("Known limitation")
	public void testShouldResolveAThreeLinkChainWithQualifiersWithAContainedResourceAtTheBeginning_NotDistinctSourcePaths() {
		// TODO: This test fails because of a known limitation in qualified searches over contained resources.
		//       Type information for intermediate resources in the chain is not being retained in the indexes.

		// Adding support for this case in SMILE-3151

		// setup
		myStorageSettings.setIndexOnContainedResources(true);

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

			Location loc = new Location();
			loc.setId("loc");
			loc.getManagingOrganization().setReference(org.getId());

			Observation obs2 = new Observation();
			obs2.getContained().add(loc);
			obs2.getCode().setText("Observation 2");
			obs2.getSubject().setReference("#loc");

			myObservationDao.create(obs2, mySrd);

			// Create a dummy record so that an unconstrained query doesn't pass the test due to returning the only record
			myObservationDao.create(new Observation(), mySrd);
		}

		String url = "/Observation?subject:Patient.organization:Organization.name=HealthCo";

		// execute
		myCaptureQueriesListener.clear();
		List<String> oids = myTestDaoSearch.searchForIds(url);
		myCaptureQueriesListener.logSelectQueries();

		// validate
		assertThat(oids).hasSize(1);
		assertThat(oids).containsExactly(oid1.getIdPart());
	}

	@Test
	@Disabled("Known limitation")
	public void testShouldResolveAThreeLinkChainWithQualifiersWithAllContainedResources() {
		// TODO: This test fails because of a known limitation in qualified searches over contained resources.
		//       Type information for intermediate resources in the chain is not being retained in the indexes.

		// setup
		myStorageSettings.setIndexOnContainedResources(true);
		myStorageSettings.setIndexOnContainedResourcesRecursively(true);

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

			Organization org2 = new Organization();
			org2.setId("org");
			org2.setName("HealthCo");

			Device d = new Device();
			d.setId("dev");
			d.getOwner().setReference("#org");

			Observation obs2 = new Observation();
			obs2.getContained().add(d);
			obs2.getContained().add(org2);
			obs2.getCode().setText("Observation 2");
			obs2.getSubject().setReference("#dev");

			myObservationDao.create(obs2, mySrd);

			// Create a dummy record so that an unconstrained query doesn't pass the test due to returning the only record
			myObservationDao.create(new Observation(), mySrd);
		}

		String url = "/Observation?subject:Patient.organization:Organization.name=HealthCo";

		// execute
		myCaptureQueriesListener.clear();
		List<String> oids = myTestDaoSearch.searchForIds(url);
		myCaptureQueriesListener.logSelectQueries();

		// validate
		assertThat(oids).hasSize(1);
		assertThat(oids).containsExactly(oid1.getIdPart());
	}

	@Test
	public void testShouldResolveAFourLinkChainWhereAllResourcesStandAlone() {

		// setup
		myStorageSettings.setIndexOnContainedResources(true);

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

			// Create a dummy record so that an unconstrained query doesn't pass the test due to returning the only record
			myObservationDao.create(new Observation(), mySrd);
		}

		String url = "/Observation?subject.organization.partof.name=HealthCo";

		// execute
		List<String> oids = myTestDaoSearch.searchForIds(url);

		// validate
		assertThat(oids).hasSize(1);
		assertThat(oids).containsExactly(oid1.getIdPart());
	}

	@Test
	public void testShouldResolveAFourLinkChainWhereTheLastReferenceIsContained() {

		// setup
		myStorageSettings.setIndexOnContainedResources(true);

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

			// Create a dummy record so that an unconstrained query doesn't pass the test due to returning the only record
			myObservationDao.create(new Observation(), mySrd);
		}

		String url = "/Observation?subject.organization.partof.name=HealthCo";

		// execute
		List<String> oids = myTestDaoSearch.searchForIds(url);

		// validate
		assertThat(oids).hasSize(1);
		assertThat(oids).containsExactly(oid1.getIdPart());
	}

	@Test
	public void testShouldResolveAFourLinkChainWhereTheLastTwoReferencesAreContained() {

		// setup
		myStorageSettings.setIndexOnContainedResources(true);
		myStorageSettings.setIndexOnContainedResourcesRecursively(true);
		IIdType oid1;

		{
			Organization org = new Organization();
			org.setId("parent");
			org.setName("HealthCo");

			Organization partOfOrg = new Organization();
			partOfOrg.setId("child");
			partOfOrg.getPartOf().setReference("#parent");

			Patient p = new Patient();
			p.getContained().add(org);
			p.getContained().add(partOfOrg);
			p.setId(IdType.newRandomUuid());
			p.addName().setFamily("Smith").addGiven("John");
			p.getManagingOrganization().setReference("#child");
			myPatientDao.create(p, mySrd);

			Observation obs = new Observation();
			obs.getCode().setText("Observation 1");
			obs.getSubject().setReference(p.getId());

			oid1 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			// Create a dummy record so that an unconstrained query doesn't pass the test due to returning the only record
			myObservationDao.create(new Observation(), mySrd);
		}

		String url = "/Observation?subject.organization.partof.name=HealthCo";

		// execute
		List<String> oids = myTestDaoSearch.searchForIds(url);

		// validate
		assertThat(oids).hasSize(1);
		assertThat(oids).containsExactly(oid1.getIdPart());
	}

	@Test
	public void testShouldResolveAFourLinkChainWithAContainedResourceInTheMiddle() {

		// setup
		myStorageSettings.setIndexOnContainedResources(true);

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

			// Create a dummy record so that an unconstrained query doesn't pass the test due to returning the only record
			myObservationDao.create(new Observation(), mySrd);
		}

		String url = "/Observation?subject.organization.partof.name=HealthCo";

		// execute
		myCaptureQueriesListener.clear();
		List<String> oids = myTestDaoSearch.searchForIds(url);
		myCaptureQueriesListener.logSelectQueries();

		// validate
		assertThat(oids).hasSize(1);
		assertThat(oids).containsExactly(oid1.getIdPart());
	}

	@Test
	public void testShouldResolveAFourLinkChainWhereTheFirstTwoReferencesAreContained() {

		// setup
		myStorageSettings.setIndexOnContainedResources(true);
		myStorageSettings.setIndexOnContainedResourcesRecursively(true);
		IIdType oid1;

		{
			Organization org = new Organization();
			org.setId(IdType.newRandomUuid());
			org.setName("HealthCo");
			myOrganizationDao.create(org, mySrd);

			Organization partOfOrg = new Organization();
			partOfOrg.setId("child");
			partOfOrg.getPartOf().setReference(org.getId());

			Patient p = new Patient();
			p.setId("pat");
			p.addName().setFamily("Smith").addGiven("John");
			p.getManagingOrganization().setReference("#child");

			Observation obs = new Observation();
			obs.getContained().add(org);
			obs.getContained().add(partOfOrg);
			obs.getContained().add(p);
			obs.getCode().setText("Observation 1");
			obs.getSubject().setReference("#pat");

			oid1 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			// Create a dummy record so that an unconstrained query doesn't pass the test due to returning the only record
			myObservationDao.create(new Observation(), mySrd);
		}

		String url = "/Observation?subject.organization.partof.name=HealthCo";

		// execute
		List<String> oids = myTestDaoSearch.searchForIds(url);

		// validate
		assertThat(oids).hasSize(1);
		assertThat(oids).containsExactly(oid1.getIdPart());
	}

	@Test
	public void testShouldResolveAFourLinkChainWhereTheFirstReferenceAndTheLastReferenceAreContained() {

		// setup
		myStorageSettings.setIndexOnContainedResources(true);
		myStorageSettings.setIndexOnContainedResourcesRecursively(true);
		IIdType oid1;

		{
			Organization org = new Organization();
			org.setId("parent");
			org.setName("HealthCo");

			Organization partOfOrg = new Organization();
			partOfOrg.getContained().add(org);
			partOfOrg.setId(IdType.newRandomUuid());
			partOfOrg.getPartOf().setReference("#parent");
			myOrganizationDao.create(partOfOrg, mySrd);

			Patient p = new Patient();
			p.setId("pat");
			p.addName().setFamily("Smith").addGiven("John");
			p.getManagingOrganization().setReference(partOfOrg.getId());

			Observation obs = new Observation();
			obs.getContained().add(p);
			obs.getCode().setText("Observation 1");
			obs.getSubject().setReference("#pat");

			oid1 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			// Create a dummy record so that an unconstrained query doesn't pass the test due to returning the only record
			myObservationDao.create(new Observation(), mySrd);
		}

		String url = "/Observation?subject.organization.partof.name=HealthCo";

		// execute
		List<String> oids = myTestDaoSearch.searchForIds(url);

		// validate
		assertThat(oids).hasSize(1);
		assertThat(oids).containsExactly(oid1.getIdPart());
	}

	@Test
	public void testShouldResolveAFourLinkChainWhereAllReferencesAreContained() {

		// setup
		myStorageSettings.setIndexOnContainedResources(true);
		myStorageSettings.setIndexOnContainedResourcesRecursively(true);
		IIdType oid1;

		{
			Organization org = new Organization();
			org.setId("parent");
			org.setName("HealthCo");

			Organization partOfOrg = new Organization();
			partOfOrg.setId("child");
			partOfOrg.getPartOf().setReference("#parent");

			Patient p = new Patient();
			p.setId("pat");
			p.addName().setFamily("Smith").addGiven("John");
			p.getManagingOrganization().setReference("#child");

			Observation obs = new Observation();
			obs.getContained().add(org);
			obs.getContained().add(partOfOrg);
			obs.getContained().add(p);
			obs.getCode().setText("Observation 1");
			obs.getSubject().setReference("#pat");

			oid1 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			// Create a dummy record so that an unconstrained query doesn't pass the test due to returning the only record
			myObservationDao.create(new Observation(), mySrd);
		}

		String url = "/Observation?subject.organization.partof.name=HealthCo";

		logAllStringIndexes();

		// execute
		myCaptureQueriesListener.clear();
		List<String> oids = myTestDaoSearch.searchForIds(url);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();

		// validate
		assertThat(oids).hasSize(1);
		assertThat(oids).containsExactly(oid1.getIdPart());
	}

	@Test
	public void testShouldThrowAnExceptionForAFiveLinkChain() {

		// setup
		myStorageSettings.setIndexOnContainedResources(true);
		myStorageSettings.setIndexOnContainedResourcesRecursively(true);

		String url = "/Observation?subject.organization.partof.partof.name=HealthCo";

		try {
			// execute
			myTestDaoSearch.searchForIds(url);
			fail("Expected an exception to be thrown");
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(2007) + "The search chain subject.organization.partof.partof.name is too long. Only chains up to three references are supported.", e.getMessage());
		}
	}

	@Test
	public void testQueryStructure() {

		// With indexing of contained resources turned off, we should not see UNION clauses in the query
		countUnionStatementsInGeneratedQuery("/Observation?patient.name=Smith", 0);
		countUnionStatementsInGeneratedQuery("/Observation?patient.organization.name=Smith", 0);
		countUnionStatementsInGeneratedQuery("/Observation?patient.organization.partof.name=Smith", 0);

		// With indexing of contained resources turned on, we take the UNION of several subselects that handle the different patterns of containment
		//  Keeping in mind that the number of clauses is one greater than the number of UNION keywords,
		//  this increases as the chain grows longer according to the Fibonacci sequence: (2, 3, 5, 8, 13)
		myStorageSettings.setIndexOnContainedResources(true);
		countUnionStatementsInGeneratedQuery("/Observation?patient.name=Smith", 1);
		countUnionStatementsInGeneratedQuery("/Observation?patient.organization.name=Smith", 2);
		countUnionStatementsInGeneratedQuery("/Observation?patient.organization.partof.name=Smith", 4);

		// With recursive indexing of contained resources turned on, even more containment patterns are considered
		//  This increases as the chain grows longer as powers of 2: (2, 4, 8, 16, 32)
		myStorageSettings.setIndexOnContainedResourcesRecursively(true);
		countUnionStatementsInGeneratedQuery("/Observation?patient.name=Smith", 1);
		countUnionStatementsInGeneratedQuery("/Observation?patient.organization.name=Smith", 3);
		countUnionStatementsInGeneratedQuery("/Observation?patient.organization.partof.name=Smith", 7);

		// If a reference in the chain has multiple potential target resource types, the number of subselects increases
		// Note: This previously had 3 unions but 2 of the selects within were duplicates of each other
		countUnionStatementsInGeneratedQuery("/Observation?subject.name=Smith", 2);

		// If such a reference if qualified to restrict the type, the number goes back down
		countUnionStatementsInGeneratedQuery("/Observation?subject:Location.name=Smith", 1);
	}

	private void countUnionStatementsInGeneratedQuery(String theUrl, int theExpectedNumberOfUnions) {
		myCaptureQueriesListener.clear();
		myTestDaoSearch.searchForIds(theUrl);
		List<SqlQuery> selectQueries = myCaptureQueriesListener.getSelectQueriesForCurrentThread();
		assertThat(selectQueries).hasSize(1);

		String sqlQuery = selectQueries.get(0).getSql(true, true).toLowerCase();
		assertThat(countMatches(sqlQuery, "union")).as(sqlQuery).isEqualTo(theExpectedNumberOfUnions);
	}

}
