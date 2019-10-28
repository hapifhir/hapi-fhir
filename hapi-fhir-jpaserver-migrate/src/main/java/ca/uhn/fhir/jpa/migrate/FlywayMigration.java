package ca.uhn.fhir.jpa.migrate;

import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.flywaydb.core.api.MigrationVersion;
import org.flywaydb.core.api.migration.Context;
import org.flywaydb.core.api.migration.JavaMigration;

import java.sql.SQLException;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class FlywayMigration implements JavaMigration {
	private final BaseTask myTask;
	private final FlywayMigrator myFlywayMigrator;
	private DriverTypeEnum.ConnectionProperties myConnectionProperties;

	public FlywayMigration(BaseTask theTask, FlywayMigrator theFlywayMigrator) {
		myTask = theTask;
		myFlywayMigrator = theFlywayMigrator;
	}

	@Override
	public MigrationVersion getVersion() {
		return MigrationVersion.fromVersion(myTask.getFlywayVersion());
	}

	@Override
	public String getDescription() {
		String retval = myTask.getDescription();
		if (retval == null) {
			retval = myTask.getClass().getSimpleName() + " " + getVersion();
		}
		return retval;
	}

	@Override
	public Integer getChecksum() {
		// FIXME KHS
		return 0;
	}

	@Override
	public boolean isUndo() {
		return false;
	}

	@Override
	public boolean canExecuteInTransaction() {
		return false;
	}

	@Override
	public void migrate(Context theContext) throws Exception {
		myTask.setDriverType(myFlywayMigrator.getDriverType());
		myTask.setDryRun(myFlywayMigrator.isDryRun());
		myTask.setNoColumnShrink(myFlywayMigrator.isNoColumnShrink());
		myTask.setConnectionProperties(myConnectionProperties);
		try {
			myTask.execute();
		} catch (SQLException e) {
			String description = myTask.getDescription();
			if (isBlank(description)) {
				description = myTask.getClass().getSimpleName();
			}
			String prefix = "Failure executing task \"" + description + "\", aborting! Cause: ";
			throw new InternalErrorException(prefix + e.toString(), e);
		}
	}

	public void setConnectionProperties(DriverTypeEnum.ConnectionProperties theConnectionProperties) {
		myConnectionProperties = theConnectionProperties;
	}
}
