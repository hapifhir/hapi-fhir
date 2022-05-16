package ca.uhn.fhir.batch2.impl;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.JobStepFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobDefinitionStep;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkCursor;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IModelJson;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

public class JobStepExecutor<PT extends IModelJson, IT extends IModelJson, OT extends IModelJson> {
	private static final Logger ourLog = LoggerFactory.getLogger(JobStepExecutor.class);

	private final IJobPersistence myJobPersistence;
	private final BatchJobSender myBatchJobSender;

	private final JobDefinition<PT> myDefinition;
	private final JobInstance myInstance;
	private final WorkChunk myWorkChunk;
	private final JobWorkCursor<PT, IT, OT> myCursor;

	JobStepExecutor(@Nonnull IJobPersistence theJobPersistence, @Nonnull BatchJobSender theBatchJobSender, @Nonnull JobInstance theInstance, @Nonnull WorkChunk theWorkChunk, @Nonnull JobWorkCursor<PT, IT, OT> theCursor) {
		myJobPersistence = theJobPersistence;
		myBatchJobSender = theBatchJobSender;
		myDefinition = theCursor.jobDefinition;
		myInstance = theInstance;
		myWorkChunk = theWorkChunk;
		myCursor = theCursor;
	}

	@SuppressWarnings("unchecked")
	void executeStep() {
		String instanceId = myInstance.getInstanceId();

		BaseDataSink<PT,IT,OT> dataSink;
		if (myCursor.isFinalStep()) {
			dataSink = (BaseDataSink<PT, IT, OT>) new FinalStepDataSink<>(myDefinition.getJobDefinitionId(), instanceId, myCursor.asFinalCursor());
		} else {
			dataSink = new JobDataSink<>(myBatchJobSender, myJobPersistence, myDefinition, instanceId, myCursor);
		}

		boolean success = executeStep(myDefinition.getJobDefinitionId(), myWorkChunk, myInstance.getParameters(myDefinition.getParametersType()), dataSink);

		if (!success) {
			return;
		}

		if (dataSink.firstStepProducedNothing()) {
			ourLog.info("First step of job myInstance {} produced no work chunks, marking as completed", instanceId);
			myJobPersistence.markInstanceAsCompleted(instanceId);
		}

		if (myDefinition.isGatedExecution() && myCursor.isFirstStep) {
			myInstance.setCurrentGatedStepId(myCursor.getTargetStepId());
			myJobPersistence.updateInstance(myInstance);
		}

	}

	private boolean executeStep(String theJobDefinitionId, @Nonnull WorkChunk theWorkChunk, PT theParameters, BaseDataSink<PT,IT,OT> theDataSink) {
		JobDefinitionStep<PT, IT, OT> theTargetStep = theDataSink.getTargetStep();
		String targetStepId = theTargetStep.getStepId();
		Class<IT> inputType = theTargetStep.getInputType();
		IJobStepWorker<PT, IT, OT> worker = theTargetStep.getJobStepWorker();

		IT inputData = null;
		if (!inputType.equals(VoidModel.class)) {
			inputData = theWorkChunk.getData(inputType);
		}

		String instanceId = theWorkChunk.getInstanceId();
		String chunkId = theWorkChunk.getId();

		StepExecutionDetails<PT, IT> stepExecutionDetails = new StepExecutionDetails<>(theParameters, inputData, instanceId, chunkId);
		RunOutcome outcome;
		try {
			outcome = worker.run(stepExecutionDetails, theDataSink);
			Validate.notNull(outcome, "Step theWorker returned null: %s", worker.getClass());
		} catch (JobExecutionFailedException e) {
			ourLog.error("Unrecoverable failure executing job {} step {}", theJobDefinitionId, targetStepId, e);
			myJobPersistence.markWorkChunkAsFailed(chunkId, e.toString());
			return false;
		} catch (Exception e) {
			ourLog.error("Failure executing job {} step {}", theJobDefinitionId, targetStepId, e);
			myJobPersistence.markWorkChunkAsErroredAndIncrementErrorCount(chunkId, e.toString());
			throw new JobStepFailedException(Msg.code(2041) + e.getMessage(), e);
			// FIXME KHS integration test to confirm that cancelling the job will kill a poisoned head
		} catch (Throwable t) {
			ourLog.error("Unexpected failure executing job {} step {}", theJobDefinitionId, targetStepId, t);
			myJobPersistence.markWorkChunkAsFailed(chunkId, t.toString());
			return false;
		}

		int recordsProcessed = outcome.getRecordsProcessed();
		myJobPersistence.markWorkChunkAsCompletedAndClearData(chunkId, recordsProcessed);

		int recoveredErrorCount = theDataSink.getRecoveredErrorCount();
		if (recoveredErrorCount > 0) {
			myJobPersistence.incrementWorkChunkErrorCount(chunkId, recoveredErrorCount);
		}

		return true;
	}
}
