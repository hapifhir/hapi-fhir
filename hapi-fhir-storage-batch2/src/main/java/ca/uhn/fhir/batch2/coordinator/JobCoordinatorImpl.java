package ca.uhn.fhir.batch2.coordinator;

/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.JobOperationResultJson;
import ca.uhn.fhir.batch2.channel.BatchJobSender;
import ca.uhn.fhir.batch2.model.FetchJobInstancesRequest;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.JobWorkNotification;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.models.JobInstanceFetchRequest;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelReceiver;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.Logs;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.messaging.MessageHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class JobCoordinatorImpl implements IJobCoordinator {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();

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
									  @Nonnull WorkChunkProcessor theExecutorSvc,
									  @Nonnull IJobMaintenanceService theJobMaintenanceService) {
		Validate.notNull(theJobPersistence);

		myJobPersistence = theJobPersistence;
		myBatchJobSender = theBatchJobSender;
		myWorkChannelReceiver = theWorkChannelReceiver;
		myJobDefinitionRegistry = theJobDefinitionRegistry;

		myReceiverHandler = new WorkChannelMessageHandler(theJobPersistence, theJobDefinitionRegistry, theBatchJobSender, theExecutorSvc, theJobMaintenanceService);
		myJobQuerySvc = new JobQuerySvc(theJobPersistence, theJobDefinitionRegistry);
		myJobParameterJsonValidator = new JobParameterJsonValidator();
	}

	@Override
	public Batch2JobStartResponse startInstance(JobInstanceStartRequest theStartRequest) {
		JobDefinition<?> jobDefinition = myJobDefinitionRegistry
			.getLatestJobDefinition(theStartRequest.getJobDefinitionId()).orElseThrow(() -> new IllegalArgumentException(Msg.code(2063) + "Unknown job definition ID: " + theStartRequest.getJobDefinitionId()));

		String paramsString = theStartRequest.getParameters();
		if (isBlank(paramsString)) {
			throw new InvalidRequestException(Msg.code(2065) + "No parameters supplied");
		}
		// if cache - use that first
		if (theStartRequest.isUseCache()) {
			FetchJobInstancesRequest request = new FetchJobInstancesRequest(theStartRequest.getJobDefinitionId(), theStartRequest.getParameters(), getStatesThatTriggerCache());

			List<JobInstance> existing = myJobPersistence.fetchInstances(request, 0, 1000);
			if (!existing.isEmpty()) {
				// we'll look for completed ones first... otherwise, take any of the others
				Collections.sort(existing, (o1, o2) -> -(o1.getStatus().ordinal() - o2.getStatus().ordinal()));

				JobInstance first = existing.stream().findFirst().get();

				Batch2JobStartResponse response = new Batch2JobStartResponse();
				response.setInstanceId(first.getInstanceId());
				response.setUsesCachedResult(true);

				ourLog.info("Reusing cached {} job with status {} and id {}", first.getJobDefinitionId(), first.getStatus(), first.getInstanceId());

				return response;
			}
		}

		myJobParameterJsonValidator.validateJobParameters(theStartRequest, jobDefinition);

		JobInstance instance = JobInstance.fromJobDefinition(jobDefinition);
		instance.setParameters(theStartRequest.getParameters());
		instance.setStatus(StatusEnum.QUEUED);

		String instanceId = myJobPersistence.storeNewInstance(instance);
		ourLog.info("Stored new {} job {} with status {}", jobDefinition.getJobDefinitionId(), instanceId, instance.getStatus());
		ourLog.debug("Job parameters: {}", instance.getParameters());

		BatchWorkChunk batchWorkChunk = BatchWorkChunk.firstChunk(jobDefinition, instanceId);
		String chunkId = myJobPersistence.storeWorkChunk(batchWorkChunk);

		JobWorkNotification workNotification = JobWorkNotification.firstStepNotification(jobDefinition, instanceId, chunkId);
		myBatchJobSender.sendWorkChannelMessage(workNotification);

		Batch2JobStartResponse response = new Batch2JobStartResponse();
		response.setInstanceId(instanceId);
		return response;
	}

	/**
	 * Cache will be used if an identical job is QUEUED or IN_PROGRESS. Otherwise a new one will kickoff.
	 */
	private StatusEnum[] getStatesThatTriggerCache() {
		return new StatusEnum[]{StatusEnum.QUEUED, StatusEnum.IN_PROGRESS};
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
	public List<JobInstance> getInstancesbyJobDefinitionIdAndEndedStatus(String theJobDefinitionId, @Nullable Boolean theEnded, int theCount, int theStart) {
		return myJobQuerySvc.getInstancesByJobDefinitionIdAndEndedStatus(theJobDefinitionId, theEnded, theCount, theStart);
	}

	@Override
	public List<JobInstance> getJobInstancesByJobDefinitionIdAndStatuses(String theJobDefinitionId, Set<StatusEnum> theStatuses, int theCount, int theStart) {
		return myJobQuerySvc.getInstancesByJobDefinitionAndStatuses(theJobDefinitionId, theStatuses, theCount, theStart);
	}

	@Override
	public List<JobInstance> getJobInstancesByJobDefinitionId(String theJobDefinitionId, int theCount, int theStart) {
		return getJobInstancesByJobDefinitionIdAndStatuses(theJobDefinitionId, new HashSet<>(Arrays.asList(StatusEnum.values())), theCount, theStart);
	}

	@Override
	public Page<JobInstance> fetchAllJobInstances(JobInstanceFetchRequest theFetchRequest) {
		return myJobQuerySvc.fetchAllInstances(theFetchRequest);
	}

	@Override
	public JobOperationResultJson cancelInstance(String theInstanceId) throws ResourceNotFoundException {
		return myJobPersistence.cancelInstance(theInstanceId);
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
