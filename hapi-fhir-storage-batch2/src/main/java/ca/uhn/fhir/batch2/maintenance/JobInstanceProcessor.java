/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
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
package ca.uhn.fhir.batch2.maintenance;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.IReductionStepExecutorService;
import ca.uhn.fhir.batch2.channel.BatchJobSender;
import ca.uhn.fhir.batch2.coordinator.JobDefinitionRegistry;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkCursor;
import ca.uhn.fhir.batch2.model.JobWorkNotification;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunkMetadata;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import ca.uhn.fhir.batch2.progress.JobInstanceProgressCalculator;
import ca.uhn.fhir.batch2.progress.JobInstanceStatusUpdater;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.model.api.PagingIterator;
import ca.uhn.fhir.util.Logs;
import ca.uhn.fhir.util.StopWatch;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class JobInstanceProcessor {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();
	public static final long PURGE_THRESHOLD = 7L * DateUtils.MILLIS_PER_DAY;

	// 10k; we want to get as many as we can
	private static final int WORK_CHUNK_METADATA_BATCH_SIZE = 10000;
	private final IJobPersistence myJobPersistence;
	private final BatchJobSender myBatchJobSender;
	private final JobChunkProgressAccumulator myProgressAccumulator;
	private final JobInstanceProgressCalculator myJobInstanceProgressCalculator;
	private final JobInstanceStatusUpdater myJobInstanceStatusUpdater;
	private final IReductionStepExecutorService myReductionStepExecutorService;
	private final String myInstanceId;
	private final JobDefinitionRegistry myJobDefinitionegistry;

	public JobInstanceProcessor(
			IJobPersistence theJobPersistence,
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
		myJobInstanceProgressCalculator =
				new JobInstanceProgressCalculator(theJobPersistence, theProgressAccumulator, theJobDefinitionRegistry);
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

		JobDefinition<? extends IModelJson> jobDefinition =
				myJobDefinitionegistry.getJobDefinitionOrThrowException(theInstance);

		// move POLL_WAITING -> READY
		processPollingChunks(theInstance.getInstanceId());
		// determine job progress; delete CANCELED/COMPLETE/FAILED jobs that are no longer needed
		cleanupInstance(theInstance);
		// move gated jobs to the next step, if needed
		// moves GATE_WAITING / QUEUED (legacy) chunks to:
		// READY (for regular gated jobs)
		// REDUCTION_READY (if it's the final reduction step)
		triggerGatedExecutions(theInstance, jobDefinition);

		if (theInstance.hasGatedStep() && theInstance.isRunning()) {
			Optional<JobInstance> updatedInstance = myJobPersistence.fetchInstance(theInstance.getInstanceId());

			if (updatedInstance.isEmpty()) {
				return;
			}

			JobWorkCursor<?, ?, ?> jobWorkCursor = JobWorkCursor.fromJobDefinitionAndRequestedStepId(
					jobDefinition, updatedInstance.get().getCurrentGatedStepId());
			if (jobWorkCursor.isReductionStep()) {
				// Reduction step work chunks should never be sent to the queue but to its specific service instead.
				triggerReductionStep(theInstance, jobWorkCursor);
				return;
			}
		}

		// enqueue all READY chunks
		enqueueReadyChunks(theInstance, jobDefinition);

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
				// wipmb For 6.8 - Are we deliberately not purging chunks for cancelled jobs?  This is a very
				// complicated way to say that.
				return;
		}

		if (theInstance.isFinished() && !theInstance.isWorkChunksPurged()) {
			myJobPersistence.deleteChunksAndMarkInstanceAsChunksPurged(theInstance.getInstanceId());
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

	private void triggerGatedExecutions(JobInstance theInstance, JobDefinition<?> theJobDefinition) {
		// QUEUE'd jobs that are gated need to start; this step will do that
		if (!theInstance.isRunning()
				&& (theInstance.getStatus() != StatusEnum.QUEUED && theJobDefinition.isGatedExecution())) {
			ourLog.debug(
					"JobInstance {} is not in a \"running\" state. Status {}",
					theInstance.getInstanceId(),
					theInstance.getStatus());
			return;
		}

		if (!theInstance.hasGatedStep()) {
			return;
		}

		JobWorkCursor<?, ?, ?> jobWorkCursor = JobWorkCursor.fromJobDefinitionAndRequestedStepId(
				theJobDefinition, theInstance.getCurrentGatedStepId());
		String instanceId = theInstance.getInstanceId();
		String currentStepId = jobWorkCursor.getCurrentStepId();
		boolean canAdvance = canAdvanceGatedJob(theInstance);
		if (canAdvance) {
			if (!jobWorkCursor.isFinalStep()) {
				// all other gated job steps except for final steps - final steps does not need to be advanced
				String nextStepId = jobWorkCursor.nextStep.getStepId();
				ourLog.info(
						"All processing is complete for gated execution of instance {} step {}. Proceeding to step {}",
						instanceId,
						currentStepId,
						nextStepId);

				processChunksForNextGatedSteps(theInstance, theJobDefinition, nextStepId);
			} else {
				ourLog.info(
						"Ready to advance gated execution of instance {} but already at the final step {}. Not proceeding to advance steps.",
						instanceId,
						jobWorkCursor.getCurrentStepId());
			}
		} else {
			String stepId = jobWorkCursor.nextStep != null
					? jobWorkCursor.nextStep.getStepId()
					: jobWorkCursor.getCurrentStepId();
			ourLog.debug(
					"Not ready to advance gated execution of instance {} from step {} to {}.",
					instanceId,
					currentStepId,
					stepId);
		}
	}

	private boolean canAdvanceGatedJob(JobInstance theInstance) {
		// make sure our instance still exists
		if (myJobPersistence.fetchInstance(theInstance.getInstanceId()).isEmpty()) {
			// no more job
			return false;
		}
		String currentGatedStepId = theInstance.getCurrentGatedStepId();

		Set<WorkChunkStatusEnum> workChunkStatuses = myJobPersistence.getDistinctWorkChunkStatesForJobAndStep(
				theInstance.getInstanceId(), currentGatedStepId);

		if (workChunkStatuses.isEmpty()) {
			// no work chunks = no output
			// trivial to advance to next step
			ourLog.info("No workchunks for {} in step id {}", theInstance.getInstanceId(), currentGatedStepId);
			return true;
		}

		// all workchunks for the current step are in COMPLETED -> proceed.
		return workChunkStatuses.equals(Set.of(WorkChunkStatusEnum.COMPLETED));
	}

	protected PagingIterator<WorkChunkMetadata> getReadyChunks() {
		return new PagingIterator<>(WORK_CHUNK_METADATA_BATCH_SIZE, (index, batchsize, consumer) -> {
			Pageable pageable = Pageable.ofSize(batchsize).withPage(index);
			Page<WorkChunkMetadata> results = myJobPersistence.fetchAllWorkChunkMetadataForJobInStates(
					pageable, myInstanceId, Set.of(WorkChunkStatusEnum.READY));
			for (WorkChunkMetadata metadata : results) {
				consumer.accept(metadata);
			}
		});
	}

	/**
	 * Trigger the reduction step for the given job instance. Reduction step chunks should never be queued.
	 */
	private void triggerReductionStep(JobInstance theInstance, JobWorkCursor<?, ?, ?> jobWorkCursor) {
		String instanceId = theInstance.getInstanceId();
		ourLog.debug("Triggering Reduction step {} of instance {}.", jobWorkCursor.getCurrentStepId(), instanceId);
		myReductionStepExecutorService.triggerReductionStep(instanceId, jobWorkCursor);
	}

	/**
	 * Chunks are initially created in READY state.
	 * We will move READY chunks to QUEUE'd and send them to the queue/topic (kafka)
	 * for processing.
	 */
	private void enqueueReadyChunks(JobInstance theJobInstance, JobDefinition<?> theJobDefinition) {
		Iterator<WorkChunkMetadata> iter = getReadyChunks();

		int counter = 0;
		while (iter.hasNext()) {
			WorkChunkMetadata metadata = iter.next();

			/*
			 * For each chunk id
			 * * Move to QUEUE'd
			 * * Send to topic
			 * * flush changes
			 * * commit
			 */
			updateChunkAndSendToQueue(metadata);
			counter++;
		}
		ourLog.debug(
				"Encountered {} READY work chunks for job {} of type {}",
				counter,
				theJobInstance.getInstanceId(),
				theJobDefinition.getJobDefinitionId());
	}

	/**
	 * Updates the Work Chunk and sends it to the queue.
	 *
	 * Because ReductionSteps are done inline by the maintenance pass,
	 * those will not be sent to the queue (but they will still have their
	 * status updated from READY -> QUEUED).
	 *
	 * Returns true after processing.
	 */
	private void updateChunkAndSendToQueue(WorkChunkMetadata theChunk) {
		String chunkId = theChunk.getId();
		myJobPersistence.enqueueWorkChunkForProcessing(chunkId, updated -> {
			ourLog.info("Updated {} workchunk with id {}", updated, chunkId);
			if (updated == 1) {
				sendNotification(theChunk);
			} else {
				// means the work chunk is likely already gone...
				// we'll log and skip it. If it's still in the DB, the next pass
				// will pick it up. Otherwise, it's no longer important
				ourLog.error(
						"Job Instance {} failed to transition work chunk with id {} from READY to QUEUED; found {}, expected 1; skipping work chunk.",
						theChunk.getInstanceId(),
						theChunk.getId(),
						updated);
			}
		});
	}

	private void sendNotification(WorkChunkMetadata theChunk) {
		// send to the queue
		// we use current step id because it has not been moved to the next step (yet)
		JobWorkNotification workNotification = new JobWorkNotification(
				theChunk.getJobDefinitionId(),
				theChunk.getJobDefinitionVersion(),
				theChunk.getInstanceId(),
				theChunk.getTargetStepId(),
				theChunk.getId());
		myBatchJobSender.sendWorkChannelMessage(workNotification);
	}

	private void processChunksForNextGatedSteps(
			JobInstance theInstance, JobDefinition<?> theJobDefinition, String nextStepId) {
		String instanceId = theInstance.getInstanceId();

		List<String> gateWaitingChunksForNextStep = myProgressAccumulator.getChunkIdsWithStatus(
				instanceId, nextStepId, WorkChunkStatusEnum.GATE_WAITING, WorkChunkStatusEnum.QUEUED);
		int totalChunksForNextStep = myProgressAccumulator.getTotalChunkCountForInstanceAndStep(instanceId, nextStepId);
		if (totalChunksForNextStep != gateWaitingChunksForNextStep.size()) {
			ourLog.debug(
					"Total ProgressAccumulator GATE_WAITING chunk count does not match GATE_WAITING chunk size! [instanceId={}, stepId={}, totalChunks={}, queuedChunks={}]",
					instanceId,
					nextStepId,
					totalChunksForNextStep,
					gateWaitingChunksForNextStep.size());
		}

		JobWorkCursor<?, ?, ?> jobWorkCursor =
				JobWorkCursor.fromJobDefinitionAndRequestedStepId(theJobDefinition, nextStepId);

		// update the job step so the workers will process them.
		// Sets all chunks from QUEUED/GATE_WAITING -> READY (REDUCTION_READY for reduction jobs)
		myJobPersistence.advanceJobStepAndUpdateChunkStatus(instanceId, nextStepId, jobWorkCursor.isReductionStep());
	}

	/**
	 * Moves all POLL_WAITING work chunks to READY for work chunks whose
	 * nextPollTime has expired.
	 */
	private void processPollingChunks(String theInstanceId) {
		int updatedChunkCount = myJobPersistence.updatePollWaitingChunksForJobIfReady(theInstanceId);

		ourLog.debug(
				"Moved {} Work Chunks in POLL_WAITING to READY for Job Instance {}", updatedChunkCount, theInstanceId);
	}
}
