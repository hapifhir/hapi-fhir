package ca.uhn.fhir.batch2.coordinator;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.IReductionStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.JobStepFailedException;
import ca.uhn.fhir.batch2.api.ReductionStepExecutionDetails;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.channel.BatchJobSender;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobDefinitionStep;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkCursor;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IModelJson;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class StepExecutionSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(StepExecutionSvc.class);

	private final IJobPersistence myJobPersistence;
	private final BatchJobSender myBatchJobSender;

	public StepExecutionSvc(IJobPersistence thePersistence,
									BatchJobSender theSender) {
		myJobPersistence = thePersistence;
		myBatchJobSender = theSender;
	}

	/**
	 * Execute the work chunk.
	 *
	 * @param theCursor - work cursor
	 * @param theInstance - the job instance
	 * @param theWorkChunk - the work chunk (if available); can be null (for reduction step only!)
	 * @param <PT> - Job parameters Type
	 * @param <IT> - Step input parameters Type
	 * @param <OT> - Step output parameters Type
	 * @return - JobStepExecution output. Contains the datasink and whether or not the execution had succeeded.
	 */
	public <PT extends IModelJson, IT extends IModelJson, OT extends IModelJson> JobStepExecutorOutput<PT, IT, OT>
	doExecution(
		JobWorkCursor<PT, IT, OT> theCursor,
		JobInstance theInstance,
		@Nullable WorkChunk theWorkChunk
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
			boolean success = executeReductionStep(theInstance,
				step,
				inputType,
				parameters,
				dataSink);

			return new JobStepExecutorOutput<>(success, dataSink);
		} else {
			// all other kinds of steps
			Validate.notNull(theWorkChunk);
			stepExecutionDetails = getExecutionDetailsForNonReductionStep(theWorkChunk, instanceId, inputType, parameters);

			// execute the step
			boolean success = executeStep(stepExecutionDetails,
				worker,
				dataSink);

			// return results with data sink
			return new JobStepExecutorOutput<>(success, dataSink);
		}
	}

	/**
	 * Get the correct datasink for the cursor/job provided.
	 */
	@SuppressWarnings("unchecked")
	protected  <PT extends IModelJson, IT extends IModelJson, OT extends IModelJson> BaseDataSink<PT, IT, OT> getDataSink(
		JobWorkCursor<PT, IT, OT> theCursor,
		JobDefinition<PT> theJobDefinition,
		String theInstanceId
	) {
		BaseDataSink<PT, IT, OT> dataSink;
		if (theCursor.isReductionStep()) {
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
	private <PT extends IModelJson, IT extends IModelJson, OT extends IModelJson> boolean executeReductionStep(
		JobInstance theInstance,
		JobDefinitionStep<PT, IT, OT> theStep,
		Class<IT> theInputType,
		PT theParameters,
		BaseDataSink<PT, IT, OT> theDataSink
	) {
		IReductionStepWorker<PT, IT, OT> theWorker = (IReductionStepWorker<PT, IT, OT>) theStep.getJobStepWorker();

		// We fetch all chunks first...
		Iterator<WorkChunk> chunkIterator = myJobPersistence.fetchAllWorkChunksIterator(theInstance.getInstanceId(), true);

		List<String> chunkIds = new ArrayList<>();
		HashSet<String> errors = new HashSet<>();
		while (chunkIterator.hasNext()) {
			WorkChunk chunk = chunkIterator.next();
			if (chunk.getStatus() != StatusEnum.QUEUED) {
				// we are currently fetching all statuses from the db
				// we will ignore non-completed steps.
				// should we throw for errored values we find here?
				continue;
			}

			chunkIds.add(chunk.getId());
			try {
				// feed them into our reduction worker
				// this is the most likely area to throw,
				// as this is where db actions and processing is likely to happen
				theWorker.addChunk(chunk, theInputType);
			} catch (Exception e) {
				// we got a failure in a reduction
				ourLog.error("Reduction step failed to execute chunk reduction for chunk {} with exception {}.",
					chunk.getId(),
					e.getMessage());

				// we add the error
				// and continue to loop (should we just exit now?)
				errors.add(e.getMessage());
			}
		}

		if (errors.isEmpty()) {
			// complete the steps without making a new work chunk
			myJobPersistence.markWorkChunksWithStatusAndWipeData(theInstance.getInstanceId(),
				chunkIds,
				StatusEnum.COMPLETED,
				null // error message (none = clear data)
			);
		} else {
			// we couldn't reduce.... mark all chunks as failed.
			// we'll just concatenate the error msgs (they are likely going to have
			// the same error, but if they don't this isn't harmful)
			String errorMsg = errors.stream().reduce("", (a, b) -> a + ", " + b);
			myJobPersistence.markWorkChunksWithStatusAndWipeData(theInstance.getInstanceId(),
				chunkIds,
				StatusEnum.FAILED,
				errorMsg
			);

			// no need to generate report - the job has failed
			return false;
		}

		// we'll call execute (to reuse existing architecture)
		// the data sink will do the storing to the instance (and not the chunks).
		// it is assumed the OT (report) data is smaller than the list of all IT data
		ReductionStepExecutionDetails<PT, IT, OT> executionDetails = new ReductionStepExecutionDetails<>(
			theParameters,
			null,
			theInstance.getInstanceId()
		);

		return executeStep(executionDetails,
			theWorker,
			theDataSink);
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
			if (theStepExecutionDetails.hasAssociatedWorkChunk()) {
				myJobPersistence.markWorkChunkAsFailed(chunkId, e.toString());
			}
			return false;
		} catch (Exception e) {
			ourLog.error("Failure executing job {} step {}", jobDefinitionId, targetStepId, e);
			if (theStepExecutionDetails.hasAssociatedWorkChunk()) {
				myJobPersistence.markWorkChunkAsErroredAndIncrementErrorCount(chunkId, e.toString());
			}
			throw new JobStepFailedException(Msg.code(2041) + e.getMessage(), e);
		} catch (Throwable t) {
			ourLog.error("Unexpected failure executing job {} step {}", jobDefinitionId, targetStepId, t);
			if (theStepExecutionDetails.hasAssociatedWorkChunk()) {
				myJobPersistence.markWorkChunkAsFailed(chunkId, t.toString());
			}
			return false;
		}

		if (theStepExecutionDetails.hasAssociatedWorkChunk()) {
			int recordsProcessed = outcome.getRecordsProcessed();
			int recoveredErrorCount = theDataSink.getRecoveredErrorCount();

			myJobPersistence.markWorkChunkAsCompletedAndClearData(chunkId, recordsProcessed);
			if (recoveredErrorCount > 0) {
				myJobPersistence.incrementWorkChunkErrorCount(chunkId, recoveredErrorCount);
			}
		}

		return true;
	}
}
