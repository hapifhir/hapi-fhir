package ca.uhn.fhir.jpa.migrate;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import ca.uhn.fhir.jpa.subscription.module.subscriber.websocket.WebsocketConnectionValidator;
import org.apache.commons.dbcp2.BasicDataSource;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.MigrationInfoService;
import org.flywaydb.core.api.MigrationVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class MigrationValidator {
	private static final Logger ourLog = LoggerFactory.getLogger(WebsocketConnectionValidator.class);

	private final BasicDataSource myDataSource;
	private final FlywayMigrator myMigrator;

	public MigrationValidator(BasicDataSource theDataSource, List<BaseTask<?>> theMigrationTasks) {
		myDataSource = theDataSource;
		myMigrator = new FlywayMigrator(theDataSource);
		myMigrator.addTasks(theMigrationTasks);
	}

	public void validate() {
		try (Connection connection = myDataSource.getConnection()) {
			MigrationInfoService migrationInfo = myMigrator.getMigrationInfo();
			if (migrationInfo.pending().length > 0) {
				throw new ConfigurationException("The database schema for " + connection.getCatalog() + " is out of date.  " +
					"Current database schema version is " + getCurrentVersion(migrationInfo) + ".  Schema version required by application is " +
					getLastVersion(migrationInfo) + ".  Please run the database migrator.");
			}
			ourLog.info("Database schema confirmed at expected version " + getCurrentVersion(migrationInfo));
		} catch (SQLException e) {
			throw new ConfigurationException("Unable to connect to " + myDataSource.toString(), e);
		}
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
