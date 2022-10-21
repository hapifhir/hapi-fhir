package ca.uhn.fhir.jpa.migrate;

/*-
 * #%L
 * HAPI FHIR Server - SQL Migration
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * The approach used in this class is borrowed from org.flywaydb.community.database.ignite.thin.IgniteThinDatabase
 */
public class HapiMigrationLock implements AutoCloseable {
	private static final Logger ourLog = LoggerFactory.getLogger(HapiMigrationLock.class);
	public static final int SLEEP_MILLIS_BETWEEN_LOCK_RETRIES = 1000;
	public static final int MAX_RETRY_ATTEMPTS = 50;

	private final String myLockDescription = UUID.randomUUID().toString();

	private final HapiMigrationStorageSvc myMigrationStorageSvc;


	public HapiMigrationLock(HapiMigrationStorageSvc theMigrationStorageSvc) {
		myMigrationStorageSvc = theMigrationStorageSvc;
		lock();
	}

	protected void lock() {

		int retryCount = 0;
		do {
			try {
				if (insertLockingRow()) {
					return;
				}
				retryCount++;
				ourLog.info("Waiting for lock on " + this);
				Thread.sleep(SLEEP_MILLIS_BETWEEN_LOCK_RETRIES);
			} catch (InterruptedException ex) {
				// Ignore - if interrupted, we still need to wait for lock to become available
			}
		} while (retryCount < MAX_RETRY_ATTEMPTS);

		throw new HapiMigrationException("Unable to obtain table lock - another Database Migration may be running");
	}

	private boolean insertLockingRow() {
		try {
			return myMigrationStorageSvc.insertLockRecord(myLockDescription);
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public void close() {
		myMigrationStorageSvc.deleteLockRecord(myLockDescription);
	}
}
