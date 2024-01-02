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
import ca.uhn.fhir.jpa.migrate.entity.HapiMigrationEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * The approach used in this class is borrowed from org.flywaydb.community.database.ignite.thin.IgniteThinDatabase
 */
public class HapiMigrationLock implements AutoCloseable {
	public static final Integer LOCK_PID = -100;
	private static final Logger ourLog = LoggerFactory.getLogger(HapiMigrationLock.class);
	public static final int SLEEP_MILLIS_BETWEEN_LOCK_RETRIES = 1000;
	public static final int DEFAULT_MAX_RETRY_ATTEMPTS = 50;
	public static int ourMaxRetryAttempts = DEFAULT_MAX_RETRY_ATTEMPTS;
	public static final String CLEAR_LOCK_TABLE_WITH_DESCRIPTION = "CLEAR_LOCK_TABLE_WITH_DESCRIPTION";

	private final String myLockDescription = UUID.randomUUID().toString();

	private final HapiMigrationStorageSvc myMigrationStorageSvc;

	/**
	 * This constructor should only ever be called from within a try-with-resources so the lock is released when the block is exited
	 */
	public HapiMigrationLock(HapiMigrationStorageSvc theMigrationStorageSvc) {
		myMigrationStorageSvc = theMigrationStorageSvc;
		lock();
	}

	private void lock() {
		cleanLockTableIfRequested();

		int retryCount = 0;
		do {
			try {
				if (insertLockingRow()) {
					return;
				}
				retryCount++;

				if (retryCount < ourMaxRetryAttempts) {
					ourLog.info(
							"Waiting for lock on {}.  Retry {}/{}",
							myMigrationStorageSvc.getMigrationTablename(),
							retryCount,
							ourMaxRetryAttempts);
					Thread.sleep(SLEEP_MILLIS_BETWEEN_LOCK_RETRIES);
				}
			} catch (InterruptedException ex) {
				// Ignore - if interrupted, we still need to wait for lock to become available
			}
		} while (retryCount < ourMaxRetryAttempts);

		String message = "Unable to obtain table lock - another database migration may be running.  If no "
				+ "other database migration is running, then the previous migration did not shut down properly and the "
				+ "lock record needs to be deleted manually.  The lock record is located in the "
				+ myMigrationStorageSvc.getMigrationTablename() + " table with " + "INSTALLED_RANK = "
				+ LOCK_PID;

		Optional<HapiMigrationEntity> otherLockFound =
				myMigrationStorageSvc.findFirstByPidAndNotDescription(LOCK_PID, myLockDescription);
		if (otherLockFound.isPresent()) {
			message += " and DESCRIPTION = " + otherLockFound.get().getDescription();
		}

		throw new HapiMigrationException(Msg.code(2153) + message);
	}

	/**
	 *
	 * @return whether a lock record was successfully deleted
	 */
	boolean cleanLockTableIfRequested() {
		String description = System.getProperty(CLEAR_LOCK_TABLE_WITH_DESCRIPTION);
		if (isBlank(description)) {
			description = System.getenv(CLEAR_LOCK_TABLE_WITH_DESCRIPTION);
		}
		if (isBlank(description)) {
			return false;
		}

		ourLog.info("Repairing lock table.  Removing row in " + myMigrationStorageSvc.getMigrationTablename()
				+ " with INSTALLED_RANK = " + LOCK_PID + " and DESCRIPTION = " + description);
		boolean result = myMigrationStorageSvc.deleteLockRecord(description);
		if (result) {
			ourLog.info("Successfully removed lock record");
		} else {
			ourLog.info("No lock record found");
		}
		return result;
	}

	private boolean insertLockingRow() {
		try {
			boolean storedSuccessfully = myMigrationStorageSvc.insertLockRecord(myLockDescription);
			if (storedSuccessfully) {
				ourLog.info("Migration Lock Row added. [uuid={}]", myLockDescription);
			}
			return storedSuccessfully;
		} catch (Exception e) {
			ourLog.debug("Failed to insert lock record: {}", e.getMessage());
			return false;
		}
	}

	@Override
	public void close() {
		boolean result = myMigrationStorageSvc.deleteLockRecord(myLockDescription);
		if (!result) {
			ourLog.error("Failed to delete migration lock record for description = [{}]", myLockDescription);
		}
	}

	public static void setMaxRetryAttempts(int theMaxRetryAttempts) {
		ourMaxRetryAttempts = theMaxRetryAttempts;
	}
}
