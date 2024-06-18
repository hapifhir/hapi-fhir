package ca.uhn.fhir.jpa.embedded;


import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.HapiMigrationStorageSvc;
import ca.uhn.fhir.jpa.migrate.MigrationTaskList;
import ca.uhn.fhir.jpa.migrate.SchemaMigrator;
import ca.uhn.fhir.jpa.migrate.dao.HapiMigrationDao;
import ca.uhn.fhir.jpa.migrate.tasks.HapiFhirJpaMigrationTasks;
import ca.uhn.fhir.system.HapiSystemProperties;
import ca.uhn.fhir.test.utilities.docker.RequiresDocker;
import ca.uhn.fhir.util.VersionEnum;
import org.apache.commons.dbcp2.BasicDataSource;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static ca.uhn.fhir.jpa.embedded.HapiEmbeddedDatabasesExtension.FIRST_TESTED_VERSION;
import static ca.uhn.fhir.jpa.migrate.SchemaMigrator.HAPI_FHIR_MIGRATION_TABLENAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@RequiresDocker
public class HapiSchemaMigrationTest {

	private static final Logger ourLog = LoggerFactory.getLogger(HapiSchemaMigrationTest.class);
	public static final String TEST_SCHEMA_NAME = "test";

	static {
		HapiSystemProperties.enableUnitTestMode();
	}

	@RegisterExtension
//	static HapiEmbeddedDatabasesExtension myEmbeddedServersExtension = new HapiEmbeddedDatabasesExtension();
	static HapiEmbeddedDatabasesExtensionPostgresOnly myEmbeddedServersExtension = new HapiEmbeddedDatabasesExtensionPostgresOnly();
//	static HapiEmbeddedDatabasesExtensionMSSQLOnly myEmbeddedServersExtension = new HapiEmbeddedDatabasesExtensionMSSQLOnly();
//	static HapiEmbeddedDatabasesExtensionOracleOnly myEmbeddedServersExtension = new HapiEmbeddedDatabasesExtensionOracleOnly();
//	static HapiEmbeddedDatabasesExtensionH2Only myEmbeddedServersExtension = new HapiEmbeddedDatabasesExtensionH2Only ();

	@AfterEach
	public void afterEach() {
		try {
			myEmbeddedServersExtension.clearDatabases();
			// The stack trace for this failure does not appear in CI logs.  Catching and rethrowing to log the error.
		} catch (Exception e) {
			ourLog.error("Failed to clear databases", e);
			throw e;
		}
	}

	@ParameterizedTest
	@ArgumentsSource(HapiEmbeddedDatabasesExtensionPostgresOnly.DatabaseVendorProvider.class)
//	@ArgumentsSource(HapiEmbeddedDatabasesExtensionMSSQLOnly.DatabaseVendorProvider.class)
//	@ArgumentsSource(HapiEmbeddedDatabasesExtensionOracleOnly.DatabaseVendorProvider.class)
//	@ArgumentsSource(HapiEmbeddedDatabasesExtensionH2Only.DatabaseVendorProvider.class)
	public void testMigration(DriverTypeEnum theDriverType) throws SQLException {
		// ensure all migrations are run
		HapiSystemProperties.disableUnitTestMode();

		ourLog.info("Running hapi fhir migration tasks for {}", theDriverType);

		myEmbeddedServersExtension.initializePersistenceSchema(theDriverType);
		myEmbeddedServersExtension.insertPersistenceTestData(theDriverType, FIRST_TESTED_VERSION);

		JpaEmbeddedDatabase database = myEmbeddedServersExtension.getEmbeddedDatabase(theDriverType);
		DataSource dataSource = database.getDataSource();
		HapiMigrationDao hapiMigrationDao = new HapiMigrationDao(dataSource, theDriverType, HAPI_FHIR_MIGRATION_TABLENAME);
		HapiMigrationStorageSvc hapiMigrationStorageSvc = new HapiMigrationStorageSvc(hapiMigrationDao);

		for (VersionEnum aVersion : VersionEnum.values()) {
			ourLog.info("Applying migrations for {}", aVersion);
			migrate(theDriverType, dataSource, hapiMigrationStorageSvc, aVersion);

			if (aVersion.isNewerThan(FIRST_TESTED_VERSION)) {
				myEmbeddedServersExtension.maybeInsertPersistenceTestData(theDriverType, aVersion);
			}
		}

		if (theDriverType == DriverTypeEnum.POSTGRES_9_4) {
			// we only run this for postgres because:
			// 1 we only really need to check one db
			// 2 H2 automatically adds indexes to foreign keys automatically (and so cannot be used)
			// 3 Oracle doesn't run on everyone's machine (and is difficult to do so)
			// 4 Postgres is generally the fastest/least terrible relational db supported
			new HapiForeignKeyIndexHelper()
				.ensureAllForeignKeysAreIndexed(dataSource);
		}

		verifyForcedIdMigration(dataSource);

		checkOutPkSqlResult(database, theDriverType);
	}

	private void checkOutPkSqlResult(JpaEmbeddedDatabase theDatabase, DriverTypeEnum theDriverType) {
		final String tableName = "hfj_res_search_url";

		final String searchUrlColumn = "res_search_url";
		final String partitionIdColumn = "partition_id";
		final String partitionDateColumn = "partition_date";

		final String alterTableAddColumnSql = "ALTER TABLE %s ADD COLUMN %s %s %s";

		ourLog.info("6145: BEFORE add partition_id");
		theDatabase.executeSqlWithParams(String.format(alterTableAddColumnSql, tableName, partitionIdColumn, "int", "DEFAULT -1"));
		ourLog.info("6145: AFTER add partition_id");
		ourLog.info("6145: BEFORE add partition_date");
		theDatabase.executeSqlWithParams(String.format(alterTableAddColumnSql, tableName, partitionDateColumn, "date", ""));
		ourLog.info("6145: AFTER add partition_date");

		final List<Map<String, Object>> query = theDatabase.query(getSql(theDriverType), tableName);

		ourLog.info("6145: query result: {}", query);

		assertFalse(query.isEmpty());
		assertEquals(1, query.size());
		final Collection<Object> rowColumns = query.get(0).values();
		assertEquals(1, rowColumns.size());
		final Object pkName = rowColumns.iterator().next();

		// LUKETODO:  DROP and ADD primary SQL works for Postgres
		ourLog.info("6145: BEFORE drop primary key: {}", pkName);

		final String dropPrimaryKeySql = "ALTER TABLE %s DROP CONSTRAINT %s";

		theDatabase.executeSqlWithParams(String.format(dropPrimaryKeySql, tableName, pkName));

		ourLog.info("6145: AFTER drop primary key: {}", pkName);

		final String setColumnToNonNull = "ALTER TABLE %s ALTER COLUMN %s SET NOT NULL";

		ourLog.info("6145: BEFORE partition_id NOT NULL: {}", pkName);

		theDatabase.executeSqlWithParams(String.format(setColumnToNonNull, tableName, partitionIdColumn));

		ourLog.info("6145: AFTER partition_id NOT NULL: {}", pkName);

		final String addPrimaryKeySql = "ALTER TABLE %s ADD PRIMARY KEY (%s, %s)";

		ourLog.info("6145: BEFORE add primary key");

		theDatabase.executeSqlWithParams(String.format(addPrimaryKeySql, tableName, searchUrlColumn, partitionIdColumn));

		ourLog.info("6145: AFTER add primary key");

		/*
		                          Table "public.hfj_res_search_url"
     Column     |            Type             | Collation | Nullable |    Default
----------------+-----------------------------+-----------+----------+---------------
 res_search_url | character varying(768)      |           | not null |
 res_id         | bigint                      |           | not null |
 created_time   | timestamp without time zone |           | not null |
 partition_id   | integer                     |           | not null | '-1'::integer
 partition_date | date                        |           |          |
Indexes:
    "hfj_res_search_url_pkey" PRIMARY KEY, btree (res_search_url, partition_id)
    "idx_ressearchurl_res" btree (res_id)
    "idx_ressearchurl_time" btree (created_time)
Foreign-key constraints:
    "fk_res_search_url_resource" FOREIGN KEY (res_id) REFERENCES hfj_resource(res_id)
		 */
	}

	@Language("SQL")
	private static String getSql(DriverTypeEnum theDriverType) {
		return switch (theDriverType) {
			case H2_EMBEDDED ->
				"""
				SELECT index_name
				FROM information_schema.indexes
				WHERE table_schema = 'PUBLIC' AND
				table_name = ? AND
				index_type_name = 'PRIMARY KEY';
				""";
			case DERBY_EMBEDDED -> null;
			case MARIADB_10_1 -> null;
			case MYSQL_5_7 -> null;
			case POSTGRES_9_4 ->
				// LUKETODO:  this works:  copy over to DropPrimaryKeyTask
				"""
				SELECT constraint_name 
				FROM information_schema.table_constraints
				WHERE table_schema = 'public'
				AND table_name = ?
				AND constraint_type = 'PRIMARY KEY'
				""";
			case ORACLE_12C ->
				"""
				SELECT constraint_name, constraint_type
				FROM user_constraints
				WHERE table_name = ?
				""";
			case MSSQL_2012 ->
				"""
				SELECT tc.constraint_name 
				FROM information_schema.table_constraints tc
				JOIN information_schema.constraint_column_usage ccu ON tc.constraint_name = ccu.constraint_name
				WHERE tc.table_name = ? AND 
				tc.constraint_type = 'PRIMARY KEY'
				""";

//			SELECT tc.*, ccu.*
//			select C.COLUMN_NAME FROM
//			INFORMATION_SCHEMA.TABLE_CONSTRAINTS T
//			JOIN INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE C
//			ON C.CONSTRAINT_NAME=T.CONSTRAINT_NAME
//			WHERE
//			C.TABLE_NAME='Employee'
//			and T.CONSTRAINT_TYPE='PRIMARY KEY'
			case COCKROACHDB_21_1 -> null;
		};
	}

	private static void migrate(DriverTypeEnum theDriverType, DataSource dataSource, HapiMigrationStorageSvc hapiMigrationStorageSvc, VersionEnum to) throws SQLException {
		MigrationTaskList migrationTasks = new HapiFhirJpaMigrationTasks(Collections.emptySet()).getAllTasks(new VersionEnum[]{to});
		SchemaMigrator schemaMigrator = new SchemaMigrator(TEST_SCHEMA_NAME, HAPI_FHIR_MIGRATION_TABLENAME, dataSource, new Properties(), migrationTasks, hapiMigrationStorageSvc);
		schemaMigrator.setDriverType(theDriverType);
		schemaMigrator.createMigrationTableIfRequired();
		schemaMigrator.migrate();
	}

	/**
	 * For bug https://github.com/hapifhir/hapi-fhir/issues/5546
	 */
	private void verifyForcedIdMigration(DataSource theDataSource) throws SQLException {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(theDataSource);
		@SuppressWarnings("DataFlowIssue")
		int nullCount = jdbcTemplate.queryForObject("select count(1) from hfj_resource where fhir_id is null", Integer.class);
		assertThat(nullCount).as("no fhir_id should be null").isEqualTo(0);
		int trailingSpaceCount = jdbcTemplate.queryForObject("select count(1) from hfj_resource where fhir_id <> trim(fhir_id)", Integer.class);
		assertThat(trailingSpaceCount).as("no fhir_id should contain a space").isEqualTo(0);
	}


	@Test
	public void testCreateMigrationTableIfRequired() throws SQLException {
		// Setup
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:h2:mem:no-tables");
		dataSource.setUsername("SA");
		dataSource.setPassword("SA");
		dataSource.start();

		MigrationTaskList migrationTasks = new HapiFhirJpaMigrationTasks(Collections.emptySet()).getTaskList(VersionEnum.V6_0_0, VersionEnum.V6_4_0);
		HapiMigrationDao hapiMigrationDao = new HapiMigrationDao(dataSource, DriverTypeEnum.H2_EMBEDDED, HAPI_FHIR_MIGRATION_TABLENAME);
		HapiMigrationStorageSvc hapiMigrationStorageSvc = new HapiMigrationStorageSvc(hapiMigrationDao);
		SchemaMigrator schemaMigrator = new SchemaMigrator(TEST_SCHEMA_NAME, HAPI_FHIR_MIGRATION_TABLENAME, dataSource, new Properties(), migrationTasks, hapiMigrationStorageSvc);
		schemaMigrator.setDriverType(DriverTypeEnum.H2_EMBEDDED);

		// Test & Validate
		assertTrue(schemaMigrator.createMigrationTableIfRequired());
		assertFalse(schemaMigrator.createMigrationTableIfRequired());
		assertFalse(schemaMigrator.createMigrationTableIfRequired());

	}
}
