package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.data.ISearchDao;
import ca.uhn.fhir.jpa.dao.data.ISearchResultDao;
import ca.uhn.fhir.jpa.search.PersistedJpaSearchFirstPageBundleProvider;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

public class ExpungeR4Test extends BaseResourceProviderR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(ExpungeR4Test.class);
	private IIdType myOneVersionPatientId;
	private IIdType myTwoVersionPatientId;
	private IIdType myDeletedPatientId;
	private IIdType myOneVersionObservationId;
	private IIdType myTwoVersionObservationId;
	private IIdType myDeletedObservationId;
	@Autowired
	private ISearchDao mySearchEntityDao;
	@Autowired
	private ISearchResultDao mySearchResultDao;

	@After
	public void afterDisableExpunge() {
		myDaoConfig.setExpungeEnabled(new DaoConfig().isExpungeEnabled());
	}

	@Before
	public void beforeEnableExpunge() {
		myDaoConfig.setExpungeEnabled(true);
	}

	private void assertExpunged(IIdType theId) {
		try {
			getDao(theId).read(theId);
			fail();
		} catch (ResourceNotFoundException e) {
			// good
		}
	}

	private void assertGone(IIdType theId) {
		try {
			getDao(theId).read(theId);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}
	}

	private void assertStillThere(IIdType theId) {
		getDao(theId).read(theId);
	}

	public void createStandardPatients() {
		Patient p = new Patient();
		p.setId("PT-ONEVERSION");
		p.getMeta().addTag().setSystem("http://foo").setCode("bar");
		p.getMeta().setSource("http://foo_source");
		p.setActive(true);
		p.addIdentifier().setSystem("foo").setValue("bar");
		p.addName().setFamily("FAM");
		myOneVersionPatientId = myPatientDao.update(p).getId();

		p = new Patient();
		p.setId("PT-TWOVERSION");
		p.getMeta().addTag().setSystem("http://foo").setCode("bar");
		p.setActive(true);
		myTwoVersionPatientId = myPatientDao.update(p).getId();
		p.setActive(false);
		myTwoVersionPatientId = myPatientDao.update(p).getId();

		p = new Patient();
		p.setId("PT-DELETED");
		p.getMeta().addTag().setSystem("http://foo").setCode("bar");
		p.setActive(true);
		myDeletedPatientId = myPatientDao.update(p).getId();
		myDeletedPatientId = myPatientDao.delete(myDeletedPatientId).getId();

		assertStillThere(myDeletedPatientId.withVersion("1"));
		assertGone(myDeletedPatientId.withVersion("2"));

		// Observation

		Observation o = new Observation();
		o.setStatus(Observation.ObservationStatus.FINAL);
		myOneVersionObservationId = myObservationDao.create(o).getId();

		o = new Observation();
		o.setStatus(Observation.ObservationStatus.FINAL);
		myTwoVersionObservationId = myObservationDao.create(o).getId();
		o.setStatus(Observation.ObservationStatus.AMENDED);
		myTwoVersionObservationId = myObservationDao.update(o).getId();

		o = new Observation();
		o.setStatus(Observation.ObservationStatus.FINAL);
		myDeletedObservationId = myObservationDao.create(o).getId();
		myDeletedObservationId = myObservationDao.delete(myDeletedObservationId).getId();
	}

	private IFhirResourceDao<?> getDao(IIdType theId) {
		IFhirResourceDao<?> dao;
		switch (theId.getResourceType()) {
			case "Patient":
				dao = myPatientDao;
				break;
			case "Observation":
				dao = myObservationDao;
				break;
			default:
				fail("Restype: " + theId.getResourceType());
				dao = myPatientDao;
		}
		return dao;
	}

	@Test
	public void testExpungeInstanceOldVersionsAndDeleted() {
		createStandardPatients();

		Patient p = new Patient();
		p.setId("PT-TWOVERSION");
		p.getMeta().addTag().setSystem("http://foo").setCode("bar");
		p.setActive(true);
		p.addName().setFamily("FOO");
		myPatientDao.update(p).getId();

		myPatientDao.expunge(myTwoVersionPatientId.toUnqualifiedVersionless(), new ExpungeOptions()
			.setExpungeDeletedResources(true)
			.setExpungeOldVersions(true), null);

		// Patients
		assertStillThere(myOneVersionPatientId);
		assertExpunged(myTwoVersionPatientId.withVersion("1"));
		assertExpunged(myTwoVersionPatientId.withVersion("2"));
		assertStillThere(myTwoVersionPatientId.withVersion("3"));
		assertGone(myDeletedPatientId);

		// No observations deleted
		assertStillThere(myOneVersionObservationId);
		assertStillThere(myTwoVersionObservationId.withVersion("1"));
		assertStillThere(myTwoVersionObservationId.withVersion("2"));
		assertGone(myDeletedObservationId);
	}

	@Test
	public void testExpungeAllVersionsDeletesRow() {
		// Create then delete
		Patient p = new Patient();
		p.setId("TEST");
		p.setActive(true);
		p.addName().setFamily("FOO");
		myPatientDao.update(p);

		p.setActive(false);
		myPatientDao.update(p);

		myPatientDao.delete(new IdType("Patient/TEST"));

		runInTransaction(() -> assertThat(myResourceTableDao.findAll(), not(empty())));
		runInTransaction(() -> assertThat(myResourceHistoryTableDao.findAll(), not(empty())));
		runInTransaction(() -> assertThat(myForcedIdDao.findAll(), not(empty())));

		myPatientDao.expunge(new ExpungeOptions()
			.setExpungeDeletedResources(true)
			.setExpungeOldVersions(true), null);

		runInTransaction(() -> assertThat(myResourceTableDao.findAll(), empty()));
		runInTransaction(() -> assertThat(myResourceHistoryTableDao.findAll(), empty()));
		runInTransaction(() -> assertThat(myForcedIdDao.findAll(), empty()));

	}

	@Test
	public void testExpungeAllVersionsWithTagsDeletesRow() {
		// Create then delete
		Patient p = new Patient();
		p.setId("TEST");
		p.getMeta().addTag().setSystem("http://foo").setCode("bar");
		p.setActive(true);
		p.addName().setFamily("FOO");
		myPatientDao.update(p).getId();

		p.setActive(false);
		myPatientDao.update(p);

		myPatientDao.delete(new IdType("Patient/TEST"));

		runInTransaction(() -> assertThat(myResourceTableDao.findAll(), not(empty())));
		runInTransaction(() -> assertThat(myResourceHistoryTableDao.findAll(), not(empty())));
		runInTransaction(() -> assertThat(myForcedIdDao.findAll(), not(empty())));

		myPatientDao.expunge(new ExpungeOptions()
			.setExpungeDeletedResources(true)
			.setExpungeOldVersions(true), null);

		runInTransaction(() -> assertThat(myResourceTableDao.findAll(), empty()));
		runInTransaction(() -> assertThat(myResourceHistoryTableDao.findAll(), empty()));
		runInTransaction(() -> assertThat(myForcedIdDao.findAll(), empty()));

	}

	@Test
	public void testExpungeInstanceVersionCurrentVersion() {
		createStandardPatients();

		try {
			myPatientDao.expunge(myTwoVersionPatientId.withVersion("2"), new ExpungeOptions()
				.setExpungeDeletedResources(true)
				.setExpungeOldVersions(true), null);
			fail();
		} catch (PreconditionFailedException e) {
			assertEquals("Can not perform version-specific expunge of resource Patient/PT-TWOVERSION/_history/2 as this is the current version", e.getMessage());
		}
	}

	@Test
	public void testExpungeInstanceVersionOldVersionsAndDeleted() {
		createStandardPatients();

		Patient p = new Patient();
		p.setId("PT-TWOVERSION");
		p.getMeta().addTag().setSystem("http://foo").setCode("bar");
		p.setActive(true);
		p.addName().setFamily("FOO");
		myPatientDao.update(p).getId();

		myPatientDao.expunge(myTwoVersionPatientId.withVersion("2"), new ExpungeOptions()
			.setExpungeDeletedResources(true)
			.setExpungeOldVersions(true), null);

		// Patients
		assertStillThere(myOneVersionPatientId);
		assertStillThere(myTwoVersionPatientId.withVersion("1"));
		assertExpunged(myTwoVersionPatientId.withVersion("2"));
		assertStillThere(myTwoVersionPatientId.withVersion("3"));
		assertGone(myDeletedPatientId);

		// No observations deleted
		assertStillThere(myOneVersionObservationId);
		assertStillThere(myTwoVersionObservationId.withVersion("1"));
		assertStillThere(myTwoVersionObservationId.withVersion("2"));
		assertGone(myDeletedObservationId);
	}

	@Test
	public void testExpungeSystemOldVersionsAndDeleted() {
		createStandardPatients();

		mySystemDao.expunge(new ExpungeOptions()
			.setExpungeDeletedResources(true)
			.setExpungeOldVersions(true), null);

		// Only deleted and prior patients
		assertStillThere(myOneVersionPatientId);
		assertExpunged(myTwoVersionPatientId.withVersion("1"));
		assertStillThere(myTwoVersionPatientId.withVersion("2"));
		assertExpunged(myDeletedPatientId);

		// Also observations deleted
		assertStillThere(myOneVersionObservationId);
		assertExpunged(myTwoVersionObservationId.withVersion("1"));
		assertStillThere(myTwoVersionObservationId.withVersion("2"));
		assertExpunged(myDeletedObservationId);
	}

	@Test
	public void testExpungeTypeDeletedResources() {
		createStandardPatients();

		myPatientDao.expunge(new ExpungeOptions()
			.setExpungeDeletedResources(true)
			.setExpungeOldVersions(false), null);

		// Only deleted and prior patients
		assertStillThere(myOneVersionPatientId);
		assertStillThere(myTwoVersionPatientId.withVersion("1"));
		assertStillThere(myTwoVersionPatientId.withVersion("2"));
		assertExpunged(myDeletedPatientId);

		// No observations deleted
		assertStillThere(myOneVersionObservationId);
		assertStillThere(myTwoVersionObservationId.withVersion("1"));
		assertStillThere(myTwoVersionObservationId.withVersion("2"));
		assertGone(myDeletedObservationId);
	}

	@Test
	public void testExpungeTypeOldVersions() {
		createStandardPatients();

		myPatientDao.expunge(new ExpungeOptions()
			.setExpungeDeletedResources(false)
			.setExpungeOldVersions(true), null);

		// Only deleted and prior patients
		assertStillThere(myOneVersionPatientId);
		assertExpunged(myTwoVersionPatientId.withVersion("1"));
		assertStillThere(myTwoVersionPatientId.withVersion("2"));
		assertExpunged(myDeletedPatientId.withVersion("1"));
		assertGone(myDeletedPatientId);

		// No observations deleted
		assertStillThere(myOneVersionObservationId);
		assertStillThere(myTwoVersionObservationId.withVersion("1"));
		assertStillThere(myTwoVersionObservationId.withVersion("2"));
		assertGone(myDeletedObservationId);
	}

	@Test
	public void testExpungeSystemEverything() {
		createStandardPatients();

		mySystemDao.expunge(new ExpungeOptions()
			.setExpungeEverything(true), null);

		// Everything deleted
		assertExpunged(myOneVersionPatientId);
		assertExpunged(myTwoVersionPatientId.withVersion("1"));
		assertExpunged(myTwoVersionPatientId.withVersion("2"));
		assertExpunged(myDeletedPatientId.withVersion("1"));
		assertExpunged(myDeletedPatientId);

		// Everything deleted
		assertExpunged(myOneVersionObservationId);
		assertExpunged(myTwoVersionObservationId.withVersion("1"));
		assertExpunged(myTwoVersionObservationId.withVersion("2"));
		assertExpunged(myDeletedObservationId);
	}

	@Test
	public void testExpungeTypeOldVersionsAndDeleted() {
		createStandardPatients();

		myPatientDao.expunge(new ExpungeOptions()
			.setExpungeDeletedResources(true)
			.setExpungeOldVersions(true), null);

		// Only deleted and prior patients
		assertStillThere(myOneVersionPatientId);
		assertExpunged(myTwoVersionPatientId.withVersion("1"));
		assertStillThere(myTwoVersionPatientId.withVersion("2"));
		assertExpunged(myDeletedPatientId);

		// No observations deleted
		assertStillThere(myOneVersionObservationId);
		assertStillThere(myTwoVersionObservationId.withVersion("1"));
		assertStillThere(myTwoVersionObservationId.withVersion("2"));
		assertGone(myDeletedObservationId);
	}

	@Test
	public void testExpungeEverythingWhereResourceInSearchResults() {
		createStandardPatients();

		await().until(()-> runInTransaction(() -> mySearchEntityDao.count() == 0));
		await().until(()-> runInTransaction(() -> mySearchResultDao.count() == 0));

		PersistedJpaSearchFirstPageBundleProvider search = (PersistedJpaSearchFirstPageBundleProvider) myPatientDao.search(new SearchParameterMap());
		assertEquals(PersistedJpaSearchFirstPageBundleProvider.class, search.getClass());
		assertEquals(2, search.size().intValue());
		assertEquals(2, search.getResources(0, 2).size());

		await().until(()-> runInTransaction(() -> mySearchEntityDao.count() == 1));
		await().until(()-> runInTransaction(() -> mySearchResultDao.count() == 2));

		mySystemDao.expunge(new ExpungeOptions()
			.setExpungeEverything(true), null);

		// Everything deleted
		assertExpunged(myOneVersionPatientId);
		assertExpunged(myTwoVersionPatientId.withVersion("1"));
		assertExpunged(myTwoVersionPatientId.withVersion("2"));
		assertExpunged(myDeletedPatientId.withVersion("1"));
		assertExpunged(myDeletedPatientId);

		// Everything deleted
		assertExpunged(myOneVersionObservationId);
		assertExpunged(myTwoVersionObservationId.withVersion("1"));
		assertExpunged(myTwoVersionObservationId.withVersion("2"));
		assertExpunged(myDeletedObservationId);
	}

	@Test
	public void testExpungeDeletedWhereResourceInSearchResults() {
		createStandardPatients();

		IBundleProvider search = myPatientDao.search(new SearchParameterMap());
		assertEquals(2, search.size().intValue());
		List<IBaseResource> resources = search.getResources(0, 2);
		myPatientDao.delete(resources.get(0).getIdElement());

		runInTransaction(() -> {
			assertEquals(2, mySearchResultDao.count());
		});


		mySystemDao.expunge(new ExpungeOptions()
			.setExpungeDeletedResources(true), null);

		// Everything deleted
		assertExpunged(myOneVersionPatientId);
		assertStillThere(myTwoVersionPatientId.withVersion("1"));
		assertStillThere(myTwoVersionPatientId.withVersion("2"));
		assertExpunged(myDeletedPatientId.withVersion("1"));
		assertExpunged(myDeletedPatientId);

	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
