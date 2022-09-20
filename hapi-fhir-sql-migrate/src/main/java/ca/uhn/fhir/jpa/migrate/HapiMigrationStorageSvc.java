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

	/**
	 * Returns a list of migration tasks that have not yet been successfully run against the database
	 * @param theTaskList the full list of tasks for this release
	 * @return a list of tasks that have not yet been successfully run against the database
	 */

	public MigrationTaskList diff(MigrationTaskList theTaskList) {
		Set<MigrationVersion> appliedMigrationVersions = fetchAppliedMigrationVersions();

		return theTaskList.diff(appliedMigrationVersions);
	}

	/**
	 *
	 * @return a list of migration versions that have been successfully run against the database
	 */
	Set<MigrationVersion> fetchAppliedMigrationVersions() {
		return myHapiMigrationDao.fetchSuccessfulMigrationVersions();
	}

	/**
	 *
	 * @return the most recent version that was run against the database (used for logging purposes)
	 */
	public String getLatestAppliedVersion() {
		return fetchAppliedMigrationVersions().stream()
			.sorted()
			.map(MigrationVersion::toString)
			.reduce((first, second) -> second)
			.orElse(UNKNOWN_VERSION);
	}

	/**
	 * Save a migration task to the database
	 */
	public void saveTask(BaseTask theBaseTask, Integer theMillis, boolean theSuccess) {
		HapiMigrationEntity entity = HapiMigrationEntity.fromBaseTask(theBaseTask);
		entity.setExecutionTime(theMillis);
		entity.setSuccess(theSuccess);
		myHapiMigrationDao.save(entity);
	}

	/**
	 * Create the migration table if it does not already exist
	 */

	public void createMigrationTableIfRequired() {
		myHapiMigrationDao.createMigrationTableIfRequired();
	}
}
