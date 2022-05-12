package ca.uhn.fhir.batch2.impl;

/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.IJobParametersValidator;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobDefinitionStep;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.JobWorkNotification;
import ca.uhn.fhir.batch2.model.JobWorkNotificationJsonMessage;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelReceiver;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.model.api.annotation.PasswordField;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.fhir.util.UrlUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class JobCoordinatorImpl extends BaseJobService implements IJobCoordinator {

	private static final Logger ourLog = LoggerFactory.getLogger(JobCoordinatorImpl.class);
	private final BatchJobSender myBatchJobSender;
	private final IChannelReceiver myWorkChannelReceiver;
	private final JobDefinitionRegistry myJobDefinitionRegistry;
	private final MessageHandler myReceiverHandler = new WorkChannelMessageHandler();
	private final ValidatorFactory myValidatorFactory = Validation.buildDefaultValidatorFactory();
	@Autowired
	private ISchedulerService mySchedulerService;

	/**
	 * Constructor
	 */
	public JobCoordinatorImpl(@Nonnull BatchJobSender theBatchJobSender, @Nonnull IChannelReceiver theWorkChannelReceiver, @Nonnull IJobPersistence theJobPersistence, @Nonnull JobDefinitionRegistry theJobDefinitionRegistry) {
		super(theJobPersistence);
		myBatchJobSender = theBatchJobSender;
		myWorkChannelReceiver = theWorkChannelReceiver;
		myJobDefinitionRegistry = theJobDefinitionRegistry;
	}

	@Override
	public String startInstance(JobInstanceStartRequest theStartRequest) {
		JobDefinition<?> jobDefinition = myJobDefinitionRegistry.getLatestJobDefinition(theStartRequest.getJobDefinitionId()).orElseThrow(() -> new IllegalArgumentException(Msg.code(2063) + "Unknown job definition ID: " + theStartRequest.getJobDefinitionId()));

		if (isBlank(theStartRequest.getParameters())) {
			throw new InvalidRequestException(Msg.code(2065) + "No parameters supplied");
		}

		validateJobParameters(theStartRequest, jobDefinition);

		String firstStepId = jobDefinition.getSteps().get(0).getStepId();
		String jobDefinitionId = jobDefinition.getJobDefinitionId();
		int jobDefinitionVersion = jobDefinition.getJobDefinitionVersion();

		JobInstance instance = new JobInstance();
		instance.setJobDefinitionId(jobDefinitionId);
		instance.setJobDefinitionVersion(jobDefinitionVersion);
		instance.setStatus(StatusEnum.QUEUED);
		instance.setParameters(theStartRequest.getParameters());

		if (jobDefinition.isGatedExecution()) {
			instance.setCurrentGatedStepId(firstStepId);
		}

		String instanceId = myJobPersistence.storeNewInstance(instance);

		BatchWorkChunk batchWorkChunk = new BatchWorkChunk(jobDefinitionId, jobDefinitionVersion, firstStepId, instanceId, 0, null);
		String chunkId = myJobPersistence.storeWorkChunk(batchWorkChunk);

		JobWorkNotification workNotification = new JobWorkNotification(jobDefinitionId, jobDefinitionVersion, instanceId, firstStepId, chunkId);
		myBatchJobSender.sendWorkChannelMessage(workNotification);

		return instanceId;
	}

	private <PT extends IModelJson> void validateJobParameters(JobInstanceStartRequest theStartRequest, JobDefinition<PT> theJobDefinition) {

		// JSR 380
		Validator validator = myValidatorFactory.getValidator();
		PT parameters = theStartRequest.getParameters(theJobDefinition.getParametersType());
		Set<ConstraintViolation<IModelJson>> constraintErrors = validator.validate(parameters);
		List<String> errorStrings = constraintErrors.stream().map(t -> t.getPropertyPath() + " - " + t.getMessage()).sorted().collect(Collectors.toList());

		// Programmatic Validator
		IJobParametersValidator<PT> parametersValidator = theJobDefinition.getParametersValidator();
		if (parametersValidator != null) {
			List<String> outcome = parametersValidator.validate(parameters);
			outcome = defaultIfNull(outcome, Collections.emptyList());
			errorStrings.addAll(outcome);
		}

		if (!errorStrings.isEmpty()) {
			String message = "Failed to validate parameters for job of type " + theJobDefinition.getJobDefinitionId() + ": " + errorStrings.stream().map(t -> "\n * " + t).collect(Collectors.joining());

			throw new InvalidRequestException(Msg.code(2039) + message);
		}
	}

	@Override
	public JobInstance getInstance(String theInstanceId) {
		return myJobPersistence.fetchInstance(theInstanceId).map(t -> massageInstanceForUserAccess(t)).orElseThrow(() -> new ResourceNotFoundException(Msg.code(2040) + "Unknown instance ID: " + UrlUtil.escapeUrlParam(theInstanceId)));
	}

	@Override
	public List<JobInstance> getInstances(int thePageSize, int thePageIndex) {
		return myJobPersistence.fetchInstances(thePageSize, thePageIndex).stream().map(t -> massageInstanceForUserAccess(t)).collect(Collectors.toList());
	}

	@Override
	public List<JobInstance> getRecentInstances(int theCount, int theStart) {
		return myJobPersistence.fetchRecentInstances(theCount, theStart)
			.stream().map(this::massageInstanceForUserAccess).collect(Collectors.toList());
	}

	@Override
	public void cancelInstance(String theInstanceId) throws ResourceNotFoundException {
		myJobPersistence.cancelInstance(theInstanceId);
	}

	private JobInstance massageInstanceForUserAccess(JobInstance theInstance) {
		JobInstance retVal = new JobInstance(theInstance);

		JobDefinition definition = getDefinitionOrThrowException(theInstance.getJobDefinitionId(), theInstance.getJobDefinitionVersion());

		// Serializing the parameters strips any write-only params
		IModelJson parameters = retVal.getParameters(definition.getParametersType());
		stripPasswordFields(parameters);
		String parametersString = JsonUtil.serializeOrInvalidRequest(parameters);
		retVal.setParameters(parametersString);

		return retVal;
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

	@PostConstruct
	public void start() {
		myWorkChannelReceiver.subscribe(myReceiverHandler);
	}

	@PreDestroy
	public void stop() {
		myWorkChannelReceiver.unsubscribe(myReceiverHandler);
	}

	private void handleWorkChannelMessage(JobWorkNotificationJsonMessage theMessage) {
		JobWorkNotification payload = theMessage.getPayload();

		String chunkId = payload.getChunkId();
		Validate.notNull(chunkId);
		Optional<WorkChunk> chunkOpt = myJobPersistence.fetchWorkChunkSetStartTimeAndMarkInProgress(chunkId);
		if (!chunkOpt.isPresent()) {
			ourLog.error("Unable to find chunk with ID {} - Aborting", chunkId);
			return;
		}
		WorkChunk chunk = chunkOpt.get();

		String jobDefinitionId = payload.getJobDefinitionId();
		int jobDefinitionVersion = payload.getJobDefinitionVersion();
		JobDefinition definition = getDefinitionOrThrowException(jobDefinitionId, jobDefinitionVersion);

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

	private JobDefinition<?> getDefinitionOrThrowException(String jobDefinitionId, int jobDefinitionVersion) {
		Optional<JobDefinition<?>> opt = myJobDefinitionRegistry.getJobDefinition(jobDefinitionId, jobDefinitionVersion);
		if (!opt.isPresent()) {
			String msg = "Unknown job definition ID[" + jobDefinitionId + "] version[" + jobDefinitionVersion + "]";
			ourLog.warn(msg);
			throw new InternalErrorException(Msg.code(2043) + msg);
		}
		return opt.get();
	}

	/**
	 * Scans a model object for fields marked as {@link PasswordField}
	 * and nulls them
	 */
	private static void stripPasswordFields(@Nonnull Object theParameters) {
		Field[] declaredFields = theParameters.getClass().getDeclaredFields();
		for (Field nextField : declaredFields) {

			JsonProperty propertyAnnotation = nextField.getAnnotation(JsonProperty.class);
			if (propertyAnnotation == null) {
				continue;
			}

			nextField.setAccessible(true);
			try {
				Object nextValue = nextField.get(theParameters);
				if (nextField.getAnnotation(PasswordField.class) != null) {
					nextField.set(theParameters, null);
				} else if (nextValue != null) {
					stripPasswordFields(nextValue);
				}
			} catch (IllegalAccessException e) {
				throw new InternalErrorException(Msg.code(2044) + e.getMessage(), e);
			}
		}
	}

	private class WorkChannelMessageHandler implements MessageHandler {
		@Override
		public void handleMessage(@Nonnull Message<?> theMessage) throws MessagingException {
			handleWorkChannelMessage((JobWorkNotificationJsonMessage) theMessage);
		}
	}
}
