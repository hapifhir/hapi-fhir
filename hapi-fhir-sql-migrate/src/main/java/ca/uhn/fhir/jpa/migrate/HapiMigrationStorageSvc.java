package ca.uhn.fhir.jpa.migrate;

import ca.uhn.fhir.jpa.migrate.dao.HapiMigrationDao;
import ca.uhn.fhir.jpa.migrate.entity.HapiMigrationEntity;
import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import org.flywaydb.core.api.MigrationVersion;

import java.util.Set;

public class HapiMigrationStorageSvc {
	private final HapiMigrationDao myHapiMigrationDao;

	public HapiMigrationStorageSvc(HapiMigrationDao theHapiMigrationDao) {
		myHapiMigrationDao = theHapiMigrationDao;
	}

	// WIP KHS this should exclude failed tasks
	public MigrationTaskList diff(MigrationTaskList theTaskList) {
		Set<MigrationVersion> appliedMigrationVersions = fetchAppliedMigrationVersions();

		return theTaskList.diff(appliedMigrationVersions);
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
