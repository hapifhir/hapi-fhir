package ca.uhn.fhir.batch2.impl;


import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobDefinitionStep;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkNotification;
import ca.uhn.fhir.batch2.model.JobWorkNotificationJsonMessage;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import javax.annotation.Nonnull;
import java.util.Optional;

class WorkChannelMessageHandler implements MessageHandler {
	private static final Logger ourLog = LoggerFactory.getLogger(WorkChannelMessageHandler.class);
	private final IJobPersistence myJobPersistence;
	private final JobDefinitionRegistry myJobDefinitionRegistry;
	private final BatchJobSender myBatchJobSender;

	WorkChannelMessageHandler(@Nonnull IJobPersistence theJobPersistence, @Nonnull JobDefinitionRegistry theJobDefinitionRegistry, @Nonnull BatchJobSender theBatchJobSender) {
		myJobPersistence = theJobPersistence;
		myJobDefinitionRegistry = theJobDefinitionRegistry;
		myBatchJobSender = theBatchJobSender;
	}

	@Override
	public void handleMessage(@Nonnull Message<?> theMessage) throws MessagingException {
		handleWorkChannelMessage((JobWorkNotificationJsonMessage) theMessage);
	}

	private void handleWorkChannelMessage(JobWorkNotificationJsonMessage theMessage) {
		JobWorkNotification payload = theMessage.getPayload();

		String chunkId = payload.getChunkId();
		Validate.notNull(chunkId);
		Optional<WorkChunk> chunkOpt = myJobPersistence.fetchWorkChunkSetStartTimeAndMarkInProgress(chunkId);
		if (chunkOpt.isEmpty()) {
			ourLog.error("Unable to find chunk with ID {} - Aborting", chunkId);
			return;
		}
		WorkChunk chunk = chunkOpt.get();

		String jobDefinitionId = payload.getJobDefinitionId();
		int jobDefinitionVersion = payload.getJobDefinitionVersion();
		JobDefinition definition = myJobDefinitionRegistry.getDefinitionOrThrowException(jobDefinitionId, jobDefinitionVersion);

		JobDefinitionStep targetStep = null;
		JobDefinitionStep nextStep = null;
		String targetStepId = payload.getTargetStepId();
		boolean firstStep = false;
		for (int i = 0; i < definition.getSteps().size(); i++) {
			JobDefinitionStep<?, ?, ?> step = (JobDefinitionStep<?, ?, ?>) definition.getSteps().get(i);
			if (step.getStepId().equals(targetStepId)) {
				targetStep = step;
				if (i == 0) {
					firstStep = true;
				}
				if (i < (definition.getSteps().size() - 1)) {
					nextStep = (JobDefinitionStep<?, ?, ?>) definition.getSteps().get(i + 1);
				}
				break;
			}
		}

		if (targetStep == null) {
			String msg = "Unknown step[" + targetStepId + "] for job definition ID[" + jobDefinitionId + "] version[" + jobDefinitionVersion + "]";
			ourLog.warn(msg);
			throw new InternalErrorException(Msg.code(2042) + msg);
		}

		Validate.isTrue(chunk.getTargetStepId().equals(targetStep.getStepId()), "Chunk %s has target step %s but expected %s", chunkId, chunk.getTargetStepId(), targetStep.getStepId());

		Optional<JobInstance> instanceOpt = myJobPersistence.fetchInstanceAndMarkInProgress(payload.getInstanceId());
		JobInstance instance = instanceOpt.orElseThrow(() -> new InternalErrorException("Unknown instance: " + payload.getInstanceId()));
		String instanceId = instance.getInstanceId();

		if (instance.isCancelled()) {
			ourLog.info("Skipping chunk {} because job instance is cancelled", chunkId);
			myJobPersistence.markInstanceAsCompleted(instanceId);
			return;
		}

		executeStep(chunk, jobDefinitionId, jobDefinitionVersion, definition, targetStep, nextStep, targetStepId, firstStep, instance);
	}


	@SuppressWarnings("unchecked")
	private <PT extends IModelJson, IT extends IModelJson, OT extends IModelJson> void executeStep(WorkChunk theWorkChunk, String theJobDefinitionId, int theJobDefinitionVersion, JobDefinition<PT> theDefinition, JobDefinitionStep<PT, IT, OT> theStep, JobDefinitionStep<PT, OT, ?> theSubsequentStep, String theTargetStepId, boolean theFirstStep, JobInstance theInstance) {
		String instanceId = theInstance.getInstanceId();
		PT parameters = theInstance.getParameters(theDefinition.getParametersType());
		IJobStepWorker<PT, IT, OT> worker = theStep.getJobStepWorker();

		BaseDataSink<OT> dataSink;
		boolean finalStep = theSubsequentStep == null;
		if (!finalStep) {
			dataSink = new JobDataSink<>(myBatchJobSender, myJobPersistence, theJobDefinitionId, theJobDefinitionVersion, theSubsequentStep, instanceId, theStep.getStepId(), theDefinition.isGatedExecution());
		} else {
			dataSink = (BaseDataSink<OT>) new FinalStepDataSink(theJobDefinitionId, instanceId, theStep.getStepId());
		}

		Class<IT> inputType = theStep.getInputType();
		boolean success = executeStep(theWorkChunk, theJobDefinitionId, theTargetStepId, inputType, parameters, worker, dataSink);
		if (!success) {
			return;
		}

		int workChunkCount = dataSink.getWorkChunkCount();
		if (theFirstStep && workChunkCount == 0) {
			ourLog.info("First step of job theInstance {} produced no work chunks, marking as completed", instanceId);
			myJobPersistence.markInstanceAsCompleted(instanceId);
		}

		if (theDefinition.isGatedExecution() && theFirstStep) {
			theInstance.setCurrentGatedStepId(theTargetStepId);
			myJobPersistence.updateInstance(theInstance);
		}

	}

	private <PT extends IModelJson, IT extends IModelJson, OT extends IModelJson> boolean executeStep(@Nonnull WorkChunk theWorkChunk, String theJobDefinitionId, String theTargetStepId, Class<IT> theInputType, PT theParameters, IJobStepWorker<PT, IT, OT> theWorker, BaseDataSink<OT> theDataSink) {
		IT data = null;
		if (!theInputType.equals(VoidModel.class)) {
			data = theWorkChunk.getData(theInputType);
		}

		String instanceId = theWorkChunk.getInstanceId();
		String chunkId = theWorkChunk.getId();

		StepExecutionDetails<PT, IT> stepExecutionDetails = new StepExecutionDetails<>(theParameters, data, instanceId, chunkId);
		RunOutcome outcome;
		try {
			outcome = theWorker.run(stepExecutionDetails, theDataSink);
			Validate.notNull(outcome, "Step theWorker returned null: %s", theWorker.getClass());
		} catch (JobExecutionFailedException e) {
			ourLog.error("Unrecoverable failure executing job {} step {}", theJobDefinitionId, theTargetStepId, e);
			myJobPersistence.markWorkChunkAsFailed(chunkId, e.toString());
			return false;
		} catch (Exception e) {
			ourLog.error("Failure executing job {} step {}", theJobDefinitionId, theTargetStepId, e);
			myJobPersistence.markWorkChunkAsErroredAndIncrementErrorCount(chunkId, e.toString());
			throw new JobExecutionFailedException(Msg.code(2041) + e.getMessage(), e);
		} catch (Throwable t) {
			ourLog.error("Unexpected failure executing job {} step {}", theJobDefinitionId, theTargetStepId, t);
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
