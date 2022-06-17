package ca.uhn.fhir.batch2.maintenance;

/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
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

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.channel.BatchJobSender;
import ca.uhn.fhir.batch2.coordinator.JobStepExecutorOutput;
import ca.uhn.fhir.batch2.coordinator.StepExecutionSvc;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkCursor;
import ca.uhn.fhir.batch2.model.JobWorkNotification;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.progress.JobInstanceProgressCalculator;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumSet;
import java.util.List;

public class JobInstanceProcessor {
	private static final Logger ourLog = LoggerFactory.getLogger(JobInstanceProcessor.class);
	public static final long PURGE_THRESHOLD = 7L * DateUtils.MILLIS_PER_DAY;

	private final IJobPersistence myJobPersistence;
	private final BatchJobSender myBatchJobSender;

	private final JobInstance myInstance;
	private final JobChunkProgressAccumulator myProgressAccumulator;
	private final JobInstanceProgressCalculator myJobInstanceProgressCalculator;
	private final StepExecutionSvc myJobExecutorSvc;

	JobInstanceProcessor(IJobPersistence theJobPersistence,
								BatchJobSender theBatchJobSender,
								JobInstance theInstance,
								JobChunkProgressAccumulator theProgressAccumulator,
								StepExecutionSvc theExecutorSvc
	) {
		myJobPersistence = theJobPersistence;
		myBatchJobSender = theBatchJobSender;
		myInstance = theInstance;
		myJobExecutorSvc = theExecutorSvc;
		myProgressAccumulator = theProgressAccumulator;
		myJobInstanceProgressCalculator = new JobInstanceProgressCalculator(theJobPersistence, theInstance, theProgressAccumulator);
	}

	public void process() {
		handleCancellation();
		cleanupInstance();
		triggerGatedExecutions();
	}

	private void handleCancellation() {
		if (myInstance.isPendingCancellation()) {
			myInstance.setErrorMessage(buildCancelledMessage());
			myInstance.setStatus(StatusEnum.CANCELLED);
			myJobPersistence.updateInstance(myInstance);
		}
	}

	private String buildCancelledMessage() {
		String msg = "Job instance cancelled";
		if (myInstance.hasGatedStep()) {
			msg += " while running step " + myInstance.getCurrentGatedStepId();
		}
		return msg;
	}

	private void cleanupInstance() {
		switch (myInstance.getStatus()) {
			case QUEUED:
				break;
			case IN_PROGRESS:
			case ERRORED:
				myJobInstanceProgressCalculator.calculateAndStoreInstanceProgress();
				break;
			case COMPLETED:
			case FAILED:
			case CANCELLED:
				if (purgeExpiredInstance()) {
					return;
				}
				break;
		}

		if (myInstance.isFinished() && !myInstance.isWorkChunksPurged()) {
			myInstance.setWorkChunksPurged(true);
			myJobPersistence.deleteChunks(myInstance.getInstanceId());
			myJobPersistence.updateInstance(myInstance);
		}
	}

	private boolean purgeExpiredInstance() {
		if (myInstance.getEndTime() != null) {
			long cutoff = System.currentTimeMillis() - PURGE_THRESHOLD;
			if (myInstance.getEndTime().getTime() < cutoff) {
				ourLog.info("Deleting old job instance {}", myInstance.getInstanceId());
				myJobPersistence.deleteInstanceAndChunks(myInstance.getInstanceId());
				return true;
			}
		}
		return false;
	}

	private void triggerGatedExecutions() {
		if (!myInstance.isRunning()) {
			return;
		}

		if (!myInstance.hasGatedStep()) {
			return;
		}

		JobWorkCursor<?, ?, ?> jobWorkCursor = JobWorkCursor.fromJobDefinitionAndRequestedStepId(myInstance.getJobDefinition(), myInstance.getCurrentGatedStepId());

		// final step
		if (jobWorkCursor.isFinalStep()) {
			return;
		}

		String instanceId = myInstance.getInstanceId();
		String currentStepId = jobWorkCursor.getCurrentStepId();
		int incompleteChunks = myProgressAccumulator.countChunksWithStatus(instanceId, currentStepId, StatusEnum.getIncompleteStatuses());

		if (incompleteChunks == 0) {
			String nextStepId = jobWorkCursor.nextStep.getStepId();

			ourLog.info("All processing is complete for gated execution of instance {} step {}. Proceeding to step {}", instanceId, currentStepId, nextStepId);

			if (jobWorkCursor.nextStep.isReductionStep()) {
				processReductionStep(jobWorkCursor);
			} else {
				// otherwise, continue processing as expected
				processChunksForNextSteps(instanceId, nextStepId);
			}
		}
	}

	private void processChunksForNextSteps(String instanceId, String nextStepId) {
		List<String> chunksForNextStep = myProgressAccumulator.getChunkIdsWithStatus(instanceId, nextStepId, EnumSet.of(StatusEnum.QUEUED));
		for (String nextChunkId : chunksForNextStep) {
			JobWorkNotification workNotification = new JobWorkNotification(myInstance, nextStepId, nextChunkId);
			myBatchJobSender.sendWorkChannelMessage(workNotification);
		}

		myInstance.setCurrentGatedStepId(nextStepId);
		myJobPersistence.updateInstance(myInstance);
	}

	private void processReductionStep(JobWorkCursor<?, ?, ?> jobWorkCursor) {
		// do execution of the final step now
		// (ie, we won't send to job workers)
		JobStepExecutorOutput<?, ?, ?> result = myJobExecutorSvc.doExecution(
			JobWorkCursor.fromJobDefinitionAndRequestedStepId(myInstance.getJobDefinition(), jobWorkCursor.nextStep.getStepId()),
			myInstance,
			null);
		if (!result.isSuccessful()) {
			myInstance.setStatus(StatusEnum.FAILED);
			myJobPersistence.updateInstance(myInstance);
		}
	}

	public static boolean updateInstanceStatus(JobInstance myInstance, StatusEnum newStatus) {
		if (myInstance.getStatus() != newStatus) {
			ourLog.info("Marking job instance {} of type {} as {}", myInstance.getInstanceId(), myInstance.getJobDefinitionId(), newStatus);
			myInstance.setStatus(newStatus);
			return true;
		}
		return false;
	}
}
