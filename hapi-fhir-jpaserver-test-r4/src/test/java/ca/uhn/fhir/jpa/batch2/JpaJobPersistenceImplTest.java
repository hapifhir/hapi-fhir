package ca.uhn.fhir.jpa.batch2;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.JobOperationResultJson;
import ca.uhn.fhir.batch2.jobs.imprt.NdJsonFileJson;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkCompletionEvent;
import ca.uhn.fhir.batch2.model.WorkChunkCreateEvent;
import ca.uhn.fhir.batch2.model.WorkChunkErrorEvent;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import ca.uhn.fhir.batch2.models.JobInstanceFetchRequest;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.dao.data.IBatch2JobInstanceRepository;
import ca.uhn.fhir.jpa.dao.data.IBatch2WorkChunkRepository;
import ca.uhn.fhir.jpa.entity.Batch2JobInstanceEntity;
import ca.uhn.fhir.jpa.entity.Batch2WorkChunkEntity;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.hapi.fhir.batch2.test.AbstractIJobPersistenceSpecificationTest;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.annotation.Nonnull;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
		WorkChunkCreateEvent batchWorkChunk = new WorkChunkCreateEvent(theJobDefinitionId, JOB_DEF_VER, theTargetStepId, theInstanceId, theSequence, theSerializedData);
		return mySvc.onWorkChunkCreate(batchWorkChunk);
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
		assertThat(foundInstance.getInstanceId()).isEqualTo(instanceId);
		assertThat(foundInstance.getJobDefinitionId()).isEqualTo(JOB_DEFINITION_ID);
		assertThat(foundInstance.getJobDefinitionVersion()).isEqualTo(JOB_DEF_VER);
		assertThat(foundInstance.getStatus()).isEqualTo(StatusEnum.QUEUED);
		assertThat(foundInstance.getParameters()).isEqualTo(CHUNK_DATA);
		assertThat(foundInstance.getReport()).isEqualTo(instance.getReport());

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
		final String cancelledId = storeJobInstanceAndUpdateWithEndTime(StatusEnum.CANCELLED, 1);
		storeJobInstanceAndUpdateWithEndTime(StatusEnum.ERRORED, 1);
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

		assertThat(jobInstancesByCutoff.stream()
			.map(JobInstance::getInstanceId)
			.collect(Collectors.toUnmodifiableSet())).isEqualTo(Set.of(completedId, failedId, cancelledId));
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

		assertThat(jobInstancesByCutoff.stream()
			.map(JobInstance::getInstanceId)
			.collect(Collectors.toUnmodifiableSet())).isEqualTo(Set.of(sevenMinutesAgoId, eightMinutesAgoId));
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

		assertThat(jobInstancesByCutoff.stream()
			.map(JobInstance::getInstanceId)
			.collect(Collectors.toUnmodifiableSet())).isEqualTo(Set.of(job1, job2));
	}

	@ParameterizedTest
	@MethodSource("provideStatuses")
	public void testStartChunkOnlyWorksOnValidChunks(WorkChunkStatusEnum theStatus, boolean theShouldBeStartedByConsumer) {
		// Setup
		JobInstance instance = createInstance();
		String instanceId = mySvc.storeNewInstance(instance);
		storeWorkChunk(JOB_DEFINITION_ID, TARGET_STEP_ID, instanceId, 0, CHUNK_DATA);
		WorkChunkCreateEvent batchWorkChunk = new WorkChunkCreateEvent(JOB_DEFINITION_ID, JOB_DEF_VER, TARGET_STEP_ID, instanceId, 0, CHUNK_DATA);
		String chunkId = mySvc.onWorkChunkCreate(batchWorkChunk);
		Optional<Batch2WorkChunkEntity> byId = myWorkChunkRepository.findById(chunkId);
		Batch2WorkChunkEntity entity = byId.get();
		entity.setStatus(theStatus);
		myWorkChunkRepository.save(entity);

		// Execute
		Optional<WorkChunk> workChunk = mySvc.onWorkChunkDequeue(chunkId);

		// Verify
		boolean chunkStarted = workChunk.isPresent();
		assertThat(theShouldBeStartedByConsumer).isEqualTo(chunkStarted);
	}

	@Test
	public void testCancelInstance() {
		JobInstance instance = createInstance();
		String instanceId = mySvc.storeNewInstance(instance);

		JobOperationResultJson result = mySvc.cancelInstance(instanceId);

		assertThat(result.getSuccess()).isTrue();
		assertThat(result.getMessage()).isEqualTo("Job instance <" + instanceId + "> successfully cancelled.");

		JobInstance foundInstance = mySvc.fetchInstance(instanceId).orElseThrow(IllegalStateException::new);
		assertThat(foundInstance.getInstanceId()).isEqualTo(instanceId);
		assertThat(foundInstance.getJobDefinitionId()).isEqualTo(JOB_DEFINITION_ID);
		assertThat(foundInstance.getJobDefinitionVersion()).isEqualTo(JOB_DEF_VER);
		assertThat(foundInstance.getStatus()).isEqualTo(StatusEnum.QUEUED);
		assertThat(foundInstance.isCancelled()).isTrue();
		assertThat(foundInstance.getParameters()).isEqualTo(CHUNK_DATA);

	}

	@Test
	void testFetchInstancesByJobDefinitionId() {
		JobInstance instance = createInstance();
		String instanceId = mySvc.storeNewInstance(instance);

		List<JobInstance> foundInstances = mySvc.fetchInstancesByJobDefinitionId(JOB_DEFINITION_ID, 10, 0);
		assertThat(foundInstances).hasSize(1);
		assertThat(foundInstances.get(0).getInstanceId()).isEqualTo(instanceId);
	}

	@Test
	void testFetchInstancesByJobDefinitionIdAndStatus() {
		JobInstance instance = createInstance();
		String instanceId = mySvc.storeNewInstance(instance);

		Set<StatusEnum> statuses = new HashSet<>();
		statuses.add(StatusEnum.QUEUED);
		statuses.add(StatusEnum.COMPLETED);
		List<JobInstance> foundInstances = mySvc.fetchInstancesByJobDefinitionIdAndStatus(JOB_DEFINITION_ID, statuses, 10, 0);
		assertThat(foundInstances).hasSize(1);
		assertThat(foundInstances.get(0).getInstanceId()).isEqualTo(instanceId);
	}

	@Test
	void testFetchInstancesWithEmptyStatus() {
		createTwoJobsDifferentStatus();
		JobInstanceFetchRequest request = createFetchRequest();

		// Test
		request.setJobStatus("");
		Page<JobInstance> foundInstances = mySvc.fetchJobInstances(request);
		assertThat(foundInstances.getTotalElements()).isEqualTo(2L);
	}

	@Test
	void testFetchInstanceByStatus() {
		createTwoJobsDifferentStatus();
		JobInstanceFetchRequest request = createFetchRequest();

		// Test
		request.setJobStatus("COMPLETED");
		Page<JobInstance> foundInstances = mySvc.fetchJobInstances(request);
		assertThat(foundInstances.getTotalElements()).isEqualTo(1L);
	}

	private JobInstanceFetchRequest createFetchRequest() {
		JobInstanceFetchRequest request = new JobInstanceFetchRequest();
		request.setPageStart(0);
		request.setBatchSize(1);
		request.setSort(Sort.by(Sort.Direction.DESC, "myCreateTime"));
		return request;
	}

	private void createTwoJobsDifferentStatus() {
		JobInstance instance = new JobInstance();
		instance.setStatus(StatusEnum.QUEUED);
		instance.setJobDefinitionId(JOB_DEFINITION_ID);
		JobInstance instance2 = new JobInstance();
		instance2.setStatus(StatusEnum.COMPLETED);
		instance2.setJobDefinitionId(JOB_DEFINITION_ID + "-2");

		mySvc.storeNewInstance(instance);
		mySvc.storeNewInstance(instance2);
	}

	/**
	 * Test bodies are defined in {@link AbstractIJobPersistenceSpecificationTest}.
	 * The nested test suite runs those tests here in a JPA context.
	 */
	@Nested
	class Batch2SpecTest extends AbstractIJobPersistenceSpecificationTest {

		@Override
		protected PlatformTransactionManager getTxManager() {
			return JpaJobPersistenceImplTest.this.getTxManager();
		}

		@Override
		protected WorkChunk freshFetchWorkChunk(String chunkId) {
			return JpaJobPersistenceImplTest.this.freshFetchWorkChunk(chunkId);
		}
	}

	@Test
	public void testUpdateTime() {
		// Setup
		JobInstance instance = createInstance();
		String instanceId = mySvc.storeNewInstance(instance);

		Date updateTime = runInTransaction(() -> new Date(myJobInstanceRepository.findById(instanceId).orElseThrow().getUpdateTime().getTime()));

		sleepUntilTimeChanges();

		// Test
		runInTransaction(() -> mySvc.updateInstanceUpdateTime(instanceId));

		// Verify
		Date updateTime2 = runInTransaction(() -> new Date(myJobInstanceRepository.findById(instanceId).orElseThrow().getUpdateTime().getTime()));
		assertThat(updateTime2).isNotEqualTo(updateTime);
	}

	@Test
	public void testFetchUnknownWork() {
		assertThat(myWorkChunkRepository.findById("FOO").isPresent()).isFalse();
	}

	@Test
	public void testStoreAndFetchWorkChunk_NoData() {
		JobInstance instance = createInstance();
		String instanceId = mySvc.storeNewInstance(instance);

		String id = storeWorkChunk(JOB_DEFINITION_ID, TARGET_STEP_ID, instanceId, 0, null);

		WorkChunk chunk = mySvc.onWorkChunkDequeue(id).orElseThrow(IllegalArgumentException::new);
		assertThat(chunk.getData()).isNull();
	}

	@Test
	void testStoreAndFetchChunksForInstance_NoData() {
	    // given
		JobInstance instance = createInstance();
		String instanceId = mySvc.storeNewInstance(instance);

		String queuedId = storeWorkChunk(JOB_DEFINITION_ID, TARGET_STEP_ID, instanceId, 0, "some data");
		String erroredId = storeWorkChunk(JOB_DEFINITION_ID, TARGET_STEP_ID, instanceId, 1, "some more data");
		String completedId = storeWorkChunk(JOB_DEFINITION_ID, TARGET_STEP_ID, instanceId, 2, "some more data");

		mySvc.onWorkChunkDequeue(erroredId);
		WorkChunkErrorEvent parameters = new WorkChunkErrorEvent(erroredId, "Our error message");
		mySvc.onWorkChunkError(parameters);

		mySvc.onWorkChunkDequeue(completedId);
		mySvc.onWorkChunkCompletion(new WorkChunkCompletionEvent(completedId, 11, 0));

	    // when
		Iterator<WorkChunk> workChunks = mySvc.fetchAllWorkChunksIterator(instanceId, false);

		// then
		ArrayList<WorkChunk> chunks = new ArrayList<>();
		Iterators.addAll(chunks, workChunks);
		assertThat(chunks).hasSize(3);

		{
			WorkChunk workChunk = chunks.get(0);
			assertThat(workChunk.getData()).as("we skip the data").isNull();
			assertThat(workChunk.getId()).isEqualTo(queuedId);
			assertThat(workChunk.getJobDefinitionId()).isEqualTo(JOB_DEFINITION_ID);
			assertThat(workChunk.getJobDefinitionVersion()).isEqualTo(JOB_DEF_VER);
			assertThat(workChunk.getInstanceId()).isEqualTo(instanceId);
			assertThat(workChunk.getTargetStepId()).isEqualTo(TARGET_STEP_ID);
			assertThat(workChunk.getSequence()).isEqualTo(0);
			assertThat(workChunk.getStatus()).isEqualTo(WorkChunkStatusEnum.QUEUED);


			assertThat(workChunk.getCreateTime()).isNotNull();
			assertThat(workChunk.getStartTime()).isNotNull();
			assertThat(workChunk.getUpdateTime()).isNotNull();
			assertThat(workChunk.getEndTime()).isNull();
			assertThat(workChunk.getErrorMessage()).isNull();
			assertThat(workChunk.getErrorCount()).isEqualTo(0);
			assertThat(workChunk.getRecordsProcessed()).isEqualTo(null);
		}

		{
			WorkChunk workChunk1 = chunks.get(1);
			assertThat(workChunk1.getStatus()).isEqualTo(WorkChunkStatusEnum.ERRORED);
			assertThat(workChunk1.getErrorMessage()).isEqualTo("Our error message");
			assertThat(workChunk1.getErrorCount()).isEqualTo(1);
			assertThat(workChunk1.getRecordsProcessed()).isEqualTo(null);
			assertThat(workChunk1.getEndTime()).isNotNull();
		}

		{
			WorkChunk workChunk2 = chunks.get(2);
			assertThat(workChunk2.getStatus()).isEqualTo(WorkChunkStatusEnum.COMPLETED);
			assertThat(workChunk2.getEndTime()).isNotNull();
			assertThat(workChunk2.getRecordsProcessed()).isEqualTo(11);
			assertThat(workChunk2.getErrorMessage()).isNull();
			assertThat(workChunk2.getErrorCount()).isEqualTo(0);
		}

	}


	@Test
	public void testStoreAndFetchWorkChunk_WithData() {
		JobInstance instance = createInstance();
		String instanceId = mySvc.storeNewInstance(instance);

		String id = storeWorkChunk(JOB_DEFINITION_ID, TARGET_STEP_ID, instanceId, 0, CHUNK_DATA);
		assertThat(id).isNotNull();
		runInTransaction(() -> assertEquals(WorkChunkStatusEnum.QUEUED, myWorkChunkRepository.findById(id).orElseThrow(IllegalArgumentException::new).getStatus()));

		WorkChunk chunk = mySvc.onWorkChunkDequeue(id).orElseThrow(IllegalArgumentException::new);
		assertThat(chunk.getInstanceId()).hasSize(36);
		assertThat(chunk.getJobDefinitionId()).isEqualTo(JOB_DEFINITION_ID);
		assertThat(chunk.getJobDefinitionVersion()).isEqualTo(JOB_DEF_VER);
		assertThat(chunk.getStatus()).isEqualTo(WorkChunkStatusEnum.IN_PROGRESS);
		assertThat(chunk.getData()).isEqualTo(CHUNK_DATA);

		runInTransaction(() -> assertEquals(WorkChunkStatusEnum.IN_PROGRESS, myWorkChunkRepository.findById(id).orElseThrow(IllegalArgumentException::new).getStatus()));
	}

	@Test
	public void testMarkChunkAsCompleted_Success() {
		JobInstance instance = createInstance();
		String instanceId = mySvc.storeNewInstance(instance);
		String chunkId = storeWorkChunk(DEF_CHUNK_ID, STEP_CHUNK_ID, instanceId, SEQUENCE_NUMBER, CHUNK_DATA);
		assertThat(chunkId).isNotNull();

			runInTransaction(() -> assertEquals(WorkChunkStatusEnum.QUEUED, myWorkChunkRepository.findById(chunkId).orElseThrow(IllegalArgumentException::new).getStatus()));

		sleepUntilTimeChanges();

		WorkChunk chunk = mySvc.onWorkChunkDequeue(chunkId).orElseThrow(IllegalArgumentException::new);
		assertThat(chunk.getSequence()).isEqualTo(SEQUENCE_NUMBER);
		assertThat(chunk.getStatus()).isEqualTo(WorkChunkStatusEnum.IN_PROGRESS);
		assertThat(chunk.getCreateTime()).isNotNull();
		assertThat(chunk.getStartTime()).isNotNull();
		assertThat(chunk.getEndTime()).isNull();
		assertThat(chunk.getRecordsProcessed()).isNull();
		assertThat(chunk.getData()).isNotNull();
		runInTransaction(() -> assertEquals(WorkChunkStatusEnum.IN_PROGRESS, myWorkChunkRepository.findById(chunkId).orElseThrow(IllegalArgumentException::new).getStatus()));

		sleepUntilTimeChanges();

		mySvc.onWorkChunkCompletion(new WorkChunkCompletionEvent(chunkId, 50, 0));
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
	public void testGatedAdvancementByStatus() {
		// Setup
		JobInstance instance = createInstance();
		String instanceId = mySvc.storeNewInstance(instance);
		String chunkId = storeWorkChunk(DEF_CHUNK_ID, STEP_CHUNK_ID, instanceId, SEQUENCE_NUMBER, null);
		mySvc.onWorkChunkCompletion(new WorkChunkCompletionEvent(chunkId, 0, 0));

		boolean canAdvance = mySvc.canAdvanceInstanceToNextStep(instanceId, STEP_CHUNK_ID);
		assertThat(canAdvance).isTrue();

		//Storing a new chunk with QUEUED should prevent advancement.
		String newChunkId = storeWorkChunk(DEF_CHUNK_ID, STEP_CHUNK_ID, instanceId, SEQUENCE_NUMBER, null);

		canAdvance = mySvc.canAdvanceInstanceToNextStep(instanceId, STEP_CHUNK_ID);
		assertThat(canAdvance).isFalse();

		//Toggle it to complete
		mySvc.onWorkChunkCompletion(new WorkChunkCompletionEvent(newChunkId, 50, 0));
		canAdvance = mySvc.canAdvanceInstanceToNextStep(instanceId, STEP_CHUNK_ID);
		assertThat(canAdvance).isTrue();

		//Create a new chunk and set it in progress.
		String newerChunkId = storeWorkChunk(DEF_CHUNK_ID, STEP_CHUNK_ID, instanceId, SEQUENCE_NUMBER, null);
		mySvc.onWorkChunkDequeue(newerChunkId);
		canAdvance = mySvc.canAdvanceInstanceToNextStep(instanceId, STEP_CHUNK_ID);
		assertThat(canAdvance).isFalse();

		//Toggle IN_PROGRESS to complete
		mySvc.onWorkChunkCompletion(new WorkChunkCompletionEvent(newerChunkId, 50, 0));
		canAdvance = mySvc.canAdvanceInstanceToNextStep(instanceId, STEP_CHUNK_ID);
		assertThat(canAdvance).isTrue();
	}

	@Test
	public void testMarkChunkAsCompleted_Error() {
		JobInstance instance = createInstance();
		String instanceId = mySvc.storeNewInstance(instance);
		String chunkId = storeWorkChunk(DEF_CHUNK_ID, STEP_CHUNK_ID, instanceId, SEQUENCE_NUMBER, null);
		assertThat(chunkId).isNotNull();

		runInTransaction(() -> assertEquals(WorkChunkStatusEnum.QUEUED, myWorkChunkRepository.findById(chunkId).orElseThrow(IllegalArgumentException::new).getStatus()));

		sleepUntilTimeChanges();

		WorkChunk chunk = mySvc.onWorkChunkDequeue(chunkId).orElseThrow(IllegalArgumentException::new);
		assertThat(chunk.getSequence()).isEqualTo(SEQUENCE_NUMBER);
		assertThat(chunk.getStatus()).isEqualTo(WorkChunkStatusEnum.IN_PROGRESS);

		sleepUntilTimeChanges();

		WorkChunkErrorEvent request = new WorkChunkErrorEvent(chunkId).setErrorMsg("This is an error message");
		mySvc.onWorkChunkError(request);
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

		WorkChunkErrorEvent request2 = new WorkChunkErrorEvent(chunkId).setErrorMsg("This is an error message 2");
		mySvc.onWorkChunkError(request2);
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

		runInTransaction(() -> assertEquals(WorkChunkStatusEnum.QUEUED, myWorkChunkRepository.findById(chunkId).orElseThrow(IllegalArgumentException::new).getStatus()));

		sleepUntilTimeChanges();

		WorkChunk chunk = mySvc.onWorkChunkDequeue(chunkId).orElseThrow(IllegalArgumentException::new);
		assertThat(chunk.getSequence()).isEqualTo(SEQUENCE_NUMBER);
		assertThat(chunk.getStatus()).isEqualTo(WorkChunkStatusEnum.IN_PROGRESS);

		sleepUntilTimeChanges();

		mySvc.onWorkChunkFailed(chunkId, "This is an error message");
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

	@Test
	public void testPrestorageInterceptor_whenModifyingJobInstance_modifiedJobInstanceIsPersisted(){
		String expectedTriggeringUserName = "bobTheUncle";

		IAnonymousInterceptor prestorageBatchJobCreateInterceptor = (pointcut, params) -> {
			JobInstance jobInstance = params.get(JobInstance.class);
			jobInstance.setTriggeringUsername(expectedTriggeringUserName);
		};

		try{
			myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRESTORAGE_BATCH_JOB_CREATE, prestorageBatchJobCreateInterceptor);
			JobInstance instance = createInstance();
			String instanceId = mySvc.storeNewInstance(instance);

			JobInstance foundInstance = mySvc.fetchInstance(instanceId).orElseThrow(IllegalStateException::new);

			assertThat(foundInstance.getTriggeringUsername()).isEqualTo(expectedTriggeringUserName);

		} finally {
			myInterceptorRegistry.unregisterInterceptor(prestorageBatchJobCreateInterceptor);
		}

	}

	private WorkChunk freshFetchWorkChunk(String chunkId) {
		return runInTransaction(() ->
			myWorkChunkRepository.findById(chunkId)
				.map(JobInstanceUtil::fromEntityToWorkChunk)
				.orElseThrow(IllegalArgumentException::new));
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

		mySvc.updateInstance(id, instance->{
			instance.setEndTime(Date.from(Instant.now().minus(minutes, ChronoUnit.MINUTES)));
			return true;
		});


		return id;
	}

	/**
	 * Returns a set of statuses, and whether they should be successfully picked up and started by a consumer.
	 */
	public static List<Arguments> provideStatuses() {
		return List.of(
			Arguments.of(WorkChunkStatusEnum.QUEUED, true),
			Arguments.of(WorkChunkStatusEnum.IN_PROGRESS, true),
			Arguments.of(WorkChunkStatusEnum.ERRORED, true),
			Arguments.of(WorkChunkStatusEnum.FAILED, false),
			Arguments.of(WorkChunkStatusEnum.COMPLETED, false)
		);
	}
}
