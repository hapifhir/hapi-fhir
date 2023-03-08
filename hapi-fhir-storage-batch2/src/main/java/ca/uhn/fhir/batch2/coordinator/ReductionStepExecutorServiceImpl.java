package ca.uhn.fhir.batch2.coordinator;

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

import ca.uhn.fhir.batch2.api.ChunkExecutionDetails;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.IReductionStepExecutorService;
import ca.uhn.fhir.batch2.api.IReductionStepWorker;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.model.ChunkOutcome;
import ca.uhn.fhir.batch2.model.JobDefinitionStep;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkCursor;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.IHasScheduledJobs;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.transaction.annotation.Propagation;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class ReductionStepExecutorServiceImpl implements IReductionStepExecutorService, IHasScheduledJobs {
	public static final String SCHEDULED_JOB_ID = ReductionStepExecutorScheduledJob.class.getName();
	private static final Logger ourLog = LoggerFactory.getLogger(ReductionStepExecutorServiceImpl.class);
	private final Map<String, JobWorkCursor> myInstanceIdToJobWorkCursor = Collections.synchronizedMap(new LinkedHashMap<>());
	private final ExecutorService myReducerExecutor;
	private final IJobPersistence myJobPersistence;
	private final IHapiTransactionService myTransactionService;
	private final Semaphore myCurrentlyExecuting = new Semaphore(1);
	private final AtomicReference<String> myCurrentlyFinalizingInstanceId = new AtomicReference<>();
	private Timer myHeartbeatTimer;
	private final JobDefinitionRegistry myJobDefinitionRegistry;


	/**
	 * Constructor
	 */
	public ReductionStepExecutorServiceImpl(IJobPersistence theJobPersistence, IHapiTransactionService theTransactionService, JobDefinitionRegistry theJobDefinitionRegistry) {
		myJobPersistence = theJobPersistence;
		myTransactionService = theTransactionService;
		myJobDefinitionRegistry = theJobDefinitionRegistry;

		myReducerExecutor = Executors.newSingleThreadExecutor(new CustomizableThreadFactory("batch2-reducer"));
	}


	@EventListener(ContextRefreshedEvent.class)
	public void start() {
		myHeartbeatTimer = new Timer("batch2-reducer-heartbeat");
		myHeartbeatTimer.schedule(new HeartbeatTimerTask(), DateUtils.MILLIS_PER_MINUTE, DateUtils.MILLIS_PER_MINUTE);
	}

	private void runHeartbeat() {
		String currentlyFinalizingInstanceId = myCurrentlyFinalizingInstanceId.get();
		if (currentlyFinalizingInstanceId != null) {
			ourLog.info("Running heartbeat for instance: {}", currentlyFinalizingInstanceId);
			executeInTransactionWithSynchronization(()->{
				myJobPersistence.updateInstanceUpdateTime(currentlyFinalizingInstanceId);
				return null;
			});
		}
	}

	@EventListener(ContextClosedEvent.class)
	public void shutdown() {
		myHeartbeatTimer.cancel();
	}


	@Override
	public void triggerReductionStep(String theInstanceId, JobWorkCursor<?, ?, ?> theJobWorkCursor) {
		myInstanceIdToJobWorkCursor.putIfAbsent(theInstanceId, theJobWorkCursor);
		if (myCurrentlyExecuting.availablePermits() > 0) {
			myReducerExecutor.submit(() -> reducerPass());
		}
	}

	@Override
	public void reducerPass() {
		if (myCurrentlyExecuting.tryAcquire()) {
			try {

				String[] instanceIds = myInstanceIdToJobWorkCursor.keySet().toArray(new String[0]);
				if (instanceIds.length > 0) {
					String instanceId = instanceIds[0];
					myCurrentlyFinalizingInstanceId.set(instanceId);
					JobWorkCursor<?, ?, ?> jobWorkCursor = myInstanceIdToJobWorkCursor.get(instanceId);
					executeReductionStep(instanceId, jobWorkCursor);

					// If we get here, this succeeded. Purge the instance from the work queue
					myInstanceIdToJobWorkCursor.remove(instanceId);
				}

			} finally {
				myCurrentlyFinalizingInstanceId.set(null);
				myCurrentlyExecuting.release();
			}
		}
	}

	@VisibleForTesting
	<PT extends IModelJson, IT extends IModelJson, OT extends IModelJson> ReductionStepChunkProcessingResponse executeReductionStep(String theInstanceId, JobWorkCursor<PT, IT, OT> theJobWorkCursor) {

		JobDefinitionStep<PT, IT, OT> step = theJobWorkCursor.getCurrentStep();

		JobInstance instance = executeInTransactionWithSynchronization(() -> {
			JobInstance currentInstance = myJobPersistence.fetchInstance(theInstanceId).orElseThrow(() -> new InternalErrorException("Unknown currentInstance: " + theInstanceId));
			boolean shouldProceed = false;
			switch (currentInstance.getStatus()) {
				case IN_PROGRESS:
				case ERRORED:
					if (myJobPersistence.markInstanceAsStatus(currentInstance.getInstanceId(), StatusEnum.FINALIZE)) {
						ourLog.info("Job instance {} has been set to FINALIZE state - Beginning reducer step", currentInstance.getInstanceId());
						shouldProceed = true;
					}
					break;
				case FINALIZE:
				case COMPLETED:
				case FAILED:
				case QUEUED:
				case CANCELLED:
					break;
			}

			if (!shouldProceed) {
				ourLog.warn(
					"JobInstance[{}] should not be finalized at this time. In memory status is {}. Reduction step will not rerun!"
						+ " This could be a long running reduction job resulting in the processed msg not being acknowledge,"
						+ " or the result of a failed process or server restarting.",
					currentInstance.getInstanceId(),
					currentInstance.getStatus().name()
				);
				return null;
			}

			return currentInstance;
		});
		if (instance == null) {
			return new ReductionStepChunkProcessingResponse(false);
		}

		PT parameters = instance.getParameters(theJobWorkCursor.getJobDefinition().getParametersType());
		IReductionStepWorker<PT, IT, OT> reductionStepWorker = (IReductionStepWorker<PT, IT, OT>) step.getJobStepWorker();

		instance.setStatus(StatusEnum.FINALIZE);

		boolean defaultSuccessValue = true;
		ReductionStepChunkProcessingResponse response = new ReductionStepChunkProcessingResponse(defaultSuccessValue);

		try {
			executeInTransactionWithSynchronization(() -> {
				try (Stream<WorkChunk> chunkIterator = myJobPersistence.fetchAllWorkChunksForStepStream(instance.getInstanceId(), step.getStepId())) {
					chunkIterator.forEach((chunk) -> {
						processChunk(chunk, instance, parameters, reductionStepWorker, response, theJobWorkCursor);
					});
				}
				return null;
			});
		} finally {

			executeInTransactionWithSynchronization(() -> {
				ourLog.info("Reduction step for instance[{}] produced {} successful and {} failed chunks", instance.getInstanceId(), response.getSuccessfulChunkIds().size(), response.getFailedChunksIds().size());

				ReductionStepDataSink<PT, IT, OT> dataSink = new ReductionStepDataSink<>(instance.getInstanceId(), theJobWorkCursor, myJobPersistence, myJobDefinitionRegistry);
				StepExecutionDetails<PT, IT> chunkDetails = new StepExecutionDetails<>(parameters, null, instance, "REDUCTION");

				if (response.isSuccessful()) {
					reductionStepWorker.run(chunkDetails, dataSink);
				}

				if (response.hasSuccessfulChunksIds()) {
					// complete the steps without making a new work chunk
					myJobPersistence.markWorkChunksWithStatusAndWipeData(instance.getInstanceId(),
						response.getSuccessfulChunkIds(),
						StatusEnum.COMPLETED,
						null // error message - none
					);
				}

				if (response.hasFailedChunkIds()) {
					// mark any failed chunks as failed for aborting
					myJobPersistence.markWorkChunksWithStatusAndWipeData(instance.getInstanceId(),
						response.getFailedChunksIds(),
						StatusEnum.FAILED,
						"JOB ABORTED");
				}
				return null;
			});

		}

		// if no successful chunks, return false
		if (!response.hasSuccessfulChunksIds()) {
			response.setSuccessful(false);
		}

		return response;
	}

	private <T> T executeInTransactionWithSynchronization(Callable<T> runnable) {
		return myTransactionService
			.withRequest(null)
			.withPropagation(Propagation.REQUIRES_NEW)
			.execute(runnable);
	}


	@Override
	public void scheduleJobs(ISchedulerService theSchedulerService) {
		theSchedulerService.scheduleClusteredJob(10 * DateUtils.MILLIS_PER_SECOND, buildJobDefinition());
	}

	@Nonnull
	private ScheduledJobDefinition buildJobDefinition() {
		ScheduledJobDefinition jobDefinition = new ScheduledJobDefinition();
		jobDefinition.setId(SCHEDULED_JOB_ID);
		jobDefinition.setJobClass(ReductionStepExecutorScheduledJob.class);
		return jobDefinition;
	}

	private <PT extends IModelJson, IT extends IModelJson, OT extends IModelJson>
	void processChunk(WorkChunk theChunk,
							JobInstance theInstance,
							PT theParameters,
							IReductionStepWorker<PT, IT, OT> theReductionStepWorker,
							ReductionStepChunkProcessingResponse theResponseObject,
							JobWorkCursor<PT, IT, OT> theJobWorkCursor) {

		if (!theChunk.getStatus().isIncomplete()) {
			// This should never happen since jobs with reduction are required to be gated
			ourLog.error("Unexpected chunk {} with status {} found while reducing {}.  No chunks feeding into a reduction step should be complete.", theChunk.getId(), theChunk.getStatus(), theInstance);
			return;
		}

		if (theResponseObject.hasFailedChunkIds()) {
			// we are going to fail all future chunks now
			theResponseObject.addFailedChunkId(theChunk);
		} else {
			try {
				// feed them into our reduction worker
				// this is the most likely area to throw,
				// as this is where db actions and processing is likely to happen
				IT chunkData = theChunk.getData(theJobWorkCursor.getCurrentStep().getInputType());
				ChunkExecutionDetails<PT, IT> chunkDetails = new ChunkExecutionDetails<>(chunkData, theParameters, theInstance.getInstanceId(), theChunk.getId());

				ChunkOutcome outcome = theReductionStepWorker.consume(chunkDetails);

				switch (outcome.getStatus()) {
					case SUCCESS:
						theResponseObject.addSuccessfulChunkId(theChunk);
						break;

					case FAILED:
						ourLog.error("Processing of work chunk {} resulted in aborting job.", theChunk.getId());

						// fail entire job - including all future workchunks
						theResponseObject.addFailedChunkId(theChunk);
						theResponseObject.setSuccessful(false);
						break;
				}
			} catch (Exception e) {
				String msg = String.format(
					"Reduction step failed to execute chunk reduction for chunk %s with exception: %s.",
					theChunk.getId(),
					e.getMessage()
				);
				// we got a failure in a reduction
				ourLog.error(msg, e);
				theResponseObject.setSuccessful(false);

				myJobPersistence.markWorkChunkAsFailed(theChunk.getId(), msg);
			}
		}
	}

	private class HeartbeatTimerTask extends TimerTask {
		@Override
		public void run() {
			runHeartbeat();
		}
	}

	public static class ReductionStepExecutorScheduledJob implements HapiJob {
		@Autowired
		private IReductionStepExecutorService myTarget;

		@Override
		public void execute(JobExecutionContext theContext) {
			myTarget.reducerPass();
		}
	}


}
