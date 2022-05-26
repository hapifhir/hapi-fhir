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


import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.channel.BatchJobSender;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkCursor;
import ca.uhn.fhir.batch2.model.JobWorkNotification;
import ca.uhn.fhir.batch2.model.JobWorkNotificationJsonMessage;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * This handler receives batch work request messages and performs the batch work requested by the message
 */
class WorkChannelMessageHandler implements MessageHandler {
	private static final Logger ourLog = LoggerFactory.getLogger(WorkChannelMessageHandler.class);
	private final IJobPersistence myJobPersistence;
	private final JobDefinitionRegistry myJobDefinitionRegistry;
	private final JobStepExecutorFactory myJobStepExecutorFactory;

	WorkChannelMessageHandler(@Nonnull IJobPersistence theJobPersistence, @Nonnull JobDefinitionRegistry theJobDefinitionRegistry, @Nonnull BatchJobSender theBatchJobSender) {
		myJobPersistence = theJobPersistence;
		myJobDefinitionRegistry = theJobDefinitionRegistry;
		myJobStepExecutorFactory = new JobStepExecutorFactory(theJobPersistence, theBatchJobSender);
	}

	@Override
	public void handleMessage(@Nonnull Message<?> theMessage) throws MessagingException {
		handleWorkChannelMessage((JobWorkNotificationJsonMessage) theMessage);
	}

	private void handleWorkChannelMessage(JobWorkNotificationJsonMessage theMessage) {
		JobWorkNotification workNotification = theMessage.getPayload();

		String chunkId = workNotification.getChunkId();
		Validate.notNull(chunkId);
		Optional<WorkChunk> chunkOpt = myJobPersistence.fetchWorkChunkSetStartTimeAndMarkInProgress(chunkId);
		if (chunkOpt.isEmpty()) {
			ourLog.error("Unable to find chunk with ID {} - Aborting", chunkId);
			return;
		}
		WorkChunk workChunk = chunkOpt.get();

		JobWorkCursor<?, ?, ?> cursor = buildCursorFromNotification(workNotification);

		Validate.isTrue(workChunk.getTargetStepId().equals(cursor.getCurrentStepId()), "Chunk %s has target step %s but expected %s", chunkId, workChunk.getTargetStepId(), cursor.getCurrentStepId());

		Optional<JobInstance> instanceOpt = myJobPersistence.fetchInstanceAndMarkInProgress(workNotification.getInstanceId());
		JobInstance instance = instanceOpt.orElseThrow(() -> new InternalErrorException("Unknown instance: " + workNotification.getInstanceId()));
		String instanceId = instance.getInstanceId();

		if (instance.isCancelled()) {
			ourLog.info("Skipping chunk {} because job instance is cancelled", chunkId);
			myJobPersistence.markInstanceAsCompleted(instanceId);
			return;
		}

		JobStepExecutor<?,?,?> stepExecutor = myJobStepExecutorFactory.newJobStepExecutor(instance, workChunk, cursor);
		stepExecutor.executeStep();
	}

	private JobWorkCursor<?, ?, ?> buildCursorFromNotification(JobWorkNotification workNotification) {
		String jobDefinitionId = workNotification.getJobDefinitionId();
		int jobDefinitionVersion = workNotification.getJobDefinitionVersion();

		JobDefinition<?> definition = myJobDefinitionRegistry.getJobDefinitionOrThrowException(jobDefinitionId, jobDefinitionVersion);

		return JobWorkCursor.fromJobDefinitionAndRequestedStepId(definition, workNotification.getTargetStepId());
	}
}
