/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 specification tests
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

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
			assertNull(chunk.getData());
		}

		@Test
		public void testStoreAndFetchWorkChunk_WithData() {
			JobInstance instance = createInstance();
			String instanceId = mySvc.storeNewInstance(instance);

			String id = storeWorkChunk(JOB_DEFINITION_ID, TARGET_STEP_ID, instanceId, 0, CHUNK_DATA);
			assertNotNull(id);
			runInTransaction(() -> assertEquals(WorkChunkStatusEnum.QUEUED, freshFetchWorkChunk(id).getStatus()));

			WorkChunk chunk = mySvc.onWorkChunkDequeue(id).orElseThrow(IllegalArgumentException::new);
			assertEquals(36, chunk.getInstanceId().length());
			assertEquals(JOB_DEFINITION_ID, chunk.getJobDefinitionId());
			assertEquals(JOB_DEF_VER, chunk.getJobDefinitionVersion());
			assertEquals(WorkChunkStatusEnum.IN_PROGRESS, chunk.getStatus());
			assertEquals(CHUNK_DATA, chunk.getData());

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
				assertEquals(WorkChunkStatusEnum.QUEUED, fetchedWorkChunk.getStatus(), "New chunks are QUEUED");
			}

			@Test
			public void chunkReceived_queuedToInProgress() {

				myChunkId = createChunk();

				// the worker has received the chunk, and marks it started.
				WorkChunk chunk = mySvc.onWorkChunkDequeue(myChunkId).orElseThrow(IllegalArgumentException::new);

				assertEquals(WorkChunkStatusEnum.IN_PROGRESS, chunk.getStatus());
				assertEquals(CHUNK_DATA, chunk.getData());

				// verify the db was updated too
				WorkChunk fetchedWorkChunk = freshFetchWorkChunk(myChunkId);
				assertEquals(WorkChunkStatusEnum.IN_PROGRESS, fetchedWorkChunk.getStatus());
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
					assertEquals(WorkChunkStatusEnum.COMPLETED, workChunkEntity.getStatus());
					assertNull(workChunkEntity.getData());
					assertEquals(3, workChunkEntity.getRecordsProcessed());
					assertNull(workChunkEntity.getErrorMessage());
					assertEquals(0, workChunkEntity.getErrorCount());
				}

				@Test
				public void processingRetryableError_inProgressToError_bumpsCountRecordsMessage() {

					// execution had a retryable error
					mySvc.onWorkChunkError(new WorkChunkErrorEvent(myChunkId, ERROR_MESSAGE_A));

					// verify the db was updated
					var workChunkEntity = freshFetchWorkChunk(myChunkId);
					assertEquals(WorkChunkStatusEnum.ERRORED, workChunkEntity.getStatus());
					assertEquals(ERROR_MESSAGE_A, workChunkEntity.getErrorMessage());
					assertEquals(1, workChunkEntity.getErrorCount());
				}

				@Test
				public void processingFailure_inProgressToFailed() {

					// execution had a failure
					mySvc.onWorkChunkFailed(myChunkId, "some error");

					// verify the db was updated
					var workChunkEntity = freshFetchWorkChunk(myChunkId);
					assertEquals(WorkChunkStatusEnum.FAILED, workChunkEntity.getStatus());
					assertEquals("some error", workChunkEntity.getErrorMessage());
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
					assertEquals(WorkChunkStatusEnum.IN_PROGRESS, chunk.getStatus());

					// verify the db state, error message, and error count
					var workChunkEntity = freshFetchWorkChunk(myChunkId);
					assertEquals(WorkChunkStatusEnum.IN_PROGRESS, workChunkEntity.getStatus());
					assertEquals(FIRST_ERROR_MESSAGE, workChunkEntity.getErrorMessage(), "Original error message kept");
					assertEquals(1, workChunkEntity.getErrorCount(), "error count kept");
				}

				@Test
				void errorRetry_repeatError_increasesErrorCount() {
					// setup - the consumer is re-trying, and marks it IN_PROGRESS
					mySvc.onWorkChunkDequeue(myChunkId);


					// when another error happens
					mySvc.onWorkChunkError(new WorkChunkErrorEvent(myChunkId, ERROR_MESSAGE_B));


					// verify the state, new message, and error count
					var workChunkEntity = freshFetchWorkChunk(myChunkId);
					assertEquals(WorkChunkStatusEnum.ERRORED, workChunkEntity.getStatus());
					assertEquals(ERROR_MESSAGE_B, workChunkEntity.getErrorMessage(), "new error message");
					assertEquals(2, workChunkEntity.getErrorCount(), "error count inc");
				}

				@Test
				void errorThenRetryAndComplete_addsErrorCounts() {
					// setup - the consumer is re-trying, and marks it IN_PROGRESS
					mySvc.onWorkChunkDequeue(myChunkId);

					// then it completes ok.
					mySvc.onWorkChunkCompletion(new WorkChunkCompletionEvent(myChunkId, 3, 1));

					// verify the state, new message, and error count
					var workChunkEntity = freshFetchWorkChunk(myChunkId);
					assertEquals(WorkChunkStatusEnum.COMPLETED, workChunkEntity.getStatus());
					assertEquals(FIRST_ERROR_MESSAGE, workChunkEntity.getErrorMessage(), "Error message kept.");
					assertEquals(2, workChunkEntity.getErrorCount(), "error combined with earlier error");
				}

				@Test
				void errorRetry_maxErrors_movesToFailed() {
					// we start with 1 error already

					// 2nd try
					mySvc.onWorkChunkDequeue(myChunkId);
					mySvc.onWorkChunkError(new WorkChunkErrorEvent(myChunkId, ERROR_MESSAGE_B));
					var chunk = freshFetchWorkChunk(myChunkId);
					assertEquals(WorkChunkStatusEnum.ERRORED, chunk.getStatus());
					assertEquals(2, chunk.getErrorCount());

					// 3rd try
					mySvc.onWorkChunkDequeue(myChunkId);
					mySvc.onWorkChunkError(new WorkChunkErrorEvent(myChunkId, ERROR_MESSAGE_B));
					chunk = freshFetchWorkChunk(myChunkId);
					assertEquals(WorkChunkStatusEnum.ERRORED, chunk.getStatus());
					assertEquals(3, chunk.getErrorCount());

					// 4th try
					mySvc.onWorkChunkDequeue(myChunkId);
					mySvc.onWorkChunkError(new WorkChunkErrorEvent(myChunkId, ERROR_MESSAGE_C));
					chunk = freshFetchWorkChunk(myChunkId);
					assertEquals(WorkChunkStatusEnum.FAILED, chunk.getStatus());
					assertEquals(4, chunk.getErrorCount());
					assertThat("Error message contains last error", chunk.getErrorMessage(), containsString(ERROR_MESSAGE_C));
					assertThat("Error message contains error count and complaint", chunk.getErrorMessage(), containsString("many errors: 4"));
				}
			}
		}

		@Test
		public void testFetchChunks() {
			JobInstance instance = createInstance();
			String instanceId = mySvc.storeNewInstance(instance);

			List<String> ids = new ArrayList<>();
			for (int i = 0; i < 10; i++) {
				String id = storeWorkChunk(JOB_DEFINITION_ID, TARGET_STEP_ID, instanceId, i, CHUNK_DATA);
				ids.add(id);
			}

			List<WorkChunk> chunks = mySvc.fetchWorkChunksWithoutData(instanceId, 3, 0);
			assertNull(chunks.get(0).getData());
			assertNull(chunks.get(1).getData());
			assertNull(chunks.get(2).getData());
			assertThat(chunks.stream().map(WorkChunk::getId).collect(Collectors.toList()),
				contains(ids.get(0), ids.get(1), ids.get(2)));

			chunks = mySvc.fetchWorkChunksWithoutData(instanceId, 3, 1);
			assertThat(chunks.stream().map(WorkChunk::getId).collect(Collectors.toList()),
				contains(ids.get(3), ids.get(4), ids.get(5)));

			chunks = mySvc.fetchWorkChunksWithoutData(instanceId, 3, 2);
			assertThat(chunks.stream().map(WorkChunk::getId).collect(Collectors.toList()),
				contains(ids.get(6), ids.get(7), ids.get(8)));

			chunks = mySvc.fetchWorkChunksWithoutData(instanceId, 3, 3);
			assertThat(chunks.stream().map(WorkChunk::getId).collect(Collectors.toList()),
				contains(ids.get(9)));

			chunks = mySvc.fetchWorkChunksWithoutData(instanceId, 3, 4);
			assertThat(chunks.stream().map(WorkChunk::getId).collect(Collectors.toList()),
				empty());
		}


		@Test
		public void testMarkChunkAsCompleted_Success() {
			JobInstance instance = createInstance();
			String instanceId = mySvc.storeNewInstance(instance);
			String chunkId = storeWorkChunk(DEF_CHUNK_ID, STEP_CHUNK_ID, instanceId, SEQUENCE_NUMBER, CHUNK_DATA);
			assertNotNull(chunkId);

			runInTransaction(() -> assertEquals(WorkChunkStatusEnum.QUEUED, freshFetchWorkChunk(chunkId).getStatus()));

			sleepUntilTimeChanges();

			WorkChunk chunk = mySvc.onWorkChunkDequeue(chunkId).orElseThrow(IllegalArgumentException::new);
			assertEquals(SEQUENCE_NUMBER, chunk.getSequence());
			assertEquals(WorkChunkStatusEnum.IN_PROGRESS, chunk.getStatus());
			assertNotNull(chunk.getCreateTime());
			assertNotNull(chunk.getStartTime());
			assertNull(chunk.getEndTime());
			assertNull(chunk.getRecordsProcessed());
			assertNotNull(chunk.getData());
			runInTransaction(() -> assertEquals(WorkChunkStatusEnum.IN_PROGRESS, freshFetchWorkChunk(chunkId).getStatus()));

			sleepUntilTimeChanges();

			runInTransaction(() -> mySvc.onWorkChunkCompletion(new WorkChunkCompletionEvent(chunkId, 50, 0)));

			WorkChunk entity = freshFetchWorkChunk(chunkId);
			assertEquals(WorkChunkStatusEnum.COMPLETED, entity.getStatus());
			assertEquals(50, entity.getRecordsProcessed());
			assertNotNull(entity.getCreateTime());
			assertNotNull(entity.getStartTime());
			assertNotNull(entity.getEndTime());
			assertNull(entity.getData());
			assertTrue(entity.getCreateTime().getTime() < entity.getStartTime().getTime());
			assertTrue(entity.getStartTime().getTime() < entity.getEndTime().getTime());
		}


		@Test
		public void testMarkChunkAsCompleted_Error() {
			JobInstance instance = createInstance();
			String instanceId = mySvc.storeNewInstance(instance);
			String chunkId = storeWorkChunk(DEF_CHUNK_ID, STEP_CHUNK_ID, instanceId, SEQUENCE_NUMBER, null);
			assertNotNull(chunkId);

			runInTransaction(() -> assertEquals(WorkChunkStatusEnum.QUEUED, freshFetchWorkChunk(chunkId).getStatus()));

			sleepUntilTimeChanges();

			WorkChunk chunk = mySvc.onWorkChunkDequeue(chunkId).orElseThrow(IllegalArgumentException::new);
			assertEquals(SEQUENCE_NUMBER, chunk.getSequence());
			assertEquals(WorkChunkStatusEnum.IN_PROGRESS, chunk.getStatus());

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

			List<WorkChunk> chunks = mySvc.fetchWorkChunksWithoutData(instanceId, 100, 0);
			assertEquals(1, chunks.size());
			assertEquals(2, chunks.get(0).getErrorCount());
		}

		@Test
		public void testMarkChunkAsCompleted_Fail() {
			JobInstance instance = createInstance();
			String instanceId = mySvc.storeNewInstance(instance);
			String chunkId = storeWorkChunk(DEF_CHUNK_ID, STEP_CHUNK_ID, instanceId, SEQUENCE_NUMBER, null);
			assertNotNull(chunkId);

			runInTransaction(() -> assertEquals(WorkChunkStatusEnum.QUEUED, freshFetchWorkChunk(chunkId).getStatus()));

			sleepUntilTimeChanges();

			WorkChunk chunk = mySvc.onWorkChunkDequeue(chunkId).orElseThrow(IllegalArgumentException::new);
			assertEquals(SEQUENCE_NUMBER, chunk.getSequence());
			assertEquals(WorkChunkStatusEnum.IN_PROGRESS, chunk.getStatus());

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
				assertTrue(chunkIds.contains(reducedChunk.getId()));
				assertEquals(WorkChunkStatusEnum.COMPLETED, reducedChunk.getStatus());
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
			assertNotNull(createResult);
			assertThat(createResult.jobInstanceId, not(emptyString()));
			assertThat(createResult.workChunkId, not(emptyString()));

			JobInstance jobInstance = freshFetchJobInstance(createResult.jobInstanceId);
			assertThat(jobInstance.getStatus(), equalTo(StatusEnum.QUEUED));
			assertThat(jobInstance.getParameters(), equalTo("{}"));

			WorkChunk firstChunk = freshFetchWorkChunk(createResult.workChunkId);
			assertThat(firstChunk.getStatus(), equalTo(WorkChunkStatusEnum.QUEUED));
			assertNull(firstChunk.getData(), "First chunk data is null - only uses parameters");
		}

		@Test
		void testCreateInstance_firstChunkDequeued_movesToInProgress() {
		    // given
			JobDefinition<?> jd = withJobDefinition();
			IJobPersistence.CreateResult createResult = newTxTemplate().execute(status->
					mySvc.onCreateWithFirstChunk(jd, "{}"));
			assertNotNull(createResult);

			// when
			newTxTemplate().execute(status -> mySvc.onChunkDequeued(createResult.jobInstanceId));

		    // then
			JobInstance jobInstance = freshFetchJobInstance(createResult.jobInstanceId);
			assertThat(jobInstance.getStatus(), equalTo(StatusEnum.IN_PROGRESS));
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
				assertEquals(StatusEnum.CANCELLED, freshInstance1.getStatus(), "cancel request processed");
				assertThat(freshInstance1.getErrorMessage(), containsString("Job instance cancelled"));
			} else {
				assertEquals(theState, freshInstance1.getStatus(), "cancel request ignored - state unchanged");
				assertNull(freshInstance1.getErrorMessage(), "no error message");
			}
			JobInstance freshInstance2 = mySvc.fetchInstance(instanceId2).orElseThrow();
			assertEquals(theState, freshInstance2.getStatus(), "cancel request ignored - cancelled not set");
		}
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
		assertEquals(42, jobInstance.getErrorCount());
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
		assertEquals(initialValue, jobInstance.isFastTracking());
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
