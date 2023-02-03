package ca.uhn.fhir.batch2.progress;

/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.maintenance.JobChunkProgressAccumulator;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.util.Logs;
import org.slf4j.Logger;

import java.util.Iterator;

public class JobInstanceProgressCalculator {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();
	private final IJobPersistence myJobPersistence;
	private final JobInstance myInstance;
	private final JobChunkProgressAccumulator myProgressAccumulator;
	private final JobInstanceStatusUpdater myJobInstanceStatusUpdater;

	public JobInstanceProgressCalculator(IJobPersistence theJobPersistence, JobInstance theInstance, JobChunkProgressAccumulator theProgressAccumulator) {
		myJobPersistence = theJobPersistence;
		myInstance = theInstance;
		myProgressAccumulator = theProgressAccumulator;
		myJobInstanceStatusUpdater = new JobInstanceStatusUpdater(theJobPersistence);
	}

	public void calculateAndStoreInstanceProgress() {
		InstanceProgress instanceProgress = new InstanceProgress();

		Iterator<WorkChunk> workChunkIterator = myJobPersistence.fetchAllWorkChunksIterator(myInstance.getInstanceId(), false);

		while (workChunkIterator.hasNext()) {
			WorkChunk next = workChunkIterator.next();
			myProgressAccumulator.addChunk(next);
			instanceProgress.addChunk(next);
		}

		instanceProgress.updateInstance(myInstance);

		if (instanceProgress.failed()) {
			myJobInstanceStatusUpdater.setFailed(myInstance);
			return;
		}


		if (instanceProgress.changed() || myInstance.getStatus() == StatusEnum.IN_PROGRESS) {
			if (myInstance.getCombinedRecordsProcessed() > 0) {
				ourLog.info("Job {} of type {} has status {} - {} records processed ({}/sec) - ETA: {}", myInstance.getInstanceId(), myInstance.getJobDefinitionId(), myInstance.getStatus(), myInstance.getCombinedRecordsProcessed(), myInstance.getCombinedRecordsProcessedPerSecond(), myInstance.getEstimatedTimeRemaining());
				ourLog.debug(instanceProgress.toString());
			} else {
				ourLog.info("Job {} of type {} has status {} - {} records processed", myInstance.getInstanceId(), myInstance.getJobDefinitionId(), myInstance.getStatus(), myInstance.getCombinedRecordsProcessed());
				ourLog.debug(instanceProgress.toString());
			}
		}

		if (instanceProgress.changed()) {
			if (instanceProgress.hasNewStatus()) {
				myJobInstanceStatusUpdater.updateInstanceStatus(myInstance, instanceProgress.getNewStatus());
			} else {
				myJobPersistence.updateInstance(myInstance);
			}
		}
	}
}
