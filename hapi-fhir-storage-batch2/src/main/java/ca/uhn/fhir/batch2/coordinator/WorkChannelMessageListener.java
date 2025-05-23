/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.batch2.coordinator;

import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.channel.BatchJobSender;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkCursor;
import ca.uhn.fhir.batch2.model.JobWorkNotification;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.broker.api.IMessageListener;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.rest.server.messaging.IMessage;
import ca.uhn.fhir.util.Logs;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.MDC;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * This handler receives batch work request messages and performs the batch work requested by the message
 */
public class WorkChannelMessageListener implements IMessageListener<JobWorkNotification> {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();
	private final IJobPersistence myJobPersistence;
	private final JobDefinitionRegistry myJobDefinitionRegistry;
	private final IHapiTransactionService myHapiTransactionService;
	private final IInterceptorBroadcaster myInterceptorBroadcaster;
	private final JobStepExecutorFactory myJobStepExecutorFactory;

	public WorkChannelMessageListener(
			@Nonnull IJobPersistence theJobPersistence,
			@Nonnull JobDefinitionRegistry theJobDefinitionRegistry,
			@Nonnull BatchJobSender theBatchJobSender,
			@Nonnull WorkChunkProcessor theExecutorSvc,
			@Nonnull IJobMaintenanceService theJobMaintenanceService,
			IHapiTransactionService theHapiTransactionService,
			IInterceptorBroadcaster theInterceptorBroadcaster) {
		myJobPersistence = theJobPersistence;
		myJobDefinitionRegistry = theJobDefinitionRegistry;
		myHapiTransactionService = theHapiTransactionService;
		myInterceptorBroadcaster = theInterceptorBroadcaster;
		myJobStepExecutorFactory = new JobStepExecutorFactory(
				theJobPersistence,
				theBatchJobSender,
				theExecutorSvc,
				theJobMaintenanceService,
				theJobDefinitionRegistry);
	}

	public Class<JobWorkNotification> getPayloadType() {
		return JobWorkNotification.class;
	}

	@Override
	public void handleMessage(@Nonnull IMessage<JobWorkNotification> theMessage) {
		handleWorkChannelMessage(theMessage.getPayload());
	}

	/**
	 * Workflow scratchpad for processing a single chunk message.
	 */
	class MessageProcess {
		final JobWorkNotification myWorkNotification;
		String myChunkId;
		WorkChunk myWorkChunk;
		JobWorkCursor<?, ?, ?> myCursor;
		JobInstance myJobInstance;
		JobDefinition<?> myJobDefinition;
		JobStepExecutor<?, ?, ?> myStepExector;

		MessageProcess(JobWorkNotification theWorkNotification) {
			myWorkNotification = theWorkNotification;
		}

		/**
		 * Save the chunkId and validate.
		 */
		Optional<MessageProcess> validateChunkId() {
			myChunkId = myWorkNotification.getChunkId();
			if (myChunkId == null) {
				ourLog.error("Received work notification with null chunkId: {}", myWorkNotification);
				return Optional.empty();
			}
			return Optional.of(this);
		}

		Optional<MessageProcess> loadJobDefinitionOrThrow() {
			String jobDefinitionId = myWorkNotification.getJobDefinitionId();
			int jobDefinitionVersion = myWorkNotification.getJobDefinitionVersion();

			// Do not catch this exception - that will discard this chunk.
			// Failing to load a job definition probably means this is an old process during upgrade.
			// Retry those until this node is killed/restarted.
			myJobDefinition =
					myJobDefinitionRegistry.getJobDefinitionOrThrowException(jobDefinitionId, jobDefinitionVersion);
			return Optional.of(this);
		}

		/**
		 * Fetch the job instance including the job definition.
		 */
		Optional<MessageProcess> loadJobInstance() {
			return myJobPersistence
					.fetchInstance(myWorkNotification.getInstanceId())
					.or(() -> {
						ourLog.error(
								"No instance {} exists for chunk notification {}",
								myWorkNotification.getInstanceId(),
								myWorkNotification);
						return Optional.empty();
					})
					.map(instance -> {
						myJobInstance = instance;
						instance.setJobDefinition(myJobDefinition);
						return this;
					});
		}

		/**
		 * Load the chunk, and mark it as dequeued.
		 */
		Optional<MessageProcess> updateChunkStatusAndValidate() {
			return myJobPersistence
					.onWorkChunkDequeue(myChunkId)
					.or(() -> {
						ourLog.error("Unable to find chunk with ID {} - Aborting.  {}", myChunkId, myWorkNotification);
						return Optional.empty();
					})
					.map(chunk -> {
						myWorkChunk = chunk;
						ourLog.debug(
								"Worker picked up chunk. [chunkId={}, stepId={}, startTime={}]",
								myChunkId,
								myWorkChunk.getTargetStepId(),
								myWorkChunk.getStartTime());
						return this;
					});
		}

		/**
		 * Move QUEUED jobs to IN_PROGRESS, and make sure we are not already in final state.
		 */
		Optional<MessageProcess> updateAndValidateJobStatus() {
			ourLog.trace(
					"Check status {} of job {} for chunk {}",
					myJobInstance.getStatus(),
					myJobInstance.getInstanceId(),
					myChunkId);
			switch (myJobInstance.getStatus()) {
				case QUEUED:
					// Update the job as started.
					myJobPersistence.onChunkDequeued(myJobInstance.getInstanceId());
					break;

				case IN_PROGRESS:
				case ERRORED:
				case FINALIZE:
					// normal processing
					break;

				case COMPLETED:
					// this is an error, but we can't do much about it.
					ourLog.error(
							"Received chunk {}, but job instance is {}.  Skipping.",
							myChunkId,
							myJobInstance.getStatus());
					return Optional.empty();

				case CANCELLED:
				case FAILED:
				default:
					// should we mark the chunk complete/failed for any of these skipped?
					ourLog.info("Skipping chunk {} because job instance is {}", myChunkId, myJobInstance.getStatus());
					return Optional.empty();
			}

			return Optional.of(this);
		}

		Optional<MessageProcess> buildCursor() {

			myCursor = JobWorkCursor.fromJobDefinitionAndRequestedStepId(
					myJobDefinition, myWorkNotification.getTargetStepId());

			if (!myWorkChunk.getTargetStepId().equals(myCursor.getCurrentStepId())) {
				ourLog.error(
						"Chunk {} has target step {} but expected {}",
						myChunkId,
						myWorkChunk.getTargetStepId(),
						myCursor.getCurrentStepId());
				return Optional.empty();
			}
			return Optional.of(this);
		}

		public Optional<MessageProcess> buildStepExecutor() {
			this.myStepExector =
					myJobStepExecutorFactory.newJobStepExecutor(this.myJobInstance, this.myWorkChunk, this.myCursor);

			return Optional.of(this);
		}
	}

	private void handleWorkChannelMessage(JobWorkNotification theWorkNotification) {
		try {
			// Load the job instance and work chunk IDs into the logging MDC context
			BatchJobTracingContext.setBatchJobIds(
					theWorkNotification.getInstanceId(), theWorkNotification.getChunkId());
			ourLog.info("Received work notification for {}", theWorkNotification);

			// There are three paths through this code:
			// 1. Normal execution.  We validate, load, update statuses, all in a tx.  Then we process the chunk.
			// 2. Discard chunk.  If some validation fails (e.g. no chunk with that id), we log and discard the chunk.
			//    Probably a db rollback, with a stale queue.
			// 3. Fail and retry.  If we throw an exception out of here, Spring will put the queue message back, and
			// redeliver later.
			//
			// We use Optional chaining here to simplify all the cases where we short-circuit exit.
			// A step that returns an empty Optional means discard the chunk.
			//
			Optional<MessageProcess> processingPreparation = executeInTxRollbackWhenEmpty(() ->

					// Use a chain of Optional flatMap to handle all the setup short-circuit exits cleanly.
					Optional.of(new MessageProcess(theWorkNotification))
							// validate and load info
							.flatMap(MessageProcess::validateChunkId)
							// no job definition should be retried - we must be a stale process encountering a new
							// job definition.
							.flatMap(MessageProcess::loadJobDefinitionOrThrow)
							.flatMap(MessageProcess::loadJobInstance)
							// update statuses now in the db: QUEUED->IN_PROGRESS
							.flatMap(MessageProcess::updateChunkStatusAndValidate)
							.flatMap(MessageProcess::updateAndValidateJobStatus)
							// ready to execute
							.flatMap(MessageProcess::buildCursor)
							.flatMap(MessageProcess::buildStepExecutor));

			processingPreparation.ifPresentOrElse(
					// all the setup is happy and committed.  Do the work.
					process -> {
						HookParams params = new HookParams()
								.add(JobInstance.class, process.myJobInstance)
								.add(WorkChunk.class, process.myWorkChunk);

						/*
						 * The executeStep() method actually performs the processing of a given work chunk, but
						 * this execution can optionally be wrapped by interceptors wanting to influence the processing.
						 */
						Runnable runnable = () -> process.myStepExector.executeStep();

						myInterceptorBroadcaster.runWithFilterHooks(
								Pointcut.BATCH2_CHUNK_PROCESS_FILTER, params, runnable);
					},
					() -> {
						// discard the chunk
						ourLog.debug("Discarding chunk notification {}", theWorkNotification);
					});
		} finally {
			BatchJobTracingContext.clearBatchJobsIds();
		}
	}

	/**
	 * Run theCallback in TX, rolling back if the supplied Optional is empty.
	 */
	<T> Optional<T> executeInTxRollbackWhenEmpty(Supplier<Optional<T>> theCallback) {
		return myHapiTransactionService
				// batch storage is not partitioned.
				.withSystemRequestOnDefaultPartition()
				.execute(theTransactionStatus -> {

					// run the processing
					Optional<T> setupProcessing = theCallback.get();

					if (setupProcessing.isEmpty()) {
						// If any setup failed, roll back the chunk and instance status changes.
						ourLog.debug("WorkChunk setup failed - rollback tx");
						theTransactionStatus.setRollbackOnly();
					}
					// else COMMIT the work.

					return setupProcessing;
				});
	}

	/**
	 * Simple wrapper around the slf4j MDC threadlocal log context.
	 */
	public static class BatchJobTracingContext {
		static final String INSTANCE_ID = "instanceId";
		static final String CHUNK_ID = "chunkId";

		public static void setBatchJobIds(String theInstanceId, String theChunkId) {
			MDC.put(INSTANCE_ID, theInstanceId);
			MDC.put(CHUNK_ID, theChunkId);
		}

		public static void clearBatchJobsIds() {
			MDC.remove(INSTANCE_ID);
			MDC.remove(CHUNK_ID);
		}
	}
}
