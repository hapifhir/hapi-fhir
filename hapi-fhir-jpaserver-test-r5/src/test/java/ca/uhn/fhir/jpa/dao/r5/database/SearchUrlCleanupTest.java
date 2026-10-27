package ca.uhn.fhir.jpa.dao.r5.database;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.data.IResourceSearchUrlDao;
import ca.uhn.fhir.jpa.search.ResourceSearchUrlSvc;
import ca.uhn.fhir.jpa.util.CircularQueueCaptureQueriesListener;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.List;
import javax.sql.DataSource;

import static org.apache.commons.lang3.time.DateUtils.MILLIS_PER_HOUR;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Shared test cases for the scheduled cleanup of stale <code>HFJ_RES_SEARCH_URL</code> rows
 * ({@link ResourceSearchUrlSvc#deleteEntriesOlderThan(Date)}): each page is one bounded <code>DELETE</code>
 * statement, and the SQL Hibernate renders for it must run on every supported database vendor. Paging
 * itself is covered by <code>ResourceSearchUrlSvcTest</code>.
 */
// Created by claude-opus-5-5
interface SearchUrlCleanupTest {

	record Context(
		ResourceSearchUrlSvc resourceSearchUrlSvc,
		IResourceSearchUrlDao resourceSearchUrlDao,
		PlatformTransactionManager txManager,
		CircularQueueCaptureQueriesListener captureQueriesListener,
		DataSource dataSource
	) {}

	/** One full page at the default batch size, then a partial one. */
	int STALE_ROWS = JpaStorageSettings.DEFAULT_SEARCH_URL_CLEANUP_BATCH_SIZE + 200;

	int RECENT_ROWS = 100;

	Context getSearchUrlCleanupTestContext();

	@Test
	default void testSearchUrlCleanup_staleEntries_deletedWithOneStatementPerPage() throws Exception {
		Context ctx = getSearchUrlCleanupTestContext();
		long now = System.currentTimeMillis();
		insertSearchUrlRows(ctx, 1, STALE_ROWS, new Timestamp(now - 2 * MILLIS_PER_HOUR));
		insertSearchUrlRows(ctx, STALE_ROWS + 1, RECENT_ROWS, new Timestamp(now));

		ctx.captureQueriesListener().clear();
		ctx.resourceSearchUrlSvc().deleteEntriesOlderThan(new Date(now - MILLIS_PER_HOUR));

		List<String> searchUrlSelects = searchUrlSql(ctx.captureQueriesListener().getSelectQueries().stream()
			.map(t -> t.getSql(true, false)).toList());
		assertThat(searchUrlSelects).as("no separate select of the stale IDs").isEmpty();
		List<String> searchUrlDeletes = searchUrlSql(ctx.captureQueriesListener().getDeleteQueries().stream()
			.map(t -> t.getSql(true, false)).toList());
		assertThat(searchUrlDeletes).as("one delete per page: a full page, then the partial last page").hasSize(2);
		assertThat(countSearchUrlRows(ctx)).isEqualTo(RECENT_ROWS);
	}

	private static List<String> searchUrlSql(List<String> theSql) {
		return theSql.stream()
			.map(String::toLowerCase)
			.filter(t -> t.contains("hfj_res_search_url"))
			.toList();
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
