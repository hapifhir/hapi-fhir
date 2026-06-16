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
package ca.uhn.fhir.batch2.coordinator;

import ca.uhn.fhir.batch2.api.ChunkExecutionDetails;
import ca.uhn.fhir.batch2.api.IJobCompletionHandler;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.IJobStepExecutionServices;
import ca.uhn.fhir.batch2.api.IReductionStepExecutorService;
import ca.uhn.fhir.batch2.api.IReductionStepWorker;
import ca.uhn.fhir.batch2.api.JobCompletionDetails;
import ca.uhn.fhir.batch2.api.ReductionStepFailureException;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.model.ChunkOutcome;
import ca.uhn.fhir.batch2.model.JobDefinitionStep;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkCursor;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import ca.uhn.fhir.batch2.progress.JobInstanceStatusUpdater;
import ca.uhn.fhir.batch2.util.BatchJobOpenTelemetryUtils;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.JsonUtil;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.transaction.annotation.Propagation;

import java.util.Date;
import java.util.EnumSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

import static ca.uhn.fhir.batch2.model.StatusEnum.COMPLETED;
import static ca.uhn.fhir.batch2.model.StatusEnum.ERRORED;
import static ca.uhn.fhir.batch2.model.StatusEnum.FINALIZE;
import static ca.uhn.fhir.batch2.model.StatusEnum.IN_PROGRESS;
import static ca.uhn.fhir.batch2.util.BatchJobOpenTelemetryUtils.JOB_STEP_EXECUTION_SPAN_NAME;

public class ReductionStepExecutorServiceImpl implements IReductionStepExecutorService {
	private static final Logger ourLog = LoggerFactory.getLogger(ReductionStepExecutorServiceImpl.class);
	private final ExecutorService myReducerChunkExecutor;
	private final IJobPersistence myJobPersistence;
	private final IHapiTransactionService myTransactionService;
	private final JobDefinitionRegistry myJobDefinitionRegistry;
	private final JobInstanceStatusUpdater myJobInstanceStatusUpdater;
	private final IJobStepExecutionServices myJobStepExecutionServices;
	private final IInterceptorService myInterceptorService;

	/**
	 * Constructor
	 */
	public ReductionStepExecutorServiceImpl(
			IJobPersistence theJobPersistence,
			IHapiTransactionService theTransactionService,
			JobDefinitionRegistry theJobDefinitionRegistry,
			IJobStepExecutionServices theJobStepExecutionServices,
			IInterceptorService theInterceptorService) {
		myJobPersistence = theJobPersistence;
		myTransactionService = theTransactionService;
		myJobDefinitionRegistry = theJobDefinitionRegistry;
		myJobStepExecutionServices = theJobStepExecutionServices;
		myJobInstanceStatusUpdater = new JobInstanceStatusUpdater(theJobDefinitionRegistry, theInterceptorService);
		myInterceptorService = theInterceptorService;

		// This is a single thread executor because there are no guarantees that the chunk
		// processing is actually thread safe. Be careful if you think you want to add more
		// threads here.
		myReducerChunkExecutor =
				Executors.newSingleThreadExecutor(new CustomizableThreadFactory("batch2-reducer-chunk"));
	}

	@Override
	@WithSpan(JOB_STEP_EXECUTION_SPAN_NAME)
	public ReductionStepChunkProcessingResponse triggerReductionStep(
			JobInstance theInstance, JobWorkCursor<?, ?, ?> theJobWorkCursor) {
		return executeReductionStep(theInstance, theJobWorkCursor);
	}

	private <PT extends IModelJson, IT extends IModelJson, OT extends IModelJson>
			ReductionStepChunkProcessingResponse executeReductionStep(
					JobInstance theInstance, JobWorkCursor<PT, IT, OT> theJobWorkCursor) {

		BatchJobOpenTelemetryUtils.addAttributesToCurrentSpan(
				theJobWorkCursor.getJobDefinition().getJobDefinitionId(),
				theJobWorkCursor.getJobDefinition().getJobDefinitionVersion(),
				theInstance.getInstanceId(),
				theJobWorkCursor.getCurrentStepId(),
				null);

		JobDefinitionStep<PT, IT, OT> step = theJobWorkCursor.getCurrentStep();

		boolean shouldProceed = false;
		switch (theInstance.getStatus()) {
			case IN_PROGRESS:
			case ERRORED:
				// this will take a write lock on the JobInstance, preventing duplicates.
				boolean changed = executeInTransactionWithSynchronization(() -> {
					myJobPersistence.updateInstanceUpdateTime(theInstance.getInstanceId());
					return myJobPersistence.markInstanceAsStatusWhenStatusIn(
							theInstance.getInstanceId(), FINALIZE, EnumSet.of(IN_PROGRESS, ERRORED));
				});
				if (changed) {
					ourLog.info(
							"Job instance {} has been set to FINALIZE state - Beginning reducer step",
							theInstance.getInstanceId());
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
							+ " This could be a long running reduction job resulting in the processed msg not being acknowledged,"
							+ " or the result of a failed process or server restarting.",
					theInstance.getInstanceId(),
					theInstance.getStatus());
			return new ReductionStepChunkProcessingResponse(false);
		}

		PT parameters =
				theInstance.getParameters(theJobWorkCursor.getJobDefinition().getParametersType());

		IReductionStepWorker<PT, IT, OT> reductionStepWorker =
				(IReductionStepWorker<PT, IT, OT>) step.getJobStepWorker();

		// Clone the worker so that we start with no built-up state
		reductionStepWorker = reductionStepWorker.newInstance();

		theInstance.setStatus(FINALIZE);

		boolean defaultSuccessValue = true;
		ReductionStepChunkProcessingResponse response = new ReductionStepChunkProcessingResponse(defaultSuccessValue);

		try {
			processChunksAndCompleteJob(theJobWorkCursor, step, theInstance, parameters, reductionStepWorker, response);
		} catch (Exception ex) {
			ourLog.error("Job completion failed for Job {}", theInstance.getInstanceId(), ex);

			executeInTransactionWithSynchronization(() -> {
				myJobPersistence.updateInstance(theInstance.getInstanceId(), instance -> {
					instance.setEndTime(new Date());
					myJobInstanceStatusUpdater.updateInstanceStatus(instance, StatusEnum.FAILED);
					return true;
				});
				return null;
			});
			response.setSuccessful(false);
		}

		// if no successful chunks, return false
		if (!response.hasSuccessfulChunksIds()) {
			response.setSuccessful(false);
		}

		return response;
	}

	private <PT extends IModelJson, IT extends IModelJson, OT extends IModelJson> void processChunksAndCompleteJob(
			JobWorkCursor<PT, IT, OT> theJobWorkCursor,
			JobDefinitionStep<PT, IT, OT> step,
			JobInstance instance,
			PT parameters,
			IReductionStepWorker<PT, IT, OT> reductionStepWorker,
			ReductionStepChunkProcessingResponse response) {
		try {
			executeInTransactionWithSynchronization(() -> {
				try (Stream<WorkChunk> chunkIterator =
						myJobPersistence.fetchAllWorkChunksForStepStream(instance.getInstanceId(), step.getStepId())) {
					chunkIterator.forEach(chunk -> executeInNoTransaction(() -> processChunk(
							chunk, instance, parameters, reductionStepWorker, response, theJobWorkCursor)));
				}
				return null;
			});
		} finally {
			executeInTransactionWithSynchronization(() -> {
				ourLog.info(
						"Reduction step for instance[{}] produced {} successful and {} failed chunks",
						instance.getInstanceId(),
						response.getSuccessfulChunkIds().size(),
						response.getFailedChunksIds().size());

				ReductionStepDataSink<PT, IT, OT> dataSink = new ReductionStepDataSink<>(
						instance.getInstanceId(),
						theJobWorkCursor,
						myJobPersistence,
						myJobDefinitionRegistry,
						myInterceptorService);
				StepExecutionDetails<PT, IT> chunkDetails = StepExecutionDetails.createReductionStepDetails(
						theJobWorkCursor.getJobDefinition(),
						theJobWorkCursor.getCurrentStepId(),
						parameters,
						null,
						instance,
						myJobStepExecutionServices);

				if (response.isSuccessful()) {
					try {
						executeInNoTransaction(() -> reductionStepWorker.run(chunkDetails, dataSink));
						// the ReductionStepDataSink will update the job status to COMPLETED
						// we should update instance here to keep it consistent with the newest version in persistence
						instance.setStatus(COMPLETED);
					} catch (ReductionStepFailureException e) {

						myJobPersistence.updateInstance(instance.getInstanceId(), i -> {
							i.setErrorMessage(e.getMessage());
							i.setEndTime(new Date());
							i.setStatus(StatusEnum.FAILED);

							if (e.getReportMsg() != null) {
								i.setReport(JsonUtil.serialize(e.getReportMsg()));
							}

							return true;
						});
					}
				}

				if (response.hasSuccessfulChunksIds()) {
					// complete the steps without making a new work chunk
					myJobPersistence.markWorkChunksWithStatusAndWipeData(
							instance.getInstanceId(),
							response.getSuccessfulChunkIds(),
							WorkChunkStatusEnum.COMPLETED,
							null // error message - none
							);
				}

				if (response.hasFailedChunkIds()) {
					// mark any failed chunks as failed for aborting
					myJobPersistence.markWorkChunksWithStatusAndWipeData(
							instance.getInstanceId(),
							response.getFailedChunksIds(),
							WorkChunkStatusEnum.FAILED,
							"JOB ABORTED");
				}

				if (response.isSuccessful()) {
					/*
					 * All reduction steps are final steps.
					 */
					IJobCompletionHandler<PT> completionHandler =
							theJobWorkCursor.getJobDefinition().getCompletionHandler();
					JobCompletionDetails<PT> jcd = new JobCompletionDetails<>(parameters, instance);
					if (completionHandler != null) {
						completionHandler.jobComplete(jcd);
					}
				}

				return null;
			});
		}
	}

	/**
	 * We need to keep a long transaction here to keep our ResultSet Stream open for the duration.
	 * But we don't want to expose this transaction to the user.  We use a new thread here so that
	 * the individual chunks get processed in their own dedicated transactions separate from the
	 * main chunk-loading transaction.
	 */
	private void executeInNoTransaction(Runnable theRunnable) {
		Future<?> future = myReducerChunkExecutor.submit(theRunnable);
		try {
			future.get();
		} catch (Exception e) {
			if (e.getCause() instanceof RuntimeException re) {
				throw re;
			}
			throw new InternalErrorException(Msg.code(2721) + e.getMessage(), e);
		}
	}

	private <T> T executeInTransactionWithSynchronization(Callable<T> runnable) {
		return myTransactionService
				.withRequest(null)
				.withPropagation(Propagation.REQUIRES_NEW)
				.execute(runnable);
	}

	private <PT extends IModelJson, IT extends IModelJson, OT extends IModelJson> void processChunk(
			WorkChunk theChunk,
			JobInstance theInstance,
			PT theParameters,
			IReductionStepWorker<PT, IT, OT> theReductionStepWorker,
			ReductionStepChunkProcessingResponse theResponseObject,
			JobWorkCursor<PT, IT, OT> theJobWorkCursor) {

		/*
		 * Reduction steps are done inline and only on gated jobs.
		 */
		if (theChunk.getStatus() == WorkChunkStatusEnum.COMPLETED) {
			// This should never happen since jobs with reduction are required to be gated
			ourLog.error(
					"Unexpected chunk {} with status {} found while reducing {}.  No chunks feeding into a reduction step should be in a state other than READY.",
					theChunk.getId(),
					theChunk.getStatus(),
					theInstance);
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
				IT chunkData =
						theChunk.getData(theJobWorkCursor.getCurrentStep().getInputType());
				ChunkExecutionDetails<PT, IT> chunkDetails = new ChunkExecutionDetails<>(
						chunkData, theParameters, theInstance.getInstanceId(), theChunk.getId());

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
						theChunk.getId(), e.getMessage());
				// we got a failure in a reduction
				ourLog.error(msg, e);
				theResponseObject.setSuccessful(false);

				myJobPersistence.onWorkChunkFailed(theChunk.getId(), msg);
			}
		}
	}
}
