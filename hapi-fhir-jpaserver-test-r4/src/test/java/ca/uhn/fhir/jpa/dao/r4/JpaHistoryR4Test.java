package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.model.HistoryCountModeEnum;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
		assertThat(history.size()).isEqualTo(null);

		// Simulate the server actually loading the resources
		history.getResources(0, 10);

		assertThat(myCaptureQueriesListener.countDeleteQueries()).isEqualTo(0);
		assertThat(myCaptureQueriesListener.countInsertQueries()).isEqualTo(0);
		assertThat(myCaptureQueriesListener.countUpdateQueries()).isEqualTo(0);
		// Resource query happens but not count query
		assertThat(myCaptureQueriesListener.countSelectQueries()).isEqualTo(1);
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
		assertThat(history.sizeOrThrowNpe()).isEqualTo(20);

		// Simulate the server actually loading the resources
		history.getResources(0, 10);

		assertThat(myCaptureQueriesListener.countDeleteQueries()).isEqualTo(0);
		assertThat(myCaptureQueriesListener.countInsertQueries()).isEqualTo(0);
		assertThat(myCaptureQueriesListener.countUpdateQueries()).isEqualTo(0);
		assertThat(myCaptureQueriesListener.countSelectQueries()).isEqualTo(2);
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(false, false).toLowerCase(Locale.ROOT)).startsWith("select count");
		assertThat(myCaptureQueriesListener.getSelectQueries().get(1).getSql(false, false).toLowerCase(Locale.ROOT)).contains(" from hfj_res_ver ");

		/*
		 * Subsequent history should also perform count
		 */

		myCaptureQueriesListener.clear();
		history = myPatientDao.history(null, null, null, new SystemRequestDetails());

		// Simulate the server requesting the Bundle.total value
		assertThat(history.sizeOrThrowNpe()).isEqualTo(20);

		// Simulate the server actually loading the resources
		history.getResources(0, 10);

		assertThat(myCaptureQueriesListener.countDeleteQueries()).isEqualTo(0);
		assertThat(myCaptureQueriesListener.countInsertQueries()).isEqualTo(0);
		assertThat(myCaptureQueriesListener.countUpdateQueries()).isEqualTo(0);
		assertThat(myCaptureQueriesListener.countSelectQueries()).isEqualTo(2);
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
		assertThat(history.sizeOrThrowNpe()).isEqualTo(20);

		// Simulate the server actually loading the resources
		history.getResources(0, 10);

		assertThat(myCaptureQueriesListener.countDeleteQueries()).isEqualTo(0);
		assertThat(myCaptureQueriesListener.countInsertQueries()).isEqualTo(0);
		assertThat(myCaptureQueriesListener.countUpdateQueries()).isEqualTo(0);
		assertThat(myCaptureQueriesListener.countSelectQueries()).isEqualTo(2);
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
		assertThat(history.sizeOrThrowNpe()).isEqualTo(20);

		// Simulate the server actually loading the resources
		history.getResources(0, 10);

		assertThat(myCaptureQueriesListener.countDeleteQueries()).isEqualTo(0);
		assertThat(myCaptureQueriesListener.countInsertQueries()).isEqualTo(0);
		assertThat(myCaptureQueriesListener.countUpdateQueries()).isEqualTo(0);
		assertThat(myCaptureQueriesListener.countSelectQueries()).isEqualTo(1);
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
		assertThat(history.size()).isEqualTo(null);

		// Simulate the server actually loading the resources
		assertThat(history.getResources(0, 999)).hasSize(20);

		assertThat(myCaptureQueriesListener.countDeleteQueries()).isEqualTo(0);
		assertThat(myCaptureQueriesListener.countInsertQueries()).isEqualTo(0);
		assertThat(myCaptureQueriesListener.countUpdateQueries()).isEqualTo(0);
		assertThat(myCaptureQueriesListener.countSelectQueries()).isEqualTo(1);
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
		assertThat(history.sizeOrThrowNpe()).isEqualTo(20);

		// Simulate the server actually loading the resources
		history.getResources(0, 10);

		assertThat(myCaptureQueriesListener.countDeleteQueries()).isEqualTo(0);
		assertThat(myCaptureQueriesListener.countInsertQueries()).isEqualTo(0);
		assertThat(myCaptureQueriesListener.countUpdateQueries()).isEqualTo(0);
		myCaptureQueriesListener.logSelectQueries(false, false);
		assertThat(myCaptureQueriesListener.countSelectQueries()).isEqualTo(2);
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(false, false).toLowerCase(Locale.ROOT)).startsWith("select count");
		assertThat(myCaptureQueriesListener.getSelectQueries().get(1).getSql(false, false).toLowerCase(Locale.ROOT)).contains(" from hfj_res_ver ");
		runInTransaction(() -> assertEquals(0, mySearchEntityDao.count()));

		/*
		 * Perform history a second time (no count should be performed)
		 */

		myCaptureQueriesListener.clear();
		history = mySystemDao.history(null, null, null, new SystemRequestDetails());

		// Simulate the server requesting the Bundle.total value
		assertThat(history.sizeOrThrowNpe()).isEqualTo(20);

		// Simulate the server actually loading the resources
		history.getResources(0, 10);

		assertThat(myCaptureQueriesListener.countDeleteQueries()).isEqualTo(0);
		assertThat(myCaptureQueriesListener.countInsertQueries()).isEqualTo(0);
		assertThat(myCaptureQueriesListener.countUpdateQueries()).isEqualTo(0);
		assertThat(myCaptureQueriesListener.countSelectQueries()).isEqualTo(1);
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
				assertThat(history.sizeOrThrowNpe()).isEqualTo(20);
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

		assertThat(myCaptureQueriesListener.countDeleteQueries()).isEqualTo(0);
		assertThat(myCaptureQueriesListener.countInsertQueries()).isEqualTo(0);
		assertThat(myCaptureQueriesListener.countUpdateQueries()).isEqualTo(0);

		// We fetch the history resources 20 times, but should only fetch the
		// count(*) once, for a total of 21
		assertThat(myCaptureQueriesListener.countSelectQueries()).isEqualTo(20 + 1);

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
