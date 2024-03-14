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
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import ca.uhn.fhir.batch2.progress.JobInstanceProgressCalculator;
import ca.uhn.fhir.batch2.progress.JobInstanceStatusUpdater;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.util.Logs;
import ca.uhn.fhir.util.StopWatch;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

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

	private final PlatformTransactionManager myTransactionManager;

	public JobInstanceProcessor(
			IJobPersistence theJobPersistence,
			BatchJobSender theBatchJobSender,
			String theInstanceId,
			JobChunkProgressAccumulator theProgressAccumulator,
			IReductionStepExecutorService theReductionStepExecutorService,
			JobDefinitionRegistry theJobDefinitionRegistry,
			PlatformTransactionManager theTransactionManager) {
		myJobPersistence = theJobPersistence;
		myBatchJobSender = theBatchJobSender;
		myInstanceId = theInstanceId;
		myProgressAccumulator = theProgressAccumulator;
		myReductionStepExecutorService = theReductionStepExecutorService;
		myJobDefinitionegistry = theJobDefinitionRegistry;
		myJobInstanceProgressCalculator =
				new JobInstanceProgressCalculator(theJobPersistence, theProgressAccumulator, theJobDefinitionRegistry);
		myJobInstanceStatusUpdater = new JobInstanceStatusUpdater(theJobDefinitionRegistry);

		myTransactionManager = theTransactionManager;
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

		enqueueReadyChunks(theInstance, jobDefinition, false);
		cleanupInstance(theInstance);
		triggerGatedExecutions(theInstance, jobDefinition);

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
		if (!theInstance.isRunning() && (theInstance.getStatus() != StatusEnum.QUEUED && theJobDefinition.isGatedExecution())) {
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
		boolean canAdvance = canAdvanceGatedJob(theJobDefinition, theInstance, currentStepId);
		if (canAdvance) {
			if (jobWorkCursor.isReductionStep()) {
				// current step is the reduction step (all reduction steps are final)
				JobWorkCursor<?, ?, ?> nextJobWorkCursor = JobWorkCursor.fromJobDefinitionAndRequestedStepId(
						jobWorkCursor.getJobDefinition(), jobWorkCursor.getCurrentStepId());
				myReductionStepExecutorService.triggerReductionStep(instanceId, nextJobWorkCursor);
			} else if (jobWorkCursor.isFinalStep()) {
				// current step is the final step in a non-reduction gated job
				processChunksForNextGatedSteps(theInstance, theJobDefinition, jobWorkCursor, jobWorkCursor.getCurrentStepId());
			} else {
				// all other gated job steps
				String nextStepId = jobWorkCursor.nextStep.getStepId();
				ourLog.info(
					"All processing is complete for gated execution of instance {} step {}. Proceeding to step {}",
					instanceId,
					currentStepId,
					nextStepId);

				// otherwise, continue processing as expected
				processChunksForNextGatedSteps(theInstance, theJobDefinition, jobWorkCursor, nextStepId);
			}
		} else {
			ourLog.debug(
					"Not ready to advance gated execution of instance {} from step {} to {}.",
					instanceId,
					currentStepId,
					jobWorkCursor.nextStep.getStepId());
		}
	}

	private boolean canAdvanceGatedJob(JobDefinition<?> theJobDefinition, JobInstance theInstance, String theStepId) {
		// make sure our instance still exists
		if (myJobPersistence.fetchInstance(theInstance.getInstanceId()).isEmpty()) {
			// no more job
			return false;
		}

		Set<WorkChunkStatusEnum> workChunkStatuses = myJobPersistence.getDistinctWorkChunkStatesForJobAndStep(theInstance.getInstanceId(), theStepId);

		if (workChunkStatuses.isEmpty()) {
			// no work chunks = no output
			// trivial to advance to next step
			return true;
		}

		if (workChunkStatuses.equals(Set.of(WorkChunkStatusEnum.COMPLETED))) {
			// all previous workchunks are complete;
			// none in READY though -> still proceed
			return true;
		}

		if (workChunkStatuses.equals(Set.of(WorkChunkStatusEnum.READY))) {
			// all workchunks ready -> proceed
			return true;
		}

		// anything else
		return false;
	}

	/**
	 * Chunks are initially created in READY state.
	 * We will move READY chunks to QUEUE'd and send them to the queue/topic (kafka)
	 * for processing.
	 *
	 * We could block chunks from being moved from QUEUE'd to READY here for gated steps
	 * but currently, progress is calculated by looking at completed chunks only;
	 * we'd need a new GATE_WAITING state to move chunks to to prevent jobs from
	 * completing prematurely.
	 */
	private void enqueueReadyChunks(JobInstance theJobInstance, JobDefinition<?> theJobDefinition, boolean theIsGatedExecutionAdvancementBool) {
		// we need a transaction to access the stream of workchunks
		// because workchunks are created in READY state, there's an unknown
		// number of them (and so we could be reading many from the db)
		TransactionStatus status = myTransactionManager.getTransaction(new DefaultTransactionDefinition());
		Stream<WorkChunk> readyChunks = myJobPersistence.fetchAllWorkChunksForJobInStates(
			theJobInstance.getInstanceId(), Set.of(WorkChunkStatusEnum.READY));

		AtomicInteger counter = new AtomicInteger();
		readyChunks.forEach(chunk -> {
			JobWorkCursor<?, ?, ?> jobWorkCursor =
				JobWorkCursor.fromJobDefinitionAndRequestedStepId(theJobDefinition, chunk.getTargetStepId());
			counter.getAndIncrement();
			if (!theIsGatedExecutionAdvancementBool && (theJobDefinition.isGatedExecution() || jobWorkCursor.isReductionStep())) {
				/*
				 * Gated executions are queued later when all work chunks are ready.
				 *
				 * Reduction steps are not submitted to the queue at all, but processed inline.
				 * Currently all reduction steps are also gated, but this might not always
				 * be true.
				 */
				return;
			}

			/*
			 * For each chunk id
			 * * Move to QUEUE'd
			 * * Send to topic
			 * * flush changes
			 * * commit
			 */
			updateChunkAndSendToQueue(chunk, theJobInstance, theJobDefinition);
		});

		myTransactionManager.commit(status);

		ourLog.debug("Encountered {} READY work chunks for job {}", counter.get(), theJobDefinition.getJobDefinitionId());
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
	private void updateChunkAndSendToQueue(
			WorkChunk theChunk, JobInstance theInstance, JobDefinition<?> theJobDefinition) {
		String chunkId = theChunk.getId();
		myJobPersistence.enqueueWorkChunkForProcessing(chunkId, updated -> {
			ourLog.info("Updated {} workchunk with id {}", updated, chunkId);
			if (updated == 1) {
				// send to the queue
				// we use current step id because it has not been moved to the next step (yet)
				JobWorkNotification workNotification = new JobWorkNotification(
						theJobDefinition.getJobDefinitionId(),
						theJobDefinition.getJobDefinitionVersion(),
						theInstance.getInstanceId(),
						theChunk.getTargetStepId(),
						chunkId);
				myBatchJobSender.sendWorkChannelMessage(workNotification);
			} else {
				// means the work chunk is likely already gone...
				// we'll log and skip it. If it's still in the DB, the next pass
				// will pick it up. Otherwise, it's no longer important
				ourLog.error(
						"Job Instance {} failed to transition work chunk with id {} from READY to QUEUED; found {}, expected 1; skipping work chunk.",
						theInstance.getInstanceId(),
						theChunk.getId(),
						updated);
			}
		});
	}

	private void processChunksForNextGatedSteps(JobInstance theInstance, JobDefinition<?> theJobDefinition, JobWorkCursor<?, ?, ?> theWorkCursor, String nextStepId) {
		String instanceId = theInstance.getInstanceId();
		List<String> readyChunksForNextStep =
				myProgressAccumulator.getChunkIdsWithStatus(instanceId, nextStepId, WorkChunkStatusEnum.READY);
		int totalChunksForNextStep = myProgressAccumulator.getTotalChunkCountForInstanceAndStep(instanceId, nextStepId);
		if (totalChunksForNextStep != readyChunksForNextStep.size()) {
			ourLog.debug(
					"Total ProgressAccumulator READY chunk count does not match READY chunk size! [instanceId={}, stepId={}, totalChunks={}, queuedChunks={}]",
					instanceId,
					nextStepId,
					totalChunksForNextStep,
					readyChunksForNextStep.size());
		}

		// update the job step so the workers will process them.
		// if it's the last (gated) step, there will be no change - but we should
		// queue up the chunks anyways
		boolean changed = theWorkCursor.isFinalStep() || myJobPersistence.updateInstance(instanceId, instance -> {
			if (instance.getCurrentGatedStepId().equals(nextStepId)) {
				// someone else beat us here.  No changes
				return false;
			}
			instance.setCurrentGatedStepId(nextStepId);
			return true;
		});

		if (!changed) {
			// we collided with another maintenance job.
			ourLog.warn("Skipping gate advance to {} for instance {} - already advanced.", nextStepId, instanceId);
			return;
		}

		// because we now have all gated job chunks in READY state,
		// we can enqueue them
		enqueueReadyChunks(theInstance, theJobDefinition, true);
	}
}
