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
package ca.uhn.fhir.batch2.progress;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.coordinator.JobDefinitionRegistry;
import ca.uhn.fhir.batch2.maintenance.JobChunkProgressAccumulator;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.util.Logs;
import ca.uhn.fhir.util.StopWatch;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.util.Iterator;

public class JobInstanceProgressCalculator {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();
	private final IJobPersistence myJobPersistence;
	private final JobChunkProgressAccumulator myProgressAccumulator;
	private final JobInstanceStatusUpdater myJobInstanceStatusUpdater;

	public JobInstanceProgressCalculator(IJobPersistence theJobPersistence, JobChunkProgressAccumulator theProgressAccumulator, JobDefinitionRegistry theJobDefinitionRegistry) {
		myJobPersistence = theJobPersistence;
		myProgressAccumulator = theProgressAccumulator;
		myJobInstanceStatusUpdater = new JobInstanceStatusUpdater(theJobDefinitionRegistry);
	}

	public void calculateAndStoreInstanceProgress(String theInstanceId) {
		StopWatch stopWatch = new StopWatch();
		ourLog.trace("calculating progress: {}", theInstanceId);

		InstanceProgress instanceProgress = calculateInstanceProgress(theInstanceId);

		myJobPersistence.updateInstance(theInstanceId, currentInstance->{
			instanceProgress.updateInstance(currentInstance);

			if (instanceProgress.changed() || currentInstance.getStatus() == StatusEnum.IN_PROGRESS) {
				if (currentInstance.getCombinedRecordsProcessed() > 0) {
					ourLog.info("Job {} of type {} has status {} - {} records processed ({}/sec) - ETA: {}", currentInstance.getInstanceId(), currentInstance.getJobDefinitionId(), currentInstance.getStatus(), currentInstance.getCombinedRecordsProcessed(), currentInstance.getCombinedRecordsProcessedPerSecond(), currentInstance.getEstimatedTimeRemaining());
				} else {
					ourLog.info("Job {} of type {} has status {} - {} records processed", currentInstance.getInstanceId(), currentInstance.getJobDefinitionId(), currentInstance.getStatus(), currentInstance.getCombinedRecordsProcessed());
				}
				ourLog.debug(instanceProgress.toString());
			}


			if (instanceProgress.hasNewStatus()) {
				myJobInstanceStatusUpdater.updateInstanceStatus(currentInstance, instanceProgress.getNewStatus());
			}

			return true;
		});
		ourLog.trace("calculating progress: {} - complete in {}", theInstanceId, stopWatch);
	}

	@Nonnull
	public InstanceProgress calculateInstanceProgress(String instanceId) {
		InstanceProgress instanceProgress = new InstanceProgress();
		Iterator<WorkChunk> workChunkIterator = myJobPersistence.fetchAllWorkChunksIterator(instanceId, false);

		while (workChunkIterator.hasNext()) {
			WorkChunk next = workChunkIterator.next();
			// global stats
			myProgressAccumulator.addChunk(next);
			// instance stats
			instanceProgress.addChunk(next);
		}

		instanceProgress.calculateNewStatus();

		return instanceProgress;
	}

}
