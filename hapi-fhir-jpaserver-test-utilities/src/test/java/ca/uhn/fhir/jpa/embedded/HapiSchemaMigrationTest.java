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
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.dbcp2.BasicDataSource;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ca.uhn.fhir.jpa.embedded.HapiEmbeddedDatabasesExtension.FIRST_TESTED_VERSION;
import static ca.uhn.fhir.jpa.migrate.DriverTypeEnum.MSSQL_2012;
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
	private static final String METADATA_DEFAULT_VALUE = "COLUMN_DEF";
	private static final String METADATA_IS_NULLABLE_NO = "NO";
	private static final String METADATA_IS_NULLABLE_YES = "YES";

	private static final String TABLE_HFJ_RES_SEARCH_URL = "HFJ_RES_SEARCH_URL";
	private static final String TABLE_TRM_CONCEPT_DESIG = "TRM_CONCEPT_DESIG";
	private static final String COLUMN_RES_SEARCH_URL = "RES_SEARCH_URL";
	private static final String COLUMN_PARTITION_ID = "PARTITION_ID";
	private static final String COLUMN_PARTITION_DATE = "PARTITION_DATE";

	private static final String COLUMN_VAL = "VAL";
	private static final String COLUMN_VAL_VC = "VAL_VC";

	private static final String NULL_PLACEHOLDER = "[NULL]";
	private static final String COLLATION_CASE_INSENSITIVE = "SQL_Latin1_General_CP1_CI_AS";
	private static final String COLLATION_CASE_SENSITIVE = "SQL_Latin1_General_CP1_CS_AS";

	static {
		HapiSystemProperties.enableUnitTestMode();
	}

	@RegisterExtension
	public static HapiEmbeddedDatabasesExtension myEmbeddedServersExtension = new HapiEmbeddedDatabasesExtension();

	private JpaEmbeddedDatabase myCurrentDatabase;

	private HapiFhirJpaMigrationTasks myHapiFhirJpaMigrationTasks = new HapiFhirJpaMigrationTasks(Collections.emptySet());

	@AfterEach
	public void afterEach() {
		if (myCurrentDatabase != null) {
			DriverTypeEnum driverType = myCurrentDatabase.getDriverType();
			try {
				ourLog.debug("Clearing database for driver type: {}", driverType);
								if (myCurrentDatabase.isInitialized()) {
										myCurrentDatabase.clearDatabase();
				}
			} catch (Exception e) {
				ourLog.error("Failed to clear database for driver type: {}", driverType, e);
				throw e;
			} finally {
				myCurrentDatabase = null;
			}
		}
	}

	public static Stream<JpaEmbeddedDatabase> getEmbeddedDatabases() {
		return myEmbeddedServersExtension.getAllEmbeddedDatabases().stream();
	}

	@ParameterizedTest
	@MethodSource("getEmbeddedDatabases")
	public void testMigration(JpaEmbeddedDatabase theDatabase) throws SQLException {
		myCurrentDatabase = theDatabase;
		DriverTypeEnum driverType = theDatabase.getDriverType();
		
		// ensure all migrations are run
		HapiSystemProperties.disableUnitTestMode();

		ourLog.info("Running hapi fhir migration tasks for {}", driverType);

		myEmbeddedServersExtension.initializePersistenceSchema(FIRST_TESTED_VERSION, myCurrentDatabase);
		myEmbeddedServersExtension.insertPersistenceTestData(FIRST_TESTED_VERSION, myCurrentDatabase);

		for (VersionEnum aVersion : VersionEnum.values()) {
			ourLog.info("Applying migrations for {}", aVersion);
			MigrationTaskList migrationTasks = myHapiFhirJpaMigrationTasks.getAllTasks(aVersion);
			migrate(myCurrentDatabase, migrationTasks);

			if (aVersion.isNewerThan(FIRST_TESTED_VERSION)) {
				myEmbeddedServersExtension.maybeInsertPersistenceTestData(myCurrentDatabase, aVersion);
			}
		}

		if (driverType == DriverTypeEnum.POSTGRES_9_4) {
			// we only run this for postgres because:
			// 1 we only really need to check one db
			// 2 H2 automatically adds indexes to foreign keys automatically (and so cannot be used)
			// 3 Oracle doesn't run on everyone's machine (and is difficult to do so)
			// 4 Postgres is generally the fastest/least terrible relational db supported
			new HapiForeignKeyIndexHelper()
				.ensureAllForeignKeysAreIndexed(myCurrentDatabase.getDataSource());
		}

		verifyForcedIdMigration(myCurrentDatabase);

		verifyHfjResSearchUrlMigration(myCurrentDatabase);

		verifyTrm_Concept_Design(myCurrentDatabase);

		verifyHfjResourceFhirIdCollation(myCurrentDatabase);
	}

	@Test
	public void testCollationFixOnSchemaBasedInitialisation() throws SQLException {
		HapiSystemProperties.disableUnitTestMode();
		String testDataSeedFile = "migration/data/MSSQL_2012_collation_fix.sql";
		myCurrentDatabase = myEmbeddedServersExtension.mssql2012Database;
		VersionEnum versionWithMssqlCollationFixOnSchemaInit = VersionEnum.V8_8_0;

		// given
		myEmbeddedServersExtension.initializePersistenceSchema(versionWithMssqlCollationFixOnSchemaInit, myCurrentDatabase);

		MigrationTaskList migrationTasks = getRunEvenWhenSchemaInitializedTasksForVersion(versionWithMssqlCollationFixOnSchemaInit);

		// when
		migrate(myCurrentDatabase, migrationTasks);

		//then
		initializeTestData(myCurrentDatabase, testDataSeedFile);
		verifyHfjResourceFhirIdCollation(myCurrentDatabase);
	}

	@Test
	public void testCollationFixForNewInstallPostRelease740() throws SQLException {
		HapiSystemProperties.disableUnitTestMode();
		String testDataSeedFile = "migration/data/MSSQL_2012_collation_fix.sql";
		myCurrentDatabase = myEmbeddedServersExtension.mssql2012Database;
		VersionEnum startVersion = VersionEnum.V7_6_0;

		// given
		myEmbeddedServersExtension.initializePersistenceSchema(startVersion, myCurrentDatabase);

		List<VersionEnum> allVersionsToMigrate = getAllVersionsMatching(v -> v.isEqualOrNewerThan(startVersion));

		// when
		for (VersionEnum aVersion : allVersionsToMigrate) {
			ourLog.info("Applying migrations for {}", aVersion);
			MigrationTaskList migrationTasks = myHapiFhirJpaMigrationTasks.getAllTasks(aVersion);
			migrate(myCurrentDatabase, migrationTasks);
		}

		initializeTestData(myCurrentDatabase, testDataSeedFile);

		//then
		verifyHfjResourceFhirIdCollation(myCurrentDatabase);
	}

	private List<VersionEnum> getAllVersionsMatching(Predicate<VersionEnum> thePredicate) {
		return Arrays.stream(VersionEnum.values())
			.filter(thePredicate)
			.collect(Collectors.toList());
	}

	private void initializeTestData(JpaEmbeddedDatabase theDatabase, String theFilePath){
		DatabaseInitializerHelper databaseInitializerHelper = myEmbeddedServersExtension.getDatabaseInitializerHelper();
		databaseInitializerHelper.insertPersistenceTestData(theDatabase, theFilePath);
	}

	private HapiMigrationStorageSvc createHapiMigrationStorageSvc(JpaEmbeddedDatabase theDatabase) {
		DataSource dataSource = theDatabase.getDataSource();
		HapiMigrationDao hapiMigrationDao = new HapiMigrationDao(dataSource, theDatabase.getDriverType(), HAPI_FHIR_MIGRATION_TABLENAME);
		return new HapiMigrationStorageSvc(hapiMigrationDao);
	}

	/**
	 * We start with a single record in HFJ_RES_SEARCH_URL:
	 * <p/>
	 * <ul>
	 *     <li>Primary key:  ONLY RES_SEARCH_URL</li>
	 *     <li>PK: RES_SEARCH_URL: https://example.com</li>
	 *     <li>CREATED_TIME: 2023-06-29 10:14:39.69</li>
	 *     <li>RES_ID: 1678</li>
	 * </ul>
	 * <p/>
	 * Once the migration is complete, we should have:
	 * <ul>
	 *     <li>Primary key:  RES_SEARCH_URL, PARTITION_ID</li>
	 *     <li>PK: RES_SEARCH_URL: https://example.com</li>
	 *     <li>PK: PARTITION_ID: -1</li>
	 *     <li>CREATED_TIME: 2023-06-29 10:14:39.69</li>
	 *     <li>RES_ID: 1678</li>
	 *     <li>PARTITION_DATE: null</li>
	 * </ul>
	 */
	private void verifyHfjResSearchUrlMigration(JpaEmbeddedDatabase theDatabase) throws SQLException {
		DriverTypeEnum driverType = theDatabase.getDriverType();
		final List<Map<String, Object>> allCount = theDatabase.query(String.format("SELECT count(*) FROM %s", TABLE_HFJ_RES_SEARCH_URL));
		final List<Map<String, Object>> minusOnePartitionCount = theDatabase.query(String.format("SELECT count(*) FROM %s WHERE %s = -1", TABLE_HFJ_RES_SEARCH_URL, COLUMN_PARTITION_ID));

		assertThat(minusOnePartitionCount).hasSize(1);
		final Collection<Object> queryResultValues = minusOnePartitionCount.get(0).values();
		assertThat(queryResultValues).hasSize(1);
		final Object queryResultValue = queryResultValues.iterator().next();
		assertThat(queryResultValue).isInstanceOf(Number.class);
		if (queryResultValue instanceof Number queryResultNumber) {
			assertThat(queryResultNumber.intValue()).isEqualTo(1);
		}

		final Object allCountValue = allCount.get(0).values().iterator().next();
		if (allCountValue instanceof Number allCountNumber) {
			assertThat(allCountNumber.intValue()).isEqualTo(2);
		}

		try (final Connection connection = theDatabase.getDataSource().getConnection()) {
			final DatabaseMetaData tableMetaData = connection.getMetaData();

			final List<Map<String, String>> actualColumnResults = new ArrayList<>();
			try (final ResultSet columnsResultSet = tableMetaData.getColumns(null, null, getTableNameWithDbSpecificCase(driverType, TABLE_HFJ_RES_SEARCH_URL), null)) {
				while (columnsResultSet.next()) {
					final Map<String, String> columnMap = new HashMap<>();
					actualColumnResults.add(columnMap);

					// Oracle: COLUMN_DEF is a LONG column and must be read FIRST before other columns
					// to avoid "ORA-17027: Stream has already been closed" error
					extractAndAddToMap(columnsResultSet, columnMap, METADATA_DEFAULT_VALUE);
					extractAndAddToMap(columnsResultSet, columnMap, METADATA_COLUMN_NAME);
					extractAndAddToMap(columnsResultSet, columnMap, METADATA_DATA_TYPE);
					extractAndAddToMap(columnsResultSet, columnMap, METADATA_IS_NULLABLE);
				}
			}

			final List<Map<String, String>> actualPrimaryKeyResults = new ArrayList<>();

			try (final ResultSet primaryKeyResultSet = tableMetaData.getPrimaryKeys(null, null, getTableNameWithDbSpecificCase(driverType, TABLE_HFJ_RES_SEARCH_URL))) {
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

			assertThat(actualPrimaryKeyResults).containsAll(expectedPrimaryKeyResults);

			final List<Map<String, String>> expectedColumnResults = List.of(
				addExpectedColumnMetadata(COLUMN_RES_SEARCH_URL, Integer.toString(Types.VARCHAR), METADATA_IS_NULLABLE_NO, null),
				addExpectedColumnMetadata("RES_ID", getExpectedSqlTypeForResId(driverType), METADATA_IS_NULLABLE_NO, null),
				addExpectedColumnMetadata("CREATED_TIME", Integer.toString(Types.TIMESTAMP), METADATA_IS_NULLABLE_NO, null),
				addExpectedColumnMetadata(COLUMN_PARTITION_ID, getExpectedSqlTypeForPartitionId(driverType), METADATA_IS_NULLABLE_NO, "-1"),
				addExpectedColumnMetadata(COLUMN_PARTITION_DATE, getExpectedSqlTypeForPartitionDate(driverType), METADATA_IS_NULLABLE_YES, null)
			);

			assertThat(actualColumnResults).containsAll(expectedColumnResults);
		}
	}

	private void verifyTrm_Concept_Design(JpaEmbeddedDatabase theDatabase) throws SQLException {
		DriverTypeEnum driverType = theDatabase.getDriverType();
		final List<Map<String, Object>> allCount = theDatabase.query(String.format("SELECT count(*) FROM %s", TABLE_TRM_CONCEPT_DESIG));
		final List<Map<String, Object>> nonNullValCount = theDatabase.query(String.format("SELECT count(*) FROM %s WHERE %s IS NOT NULL", TABLE_TRM_CONCEPT_DESIG, COLUMN_VAL));
		final List<Map<String, Object>> nullValVcCount = theDatabase.query(String.format("SELECT count(*) FROM %s WHERE %s IS NULL", TABLE_TRM_CONCEPT_DESIG, COLUMN_VAL_VC));

		assertThat(nonNullValCount).hasSize(1);
		final Collection<Object> queryResultValuesVal = nonNullValCount.get(0).values();
		assertThat(queryResultValuesVal).hasSize(1);
		final Object queryResultValueVal = queryResultValuesVal.iterator().next();
		assertThat(queryResultValueVal).isInstanceOf(Number.class);
		if (queryResultValueVal instanceof Number queryResultNumber) {
			assertThat(queryResultNumber.intValue()).isEqualTo(3);
		}

		assertThat(nullValVcCount).hasSize(1);
		final Collection<Object> queryResultValuesValVc = nullValVcCount.get(0).values();
		assertThat(queryResultValuesValVc).hasSize(1);
		final Object queryResultValueValVc = queryResultValuesValVc.iterator().next();
		assertThat(queryResultValueValVc).isInstanceOf(Number.class);
		if (queryResultValueValVc instanceof Number queryResultNumber) {
			assertThat(queryResultNumber.intValue()).isEqualTo(1);
		}

		final Object allCountValue = allCount.get(0).values().iterator().next();
		if (allCountValue instanceof Number allCountNumber) {
			assertThat(allCountNumber.intValue()).isEqualTo(3);
		}

		try (final Connection connection = theDatabase.getDataSource().getConnection()) {
			final DatabaseMetaData tableMetaData = connection.getMetaData();

			final List<Map<String, String>> actualColumnResults = new ArrayList<>();
			try (final ResultSet columnsResultSet = tableMetaData.getColumns(null, null, getTableNameWithDbSpecificCase(driverType, TABLE_TRM_CONCEPT_DESIG), null)) {
				while (columnsResultSet.next()) {
					final Map<String, String> columnMap = new HashMap<>();
					actualColumnResults.add(columnMap);

					// Oracle: COLUMN_DEF is a LONG column and must be read FIRST before other columns
					// to avoid "ORA-17027: Stream has already been closed" error
					extractAndAddToMap(columnsResultSet, columnMap, METADATA_DEFAULT_VALUE);
					extractAndAddToMap(columnsResultSet, columnMap, METADATA_COLUMN_NAME);
					extractAndAddToMap(columnsResultSet, columnMap, METADATA_DATA_TYPE);
					extractAndAddToMap(columnsResultSet, columnMap, METADATA_IS_NULLABLE);
				}

				assertThat(actualColumnResults).contains(addExpectedColumnMetadata(COLUMN_VAL, Integer.toString(Types.VARCHAR), METADATA_IS_NULLABLE_YES, null));
				assertThat(actualColumnResults).contains(addExpectedColumnMetadata(COLUMN_VAL_VC, getExpectedSqlTypeForValVc(driverType), METADATA_IS_NULLABLE_YES, null));
			}
		}
	}

	private String getTableNameWithDbSpecificCase(DriverTypeEnum theDriverType, String theTableName) {
		return Set.of(DriverTypeEnum.ORACLE_12C, DriverTypeEnum.H2_EMBEDDED).contains(theDriverType)
			? theTableName
			: theTableName.toLowerCase();
	}

	@Nonnull
	private Map<String, String> addExpectedColumnMetadata(String theColumnName, String theDataType, String theNullable, @Nullable String theDefaultValue) {
		return Map.of(METADATA_COLUMN_NAME, theColumnName,
			METADATA_DATA_TYPE, theDataType,
			METADATA_IS_NULLABLE, theNullable,
			METADATA_DEFAULT_VALUE, Optional.ofNullable(theDefaultValue)
				.orElse(NULL_PLACEHOLDER));
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

	private String getExpectedSqlTypeForValVc(DriverTypeEnum theDriverType) {
		return Set.of(DriverTypeEnum.ORACLE_12C, DriverTypeEnum.H2_EMBEDDED).contains(theDriverType)
			? Integer.toString(Types.CLOB)
			: Integer.toString(Types.VARCHAR);
	}

	private void extractAndAddToMap(ResultSet theResultSet, Map<String, String> theMap, String theColumn) throws SQLException {
		theMap.put(theColumn, Optional.ofNullable(theResultSet.getString(theColumn))
			.map(defaultValueNonNull -> defaultValueNonNull.equals("((-1))") ? "-1" : defaultValueNonNull) // MSSQL returns "((-1))" for default value
			.map(defaultValueNonNull -> defaultValueNonNull.equals("'-1'::integer") ? "-1" : defaultValueNonNull) // Postgres returns "'-1'::integer" for default value
			.map(String::toUpperCase)
			.orElse(NULL_PLACEHOLDER));
	}

	private void migrate(JpaEmbeddedDatabase theDatabase, MigrationTaskList theMigrationTasks) {
		HapiMigrationStorageSvc hapiMigrationStorageSvc = createHapiMigrationStorageSvc(theDatabase);
		DataSource dataSource = theDatabase.getDataSource();
		SchemaMigrator schemaMigrator = new SchemaMigrator(TEST_SCHEMA_NAME, HAPI_FHIR_MIGRATION_TABLENAME, dataSource, new Properties(), theMigrationTasks, hapiMigrationStorageSvc);
		schemaMigrator.setDriverType(theDatabase.getDriverType());
		schemaMigrator.createMigrationTableIfRequired();
		schemaMigrator.migrate();
	}

	private MigrationTaskList getRunEvenWhenSchemaInitializedTasksForVersion(VersionEnum theVersion) {
		MigrationTaskList tasks = new HapiFhirJpaMigrationTasks(Collections.emptySet()).getAllTasks(theVersion);
		tasks.removeIf(theBaseTask -> !theBaseTask.isRunDuringSchemaInitialization());
		return tasks;
	}

	/**
	 * For bug <a href="https://github.com/hapifhir/hapi-fhir/issues/5546">https://github.com/hapifhir/hapi-fhir/issues/5546</a>
	 */
	private void verifyForcedIdMigration(JpaEmbeddedDatabase theDatabase) {
		DataSource dataSource = theDatabase.getDataSource();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		@SuppressWarnings("DataFlowIssue")
		int nullCount = jdbcTemplate.queryForObject("select count(1) from hfj_resource where fhir_id is null", Integer.class);
		assertThat(nullCount).as("no fhir_id should be null").isZero();
		int trailingSpaceCount = jdbcTemplate.queryForObject("select count(1) from hfj_resource where fhir_id <> trim(fhir_id)", Integer.class);
		assertThat(trailingSpaceCount).as("no fhir_id should contain a space").isZero();
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

	private void verifyHfjResourceFhirIdCollation(JpaEmbeddedDatabase theDatabase) throws SQLException {
		DriverTypeEnum driverType = theDatabase.getDriverType();
		if (MSSQL_2012 == driverType) { // Other databases are unaffected by this migration and are irrelevant

			DataSource dataSource = theDatabase.getDataSource();

			try (final Connection connection = dataSource.getConnection()) {
				@Language("SQL")
				final String databaseCollationSql = """
					SELECT collation_name
					FROM sys.databases
					WHERE name = 'master'
				""";

				final Map<String, Object> databaseCollationRow = querySingleRow(connection, databaseCollationSql);
				assertThat(databaseCollationRow).containsEntry("collation_name", COLLATION_CASE_INSENSITIVE);

				@Language("SQL")
				final String tableColumnSql = """
					SELECT  c.collation_name
					FROM sys.columns c
					INNER JOIN sys.tables t on c.object_id = t.object_id
					INNER JOIN sys.schemas s on t.schema_id = s.schema_id
					INNER JOIN sys.databases d on s.principal_id = d.database_id
					where d.name = 'master'
					AND s.name = 'dbo'
					AND t.name = 'HFJ_RESOURCE'
					AND c.name = 'FHIR_ID';
				""";

				final Map<String, Object> tableColumnCollationRow = querySingleRow(connection, tableColumnSql);
				assertThat(tableColumnCollationRow).containsEntry("collation_name",COLLATION_CASE_SENSITIVE);

				// We have not changed the database collation, so we can reference the table and column names with the wrong
				// case and the query will work
				@Language("SQL")
				final String fhirIdSql = """
					SELECT fhir_id 
					FROM hFj_ReSoUrCe  -- db must be case insensitive for the table name to be recognized 
					WHERE fhir_ID = 'PatientId22'
				""";

				final Map<String, Object> fhirIdRow = querySingleRow(connection, fhirIdSql);
				assertThat(fhirIdRow).containsEntry("fhir_id", "PatientId22");
			}
		}
	}

	private Map<String,Object> querySingleRow(Connection connection, String theSql) throws SQLException {
		final Map<String, Object> row = new HashMap<>();
		try (final PreparedStatement preparedStatement = connection.prepareStatement(theSql)) {
			try (final ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					final ResultSetMetaData resultSetMetadata = resultSet.getMetaData();
					for (int index = 1; index < resultSetMetadata.getColumnCount() +1; index++) {
						row.put(resultSetMetadata.getColumnName(index), resultSet.getObject(index));
					}
				}
			}
		}

		return row;
	}
}
