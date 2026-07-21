package ca.uhn.fhir.jpa.dao.r4;

import static org.junit.jupiter.api.Assertions.assertNull;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.model.HistoryCountModeEnum;
import ca.uhn.fhir.rest.param.HistorySearchDateRangeParam;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.StopWatch;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static ca.uhn.fhir.util.TestUtil.sleepAtLeast;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class JpaHistoryR4Test extends BaseJpaR4SystemTest {

	private static final Logger ourLog = LoggerFactory.getLogger(JpaHistoryR4Test.class);

	@AfterEach
	public void after() {
		myStorageSettings.setHistoryCountMode(JpaStorageSettings.DEFAULT_HISTORY_COUNT_MODE);
	}

	@Test
	public void testTypeHistory_TotalDisabled() {
		myStorageSettings.setHistoryCountMode(HistoryCountModeEnum.COUNT_DISABLED);
		create20Patients();

		/*
		 * Perform initial history
		 */

		myCaptureQueriesListener.clear();
		IBundleProvider history = myPatientDao.history(null, null, null, new SystemRequestDetails());

		// Simulate the server requesting the Bundle.total value
		assertNull(history.size());

		// Simulate the server actually loading the resources
		history.getResources(0, 10);

		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		// Resource query happens but not count query
		assertEquals(1, myCaptureQueriesListener.countSelectQueries());
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(false, false).toLowerCase(Locale.ROOT)).doesNotStartWith("select count");

	}

	@Test
	public void testTypeHistory_CountAccurate() {
		runInTransaction(()->{
			assertEquals(0, myResourceHistoryTableDao.count());
		});

		myStorageSettings.setHistoryCountMode(HistoryCountModeEnum.COUNT_ACCURATE);
		create20Patients();

		runInTransaction(()->{
			assertEquals(20, myResourceHistoryTableDao.count());
		});

		/*
		 * Perform initial history
		 */

		myCaptureQueriesListener.clear();
		IBundleProvider history = myPatientDao.history(null, null, null, new SystemRequestDetails());

		// Simulate the server requesting the Bundle.total value
		assertEquals(20, history.sizeOrThrowNpe());

		// Simulate the server actually loading the resources
		history.getResources(0, 10);

		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(false, false).toLowerCase(Locale.ROOT)).startsWith("select count");
		assertThat(myCaptureQueriesListener.getSelectQueries().get(1).getSql(false, false).toLowerCase(Locale.ROOT)).contains(" from hfj_res_ver ");

		/*
		 * Subsequent history should also perform count
		 */

		myCaptureQueriesListener.clear();
		history = myPatientDao.history(null, null, null, new SystemRequestDetails());

		// Simulate the server requesting the Bundle.total value
		assertEquals(20, history.sizeOrThrowNpe());

		// Simulate the server actually loading the resources
		history.getResources(0, 10);

		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(false, false).toLowerCase(Locale.ROOT)).startsWith("select count");
		assertThat(myCaptureQueriesListener.getSelectQueries().get(1).getSql(false, false).toLowerCase(Locale.ROOT)).contains(" from hfj_res_ver ");

	}

	@Test
	public void testTypeHistory_CountCacheEnabled() {
		create20Patients();

		/*
		 * Perform initial history
		 */

		myCaptureQueriesListener.clear();
		IBundleProvider history = myPatientDao.history(null, null, null, new SystemRequestDetails());

		// Simulate the server requesting the Bundle.total value
		assertEquals(20, history.sizeOrThrowNpe());

		// Simulate the server actually loading the resources
		history.getResources(0, 10);

		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
		myCaptureQueriesListener.logSelectQueries(false, false);
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(false, false).toLowerCase(Locale.ROOT)).startsWith("select count");
		assertThat(myCaptureQueriesListener.getSelectQueries().get(1).getSql(false, false).toLowerCase(Locale.ROOT)).contains(" from hfj_res_ver ");
		runInTransaction(() -> assertEquals(0, mySearchEntityDao.count()));

		/*
		 * Perform history a second time (no count should be performed)
		 */

		myCaptureQueriesListener.clear();
		history = myPatientDao.history(null, null, null, new SystemRequestDetails());

		// Simulate the server requesting the Bundle.total value
		assertEquals(20, history.sizeOrThrowNpe());

		// Simulate the server actually loading the resources
		history.getResources(0, 10);

		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(1, myCaptureQueriesListener.countSelectQueries());
		myCaptureQueriesListener.logSelectQueries(false, false);
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(false, false).toLowerCase(Locale.ROOT)).contains(" from hfj_res_ver ");
		runInTransaction(() -> assertEquals(0, mySearchEntityDao.count()));

	}

	@Test
	public void testTypeHistory_CountCacheEnabled_WithOffset() {
		create20Patients();
		sleepAtLeast(10);

		/*
		 * Perform initial history
		 */

		myCaptureQueriesListener.clear();
		IBundleProvider history = myPatientDao.history(null, new Date(), null, new SystemRequestDetails());

		// No count since there is an offset
		assertNull(history.size());

		// Simulate the server actually loading the resources
		assertThat(history.getResources(0, 999)).hasSize(20);

		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(1, myCaptureQueriesListener.countSelectQueries());
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(false, false).toLowerCase(Locale.ROOT)).doesNotStartWith("select count");

	}

	@Test
	public void testSystemHistory_CountCacheEnabled() {
		create20Patients();

		/*
		 * Perform initial history
		 */

		myCaptureQueriesListener.clear();
		IBundleProvider history = mySystemDao.history(null, null, null, new SystemRequestDetails());

		// Simulate the server requesting the Bundle.total value
		assertEquals(20, history.sizeOrThrowNpe());

		// Simulate the server actually loading the resources
		history.getResources(0, 10);

		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		myCaptureQueriesListener.logSelectQueries(false, false);
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(false, false).toLowerCase(Locale.ROOT)).startsWith("select count");
		assertThat(myCaptureQueriesListener.getSelectQueries().get(1).getSql(false, false).toLowerCase(Locale.ROOT)).contains(" from hfj_res_ver ");
		runInTransaction(() -> assertEquals(0, mySearchEntityDao.count()));

		/*
		 * Perform history a second time (no count should be performed)
		 */

		myCaptureQueriesListener.clear();
		history = mySystemDao.history(null, null, null, new SystemRequestDetails());

		// Simulate the server requesting the Bundle.total value
		assertEquals(20, history.sizeOrThrowNpe());

		// Simulate the server actually loading the resources
		history.getResources(0, 10);

		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(1, myCaptureQueriesListener.countSelectQueries());
		myCaptureQueriesListener.logSelectQueries(false, false);
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(false, false).toLowerCase(Locale.ROOT)).contains(" from hfj_res_ver ");
		runInTransaction(() -> assertEquals(0, mySearchEntityDao.count()));

	}

	@Test
	public void testSystemHistory_CountCacheEnabled_Concurrent() throws ExecutionException, InterruptedException {
		create20Patients();
		myCaptureQueriesListener.clear();

		ExecutorService threadPool = Executors.newFixedThreadPool(20);
		try {
			Runnable task = () -> {
				IBundleProvider history = mySystemDao.history(null, null, null, new SystemRequestDetails());
				assertEquals(20, history.sizeOrThrowNpe());
				assertThat(history.getResources(0, 999)).hasSize(20);
			};
			List<Future<?>> futures = new ArrayList<>();
			for (int i = 0; i < 20; i++) {
				futures.add(threadPool.submit(task));
			}

			for (Future<?> next : futures) {
				next.get();
			}

		} finally {
			threadPool.shutdown();
		}

		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());

		// We fetch the history resources 20 times, but should only fetch the
		// count(*) once, for a total of 21
		assertEquals(20 + 1, myCaptureQueriesListener.countSelectQueries());

	}

	@Test
	void testInstanceHistory_AfterUpdate_CacheInvalidated() {
		IIdType pid = createPatient();

		// Prime the cache: total=1, single entry [v1]
		IBundleProvider history = myPatientDao.history(pid, new HistorySearchDateRangeParam(), mySrd);
		assertEquals(1, history.sizeOrThrowNpe());
		assertThat(history.getResources(0, 10)).hasSize(1);

		// Update -> v2
		updatePatientById(pid);

		// Second read within the 60s TTL must show both versions, not just v2
		myCaptureQueriesListener.clear();
		history = myPatientDao.history(pid, new HistorySearchDateRangeParam(), mySrd);
		assertEquals(2, history.sizeOrThrowNpe());
		assertThat(history.getResources(0, 10)).hasSize(2);
		assertCountQueryIssued();
	}

	@Test
	void testTypeHistory_AfterUpdate_CacheInvalidated() {
		IIdType pid = createPatient();

		// Prime the type-level cache
		IBundleProvider history = myPatientDao.history(null, null, null, mySrd);
		assertEquals(1, history.sizeOrThrowNpe());
		assertThat(history.getResources(0, 10)).hasSize(1);

		// Update -> v2
		updatePatientById(pid);

		myCaptureQueriesListener.clear();
		history = myPatientDao.history(null, null, null, mySrd);
		assertEquals(2, history.sizeOrThrowNpe());
		assertThat(history.getResources(0, 10)).hasSize(2);
		assertCountQueryIssued();
	}

	@Test
	void testSystemHistory_AfterAnyWrite_CacheInvalidated() {
		createPatient();

		// Prime the system-level cache (total=1)
		IBundleProvider history = mySystemDao.history(null, null, null, mySrd);
		assertEquals(1, history.sizeOrThrowNpe());
		assertThat(history.getResources(0, 10)).hasSize(1);

		// Write a different resource type -> system count must move to 2
		Observation o = new Observation();
		o.setStatus(Observation.ObservationStatus.FINAL);
		myObservationDao.create(o, mySrd);

		myCaptureQueriesListener.clear();
		history = mySystemDao.history(null, null, null, mySrd);
		assertEquals(2, history.sizeOrThrowNpe());
		assertThat(history.getResources(0, 10)).hasSize(2);
		assertCountQueryIssued();
	}

	@Test
	void testInstanceHistory_AfterDelete_CacheInvalidated() {
		IIdType pid = createPatient();

		// Prime the cache (total=1)
		IBundleProvider history = myPatientDao.history(pid, new HistorySearchDateRangeParam(), mySrd);
		assertEquals(1, history.sizeOrThrowNpe());

		// Delete creates a deletion-marker row in HFJ_RES_VER -> total should be 2
		myPatientDao.delete(pid, mySrd);

		myCaptureQueriesListener.clear();
		history = myPatientDao.history(pid, new HistorySearchDateRangeParam(), mySrd);
		assertEquals(2, history.sizeOrThrowNpe());
		assertThat(history.getResources(0, 10)).hasSize(2);
		assertCountQueryIssued();
	}

	private IIdType createPatient() {
		Patient p = new Patient();
		p.setActive(true);
		return myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();
	}

	private void updatePatientById(IIdType pid) {
		Patient updated = new Patient();
		updated.setId(pid);
		updated.setActive(false);
		myPatientDao.update(updated, mySrd);
	}

	private void assertCountQueryIssued() {
		assertThat(myCaptureQueriesListener.getSelectQueries())
				.as("post-write read must re-issue count(*) because the cache was invalidated")
				.anyMatch(q -> q.getSql(false, false).toLowerCase(Locale.ROOT).startsWith("select count"));
	}

	private void create20Patients() {
		BundleBuilder bb = new BundleBuilder(myFhirContext);
		int count = 20;
		for (int i = 0; i < count; i++) {
			Patient p = new Patient();
			p.setActive(true);
			bb.addTransactionCreateEntry(p);
		}
		StopWatch sw = new StopWatch();
		mySystemDao.transaction(new SystemRequestDetails(), (Bundle) bb.getBundle());
		ourLog.info("Created {} patients in {}", count, sw);
	}
}
