package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.TestUtil;
import ca.uhn.fhir.rest.param.ReferenceParam;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

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
		assertEquals(5, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
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
		assertEquals(6, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
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
//		myCaptureQueriesListener.logAllQueriesForCurrentThread();
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
		// Resolve forced ID, Perform search, load result
		assertEquals(3, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
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


	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
