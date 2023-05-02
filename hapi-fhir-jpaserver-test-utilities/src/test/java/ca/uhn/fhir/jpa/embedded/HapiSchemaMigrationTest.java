package ca.uhn.fhir.jpa.embedded;


import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.HapiMigrationStorageSvc;
import ca.uhn.fhir.jpa.migrate.MigrationTaskList;
import ca.uhn.fhir.jpa.migrate.SchemaMigrator;
import ca.uhn.fhir.jpa.migrate.dao.HapiMigrationDao;
import ca.uhn.fhir.jpa.migrate.tasks.HapiFhirJpaMigrationTasks;
import ca.uhn.fhir.util.VersionEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Properties;

import static ca.uhn.fhir.jpa.migrate.SchemaMigrator.HAPI_FHIR_MIGRATION_TABLENAME;


public class HapiSchemaMigrationTest {

    private static final Logger ourLog = LoggerFactory.getLogger(HapiSchemaMigrationTest.class);

    public static final VersionEnum FIRST_TESTED_VERSION = VersionEnum.V5_1_0;
	public static final String TEST_SCHEMA_NAME = "test";

	@RegisterExtension
	static HapiEmbeddedDatabasesExtension myEmbeddedServersExtension = new HapiEmbeddedDatabasesExtension();

	@AfterEach
	public void afterEach(){
        myEmbeddedServersExtension.clearDatabases();
	}

	@ParameterizedTest
	@ArgumentsSource(HapiEmbeddedDatabasesExtension.DatabaseVendorProvider.class)
	public void testMigration(DriverTypeEnum theDriverType){
		ourLog.info("Running hapi fhir migration tasks for {}", theDriverType);

        JpaEmbeddedDatabase embeddedDatabase = myEmbeddedServersExtension.getEmbeddedDatabase(theDriverType);
        embeddedDatabase.initializeDatabaseForVersion(FIRST_TESTED_VERSION);
        embeddedDatabase.insertTestData(FIRST_TESTED_VERSION);

		HapiMigrationDao hapiMigrationDao = new HapiMigrationDao(embeddedDatabase.getDataSource(), theDriverType, HAPI_FHIR_MIGRATION_TABLENAME);
		HapiMigrationStorageSvc hapiMigrationStorageSvc = new HapiMigrationStorageSvc(hapiMigrationDao);

        MigrationTaskList migrationTasks = new HapiFhirJpaMigrationTasks(Collections.EMPTY_SET).getAllTasks(VersionEnum.values());
		SchemaMigrator schemaMigrator = new SchemaMigrator(TEST_SCHEMA_NAME, HAPI_FHIR_MIGRATION_TABLENAME, embeddedDatabase.getDataSource(), new Properties(), migrationTasks, hapiMigrationStorageSvc);
		schemaMigrator.setDriverType(theDriverType);
		schemaMigrator.createMigrationTableIfRequired();
        schemaMigrator.migrate();
    }
}
