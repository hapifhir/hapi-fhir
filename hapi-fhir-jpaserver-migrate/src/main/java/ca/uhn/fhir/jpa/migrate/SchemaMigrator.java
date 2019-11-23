package ca.uhn.fhir.jpa.migrate;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import org.apache.commons.dbcp2.BasicDataSource;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.MigrationInfoService;
import org.hibernate.cfg.AvailableSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class SchemaMigrator {
	private static final Logger ourLog = LoggerFactory.getLogger(SchemaMigrator.class);

	private final BasicDataSource myDataSource;
	private final FlywayMigrator myMigrator;
	private final boolean mySkipValidation;

	public SchemaMigrator(BasicDataSource theDataSource, Properties jpaProperties, List<BaseTask<?>> theMigrationTasks) {
		myDataSource = theDataSource;
		if (jpaProperties.containsKey(AvailableSettings.HBM2DDL_AUTO) && "update".equals(jpaProperties.getProperty(AvailableSettings.HBM2DDL_AUTO))) {
			mySkipValidation = true;
		} else {
			mySkipValidation = false;
		}
		myMigrator = new FlywayMigrator(theDataSource);
		myMigrator.addTasks(theMigrationTasks);
	}

	public void validate() {
		if (mySkipValidation) {
			ourLog.warn("Database running in hibernate auto-update mode.  Skipping schema validation.");
			return;
		}
		try (Connection connection = myDataSource.getConnection()) {
			MigrationInfoService migrationInfo = myMigrator.getMigrationInfo();
			if (migrationInfo.pending().length > 0) {
				throw new ConfigurationException("The database schema for " + myDataSource.getUrl() + " is out of date.  " +
					"Current database schema version is " + getCurrentVersion(migrationInfo) + ".  Schema version required by application is " +
					getLastVersion(migrationInfo) + ".  Please run the database migrator.");
			}
			ourLog.info("Database schema confirmed at expected version " + getCurrentVersion(migrationInfo));
		} catch (SQLException e) {
			throw new ConfigurationException("Unable to connect to " + myDataSource.toString(), e);
		}
	}

	public void migrate() {
		if (mySkipValidation) {
			ourLog.warn("Database running in hibernate auto-update mode.  Skipping schema migration.");
			return;
		}
		myMigrator.migrate();
	}

	private String getCurrentVersion(MigrationInfoService theMigrationInfo) {
		MigrationInfo migrationInfo = theMigrationInfo.current();
		if (migrationInfo == null) {
			return "unknown";
		}
		return migrationInfo.getVersion().toString();
	}

	private String getLastVersion(MigrationInfoService theMigrationInfo) {
		MigrationInfo[] pending = theMigrationInfo.pending();
		if (pending.length > 0) {
			return pending[pending.length - 1].getVersion().toString();
		}
		return "unknown";
	}
}
