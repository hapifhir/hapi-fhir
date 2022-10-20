package ca.uhn.fhir.jpa.migrate;

import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import org.flywaydb.core.api.CoreMigrationType;
import org.flywaydb.core.api.MigrationVersion;
import org.flywaydb.core.api.executor.MigrationExecutor;
import org.flywaydb.core.api.resolver.ResolvedMigration;
import org.flywaydb.core.extensibility.MigrationType;

public class HapiFhirResolvedMigration implements ResolvedMigration {
	private final BaseTask myTask;

	public HapiFhirResolvedMigration(BaseTask theTask) {
		myTask = theTask;
	}

	@Override
	public MigrationVersion getVersion() {
		return MigrationVersion.fromVersion(myTask.getMigrationVersion());
	}

	@Override
	public String getDescription() {
		return myTask.getDescription();
	}

	@Override
	public String getScript() {
		return "";
	}

	@Override
	public Integer getChecksum() {
		return myTask.hashCode();
	}

	@Override
	public MigrationType getType() {
		return CoreMigrationType.SCRIPT;
	}

	@Override
	public String getPhysicalLocation() {
		return "";
	}

	@Override
	public MigrationExecutor getExecutor() {
		return new HapiFhirMigrationExecutor(myTask);
	}

	@Override
	public boolean checksumMatches(Integer checksum) {
		return myTask.hashCode() == checksum;
	}

	@Override
	public boolean checksumMatchesWithoutBeingIdentical(Integer checksum) {
		return myTask.hashCode() == checksum;
	}
}
