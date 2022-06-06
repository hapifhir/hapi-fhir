package ca.uhn.fhir.batch2.coordinator;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.JobStepFailedException;
import ca.uhn.fhir.batch2.api.ReductionStepExecutionDetails;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.channel.BatchJobSender;
import ca.uhn.fhir.batch2.maintenance.JobChunkProgressAccumulator;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobDefinitionStep;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkCursor;
import ca.uhn.fhir.batch2.model.ListResult;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.progress.JobInstanceProgressCalculator;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.util.JsonUtil;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public class JobStepExecutorSvc {


	private static final Logger ourLog = LoggerFactory.getLogger(JobStepExecutorSvc.class);

	private final IJobPersistence myJobPersistence;
	private final BatchJobSender myBatchJobSender;

	public JobStepExecutorSvc(IJobPersistence thePersistence,
									  BatchJobSender theSender) {
		myJobPersistence = thePersistence;
		myBatchJobSender = theSender;
	}

	public <PT extends IModelJson, IT extends IModelJson, OT extends IModelJson> JobStepExecutorOutput<PT, IT, OT>
	doExecution(
		JobWorkCursor<PT, IT, OT> theCursor,
		JobInstance theInstance,
		@Nullable WorkChunk theWorkChunk,
		JobChunkProgressAccumulator theAccumulator
	) {
		JobDefinitionStep<PT, IT, OT> step = theCursor.getCurrentStep();
		JobDefinition<PT> jobDefinition = theCursor.getJobDefinition();
		String instanceId = theInstance.getInstanceId();
		Class<OT> outputType = step.getOutputType();
		Class<IT> inputType = step.getInputType();

		PT parameters = theInstance.getParameters(jobDefinition.getParametersType());
		IJobStepWorker<PT, IT, OT> worker = step.getJobStepWorker();

		StepExecutionDetails<PT, IT> stepExecutionDetails;
		BaseDataSink<PT, IT, OT> dataSink;
		if (step.isReductionStep()) {
			/*
			 * Combine the outputs of all previous steps into
			 * a list as input for the final step
			 */
			JobInstanceProgressCalculator calculator = new JobInstanceProgressCalculator(
				myJobPersistence,
				theInstance,
				theAccumulator
			);
			calculator.calculateAndStoreInstanceProgress();
			List<String> chunksForNextStep = theAccumulator.getChunkIdsWithStatus(instanceId,
				step.getStepId(),
				EnumSet.of(StatusEnum.QUEUED, StatusEnum.IN_PROGRESS));

			List<IT> data = new ArrayList<>();
			List<String> chunkIds = new ArrayList<>();
			for (String nextChunk : chunksForNextStep) {
				Optional<WorkChunk> chunkOp = myJobPersistence.fetchWorkChunkSetStartTimeAndMarkInProgress(nextChunk);

				if (chunkOp.isPresent()) {
					WorkChunk chunk = chunkOp.get();

					data.add(chunk.getData(inputType));
					chunkIds.add(chunk.getId());
				}
			}

			// this is the data we'll store (which is a list of all the previous outputs)
			ListResult<IT> listResult = new ListResult<>(data);

			// reduce multiple work chunks -> single work chunk (for simpler processing)
			BatchWorkChunk newWorkChunk = new BatchWorkChunk(
				theInstance.getJobDefinitionId(),
				theInstance.getJobDefinitionVersion(),
				step.getStepId(),
				instanceId,
				0, // what is sequence? Does it matter here?
				JsonUtil.serialize(listResult)
			);
			String newChunkId = myJobPersistence.reduceWorkChunksToSingleChunk(instanceId, chunkIds, newWorkChunk);

			// create the details, worker, and datasink
			ReductionStepExecutionDetails<PT, IT, OT> executionDetails = new ReductionStepExecutionDetails<>(
				parameters,
				listResult,
				instanceId,
				newChunkId
			);
			stepExecutionDetails = (StepExecutionDetails<PT, IT>) executionDetails;

			dataSink = new ReductionStepDataSink<PT, IT, OT>(
				instanceId,
				theCursor,
				jobDefinition,
				myJobPersistence
			);
		} else {
			// all other kinds of steps
			Validate.notNull(theWorkChunk);

			if (theCursor.isFinalStep()) {
				dataSink = (BaseDataSink<PT, IT, OT>) new FinalStepDataSink<>(jobDefinition.getJobDefinitionId(), instanceId, theCursor.asFinalCursor());
			} else {
				dataSink = new JobDataSink<>(myBatchJobSender, myJobPersistence, jobDefinition, instanceId, theCursor);
			}

			JobDefinitionStep<PT, IT, OT> theTargetStep = dataSink.getTargetStep();

			//Class<IT> inputType = theTargetStep.getInputType();

			IT inputData = null;
			if (!inputType.equals(VoidModel.class)) {
				inputData = theWorkChunk.getData(inputType);
			}

			String chunkId = theWorkChunk.getId();

			stepExecutionDetails = new StepExecutionDetails<>(
				parameters,
				inputData,
				instanceId,
				chunkId
			);
		}

		// execute the step
		boolean succeed = executeStep(stepExecutionDetails,
			worker,
			dataSink);

		return new JobStepExecutorOutput<>(succeed, dataSink);
	}

	/**
	 * Calls the worker execution step, and performs error handling logic for jobs that failed.
	 *
	 * @param theStepExecutionDetails - the execution step details
	 * @param theStepWorker - the worker
	 * @param theDataSink - the data sink
	 * @param <PT> - job parameters
	 * @param <IT> - step input parameters
	 * @param <OT> - expected step output parameters (VoidModel for all but Reduction steps)
	 * @return - true if the job completed successfully, false otherwise.
	 */
	private <PT extends IModelJson, IT extends IModelJson, OT extends IModelJson> boolean executeStep(
		StepExecutionDetails<PT, IT> theStepExecutionDetails,
		IJobStepWorker<PT, IT, OT> theStepWorker,
		BaseDataSink<PT, IT, OT> theDataSink
	) {
		String jobDefinitionId = theDataSink.getJobDefinitionId();
		String targetStepId = theDataSink.getTargetStep().getStepId();
		String chunkId = theStepExecutionDetails.getChunkId();

		RunOutcome outcome;
		try {
			outcome = theStepWorker.run(theStepExecutionDetails, theDataSink);
			Validate.notNull(outcome, "Step theWorker returned null: %s", theStepWorker.getClass());
		} catch (JobExecutionFailedException e) {
			ourLog.error("Unrecoverable failure executing job {} step {}",
				jobDefinitionId,
				targetStepId,
				e);
			myJobPersistence.markWorkChunkAsFailed(chunkId, e.toString());
			return false;
		} catch (Exception e) {
			ourLog.error("Failure executing job {} step {}", jobDefinitionId, targetStepId, e);
			myJobPersistence.markWorkChunkAsErroredAndIncrementErrorCount(chunkId, e.toString());
			throw new JobStepFailedException(Msg.code(2041) + e.getMessage(), e);
		} catch (Throwable t) {
			ourLog.error("Unexpected failure executing job {} step {}", jobDefinitionId, targetStepId, t);
			myJobPersistence.markWorkChunkAsFailed(chunkId, t.toString());
			return false;
		}

		int recordsProcessed = outcome.getRecordsProcessed();
		int recoveredErrorCount = theDataSink.getRecoveredErrorCount();

		myJobPersistence.markWorkChunkAsCompletedAndClearData(chunkId, recordsProcessed);
		if (recoveredErrorCount > 0) {
			myJobPersistence.incrementWorkChunkErrorCount(chunkId, recoveredErrorCount);
		}

		return true;
	}
}
