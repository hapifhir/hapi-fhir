package ca.uhn.fhir.jpa.migrate;

import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import org.flywaydb.core.api.MigrationInfoService;

import java.util.List;
import java.util.Optional;

public interface IMigrator {

	void migrate();

	Optional<MigrationInfoService> getMigrationInfo();

	void addTasks(List<BaseTask> theMigrationTasks);
}
