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
package ca.uhn.fhir.batch2.maintenance;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.util.Logs;
import ca.uhn.fhir.util.StopWatch;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;

/**
 * This processor is invoked by {@link JobMaintenanceServiceImpl}
 * and is responsible for cleaning up job instances that have ended.
 * This includes deleting work chunks for failed or completed jobs, as well as
 * eventually purging old job instances entirely.
 * It is intended to be run less frequently.
 */
public class EndedJobInstanceProcessor {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();
	public static final long PURGE_THRESHOLD = 7L * DateUtils.MILLIS_PER_DAY;

	// 10k; we want to get as many as we can
	private final IJobPersistence myJobPersistence;
	private final String myInstanceId;

	private long myPurgeThreshold = PURGE_THRESHOLD;

	public EndedJobInstanceProcessor(IJobPersistence theJobPersistence, String theInstanceId) {
		myJobPersistence = theJobPersistence;
		myInstanceId = theInstanceId;
	}

	public void setPurgeThreshold(long thePurgeThreshold) {
		myPurgeThreshold = thePurgeThreshold;
	}

	public void process() {
		ourLog.debug("Starting job processing: {}", myInstanceId);
		StopWatch stopWatch = new StopWatch();

		JobInstance theInstance = myJobPersistence.fetchInstance(myInstanceId).orElse(null);
		if (theInstance == null) {
			return;
		}

		// determine job progress; delete CANCELED/COMPLETE/FAILED jobs that are no longer needed
		cleanupInstance(theInstance);

		ourLog.debug("Finished job processing: {} - {}", myInstanceId, stopWatch);
	}

	private void cleanupInstance(JobInstance theInstance) {
		switch (theInstance.getStatus()) {
			case QUEUED:
			case FINALIZE:
			case IN_PROGRESS:
			case ERRORED:
				break;
			case COMPLETED:
			case FAILED:
			case CANCELLED:
				purgeExpiredInstance(theInstance);
				break;
		}

		if (theInstance.isFinished() && !theInstance.isWorkChunksPurged()) {
			myJobPersistence.deleteChunksAndMarkInstanceAsChunksPurged(theInstance.getInstanceId());
		}
	}

	private boolean purgeExpiredInstance(JobInstance theInstance) {
		if (theInstance.getEndTime() != null) {
			long cutoff = System.currentTimeMillis() - myPurgeThreshold;
			if (theInstance.getEndTime().getTime() < cutoff) {
				ourLog.info("Deleting old job instance {}", theInstance.getInstanceId());
				myJobPersistence.deleteInstanceAndChunks(theInstance.getInstanceId());
				return true;
			}
		}
		return false;
	}
}
