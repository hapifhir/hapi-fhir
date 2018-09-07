package ca.uhn.fhir.cli;

import ca.uhn.fhir.jpa.migrate.Migrator;
import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import ca.uhn.fhir.jpa.migrate.tasks.HapiFhirJpaMigrationTasks;
import ca.uhn.fhir.util.VersionEnum;

import java.util.List;

public class HapiMigrateDatabaseCommand extends BaseMigrateDatabaseCommand {
	@Override
	protected void addTasks(Migrator theMigrator, VersionEnum theFrom, VersionEnum theTo) {
		List<BaseTask<?>> tasks = new HapiFhirJpaMigrationTasks().getTasks(theFrom, theTo);
		tasks.forEach(theMigrator::addTask);
	}
}
