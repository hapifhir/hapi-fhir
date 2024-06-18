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
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
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

	private static final String METADATA_COLUMN_NAME = "COLUMN_NAME";
	private static final String METADATA_COLUMN_SIZE = "COLUMN_SIZE";
	private static final String METADATA_DATA_TYPE = "DATA_TYPE";
	private static final String METADATA_IS_NULLABLE = "IS_NULLABLE";
	private static final String METADATA_IS_NULLABLE_NO = "NO";
	private static final String METADATA_IS_NULLABLE_YES = "YES";

	private static final String TABLE_HFJ_RES_SEARCH_URL = "HFJ_RES_SEARCH_URL";
	private static final String COLUMN_RES_SEARCH_URL = "RES_SEARCH_URL";
	private static final String COLUMN_PARTITION_ID = "PARTITION_ID";
	private static final String COLUMN_PARTITION_DATE = "PARTITION_DATE";

	static {
		HapiSystemProperties.enableUnitTestMode();
	}

	@RegisterExtension
//	static HapiEmbeddedDatabasesExtension myEmbeddedServersExtension = new HapiEmbeddedDatabasesExtension();
//	static HapiEmbeddedDatabasesExtensionPostgresOnly myEmbeddedServersExtension = new HapiEmbeddedDatabasesExtensionPostgresOnly();
//	static HapiEmbeddedDatabasesExtensionMSSQLOnly myEmbeddedServersExtension = new HapiEmbeddedDatabasesExtensionMSSQLOnly();
//	static HapiEmbeddedDatabasesExtensionOracleOnly myEmbeddedServersExtension = new HapiEmbeddedDatabasesExtensionOracleOnly();
	static HapiEmbeddedDatabasesExtensionH2Only myEmbeddedServersExtension = new HapiEmbeddedDatabasesExtensionH2Only ();

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
//	@ArgumentsSource(HapiEmbeddedDatabasesExtension.DatabaseVendorProvider.class)
//	@ArgumentsSource(HapiEmbeddedDatabasesExtensionPostgresOnly.DatabaseVendorProvider.class)
//	@ArgumentsSource(HapiEmbeddedDatabasesExtensionMSSQLOnly.DatabaseVendorProvider.class)
//	@ArgumentsSource(HapiEmbeddedDatabasesExtensionOracleOnly.DatabaseVendorProvider.class)
//	@ArgumentsSource(HapiEmbeddedDatabasesExtensionH2Only.DatabaseVendorProvider.class)
	@ArgumentsSource(HapiEmbeddedDatabasesExtensionH2Only.DatabaseVendorProvider.class)
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

		verifyHfjResSearchUrlMigration(database, theDriverType);
	}

	private void verifyHfjResSearchUrlMigration(JpaEmbeddedDatabase theDatabase, DriverTypeEnum theDriverType) throws SQLException {
		final List<Map<String, Object>> allCount = theDatabase.query(String.format("SELECT count(*) FROM %s", TABLE_HFJ_RES_SEARCH_URL));
		final List<Map<String, Object>> minusOnePartitionCount = theDatabase.query(String.format("SELECT count(*) FROM %s WHERE %s = -1", TABLE_HFJ_RES_SEARCH_URL, COLUMN_PARTITION_ID));

		final Long minusCount = (Long)minusOnePartitionCount.get(0).values().iterator().next();
		assertThat(minusCount).isEqualTo(1L);
		assertThat(allCount.get(0).values().iterator().next()).isEqualTo(1L);

		try (final Connection connection = theDatabase.getDataSource().getConnection()) {
			final DatabaseMetaData tableMetaData = connection.getMetaData();

			final List<Map<String,String>> actualColumnResults = new ArrayList<>();
			try (final ResultSet columnsResultSet = tableMetaData.getColumns(null, null, TABLE_HFJ_RES_SEARCH_URL, null)) {
				while (columnsResultSet.next()) {
					final Map<String, String> columnMap = new HashMap<>();
					actualColumnResults.add(columnMap);

					extractAndAddToMap(columnsResultSet, columnMap, METADATA_COLUMN_NAME);
					extractAndAddToMap(columnsResultSet, columnMap, METADATA_COLUMN_SIZE);
					extractAndAddToMap(columnsResultSet, columnMap, METADATA_DATA_TYPE);
					extractAndAddToMap(columnsResultSet, columnMap, METADATA_IS_NULLABLE);
				}
			}

			ourLog.info("6145: actualColumnResults: {}", actualColumnResults);

			final List<Map<String,String>> actualPrimaryKeyResults = new ArrayList<>();

			try (final ResultSet primaryKeyResultSet = tableMetaData.getPrimaryKeys(null, null, TABLE_HFJ_RES_SEARCH_URL)) {
				while (primaryKeyResultSet.next()) {
					final Map<String, String> primaryKeyMap = new HashMap<>();
					actualPrimaryKeyResults.add(primaryKeyMap);
					extractAndAddToMap(primaryKeyResultSet, primaryKeyMap, METADATA_COLUMN_NAME);
				}
			}

			final List<Map<String, String>> expectedPrimaryKeyResults = List.of(
				Map.of(METADATA_COLUMN_NAME, COLUMN_RES_SEARCH_URL),
				Map.of(METADATA_COLUMN_NAME, COLUMN_PARTITION_ID)
			);

			assertThat(expectedPrimaryKeyResults).containsAll(actualPrimaryKeyResults);

			final List<Map<String, String>> expectedColumnResults = List.of(
				Map.of(METADATA_COLUMN_NAME, COLUMN_RES_SEARCH_URL,
					METADATA_COLUMN_SIZE, "768",
					METADATA_DATA_TYPE, Integer.toString(Types.VARCHAR),
					METADATA_IS_NULLABLE, METADATA_IS_NULLABLE_NO),
				Map.of(METADATA_COLUMN_NAME, "RES_ID",
					METADATA_COLUMN_SIZE, "64",
					METADATA_DATA_TYPE, Integer.toString(Types.BIGINT),
					METADATA_IS_NULLABLE, METADATA_IS_NULLABLE_NO),
				Map.of(METADATA_COLUMN_NAME, "CREATED_TIME",
					METADATA_COLUMN_SIZE, "26",
					METADATA_DATA_TYPE, Integer.toString(Types.TIMESTAMP),
					METADATA_IS_NULLABLE, METADATA_IS_NULLABLE_NO),
				Map.of(METADATA_COLUMN_NAME, COLUMN_PARTITION_ID,
					METADATA_COLUMN_SIZE, "32",
					METADATA_DATA_TYPE, Integer.toString(Types.INTEGER),
					METADATA_IS_NULLABLE, METADATA_IS_NULLABLE_NO),
				Map.of(METADATA_COLUMN_NAME, COLUMN_PARTITION_DATE,
					METADATA_COLUMN_SIZE, "10",
					METADATA_DATA_TYPE, Integer.toString(Types.DATE),
					METADATA_IS_NULLABLE, METADATA_IS_NULLABLE_YES)
			);

			assertThat(expectedColumnResults).containsAll(actualColumnResults);
		}
	}

	private void extractAndAddToMap(ResultSet theResultSet, Map<String,String> theMap, String theColumn) throws SQLException {
		theMap.put(theColumn, theResultSet.getString(theColumn).toUpperCase());
	}

	private void checkOutPkSqlResult(JpaEmbeddedDatabase theDatabase, DriverTypeEnum theDriverType) {
//		final String alterTableAddColumnWithDefaultSql = getAddColumnWithDefaultSql(theDriverType,TABLE_HFJ_RES_SEARCH_URL.toLowerCase(), COLUMN_PARTITION_ID.toLowerCase(), "int", "-1");
//		final String alterTableAddColumnSql = getAddColumnSql(theDriverType, TABLE_HFJ_RES_SEARCH_URL.toLowerCase(), COLUMN_PARTITION_DATE.toLowerCase(), "date");
//
//		ourLog.info("6145: BEFORE add partition_id");
//		theDatabase.executeSqlWithParams(alterTableAddColumnWithDefaultSql);
//		ourLog.info("6145: AFTER add partition_id");
//		ourLog.info("6145: BEFORE add partition_date");
//		theDatabase.executeSqlWithParams(alterTableAddColumnSql);
//		ourLog.info("6145: AFTER add partition_date");

		final List<Map<String, Object>> query = theDatabase.query(getPrimaryKeyForTableSql(theDriverType, TABLE_HFJ_RES_SEARCH_URL.toLowerCase()));

		ourLog.info("6145: query result: {}", query);

		assertFalse(query.isEmpty());
		assertEquals(1, query.size());
		final Collection<Object> rowColumns = query.get(0).values();
		assertEquals(1, rowColumns.size());
		final String primaryKeyName = (String)rowColumns.iterator().next();

		// LUKETODO:  DROP and ADD primary SQL works for Postgres
		ourLog.info("6145: BEFORE drop primary key: {}", primaryKeyName);

		final String dropPrimaryKeySql = getDropPrimaryKeySql(theDriverType, TABLE_HFJ_RES_SEARCH_URL.toLowerCase(), primaryKeyName);

		theDatabase.executeSqlWithParams(dropPrimaryKeySql);

		ourLog.info("6145: AFTER drop primary key: {}", primaryKeyName);

		final List<Map<String, Object>> countRows = theDatabase.query("SELECT count(*) FROM " + TABLE_HFJ_RES_SEARCH_URL.toLowerCase());

		ourLog.info("6145: countRows: {}", countRows);

//		// LUKETODO:  update all search URLs to have a default value of -1
//		theDatabase.executeSqlAsBatch(String.format("UPDATE %s SET %s = %s", TABLE_HFJ_RES_SEARCH_URL.toLowerCase(), COLUMN_PARTITION_ID.toLowerCase(), "-1"));

//		ourLog.info("6145: BEFORE partition_id NOT NULL: {}", primaryKeyName);
//
//		theDatabase.executeSqlWithParams(getSetColumnToNonNullSql(theDriverType, TABLE_HFJ_RES_SEARCH_URL.toLowerCase(), COLUMN_PARTITION_ID.toLowerCase(), "int"));
//
//		ourLog.info("6145: AFTER partition_id NOT NULL: {}", primaryKeyName);

		final String addPrimaryKeySql = "ALTER TABLE %s ADD PRIMARY KEY (%s, %s)";

		ourLog.info("6145: BEFORE add primary key");

		theDatabase.executeSqlWithParams(String.format(addPrimaryKeySql, TABLE_HFJ_RES_SEARCH_URL.toLowerCase(), COLUMN_RES_SEARCH_URL.toUpperCase(), COLUMN_PARTITION_ID.toLowerCase()));

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
	private static String getDropPrimaryKeySql(DriverTypeEnum theDriverType, String theTableName, String thePrimaryKeyName) {

		return switch (theDriverType) {
			case COCKROACHDB_21_1 -> null;
			case DERBY_EMBEDDED -> null;
			case MARIADB_10_1 -> null;
			case MYSQL_5_7 -> null;
			case H2_EMBEDDED ->
				String.format("ALTER TABLE %s DROP PRIMARY KEY", theTableName);
			case POSTGRES_9_4, ORACLE_12C, MSSQL_2012 ->
				String.format("ALTER TABLE %s DROP CONSTRAINT %s", theTableName, thePrimaryKeyName);
		};
	}

	@Language("SQL")
	private static String getSetColumnToNonNullSql(DriverTypeEnum theDriverType, String theTableName, String theColumnName, String theColumnType) {
		return switch (theDriverType) {
			case COCKROACHDB_21_1 -> null;
			case DERBY_EMBEDDED -> null;
			case MARIADB_10_1 -> null;
			case MYSQL_5_7 -> null;
			case POSTGRES_9_4, H2_EMBEDDED ->
				String.format("ALTER TABLE %s ALTER COLUMN %s SET NOT NULL", theTableName, theColumnName);
			case ORACLE_12C, MSSQL_2012 ->
				String.format("ALTER TABLE %s ALTER COLUMN %s %s NOT NULL", theTableName, theColumnName, theColumnType);
		};
	}

	@Language("SQL")
	private static String getAddColumnWithDefaultSql(DriverTypeEnum theDriverType, String theTableName, String theColumnName, String theColumnType, String theDefaultValue) {
		return switch (theDriverType) {
			case COCKROACHDB_21_1 -> null;
			case DERBY_EMBEDDED -> null;
			case MARIADB_10_1 -> null;
			case MYSQL_5_7 -> null;
			case POSTGRES_9_4 ->
				String.format("ALTER TABLE %s ADD COLUMN %s %s DEFAULT %s", theTableName, theColumnName, theColumnType, theDefaultValue);
			case H2_EMBEDDED, ORACLE_12C, MSSQL_2012 ->
				String.format("ALTER TABLE %s ADD %s %s DEFAULT %s", theTableName, theColumnName, theColumnType, theDefaultValue);
		};
	}

	@Language("SQL")
	private static String getAddColumnSql(DriverTypeEnum theDriverType, String theTableName, String theColumnName, String theColumnType) {
		return switch (theDriverType) {
			case COCKROACHDB_21_1 -> null;
			case DERBY_EMBEDDED -> null;
			case MARIADB_10_1 -> null;
			case MYSQL_5_7 -> null;
			case POSTGRES_9_4 ->
				String.format("ALTER TABLE %s ADD COLUMN %s %s", theTableName, theColumnName, theColumnType);
			case H2_EMBEDDED, ORACLE_12C, MSSQL_2012 ->
				String.format("ALTER TABLE %s ADD %s %s", theTableName, theColumnName, theColumnType);
		};
	}

	@Language("SQL")
	private static String getPrimaryKeyForTableSql(DriverTypeEnum theDriverType, String theTableName) {
		return switch (theDriverType) {
			case H2_EMBEDDED ->
				String.format(
					"""
					SELECT index_name
					FROM information_schema.indexes
					WHERE table_schema = 'PUBLIC' AND
					table_name = '%s' AND
					index_type_name = 'PRIMARY KEY';
					""",
					theTableName.toUpperCase());
			case DERBY_EMBEDDED -> null;
			case MARIADB_10_1 -> null;
			case MYSQL_5_7 -> null;
			case POSTGRES_9_4 ->
				String.format(
					"""
					SELECT constraint_name
					FROM information_schema.table_constraints
					WHERE table_schema = 'public'
					AND table_name = '%s'
					AND constraint_type = 'PRIMARY KEY'
					""",
					theTableName);
			case ORACLE_12C ->
				String.format(
					"""
					SELECT constraint_name, constraint_type
					FROM user_constraints
					WHERE table_name = '%s'
					""",
					theTableName);
			case MSSQL_2012 ->
				String.format(
					"""
					SELECT tc.constraint_name
					FROM information_schema.table_constraints tc
					JOIN information_schema.constraint_column_usage ccu ON tc.constraint_name = ccu.constraint_name
					WHERE tc.table_name = '%s' AND
					tc.constraint_type = 'PRIMARY KEY'
					""",
					theTableName);

			case COCKROACHDB_21_1 -> null;
		};
	}

	private static void migrate(DriverTypeEnum theDriverType, DataSource dataSource, HapiMigrationStorageSvc hapiMigrationStorageSvc, VersionEnum to) {
		MigrationTaskList migrationTasks = new HapiFhirJpaMigrationTasks(Collections.emptySet()).getAllTasks(to);
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
