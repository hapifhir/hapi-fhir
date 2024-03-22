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
import ca.uhn.fhir.batch2.model.JobDefinitionStep;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkCursor;
import ca.uhn.fhir.batch2.model.JobWorkNotification;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunkMetadata;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import ca.uhn.fhir.batch2.progress.JobInstanceProgressCalculator;
import ca.uhn.fhir.batch2.progress.JobInstanceStatusUpdater;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.util.Logs;
import ca.uhn.fhir.util.StopWatch;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class JobInstanceProcessor {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();
	public static final long PURGE_THRESHOLD = 7L * DateUtils.MILLIS_PER_DAY;

	class ReadyChunkIterator implements Iterator<WorkChunkMetadata> {

		// 10,000 - we want big batches
		private static final int PAGE_SIZE = 10000;

		private Page<WorkChunkMetadata> currentPage;

		private int myPageIndex = 0;

		private int myItemIndex = 0;

		private final String myInstanceId;

		public ReadyChunkIterator(String theInstanceId) {
			myInstanceId = theInstanceId;
		}

		private void getNextPage() {
			if (currentPage == null || currentPage.hasNext()) {
				currentPage = myJobPersistence.fetchAllWorkChunkMetadataForJobInStates(
						myPageIndex++, getPageSize(), myInstanceId, Set.of(WorkChunkStatusEnum.READY));
				myItemIndex = 0;
			} else {
				currentPage = Page.empty();
			}
		}

		int getPageSize() {
			return PAGE_SIZE;
		}

		@Override
		public boolean hasNext() {
			if (currentPage == null) {
				getNextPage();
			}
			return currentPage.getContent().size() > myItemIndex || currentPage.hasNext();
		}

		@Override
		public WorkChunkMetadata next() {
			if (myItemIndex >= currentPage.getSize()) {
				getNextPage();
			}
			if (myItemIndex < currentPage.getSize()) {
				return currentPage.getContent().get(myItemIndex++);
			}
			return null;
		}
	}

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
		boolean canAdvance = canAdvanceGatedJob(theJobDefinition, theInstance, jobWorkCursor);
		if (canAdvance) {
			if (jobWorkCursor.isReductionStep()) {
				// current step is the reduction step (all reduction steps are final)
				JobWorkCursor<?, ?, ?> nextJobWorkCursor = JobWorkCursor.fromJobDefinitionAndRequestedStepId(
						jobWorkCursor.getJobDefinition(), jobWorkCursor.getCurrentStepId());
				myReductionStepExecutorService.triggerReductionStep(instanceId, nextJobWorkCursor);
			} else if (jobWorkCursor.isFinalStep()) {
				// current step is the final step in a non-reduction gated job
				processChunksForNextGatedSteps(
						theInstance, theJobDefinition, jobWorkCursor, jobWorkCursor.getCurrentStepId());
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

	private boolean canAdvanceGatedJob(
			JobDefinition<?> theJobDefinition, JobInstance theInstance, JobWorkCursor<?, ?, ?> theWorkCursor) {
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
			return true;
		}

		if (workChunkStatuses.equals(Set.of(WorkChunkStatusEnum.COMPLETED))) {
			// all previous workchunks are complete;
			// none in READY though -> still proceed
			return true;
		}

		if (workChunkStatuses.equals(Set.of(WorkChunkStatusEnum.READY))) {
			if (theWorkCursor.isFirstStep()) {
				// first step - all ready means we're ready to proceed to the next step
				return true;
			} else {
				// it's a future step;
				// make sure previous step's workchunks are completed
				JobDefinitionStep<?, ?, ?> previousStep =
						theJobDefinition.getSteps().get(0);
				for (JobDefinitionStep<?, ?, ?> step : theJobDefinition.getSteps()) {
					if (step.getStepId().equalsIgnoreCase(currentGatedStepId)) {
						break;
					}
					previousStep = step;
				}
				Set<WorkChunkStatusEnum> previousStepWorkChunkStates =
						myJobPersistence.getDistinctWorkChunkStatesForJobAndStep(
								theInstance.getInstanceId(), previousStep.getStepId());

				// completed means "all in COMPLETE state" or no previous chunks (they're cleaned up or never existed)
				if (previousStepWorkChunkStates.isEmpty()
						|| previousStepWorkChunkStates.equals(Set.of(WorkChunkStatusEnum.COMPLETED))) {
					return true;
				}
			}
		}

		// anything else
		return false;
	}

	protected ReadyChunkIterator getReadyChunks(String theInstanceId) {
		return new ReadyChunkIterator(theInstanceId);
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
	private void enqueueReadyChunks(
			JobInstance theJobInstance, JobDefinition<?> theJobDefinition, boolean theIsGatedExecutionAdvancementBool) {
		// we need a transaction to access the stream of workchunks
		// because workchunks are created in READY state, there's an unknown
		// number of them (and so we could be reading many from the db)
		Iterator<WorkChunkMetadata> iter = getReadyChunks(theJobInstance.getInstanceId());

		AtomicInteger counter = new AtomicInteger();
		ConcurrentHashMap<String, JobWorkCursor<?, ?, ?>> stepToWorkCursor = new ConcurrentHashMap<>();
		while (iter.hasNext()) {
			WorkChunkMetadata metadata = iter.next();
			JobWorkCursor<?, ?, ?> jobWorkCursor = stepToWorkCursor.computeIfAbsent(metadata.getTargetStepId(), (e) -> {
				return JobWorkCursor.fromJobDefinitionAndRequestedStepId(theJobDefinition, metadata.getTargetStepId());
			});
			counter.getAndIncrement();
			if (!theIsGatedExecutionAdvancementBool
					&& (theJobDefinition.isGatedExecution() || jobWorkCursor.isReductionStep())) {
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
			updateChunkAndSendToQueue(metadata);
		}
		ourLog.debug(
				"Encountered {} READY work chunks for job {}", counter.get(), theJobDefinition.getJobDefinitionId());
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
			JobInstance theInstance,
			JobDefinition<?> theJobDefinition,
			JobWorkCursor<?, ?, ?> theWorkCursor,
			String nextStepId) {
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

		// TODO
		// create a new persistence transition for state advance
		// update stepId to next step AND update all chunks in this step to READY (from GATED or QUEUED ;-P)
		// so we can queue them safely.

		// update the job step so the workers will process them.
		// if it's the last (gated) step, there will be no change - but we should
		// queue up the chunks anyways
		boolean changed = theWorkCursor.isFinalStep()
				|| myJobPersistence.updateInstance(instanceId, instance -> {
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

		ourLog.debug("Moving gated instance {} to next step.", theInstance.getInstanceId());

		// because we now have all gated job chunks in READY state,
		// we can enqueue them
		enqueueReadyChunks(theInstance, theJobDefinition, true);
	}
}
