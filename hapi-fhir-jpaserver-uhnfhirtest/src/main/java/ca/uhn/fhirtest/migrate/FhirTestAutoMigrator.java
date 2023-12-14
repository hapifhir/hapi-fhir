package ca.uhn.fhirtest.migrate;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.HapiMigrationStorageSvc;
import ca.uhn.fhir.jpa.migrate.MigrationTaskList;
import ca.uhn.fhir.jpa.migrate.SchemaMigrator;
import ca.uhn.fhir.jpa.migrate.dao.HapiMigrationDao;
import ca.uhn.fhir.jpa.migrate.tasks.HapiFhirJpaMigrationTasks;
import ca.uhn.fhir.util.VersionEnum;
import ca.uhn.fhirtest.config.CommonConfig;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Properties;
import java.util.Set;
import javax.sql.DataSource;

public class FhirTestAutoMigrator {

	private static final Logger ourLog = LoggerFactory.getLogger(FhirTestAutoMigrator.class);
	public static final String MIGRATION_TABLENAME = "MIGRATIONS";

	@Autowired
	private DataSource myDataSource;

	@PostConstruct
	public void run() {
		DriverTypeEnum driver;
		if (CommonConfig.isLocalTestMode()) {
			driver = DriverTypeEnum.H2_EMBEDDED;
		} else {
			driver = DriverTypeEnum.POSTGRES_9_4;
		}

		HapiMigrationDao hapiMigrationDao = new HapiMigrationDao(myDataSource, driver, MIGRATION_TABLENAME);
		HapiMigrationStorageSvc hapiMigrationStorageSvc = new HapiMigrationStorageSvc(hapiMigrationDao);

		MigrationTaskList tasks = new HapiFhirJpaMigrationTasks(Set.of()).getAllTasks(VersionEnum.values());

		SchemaMigrator schemaMigrator = new SchemaMigrator(
				"HAPI FHIR", MIGRATION_TABLENAME, myDataSource, new Properties(), tasks, hapiMigrationStorageSvc);
		schemaMigrator.setDriverType(driver);

		ourLog.info("About to run migration...");
		schemaMigrator.createMigrationTableIfRequired();
		schemaMigrator.migrate();
		ourLog.info("Migration complete");
	}
}
