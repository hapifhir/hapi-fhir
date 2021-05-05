package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.StopWatch;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JpaHistoryR4Test extends BaseJpaR4SystemTest {

	private static final Logger ourLog = LoggerFactory.getLogger(JpaHistoryR4Test.class);

	@Test
	public void testSystemHistory_CountCacheEnabled() {
		create50Patients();

		/*
		 * Perform initial history
		 */

		myCaptureQueriesListener.clear();
		IBundleProvider history = mySystemDao.history(null, null, new SystemRequestDetails());

		// Simulate the server requesting the Bundle.total value
		assertEquals(50, history.sizeOrThrowNpe());

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
		 * Perform history a second time
		 */

		myCaptureQueriesListener.clear();
		history = mySystemDao.history(null, null, new SystemRequestDetails());

		// Simulate the server requesting the Bundle.total value
		assertEquals(50, history.sizeOrThrowNpe());

		// Simulate the server actually loading the resources
		history.getResources(0, 10);

		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(1, myCaptureQueriesListener.countSelectQueries());
		myCaptureQueriesListener.logSelectQueries(false, false);
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(false, false).toLowerCase(Locale.ROOT), startsWith("select count"));
		assertThat(myCaptureQueriesListener.getSelectQueries().get(1).getSql(false, false).toLowerCase(Locale.ROOT), containsString(" from hfj_res_ver "));
		runInTransaction(() -> assertEquals(0, mySearchEntityDao.count()));

	}

	private void create50Patients() {
		BundleBuilder bb = new BundleBuilder(myFhirCtx);
		int count = 50;
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
