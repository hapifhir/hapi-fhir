package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.model.HistoryCountModeEnum;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.StopWatch;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JpaHistoryR4Test extends BaseJpaR4SystemTest {

	private static final Logger ourLog = LoggerFactory.getLogger(JpaHistoryR4Test.class);

	@AfterEach
	public void after() {
		myDaoConfig.setHistoryCountMode(DaoConfig.DEFAULT_HISTORY_COUNT_MODE);
	}

	@Test
	public void testTypeHistory_TotalDisabled() {
		myDaoConfig.setHistoryCountMode(HistoryCountModeEnum.COUNT_DISABLED);
		create20Patients();

		/*
		 * Perform initial history
		 */

		myCaptureQueriesListener.clear();
		IBundleProvider history = myPatientDao.history(null, null, null, new SystemRequestDetails());

		// Simulate the server requesting the Bundle.total value
		assertEquals(null, history.size());

		// Simulate the server actually loading the resources
		history.getResources(0, 10);

		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		// Resource query happens but not count query
		assertEquals(1, myCaptureQueriesListener.countSelectQueries());
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(false, false).toLowerCase(Locale.ROOT), not(startsWith("select count")));

	}

	@Test
	public void testTypeHistory_CountAccurate() {
		myDaoConfig.setHistoryCountMode(HistoryCountModeEnum.COUNT_ACCURATE);
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
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(false, false).toLowerCase(Locale.ROOT), startsWith("select count"));
		assertThat(myCaptureQueriesListener.getSelectQueries().get(1).getSql(false, false).toLowerCase(Locale.ROOT), containsString(" from hfj_res_ver "));

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
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(false, false).toLowerCase(Locale.ROOT), startsWith("select count"));
		assertThat(myCaptureQueriesListener.getSelectQueries().get(1).getSql(false, false).toLowerCase(Locale.ROOT), containsString(" from hfj_res_ver "));

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
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(false, false).toLowerCase(Locale.ROOT), startsWith("select count"));
		assertThat(myCaptureQueriesListener.getSelectQueries().get(1).getSql(false, false).toLowerCase(Locale.ROOT), containsString(" from hfj_res_ver "));
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
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(false, false).toLowerCase(Locale.ROOT), containsString(" from hfj_res_ver "));
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
		assertEquals(null, history.size());

		// Simulate the server actually loading the resources
		assertEquals(20, history.getResources(0, 999).size());

		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(1, myCaptureQueriesListener.countSelectQueries());
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(false, false).toLowerCase(Locale.ROOT), not(startsWith("select count")));

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
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
		myCaptureQueriesListener.logSelectQueries(false, false);
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(false, false).toLowerCase(Locale.ROOT), startsWith("select count"));
		assertThat(myCaptureQueriesListener.getSelectQueries().get(1).getSql(false, false).toLowerCase(Locale.ROOT), containsString(" from hfj_res_ver "));
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
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(false, false).toLowerCase(Locale.ROOT), containsString(" from hfj_res_ver "));
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
				assertEquals(20, history.getResources(0, 999).size());
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
