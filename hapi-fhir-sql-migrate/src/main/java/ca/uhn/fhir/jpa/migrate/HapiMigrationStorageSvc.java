package ca.uhn.fhir.jpa.migrate;

import ca.uhn.fhir.jpa.migrate.dao.HapiMigrationDao;
import ca.uhn.fhir.jpa.migrate.entity.HapiMigrationEntity;
import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import org.flywaydb.core.api.MigrationVersion;

import java.util.Set;

public class HapiMigrationStorageSvc {
	public static final String UNKNOWN_VERSION = "unknown";
	private final HapiMigrationDao myHapiMigrationDao;

	public HapiMigrationStorageSvc(HapiMigrationDao theHapiMigrationDao) {
		myHapiMigrationDao = theHapiMigrationDao;
	}

	public MigrationTaskList diff(MigrationTaskList theTaskList) {
		Set<MigrationVersion> appliedMigrationVersions = fetchAppliedMigrationVersions();

		return theTaskList.diff(appliedMigrationVersions);
	}

	Set<MigrationVersion> fetchAppliedMigrationVersions() {
		return myHapiMigrationDao.fetchSuccessfulMigrationVersions();
	}

	public String getLatestAppliedVersion() {
		return fetchAppliedMigrationVersions().stream()
			.sorted()
			.map(MigrationVersion::toString)
			.reduce((first, second) -> second)
			.orElse(UNKNOWN_VERSION);
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
