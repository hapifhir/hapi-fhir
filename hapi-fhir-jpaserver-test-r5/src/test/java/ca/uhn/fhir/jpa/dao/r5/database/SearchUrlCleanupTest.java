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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import javax.sql.DataSource;

import static org.apache.commons.lang3.time.DateUtils.MILLIS_PER_HOUR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Shared test cases for the scheduled cleanup of stale <code>HFJ_RES_SEARCH_URL</code> rows
 * ({@link ResourceSearchUrlSvc#deleteEntriesOlderThan(Date)}): each page select locks its rows while
 * skipping rows locked by other transactions, using each vendor's own syntax, and a row held by another
 * transaction is left behind instead of blocking the cleanup. Paging itself is covered by
 * <code>ResourceSearchUrlSvcTest</code>.
 */
// Created by claude-opus-5-5
interface SearchUrlCleanupTest {

	record Context(
		RestfulServerExtension server,
		ResourceSearchUrlSvc resourceSearchUrlSvc,
		IResourceSearchUrlDao resourceSearchUrlDao,
		PlatformTransactionManager txManager,
		CircularQueueCaptureQueriesListener captureQueriesListener,
		DataSource dataSource,
		DriverTypeEnum driverType
	) {}

	/**
	 * How long the cleanup may take with one row locked elsewhere. The lock is held for the whole cleanup on
	 * purpose: only then does a cleanup that waits on locked rows fail, where one that skips them finishes
	 * well within this (under a second). If skip-locked stops working, the cleanup blocks on that row until
	 * this runs out, and the lock is then released so the build doesn't hang.
	 */
	Duration CLEANUP_TIMEOUT = Duration.ofSeconds(10);

	int STALE_ROWS = 1_500;

	int RECENT_ROWS = 100;

	Context getSearchUrlCleanupTestContext();

	@Test
	default void testSearchUrlCleanup_staleEntries_deletedWithVendorLockSyntax() {
		Context ctx = getSearchUrlCleanupTestContext();
		createSearchUrlRows(ctx, 5);
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

	/**
	 * Tests that a row locked by another transaction is skipped while other stale rows are deleted. Runs on
	 * Postgres and Oracle only: MSSQL is covered by
	 * <code>SearchUrlMaintenanceSlowDeleteMsSqlIT#cleanup_skipsRowsLockedByConcurrentTransaction_andDrainsTheRest</code>,
	 * and H2 has no skip-locked support.
	 */
	@Test
	default void testSearchUrlCleanup_rowLockedByOtherTransaction_skippedWhileOthersDeleted() throws Exception {
		Context ctx = getSearchUrlCleanupTestContext();
		assumeTrue(ctx.driverType() != DriverTypeEnum.H2_EMBEDDED, "H2 has no skip-locked support");
		assumeTrue(
			ctx.driverType() != DriverTypeEnum.MSSQL_2012,
			"Covered on MSSQL by SearchUrlMaintenanceSlowDeleteMsSqlIT");
		long now = System.currentTimeMillis();
		insertSearchUrlRows(ctx, 1, STALE_ROWS, new Timestamp(now - 2 * MILLIS_PER_HOUR));
		insertSearchUrlRows(ctx, STALE_ROWS + 1, RECENT_ROWS, new Timestamp(now));

		long lockedResourcePid = 1;

		// Simulates an in-flight update clearing its own entry (ResourceSearchUrlSvc#deleteByResIds)
		try (Connection lockHolder = ctx.dataSource().getConnection()) {
			lockHolder.setAutoCommit(false);
			try (PreparedStatement delete =
					lockHolder.prepareStatement("DELETE FROM HFJ_RES_SEARCH_URL WHERE RES_ID = ?")) {
				delete.setLong(1, lockedResourcePid);
				assertThat(delete.executeUpdate()).isEqualTo(1);
			}
			try {
				CompletableFuture<Void> cleanup = CompletableFuture.runAsync(() -> ctx.resourceSearchUrlSvc()
					.deleteEntriesOlderThan(new Date(now - MILLIS_PER_HOUR)));

				assertThat(cleanup).succeedsWithin(CLEANUP_TIMEOUT);
			} finally {
				lockHolder.rollback();
			}
		}

		// The rollback restores the locked row; stale rows were deleted, leaving the recent rows plus the locked row
		assertThat(countSearchUrlRows(ctx)).isEqualTo(RECENT_ROWS + 1L);
	}

	private static void createSearchUrlRows(Context theContext, int theCount) {
		for (int i = 0; i < theCount; i++) {
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("http://cleanup").setValue("value-" + i);
			theContext.server().getFhirClient()
				.create()
				.resource(patient)
				.conditionalByUrl("Patient?identifier=http://cleanup|value-" + i)
				.execute();
		}
	}

	/**
	 * Inserts {@code theCount} search URL rows directly, with RES_IDs from {@code theFirstResourcePid}. Much
	 * faster than conditional creates, and the cleanup only looks at this table (RES_ID has no foreign key).
	 */
	private static void insertSearchUrlRows(
		Context theContext, long theFirstResourcePid, int theCount, Timestamp theCreatedTime) throws Exception {
		try (Connection connection = theContext.dataSource().getConnection()) {
			connection.setAutoCommit(false);
			try (PreparedStatement insert = connection.prepareStatement(
				"INSERT INTO HFJ_RES_SEARCH_URL (RES_SEARCH_URL, PARTITION_ID, RES_ID, CREATED_TIME, PARTITION_DATE) "
					+ "VALUES (?, ?, ?, ?, ?)")) {
				for (long pid = theFirstResourcePid; pid < theFirstResourcePid + theCount; pid++) {
					insert.setString(1, "Patient?identifier=urn:cleanup|" + pid);
					insert.setInt(2, -1);
					insert.setLong(3, pid);
					insert.setTimestamp(4, theCreatedTime);
					insert.setNull(5, Types.DATE);
					insert.addBatch();
					if ((pid - theFirstResourcePid + 1) % 1_000 == 0) {
						insert.executeBatch();
					}
				}
				insert.executeBatch();
			}
			connection.commit();
		}
	}

	private static long countSearchUrlRows(Context theContext) {
		Long count = new TransactionTemplate(theContext.txManager())
			.execute(t -> theContext.resourceSearchUrlDao().count());
		return count == null ? 0 : count;
	}
}
