package ca.uhn.fhir.jpa.dao.r5.database;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoPatient;
import ca.uhn.fhir.jpa.dao.TestDaoSearch;
import ca.uhn.fhir.jpa.embedded.HapiEmbeddedDatabasesExtension;
import ca.uhn.fhir.jpa.embedded.JpaEmbeddedDatabase;
import ca.uhn.fhir.jpa.embedded.HapiForeignKeyIndexHelper;
import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.HapiMigrationStorageSvc;
import ca.uhn.fhir.jpa.migrate.MigrationTaskList;
import ca.uhn.fhir.jpa.migrate.SchemaMigrator;
import ca.uhn.fhir.jpa.migrate.dao.HapiMigrationDao;
import ca.uhn.fhir.jpa.migrate.tasks.HapiFhirJpaMigrationTasks;
import ca.uhn.fhir.jpa.migrate.util.SqlUtil;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.jpa.test.BaseJpaTest;
import ca.uhn.fhir.jpa.test.QueryTestCases;
import ca.uhn.fhir.jpa.test.config.TestR5Config;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.provider.ResourceProviderFactory;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import ca.uhn.fhir.test.utilities.server.RestfulServerConfigurerExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.ClasspathUtil;
import ca.uhn.fhir.util.VersionEnum;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.Parameters;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.Practitioner;
import org.hl7.fhir.r5.model.PractitionerRole;
import org.hl7.fhir.r5.model.Reference;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

import static ca.uhn.fhir.jpa.model.util.JpaConstants.HAPI_DATABASE_PARTITION_MODE;
import static ca.uhn.fhir.jpa.model.util.JpaConstants.OPERATION_EVERYTHING;
import static ca.uhn.fhir.jpa.migrate.SchemaMigrator.HAPI_FHIR_MIGRATION_TABLENAME;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@EnableJpaRepositories(repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
@ContextConfiguration(classes = {BaseDatabaseVerificationIT.TestConfig.class, TestDaoSearch.Config.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class BaseDatabaseVerificationIT extends BaseJpaTest implements ITestDataBuilder {
	private static final Logger ourLog = LoggerFactory.getLogger(BaseDatabaseVerificationIT.class);
	private static final String MIGRATION_TABLENAME = "MIGRATIONS";
	public static final String INIT_SCHEMA = "init_schema";
	public static final String TEST_SCHEMA_NAME = "test";

	// Schema migration test constants
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

	@Autowired
	IFhirResourceDaoPatient<Patient> myPatientDao;

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private PlatformTransactionManager myTxManager;

	@Autowired
	protected ResourceProviderFactory myResourceProviders;

	@Autowired
	private DatabaseBackedPagingProvider myPagingProvider;

	@Autowired
	TestDaoSearch myTestDaoSearch;

	@Autowired
	JpaEmbeddedDatabase myJpaEmbeddedDatabase;

	SystemRequestDetails myRequestDetails = new SystemRequestDetails();

	@RegisterExtension
	protected static RestfulServerExtension myServer = new RestfulServerExtension(FhirContext.forR5Cached());

	@RegisterExtension
	protected RestfulServerConfigurerExtension myServerConfigurer = new RestfulServerConfigurerExtension(() -> myServer)
		.withServerBeforeAll(s -> {
			s.registerProviders(myResourceProviders.createProviders());
			s.setDefaultResponseEncoding(EncodingEnum.JSON);
			s.setDefaultPrettyPrint(false);
			s.setPagingProvider(myPagingProvider);
		});


	@ParameterizedTest
	@ValueSource(ints = {10, 100000})
	@Order(100)
	public void testCreateRead(int theSize) {
		String name = StringUtils.leftPad("", theSize, "a");

		Patient patient = new Patient();
		patient.setActive(true);
		patient.addName().setFamily(name);
		IIdType id = myPatientDao.create(patient, myRequestDetails).getId();

		Patient actual = myPatientDao.read(id, myRequestDetails);
		assertEquals(name, actual.getName().get(0).getFamily());
	}


	@Test
	@Order(200)
	public void testDelete() {
		Patient patient = new Patient();
		patient.setActive(true);
		IIdType id = myPatientDao.create(patient, myRequestDetails).getId().toUnqualifiedVersionless();

		myPatientDao.delete(id, new SystemRequestDetails());

		assertThatExceptionOfType(ResourceGoneException.class).isThrownBy(() -> myPatientDao.read(id, new SystemRequestDetails()));
	}


	@Test
	@Order(300)
	public void testEverything() {
        Set<String> expectedIds = new HashSet<>();
        expectedIds.add(createPatient(withId("A"), withActiveTrue()).toUnqualifiedVersionless().getValue());
        for (int i = 0; i < 25; i++) {
            expectedIds.add(createObservation(withSubject("Patient/A")).toUnqualifiedVersionless().getValue());
        }

        IGenericClient client = myServer.getFhirClient();
		Bundle outcome = client
			.operation()
			.onInstanceVersion(new IdType("Patient/A"))
			.named(OPERATION_EVERYTHING)
			.withNoParameters(Parameters.class)
			.returnResourceType(Bundle.class)
			.execute();
        List<String> values = toUnqualifiedVersionlessIdValues(outcome);

		while (outcome.getLink("next") != null) {
			outcome = client.loadPage().next(outcome).execute();
			values.addAll(toUnqualifiedVersionlessIdValues(outcome));
		}

		assertThat(values).as(values.toString()).containsExactlyInAnyOrder(expectedIds.toArray(new String[0]));
    }

	/**
	 * See #6199
	 */

	@Test
	@Order(400)
	public void testSearchWithInclude() {
		// Setup
		IGenericClient client = myServer.getFhirClient();

		Practitioner p = new Practitioner();
		p.setActive(true);
		IIdType pId = client.create().resource(p).execute().getId().toUnqualifiedVersionless();

		PractitionerRole practitionerRole = new PractitionerRole();
		practitionerRole.setPractitioner(new Reference(pId));
		IIdType prId = client.create().resource(practitionerRole).execute().getId().toUnqualifiedVersionless();

		// Test
		Bundle results = client
			.search()
			.forResource(PractitionerRole.class)
			.include(PractitionerRole.INCLUDE_PRACTITIONER)
			.returnBundle(Bundle.class)
			.execute();

		// Verify
		List<String> actualIds = toUnqualifiedVersionlessIdValues(results);
		assertThat(actualIds).asList().containsExactly(prId.getValue(), pId.getValue());
	}


	@ParameterizedTest
	@MethodSource("ca.uhn.fhir.jpa.test.QueryTestCases#get")
	@Order(500)
	void testSyntaxForVariousQueries(QueryTestCases theQueryTestCase) {
		assertDoesNotThrow(() -> myTestDaoSearch.searchForBundleProvider(theQueryTestCase.getQuery()), theQueryTestCase.getName());
	}


	// Schema migration helper methods
	private void initializePersistenceSchema(JpaEmbeddedDatabase theDatabase, DriverTypeEnum theDriverType) {
		String fileName = String.format(
				"migration/releases/%s/schema/%s.sql",
				ca.uhn.fhir.jpa.embedded.HapiEmbeddedDatabasesExtension.FIRST_TESTED_VERSION, theDriverType);
		String sql = getSqlFromResourceFile(fileName);
		theDatabase.executeSqlAsBatch(sql);
	}

	private void insertPersistenceTestData(JpaEmbeddedDatabase theDatabase, DriverTypeEnum theDriverType, VersionEnum theVersionEnum) {
		String fileName =
				String.format("migration/releases/%s/data/%s.sql", theVersionEnum, theDriverType);
		String sql = getSqlFromResourceFile(fileName);
		theDatabase.insertTestData(sql);
	}

	private void maybeInsertPersistenceTestData(JpaEmbeddedDatabase theDatabase, DriverTypeEnum theDriverType, VersionEnum theVersionEnum) {
		try {
			insertPersistenceTestData(theDatabase, theDriverType, theVersionEnum);
		} catch (Exception theE) {
			if (theE.getMessage().contains("Error loading file: migration/releases/")) {
				ourLog.info(
						"Could not insert persistence test data most likely because we don't have any for version {} and driver {}",
						theVersionEnum,
						theDriverType);
			} else {
				// throw sql execution Exceptions
				throw theE;
			}
		}
	}

	private String getSqlFromResourceFile(String theFileName) {
		try {
			ourLog.info("Loading file: {}", theFileName);
			final java.net.URL resource = this.getClass().getClassLoader().getResource(theFileName);
			if (resource == null) {
				throw new RuntimeException("Resource not found: " + theFileName);
			}
			
			// Use ClasspathUtil which is already imported and handles jar resources properly
			return ClasspathUtil.loadResource(theFileName);
		} catch (Exception e) {
			throw new RuntimeException("Error loading file: " + theFileName, e);
		}
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
	private void verifyHfjResSearchUrlMigration(JpaEmbeddedDatabase theDatabase, DriverTypeEnum theDriverType) throws SQLException {
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
			try (final ResultSet columnsResultSet = tableMetaData.getColumns(null, null, TABLE_HFJ_RES_SEARCH_URL, null)) {
				while (columnsResultSet.next()) {
					final Map<String, String> columnMap = new HashMap<>();
					actualColumnResults.add(columnMap);

					extractAndAddToMap(columnsResultSet, columnMap, METADATA_COLUMN_NAME);
					extractAndAddToMap(columnsResultSet, columnMap, METADATA_DATA_TYPE);
					extractAndAddToMap(columnsResultSet, columnMap, METADATA_IS_NULLABLE);
					extractAndAddToMap(columnsResultSet, columnMap, METADATA_DEFAULT_VALUE);
				}
			}

			final List<Map<String, String>> actualPrimaryKeyResults = new ArrayList<>();

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
				addExpectedColumnMetadata(COLUMN_RES_SEARCH_URL, Integer.toString(Types.VARCHAR), METADATA_IS_NULLABLE_NO, null),
				addExpectedColumnMetadata("RES_ID", getExpectedSqlTypeForResId(theDriverType), METADATA_IS_NULLABLE_NO, null),
				addExpectedColumnMetadata("CREATED_TIME", Integer.toString(Types.TIMESTAMP), METADATA_IS_NULLABLE_NO, null),
				addExpectedColumnMetadata(COLUMN_PARTITION_ID, getExpectedSqlTypeForPartitionId(theDriverType), METADATA_IS_NULLABLE_NO, "-1"),
				addExpectedColumnMetadata(COLUMN_PARTITION_DATE, getExpectedSqlTypeForPartitionDate(theDriverType), METADATA_IS_NULLABLE_YES, null)
			);

			assertThat(expectedColumnResults).containsAll(actualColumnResults);
		}
	}

	private void verifyTrm_Concept_Desig(JpaEmbeddedDatabase theDatabase, DriverTypeEnum theDriverType) throws SQLException {
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
			try (final ResultSet columnsResultSet = tableMetaData.getColumns(null, null, getTableNameWithDbSpecificCase(theDriverType, TABLE_TRM_CONCEPT_DESIG), null)) {
				while (columnsResultSet.next()) {
					final Map<String, String> columnMap = new HashMap<>();
					actualColumnResults.add(columnMap);

					extractAndAddToMap(columnsResultSet, columnMap, METADATA_COLUMN_NAME);
					extractAndAddToMap(columnsResultSet, columnMap, METADATA_DATA_TYPE);
					extractAndAddToMap(columnsResultSet, columnMap, METADATA_IS_NULLABLE);
					extractAndAddToMap(columnsResultSet, columnMap, METADATA_DEFAULT_VALUE);
				}

				assertThat(actualColumnResults).contains(addExpectedColumnMetadata(COLUMN_VAL, Integer.toString(Types.VARCHAR), METADATA_IS_NULLABLE_YES, null));
				assertThat(actualColumnResults).contains(addExpectedColumnMetadata(COLUMN_VAL_VC, getExpectedSqlTypeForValVc(theDriverType), METADATA_IS_NULLABLE_YES, null));
			}
		}
	}

	@Test
	@Order(9999)
	public void testSchemaMigration() throws SQLException {
		// ensure all migrations are run
		ca.uhn.fhir.system.HapiSystemProperties.disableUnitTestMode();

		DriverTypeEnum theDriverType = myJpaEmbeddedDatabase.getDriverType();
		JpaEmbeddedDatabase database = myJpaEmbeddedDatabase;

		ourLog.info("Running hapi fhir migration tasks for {}", theDriverType);

		// Clear the database first since it may already have schema from the application context
		database.clearDatabase();

		// Initialize persistence schema and test data
		initializePersistenceSchema(database, theDriverType);
		insertPersistenceTestData(database, theDriverType, ca.uhn.fhir.jpa.embedded.HapiEmbeddedDatabasesExtension.FIRST_TESTED_VERSION);

		DataSource dataSource = database.getDataSource();
		HapiMigrationDao hapiMigrationDao = new HapiMigrationDao(dataSource, theDriverType, HAPI_FHIR_MIGRATION_TABLENAME);
		HapiMigrationStorageSvc hapiMigrationStorageSvc = new HapiMigrationStorageSvc(hapiMigrationDao);

		for (VersionEnum aVersion : VersionEnum.values()) {
			ourLog.info("Applying migrations for {}", aVersion);
			migrate(theDriverType, dataSource, hapiMigrationStorageSvc, aVersion);

			if (aVersion.isNewerThan(ca.uhn.fhir.jpa.embedded.HapiEmbeddedDatabasesExtension.FIRST_TESTED_VERSION)) {
				maybeInsertPersistenceTestData(database, theDriverType, aVersion);
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
		verifyTrm_Concept_Desig(database, theDriverType);
		verifyHfjResourceFhirIdCollation(database, theDriverType);
		ca.uhn.fhir.system.HapiSystemProperties.enableUnitTestMode();
	}

	private void verifyHfjResourceFhirIdCollation(JpaEmbeddedDatabase database, DriverTypeEnum theDriverType) throws SQLException {
		if (DriverTypeEnum.MSSQL_2012 == theDriverType) { // Other databases are unaffected by this migration and are irrelevant
			try (final Connection connection = database.getDataSource().getConnection()) {
				final String databaseCollationSql = """
					SELECT collation_name
					FROM sys.databases
					WHERE name = 'master'
				""";

				final Map<String, Object> databaseCollationRow = querySingleRow(connection, databaseCollationSql);
				assertThat(databaseCollationRow.get("collation_name")).isEqualTo(COLLATION_CASE_INSENSITIVE);

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
				assertThat(tableColumnCollationRow.get("collation_name")).isEqualTo(COLLATION_CASE_SENSITIVE);

				// We have not changed the database collation, so we can reference the table and column names with the wrong
				// case and the query will work
				final String fhirIdSql = """
					SELECT fhir_id 
					FROM hfj_resource 
					WHERE fhir_id = '2029'
				""";

				final Map<String, Object> fhirIdRow = querySingleRow(connection, fhirIdSql);
				assertThat(fhirIdRow.get("fhir_id")).isEqualTo("2029");
			}
		}
	}

	// Helper methods for schema migration tests
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
			.map(String::toUpperCase)
			.orElse(NULL_PLACEHOLDER));
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

	@Configuration
	public static class TestConfig extends TestR5Config {

		@Value("${" + HAPI_DATABASE_PARTITION_MODE + ":false}")
		private boolean myDatabasePartitionMode;

		@Value("${" + INIT_SCHEMA + ":}")
		private String myInitSchemaClasspath;

		@Autowired
		private JpaDatabaseContextConfigParamObject myJpaDatabaseContextConfigParamObject;

		@Override
		@Bean
		public DataSource dataSource() {
			DataSource dataSource = myJpaDatabaseContextConfigParamObject.getJpaEmbeddedDatabase().getDataSource();

			HapiMigrationDao hapiMigrationDao = new HapiMigrationDao(dataSource, myJpaDatabaseContextConfigParamObject.getJpaEmbeddedDatabase().getDriverType(), MIGRATION_TABLENAME);
			HapiMigrationStorageSvc hapiMigrationStorageSvc = new HapiMigrationStorageSvc(hapiMigrationDao);

			if (isNotBlank(myInitSchemaClasspath)) {
				JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
				ourLog.info("Using database DDL file: {}", myInitSchemaClasspath);
				String statementsFile = ClasspathUtil.loadResource(myInitSchemaClasspath);
				List<String> statements = SqlUtil.splitSqlFileIntoStatements(statementsFile);
				for (String sql : statements) {
					jdbcTemplate.execute(sql);
				}
			}

			Set<String> flags= new HashSet<>();
			if (myDatabasePartitionMode) {
				flags.add(HapiFhirJpaMigrationTasks.FlagEnum.DB_PARTITION_MODE.getCommandLineValue());
			}

			MigrationTaskList tasks = new HapiFhirJpaMigrationTasks(flags).getAllTasks(VersionEnum.values());

			SchemaMigrator schemaMigrator = new SchemaMigrator(
				"HAPI FHIR", MIGRATION_TABLENAME, dataSource, new Properties(), tasks, hapiMigrationStorageSvc);
			schemaMigrator.setDriverType(myJpaDatabaseContextConfigParamObject.getJpaEmbeddedDatabase().getDriverType());

			ourLog.info("About to run migration...");
			schemaMigrator.createMigrationTableIfRequired();
			schemaMigrator.migrate();
			ourLog.info("Migration complete");


			return dataSource;
		}

		@Bean
		public JpaEmbeddedDatabase jpaEmbeddedDatabase(JpaDatabaseContextConfigParamObject theJpaDatabaseContextConfigParamObject) {
			return theJpaDatabaseContextConfigParamObject.getJpaEmbeddedDatabase();
		}

		@Override
		protected Properties jpaProperties() {
			Properties retVal = super.jpaProperties();
			retVal.put("hibernate.hbm2ddl.auto", "none");
			retVal.put("hibernate.dialect", myJpaDatabaseContextConfigParamObject.getDialect());
			return retVal;
		}

	}

	public static class JpaDatabaseContextConfigParamObject {
		final JpaEmbeddedDatabase myJpaEmbeddedDatabase;
		final String myDialect;

		public JpaDatabaseContextConfigParamObject(JpaEmbeddedDatabase theJpaEmbeddedDatabase, String theDialect) {
			myJpaEmbeddedDatabase = theJpaEmbeddedDatabase;
			myDialect = theDialect;
		}

		public JpaEmbeddedDatabase getJpaEmbeddedDatabase() {
			return myJpaEmbeddedDatabase;
		}

		public String getDialect() {
			return myDialect;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public IIdType doCreateResource(IBaseResource theResource) {
		return myDaoRegistry.getResourceDao(myFhirContext.getResourceType(theResource)).create(theResource, new SystemRequestDetails()).getId();
	}

	@SuppressWarnings("unchecked")
	@Override
	public IIdType doUpdateResource(IBaseResource theResource) {
		return myDaoRegistry.getResourceDao(myFhirContext.getResourceType(theResource)).update(theResource, new SystemRequestDetails()).getId();
	}

	@Override
	public FhirContext getFhirContext() {
		return myFhirContext;
	}

	@Override
	protected PlatformTransactionManager getTxManager() {
		return myTxManager;
	}

}


