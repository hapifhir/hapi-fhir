package ca.uhn.fhir.batch2.coordinator;

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
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.transaction.annotation.Propagation;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReductionStepExecutorServiceImpl implements IReductionStepExecutorService, IHasScheduledJobs {
	public static final String SCHEDULED_JOB_ID = ReductionStepExecutorScheduledJob.class.getName();
	private static final Logger ourLog = LoggerFactory.getLogger(ReductionStepExecutorServiceImpl.class);
	private final Map<String, JobWorkCursor> myInstanceIdToJobWorkCursor = Collections.synchronizedMap(new LinkedHashMap<>());
	private final ExecutorService myReducerExecutor;
	private final IJobPersistence myJobPersistence;
	private final IHapiTransactionService myTransactionService;
	private final Semaphore myCurrentlyExecuting = new Semaphore(1);


	/**
	 * Constructor
	 */
	public ReductionStepExecutorServiceImpl(IJobPersistence theJobPersistence, IHapiTransactionService theTransactionService) {
		myJobPersistence = theJobPersistence;
		myTransactionService = theTransactionService;

		myReducerExecutor = Executors.newSingleThreadExecutor(new CustomizableThreadFactory("batch2-reducer"));
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
					JobWorkCursor<?, ?, ?> jobWorkCursor = myInstanceIdToJobWorkCursor.get(instanceId);
					executeReductionStep(instanceId, jobWorkCursor);

					// If we get here, this succeeded. Purge the instance from the work queue
					myInstanceIdToJobWorkCursor.remove(instanceId);
				}

			} finally {
				myCurrentlyExecuting.release();
			}
		}
	}

	private <PT extends IModelJson, IT extends IModelJson, OT extends IModelJson> void executeReductionStep(String theInstanceId, JobWorkCursor<PT, IT, OT> theJobWorkCursor) {
		Optional<JobInstance> instanceOpt = myJobPersistence.fetchInstance(theInstanceId);
		if (instanceOpt.isEmpty()) {
			return;
		}
		JobInstance instance = instanceOpt.get();

		JobDefinitionStep<PT, IT, OT> step = theJobWorkCursor.getCurrentStep();
		PT parameters = instance.getParameters(theJobWorkCursor.getJobDefinition().getParametersType());
		IReductionStepWorker<PT, IT, OT> reductionStepWorker = (IReductionStepWorker<PT, IT, OT>) step.getJobStepWorker();

		Boolean markedForFinalize = myTransactionService
			.withRequest(null)
			.withPropagation(Propagation.REQUIRES_NEW)
			.execute(() -> {
				JobInstance currentInstance = myJobPersistence.fetchInstance(instance.getInstanceId()).orElseThrow(() -> new InternalErrorException("Unknown instance: " + instance.getInstanceId()));
				boolean shouldProceed = false;
				switch (currentInstance.getStatus()) {
					case IN_PROGRESS:
					case ERRORED:
						if (myJobPersistence.markInstanceAsStatus(instance.getInstanceId(), StatusEnum.FINALIZE)) {
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
						instance.getInstanceId(),
						instance.getStatus().name()
					);
				}

				return shouldProceed;
			});
		if (markedForFinalize != Boolean.TRUE) {
			return;
		}

		instance.setStatus(StatusEnum.FINALIZE);

		boolean defaultSuccessValue = true;
		ReductionStepChunkProcessingResponse response = new ReductionStepChunkProcessingResponse(defaultSuccessValue);

		try {
			myTransactionService
				.withRequest(null)
				.withPropagation(Propagation.REQUIRES_NEW)
				.execute(() -> {
					try (Stream<WorkChunk> chunkIterator2 = myJobPersistence.fetchAllWorkChunksForStepStream(instance.getInstanceId(), step.getStepId())) {
						chunkIterator2.forEach((chunk) -> {
							processChunk(chunk, instance, theJobWorkCursor.getCurrentStep().getInputType(), parameters, reductionStepWorker, response, theJobWorkCursor);
						});
					}
				});
		} finally {

			myTransactionService
				.withRequest(null)
				.withPropagation(Propagation.REQUIRES_NEW)
				.execute(() -> {
					ourLog.info("Reduction step for instance[{}] produced {} successful and {} failed chunks", instance.getInstanceId(), response.getSuccessfulChunkIds().size(), response.getFailedChunksIds().size());

					ReductionStepDataSink<PT, IT, OT> dataSink = new ReductionStepDataSink<>(instance.getInstanceId(), theJobWorkCursor, myJobPersistence);
					StepExecutionDetails<PT, IT> chunkDetails = new StepExecutionDetails<>(parameters, null, instance, "REDUCTION");
					reductionStepWorker.run(chunkDetails, dataSink);

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
				});

		}

		// if no successful chunks, return false
		if (!response.hasSuccessfulChunksIds()) {
			response.setSuccessful(false);
		}

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
							Class<IT> theInputType_,
							PT theParameters,
							IReductionStepWorker<PT, IT, OT> theReductionStepWorker,
							ReductionStepChunkProcessingResponse theResponseObject,
							JobWorkCursor<PT,IT,OT> theJobWorkCursor) {

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

					case ABORT:
						ourLog.error("Processing of work chunk {} resulted in aborting job.", theChunk.getId());

						// fail entire job - including all future workchunks
						theResponseObject.addFailedChunkId(theChunk);
						theResponseObject.setSuccessful(false);
						break;

					case FAIL:
						// non-idempotent; but failed chunks will be
						// ignored on a second runthrough of reduction step
						myJobPersistence.markWorkChunkAsFailed(theChunk.getId(),
							"Step worker failed to process work chunk " + theChunk.getId());
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

	public static class ReductionStepExecutorScheduledJob implements HapiJob {
		@Autowired
		private IReductionStepExecutorService myTarget;

		@Override
		public void execute(JobExecutionContext theContext) {
			myTarget.reducerPass();
		}
	}


}
