package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.SqlQuery;
import ca.uhn.fhir.jpa.util.TestUtil;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.ReferenceParam;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.ServiceRequest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class FhirResourceDaoR4QueryCountTest extends BaseJpaR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4QueryCountTest.class);

	@After
	public void afterResetDao() {
		myDaoConfig.setResourceMetaCountHardLimit(new DaoConfig().getResourceMetaCountHardLimit());
		myDaoConfig.setIndexMissingFields(new DaoConfig().getIndexMissingFields());
		myDaoConfig.setDeleteEnabled(new DaoConfig().isDeleteEnabled());
	}

	@Before
	public void before() {
		myInterceptorRegistry.registerInterceptor(myInterceptor);
	}


	@Test
	public void testUpdateWithNoChanges() {
		IIdType id = runInTransaction(() -> {
			Patient p = new Patient();
			p.addIdentifier().setSystem("urn:system").setValue("2");
			return myPatientDao.create(p).getId().toUnqualified();
		});

		myCaptureQueriesListener.clear();
		runInTransaction(() -> {
			Patient p = new Patient();
			p.setId(id.getIdPart());
			p.addIdentifier().setSystem("urn:system").setValue("2");
			myPatientDao.update(p);
		});
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(3, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getUpdateQueriesForCurrentThread().size());
		assertThat(myCaptureQueriesListener.getInsertQueriesForCurrentThread(), empty());
		assertThat(myCaptureQueriesListener.getDeleteQueriesForCurrentThread(), empty());
	}


	@Test
	public void testUpdateWithChanges() {
		IIdType id = runInTransaction(() -> {
			Patient p = new Patient();
			p.addIdentifier().setSystem("urn:system").setValue("2");
			return myPatientDao.create(p).getId().toUnqualified();
		});

		myCaptureQueriesListener.clear();
		runInTransaction(() -> {
			Patient p = new Patient();
			p.setId(id.getIdPart());
			p.addIdentifier().setSystem("urn:system").setValue("3");
			myPatientDao.update(p).getResource();
		});
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(3, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(2, myCaptureQueriesListener.getUpdateQueriesForCurrentThread().size());
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertEquals(1, myCaptureQueriesListener.getInsertQueriesForCurrentThread().size());
		myCaptureQueriesListener.logDeleteQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getDeleteQueriesForCurrentThread().size());
	}

	@Test
	public void testRead() {
		IIdType id = runInTransaction(() -> {
			Patient p = new Patient();
			p.addIdentifier().setSystem("urn:system").setValue("2");
			return myPatientDao.create(p).getId().toUnqualified();
		});

		myCaptureQueriesListener.clear();
		runInTransaction(() -> {
			myPatientDao.read(id.toVersionless());
		});
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(2, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getUpdateQueriesForCurrentThread().size());
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getInsertQueriesForCurrentThread().size());
		myCaptureQueriesListener.logDeleteQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getDeleteQueriesForCurrentThread().size());
	}

	@Test
	public void testVRead() {
		IIdType id = runInTransaction(() -> {
			Patient p = new Patient();
			p.addIdentifier().setSystem("urn:system").setValue("2");
			return myPatientDao.create(p).getId().toUnqualified();
		});

		myCaptureQueriesListener.clear();
		runInTransaction(() -> {
			myPatientDao.read(id.withVersion("1"));
		});
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(2, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getUpdateQueriesForCurrentThread().size());
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getInsertQueriesForCurrentThread().size());
		myCaptureQueriesListener.logDeleteQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getDeleteQueriesForCurrentThread().size());
	}


	@Test
	public void testCreateWithClientAssignedId() {
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.DISABLED);

		runInTransaction(() -> {
			Patient p = new Patient();
			p.getMaritalStatus().setText("123");
			return myPatientDao.create(p).getId().toUnqualified();
		});

		myCaptureQueriesListener.clear();

		runInTransaction(() -> {
			Patient p = new Patient();
			p.setId("AAA");
			p.getMaritalStatus().setText("123");
			return myPatientDao.update(p).getId().toUnqualified();
		});

		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(1, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getUpdateQueriesForCurrentThread().size());
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertEquals(4, myCaptureQueriesListener.getInsertQueriesForCurrentThread().size());
		myCaptureQueriesListener.logDeleteQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getDeleteQueriesForCurrentThread().size());
	}


	@Test
	public void testReferenceToForcedId() {
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.DISABLED);

		Patient patient = new Patient();
		patient.setId("P");
		patient.setActive(true);

		myCaptureQueriesListener.clear();
		myPatientDao.update(patient);

		/*
		 * Add a resource with a forced ID target link
		 */

		myCaptureQueriesListener.clear();
		Observation observation = new Observation();
		observation.getSubject().setReference("Patient/P");
		myObservationDao.create(observation);
		myCaptureQueriesListener.logAllQueriesForCurrentThread();
		// select: lookup forced ID
		assertEquals(1, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());
		// insert to: HFJ_RESOURCE, HFJ_RES_VER, HFJ_RES_LINK
		assertEquals(3, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());

		/*
		 * Add another
		 */

		myCaptureQueriesListener.clear();
		observation = new Observation();
		observation.getSubject().setReference("Patient/P");
		myObservationDao.create(observation);
		// select: lookup forced ID
		assertEquals(1, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());
		// insert to: HFJ_RESOURCE, HFJ_RES_VER, HFJ_RES_LINK
		assertEquals(3, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());

	}


	@Test
	public void testReferenceToForcedId_DeletesDisabled() {
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.DISABLED);
		myDaoConfig.setDeleteEnabled(false);

		Patient patient = new Patient();
		patient.setId("P");
		patient.setActive(true);

		myCaptureQueriesListener.clear();
		myPatientDao.update(patient);

		/*
		 * Add a resource with a forced ID target link
		 */

		myCaptureQueriesListener.clear();
		Observation observation = new Observation();
		observation.getSubject().setReference("Patient/P");
		myObservationDao.create(observation);
		myCaptureQueriesListener.logAllQueriesForCurrentThread();
		// select: lookup forced ID
		assertEquals(1, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertNoPartitionSelectors();
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());
		// insert to: HFJ_RESOURCE, HFJ_RES_VER, HFJ_RES_LINK
		assertEquals(3, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());

		/*
		 * Add another
		 */

		myCaptureQueriesListener.clear();
		observation = new Observation();
		observation.getSubject().setReference("Patient/P");
		myObservationDao.create(observation);
		// select: no lookups needed because of cache
		assertEquals(0, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());
		// insert to: HFJ_RESOURCE, HFJ_RES_VER, HFJ_RES_LINK
		assertEquals(3, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());

	}

	public void assertNoPartitionSelectors() {
		List<SqlQuery> selectQueries = myCaptureQueriesListener.getSelectQueriesForCurrentThread();
		for (SqlQuery next : selectQueries) {
			assertEquals(0, StringUtils.countMatches(next.getSql(true, true).toLowerCase(), "partition_id is null"));
			assertEquals(0, StringUtils.countMatches(next.getSql(true, true).toLowerCase(), "partition_id="));
			assertEquals(0, StringUtils.countMatches(next.getSql(true, true).toLowerCase(), "partition_id ="));
		}
	}

	@Test
	public void testHistory_Server() {
		runInTransaction(() -> {
			Patient p = new Patient();
			p.setId("A");
			p.addIdentifier().setSystem("urn:system").setValue("1");
			myPatientDao.update(p).getId().toUnqualified();

			p = new Patient();
			p.setId("B");
			p.addIdentifier().setSystem("urn:system").setValue("2");
			myPatientDao.update(p).getId().toUnqualified();

			p = new Patient();
			p.addIdentifier().setSystem("urn:system").setValue("2");
			myPatientDao.create(p).getId().toUnqualified();
		});

		myCaptureQueriesListener.clear();
		runInTransaction(() -> {
			IBundleProvider history = mySystemDao.history(null, null, null);
			assertEquals(3, history.getResources(0, 99).size());
		});
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		// Perform count, Search history table, resolve forced IDs
		assertEquals(3, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
		assertNoPartitionSelectors();
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getUpdateQueriesForCurrentThread().size());
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getInsertQueriesForCurrentThread().size());
		myCaptureQueriesListener.logDeleteQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getDeleteQueriesForCurrentThread().size());

		// Second time should leverage forced ID cache
		myCaptureQueriesListener.clear();
		runInTransaction(() -> {
			IBundleProvider history = mySystemDao.history(null, null, null);
			assertEquals(3, history.getResources(0, 99).size());
		});
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		// Perform count, Search history table
		assertEquals(2, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getUpdateQueriesForCurrentThread().size());
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getInsertQueriesForCurrentThread().size());
		myCaptureQueriesListener.logDeleteQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getDeleteQueriesForCurrentThread().size());
	}


	/**
	 * This could definitely stand to be optimized some, since we load tags individually
	 * for each resource
	 */
	@Test
	public void testHistory_Server_WithTags() {
		runInTransaction(() -> {
			Patient p = new Patient();
			p.getMeta().addTag("system", "code1", "displaY1");
			p.getMeta().addTag("system", "code2", "displaY2");
			p.setId("A");
			p.addIdentifier().setSystem("urn:system").setValue("1");
			myPatientDao.update(p).getId().toUnqualified();

			p = new Patient();
			p.getMeta().addTag("system", "code1", "displaY1");
			p.getMeta().addTag("system", "code2", "displaY2");
			p.setId("B");
			p.addIdentifier().setSystem("urn:system").setValue("2");
			myPatientDao.update(p).getId().toUnqualified();

			p = new Patient();
			p.getMeta().addTag("system", "code1", "displaY1");
			p.getMeta().addTag("system", "code2", "displaY2");
			p.addIdentifier().setSystem("urn:system").setValue("2");
			myPatientDao.create(p).getId().toUnqualified();
		});

		myCaptureQueriesListener.clear();
		runInTransaction(() -> {
			IBundleProvider history = mySystemDao.history(null, null, null);
			assertEquals(3, history.getResources(0, 3).size());
		});
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		// Perform count, Search history table, resolve forced IDs, load tags (x3)
		assertEquals(6, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getUpdateQueriesForCurrentThread().size());
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getInsertQueriesForCurrentThread().size());
		myCaptureQueriesListener.logDeleteQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getDeleteQueriesForCurrentThread().size());

		// Second time should leverage forced ID cache
		myCaptureQueriesListener.clear();
		runInTransaction(() -> {
			IBundleProvider history = mySystemDao.history(null, null, null);
			assertEquals(3, history.getResources(0, 3).size());
		});
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		// Perform count, Search history table, load tags (x3)
		assertEquals(5, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getUpdateQueriesForCurrentThread().size());
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getInsertQueriesForCurrentThread().size());
		myCaptureQueriesListener.logDeleteQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getDeleteQueriesForCurrentThread().size());
	}


	@Test
	public void testSearchUsingForcedIdReference() {

		Patient patient = new Patient();
		patient.setId("P");
		patient.setActive(true);
		myPatientDao.update(patient);

		Observation obs = new Observation();
		obs.getSubject().setReference("Patient/P");
		myObservationDao.update(obs);

		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add("subject", new ReferenceParam("Patient/P"));

		myCaptureQueriesListener.clear();
		assertEquals(1, myObservationDao.search(map).size().intValue());
		// Resolve forced ID, Perform search, load result
		assertEquals(3, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertNoPartitionSelectors();
		assertEquals(0, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

		/*
		 * Again
		 */

		myCaptureQueriesListener.clear();
		assertEquals(1, myObservationDao.search(map).size().intValue());
		myCaptureQueriesListener.logAllQueriesForCurrentThread();
		// Resolve forced ID, Perform search, load result (this time we reuse the cached forced-id resolution)
		assertEquals(2, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());
	}


	@Test
	public void testSearchUsingForcedIdReference_DeletedDisabled() {
		myDaoConfig.setDeleteEnabled(false);

		Patient patient = new Patient();
		patient.setId("P");
		patient.setActive(true);
		myPatientDao.update(patient);

		Observation obs = new Observation();
		obs.getSubject().setReference("Patient/P");
		myObservationDao.update(obs);

		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add("subject", new ReferenceParam("Patient/P"));

		myCaptureQueriesListener.clear();
		assertEquals(1, myObservationDao.search(map).size().intValue());
		myCaptureQueriesListener.logAllQueriesForCurrentThread();
		// Resolve forced ID, Perform search, load result
		assertEquals(3, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

		/*
		 * Again
		 */

		myCaptureQueriesListener.clear();
		assertEquals(1, myObservationDao.search(map).size().intValue());
		myCaptureQueriesListener.logAllQueriesForCurrentThread();
		// (NO resolve forced ID), Perform search, load result
		assertEquals(2, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());
	}
	
	
	@Test
	public void testTransactionWithMultipleReferences() {
		Bundle input = new Bundle();
		
		Patient patient = new Patient();
		patient.setId(IdType.newRandomUuid());
		patient.setActive(true);
		input.addEntry()
			.setFullUrl(patient.getId())
			.setResource(patient)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("Patient");

		Practitioner practitioner = new Practitioner();
		practitioner.setId(IdType.newRandomUuid());
		practitioner.setActive(true);
		input.addEntry()
			.setFullUrl(practitioner.getId())
			.setResource(practitioner)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("Practitioner");

		ServiceRequest sr = new ServiceRequest();
		sr.getSubject().setReference(patient.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		input.addEntry()
			.setFullUrl(sr.getId())
			.setResource(sr)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("ServiceRequest");

		sr = new ServiceRequest();
		sr.getSubject().setReference(patient.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		input.addEntry()
			.setFullUrl(sr.getId())
			.setResource(sr)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("ServiceRequest");

		myCaptureQueriesListener.clear();
		Bundle output = mySystemDao.transaction(mySrd, input);
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertEquals(4, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(1, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

	}


	@Test
	public void testTransactionWithMultiplePreExistingReferences_ForcedId() {
		myDaoConfig.setDeleteEnabled(true);

		Patient patient = new Patient();
		patient.setId("Patient/A");
		patient.setActive(true);
		myPatientDao.update(patient);

		Practitioner practitioner = new Practitioner();
		practitioner.setId("Practitioner/B");
		practitioner.setActive(true);
		myPractitionerDao.update(practitioner);

		// Create transaction

		Bundle input = new Bundle();

		ServiceRequest sr = new ServiceRequest();
		sr.getSubject().setReference(patient.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		input.addEntry()
			.setFullUrl(sr.getId())
			.setResource(sr)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("ServiceRequest");

		sr = new ServiceRequest();
		sr.getSubject().setReference(patient.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		input.addEntry()
			.setFullUrl(sr.getId())
			.setResource(sr)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("ServiceRequest");

		myCaptureQueriesListener.clear();
		Bundle output = mySystemDao.transaction(mySrd, input);
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		// Lookup the two existing IDs to make sure they are legit
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(2, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(3, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(2, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

		// Do the same a second time - Deletes are enabled so we expect to have to resolve the
		// targets again to make sure they weren't deleted

		input = new Bundle();

		sr = new ServiceRequest();
		sr.getSubject().setReference(patient.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		input.addEntry()
			.setFullUrl(sr.getId())
			.setResource(sr)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("ServiceRequest");

		sr = new ServiceRequest();
		sr.getSubject().setReference(patient.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		input.addEntry()
			.setFullUrl(sr.getId())
			.setResource(sr)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("ServiceRequest");

		myCaptureQueriesListener.clear();
		output = mySystemDao.transaction(mySrd, input);
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		// Lookup the two existing IDs to make sure they are legit
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(2, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(3, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(2, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

	}

	@Test
	public void testTransactionWithMultiplePreExistingReferences_Numeric() {
		myDaoConfig.setDeleteEnabled(true);

		Patient patient = new Patient();
		patient.setActive(true);
		IIdType patientId = myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		Practitioner practitioner = new Practitioner();
		practitioner.setActive(true);
		IIdType practitionerId = myPractitionerDao.create(practitioner).getId().toUnqualifiedVersionless();

		// Create transaction
		Bundle input = new Bundle();

		ServiceRequest sr = new ServiceRequest();
		sr.getSubject().setReferenceElement(patientId);
		sr.addPerformer().setReferenceElement(practitionerId);
		sr.addPerformer().setReferenceElement(practitionerId);
		input.addEntry()
			.setFullUrl(sr.getId())
			.setResource(sr)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("ServiceRequest");

		sr = new ServiceRequest();
		sr.getSubject().setReferenceElement(patientId);
		sr.addPerformer().setReferenceElement(practitionerId);
		sr.addPerformer().setReferenceElement(practitionerId);
		input.addEntry()
			.setFullUrl(sr.getId())
			.setResource(sr)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("ServiceRequest");

		myCaptureQueriesListener.clear();
		Bundle output = mySystemDao.transaction(mySrd, input);
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		// Lookup the two existing IDs to make sure they are legit
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(2, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(3, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(2, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

		// Do the same a second time - Deletes are enabled so we expect to have to resolve the
		// targets again to make sure they weren't deleted

		input = new Bundle();

		sr = new ServiceRequest();
		sr.getSubject().setReferenceElement(patientId);
		sr.addPerformer().setReferenceElement(practitionerId);
		sr.addPerformer().setReferenceElement(practitionerId);
		input.addEntry()
			.setFullUrl(sr.getId())
			.setResource(sr)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("ServiceRequest");

		sr = new ServiceRequest();
		sr.getSubject().setReferenceElement(patientId);
		sr.addPerformer().setReferenceElement(practitionerId);
		sr.addPerformer().setReferenceElement(practitionerId);
		input.addEntry()
			.setFullUrl(sr.getId())
			.setResource(sr)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("ServiceRequest");

		myCaptureQueriesListener.clear();
		output = mySystemDao.transaction(mySrd, input);
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		// Lookup the two existing IDs to make sure they are legit
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(2, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(3, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(2, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

	}

	@Test
	public void testTransactionWithMultiplePreExistingReferences_ForcedId_DeletesDisabled() {
		myDaoConfig.setDeleteEnabled(false);

		Patient patient = new Patient();
		patient.setId("Patient/A");
		patient.setActive(true);
		myPatientDao.update(patient);

		Practitioner practitioner = new Practitioner();
		practitioner.setId("Practitioner/B");
		practitioner.setActive(true);
		myPractitionerDao.update(practitioner);

		// Create transaction

		Bundle input = new Bundle();

		ServiceRequest sr = new ServiceRequest();
		sr.getSubject().setReference(patient.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		input.addEntry()
			.setFullUrl(sr.getId())
			.setResource(sr)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("ServiceRequest");

		sr = new ServiceRequest();
		sr.getSubject().setReference(patient.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		input.addEntry()
			.setFullUrl(sr.getId())
			.setResource(sr)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("ServiceRequest");

		myCaptureQueriesListener.clear();
		Bundle output = mySystemDao.transaction(mySrd, input);
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		// Lookup the two existing IDs to make sure they are legit
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(2, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(3, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		// See notes in testTransactionWithMultiplePreExistingReferences_Numeric_DeletesDisabled below
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(2, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

		// Do the same a second time - Deletes are enabled so we expect to have to resolve the
		// targets again to make sure they weren't deleted

		input = new Bundle();

		sr = new ServiceRequest();
		sr.getSubject().setReference(patient.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		input.addEntry()
			.setFullUrl(sr.getId())
			.setResource(sr)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("ServiceRequest");

		sr = new ServiceRequest();
		sr.getSubject().setReference(patient.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		input.addEntry()
			.setFullUrl(sr.getId())
			.setResource(sr)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("ServiceRequest");

		myCaptureQueriesListener.clear();
		output = mySystemDao.transaction(mySrd, input);
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		// We do not need to resolve the target IDs a second time
		assertEquals(0, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(3, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(1, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

	}

	@Test
	public void testTransactionWithMultiplePreExistingReferences_Numeric_DeletesDisabled() {
		myDaoConfig.setDeleteEnabled(false);

		Patient patient = new Patient();
		patient.setActive(true);
		IIdType patientId = myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		Practitioner practitioner = new Practitioner();
		practitioner.setActive(true);
		IIdType practitionerId = myPractitionerDao.create(practitioner).getId().toUnqualifiedVersionless();

		// Create transaction
		Bundle input = new Bundle();

		ServiceRequest sr = new ServiceRequest();
		sr.getSubject().setReferenceElement(patientId);
		sr.addPerformer().setReferenceElement(practitionerId);
		sr.addPerformer().setReferenceElement(practitionerId);
		input.addEntry()
			.setFullUrl(sr.getId())
			.setResource(sr)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("ServiceRequest");

		sr = new ServiceRequest();
		sr.getSubject().setReferenceElement(patientId);
		sr.addPerformer().setReferenceElement(practitionerId);
		sr.addPerformer().setReferenceElement(practitionerId);
		input.addEntry()
			.setFullUrl(sr.getId())
			.setResource(sr)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("ServiceRequest");

		myCaptureQueriesListener.clear();
		Bundle output = mySystemDao.transaction(mySrd, input);
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		// Lookup the two existing IDs to make sure they are legit
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(2, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(3, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		// TODO: We have 2 updates here that are caused by Hibernate deciding to flush its action queue half way through
		// the transaction because a read is about to take place. I think these are unnecessary but I don't see a simple
		// way of getting rid of them. Hopefully these can be optimized out later
		assertEquals(2, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

		// Do the same a second time - Deletes are enabled so we expect to have to resolve the
		// targets again to make sure they weren't deleted

		input = new Bundle();

		sr = new ServiceRequest();
		sr.getSubject().setReferenceElement(patientId);
		sr.addPerformer().setReferenceElement(practitionerId);
		sr.addPerformer().setReferenceElement(practitionerId);
		input.addEntry()
			.setFullUrl(sr.getId())
			.setResource(sr)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("ServiceRequest");

		sr = new ServiceRequest();
		sr.getSubject().setReferenceElement(patientId);
		sr.addPerformer().setReferenceElement(practitionerId);
		sr.addPerformer().setReferenceElement(practitionerId);
		input.addEntry()
			.setFullUrl(sr.getId())
			.setResource(sr)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("ServiceRequest");

		myCaptureQueriesListener.clear();
		output = mySystemDao.transaction(mySrd, input);
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		// We do not need to resolve the target IDs a second time
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(3, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		// Similar to the note above - No idea why this update is here, it's basically a NO-OP
		assertEquals(1, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

	}

	@Test
	public void testTransactionWithMultiplePreExistingReferences_IfNoneExist() {
		myDaoConfig.setDeleteEnabled(true);

		Patient patient = new Patient();
		patient.setId("Patient/A");
		patient.setActive(true);
		myPatientDao.update(patient);

		Practitioner practitioner = new Practitioner();
		practitioner.setId("Practitioner/B");
		practitioner.setActive(true);
		myPractitionerDao.update(practitioner);

		// Create transaction

		Bundle input = new Bundle();

		patient = new Patient();
		patient.setId(IdType.newRandomUuid());
		patient.setActive(true);
		input.addEntry()
			.setFullUrl(patient.getId())
			.setResource(patient)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("Patient")
			.setIfNoneExist("Patient?active=true");

		practitioner = new Practitioner();
		practitioner.setId(IdType.newRandomUuid());
		practitioner.setActive(true);
		input.addEntry()
			.setFullUrl(practitioner.getId())
			.setResource(practitioner)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("Practitioner")
			.setIfNoneExist("Practitioner?active=true");

		ServiceRequest sr = new ServiceRequest();
		sr.getSubject().setReference(patient.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		input.addEntry()
			.setFullUrl(sr.getId())
			.setResource(sr)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("ServiceRequest");

		sr = new ServiceRequest();
		sr.getSubject().setReference(patient.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		input.addEntry()
			.setFullUrl(sr.getId())
			.setResource(sr)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("ServiceRequest");

		myCaptureQueriesListener.clear();
		Bundle output = mySystemDao.transaction(mySrd, input);
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		// Lookup the two existing IDs to make sure they are legit
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(6, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(3, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(2, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

		// Do the same a second time

		input = new Bundle();

		patient = new Patient();
		patient.setId(IdType.newRandomUuid());
		patient.setActive(true);
		input.addEntry()
			.setFullUrl(patient.getId())
			.setResource(patient)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("Patient")
			.setIfNoneExist("Patient?active=true");

		practitioner = new Practitioner();
		practitioner.setId(IdType.newRandomUuid());
		practitioner.setActive(true);
		input.addEntry()
			.setFullUrl(practitioner.getId())
			.setResource(practitioner)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("Practitioner")
			.setIfNoneExist("Practitioner?active=true");

		sr = new ServiceRequest();
		sr.getSubject().setReference(patient.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		input.addEntry()
			.setFullUrl(sr.getId())
			.setResource(sr)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("ServiceRequest");

		sr = new ServiceRequest();
		sr.getSubject().setReference(patient.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		input.addEntry()
			.setFullUrl(sr.getId())
			.setResource(sr)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("ServiceRequest");

		myCaptureQueriesListener.clear();
		output = mySystemDao.transaction(mySrd, input);
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		// Lookup the two existing IDs to make sure they are legit
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(6, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(3, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(2, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
