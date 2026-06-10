package ca.uhn.fhir.jpa.dao.r4.database;

import ca.uhn.fhir.jpa.api.svc.ISearchUrlJobMaintenanceSvc;
import ca.uhn.fhir.jpa.embedded.JpaEmbeddedDatabase;
import ca.uhn.fhir.jpa.embedded.MsSqlEmbeddedDatabase;
import ca.uhn.fhir.jpa.migrate.HapiMigrationStorageSvc;
import ca.uhn.fhir.jpa.migrate.MigrationTaskList;
import ca.uhn.fhir.jpa.migrate.SchemaMigrator;
import ca.uhn.fhir.jpa.migrate.dao.HapiMigrationDao;
import ca.uhn.fhir.jpa.migrate.tasks.HapiFhirJpaMigrationTasks;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirSQLServerDialect;
import ca.uhn.fhir.jpa.test.config.TestR4Config;
import ca.uhn.fhir.jpa.util.DatabaseSupportUtil;
import ca.uhn.fhir.test.utilities.UnregisterScheduledProcessor;
import ca.uhn.fhir.util.VersionEnum;
import java.time.ZoneId;
import org.apache.commons.dbcp2.BasicDataSource;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.utility.DockerImageName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Duration;
import java.util.HashSet;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

// Created by claude-opus-4-7

/**
 * TDD regression test for SMILE-12080 / work item 8819. Runs against a Testcontainers-managed
 * MSSQL container a mixed stale/recent dataset seeded reproducing PROD data.
 *
 * <p><b>RED</b> on the pre-fix code: the single unbounded {@code DELETE WHERE CREATED_TIME < ?}
 * on the stale rows over-runs the JDBC {@code queryTimeout} — a SQL timeout exception propagates
 * up, test fails with a clear message.
 *
 * <p><b>GREEN</b> on the chunked fix: ~56 page iterations of 1,800 IDs, each statement well under
 * the budget. Assertion: every stale row drained, every recent row kept.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SearchUrlMaintenanceSlowDeleteMsSqlIT.TestConfig.class)
// Disable scheduler to avoid clashes
@TestPropertySource(properties = {
	UnregisterScheduledProcessor.SCHEDULING_DISABLED_EQUALS_TRUE,
})
public class SearchUrlMaintenanceSlowDeleteMsSqlIT {
	private static final Logger ourLog = LoggerFactory.getLogger(SearchUrlMaintenanceSlowDeleteMsSqlIT.class);

	// Data shape — 10% stale / 90% recent, mirroring production-like selectivity for the
	// IDX_RESSEARCHURL_TIME index seek in the chunked fix.
	private static final long STALE_ROW_COUNT = 100_000L;
	private static final long RECENT_ROW_COUNT = 10_000L;
	private static final long TOTAL_ROW_COUNT = STALE_ROW_COUNT + RECENT_ROW_COUNT;
	private static final long SEED_BATCH = 50_000L;
	private static final int PARTITIONS = 100;

	// JDBC per-statement timeout in seconds, applied via the mssql-jdbc URL property
	// {@code queryTimeout} so every statement issued through the Spring DataSource is bounded.
	// 1 s is the TDD discriminator on this 100 K-row seed: the pre-fix unbounded DELETE measures
	// ~1.7 s on Azure SQL Edge → reliable RED. The chunked fix's per-statement times stay in the
	// ~100–300 ms range → reliable GREEN. If this becomes flaky on faster hardware, bump
	// STALE_ROW_COUNT to ~250 K instead of raising the timeout.
	private static final int QUERY_TIMEOUT_SECONDS = 1;

	private static final String MIGRATION_TABLENAME = "MIGRATIONS";

	// Stale rows — CREATED_TIME 2-24 hours before insert.
	private static final String STALE_BATCH_SQL_TEMPLATE =
		";WITH N AS ("
			+ "  SELECT TOP (%d) %d + ROW_NUMBER() OVER (ORDER BY (SELECT 0)) - 1 AS rn"
			+ "  FROM sys.all_columns a CROSS JOIN sys.all_columns b"
			+ ") "
			+ "INSERT INTO HFJ_RES_SEARCH_URL (RES_SEARCH_URL, PARTITION_ID, RES_ID, CREATED_TIME, PARTITION_DATE) "
			+ "SELECT "
			+ "  CONCAT('Patient?identifier=urn:smile-12080|stale|', rn), "
			+ "  rn %% " + PARTITIONS + ", "
			+ "  rn, "
			+ "  DATEADD(MINUTE, -((rn %% 1320) + 120), GETDATE()), "
			+ "  NULL "
			+ "FROM N";

	// Recent rows — CREATED_TIME 0-60 seconds before insert.
	private static final String RECENT_BATCH_SQL_TEMPLATE =
		";WITH N AS ("
			+ "  SELECT TOP (%d) %d + ROW_NUMBER() OVER (ORDER BY (SELECT 0)) - 1 AS rn"
			+ "  FROM sys.all_columns a CROSS JOIN sys.all_columns b"
			+ ") "
			+ "INSERT INTO HFJ_RES_SEARCH_URL (RES_SEARCH_URL, PARTITION_ID, RES_ID, CREATED_TIME, PARTITION_DATE) "
			+ "SELECT "
			+ "  CONCAT('Patient?identifier=urn:smile-12080|recent|', rn), "
			+ "  rn %% " + PARTITIONS + ", "
			+ "  rn, "
			+ "  DATEADD(SECOND, -(rn %% 60), GETDATE()), "
			+ "  NULL "
			+ "FROM N";

	private static final String TRUNCATE_SQL = "TRUNCATE TABLE HFJ_RES_SEARCH_URL";
	private static final String COUNT_LIVE_SQL = "SELECT COUNT_BIG(*) FROM HFJ_RES_SEARCH_URL";
	// Breakdown by URL prefix (set in the seed) and by actual cutoff. Reveals whether undeleted
	// rows are truly-stale (chunk-loop bug or race) or actually within the 1h cutoff (data-shape).
	private static final String DIAGNOSTIC_BREAKDOWN_SQL =
		"SELECT "
			+ "  SUM(CASE WHEN RES_SEARCH_URL LIKE '%|stale|%' THEN 1 ELSE 0 END) AS stale_by_prefix, "
			+ "  SUM(CASE WHEN RES_SEARCH_URL LIKE '%|recent|%' THEN 1 ELSE 0 END) AS recent_by_prefix, "
			+ "  SUM(CASE WHEN CREATED_TIME < DATEADD(HOUR, -1, GETDATE()) THEN 1 ELSE 0 END) AS older_than_cutoff, "
			+ "  SUM(CASE WHEN CREATED_TIME >= DATEADD(HOUR, -1, GETDATE()) THEN 1 ELSE 0 END) AS within_cutoff "
			+ "FROM HFJ_RES_SEARCH_URL";

	@Autowired
	private DataSource myDataSource;

	@Autowired
	private ISearchUrlJobMaintenanceSvc myMaintenanceSvc;

	@BeforeEach
	void seedFreshDataset() throws Exception {
		runWithProgress("seed", () -> {
			try (Connection conn = myDataSource.getConnection();
			     Statement st = conn.createStatement()) {
				// Bypass the URL-level queryTimeout on test-setup statements.
				st.setQueryTimeout(0);
				st.executeUpdate(TRUNCATE_SQL);
				long totalInserted = 0;
				// Stale half: rn = [0, STALE_ROW_COUNT)
				for (long offset = 0; offset < STALE_ROW_COUNT; offset += SEED_BATCH) {
					long batchSize = Math.min(SEED_BATCH, STALE_ROW_COUNT - offset);
					int affected = st.executeUpdate(String.format(STALE_BATCH_SQL_TEMPLATE, batchSize, offset));
					totalInserted += affected;
					ourLog.info("Seeded stale batch: requested {}, affected {}, cumulative inserted {}",
						String.format("%,d", batchSize), String.format("%,d", affected),
						String.format("%,d", totalInserted));
				}
				// Recent half: rn = [STALE_ROW_COUNT, TOTAL_ROW_COUNT)
				for (long offset = STALE_ROW_COUNT; offset < TOTAL_ROW_COUNT; offset += SEED_BATCH) {
					long batchSize = Math.min(SEED_BATCH, TOTAL_ROW_COUNT - offset);
					int affected = st.executeUpdate(String.format(RECENT_BATCH_SQL_TEMPLATE, batchSize, offset));
					totalInserted += affected;
					ourLog.info("Seeded recent batch: requested {}, affected {}, cumulative inserted {}",
						String.format("%,d", batchSize), String.format("%,d", affected),
						String.format("%,d", totalInserted));
				}
			}
			return null;
		});
		// Sanity-check what's actually in the table before we hand it to the cleanup.
		ourLog.info("=== Pre-maintenance state ===");
		logRemainingBreakdown();
	}

	@Test
	void cleanup_completesWithoutPerStatementTimeout_andDrainsStaleRows() throws Exception {
		ourLog.info("=== Starting maintenance call (queryTimeout = {} s) ===", QUERY_TIMEOUT_SECONDS);
		long start = System.nanoTime();
		Throwable maintenanceFailure = catchThrowable(() ->
			runWithProgress("maintenance", () -> {
				myMaintenanceSvc.removeStaleEntries();
				return null;
			}));
		Duration elapsed = Duration.ofNanos(System.nanoTime() - start);
		long remaining = countLiveRows();
		ourLog.info("=== Maintenance returned after {} ms — {} rows remain ===",
			String.format("%,d", elapsed.toMillis()), String.format("%,d", remaining));
		logRemainingBreakdown();
		if (maintenanceFailure != null) {
			ourLog.error("Maintenance call threw", maintenanceFailure);
		}

		// RED: an unbounded DELETE on STALE_ROW_COUNT rows would over-run the per-statement
		// budget, MSSQL would roll back, table still at TOTAL_ROW_COUNT. This is the bug.
		assertThat(maintenanceFailure)
			.as("removeStaleEntries() on %d stale rows must not trip the %d s statement timeout "
					+ "(elapsed %d ms, %d rows remaining). The paged fix should keep each statement under it.",
				STALE_ROW_COUNT, QUERY_TIMEOUT_SECONDS, elapsed.toMillis(), remaining)
			.isNull();

		// GREEN: every stale row drained, every recent row kept.
		assertThat(remaining)
			.as("After cleanup, live table should hold exactly the %d recent rows "
				+ "(stale rows drained). Found %d.", RECENT_ROW_COUNT, remaining)
			.isEqualTo(RECENT_ROW_COUNT);
	}

	private long countLiveRows() throws Exception {
		try (Connection conn = myDataSource.getConnection();
		     Statement st = conn.createStatement()) {
			st.setQueryTimeout(0); // verification — not the thing under test
			try (ResultSet rs = st.executeQuery(COUNT_LIVE_SQL)) {
				rs.next();
				return rs.getLong(1);
			}
		}
	}

	private void logRemainingBreakdown() throws Exception {
		try (Connection conn = myDataSource.getConnection();
		     Statement st = conn.createStatement()) {
			st.setQueryTimeout(0);
			try (ResultSet rs = st.executeQuery(DIAGNOSTIC_BREAKDOWN_SQL)) {
				if (rs.next()) {
					ourLog.info("=== Remaining breakdown: stale-by-prefix={}, recent-by-prefix={}, "
							+ "older-than-cutoff={}, within-cutoff={} ===",
						String.format("%,d", rs.getLong("stale_by_prefix")),
						String.format("%,d", rs.getLong("recent_by_prefix")),
						String.format("%,d", rs.getLong("older_than_cutoff")),
						String.format("%,d", rs.getLong("within_cutoff")));
				}
			}
		}
	}

	/**
	 * Wraps a long operation with a 30 s heartbeat log so the test isn't silent during the seed.
	 */
	private static <T> T runWithProgress(String theOpName, Callable<T> theOp) throws Exception {
		ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
			Thread t = new Thread(r, "progress-" + theOpName);
			t.setDaemon(true);
			return t;
		});
		long start = System.nanoTime();
		ourLog.info(">>> [{}] starting", theOpName);
		scheduler.scheduleAtFixedRate(() -> {
			long sec = Duration.ofNanos(System.nanoTime() - start).toSeconds();
			ourLog.info("    [{}] still running — {} s elapsed", theOpName, sec);
		}, 30, 30, TimeUnit.SECONDS);
		try {
			return theOp.call();
		} finally {
			scheduler.shutdownNow();
			long sec = Duration.ofNanos(System.nanoTime() - start).toSeconds();
			ourLog.info("<<< [{}] done in {} s", theOpName, sec);
		}
	}

	/**
	 * Spins up a Testcontainers MSSQL (via {@link MsSqlEmbeddedDatabase}), runs HAPI migrations
	 * on it, and exposes a {@link DataSource} whose JDBC URL has {@code queryTimeout} baked in.
	 * The container is shared across all test methods in this class (Spring context is cached);
	 * data freshness is handled by {@link #seedFreshDataset()} per test method.
	 */
	@Configuration
	public static class TestConfig extends TestR4Config {

		// Run the MSSQL container in the JVM's local timezone. Without this verification
		// SELECTs (server-side GETDATE()) and DELETEs target different cutoffs.
		private final MsSqlEmbeddedDatabase myEmbeddedDb =
			new MsSqlEmbeddedDatabase(buildMsSqlContainerInHostTimezone());

		// Built by hand so we can attach the TZ env var — the no-arg MsSqlEmbeddedDatabase()
		// constructor builds its container internally and exposes no hook to customize it.
		// Image selection mirrors MsSqlEmbeddedDatabase.determineMsSqlContainerToUse() so CI
		// (Linux) and local Mac runs each use what the rest of the test suite uses.
		@SuppressWarnings({"resource", "deprecation"})
		private static MSSQLServerContainer<?> buildMsSqlContainerInHostTimezone() {
			DockerImageName image = DatabaseSupportUtil.canUseMsSql2019()
				? DockerImageName.parse("mcr.microsoft.com/mssql/server:2019-latest")
				: DockerImageName.parse("mcr.microsoft.com/azure-sql-edge:latest")
					.asCompatibleSubstituteFor("mcr.microsoft.com/mssql/server");
			return new MSSQLServerContainer<>(image)
				.acceptLicense()
				.withEnv("TZ", ZoneId.systemDefault().getId());
		}

		@Bean
		public JpaEmbeddedDatabase jpaEmbeddedDatabase() {
			return myEmbeddedDb;
		}

		@Override
		@Bean
		public DataSource dataSource() {
			// Trigger container start + run migrations against the raw container DataSource.
			runMigrations(myEmbeddedDb.getDataSource());

			// Return a fresh BasicDataSource whose URL has queryTimeout appended — that's the
			// bulletproof way to apply a per-statement budget that survives Hibernate / pool
			// unwrapping. Reuses the container's own credentials.
			String url = myEmbeddedDb.getUrl();
			if (!url.contains("queryTimeout=")) {
				url += ";queryTimeout=" + QUERY_TIMEOUT_SECONDS;
			}
			BasicDataSource ds = new BasicDataSource();
			ds.setDriverClassName(myEmbeddedDb.getDriverType().getDriverClassName());
			ds.setUrl(url);
			ds.setUsername(myEmbeddedDb.getUsername());
			ds.setPassword(myEmbeddedDb.getPassword());
			return ds;
		}

		private void runMigrations(DataSource theDataSource) {
			HapiMigrationDao migrationDao =
				new HapiMigrationDao(theDataSource, myEmbeddedDb.getDriverType(), MIGRATION_TABLENAME);
			HapiMigrationStorageSvc storageSvc = new HapiMigrationStorageSvc(migrationDao);
			MigrationTaskList tasks =
				new HapiFhirJpaMigrationTasks(new HashSet<>()).getAllTasks(VersionEnum.values());
			SchemaMigrator migrator = new SchemaMigrator(
				"HAPI FHIR", MIGRATION_TABLENAME, theDataSource, new Properties(), tasks, storageSvc);
			migrator.setDriverType(myEmbeddedDb.getDriverType());
			ourLog.info("Running HAPI migrations against MSSQL container...");
			migrator.createMigrationTableIfRequired();
			migrator.migrate();
			ourLog.info("Migration complete.");
		}

		@Override
		public String getHibernateDialect() {
			return HapiFhirSQLServerDialect.class.getName();
		}
	}
}
