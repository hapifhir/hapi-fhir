package ca.uhn.fhir.jpa.batch2;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.IWorkChunkPersistence;
import ca.uhn.fhir.batch2.api.JobOperationResultJson;
import ca.uhn.fhir.batch2.channel.BatchJobSender;
import ca.uhn.fhir.batch2.coordinator.BatchWorkChunk;
import ca.uhn.fhir.batch2.jobs.imprt.NdJsonFileJson;
import ca.uhn.fhir.batch2.maintenance.JobChunkProgressAccumulator;
import ca.uhn.fhir.batch2.maintenance.JobInstanceProcessor;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunkErrorEvent;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import ca.uhn.fhir.jpa.dao.data.IBatch2JobInstanceRepository;
import ca.uhn.fhir.jpa.dao.data.IBatch2WorkChunkRepository;
import ca.uhn.fhir.jpa.entity.Batch2JobInstanceEntity;
import ca.uhn.fhir.jpa.entity.Batch2WorkChunkEntity;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelProducer;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.util.JobInstanceUtil;
import ca.uhn.fhir.util.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class JpaJobPersistenceImplTest extends BaseJpaR4Test {

	public static final String JOB_DEFINITION_ID = "definition-id";
	public static final String TARGET_STEP_ID = "step-id";
	public static final String DEF_CHUNK_ID = "definition-chunkId";
	public static final String STEP_CHUNK_ID = "step-chunkId";
	public static final int JOB_DEF_VER = 1;
	public static final int SEQUENCE_NUMBER = 1;
	public static final String CHUNK_DATA = "{\"key\":\"value\"}";

	@Autowired
	private IJobPersistence mySvc;
	@Autowired
	private IBatch2WorkChunkRepository myWorkChunkRepository;
	@Autowired
	private IBatch2JobInstanceRepository myJobInstanceRepository;

	@Test
	public void testDeleteInstance() {
		// Setup

		JobInstance instance = createInstance();
		String instanceId = mySvc.storeNewInstance(instance);
		for (int i = 0; i < 10; i++) {
			storeWorkChunk(JOB_DEFINITION_ID, TARGET_STEP_ID, instanceId, i, JsonUtil.serialize(new NdJsonFileJson().setNdJsonText("{}")));
		}

		// Execute

		mySvc.deleteInstanceAndChunks(instanceId);

		// Verify

		runInTransaction(() -> {
			assertEquals(0, myJobInstanceRepository.findAll().size());
			assertEquals(0, myWorkChunkRepository.findAll().size());
		});
	}

	private String storeWorkChunk(String theJobDefinitionId, String theTargetStepId, String theInstanceId, int theSequence, String theSerializedData) {
		BatchWorkChunk batchWorkChunk = new BatchWorkChunk(theJobDefinitionId, JOB_DEF_VER, theTargetStepId, theInstanceId, theSequence, theSerializedData);
		return mySvc.storeWorkChunk(batchWorkChunk);
	}

	@Test
	public void testDeleteChunks() {
		// Setup

		JobInstance instance = createInstance();
		String instanceId = mySvc.storeNewInstance(instance);
		for (int i = 0; i < 10; i++) {
			storeWorkChunk(JOB_DEFINITION_ID, TARGET_STEP_ID, instanceId, i, CHUNK_DATA);
		}

		// Execute

		mySvc.deleteChunks(instanceId);

		// Verify

		runInTransaction(() -> {
			assertEquals(1, myJobInstanceRepository.findAll().size());
			assertEquals(0, myWorkChunkRepository.findAll().size());
		});
	}

	@Test
	public void testStoreAndFetchInstance() {
		JobInstance instance = createInstance();
		String instanceId = mySvc.storeNewInstance(instance);

		runInTransaction(() -> {
			Batch2JobInstanceEntity instanceEntity = myJobInstanceRepository.findById(instanceId).orElseThrow(IllegalStateException::new);
			assertEquals(StatusEnum.QUEUED, instanceEntity.getStatus());
		});

		JobInstance foundInstance = mySvc.fetchInstance(instanceId).orElseThrow(IllegalStateException::new);
		assertEquals(instanceId, foundInstance.getInstanceId());
		assertEquals(JOB_DEFINITION_ID, foundInstance.getJobDefinitionId());
		assertEquals(JOB_DEF_VER, foundInstance.getJobDefinitionVersion());
		assertEquals(StatusEnum.QUEUED, foundInstance.getStatus());
		assertEquals(CHUNK_DATA, foundInstance.getParameters());
		assertEquals(instance.getReport(), foundInstance.getReport());

		runInTransaction(() -> {
			Batch2JobInstanceEntity instanceEntity = myJobInstanceRepository.findById(instanceId).orElseThrow(IllegalStateException::new);
			assertEquals(StatusEnum.QUEUED, instanceEntity.getStatus());
		});
	}

	@Test
	public void testFetchInstanceWithStatusAndCutoff_statues() {
		myCaptureQueriesListener.clear();

		final String completedId = storeJobInstanceAndUpdateWithEndTime(StatusEnum.COMPLETED, 1);
		final String failedId = storeJobInstanceAndUpdateWithEndTime(StatusEnum.FAILED, 1);
		final String erroredId = storeJobInstanceAndUpdateWithEndTime(StatusEnum.ERRORED, 1);
		final String cancelledId = storeJobInstanceAndUpdateWithEndTime(StatusEnum.CANCELLED, 1);
		storeJobInstanceAndUpdateWithEndTime(StatusEnum.QUEUED, 1);
		storeJobInstanceAndUpdateWithEndTime(StatusEnum.IN_PROGRESS, 1);
		storeJobInstanceAndUpdateWithEndTime(StatusEnum.FINALIZE, 1);

		final LocalDateTime cutoffLocalDateTime = LocalDateTime.now()
			.minusMinutes(0);
		final Date cutoffDate = Date.from(cutoffLocalDateTime
			.atZone(ZoneId.systemDefault())
			.toInstant());

		final List<JobInstance> jobInstancesByCutoff =
			mySvc.fetchInstances(JOB_DEFINITION_ID, StatusEnum.getEndedStatuses(), cutoffDate, PageRequest.of(0, 100));

		assertEquals(Set.of(completedId, failedId, erroredId, cancelledId),
			jobInstancesByCutoff.stream()
				.map(JobInstance::getInstanceId)
				.collect(Collectors.toUnmodifiableSet()));
	}

	@Test
	public void testFetchInstanceWithStatusAndCutoff_cutoffs() {
		myCaptureQueriesListener.clear();

		storeJobInstanceAndUpdateWithEndTime(StatusEnum.COMPLETED, 3);
		storeJobInstanceAndUpdateWithEndTime(StatusEnum.COMPLETED, 4);
		final String sevenMinutesAgoId = storeJobInstanceAndUpdateWithEndTime(StatusEnum.COMPLETED, 7);
		final String eightMinutesAgoId = storeJobInstanceAndUpdateWithEndTime(StatusEnum.COMPLETED, 8);

		final LocalDateTime cutoffLocalDateTime = LocalDateTime.now()
			.minusMinutes(6);

		final Date cutoffDate = Date.from(cutoffLocalDateTime
			.atZone(ZoneId.systemDefault())
			.toInstant());

		final List<JobInstance> jobInstancesByCutoff =
			mySvc.fetchInstances(JOB_DEFINITION_ID, StatusEnum.getEndedStatuses(), cutoffDate, PageRequest.of(0, 100));

		myCaptureQueriesListener.logSelectQueries();
		myCaptureQueriesListener.getSelectQueries().forEach(query -> ourLog.info("query: {}", query.getSql(true, true)));

		assertEquals(Set.of(sevenMinutesAgoId, eightMinutesAgoId),
			jobInstancesByCutoff.stream()
				.map(JobInstance::getInstanceId)
				.collect(Collectors.toUnmodifiableSet()));
	}

	@Test
	public void testFetchInstanceWithStatusAndCutoff_pages() {
		final String job1 = storeJobInstanceAndUpdateWithEndTime(StatusEnum.COMPLETED, 5);
		final String job2 = storeJobInstanceAndUpdateWithEndTime(StatusEnum.COMPLETED, 5);
		storeJobInstanceAndUpdateWithEndTime(StatusEnum.COMPLETED, 5);
		storeJobInstanceAndUpdateWithEndTime(StatusEnum.COMPLETED, 5);
		storeJobInstanceAndUpdateWithEndTime(StatusEnum.COMPLETED, 5);
		storeJobInstanceAndUpdateWithEndTime(StatusEnum.COMPLETED, 5);

		final LocalDateTime cutoffLocalDateTime = LocalDateTime.now()
			.minusMinutes(0);

		final Date cutoffDate = Date.from(cutoffLocalDateTime
			.atZone(ZoneId.systemDefault())
			.toInstant());

		final List<JobInstance> jobInstancesByCutoff =
			mySvc.fetchInstances(JOB_DEFINITION_ID, StatusEnum.getEndedStatuses(), cutoffDate, PageRequest.of(0, 2));

		assertEquals(Set.of(job1, job2),
			jobInstancesByCutoff.stream()
				.map(JobInstance::getInstanceId)
				.collect(Collectors.toUnmodifiableSet()));
	}

	/**
	 * Returns a set of statuses, and whether they should be successfully picked up and started by a consumer.
	 */
	public static List<Arguments> provideStatuses() {
		return List.of(
			Arguments.of(WorkChunkStatusEnum.QUEUED, true),
			// wipmb this looks like a bug
			Arguments.of(WorkChunkStatusEnum.IN_PROGRESS, true),
			Arguments.of(WorkChunkStatusEnum.ERRORED, true),
			Arguments.of(WorkChunkStatusEnum.FAILED, false),
			Arguments.of(WorkChunkStatusEnum.COMPLETED, false)
		);
	}
	@ParameterizedTest
	@MethodSource("provideStatuses")
	public void testStartChunkOnlyWorksOnValidChunks(WorkChunkStatusEnum theStatus, boolean theShouldBeStartedByConsumer) {
		// Setup
		JobInstance instance = createInstance();
		String instanceId = mySvc.storeNewInstance(instance);
		storeWorkChunk(JOB_DEFINITION_ID, TARGET_STEP_ID, instanceId, 0, CHUNK_DATA);
		BatchWorkChunk batchWorkChunk = new BatchWorkChunk(JOB_DEFINITION_ID, JOB_DEF_VER, TARGET_STEP_ID, instanceId,0, CHUNK_DATA);
		String chunkId = mySvc.storeWorkChunk(batchWorkChunk);
		Optional<Batch2WorkChunkEntity> byId = myWorkChunkRepository.findById(chunkId);
		Batch2WorkChunkEntity entity = byId.get();
		entity.setStatus(theStatus);
		myWorkChunkRepository.save(entity);

		// Execute
		Optional<WorkChunk> workChunk = mySvc.fetchWorkChunkSetStartTimeAndMarkInProgress(chunkId);

		// Verify
		boolean chunkStarted = workChunk.isPresent();
		assertEquals(chunkStarted, theShouldBeStartedByConsumer);
	}

	@Test
	public void testCancelInstance() {
		JobInstance instance = createInstance();
		String instanceId = mySvc.storeNewInstance(instance);

		runInTransaction(() -> {
			Batch2JobInstanceEntity instanceEntity = myJobInstanceRepository.findById(instanceId).orElseThrow(IllegalStateException::new);
			assertEquals(StatusEnum.QUEUED, instanceEntity.getStatus());
			instanceEntity.setCancelled(true);
			myJobInstanceRepository.save(instanceEntity);
		});

		JobOperationResultJson result = mySvc.cancelInstance(instanceId);
		assertTrue(result.getSuccess());
		assertEquals("Job instance <" + instanceId + "> successfully cancelled.", result.getMessage());

		JobInstance foundInstance = mySvc.fetchInstance(instanceId).orElseThrow(IllegalStateException::new);
		assertEquals(instanceId, foundInstance.getInstanceId());
		assertEquals(JOB_DEFINITION_ID, foundInstance.getJobDefinitionId());
		assertEquals(JOB_DEF_VER, foundInstance.getJobDefinitionVersion());
		assertEquals(StatusEnum.QUEUED, foundInstance.getStatus());
		assertTrue(foundInstance.isCancelled());
		assertEquals(CHUNK_DATA, foundInstance.getParameters());

	}

	@Test
	void testFetchInstancesByJobDefinitionId() {
		JobInstance instance = createInstance();
		String instanceId = mySvc.storeNewInstance(instance);

		List<JobInstance> foundInstances = mySvc.fetchInstancesByJobDefinitionId(JOB_DEFINITION_ID, 10, 0);
		assertThat(foundInstances, hasSize(1));
		assertEquals(instanceId, foundInstances.get(0).getInstanceId());
	}

	@Test
	void testFetchInstancesByJobDefinitionIdAndStatus() {
		JobInstance instance = createInstance();
		String instanceId = mySvc.storeNewInstance(instance);

		Set<StatusEnum> statuses = new HashSet<>();
		statuses.add(StatusEnum.QUEUED);
		statuses.add(StatusEnum.COMPLETED);
		List<JobInstance> foundInstances = mySvc.fetchInstancesByJobDefinitionIdAndStatus(JOB_DEFINITION_ID, statuses, 10, 0);
		assertThat(foundInstances, hasSize(1));
		assertEquals(instanceId, foundInstances.get(0).getInstanceId());
	}

	@Test
	public void testGatedAdvancementByStatus() {
		// Setup
		JobInstance instance = createInstance();
		String instanceId = mySvc.storeNewInstance(instance);
		String chunkId = storeWorkChunk(DEF_CHUNK_ID, STEP_CHUNK_ID, instanceId, SEQUENCE_NUMBER, null);
		mySvc.workChunkCompletionEvent(new IWorkChunkPersistence.WorkChunkCompletionEvent(chunkId, 0, 0));

		boolean canAdvance = mySvc.canAdvanceInstanceToNextStep(instanceId, STEP_CHUNK_ID);
		assertTrue(canAdvance);

		//Storing a new chunk with QUEUED should prevent advancement.
		String newChunkId = storeWorkChunk(DEF_CHUNK_ID, STEP_CHUNK_ID, instanceId, SEQUENCE_NUMBER, null);

		canAdvance = mySvc.canAdvanceInstanceToNextStep(instanceId, STEP_CHUNK_ID);
		assertFalse(canAdvance);

		//Toggle it to complete
		mySvc.workChunkCompletionEvent(new IWorkChunkPersistence.WorkChunkCompletionEvent(newChunkId, 0, 0));
		canAdvance = mySvc.canAdvanceInstanceToNextStep(instanceId, STEP_CHUNK_ID);
		assertTrue(canAdvance);

		//Create a new chunk and set it in progress.
		String newerChunkId= storeWorkChunk(DEF_CHUNK_ID, STEP_CHUNK_ID, instanceId, SEQUENCE_NUMBER, null);
		mySvc.fetchWorkChunkSetStartTimeAndMarkInProgress(newerChunkId);
		canAdvance = mySvc.canAdvanceInstanceToNextStep(instanceId, STEP_CHUNK_ID);
		assertFalse(canAdvance);

		//Toggle IN_PROGRESS to complete
		mySvc.workChunkCompletionEvent(new IWorkChunkPersistence.WorkChunkCompletionEvent(newerChunkId, 0, 0));
		canAdvance = mySvc.canAdvanceInstanceToNextStep(instanceId, STEP_CHUNK_ID);
		assertTrue(canAdvance);
	}

	@Test
	public void testMarkInstanceAsCompleted() {
		String instanceId = mySvc.storeNewInstance(createInstance());

		assertTrue(mySvc.markInstanceAsCompleted(instanceId));
		assertFalse(mySvc.markInstanceAsCompleted(instanceId));

		runInTransaction(() -> {
			Batch2JobInstanceEntity entity = myJobInstanceRepository.findById(instanceId).orElseThrow(IllegalArgumentException::new);
			assertEquals(StatusEnum.COMPLETED, entity.getStatus());
		});
	}

	@Test
	public void testUpdateInstance() {
		String instanceId = mySvc.storeNewInstance(createInstance());

		JobInstance instance = mySvc.fetchInstance(instanceId).orElseThrow(IllegalArgumentException::new);
		assertEquals(instanceId, instance.getInstanceId());
		assertFalse(instance.isWorkChunksPurged());

		instance.setStartTime(new Date());
		sleepUntilTimeChanges();
		instance.setEndTime(new Date());
		instance.setCombinedRecordsProcessed(100);
		instance.setCombinedRecordsProcessedPerSecond(22.0);
		instance.setWorkChunksPurged(true);
		instance.setProgress(0.5d);
		instance.setErrorCount(3);
		instance.setEstimatedTimeRemaining("32d");

		mySvc.updateInstance(instance);

		runInTransaction(() -> {
			Batch2JobInstanceEntity entity = myJobInstanceRepository.findById(instanceId).orElseThrow(IllegalArgumentException::new);
			assertEquals(instance.getStartTime().getTime(), entity.getStartTime().getTime());
			assertEquals(instance.getEndTime().getTime(), entity.getEndTime().getTime());
		});

		JobInstance finalInstance = mySvc.fetchInstance(instanceId).orElseThrow(IllegalArgumentException::new);
		assertEquals(instanceId, finalInstance.getInstanceId());
		assertEquals(0.5d, finalInstance.getProgress());
		assertTrue(finalInstance.isWorkChunksPurged());
		assertEquals(3, finalInstance.getErrorCount());
		assertEquals(instance.getReport(), finalInstance.getReport());
		assertEquals(instance.getEstimatedTimeRemaining(), finalInstance.getEstimatedTimeRemaining());
	}

	@Nested
	class InstanceStateTransitions {
		IChannelProducer myChannelProducer = Mockito.mock(IChannelProducer.class);
		BatchJobSender myBatchJobSender = new BatchJobSender(myChannelProducer);

		// wipmb extract and run on Mongo too

		@ParameterizedTest
		@EnumSource(StatusEnum.class)
		void cancelRequest_cancelsJob_whenNotFinalState(StatusEnum theState) {
			// given
			JobInstance instance = createInstance();
			instance.setStatus(theState);
			instance.setCancelled(true);
			String instanceId = mySvc.storeNewInstance(instance);

			// when
			mySvc.processCancelRequests();

			// then
			JobInstance freshInstance = mySvc.fetchInstance(instanceId).orElseThrow();
			if (theState.isCancellable()) {
				assertEquals(StatusEnum.CANCELLED, freshInstance.getStatus(), "cancel request processed");
			} else {
				assertEquals(theState, freshInstance.getStatus(), "cancel request ignored - state unchanged");
			}
		}

		static class WorkChunkWriter {
			final IJobPersistence myJobPersistence;
			final JobInstance myJobInstance;

			WorkChunkWriter(IJobPersistence theJobPersistence, JobInstance theJobInstance) {
				myJobInstance = theJobInstance;
				myJobPersistence = theJobPersistence;
			}

			public String createChunk(String theStepId, int theSequence, String theData, Consumer<BatchWorkChunk> theCallback) {
				BatchWorkChunk batchWorkChunk = new BatchWorkChunk(myJobInstance.getJobDefinitionId(), myJobInstance.getJobDefinitionVersion(), theStepId, myJobInstance.getInstanceId(), theSequence, theData);
				theCallback.accept(batchWorkChunk);
				return myJobPersistence.storeWorkChunk(batchWorkChunk);
			}
		}

		@Nested
		class InstanceProcessing {

			abstract class InstanceProcessingTests {
				abstract void process(String theInstanceId);

				@Test
				void testTotalRecordCount_isSumOfAllChunks() {
					// given
					JobInstance instance = createInstance();
					String instanceId = mySvc.storeNewInstance(instance);
					instance.setInstanceId(instanceId);
					WorkChunkWriter chunkWriter = new WorkChunkWriter(mySvc, instance);

					String chunk0 = chunkWriter.createChunk(TARGET_STEP_ID, 0, "{}", (c) -> {});
					String chunk1 = chunkWriter.createChunk(TARGET_STEP_ID, 1, "{}", (c) -> {});
					String chunk2 = chunkWriter.createChunk(TARGET_STEP_ID, 2, "{}", (c) -> {});
					mySvc.workChunkCompletionEvent(new IWorkChunkPersistence.WorkChunkCompletionEvent(chunk0, 5, 0));
					mySvc.workChunkCompletionEvent(new IWorkChunkPersistence.WorkChunkCompletionEvent(chunk1, 3, 1));
					mySvc.markInstanceAsStatus(instanceId, StatusEnum.IN_PROGRESS);

					process(instanceId);

					JobInstance freshInstance = mySvc.fetchInstance(instanceId).orElseThrow();
					assertEquals(8, freshInstance.getCombinedRecordsProcessed());
					// wipmb do we need this on the entity?
					//assertEquals(0, freshInstance.getCombinedRecordsProcessedPerSecond());
					assertEquals(1, freshInstance.getErrorCount());
					// wipmb add progress
					//assertEquals(0, freshInstance.getProgress());
				}		}

			@Nested
			class OldWay extends InstanceProcessingTests {
				void process(String theInstanceId) {
					JobInstanceProcessor jobInstanceProcessor = new JobInstanceProcessor(mySvc, myBatchJobSender, mySvc.fetchInstance(theInstanceId).orElseThrow(), new JobChunkProgressAccumulator(), null);
					jobInstanceProcessor.process();
				}
			}

			@Nested
			class NewWay extends InstanceProcessingTests {
				void process(String theInstanceId) {
					mySvc.updateRunningJobStatistics();
				}
			}

		}




		// wipmb persistence state tests

	}

	@Nested
	class WorkChunkStorage {

		@Test
		public void testStoreAndFetchWorkChunk_NoData() {
			JobInstance instance = createInstance();
			String instanceId = mySvc.storeNewInstance(instance);

			String id = storeWorkChunk(JOB_DEFINITION_ID, TARGET_STEP_ID, instanceId, 0, null);

			WorkChunk chunk = mySvc.fetchWorkChunkSetStartTimeAndMarkInProgress(id).orElseThrow(IllegalArgumentException::new);
			assertNull(chunk.getData());
		}

		@Test
		public void testStoreAndFetchWorkChunk_WithData() {
			JobInstance instance = createInstance();
			String instanceId = mySvc.storeNewInstance(instance);

			String id = storeWorkChunk(JOB_DEFINITION_ID, TARGET_STEP_ID, instanceId, 0, CHUNK_DATA);
			assertNotNull(id);
			runInTransaction(() -> assertEquals(WorkChunkStatusEnum.QUEUED, myWorkChunkRepository.findById(id).orElseThrow(IllegalArgumentException::new).getStatus()));

			WorkChunk chunk = mySvc.fetchWorkChunkSetStartTimeAndMarkInProgress(id).orElseThrow(IllegalArgumentException::new);
			assertEquals(36, chunk.getInstanceId().length());
			assertEquals(JOB_DEFINITION_ID, chunk.getJobDefinitionId());
			assertEquals(JOB_DEF_VER, chunk.getJobDefinitionVersion());
			assertEquals(WorkChunkStatusEnum.IN_PROGRESS, chunk.getStatus());
			assertEquals(CHUNK_DATA, chunk.getData());

			runInTransaction(() -> assertEquals(WorkChunkStatusEnum.IN_PROGRESS, myWorkChunkRepository.findById(id).orElseThrow(IllegalArgumentException::new).getStatus()));
		}

		/**
		 * Should match the diagram in batch2_states.md
		 ```mermaid
		 ---
		 title: Batch2 Job Work Chunk state transitions
		 ---
		 stateDiagram-v2
		 [*]         --> QUEUED        : on store
		 state on_receive <<choice>>
		 QUEUED      --> on_receive : on receive by worker
		 on_receive --> IN_PROGRESS : start execution
		 state execute <<choice>>
		 IN_PROGRESS --> execute: execute
		 %%  (JobExecutionFailedException or Throwable)
		 execute --> COMPLETED   : success - maybe trigger instance first_step_finished
		 execute --> ERROR       : on re-triable error
		 execute --> FAILED      : on unrecoverable \n or too many errors
		 ERROR       --> on_receive : exception rollback triggers redelivery
		 COMPLETED       --> [*]
		 FAILED       --> [*]
		 ```
		 wipmb WorkChunk state transition tests
		 wipmb extract and re-use in Mongo as an abstract specification test.  Can live in jpaserver-test-utils
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
				WorkChunk chunk = mySvc.fetchWorkChunkSetStartTimeAndMarkInProgress(myChunkId).orElseThrow(IllegalArgumentException::new);

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
					mySvc.fetchWorkChunkSetStartTimeAndMarkInProgress(myChunkId).orElseThrow(IllegalArgumentException::new);
				}

				@Test
				public void processingOk_inProgressToSuccess_clearsDataSavesRecordCount() {

					// execution ok
					mySvc.workChunkCompletionEvent(new IWorkChunkPersistence.WorkChunkCompletionEvent(myChunkId, 3, 0));

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
					mySvc.workChunkErrorEvent(new WorkChunkErrorEvent(myChunkId, "some error"));

					// verify the db was updated
					var workChunkEntity = freshFetchWorkChunk(myChunkId);
					assertEquals(WorkChunkStatusEnum.ERRORED, workChunkEntity.getStatus());
					assertEquals("some error", workChunkEntity.getErrorMessage());
					assertEquals(1, workChunkEntity.getErrorCount());
				}

				@Test
				public void processingFailure_inProgressToFailed() {

					// execution had a failure
					mySvc.markWorkChunkAsFailed(myChunkId, "some error");

					// verify the db was updated
					var workChunkEntity = freshFetchWorkChunk(myChunkId);
					assertEquals(WorkChunkStatusEnum.FAILED, workChunkEntity.getStatus());
					assertEquals("some error", workChunkEntity.getErrorMessage());
				}
			}

			@Nested
			class ErrorActions {
				@BeforeEach
				void setUp() {
					// setup - the worker has received the chunk, and has marked it IN_PROGRESS.
					myChunkId = createChunk();
					WorkChunk chunk = mySvc.fetchWorkChunkSetStartTimeAndMarkInProgress(myChunkId).orElseThrow(IllegalArgumentException::new);
					// execution had a retryable error
					mySvc.workChunkErrorEvent(new WorkChunkErrorEvent(myChunkId, "some error"));
				}

				/**
				 * The consumer will retry after a retryable error is thrown
				 */
				@Test
				void errorRetry_errorToInProgress() {

				    // when consumer restarts chunk
					WorkChunk chunk = mySvc.fetchWorkChunkSetStartTimeAndMarkInProgress(myChunkId).orElseThrow(IllegalArgumentException::new);

					// then
					assertEquals(WorkChunkStatusEnum.IN_PROGRESS, chunk.getStatus());

					// verify the db state, error message, and error count
					var workChunkEntity = freshFetchWorkChunk(myChunkId);
					assertEquals(WorkChunkStatusEnum.IN_PROGRESS, workChunkEntity.getStatus());
					assertEquals("some error", workChunkEntity.getErrorMessage(), "Original error message kept");
					assertEquals(1, workChunkEntity.getErrorCount(), "error count kept");
				}

				@Test
				void errorRetry_repeatError_increasesErrorCount() {
					// setup - the consumer is re-trying, and marks it IN_PROGRESS
					WorkChunk chunk = mySvc.fetchWorkChunkSetStartTimeAndMarkInProgress(myChunkId).orElseThrow(IllegalArgumentException::new);


					// when another error happens
					mySvc.workChunkErrorEvent(new WorkChunkErrorEvent(myChunkId, "some other error"));


					// verify the state, new message, and error count
					var workChunkEntity = freshFetchWorkChunk(myChunkId);
					assertEquals(WorkChunkStatusEnum.ERRORED, workChunkEntity.getStatus());
					assertEquals("some other error", workChunkEntity.getErrorMessage(), "new error message");
					assertEquals(2, workChunkEntity.getErrorCount(), "error count inc");
				}

				@Test
				void errorRetry_maxErrors_movesToFailed() {
					// we start with 1 error already

					// 2nd try
					mySvc.fetchWorkChunkSetStartTimeAndMarkInProgress(myChunkId).orElseThrow(IllegalArgumentException::new);
					mySvc.workChunkErrorEvent(new WorkChunkErrorEvent(myChunkId, "some other error"));
					var chunk = freshFetchWorkChunk(myChunkId);
					assertEquals(WorkChunkStatusEnum.ERRORED, chunk.getStatus());
					assertEquals(2, chunk.getErrorCount());

					// 3rd try
					mySvc.fetchWorkChunkSetStartTimeAndMarkInProgress(myChunkId).orElseThrow(IllegalArgumentException::new);
					mySvc.workChunkErrorEvent(new WorkChunkErrorEvent(myChunkId, "some other error"));
					chunk = freshFetchWorkChunk(myChunkId);
					assertEquals(WorkChunkStatusEnum.ERRORED, chunk.getStatus());
					assertEquals(3, chunk.getErrorCount());

					// 4th try
					mySvc.fetchWorkChunkSetStartTimeAndMarkInProgress(myChunkId).orElseThrow(IllegalArgumentException::new);
					mySvc.workChunkErrorEvent(new WorkChunkErrorEvent(myChunkId, "final error"));
					chunk = freshFetchWorkChunk(myChunkId);
					assertEquals(WorkChunkStatusEnum.FAILED, chunk.getStatus());
					assertEquals(4, chunk.getErrorCount());
					assertEquals("final error", chunk.getErrorMessage(), "last error message wins");
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
		public void testFetchUnknownWork() {
			assertFalse(myWorkChunkRepository.findById("FOO").isPresent());
		}


		@Test
		public void testMarkChunkAsCompleted_Success() {
			JobInstance instance = createInstance();
			String instanceId = mySvc.storeNewInstance(instance);
			String chunkId = storeWorkChunk(DEF_CHUNK_ID, STEP_CHUNK_ID, instanceId, SEQUENCE_NUMBER, CHUNK_DATA);
			assertNotNull(chunkId);

			runInTransaction(() -> assertEquals(WorkChunkStatusEnum.QUEUED, myWorkChunkRepository.findById(chunkId).orElseThrow(IllegalArgumentException::new).getStatus()));

			sleepUntilTimeChanges();

			WorkChunk chunk = mySvc.fetchWorkChunkSetStartTimeAndMarkInProgress(chunkId).orElseThrow(IllegalArgumentException::new);
			assertEquals(SEQUENCE_NUMBER, chunk.getSequence());
			assertEquals(WorkChunkStatusEnum.IN_PROGRESS, chunk.getStatus());
			assertNotNull(chunk.getCreateTime());
			assertNotNull(chunk.getStartTime());
			assertNull(chunk.getEndTime());
			assertNull(chunk.getRecordsProcessed());
			assertNotNull(chunk.getData());
			runInTransaction(() -> assertEquals(WorkChunkStatusEnum.IN_PROGRESS, myWorkChunkRepository.findById(chunkId).orElseThrow(IllegalArgumentException::new).getStatus()));

			sleepUntilTimeChanges();

			mySvc.workChunkCompletionEvent(new IWorkChunkPersistence.WorkChunkCompletionEvent(chunkId, 50, 0));
			runInTransaction(() -> {
				Batch2WorkChunkEntity entity = myWorkChunkRepository.findById(chunkId).orElseThrow(IllegalArgumentException::new);
				assertEquals(WorkChunkStatusEnum.COMPLETED, entity.getStatus());
				assertEquals(50, entity.getRecordsProcessed());
				assertNotNull(entity.getCreateTime());
				assertNotNull(entity.getStartTime());
				assertNotNull(entity.getEndTime());
				assertNull(entity.getSerializedData());
				assertTrue(entity.getCreateTime().getTime() < entity.getStartTime().getTime());
				assertTrue(entity.getStartTime().getTime() < entity.getEndTime().getTime());
			});
		}

		@Test
		public void testIncrementWorkChunkErrorCount() {
			// Setup

			JobInstance instance = createInstance();
			String instanceId = mySvc.storeNewInstance(instance);
			String chunkId = storeWorkChunk(DEF_CHUNK_ID, STEP_CHUNK_ID, instanceId, SEQUENCE_NUMBER, null);
			assertNotNull(chunkId);

			// Execute

			mySvc.incrementWorkChunkErrorCount(chunkId, 2);
			mySvc.incrementWorkChunkErrorCount(chunkId, 3);

			// Verify

			List<WorkChunk> chunks = mySvc.fetchWorkChunksWithoutData(instanceId, 100, 0);
			assertEquals(1, chunks.size());
			assertEquals(5, chunks.get(0).getErrorCount());
		}

		@Test
		public void testMarkChunkAsCompleted_Error() {
			JobInstance instance = createInstance();
			String instanceId = mySvc.storeNewInstance(instance);
			String chunkId = storeWorkChunk(DEF_CHUNK_ID, STEP_CHUNK_ID, instanceId, SEQUENCE_NUMBER, null);
			assertNotNull(chunkId);

			runInTransaction(() -> assertEquals(WorkChunkStatusEnum.QUEUED, myWorkChunkRepository.findById(chunkId).orElseThrow(IllegalArgumentException::new).getStatus()));

			sleepUntilTimeChanges();

			WorkChunk chunk = mySvc.fetchWorkChunkSetStartTimeAndMarkInProgress(chunkId).orElseThrow(IllegalArgumentException::new);
			assertEquals(SEQUENCE_NUMBER, chunk.getSequence());
			assertEquals(WorkChunkStatusEnum.IN_PROGRESS, chunk.getStatus());

			sleepUntilTimeChanges();

			WorkChunkErrorEvent request = new WorkChunkErrorEvent(chunkId, "This is an error message");
			mySvc.workChunkErrorEvent(request);
			runInTransaction(() -> {
				Batch2WorkChunkEntity entity = myWorkChunkRepository.findById(chunkId).orElseThrow(IllegalArgumentException::new);
				assertEquals(WorkChunkStatusEnum.ERRORED, entity.getStatus());
				assertEquals("This is an error message", entity.getErrorMessage());
				assertNotNull(entity.getCreateTime());
				assertNotNull(entity.getStartTime());
				assertNotNull(entity.getEndTime());
				assertEquals(1, entity.getErrorCount());
				assertTrue(entity.getCreateTime().getTime() < entity.getStartTime().getTime());
				assertTrue(entity.getStartTime().getTime() < entity.getEndTime().getTime());
			});

			// Mark errored again

			WorkChunkErrorEvent request2 = new WorkChunkErrorEvent(chunkId, "This is an error message 2");
			mySvc.workChunkErrorEvent(request2);
			runInTransaction(() -> {
				Batch2WorkChunkEntity entity = myWorkChunkRepository.findById(chunkId).orElseThrow(IllegalArgumentException::new);
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

			runInTransaction(() -> assertEquals(WorkChunkStatusEnum.QUEUED, myWorkChunkRepository.findById(chunkId).orElseThrow(IllegalArgumentException::new).getStatus()));

			sleepUntilTimeChanges();

			WorkChunk chunk = mySvc.fetchWorkChunkSetStartTimeAndMarkInProgress(chunkId).orElseThrow(IllegalArgumentException::new);
			assertEquals(SEQUENCE_NUMBER, chunk.getSequence());
			assertEquals(WorkChunkStatusEnum.IN_PROGRESS, chunk.getStatus());

			sleepUntilTimeChanges();

			mySvc.markWorkChunkAsFailed(chunkId, "This is an error message");
			runInTransaction(() -> {
				Batch2WorkChunkEntity entity = myWorkChunkRepository.findById(chunkId).orElseThrow(IllegalArgumentException::new);
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
				BatchWorkChunk chunk = new BatchWorkChunk(
					"defId",
					1,
					"stepId",
					instanceId,
					0,
					"{}"
				);
				String id = mySvc.storeWorkChunk(chunk);
				chunkIds.add(id);
			}

			mySvc.markWorkChunksWithStatusAndWipeData(instance.getInstanceId(), chunkIds, WorkChunkStatusEnum.COMPLETED, null);

			Iterator<WorkChunk> reducedChunks = mySvc.fetchAllWorkChunksIterator(instanceId, true);

			while (reducedChunks.hasNext()) {
				WorkChunk reducedChunk = reducedChunks.next();
				assertTrue(chunkIds.contains(reducedChunk.getId()));
				assertEquals(WorkChunkStatusEnum.COMPLETED, reducedChunk.getStatus());
			}
		}
	}

	private WorkChunk freshFetchWorkChunk(String chunkId) {
		return runInTransaction(() -> {
			return myWorkChunkRepository.findById(chunkId)
				.map(e-> JobInstanceUtil.fromEntityToWorkChunk(e, true))
				.orElseThrow(IllegalArgumentException::new);
		});
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


	@Nonnull
	private String storeJobInstanceAndUpdateWithEndTime(StatusEnum theStatus, int minutes) {
		final JobInstance jobInstance = new JobInstance();

		jobInstance.setJobDefinitionId(JOB_DEFINITION_ID);
		jobInstance.setStatus(theStatus);
		jobInstance.setJobDefinitionVersion(JOB_DEF_VER);
		jobInstance.setParameters(CHUNK_DATA);
		jobInstance.setReport("TEST");

		final String id = mySvc.storeNewInstance(jobInstance);

		jobInstance.setInstanceId(id);
		final LocalDateTime localDateTime = LocalDateTime.now()
			.minusMinutes(minutes);
		ourLog.info("localDateTime: {}", localDateTime);
		jobInstance.setEndTime(Date.from(localDateTime
			.atZone(ZoneId.systemDefault())
			.toInstant()));

		mySvc.updateInstance(jobInstance);

		return id;
	}
}
