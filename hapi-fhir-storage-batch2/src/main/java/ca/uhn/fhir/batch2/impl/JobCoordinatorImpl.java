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
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.JobWorkNotification;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.i18n.Msg;
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
import org.springframework.messaging.MessageHandler;

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
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class JobCoordinatorImpl implements IJobCoordinator {

	private final IJobPersistence myJobPersistence;
	private final BatchJobSender myBatchJobSender;
	private final IChannelReceiver myWorkChannelReceiver;
	private final JobDefinitionRegistry myJobDefinitionRegistry;
	private final MessageHandler myReceiverHandler;
	private final ValidatorFactory myValidatorFactory = Validation.buildDefaultValidatorFactory();

	/**
	 * Constructor
	 */
	public JobCoordinatorImpl(@Nonnull BatchJobSender theBatchJobSender, @Nonnull IChannelReceiver theWorkChannelReceiver, @Nonnull IJobPersistence theJobPersistence, @Nonnull JobDefinitionRegistry theJobDefinitionRegistry) {
		Validate.notNull(theJobPersistence);

		myJobPersistence = theJobPersistence;
		myBatchJobSender = theBatchJobSender;
		myWorkChannelReceiver = theWorkChannelReceiver;
		myJobDefinitionRegistry = theJobDefinitionRegistry;

		myReceiverHandler = new WorkChannelMessageHandler(theJobPersistence, theJobDefinitionRegistry, theBatchJobSender);
	}

	@Override
	public String startInstance(JobInstanceStartRequest theStartRequest) {
		JobDefinition<?> jobDefinition = myJobDefinitionRegistry.getLatestJobDefinition(theStartRequest.getJobDefinitionId()).orElseThrow(() -> new IllegalArgumentException(Msg.code(2063) + "Unknown job definition ID: " + theStartRequest.getJobDefinitionId()));

		if (isBlank(theStartRequest.getParameters())) {
			throw new InvalidRequestException(Msg.code(2065) + "No parameters supplied");
		}

		validateJobParameters(theStartRequest, jobDefinition);

		JobInstance instance = JobInstance.fromJobDefinition(jobDefinition);
		instance.setParameters(theStartRequest.getParameters());
		instance.setStatus(StatusEnum.QUEUED);

		String instanceId = myJobPersistence.storeNewInstance(instance);

		BatchWorkChunk batchWorkChunk = BatchWorkChunk.firstChunk(jobDefinition, instanceId);
		String chunkId = myJobPersistence.storeWorkChunk(batchWorkChunk);

		JobWorkNotification workNotification = JobWorkNotification.firstNotification(jobDefinition, instanceId, chunkId);
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
	public void cancelInstance(String theInstanceId) throws ResourceNotFoundException {
		myJobPersistence.cancelInstance(theInstanceId);
	}

	private JobInstance massageInstanceForUserAccess(JobInstance theInstance) {
		JobInstance retVal = new JobInstance(theInstance);

		JobDefinition definition = myJobDefinitionRegistry.getDefinitionOrThrowException(theInstance.getJobDefinitionId(), theInstance.getJobDefinitionVersion());

		// Serializing the parameters strips any write-only params
		IModelJson parameters = retVal.getParameters(definition.getParametersType());
		stripPasswordFields(parameters);
		String parametersString = JsonUtil.serializeOrInvalidRequest(parameters);
		retVal.setParameters(parametersString);

		return retVal;
	}

	@PostConstruct
	public void start() {
		myWorkChannelReceiver.subscribe(myReceiverHandler);
	}

	@PreDestroy
	public void stop() {
		myWorkChannelReceiver.unsubscribe(myReceiverHandler);
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
}
