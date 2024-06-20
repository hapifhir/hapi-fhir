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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.math.BigDecimal;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@RequiresDocker
public class HapiSchemaMigrationTest {

	private static final Logger ourLog = LoggerFactory.getLogger(HapiSchemaMigrationTest.class);
	public static final String TEST_SCHEMA_NAME = "test";

	private static final String METADATA_COLUMN_NAME = "COLUMN_NAME";
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
	static HapiEmbeddedDatabasesExtension myEmbeddedServersExtension = new HapiEmbeddedDatabasesExtension();

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
	@ArgumentsSource(HapiEmbeddedDatabasesExtension.DatabaseVendorProvider.class)
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

		verifyHfjResSearchUrlMigration(database, theDriverType);
	}

	private void verifyHfjResSearchUrlMigration(JpaEmbeddedDatabase theDatabase, DriverTypeEnum theDriverType) throws SQLException {
		final List<Map<String, Object>> allCount = theDatabase.query(String.format("SELECT count(*) FROM %s", TABLE_HFJ_RES_SEARCH_URL));
		final List<Map<String, Object>> minusOnePartitionCount = theDatabase.query(String.format("SELECT count(*) FROM %s WHERE %s = -1", TABLE_HFJ_RES_SEARCH_URL, COLUMN_PARTITION_ID));

		assertThat(minusOnePartitionCount).hasSize(1);
		final Collection<Object> queryResultValues = minusOnePartitionCount.get(0).values();
		assertThat(queryResultValues).hasSize(1);
		final Object queryResultValue = queryResultValues.iterator().next();

		switch (theDriverType) {
			case MSSQL_2012:
				assertThat(queryResultValue).isOfAnyClassIn(Integer.class);
				if (queryResultValue instanceof Integer minusCountInt) {
					assertThat(minusCountInt).isEqualTo(1);
					assertThat(allCount.get(0).values().iterator().next()).isEqualTo(1);
				}
				break;
			case ORACLE_12C:
				assertThat(queryResultValue).isOfAnyClassIn(BigDecimal.class);
				if (queryResultValue instanceof BigDecimal minusCountBigDecimal) {
					assertThat(minusCountBigDecimal).isEqualTo(BigDecimal.ONE);
					assertThat(allCount.get(0).values().iterator().next()).isEqualTo(BigDecimal.ONE);
				}
				break;
			default:
				assertThat(queryResultValue).isOfAnyClassIn(Long.class);
				if (queryResultValue instanceof Long minusCountLong) {
					assertThat(minusCountLong).isEqualTo(1L);
					assertThat(allCount.get(0).values().iterator().next()).isEqualTo(1L);
				}
		}

		try (final Connection connection = theDatabase.getDataSource().getConnection()) {
			final DatabaseMetaData tableMetaData = connection.getMetaData();

			final List<Map<String,String>> actualColumnResults = new ArrayList<>();
			try (final ResultSet columnsResultSet = tableMetaData.getColumns(null, null, TABLE_HFJ_RES_SEARCH_URL, null)) {
				while (columnsResultSet.next()) {
					final Map<String, String> columnMap = new HashMap<>();
					actualColumnResults.add(columnMap);

					extractAndAddToMap(columnsResultSet, columnMap, METADATA_COLUMN_NAME);
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
					METADATA_DATA_TYPE, Integer.toString(Types.VARCHAR),
					METADATA_IS_NULLABLE, METADATA_IS_NULLABLE_NO),
				Map.of(METADATA_COLUMN_NAME, "RES_ID",
					METADATA_DATA_TYPE, getExpectedSqlTypeForResId(theDriverType),
					METADATA_IS_NULLABLE, METADATA_IS_NULLABLE_NO),
				Map.of(METADATA_COLUMN_NAME, "CREATED_TIME",
					METADATA_DATA_TYPE, Integer.toString(Types.TIMESTAMP),
					METADATA_IS_NULLABLE, METADATA_IS_NULLABLE_NO),
				Map.of(METADATA_COLUMN_NAME, COLUMN_PARTITION_ID,
					METADATA_DATA_TYPE, getExpectedSqlTypeForPartitionId(theDriverType),
					METADATA_IS_NULLABLE, METADATA_IS_NULLABLE_NO),
				Map.of(METADATA_COLUMN_NAME, COLUMN_PARTITION_DATE,
					METADATA_DATA_TYPE, getExpectedSqlTypeForPartitionDate(theDriverType),
					METADATA_IS_NULLABLE, METADATA_IS_NULLABLE_YES)
			);

			assertThat(expectedColumnResults).containsAll(actualColumnResults);
		}
	}

	private String getExpectedSqlTypeForResId(DriverTypeEnum theDriverType) {
		return DriverTypeEnum.ORACLE_12C == theDriverType
			? Integer.toString(Types.NUMERIC)
			: Integer.toString(Types.BIGINT);
	}

	private String getExpectedSqlTypeForPartitionId(DriverTypeEnum theDriverType) {
		return DriverTypeEnum.ORACLE_12C == theDriverType
			? Integer.toString(Types.NUMERIC)
			: Integer.toString(Types.INTEGER);
	}

	private String getExpectedSqlTypeForPartitionDate(DriverTypeEnum theDriverType) {
		return DriverTypeEnum.ORACLE_12C == theDriverType
			? Integer.toString(Types.TIMESTAMP)
			: Integer.toString(Types.DATE);
	}

	private void extractAndAddToMap(ResultSet theResultSet, Map<String,String> theMap, String theColumn) throws SQLException {
		theMap.put(theColumn, theResultSet.getString(theColumn).toUpperCase());
	}

	private static void migrate(DriverTypeEnum theDriverType, DataSource dataSource, HapiMigrationStorageSvc hapiMigrationStorageSvc, VersionEnum to) {
		MigrationTaskList migrationTasks = new HapiFhirJpaMigrationTasks(Collections.emptySet()).getAllTasks(to);
		SchemaMigrator schemaMigrator = new SchemaMigrator(TEST_SCHEMA_NAME, HAPI_FHIR_MIGRATION_TABLENAME, dataSource, new Properties(), migrationTasks, hapiMigrationStorageSvc);
		schemaMigrator.setDriverType(theDriverType);
		schemaMigrator.createMigrationTableIfRequired();
		schemaMigrator.migrate();
	}

	/**
	 * For bug <a href="https://github.com/hapifhir/hapi-fhir/issues/5546">https://github.com/hapifhir/hapi-fhir/issues/5546</a>
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
