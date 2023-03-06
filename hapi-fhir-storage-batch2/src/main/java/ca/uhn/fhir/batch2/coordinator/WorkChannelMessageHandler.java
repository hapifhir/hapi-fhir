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


import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.channel.BatchJobSender;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkCursor;
import ca.uhn.fhir.batch2.model.JobWorkNotification;
import ca.uhn.fhir.batch2.model.JobWorkNotificationJsonMessage;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.progress.JobInstanceStatusUpdater;
import ca.uhn.fhir.batch2.util.Batch2Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.Logs;
import org.apache.commons.lang3.Validate;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

/**
 * This handler receives batch work request messages and performs the batch work requested by the message
 */
class WorkChannelMessageHandler implements MessageHandler {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();
	private final IJobPersistence myJobPersistence;
	private final JobDefinitionRegistry myJobDefinitionRegistry;
	private final JobStepExecutorFactory myJobStepExecutorFactory;
	private final JobInstanceStatusUpdater myJobInstanceStatusUpdater;

	WorkChannelMessageHandler(@Nonnull IJobPersistence theJobPersistence,
									  @Nonnull JobDefinitionRegistry theJobDefinitionRegistry,
									  @Nonnull BatchJobSender theBatchJobSender,
									  @Nonnull WorkChunkProcessor theExecutorSvc,
									  @Nonnull IJobMaintenanceService theJobMaintenanceService) {
		myJobPersistence = theJobPersistence;
		myJobDefinitionRegistry = theJobDefinitionRegistry;
		myJobStepExecutorFactory = new JobStepExecutorFactory(theJobPersistence, theBatchJobSender, theExecutorSvc, theJobMaintenanceService, theJobDefinitionRegistry);
		myJobInstanceStatusUpdater = new JobInstanceStatusUpdater(theJobPersistence, theJobDefinitionRegistry);
	}

	@Override
	public void handleMessage(@Nonnull Message<?> theMessage) throws MessagingException {
		handleWorkChannelMessage((JobWorkNotificationJsonMessage) theMessage);
	}

	private void handleWorkChannelMessage(JobWorkNotificationJsonMessage theMessage) {
		JobWorkNotification workNotification = theMessage.getPayload();
		ourLog.info("Received work notification for {}", workNotification);

		String chunkId = workNotification.getChunkId();
		Validate.notNull(chunkId);

		JobWorkCursor<?, ?, ?> cursor = null;
		WorkChunk workChunk = null;
		Optional<WorkChunk> chunkOpt = myJobPersistence.fetchWorkChunkSetStartTimeAndMarkInProgress(chunkId);
		if (chunkOpt.isEmpty()) {
			ourLog.error("Unable to find chunk with ID {} - Aborting", chunkId);
			return;
		}
		workChunk = chunkOpt.get();
		ourLog.debug("Worker picked up chunk. [chunkId={}, stepId={}, startTime={}]", chunkId, workChunk.getTargetStepId(), workChunk.getStartTime());

		cursor = buildCursorFromNotification(workNotification);

		Validate.isTrue(workChunk.getTargetStepId().equals(cursor.getCurrentStepId()), "Chunk %s has target step %s but expected %s", chunkId, workChunk.getTargetStepId(), cursor.getCurrentStepId());

		Optional<JobInstance> instanceOpt = myJobPersistence.fetchInstance(workNotification.getInstanceId());
		JobInstance instance = instanceOpt.orElseThrow(() -> new InternalErrorException("Unknown instance: " + workNotification.getInstanceId()));
		markInProgressIfQueued(instance);
		myJobDefinitionRegistry.setJobDefinition(instance);
		String instanceId = instance.getInstanceId();

		if (instance.isCancelled()) {
			ourLog.info("Skipping chunk {} because job instance is cancelled", chunkId);
			myJobPersistence.markInstanceAsCompleted(instanceId);
			return;
		}

		JobStepExecutor<?,?,?> stepExecutor = myJobStepExecutorFactory.newJobStepExecutor(instance, workChunk, cursor);
		stepExecutor.executeStep();
	}

	private void markInProgressIfQueued(JobInstance theInstance) {
		if (theInstance.getStatus() == StatusEnum.QUEUED) {
			myJobInstanceStatusUpdater.updateInstanceStatus(theInstance, StatusEnum.IN_PROGRESS);
		}
	}

	private JobWorkCursor<?, ?, ?> buildCursorFromNotification(JobWorkNotification workNotification) {
		String jobDefinitionId = workNotification.getJobDefinitionId();
		int jobDefinitionVersion = workNotification.getJobDefinitionVersion();

		JobDefinition<?> definition = myJobDefinitionRegistry.getJobDefinitionOrThrowException(jobDefinitionId, jobDefinitionVersion);

		return JobWorkCursor.fromJobDefinitionAndRequestedStepId(definition, workNotification.getTargetStepId());
	}
}
