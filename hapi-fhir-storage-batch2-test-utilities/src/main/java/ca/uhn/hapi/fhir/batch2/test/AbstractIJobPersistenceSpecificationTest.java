/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 specification tests
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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

package ca.uhn.hapi.fhir.batch2.test;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.coordinator.JobDefinitionRegistry;
import ca.uhn.fhir.batch2.maintenance.JobChunkProgressAccumulator;
import ca.uhn.fhir.batch2.maintenance.JobInstanceProcessor;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkCompletionEvent;
import ca.uhn.fhir.batch2.model.WorkChunkCreateEvent;
import ca.uhn.fhir.batch2.model.WorkChunkErrorEvent;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.hapi.fhir.batch2.test.support.TestJobParameters;
import ca.uhn.hapi.fhir.batch2.test.support.TestJobStep2InputType;
import ca.uhn.hapi.fhir.batch2.test.support.TestJobStep3InputType;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import jakarta.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Specification tests for batch2 storage and event system.
 * These tests are abstract, and do not depend on JPA.
 * Test setups should use the public batch2 api to create scenarios.
 */
public abstract class AbstractIJobPersistenceSpecificationTest {
	private static final Logger ourLog = LoggerFactory.getLogger(AbstractIJobPersistenceSpecificationTest.class);

	public static final String JOB_DEFINITION_ID = "definition-id";
	public static final String TARGET_STEP_ID = "step-id";
	public static final String DEF_CHUNK_ID = "definition-chunkId";
	public static final String STEP_CHUNK_ID = "step-chunkId";
	public static final int JOB_DEF_VER = 1;
	public static final int SEQUENCE_NUMBER = 1;
	public static final String CHUNK_DATA = "{\"key\":\"value\"}";
	public static final String ERROR_MESSAGE_A = "This is an error message: A";
	public static final String ERROR_MESSAGE_B = "This is a different error message: B";
	public static final String ERROR_MESSAGE_C = "This is a different error message: C";

	@Autowired
	private IJobPersistence mySvc;

	@Nested
	class WorkChunkStorage {

		@Test
		public void testStoreAndFetchWorkChunk_NoData() {
			JobInstance instance = createInstance();
			String instanceId = mySvc.storeNewInstance(instance);

			String id = storeWorkChunk(JOB_DEFINITION_ID, TARGET_STEP_ID, instanceId, 0, null);

			WorkChunk chunk = mySvc.onWorkChunkDequeue(id).orElseThrow(IllegalArgumentException::new);
			assertThat(chunk.getData()).isNull();
		}

		@Test
		public void testStoreAndFetchWorkChunk_WithData() {
			JobInstance instance = createInstance();
			String instanceId = mySvc.storeNewInstance(instance);

			String id = storeWorkChunk(JOB_DEFINITION_ID, TARGET_STEP_ID, instanceId, 0, CHUNK_DATA);
			assertThat(id).isNotNull();
			runInTransaction(() -> assertEquals(WorkChunkStatusEnum.QUEUED, freshFetchWorkChunk(id).getStatus()));

			WorkChunk chunk = mySvc.onWorkChunkDequeue(id).orElseThrow(IllegalArgumentException::new);
			assertThat(chunk.getInstanceId()).hasSize(36);
			assertThat(chunk.getJobDefinitionId()).isEqualTo(JOB_DEFINITION_ID);
			assertThat(chunk.getJobDefinitionVersion()).isEqualTo(JOB_DEF_VER);
			assertThat(chunk.getStatus()).isEqualTo(WorkChunkStatusEnum.IN_PROGRESS);
			assertThat(chunk.getData()).isEqualTo(CHUNK_DATA);

			runInTransaction(() -> assertEquals(WorkChunkStatusEnum.IN_PROGRESS, freshFetchWorkChunk(id).getStatus()));
		}

		/**
		 * Should match the diagram in batch2_states.md
		 * @see hapi-fhir-docs/src/main/resources/ca/uhn/hapi/fhir/docs/server_jpa_batch/batch2_states.md
		 */
		@Nested
		class StateTransitions {

			private String myInstanceId;
			private String myChunkId;

			@BeforeEach
			void setUp() {
				JobInstance jobInstance = createInstance();
				myInstanceId = mySvc.storeNewInstance(jobInstance);

			}

			private String createChunk() {
				return storeWorkChunk(JOB_DEFINITION_ID, TARGET_STEP_ID, myInstanceId, 0, CHUNK_DATA);
			}

			@Test
			public void chunkCreation_isQueued() {

				myChunkId = createChunk();

				WorkChunk fetchedWorkChunk = freshFetchWorkChunk(myChunkId);
				assertThat(fetchedWorkChunk.getStatus()).as("New chunks are QUEUED").isEqualTo(WorkChunkStatusEnum.QUEUED);
			}

			@Test
			public void chunkReceived_queuedToInProgress() {

				myChunkId = createChunk();

				// the worker has received the chunk, and marks it started.
				WorkChunk chunk = mySvc.onWorkChunkDequeue(myChunkId).orElseThrow(IllegalArgumentException::new);

				assertThat(chunk.getStatus()).isEqualTo(WorkChunkStatusEnum.IN_PROGRESS);
				assertThat(chunk.getData()).isEqualTo(CHUNK_DATA);

				// verify the db was updated too
				WorkChunk fetchedWorkChunk = freshFetchWorkChunk(myChunkId);
				assertThat(fetchedWorkChunk.getStatus()).isEqualTo(WorkChunkStatusEnum.IN_PROGRESS);
			}

			@Nested
			class InProgressActions {
				@BeforeEach
				void setUp() {
					// setup - the worker has received the chunk, and has marked it IN_PROGRESS.
					myChunkId = createChunk();
					mySvc.onWorkChunkDequeue(myChunkId);
				}

				@Test
				public void processingOk_inProgressToSuccess_clearsDataSavesRecordCount() {

					// execution ok
					mySvc.onWorkChunkCompletion(new WorkChunkCompletionEvent(myChunkId, 3, 0));

					// verify the db was updated
					var workChunkEntity = freshFetchWorkChunk(myChunkId);
					assertThat(workChunkEntity.getStatus()).isEqualTo(WorkChunkStatusEnum.COMPLETED);
					assertThat(workChunkEntity.getData()).isNull();
					assertThat(workChunkEntity.getRecordsProcessed()).isEqualTo(3);
					assertThat(workChunkEntity.getErrorMessage()).isNull();
					assertThat(workChunkEntity.getErrorCount()).isEqualTo(0);
				}

				@Test
				public void processingRetryableError_inProgressToError_bumpsCountRecordsMessage() {

					// execution had a retryable error
					mySvc.onWorkChunkError(new WorkChunkErrorEvent(myChunkId, ERROR_MESSAGE_A));

					// verify the db was updated
					var workChunkEntity = freshFetchWorkChunk(myChunkId);
					assertThat(workChunkEntity.getStatus()).isEqualTo(WorkChunkStatusEnum.ERRORED);
					assertThat(workChunkEntity.getErrorMessage()).isEqualTo(ERROR_MESSAGE_A);
					assertThat(workChunkEntity.getErrorCount()).isEqualTo(1);
				}

				@Test
				public void processingFailure_inProgressToFailed() {

					// execution had a failure
					mySvc.onWorkChunkFailed(myChunkId, "some error");

					// verify the db was updated
					var workChunkEntity = freshFetchWorkChunk(myChunkId);
					assertThat(workChunkEntity.getStatus()).isEqualTo(WorkChunkStatusEnum.FAILED);
					assertThat(workChunkEntity.getErrorMessage()).isEqualTo("some error");
				}
			}

			@Nested
			class ErrorActions {
				public static final String FIRST_ERROR_MESSAGE = ERROR_MESSAGE_A;
				@BeforeEach
				void setUp() {
					// setup - the worker has received the chunk, and has marked it IN_PROGRESS.
					myChunkId = createChunk();
					mySvc.onWorkChunkDequeue(myChunkId);
					// execution had a retryable error
					mySvc.onWorkChunkError(new WorkChunkErrorEvent(myChunkId, FIRST_ERROR_MESSAGE));
				}

				/**
				 * The consumer will retry after a retryable error is thrown
				 */
				@Test
				void errorRetry_errorToInProgress() {

					// when consumer restarts chunk
					WorkChunk chunk = mySvc.onWorkChunkDequeue(myChunkId).orElseThrow(IllegalArgumentException::new);

					// then
					assertThat(chunk.getStatus()).isEqualTo(WorkChunkStatusEnum.IN_PROGRESS);

					// verify the db state, error message, and error count
					var workChunkEntity = freshFetchWorkChunk(myChunkId);
					assertThat(workChunkEntity.getStatus()).isEqualTo(WorkChunkStatusEnum.IN_PROGRESS);
					assertThat(workChunkEntity.getErrorMessage()).as("Original error message kept").isEqualTo(FIRST_ERROR_MESSAGE);
					assertThat(workChunkEntity.getErrorCount()).as("error count kept").isEqualTo(1);
				}

				@Test
				void errorRetry_repeatError_increasesErrorCount() {
					// setup - the consumer is re-trying, and marks it IN_PROGRESS
					mySvc.onWorkChunkDequeue(myChunkId);


					// when another error happens
					mySvc.onWorkChunkError(new WorkChunkErrorEvent(myChunkId, ERROR_MESSAGE_B));


					// verify the state, new message, and error count
					var workChunkEntity = freshFetchWorkChunk(myChunkId);
					assertThat(workChunkEntity.getStatus()).isEqualTo(WorkChunkStatusEnum.ERRORED);
					assertThat(workChunkEntity.getErrorMessage()).as("new error message").isEqualTo(ERROR_MESSAGE_B);
					assertThat(workChunkEntity.getErrorCount()).as("error count inc").isEqualTo(2);
				}

				@Test
				void errorThenRetryAndComplete_addsErrorCounts() {
					// setup - the consumer is re-trying, and marks it IN_PROGRESS
					mySvc.onWorkChunkDequeue(myChunkId);

					// then it completes ok.
					mySvc.onWorkChunkCompletion(new WorkChunkCompletionEvent(myChunkId, 3, 1));

					// verify the state, new message, and error count
					var workChunkEntity = freshFetchWorkChunk(myChunkId);
					assertThat(workChunkEntity.getStatus()).isEqualTo(WorkChunkStatusEnum.COMPLETED);
					assertThat(workChunkEntity.getErrorMessage()).as("Error message kept.").isEqualTo(FIRST_ERROR_MESSAGE);
					assertThat(workChunkEntity.getErrorCount()).as("error combined with earlier error").isEqualTo(2);
				}

				@Test
				void errorRetry_maxErrors_movesToFailed() {
					// we start with 1 error already

					// 2nd try
					mySvc.onWorkChunkDequeue(myChunkId);
					mySvc.onWorkChunkError(new WorkChunkErrorEvent(myChunkId, ERROR_MESSAGE_B));
					var chunk = freshFetchWorkChunk(myChunkId);
					assertThat(chunk.getStatus()).isEqualTo(WorkChunkStatusEnum.ERRORED);
					assertThat(chunk.getErrorCount()).isEqualTo(2);

					// 3rd try
					mySvc.onWorkChunkDequeue(myChunkId);
					mySvc.onWorkChunkError(new WorkChunkErrorEvent(myChunkId, ERROR_MESSAGE_B));
					chunk = freshFetchWorkChunk(myChunkId);
					assertThat(chunk.getStatus()).isEqualTo(WorkChunkStatusEnum.ERRORED);
					assertThat(chunk.getErrorCount()).isEqualTo(3);

					// 4th try
					mySvc.onWorkChunkDequeue(myChunkId);
					mySvc.onWorkChunkError(new WorkChunkErrorEvent(myChunkId, ERROR_MESSAGE_C));
					chunk = freshFetchWorkChunk(myChunkId);
					assertThat(chunk.getStatus()).isEqualTo(WorkChunkStatusEnum.FAILED);
					assertThat(chunk.getErrorCount()).isEqualTo(4);
					assertThat(chunk.getErrorMessage()).as("Error message contains last error").contains(ERROR_MESSAGE_C);
					assertThat(chunk.getErrorMessage()).as("Error message contains error count and complaint").contains("many errors: 4");
				}
			}
		}

		@Test
		public void testMarkChunkAsCompleted_Success() {
			JobInstance instance = createInstance();
			String instanceId = mySvc.storeNewInstance(instance);
			String chunkId = storeWorkChunk(DEF_CHUNK_ID, STEP_CHUNK_ID, instanceId, SEQUENCE_NUMBER, CHUNK_DATA);
			assertThat(chunkId).isNotNull();

			runInTransaction(() -> assertEquals(WorkChunkStatusEnum.QUEUED, freshFetchWorkChunk(chunkId).getStatus()));

			sleepUntilTimeChanges();

			WorkChunk chunk = mySvc.onWorkChunkDequeue(chunkId).orElseThrow(IllegalArgumentException::new);
			assertThat(chunk.getSequence()).isEqualTo(SEQUENCE_NUMBER);
			assertThat(chunk.getStatus()).isEqualTo(WorkChunkStatusEnum.IN_PROGRESS);
			assertThat(chunk.getCreateTime()).isNotNull();
			assertThat(chunk.getStartTime()).isNotNull();
			assertThat(chunk.getEndTime()).isNull();
			assertThat(chunk.getRecordsProcessed()).isNull();
			assertThat(chunk.getData()).isNotNull();
			runInTransaction(() -> assertEquals(WorkChunkStatusEnum.IN_PROGRESS, freshFetchWorkChunk(chunkId).getStatus()));

			sleepUntilTimeChanges();

			runInTransaction(() -> mySvc.onWorkChunkCompletion(new WorkChunkCompletionEvent(chunkId, 50, 0)));

			WorkChunk entity = freshFetchWorkChunk(chunkId);
			assertThat(entity.getStatus()).isEqualTo(WorkChunkStatusEnum.COMPLETED);
			assertThat(entity.getRecordsProcessed()).isEqualTo(50);
			assertThat(entity.getCreateTime()).isNotNull();
			assertThat(entity.getStartTime()).isNotNull();
			assertThat(entity.getEndTime()).isNotNull();
			assertThat(entity.getData()).isNull();
			assertThat(entity.getCreateTime().getTime() < entity.getStartTime().getTime()).isTrue();
			assertThat(entity.getStartTime().getTime() < entity.getEndTime().getTime()).isTrue();
		}


		@Test
		public void testMarkChunkAsCompleted_Error() {
			JobInstance instance = createInstance();
			String instanceId = mySvc.storeNewInstance(instance);
			String chunkId = storeWorkChunk(DEF_CHUNK_ID, STEP_CHUNK_ID, instanceId, SEQUENCE_NUMBER, null);
			assertThat(chunkId).isNotNull();

			runInTransaction(() -> assertEquals(WorkChunkStatusEnum.QUEUED, freshFetchWorkChunk(chunkId).getStatus()));

			sleepUntilTimeChanges();

			WorkChunk chunk = mySvc.onWorkChunkDequeue(chunkId).orElseThrow(IllegalArgumentException::new);
			assertThat(chunk.getSequence()).isEqualTo(SEQUENCE_NUMBER);
			assertThat(chunk.getStatus()).isEqualTo(WorkChunkStatusEnum.IN_PROGRESS);

			sleepUntilTimeChanges();

			WorkChunkErrorEvent request = new WorkChunkErrorEvent(chunkId, ERROR_MESSAGE_A);
			mySvc.onWorkChunkError(request);
			runInTransaction(() -> {
				WorkChunk entity = freshFetchWorkChunk(chunkId);
				assertEquals(WorkChunkStatusEnum.ERRORED, entity.getStatus());
				assertEquals(ERROR_MESSAGE_A, entity.getErrorMessage());
				assertNotNull(entity.getCreateTime());
				assertNotNull(entity.getStartTime());
				assertNotNull(entity.getEndTime());
				assertEquals(1, entity.getErrorCount());
				assertTrue(entity.getCreateTime().getTime() < entity.getStartTime().getTime());
				assertTrue(entity.getStartTime().getTime() < entity.getEndTime().getTime());
			});

			// Mark errored again

			WorkChunkErrorEvent request2 = new WorkChunkErrorEvent(chunkId, "This is an error message 2");
			mySvc.onWorkChunkError(request2);
			runInTransaction(() -> {
				WorkChunk entity = freshFetchWorkChunk(chunkId);
				assertEquals(WorkChunkStatusEnum.ERRORED, entity.getStatus());
				assertEquals("This is an error message 2", entity.getErrorMessage());
				assertNotNull(entity.getCreateTime());
				assertNotNull(entity.getStartTime());
				assertNotNull(entity.getEndTime());
				assertEquals(2, entity.getErrorCount());
				assertTrue(entity.getCreateTime().getTime() < entity.getStartTime().getTime());
				assertTrue(entity.getStartTime().getTime() < entity.getEndTime().getTime());
			});

			List<WorkChunk> chunks = ImmutableList.copyOf(mySvc.fetchAllWorkChunksIterator(instanceId, true));
			assertThat(chunks).hasSize(1);
			assertThat(chunks.get(0).getErrorCount()).isEqualTo(2);
		}

		@Test
		public void testMarkChunkAsCompleted_Fail() {
			JobInstance instance = createInstance();
			String instanceId = mySvc.storeNewInstance(instance);
			String chunkId = storeWorkChunk(DEF_CHUNK_ID, STEP_CHUNK_ID, instanceId, SEQUENCE_NUMBER, null);
			assertThat(chunkId).isNotNull();

			runInTransaction(() -> assertEquals(WorkChunkStatusEnum.QUEUED, freshFetchWorkChunk(chunkId).getStatus()));

			sleepUntilTimeChanges();

			WorkChunk chunk = mySvc.onWorkChunkDequeue(chunkId).orElseThrow(IllegalArgumentException::new);
			assertThat(chunk.getSequence()).isEqualTo(SEQUENCE_NUMBER);
			assertThat(chunk.getStatus()).isEqualTo(WorkChunkStatusEnum.IN_PROGRESS);

			sleepUntilTimeChanges();

			mySvc.onWorkChunkFailed(chunkId, "This is an error message");
			runInTransaction(() -> {
				WorkChunk entity = freshFetchWorkChunk(chunkId);
				assertEquals(WorkChunkStatusEnum.FAILED, entity.getStatus());
				assertEquals("This is an error message", entity.getErrorMessage());
				assertNotNull(entity.getCreateTime());
				assertNotNull(entity.getStartTime());
				assertNotNull(entity.getEndTime());
				assertTrue(entity.getCreateTime().getTime() < entity.getStartTime().getTime());
				assertTrue(entity.getStartTime().getTime() < entity.getEndTime().getTime());
			});
		}

		@Test
		public void markWorkChunksWithStatusAndWipeData_marksMultipleChunksWithStatus_asExpected() {
			JobInstance instance = createInstance();
			String instanceId = mySvc.storeNewInstance(instance);
			ArrayList<String> chunkIds = new ArrayList<>();
			for (int i = 0; i < 10; i++) {
				WorkChunkCreateEvent chunk = new WorkChunkCreateEvent(
					"defId",
					1,
					"stepId",
					instanceId,
					0,
					"{}"
				);
				String id = mySvc.onWorkChunkCreate(chunk);
				chunkIds.add(id);
			}

			runInTransaction(() -> mySvc.markWorkChunksWithStatusAndWipeData(instance.getInstanceId(), chunkIds, WorkChunkStatusEnum.COMPLETED, null));

			Iterator<WorkChunk> reducedChunks = mySvc.fetchAllWorkChunksIterator(instanceId, true);

			while (reducedChunks.hasNext()) {
				WorkChunk reducedChunk = reducedChunks.next();
				assertThat(chunkIds).contains(reducedChunk);
				assertThat(reducedChunk.getStatus()).isEqualTo(WorkChunkStatusEnum.COMPLETED);
			}
		}
	}

	/**
	 * Test
	 *  * @see hapi-fhir-docs/src/main/resources/ca/uhn/hapi/fhir/docs/server_jpa_batch/batch2_states.md
	 */
	@Nested
	class InstanceStateTransitions {
		
		@Test
		void createInstance_createsInQueuedWithChunk() {
		    // given
			JobDefinition<?> jd = withJobDefinition();

		    // when
			IJobPersistence.CreateResult createResult =
			newTxTemplate().execute(status->
				mySvc.onCreateWithFirstChunk(jd, "{}"));

			// then
			ourLog.info("job and chunk created {}", createResult);
			assertThat(createResult).isNotNull();
			assertThat(createResult.jobInstanceId).isNotEmpty();
			assertThat(createResult.workChunkId).isNotEmpty();

			JobInstance jobInstance = freshFetchJobInstance(createResult.jobInstanceId);
			assertThat(jobInstance.getStatus()).isEqualTo(StatusEnum.QUEUED);
			assertThat(jobInstance.getParameters()).isEqualTo("{}");

			WorkChunk firstChunk = freshFetchWorkChunk(createResult.workChunkId);
			assertThat(firstChunk.getStatus()).isEqualTo(WorkChunkStatusEnum.QUEUED);
			assertThat(firstChunk.getData()).as("First chunk data is null - only uses parameters").isNull();
		}

		@Test
		void testCreateInstance_firstChunkDequeued_movesToInProgress() {
		    // given
			JobDefinition<?> jd = withJobDefinition();
			IJobPersistence.CreateResult createResult = newTxTemplate().execute(status->
					mySvc.onCreateWithFirstChunk(jd, "{}"));
			assertThat(createResult).isNotNull();

			// when
			newTxTemplate().execute(status -> mySvc.onChunkDequeued(createResult.jobInstanceId));

		    // then
			JobInstance jobInstance = freshFetchJobInstance(createResult.jobInstanceId);
			assertThat(jobInstance.getStatus()).isEqualTo(StatusEnum.IN_PROGRESS);
		}

		

		@ParameterizedTest
		@EnumSource(StatusEnum.class)
		void cancelRequest_cancelsJob_whenNotFinalState(StatusEnum theState) {
			// given
			JobInstance cancelledInstance = createInstance();
			cancelledInstance.setStatus(theState);
			String instanceId1 = mySvc.storeNewInstance(cancelledInstance);
			mySvc.cancelInstance(instanceId1);

			JobInstance normalInstance = createInstance();
			normalInstance.setStatus(theState);
			String instanceId2 = mySvc.storeNewInstance(normalInstance);

			JobDefinitionRegistry jobDefinitionRegistry = new JobDefinitionRegistry();
			jobDefinitionRegistry.addJobDefinitionIfNotRegistered(withJobDefinition());


			// when
			runInTransaction(()-> new JobInstanceProcessor(mySvc, null, instanceId1, new JobChunkProgressAccumulator(), null, jobDefinitionRegistry)
				.process());


			// then
			JobInstance freshInstance1 = mySvc.fetchInstance(instanceId1).orElseThrow();
			if (theState.isCancellable()) {
				assertThat(freshInstance1.getStatus()).as("cancel request processed").isEqualTo(StatusEnum.CANCELLED);
				assertThat(freshInstance1.getErrorMessage()).contains("Job instance cancelled");
			} else {
				assertThat(freshInstance1.getStatus()).as("cancel request ignored - state unchanged").isEqualTo(theState);
				assertThat(freshInstance1.getErrorMessage()).as("no error message").isNull();
			}
			JobInstance freshInstance2 = mySvc.fetchInstance(instanceId2).orElseThrow();
			assertThat(freshInstance2.getStatus()).as("cancel request ignored - cancelled not set").isEqualTo(theState);
		}
	}

	@Test
	void testDeleteChunksAndMarkInstanceAsChunksPurged_doesWhatItSays() {
	    // given
		JobDefinition<?> jd = withJobDefinition();
		IJobPersistence.CreateResult createResult = newTxTemplate().execute(status->
				mySvc.onCreateWithFirstChunk(jd, "{}"));
		String instanceId = createResult.jobInstanceId;
		for (int i = 0; i < 10; i++) {
			storeWorkChunk(JOB_DEFINITION_ID, TARGET_STEP_ID, instanceId, i, CHUNK_DATA);
		}
		JobInstance readback = freshFetchJobInstance(instanceId);
		assertThat(readback.isWorkChunksPurged()).isFalse();
		assertThat(mySvc.fetchAllWorkChunksIterator(instanceId, true).hasNext()).as("has chunk").isTrue();

		// when
		mySvc.deleteChunksAndMarkInstanceAsChunksPurged(instanceId);

	    // then
		readback = freshFetchJobInstance(instanceId);
		assertThat(readback.isWorkChunksPurged()).as("purged set").isTrue();
		assertThat(mySvc.fetchAllWorkChunksIterator(instanceId, true).hasNext()).as("chunks gone").isFalse();
	}
	
	@Test
	void testInstanceUpdate_modifierApplied() {
	    // given
		String instanceId = mySvc.storeNewInstance(createInstance());

		// when
		mySvc.updateInstance(instanceId, instance ->{
			instance.setErrorCount(42);
			return true;
		});

	    // then
		JobInstance jobInstance = freshFetchJobInstance(instanceId);
		assertThat(jobInstance.getErrorCount()).isEqualTo(42);
	}

	@Test
	void testInstanceUpdate_modifierNotAppliedWhenPredicateReturnsFalse() {
		// given
		JobInstance instance1 = createInstance();
		boolean initialValue = true;
		instance1.setFastTracking(initialValue);
		String instanceId = mySvc.storeNewInstance(instance1);

		// when
		mySvc.updateInstance(instanceId, instance ->{
			instance.setFastTracking(false);
			return false;
		});

		// then
		JobInstance jobInstance = freshFetchJobInstance(instanceId);
		assertThat(jobInstance.isFastTracking()).isEqualTo(initialValue);
	}

	private JobDefinition<TestJobParameters> withJobDefinition() {
		return JobDefinition.newBuilder()
			.setJobDefinitionId(JOB_DEFINITION_ID)
			.setJobDefinitionVersion(JOB_DEF_VER)
			.setJobDescription("A job description")
			.setParametersType(TestJobParameters.class)
			.addFirstStep(TARGET_STEP_ID, "the first step", TestJobStep2InputType.class, (theStepExecutionDetails, theDataSink) -> new RunOutcome(0))
			.addIntermediateStep("2nd-step-id", "the second step", TestJobStep3InputType.class, (theStepExecutionDetails, theDataSink) -> new RunOutcome(0))
			.addLastStep("last-step-id", "the final step", (theStepExecutionDetails, theDataSink) -> new RunOutcome(0))
			.build();
	}


	@Nonnull
	private JobInstance createInstance() {
		JobInstance instance = new JobInstance();
		instance.setJobDefinitionId(JOB_DEFINITION_ID);
		instance.setStatus(StatusEnum.QUEUED);
		instance.setJobDefinitionVersion(JOB_DEF_VER);
		instance.setParameters(CHUNK_DATA);
		instance.setReport("TEST");
		return instance;
	}

	private String storeWorkChunk(String theJobDefinitionId, String theTargetStepId, String theInstanceId, int theSequence, String theSerializedData) {
		WorkChunkCreateEvent batchWorkChunk = new WorkChunkCreateEvent(theJobDefinitionId, JOB_DEF_VER, theTargetStepId, theInstanceId, theSequence, theSerializedData);
		return mySvc.onWorkChunkCreate(batchWorkChunk);
	}


	protected abstract PlatformTransactionManager getTxManager();
	protected abstract WorkChunk freshFetchWorkChunk(String theChunkId);
	protected JobInstance freshFetchJobInstance(String theInstanceId) {
		return runInTransaction(() -> mySvc.fetchInstance(theInstanceId).orElseThrow());
	}

	public TransactionTemplate newTxTemplate() {
		TransactionTemplate retVal = new TransactionTemplate(getTxManager());
		retVal.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		retVal.afterPropertiesSet();
		return retVal;
	}

	public void runInTransaction(Runnable theRunnable) {
		newTxTemplate().execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
				theRunnable.run();
			}
		});
	}

	public <T> T runInTransaction(Callable<T> theRunnable) {
		return newTxTemplate().execute(t -> {
			try {
				return theRunnable.call();
			} catch (Exception theE) {
				throw new InternalErrorException(theE);
			}
		});
	}


	/**
	 * Sleep until at least 1 ms has elapsed
	 */
	public void sleepUntilTimeChanges() {
		StopWatch sw = new StopWatch();
		await().until(() -> sw.getMillis() > 0);
	}



}
