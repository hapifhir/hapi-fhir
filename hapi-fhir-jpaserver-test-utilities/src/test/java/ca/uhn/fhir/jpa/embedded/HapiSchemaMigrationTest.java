package ca.uhn.fhir.jpa.embedded;


import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.HapiMigrationStorageSvc;
import ca.uhn.fhir.jpa.migrate.MigrationTaskList;
import ca.uhn.fhir.jpa.migrate.SchemaMigrator;
import ca.uhn.fhir.jpa.migrate.dao.HapiMigrationDao;
import ca.uhn.fhir.jpa.migrate.tasks.HapiFhirJpaMigrationTasks;
import ca.uhn.fhir.system.HapiSystemProperties;
import ca.uhn.fhir.util.VersionEnum;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Properties;
import java.util.Set;

import static ca.uhn.fhir.jpa.embedded.HapiEmbeddedDatabasesExtension.FIRST_TESTED_VERSION;
import static ca.uhn.fhir.jpa.migrate.SchemaMigrator.HAPI_FHIR_MIGRATION_TABLENAME;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class HapiSchemaMigrationTest {

	private static final Logger ourLog = LoggerFactory.getLogger(HapiSchemaMigrationTest.class);
	public static final String TEST_SCHEMA_NAME = "test";

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

		VersionEnum[] allVersions =  VersionEnum.values();

		Set<VersionEnum> dataVersions = Set.of(
			VersionEnum.V5_2_0,
			VersionEnum.V5_3_0,
			VersionEnum.V5_4_0,
			VersionEnum.V5_5_0,
			VersionEnum.V6_0_0,
			VersionEnum.V6_6_0
		);

		int fromVersion = 0;
		VersionEnum from = allVersions[fromVersion];
		VersionEnum toVersion;

		for (int i = 0; i < allVersions.length; i++) {
			toVersion = allVersions[i];
			migrate(theDriverType, dataSource, hapiMigrationStorageSvc, toVersion);
			if (dataVersions.contains(toVersion)) {
				myEmbeddedServersExtension.insertPersistenceTestData(theDriverType, toVersion);
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
	}

	private static void migrate(DriverTypeEnum theDriverType, DataSource dataSource, HapiMigrationStorageSvc hapiMigrationStorageSvc, VersionEnum from, VersionEnum to) throws SQLException {
		MigrationTaskList migrationTasks = new HapiFhirJpaMigrationTasks(Collections.emptySet()).getTaskList(from, to);
		SchemaMigrator schemaMigrator = new SchemaMigrator(TEST_SCHEMA_NAME, HAPI_FHIR_MIGRATION_TABLENAME, dataSource, new Properties(), migrationTasks, hapiMigrationStorageSvc);
		schemaMigrator.setDriverType(theDriverType);
		schemaMigrator.createMigrationTableIfRequired();
		schemaMigrator.migrate();
	}

	private static void migrate(DriverTypeEnum theDriverType, DataSource dataSource, HapiMigrationStorageSvc hapiMigrationStorageSvc, VersionEnum to) throws SQLException {
		MigrationTaskList migrationTasks = new HapiFhirJpaMigrationTasks(Collections.emptySet()).getAllTasks(new VersionEnum[]{to});
		SchemaMigrator schemaMigrator = new SchemaMigrator(TEST_SCHEMA_NAME, HAPI_FHIR_MIGRATION_TABLENAME, dataSource, new Properties(), migrationTasks, hapiMigrationStorageSvc);
		schemaMigrator.setDriverType(theDriverType);
		schemaMigrator.createMigrationTableIfRequired();
		schemaMigrator.migrate();
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
