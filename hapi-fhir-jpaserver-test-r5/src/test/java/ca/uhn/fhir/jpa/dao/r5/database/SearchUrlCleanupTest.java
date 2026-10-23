package ca.uhn.fhir.jpa.dao.r5.database;

import ca.uhn.fhir.jpa.dao.data.IResourceSearchUrlDao;
import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.search.ResourceSearchUrlSvc;
import ca.uhn.fhir.jpa.util.CircularQueueCaptureQueriesListener;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.hl7.fhir.r5.model.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;
import java.util.List;

import static org.apache.commons.lang3.time.DateUtils.MILLIS_PER_HOUR;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Shared test cases for the scheduled cleanup of stale <code>HFJ_RES_SEARCH_URL</code> rows
 * ({@link ResourceSearchUrlSvc#deleteEntriesOlderThan(Date)}): each page select locks its rows while
 * skipping rows locked by other transactions, using each vendor's own syntax. Paging itself is covered
 * by <code>ResourceSearchUrlSvcTest</code>.
 * <p>
 * Implemented by {@link BaseDatabaseVerificationIT} so every case runs against every supported database vendor.
 * </p>
 */
// Created by claude-opus-5-5
interface SearchUrlCleanupTest {

	record Context(
		RestfulServerExtension server,
		ResourceSearchUrlSvc resourceSearchUrlSvc,
		IResourceSearchUrlDao resourceSearchUrlDao,
		PlatformTransactionManager txManager,
		CircularQueueCaptureQueriesListener captureQueriesListener,
		DriverTypeEnum driverType
	) {}

	Context getSearchUrlCleanupTestContext();

	@Test
	default void testSearchUrlCleanup_staleEntries_deletedWithVendorLockSyntax() {
		Context ctx = getSearchUrlCleanupTestContext();
		for (int i = 0; i < 5; i++) {
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("http://cleanup").setValue("value-" + i);
			ctx.server().getFhirClient()
				.create()
				.resource(patient)
				.conditionalByUrl("Patient?identifier=http://cleanup|value-" + i)
				.execute();
		}
		assertThat(countSearchUrlRows(ctx)).isEqualTo(5L);

		// A cutoff in the future makes every entry stale
		ctx.captureQueriesListener().clear();
		ctx.resourceSearchUrlSvc().deleteEntriesOlderThan(new Date(System.currentTimeMillis() + MILLIS_PER_HOUR));

		assertThat(countSearchUrlRows(ctx)).isZero();
		List<String> staleSelects = ctx.captureQueriesListener().getSelectQueries().stream()
			.map(t -> t.getSql(true, false).toLowerCase())
			.filter(t -> t.contains("hfj_res_search_url") && t.contains("created_time"))
			.toList();
		assertThat(staleSelects).isNotEmpty();
		switch (ctx.driverType()) {
			case MSSQL_2012 -> assertThat(staleSelects).allMatch(t -> t.contains("readpast"), "uses READPAST");
			case POSTGRES_9_4, ORACLE_12C ->
				assertThat(staleSelects).allMatch(t -> t.contains("skip locked"), "uses SKIP LOCKED");
			default -> assertThat(staleSelects).allMatch(t -> t.contains("for update"), "locks the page");
		}
	}

	private static long countSearchUrlRows(Context theContext) {
		Long count = new TransactionTemplate(theContext.txManager())
			.execute(t -> theContext.resourceSearchUrlDao().count());
		return count == null ? 0 : count;
	}
}
