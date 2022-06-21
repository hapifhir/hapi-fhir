package ca.uhn.fhir.batch2.coordinator;

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
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.channel.BatchJobSender;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.JobWorkNotification;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelReceiver;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.apache.commons.lang3.Validate;
import org.springframework.messaging.MessageHandler;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class JobCoordinatorImpl implements IJobCoordinator {

	private final IJobPersistence myJobPersistence;
	private final BatchJobSender myBatchJobSender;
	private final IChannelReceiver myWorkChannelReceiver;
	private final JobDefinitionRegistry myJobDefinitionRegistry;
	private final MessageHandler myReceiverHandler;
	private final JobQuerySvc myJobQuerySvc;
	private final JobParameterJsonValidator myJobParameterJsonValidator;

	/**
	 * Constructor
	 */
	public JobCoordinatorImpl(@Nonnull BatchJobSender theBatchJobSender,
									  @Nonnull IChannelReceiver theWorkChannelReceiver,
									  @Nonnull IJobPersistence theJobPersistence,
									  @Nonnull JobDefinitionRegistry theJobDefinitionRegistry,
									  @Nonnull StepExecutionSvc theExecutorSvc
	) {
		Validate.notNull(theJobPersistence);

		myJobPersistence = theJobPersistence;
		myBatchJobSender = theBatchJobSender;
		myWorkChannelReceiver = theWorkChannelReceiver;
		myJobDefinitionRegistry = theJobDefinitionRegistry;

		myReceiverHandler = new WorkChannelMessageHandler(theJobPersistence, theJobDefinitionRegistry, theBatchJobSender, theExecutorSvc);
		myJobQuerySvc = new JobQuerySvc(theJobPersistence, theJobDefinitionRegistry);
		myJobParameterJsonValidator = new JobParameterJsonValidator();
	}

	@Override
	public String startInstance(JobInstanceStartRequest theStartRequest) {
		JobDefinition<?> jobDefinition = myJobDefinitionRegistry.getLatestJobDefinition(theStartRequest.getJobDefinitionId()).orElseThrow(() -> new IllegalArgumentException(Msg.code(2063) + "Unknown job definition ID: " + theStartRequest.getJobDefinitionId()));

		if (isBlank(theStartRequest.getParameters())) {
			throw new InvalidRequestException(Msg.code(2065) + "No parameters supplied");
		}

		myJobParameterJsonValidator.validateJobParameters(theStartRequest, jobDefinition);

		JobInstance instance = JobInstance.fromJobDefinition(jobDefinition);
		instance.setParameters(theStartRequest.getParameters());
		instance.setStatus(StatusEnum.QUEUED);

		String instanceId = myJobPersistence.storeNewInstance(instance);

		BatchWorkChunk batchWorkChunk = BatchWorkChunk.firstChunk(jobDefinition, instanceId);
		String chunkId = myJobPersistence.storeWorkChunk(batchWorkChunk);

		JobWorkNotification workNotification = JobWorkNotification.firstStepNotification(jobDefinition, instanceId, chunkId);
		myBatchJobSender.sendWorkChannelMessage(workNotification);

		return instanceId;
	}

	@Override
	public JobInstance getInstance(String theInstanceId) {
		return myJobQuerySvc.fetchInstance(theInstanceId);
	}

	@Override
	public List<JobInstance> getInstances(int thePageSize, int thePageIndex) {
		return myJobQuerySvc.fetchInstances(thePageSize, thePageIndex);
	}

	@Override
	public List<JobInstance> getRecentInstances(int theCount, int theStart) {
		return myJobQuerySvc.fetchRecentInstances(theCount, theStart);
	}

	@Override
	public void cancelInstance(String theInstanceId) throws ResourceNotFoundException {
		myJobPersistence.cancelInstance(theInstanceId);
	}

	@PostConstruct
	public void start() {
		myWorkChannelReceiver.subscribe(myReceiverHandler);
	}

	@PreDestroy
	public void stop() {
		myWorkChannelReceiver.unsubscribe(myReceiverHandler);
	}
}
