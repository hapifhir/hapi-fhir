/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.batch2.api;

import com.google.common.annotations.VisibleForTesting;

import java.io.Closeable;

public interface IJobMaintenanceService {

	/**
	 * Acquires the maintenance semaphore, blocking until any in-flight maintenance pass completes,
	 * then preventing new maintenance passes from starting until the returned {@link Closeable} is closed.
	 *
	 * <p>This is used by the expunge-everything operation to ensure that no maintenance pass
	 * is running while batch2 entities are being deleted, preventing FK constraint violations
	 * and deadlocks.
	 *
	 * @return a {@link Closeable} that releases the maintenance semaphore when closed.
	 *         The Closeable is idempotent — calling close() multiple times is safe.
	 */
	default Closeable holdMaintenanceForExpunge() {
		// No-op default: implementations that support expunge coordination should override this.
		return () -> {};
	}

	/**
	 * Do not wait for the next scheduled time for maintenance. Trigger it immediately.
	 * @return true if a request to run a maintenance pass was fired, false if there was already a trigger request in queue so we can just use that one
	 */
	boolean triggerMaintenancePass();

	void runMaintenancePass();

	/**
	 * Forces a second maintenance run.
	 * Only to be used in tests to simulate a long running maintenance step.
	 *
	 * <p><b>Warning:</b> This method acquires the maintenance semaphore with an uninterruptible
	 * blocking wait. Do not call this while {@link #holdMaintenanceForExpunge()} is held on the
	 * same thread — it will deadlock.
	 */
	@VisibleForTesting
	void forceMaintenancePass();

	/**
	 * This is only to be called in a testing environment
	 * to ensure state changes are controlled.
	 */
	@VisibleForTesting
	void enableMaintenancePass(boolean thetoEnable);
}
