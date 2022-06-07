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

	/**
	 * Execute the work chunk
	 * @param theCursor - work cursor
	 * @param theInstance - the job instance
	 * @param theWorkChunk - the work chunk (if available); can be null (for reduction steps)
	 * @param theAccumulator - the job accumulator
	 * @param <PT> - Job parameters Type
	 * @param <IT> - Step input parameters Type
	 * @param <OT> - Step output parameters Type
	 * @return - JobStepExecution output. Contains the datasink and whether or not the execution had succeeded.
	 */
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
		Class<IT> inputType = step.getInputType();
		PT parameters = theInstance.getParameters(jobDefinition.getParametersType());

		IJobStepWorker<PT, IT, OT> worker = step.getJobStepWorker();
		BaseDataSink<PT, IT, OT> dataSink = getDataSink(theCursor, jobDefinition, instanceId);

		StepExecutionDetails<PT, IT> stepExecutionDetails;
		if (step.isReductionStep()) {
			// reduction step details
			stepExecutionDetails = getExecutionDetailsForReductionStep(theInstance, theAccumulator, step, inputType, parameters);
		} else {
			// all other kinds of steps
			Validate.notNull(theWorkChunk);
			stepExecutionDetails = getExecutionDetailsForNonReductionStep(theWorkChunk, instanceId, inputType, parameters);
		}

		// execute the step
		boolean succeed = executeStep(stepExecutionDetails,
			worker,
			dataSink);

		return new JobStepExecutorOutput<>(succeed, dataSink);
	}

	protected  <PT extends IModelJson, IT extends IModelJson, OT extends IModelJson> BaseDataSink<PT, IT, OT> getDataSink(
		JobWorkCursor<PT, IT, OT> theCursor,
		JobDefinition<PT> theJobDefinition,
		String theInstanceId
	) {
		BaseDataSink<PT, IT, OT> dataSink;
		if (theCursor.getCurrentStep().isReductionStep()) {
			dataSink = new ReductionStepDataSink<>(
				theInstanceId,
				theCursor,
				theJobDefinition,
				myJobPersistence
			);
		}
		else if (theCursor.isFinalStep()) {
			dataSink = (BaseDataSink<PT, IT, OT>) new FinalStepDataSink<>(theJobDefinition.getJobDefinitionId(), theInstanceId, theCursor.asFinalCursor());
		} else {
			dataSink = new JobDataSink<>(myBatchJobSender, myJobPersistence, theJobDefinition, theInstanceId, theCursor);
		}
		return dataSink;
	}

	/**
	 * Construct execution details for non-reduction step
	 */
	private <PT extends IModelJson, IT extends IModelJson> StepExecutionDetails<PT, IT> getExecutionDetailsForNonReductionStep(
		WorkChunk theWorkChunk,
		String theInstanceId,
		Class<IT> theInputType,
		PT theParameters
	) {
		StepExecutionDetails<PT, IT> stepExecutionDetails;
		IT inputData = null;
		if (!theInputType.equals(VoidModel.class)) {
			inputData = theWorkChunk.getData(theInputType);
		}

		String chunkId = theWorkChunk.getId();

		stepExecutionDetails = new StepExecutionDetails<>(
			theParameters,
			inputData,
			theInstanceId,
			chunkId
		);
		return stepExecutionDetails;
	}

	/**
	 * Do work and construct execution details for job reduction step
	 */
	private <PT extends IModelJson, IT extends IModelJson, OT extends IModelJson> StepExecutionDetails<PT, IT> getExecutionDetailsForReductionStep(
		JobInstance theInstance,
		JobChunkProgressAccumulator theAccumulator,
		JobDefinitionStep<PT, IT, OT> theStep,
		Class<IT> theInputType,
		PT theParameters
	) {
		StepExecutionDetails<PT, IT> stepExecutionDetails;
		List<String> chunksForNextStep = getChunkIdsForNextStep(theInstance, theAccumulator, theStep);

		/*
		 * Combine the outputs of all previous steps into
		 * a list as input for the final step
		 */
		List<IT> data = new ArrayList<>();
		List<String> chunkIds = new ArrayList<>();
		for (String nextChunk : chunksForNextStep) {
			Optional<WorkChunk> chunkOp = myJobPersistence.fetchWorkChunkSetStartTimeAndMarkInProgress(nextChunk);

			if (chunkOp.isPresent()) {
				WorkChunk chunk = chunkOp.get();

				data.add(chunk.getData(theInputType));
				chunkIds.add(chunk.getId());
			}
		}

		// this is the data we'll store (which is a list of all the previous outputs)
		ListResult<IT> listResult = new ListResult<>(data);

		// reduce multiple work chunks -> single work chunk (for simpler processing)
		BatchWorkChunk newWorkChunk = new BatchWorkChunk(
			theInstance.getJobDefinitionId(),
			theInstance.getJobDefinitionVersion(),
			theStep.getStepId(),
			theInstance.getInstanceId(),
			0, // what is sequence? Does it matter in reduction?
			JsonUtil.serialize(listResult)
		);
		String newChunkId = myJobPersistence.reduceWorkChunksToSingleChunk(theInstance.getInstanceId(), chunkIds, newWorkChunk);

		// create the details, and datasink
		ReductionStepExecutionDetails<PT, IT, OT> executionDetails = new ReductionStepExecutionDetails<>(
			theParameters,
			listResult,
			theInstance.getInstanceId(),
			newChunkId
		);
		stepExecutionDetails = (StepExecutionDetails<PT, IT>) executionDetails;
		return stepExecutionDetails;
	}

	private  <PT extends IModelJson, IT extends IModelJson, OT extends IModelJson> List<String> getChunkIdsForNextStep(
		JobInstance theInstance,
		JobChunkProgressAccumulator theAccumulator,
		JobDefinitionStep<PT, IT, OT> theStep
	) {
		JobInstanceProgressCalculator calculator = getProgressCalculator(theInstance, theAccumulator);
		calculator.calculateAndStoreInstanceProgress();
		List<String> chunksForNextStep = theAccumulator.getChunkIdsWithStatus(theInstance.getInstanceId(),
			theStep.getStepId(),
			EnumSet.of(StatusEnum.QUEUED));
		return chunksForNextStep;
	}

	protected JobInstanceProgressCalculator getProgressCalculator(JobInstance theInstance, JobChunkProgressAccumulator theAccumulator) {
		return new JobInstanceProgressCalculator(
			myJobPersistence,
			theInstance,
			theAccumulator
		);
	}

	/**
	 * Calls the worker execution step, and performs error handling logic for jobs that failed.
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
