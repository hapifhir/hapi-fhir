package ca.uhn.fhir.jpa.migrate;

import ca.uhn.fhir.jpa.migrate.dao.HapiMigrationDao;
import ca.uhn.fhir.jpa.migrate.entity.HapiMigrationEntity;
import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import org.flywaydb.core.api.MigrationVersion;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HapiMigrationStorageSvc {
	private final HapiMigrationDao myHapiMigrationDao;

	public HapiMigrationStorageSvc(HapiMigrationDao theHapiMigrationDao) {
		myHapiMigrationDao = theHapiMigrationDao;
	}

	// WIP KHS this should exclude failed tasks
	public List<BaseTask> diff(List<BaseTask> theTasks) {
		Set<MigrationVersion> appliedMigrationVersions = fetchAppliedMigrationVersions();

		return theTasks.stream()
			.filter(task -> !appliedMigrationVersions.contains(MigrationVersion.fromVersion(task.getMigrationVersion())))
			.collect(Collectors.toList());
	}

	Set<MigrationVersion> fetchAppliedMigrationVersions() {
		return myHapiMigrationDao.fetchMigrationVersions();
	}

	// WIP KHS test
	public String getLatestAppliedVersion() {
		return fetchAppliedMigrationVersions().stream()
			.sorted()
			.map(MigrationVersion::toString)
			.reduce((first, second) -> second)
			.orElse("unknown");
	}

	public void saveTask(BaseTask theBaseTask, Integer theMillis, boolean theSuccess) {
		HapiMigrationEntity entity = HapiMigrationEntity.fromBaseTask(theBaseTask);
		entity.setExecutionTime(theMillis);
		entity.setSuccess(theSuccess);
		myHapiMigrationDao.save(entity);
	}

	public void createMigrationTableIfRequired() {
		myHapiMigrationDao.createMigrationTableIfRequired();
	}
}
