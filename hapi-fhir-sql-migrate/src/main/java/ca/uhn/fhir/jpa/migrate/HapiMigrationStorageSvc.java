/*-
 * #%L
 * HAPI FHIR Server - SQL Migration
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.migrate;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.migrate.dao.HapiMigrationDao;
import ca.uhn.fhir.jpa.migrate.entity.HapiMigrationEntity;
import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import org.flywaydb.core.api.MigrationVersion;

import java.util.Optional;
import java.util.Set;

public class HapiMigrationStorageSvc {
	public static final String UNKNOWN_VERSION = "unknown";
	public static final String LOCK_TYPE = "hapi-fhir-lock";

	private final HapiMigrationDao myHapiMigrationDao;

	public HapiMigrationStorageSvc(HapiMigrationDao theHapiMigrationDao) {
		myHapiMigrationDao = theHapiMigrationDao;
	}

	public String getMigrationTablename() {
		return myHapiMigrationDao.getMigrationTablename();
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
	public boolean createMigrationTableIfRequired() {
		return myHapiMigrationDao.createMigrationTableIfRequired();
	}

	/**
	 *
	 * @param  theLockDescription value of the Description for the lock record
	 * @return true if the record was successfully deleted
	 */
	public boolean deleteLockRecord(String theLockDescription) {
		verifyNoOtherLocksPresent(theLockDescription);

		// Remove the locking row
		return myHapiMigrationDao.deleteLockRecord(HapiMigrationLock.LOCK_PID, theLockDescription);
	}

	void verifyNoOtherLocksPresent(String theLockDescription) {
		Optional<HapiMigrationEntity> otherLockFound =
				myHapiMigrationDao.findFirstByPidAndNotDescription(HapiMigrationLock.LOCK_PID, theLockDescription);

		// Check that there are no other locks in place. This should not happen!
		if (otherLockFound.isPresent()) {
			throw new HapiMigrationException(
					Msg.code(2152) + "Internal error: on unlocking, a competing lock was found");
		}
	}

	public boolean insertLockRecord(String theLockDescription) {
		HapiMigrationEntity entity = new HapiMigrationEntity();
		entity.setPid(HapiMigrationLock.LOCK_PID);
		entity.setType(LOCK_TYPE);
		entity.setDescription(theLockDescription);
		entity.setExecutionTime(0);
		entity.setSuccess(true);

		return myHapiMigrationDao.save(entity);
	}

	public Optional<HapiMigrationEntity> findFirstByPidAndNotDescription(
			Integer theLockPid, String theLockDescription) {
		return myHapiMigrationDao.findFirstByPidAndNotDescription(theLockPid, theLockDescription);
	}
}
