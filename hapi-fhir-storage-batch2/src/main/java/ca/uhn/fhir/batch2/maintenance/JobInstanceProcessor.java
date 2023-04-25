package ca.uhn.fhir.batch2.maintenance;

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
import ca.uhn.fhir.batch2.api.IReductionStepExecutorService;
import ca.uhn.fhir.batch2.channel.BatchJobSender;
import ca.uhn.fhir.batch2.coordinator.JobDefinitionRegistry;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkCursor;
import ca.uhn.fhir.batch2.model.JobWorkNotification;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import ca.uhn.fhir.batch2.progress.InstanceProgress;
import ca.uhn.fhir.batch2.progress.JobInstanceProgressCalculator;
import ca.uhn.fhir.batch2.progress.JobInstanceStatusUpdater;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.util.Logs;
import ca.uhn.fhir.util.StopWatch;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;

import java.util.List;

public class JobInstanceProcessor {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();
	public static final long PURGE_THRESHOLD = 7L * DateUtils.MILLIS_PER_DAY;

	private final IJobPersistence myJobPersistence;
	private final BatchJobSender myBatchJobSender;
	private final JobChunkProgressAccumulator myProgressAccumulator;
	private final JobInstanceProgressCalculator myJobInstanceProgressCalculator;
	private final JobInstanceStatusUpdater myJobInstanceStatusUpdater;
	private final IReductionStepExecutorService myReductionStepExecutorService;
	private final String myInstanceId;
	private final JobDefinitionRegistry myJobDefinitionegistry;

	public JobInstanceProcessor(IJobPersistence theJobPersistence,
								BatchJobSender theBatchJobSender,
								String theInstanceId,
								JobChunkProgressAccumulator theProgressAccumulator,
								IReductionStepExecutorService theReductionStepExecutorService,
								JobDefinitionRegistry theJobDefinitionRegistry) {
		myJobPersistence = theJobPersistence;
		myBatchJobSender = theBatchJobSender;
		myInstanceId = theInstanceId;
		myProgressAccumulator = theProgressAccumulator;
		myReductionStepExecutorService = theReductionStepExecutorService;
		myJobDefinitionegistry = theJobDefinitionRegistry;
		myJobInstanceProgressCalculator = new JobInstanceProgressCalculator(theJobPersistence, theProgressAccumulator, theJobDefinitionRegistry);
		myJobInstanceStatusUpdater = new JobInstanceStatusUpdater(theJobDefinitionRegistry);
	}

	public void process() {
		ourLog.debug("Starting job processing: {}", myInstanceId);
		StopWatch stopWatch = new StopWatch();

		JobInstance theInstance = myJobPersistence.fetchInstance(myInstanceId).orElse(null);
		if (theInstance == null) {
			return;
		}

		boolean cancelUpdate = handleCancellation(theInstance);
		if (cancelUpdate) {
			// reload after update
			theInstance = myJobPersistence.fetchInstance(myInstanceId).orElseThrow();
		}
		cleanupInstance(theInstance);
		triggerGatedExecutions(theInstance);
		
		ourLog.debug("Finished job processing: {} - {}", myInstanceId, stopWatch);
	}

	private boolean handleCancellation(JobInstance theInstance) {
		if (theInstance.isPendingCancellationRequest()) {
			String errorMessage = buildCancelledMessage(theInstance);
			ourLog.info("Job {} moving to CANCELLED", theInstance.getInstanceId());
			return myJobPersistence.updateInstance(theInstance.getInstanceId(), instance -> {
				boolean changed = myJobInstanceStatusUpdater.updateInstanceStatus(instance, StatusEnum.CANCELLED);
				if (changed) {
					instance.setErrorMessage(errorMessage);
				}
				return changed;
			});
		}
		return false;
	}

	private String buildCancelledMessage(JobInstance theInstance) {
		String msg = "Job instance cancelled";
		if (theInstance.hasGatedStep()) {
			msg += " while running step " + theInstance.getCurrentGatedStepId();
		}
		return msg;
	}

	private void cleanupInstance(JobInstance theInstance) {
		switch (theInstance.getStatus()) {
			case QUEUED:
				// If we're still QUEUED, there are no stats to calculate
				break;
			case FINALIZE:
				// If we're in FINALIZE, the reduction step is working, so we should stay out of the way until it
				// marks the job as COMPLETED
				return;
			case IN_PROGRESS:
			case ERRORED:
				myJobInstanceProgressCalculator.calculateAndStoreInstanceProgress(theInstance.getInstanceId());
				break;
			case COMPLETED:
			case FAILED:
				if (purgeExpiredInstance(theInstance)) {
					return;
				}
				break;
			case CANCELLED:
				purgeExpiredInstance(theInstance);
				return;
		}

		if (theInstance.isFinished() && !theInstance.isWorkChunksPurged()) {
			myJobPersistence.deleteChunksAndMarkInstanceAsChunksPurged(theInstance.getInstanceId());

			InstanceProgress progress = myJobInstanceProgressCalculator.calculateInstanceProgress(theInstance.getInstanceId());

			myJobPersistence.updateInstance(theInstance.getInstanceId(), instance->{
				progress.updateInstance(instance);
				instance.setWorkChunksPurged(true);
				return true;
			});
		}
	}

	private boolean purgeExpiredInstance(JobInstance theInstance) {
		if (theInstance.getEndTime() != null) {
			long cutoff = System.currentTimeMillis() - PURGE_THRESHOLD;
			if (theInstance.getEndTime().getTime() < cutoff) {
				ourLog.info("Deleting old job instance {}", theInstance.getInstanceId());
				myJobPersistence.deleteInstanceAndChunks(theInstance.getInstanceId());
				return true;
			}
		}
		return false;
	}

	private void triggerGatedExecutions(JobInstance theInstance) {
		if (!theInstance.isRunning()) {
			ourLog.debug("JobInstance {} is not in a \"running\" state. Status {}",
				theInstance.getInstanceId(), theInstance.getStatus());
			return;
		}

		if (!theInstance.hasGatedStep()) {
			return;
		}

		JobDefinition<? extends IModelJson> jobDefinition = myJobDefinitionegistry.getJobDefinitionOrThrowException(theInstance);
		JobWorkCursor<?, ?, ?> jobWorkCursor = JobWorkCursor.fromJobDefinitionAndRequestedStepId(jobDefinition, theInstance.getCurrentGatedStepId());

		// final step
		if (jobWorkCursor.isFinalStep() && !jobWorkCursor.isReductionStep()) {
			ourLog.debug("Job instance {} is in final step and it's not a reducer step", theInstance.getInstanceId());
			return;
		}

		String instanceId = theInstance.getInstanceId();
		String currentStepId = jobWorkCursor.getCurrentStepId();
		boolean shouldAdvance = myJobPersistence.canAdvanceInstanceToNextStep(instanceId, currentStepId);
		if (shouldAdvance) {
			String nextStepId = jobWorkCursor.nextStep.getStepId();
			ourLog.info("All processing is complete for gated execution of instance {} step {}. Proceeding to step {}", instanceId, currentStepId, nextStepId);

			if (jobWorkCursor.nextStep.isReductionStep()) {
				JobWorkCursor<?, ?, ?> nextJobWorkCursor = JobWorkCursor.fromJobDefinitionAndRequestedStepId(jobDefinition, jobWorkCursor.nextStep.getStepId());
				myReductionStepExecutorService.triggerReductionStep(instanceId, nextJobWorkCursor);
			} else {
				// otherwise, continue processing as expected
				processChunksForNextSteps(theInstance, nextStepId);
			}
		} else {
			ourLog.debug("Not ready to advance gated execution of instance {} from step {} to {}.",
				instanceId, currentStepId, jobWorkCursor.nextStep.getStepId());
		}
	}

	private void processChunksForNextSteps(JobInstance theInstance, String nextStepId) {
		String instanceId = theInstance.getInstanceId();
		List<String> queuedChunksForNextStep = myProgressAccumulator.getChunkIdsWithStatus(instanceId, nextStepId, WorkChunkStatusEnum.QUEUED);
		int totalChunksForNextStep = myProgressAccumulator.getTotalChunkCountForInstanceAndStep(instanceId, nextStepId);
		if (totalChunksForNextStep != queuedChunksForNextStep.size()) {
			ourLog.debug("Total ProgressAccumulator QUEUED chunk count does not match QUEUED chunk size! [instanceId={}, stepId={}, totalChunks={}, queuedChunks={}]", instanceId, nextStepId, totalChunksForNextStep, queuedChunksForNextStep.size());
		}
		// Note on sequence: we don't have XA transactions, and are talking to two stores (JPA + Queue)
		// Sequence: 1 - So we run the query to minimize the work overlapping.
		List<String> chunksToSubmit = myJobPersistence.fetchAllChunkIdsForStepWithStatus(instanceId, nextStepId, WorkChunkStatusEnum.QUEUED);
		// Sequence: 2 - update the job step so the workers will process them.
		boolean changed = myJobPersistence.updateInstance(instanceId, instance -> {
			if (instance.getCurrentGatedStepId().equals(nextStepId)) {
				// someone else beat us here.  No changes
				return false;
			}
			instance.setCurrentGatedStepId(nextStepId);
			return true;
		});
		if (!changed) {
			// we collided with another maintenance job.
			return;
		}

		// DESIGN GAP: if we die here, these chunks will never be queued.
		// Need a WAITING stage before QUEUED for chunks, so we can catch them.

		// Sequence: 3 - send the notifications
		for (String nextChunkId : chunksToSubmit) {
			JobWorkNotification workNotification = new JobWorkNotification(theInstance, nextStepId, nextChunkId);
			myBatchJobSender.sendWorkChannelMessage(workNotification);
		}
		ourLog.debug("Submitted a batch of chunks for processing. [chunkCount={}, instanceId={}, stepId={}]", chunksToSubmit.size(), instanceId, nextStepId);
	}

}
