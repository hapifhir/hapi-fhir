package ca.uhn.fhir.jpa.batch2;

import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.JobOperationResultJson;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.channel.BatchJobSender;
import ca.uhn.fhir.batch2.coordinator.JobDefinitionRegistry;
import ca.uhn.fhir.batch2.jobs.imprt.NdJsonFileJson;
import ca.uhn.fhir.batch2.model.BatchInstanceStatusDTO;
import ca.uhn.fhir.batch2.model.BatchWorkChunkStatusDTO;
import ca.uhn.fhir.batch2.model.ChunkOutcome;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkNotification;
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
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.jpa.test.config.Batch2FastSchedulerConfig;
import ca.uhn.fhir.testjob.TestJobDefinitionUtils;
import ca.uhn.fhir.testjob.models.FirstStepOutput;
import ca.uhn.fhir.testjob.models.TestJobParameters;
import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.hapi.fhir.batch2.test.AbstractIJobPersistenceSpecificationTest;
import ca.uhn.hapi.fhir.batch2.test.configs.SpyOverrideConfig;
import ca.uhn.test.concurrency.PointcutLatch;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@TestMethodOrder(MethodOrderer.MethodName.class)
@ContextConfiguration(classes = {
	Batch2FastSchedulerConfig.class
})
@Import(SpyOverrideConfig.class)
public class JpaJobPersistenceImplTest extends BaseJpaR4Test {

	public static final String JOB_DEFINITION_ID = "definition-id";
	private static final String THREE_STEP_JOB_DEF_ID = "three-step-gated-job";
	private static final String GATING_RACE_JOB_DEF_ID = "gating-race-job-def-id";
	private static final String REDUCTION_RACE_JOB_DEF_ID = "reduction-race-job-def-id";
	public static final String FIRST_STEP_ID = TestJobDefinitionUtils.FIRST_STEP_ID;
	private static final String SECOND_STEP_ID = "second-step";
	public static final String LAST_STEP_ID = TestJobDefinitionUtils.LAST_STEP_ID;
	public static final String DEF_CHUNK_ID = "definition-chunkId";
	public static final String STEP_CHUNK_ID = TestJobDefinitionUtils.FIRST_STEP_ID;
	public static final int JOB_DEF_VER = 1;
	public static final int SEQUENCE_NUMBER = 1;
	public static final String CHUNK_DATA = "{\"key\":\"value\"}";

	@Autowired
	private IJobPersistence mySvc;
	@Autowired
	private IBatch2WorkChunkRepository myWorkChunkRepository;
	@Autowired
	private IBatch2JobInstanceRepository myJobInstanceRepository;

	@Autowired
	public Batch2JobHelper myBatch2JobHelper;

	// this is our spy
	@Autowired
	private BatchJobSender myBatchSender;

	@Autowired
	private IJobMaintenanceService myMaintenanceService;

	@Autowired
	public JobDefinitionRegistry myJobDefinitionRegistry;

	@AfterEach
	public void after() {
		// Delete any instances created by SMILE-11603 race condition tests BEFORE
		// re-enabling maintenance, to prevent the background scheduler from processing
		// orphaned READY chunks and interfering with other tests' mock verifications.
		// Note: We keep the race job definitions registered to avoid HAPI-2043 errors
		// from in-flight async work channel messages that reference these job definitions.
		for (String raceJobDefId : List.of(GATING_RACE_JOB_DEF_ID, REDUCTION_RACE_JOB_DEF_ID)) {
			List<JobInstance> raceInstances = mySvc.fetchInstancesByJobDefinitionId(raceJobDefId, 100, 0);
			for (JobInstance raceInstance : raceInstances) {
				mySvc.deleteInstanceAndChunks(raceInstance.getInstanceId());
			}
		}
		clearInvocations(myBatchSender);
		myJobDefinitionRegistry.removeJobDefinition(JOB_DEFINITION_ID, JOB_DEF_VER);
		myJobDefinitionRegistry.removeJobDefinition(THREE_STEP_JOB_DEF_ID, JOB_DEF_VER);
		myMaintenanceService.enableMaintenancePass(true);
	}

	@Test
	public void testDeleteInstance() {
		// Setup

		JobInstance instance = createInstance();
		String instanceId = mySvc.storeNewInstance(newSrd(), instance);
		for (int i = 0; i < 10; i++) {
			storeWorkChunk(JOB_DEFINITION_ID, FIRST_STEP_ID, instanceId, i, JsonUtil.serialize(new NdJsonFileJson().setNdJsonText("{}")), false);
		}

		// Execute

		mySvc.deleteInstanceAndChunks(instanceId);

		// Verify

		runInTransaction(() -> {
			assertEquals(0, myJobInstanceRepository.findAll().size());
			assertEquals(0, myWorkChunkRepository.findAll().size());
		});
	}

	private String storeWorkChunk(String theJobDefinitionId, String theTargetStepId, String theInstanceId, int theSequence, String theSerializedData, boolean theGatedExecution) {
		WorkChunkCreateEvent batchWorkChunk = new WorkChunkCreateEvent(theJobDefinitionId, TestJobDefinitionUtils.TEST_JOB_VERSION, theTargetStepId, theInstanceId, theSequence, theSerializedData, theGatedExecution);
		return mySvc.onWorkChunkCreate(batchWorkChunk);
	}

	private String storeFirstWorkChunk(String theJobDefinitionId, String theTargetStepId, String theInstanceId, int theSequence, String theSerializedData) {
		WorkChunkCreateEvent batchWorkChunk = new WorkChunkCreateEvent(theJobDefinitionId, TestJobDefinitionUtils.TEST_JOB_VERSION, theTargetStepId, theInstanceId, theSequence, theSerializedData, false);
		return mySvc.onWorkChunkCreate(batchWorkChunk);
	}

	@Test
	public void testStoreAndFetchInstance() {
		JobInstance instance = createInstance();
		String instanceId = mySvc.storeNewInstance(newSrd(), instance);

		runInTransaction(() -> {
			Batch2JobInstanceEntity instanceEntity = findInstanceByIdOrThrow(instanceId);
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
			Batch2JobInstanceEntity instanceEntity = findInstanceByIdOrThrow(instanceId);
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
	public void testStartChunkOnlyWorksOnValidChunks(WorkChunkStatusEnum theStatus, boolean theShouldBeStartedByConsumer) throws InterruptedException {
		// Setup
		JobInstance instance = createInstance();
		myMaintenanceService.enableMaintenancePass(false);
		String instanceId = mySvc.storeNewInstance(newSrd(), instance);

		storeWorkChunk(JOB_DEFINITION_ID, FIRST_STEP_ID, instanceId, 0, CHUNK_DATA, false);
		WorkChunkCreateEvent batchWorkChunk = new WorkChunkCreateEvent(JOB_DEFINITION_ID, JOB_DEF_VER, FIRST_STEP_ID, instanceId, 0, CHUNK_DATA, false);
		String chunkId = mySvc.onWorkChunkCreate(batchWorkChunk);
		Optional<Batch2WorkChunkEntity> byId = myWorkChunkRepository.findById(chunkId);
		Batch2WorkChunkEntity entity = byId.get();
		entity.setStatus(theStatus);
		myWorkChunkRepository.save(entity);

		// Execute
		Optional<WorkChunk> workChunk = mySvc.onWorkChunkDequeue(chunkId);

		// Verify
		boolean chunkStarted = workChunk.isPresent();
		assertEquals(theShouldBeStartedByConsumer, chunkStarted);
		verify(myBatchSender, never())
			.sendWorkChannelMessage(any());
	}

	@Test
	public void testCancelInstance() {
		JobInstance instance = createInstance();
		String instanceId = mySvc.storeNewInstance(newSrd(), instance);

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
		String instanceId = mySvc.storeNewInstance(newSrd(), instance);

		List<JobInstance> foundInstances = mySvc.fetchInstancesByJobDefinitionId(JOB_DEFINITION_ID, 10, 0);
		assertThat(foundInstances).hasSize(1);
		assertEquals(instanceId, foundInstances.get(0).getInstanceId());
	}

	@Test
	void testFetchInstancesByJobDefinitionIdAndStatus() {
		JobInstance instance = createInstance();
		String instanceId = mySvc.storeNewInstance(newSrd(), instance);

		Set<StatusEnum> statuses = new HashSet<>();
		statuses.add(StatusEnum.QUEUED);
		statuses.add(StatusEnum.COMPLETED);
		List<JobInstance> foundInstances = mySvc.fetchInstancesByJobDefinitionIdAndStatus(JOB_DEFINITION_ID, statuses, 10, 0);
		assertThat(foundInstances).hasSize(1);
		assertEquals(instanceId, foundInstances.get(0).getInstanceId());
	}

	@Test
	void testFetchFilteredInstances_noFilter_allJobsFound() {
		// Setup
		createTwoJobsDifferentStatus();
		JobInstanceFetchRequest request = createFetchRequest();

		// Execute
		Page<JobInstance> foundInstances = mySvc.fetchJobInstances(request);

		// Verify
		assertEquals(2L, foundInstances.getTotalElements());
	}

	@Test
	void testFetchFilteredInstancesByStatus() {
		// Setup
		createTwoJobsDifferentStatus();
		JobInstanceFetchRequest request = createFetchRequest();
		request.setJobStatus(StatusEnum.COMPLETED.name());

		// Execute
		Page<JobInstance> foundInstances = mySvc.fetchJobInstances(request);

		// Verify
		assertEquals(1L, foundInstances.getTotalElements());
	}

	@Test
	void testFetchFilteredInstancesByJobType() {
		// Setup
		createTwoJobsDifferentStatus();
		JobInstanceFetchRequest request = createFetchRequest();
		request.setJobDefinitionId(JOB_DEFINITION_ID);

		// Execute
		Page<JobInstance> foundInstances = mySvc.fetchJobInstances(request);

		// Verify
		assertEquals(1L, foundInstances.getTotalElements());
	}

	@Test
	void testFetchFilteredInstancesByJobStatusAndJobType() {
		// Setup
		createTwoJobsDifferentStatus();
		JobInstanceFetchRequest request = createFetchRequest();
		request.setJobStatus(StatusEnum.QUEUED.name());
		request.setJobDefinitionId(JOB_DEFINITION_ID);

		// Execute
		Page<JobInstance> foundInstances = mySvc.fetchJobInstances(request);

		// Verify
		assertEquals(1L, foundInstances.getTotalElements());
	}

	@Test
	void testFetchFilteredInstanceByJobId() {
		// Setup
		createTwoJobsDifferentStatus();
		JobInstanceFetchRequest request = createFetchRequest();

		Page<JobInstance> foundInstances = mySvc.fetchJobInstances(request);
		assertEquals(2L, foundInstances.getTotalElements());
		String jobId = foundInstances.getContent().get(0).getInstanceId();
		request.setJobId(jobId);

		// Execute
		foundInstances = mySvc.fetchJobInstances(request);

		// Verify
		assertEquals(1L, foundInstances.getTotalElements());
	}

	@Test
	void testFetchFilteredInstancesByCreateTime_withDateRangeBeyondAnyJobs_findNothing() {
		// Setup
		createTwoJobsDifferentStatus();
		JobInstanceFetchRequest request = createFetchRequest();

		ZonedDateTime startDt = ZonedDateTime.now().minusDays(1).with(LocalTime.MIN);
		ZonedDateTime endDt = ZonedDateTime.now().minusDays(1).with(LocalTime.MAX);
		request.setJobCreateTimeFrom(Date.from(startDt.toInstant()));
		request.setJobCreateTimeTo(Date.from(endDt.toInstant()));

		// Execute
		Page<JobInstance> foundInstances = mySvc.fetchJobInstances(request);

		// Verify
		assertEquals(0L, foundInstances.getTotalElements());
	}

	@Test
	void testFetchFilteredInstancesByCreateTime_withValidStartAndEndDate_findAllInRange() {
		// Setup
		createTwoJobsDifferentStatus();
		JobInstanceFetchRequest request = createFetchRequest();

		ZonedDateTime startDt = ZonedDateTime.now().minusDays(1);
		ZonedDateTime endDt = ZonedDateTime.now().with(LocalTime.MAX);
		request.setJobCreateTimeFrom(Date.from(startDt.toInstant()));
		request.setJobCreateTimeTo(Date.from(endDt.toInstant()));

		// Execute
		Page<JobInstance> foundInstances = mySvc.fetchJobInstances(request);

		// Verify
		assertEquals(2L, foundInstances.getTotalElements());
	}

	@Test
	void testFetchFilteredInstancesByCreateTime_withStartDateOnly_findJobsAfter() {
		// Setup
		createTwoJobsDifferentStatus();
		JobInstanceFetchRequest request = createFetchRequest();

		ZonedDateTime startDt = ZonedDateTime.now().minusDays(1);
		request.setJobCreateTimeFrom(Date.from(startDt.toInstant()));

		// Execute
		Page<JobInstance> foundInstances = mySvc.fetchJobInstances(request);

		// Verify
		assertEquals(2L, foundInstances.getTotalElements());
	}

	@Test
	void testFetchFilteredInstancesByCreateTime_withStartDateBeyond_findNothing() {
		// Setup
		createTwoJobsDifferentStatus();
		JobInstanceFetchRequest request = createFetchRequest();

		ZonedDateTime startDt = ZonedDateTime.now().plusHours(1);
		request.setJobCreateTimeFrom(Date.from(startDt.toInstant()));

		// Execute
		Page<JobInstance> foundInstances = mySvc.fetchJobInstances(request);

		// Verify
		assertEquals(0L, foundInstances.getTotalElements());
	}

	@Test
	void testFetchFilteredInstancesByCreateTime_withEndDateOnly_findJobsBefore() {
		// Setup
		createTwoJobsDifferentStatus();
		JobInstanceFetchRequest request = createFetchRequest();

		ZonedDateTime endDt = ZonedDateTime.now().with(LocalTime.MAX);
		request.setJobCreateTimeTo(Date.from(endDt.toInstant()));

		// Execute
		Page<JobInstance> foundInstances = mySvc.fetchJobInstances(request);

		// Verify
		assertEquals(2L, foundInstances.getTotalElements());
	}

	@Test
	void testFetchFilteredInstancesByCreateTime_withEndDateBeyond_findNothing() {
		// Setup
		createTwoJobsDifferentStatus();
		JobInstanceFetchRequest request = createFetchRequest();

		ZonedDateTime endDt = ZonedDateTime.now().minusHours(1);
		request.setJobCreateTimeTo(Date.from(endDt.toInstant()));

		// Execute
		Page<JobInstance> foundInstances = mySvc.fetchJobInstances(request);

		// Verify
		assertEquals(0L, foundInstances.getTotalElements());
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

		mySvc.storeNewInstance(newSrd(), instance);
		mySvc.storeNewInstance(newSrd(), instance2);
	}

	/**
	 * Test bodies are defined in {@link AbstractIJobPersistenceSpecificationTest}.
	 * The nested test suite runs those tests here in a JPA context.
	 */
	@Nested
	class Batch2SpecTest extends AbstractIJobPersistenceSpecificationTest {

		@Override
		public PlatformTransactionManager getTxManager() {
			return JpaJobPersistenceImplTest.this.getTxManager();
		}

		@Override
		public WorkChunk freshFetchWorkChunk(String chunkId) {
			return JpaJobPersistenceImplTest.this.freshFetchWorkChunk(chunkId);
		}

		@Override
		public void runMaintenancePass() {
			myBatch2JobHelper.forceRunMaintenancePass();
		}
	}

	@Test
	public void testUpdateTime() {
		// Setup
		boolean isGatedExecution = false;
		JobInstance instance = createInstance(true, isGatedExecution);
		String instanceId = mySvc.storeNewInstance(newSrd(), instance);

		Date updateTime = runInTransaction(() -> new Date(findInstanceByIdOrThrow(instanceId).getUpdateTime().getTime()));

		sleepUntilTimeChange();

		// Test
		runInTransaction(() -> mySvc.updateInstanceUpdateTime(instanceId));

		// Verify
		Date updateTime2 = runInTransaction(() -> new Date(findInstanceByIdOrThrow(instanceId).getUpdateTime().getTime()));
		assertNotEquals(updateTime, updateTime2);
	}

	@Test
	public void advanceJobStepAndUpdateChunkStatus_forGatedJobWithoutReduction_updatesCurrentStepAndChunkStatus() {
		// setup
		boolean isGatedExecution = true;
		JobInstance instance = createInstance(true, isGatedExecution);
		String instanceId = mySvc.storeNewInstance(newSrd(), instance);
		String chunkIdSecondStep1 = storeWorkChunk(JOB_DEFINITION_ID, LAST_STEP_ID, instanceId, 0, null, isGatedExecution);
		String chunkIdSecondStep2 = storeWorkChunk(JOB_DEFINITION_ID, LAST_STEP_ID, instanceId, 0, null, isGatedExecution);

		runInTransaction(() -> assertEquals(FIRST_STEP_ID, findInstanceByIdOrThrow(instanceId).getCurrentGatedStepId()));

		// execute
		runInTransaction(() -> {
			boolean changed = mySvc.advanceJobStepAndUpdateChunkStatus(instanceId, LAST_STEP_ID, false);
			assertTrue(changed);
		});

		// verify
		runInTransaction(() -> {
			assertEquals(WorkChunkStatusEnum.READY, findChunkByIdOrThrow(chunkIdSecondStep1).getStatus());
			assertEquals(WorkChunkStatusEnum.READY, findChunkByIdOrThrow(chunkIdSecondStep2).getStatus());
			assertEquals(LAST_STEP_ID, findInstanceByIdOrThrow(instanceId).getCurrentGatedStepId());
		});
	}

	@Test
	public void advanceJobStepAndUpdateChunkStatus_whenAlreadyInTargetStep_DoesNotUpdateStepOrChunks() {
		// setup
		boolean isGatedExecution = true;
		JobInstance instance = createInstance(true, isGatedExecution);
		String instanceId = mySvc.storeNewInstance(newSrd(), instance);
		String chunkIdSecondStep1 = storeWorkChunk(JOB_DEFINITION_ID, LAST_STEP_ID, instanceId, 0, null, isGatedExecution);
		String chunkIdSecondStep2 = storeWorkChunk(JOB_DEFINITION_ID, LAST_STEP_ID, instanceId, 0, null, isGatedExecution);

		runInTransaction(() -> assertEquals(FIRST_STEP_ID, findInstanceByIdOrThrow(instanceId).getCurrentGatedStepId()));

		// execute
		runInTransaction(() -> {
			boolean changed = mySvc.advanceJobStepAndUpdateChunkStatus(instanceId, FIRST_STEP_ID, false);
			assertFalse(changed);
		});

		// verify
		runInTransaction(() -> {
			assertEquals(WorkChunkStatusEnum.GATE_WAITING, findChunkByIdOrThrow(chunkIdSecondStep1).getStatus());
			assertEquals(WorkChunkStatusEnum.GATE_WAITING, findChunkByIdOrThrow(chunkIdSecondStep2).getStatus());
			assertEquals(FIRST_STEP_ID, findInstanceByIdOrThrow(instanceId).getCurrentGatedStepId());
		});
	}

	@Test
	public void testFetchUnknownWork() {
		assertFalse(myWorkChunkRepository.findById("FOO").isPresent());
	}

	@ParameterizedTest
	@CsvSource({
		"false, READY, QUEUED",
		"true, GATE_WAITING, QUEUED"
	})
	public void testStoreAndFetchWorkChunk_withOrWithoutGatedExecutionNoData_createdAndTransitionToExpectedStatus(boolean theGatedExecution, WorkChunkStatusEnum theExpectedStatusOnCreate, WorkChunkStatusEnum theExpectedStatusAfterTransition) throws InterruptedException {
		// setup
		JobInstance instance = createInstance(true, theGatedExecution);

		// when
		PointcutLatch latch = new PointcutLatch("senderlatch");
		doAnswer(a -> {
			latch.call(1);
			return Void.class;
		}).when(myBatchSender).sendWorkChannelMessage(any(JobWorkNotification.class));
		latch.setExpectedCount(1);
		myMaintenanceService.enableMaintenancePass(false);
		String instanceId = mySvc.storeNewInstance(newSrd(), instance);

		// execute & verify
		String firstChunkId = storeFirstWorkChunk(JOB_DEFINITION_ID, FIRST_STEP_ID, instanceId, 0, null);
		// mark the first chunk as COMPLETED to allow step advance
		runInTransaction(() -> myWorkChunkRepository.updateChunkStatus(firstChunkId, WorkChunkStatusEnum.READY, WorkChunkStatusEnum.COMPLETED));

		String id = storeWorkChunk(JOB_DEFINITION_ID, LAST_STEP_ID, instanceId, 0, null, theGatedExecution);
		runInTransaction(() -> assertEquals(theExpectedStatusOnCreate, findChunkByIdOrThrow(id).getStatus()));
		myBatch2JobHelper.runMaintenancePass();
		runInTransaction(() -> assertEquals(theExpectedStatusAfterTransition, findChunkByIdOrThrow(id).getStatus()));

		WorkChunk chunk = mySvc.onWorkChunkDequeue(id).orElseThrow(IllegalArgumentException::new);
		// assert null since we did not input any data when creating the chunks
		assertNull(chunk.getData());

		latch.awaitExpected();
		verify(myBatchSender).sendWorkChannelMessage(any());
		clearInvocations(myBatchSender);
	}

	@Test
	public void testStoreAndFetchWorkChunk_withGatedJobMultipleChunk_correctTransitions() throws InterruptedException {
		// setup
		boolean isGatedExecution = true;
		String expectedFirstChunkData = "IAmChunk1";
		String expectedSecondChunkData = "IAmChunk2";
		JobInstance instance = createInstance(true, isGatedExecution);
		myMaintenanceService.enableMaintenancePass(false);
		String instanceId = mySvc.storeNewInstance(newSrd(), instance);
		PointcutLatch latch = new PointcutLatch("senderlatch");
		doAnswer(a -> {
			latch.call(1);
			return Void.class;
		}).when(myBatchSender).sendWorkChannelMessage(any(JobWorkNotification.class));
		latch.setExpectedCount(2);

		// execute & verify
		String firstChunkId = storeFirstWorkChunk(JOB_DEFINITION_ID, FIRST_STEP_ID, instanceId, 0, expectedFirstChunkData);
		String secondChunkId = storeWorkChunk(JOB_DEFINITION_ID, LAST_STEP_ID, instanceId, 0, expectedSecondChunkData, isGatedExecution);

		runInTransaction(() -> {
			// check chunks created in expected states
			assertEquals(WorkChunkStatusEnum.READY, findChunkByIdOrThrow(firstChunkId).getStatus());
			assertEquals(WorkChunkStatusEnum.GATE_WAITING, findChunkByIdOrThrow(secondChunkId).getStatus());
		});

		myBatch2JobHelper.runMaintenancePass();
		runInTransaction(() -> {
			assertEquals(WorkChunkStatusEnum.QUEUED, findChunkByIdOrThrow(firstChunkId).getStatus());
			// maintenance should not affect chunks in step 2
			assertEquals(WorkChunkStatusEnum.GATE_WAITING, findChunkByIdOrThrow(secondChunkId).getStatus());
		});

		WorkChunk actualFirstChunkData = mySvc.onWorkChunkDequeue(firstChunkId).orElseThrow(IllegalArgumentException::new);
		runInTransaction(() -> assertEquals(WorkChunkStatusEnum.IN_PROGRESS, findChunkByIdOrThrow(firstChunkId).getStatus()));
		assertEquals(expectedFirstChunkData, actualFirstChunkData.getData());

		mySvc.onWorkChunkCompletion(new WorkChunkCompletionEvent(firstChunkId, 50, 0));
		runInTransaction(() -> {
			assertEquals(WorkChunkStatusEnum.COMPLETED, findChunkByIdOrThrow(firstChunkId).getStatus());
			assertEquals(WorkChunkStatusEnum.GATE_WAITING, findChunkByIdOrThrow(secondChunkId).getStatus());
		});

		myBatch2JobHelper.runMaintenancePass();
		runInTransaction(() -> {
			assertEquals(WorkChunkStatusEnum.COMPLETED, findChunkByIdOrThrow(firstChunkId).getStatus());
			// now that all chunks for step 1 is COMPLETED, should enqueue chunks in step 2
			assertEquals(WorkChunkStatusEnum.QUEUED, findChunkByIdOrThrow(secondChunkId).getStatus());
		});

		WorkChunk actualSecondChunkData = mySvc.onWorkChunkDequeue(secondChunkId).orElseThrow(IllegalArgumentException::new);
		runInTransaction(() -> assertEquals(WorkChunkStatusEnum.IN_PROGRESS, findChunkByIdOrThrow(secondChunkId).getStatus()));
		assertEquals(expectedSecondChunkData, actualSecondChunkData.getData());

		latch.awaitExpected();
		verify(myBatchSender, times(2))
			.sendWorkChannelMessage(any());
		clearInvocations(myBatchSender);
	}

	@Test
	void testStoreAndFetchChunksForInstance_NoData() {
	    // given
		boolean isGatedExecution = false;
		JobInstance instance = createInstance();
		String instanceId = mySvc.storeNewInstance(newSrd(), instance);

		String queuedId = storeWorkChunk(JOB_DEFINITION_ID, FIRST_STEP_ID, instanceId, 0, "some data", isGatedExecution);
		String erroredId = storeWorkChunk(JOB_DEFINITION_ID, FIRST_STEP_ID, instanceId, 1, "some more data", isGatedExecution);
		String completedId = storeWorkChunk(JOB_DEFINITION_ID, FIRST_STEP_ID, instanceId, 2, "some more data", isGatedExecution);

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
			assertEquals(queuedId, workChunk.getId());
			assertEquals(JOB_DEFINITION_ID, workChunk.getJobDefinitionId());
			assertEquals(JOB_DEF_VER, workChunk.getJobDefinitionVersion());
			assertEquals(instanceId, workChunk.getInstanceId());
			assertEquals(FIRST_STEP_ID, workChunk.getTargetStepId());
			assertEquals(0, workChunk.getSequence());
			assertEquals(WorkChunkStatusEnum.READY, workChunk.getStatus());


			assertNotNull(workChunk.getCreateTime());
			assertNotNull(workChunk.getStartTime());
			assertNotNull(workChunk.getUpdateTime());
			assertNull(workChunk.getEndTime());
			assertNull(workChunk.getErrorMessage());
			assertEquals(0, workChunk.getErrorCount());
			assertNull(workChunk.getRecordsProcessed());
		}

		{
			WorkChunk workChunk1 = chunks.get(1);
			assertEquals(WorkChunkStatusEnum.ERRORED, workChunk1.getStatus());
			assertEquals("Our error message", workChunk1.getErrorMessage());
			assertEquals(1, workChunk1.getErrorCount());
			assertNull(workChunk1.getRecordsProcessed());
			assertNotNull(workChunk1.getEndTime());
		}

		{
			WorkChunk workChunk2 = chunks.get(2);
			assertEquals(WorkChunkStatusEnum.COMPLETED, workChunk2.getStatus());
			assertNotNull(workChunk2.getEndTime());
			assertEquals(11, workChunk2.getRecordsProcessed());
			assertNull(workChunk2.getErrorMessage());
			assertEquals(0, workChunk2.getErrorCount());
		}
	}

	@ParameterizedTest
	@CsvSource({
		"false, READY, QUEUED",
		"true, GATE_WAITING, QUEUED"
	})
	public void testStoreAndFetchWorkChunk_withOrWithoutGatedExecutionwithData_createdAndTransitionToExpectedStatus(boolean theGatedExecution, WorkChunkStatusEnum theExpectedCreatedStatus, WorkChunkStatusEnum theExpectedTransitionStatus) throws InterruptedException {
		// setup
		JobInstance instance = createInstance(true, theGatedExecution);
		myMaintenanceService.enableMaintenancePass(false);
		String instanceId = mySvc.storeNewInstance(newSrd(), instance);
		PointcutLatch latch = new PointcutLatch("senderlatch");
		doAnswer(a -> {
			latch.call(1);
			return Void.class;
		}).when(myBatchSender).sendWorkChannelMessage(any(JobWorkNotification.class));
		latch.setExpectedCount(1);

		// execute & verify
		String firstChunkId = storeFirstWorkChunk(JOB_DEFINITION_ID, FIRST_STEP_ID, instanceId, 0, null);
		// mark the first chunk as COMPLETED to allow step advance
		runInTransaction(() -> myWorkChunkRepository.updateChunkStatus(firstChunkId, WorkChunkStatusEnum.READY, WorkChunkStatusEnum.COMPLETED));

		String id = storeWorkChunk(JOB_DEFINITION_ID, LAST_STEP_ID, instanceId, 0, CHUNK_DATA, theGatedExecution);
		assertNotNull(id);
		runInTransaction(() -> assertEquals(theExpectedCreatedStatus, findChunkByIdOrThrow(id).getStatus()));
		myBatch2JobHelper.runMaintenancePass();
		runInTransaction(() -> assertEquals(theExpectedTransitionStatus, findChunkByIdOrThrow(id).getStatus()));

		WorkChunk chunk = mySvc.onWorkChunkDequeue(id).orElseThrow(IllegalArgumentException::new);
		assertThat(chunk.getInstanceId()).hasSize(36);
		assertEquals(JOB_DEFINITION_ID, chunk.getJobDefinitionId());
		assertEquals(JOB_DEF_VER, chunk.getJobDefinitionVersion());
		assertEquals(WorkChunkStatusEnum.IN_PROGRESS, chunk.getStatus());
		assertEquals(CHUNK_DATA, chunk.getData());

		runInTransaction(() -> assertEquals(WorkChunkStatusEnum.IN_PROGRESS, findChunkByIdOrThrow(id).getStatus()));
		latch.awaitExpected();
		verify(myBatchSender).sendWorkChannelMessage(any());
		clearInvocations(myBatchSender);
	}

	@Test
	public void testMarkChunkAsCompleted_Success() throws InterruptedException {
		boolean isGatedExecution = false;
		myMaintenanceService.enableMaintenancePass(false);
		JobInstance instance = createInstance(true, isGatedExecution);
		String instanceId = mySvc.storeNewInstance(newSrd(), instance);
		String chunkId = storeWorkChunk(DEF_CHUNK_ID, STEP_CHUNK_ID, instanceId, SEQUENCE_NUMBER, CHUNK_DATA, isGatedExecution);
		assertNotNull(chunkId);
		PointcutLatch latch = new PointcutLatch("senderlatch");
		doAnswer(a -> {
			latch.call(1);
			return Void.class;
		}).when(myBatchSender).sendWorkChannelMessage(any(JobWorkNotification.class));
		latch.setExpectedCount(1);

		runInTransaction(() -> assertEquals(WorkChunkStatusEnum.READY, findChunkByIdOrThrow(chunkId).getStatus()));
		myBatch2JobHelper.runMaintenancePass();
		runInTransaction(() -> assertEquals(WorkChunkStatusEnum.QUEUED, findChunkByIdOrThrow(chunkId).getStatus()));

		WorkChunk chunk = mySvc.onWorkChunkDequeue(chunkId).orElseThrow(IllegalArgumentException::new);
		assertEquals(SEQUENCE_NUMBER, chunk.getSequence());
		assertEquals(WorkChunkStatusEnum.IN_PROGRESS, chunk.getStatus());
		assertNotNull(chunk.getCreateTime());
		assertNotNull(chunk.getStartTime());
		assertNull(chunk.getEndTime());
		assertNull(chunk.getRecordsProcessed());
		assertNotNull(chunk.getData());
		runInTransaction(() -> assertEquals(WorkChunkStatusEnum.IN_PROGRESS, findChunkByIdOrThrow(chunkId).getStatus()));

		sleepUntilTimeChange();

		mySvc.onWorkChunkCompletion(new WorkChunkCompletionEvent(chunkId, 50, 0));
		runInTransaction(() -> {
			Batch2WorkChunkEntity entity = findChunkByIdOrThrow(chunkId);
			assertEquals(WorkChunkStatusEnum.COMPLETED, entity.getStatus());
			assertEquals(50, entity.getRecordsProcessed());
			assertNotNull(entity.getCreateTime());
			assertNotNull(entity.getStartTime());
			assertNotNull(entity.getEndTime());
			assertNull(entity.getSerializedData());
			assertTrue(entity.getCreateTime().getTime() < entity.getStartTime().getTime());
			assertTrue(entity.getStartTime().getTime() < entity.getEndTime().getTime());
		});
		latch.awaitExpected();
		verify(myBatchSender).sendWorkChannelMessage(any());
		clearInvocations(myBatchSender);
	}

	@Test
	public void testMarkChunkAsCompleted_Error() {
		boolean isGatedExecution = false;
		PointcutLatch latch = new PointcutLatch("senderlatch");
		doAnswer(a -> {
			latch.call(1);
			return Void.class;
		}).when(myBatchSender).sendWorkChannelMessage(any(JobWorkNotification.class));
		latch.setExpectedCount(1);
		myMaintenanceService.enableMaintenancePass(false);

		JobInstance instance = createInstance(true, isGatedExecution);
		String instanceId = mySvc.storeNewInstance(newSrd(), instance);
		String chunkId = storeWorkChunk(JOB_DEFINITION_ID, TestJobDefinitionUtils.FIRST_STEP_ID, instanceId, SEQUENCE_NUMBER, null, isGatedExecution);
		assertNotNull(chunkId);

		runInTransaction(() -> assertEquals(WorkChunkStatusEnum.READY, findChunkByIdOrThrow(chunkId).getStatus()));
		myBatch2JobHelper.runMaintenancePass();
		runInTransaction(() -> assertEquals(WorkChunkStatusEnum.QUEUED, findChunkByIdOrThrow(chunkId).getStatus()));

		WorkChunk chunk = mySvc.onWorkChunkDequeue(chunkId).orElseThrow(IllegalArgumentException::new);
		assertEquals(SEQUENCE_NUMBER, chunk.getSequence());
		assertEquals(WorkChunkStatusEnum.IN_PROGRESS, chunk.getStatus());

		sleepUntilTimeChange();

		WorkChunkErrorEvent request = new WorkChunkErrorEvent(chunkId).setErrorMsg("This is an error message");
		mySvc.onWorkChunkError(request);
		runInTransaction(() -> {
			Batch2WorkChunkEntity entity = findChunkByIdOrThrow(chunkId);
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
			Batch2WorkChunkEntity entity = findChunkByIdOrThrow(chunkId);
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
		assertEquals(2, chunks.get(0).getErrorCount());

		verify(myBatchSender).sendWorkChannelMessage(any());
		clearInvocations(myBatchSender);
	}

	@Test
	public void testMarkChunkAsCompleted_Fail() throws InterruptedException {
		boolean isGatedExecution = false;
		myMaintenanceService.enableMaintenancePass(false);
		JobInstance instance = createInstance(true, isGatedExecution);
		String instanceId = mySvc.storeNewInstance(newSrd(), instance);
		String chunkId = storeWorkChunk(DEF_CHUNK_ID, STEP_CHUNK_ID, instanceId, SEQUENCE_NUMBER, null, isGatedExecution);
		assertNotNull(chunkId);
		PointcutLatch latch = new PointcutLatch("senderlatch");
		doAnswer(a -> {
			latch.call(1);
			return Void.class;
		}).when(myBatchSender).sendWorkChannelMessage(any(JobWorkNotification.class));
		latch.setExpectedCount(1);

		runInTransaction(() -> assertEquals(WorkChunkStatusEnum.READY, findChunkByIdOrThrow(chunkId).getStatus()));
		myBatch2JobHelper.runMaintenancePass();
		runInTransaction(() -> assertEquals(WorkChunkStatusEnum.QUEUED, findChunkByIdOrThrow(chunkId).getStatus()));

		WorkChunk chunk = mySvc.onWorkChunkDequeue(chunkId).orElseThrow(IllegalArgumentException::new);
		assertEquals(SEQUENCE_NUMBER, chunk.getSequence());
		assertEquals(WorkChunkStatusEnum.IN_PROGRESS, chunk.getStatus());

		sleepUntilTimeChange();

		mySvc.onWorkChunkFailed(chunkId, "This is an error message");
		runInTransaction(() -> {
			Batch2WorkChunkEntity entity = findChunkByIdOrThrow(chunkId);
			assertEquals(WorkChunkStatusEnum.FAILED, entity.getStatus());
			assertEquals("This is an error message", entity.getErrorMessage());
			assertNotNull(entity.getCreateTime());
			assertNotNull(entity.getStartTime());
			assertNotNull(entity.getEndTime());
			assertTrue(entity.getCreateTime().getTime() < entity.getStartTime().getTime());
			assertTrue(entity.getStartTime().getTime() < entity.getEndTime().getTime());
		});
		latch.awaitExpected();
		verify(myBatchSender)
			.sendWorkChannelMessage(any());
		clearInvocations(myBatchSender);
	}

	@Test
	public void markWorkChunksWithStatusAndWipeData_marksMultipleChunksWithStatus_asExpected() {
		JobInstance instance = createInstance();
		String instanceId = mySvc.storeNewInstance(newSrd(), instance);
		ArrayList<String> chunkIds = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			WorkChunkCreateEvent chunk = new WorkChunkCreateEvent(
				"defId",
				1,
				"stepId",
				instanceId,
				0,
				"{}",
				false
			);
			String id = mySvc.onWorkChunkCreate(chunk);
			chunkIds.add(id);
		}

		runInTransaction(() -> mySvc.markWorkChunksWithStatusAndWipeData(instance.getInstanceId(), chunkIds, WorkChunkStatusEnum.COMPLETED, null));

		Iterator<WorkChunk> reducedChunks = mySvc.fetchAllWorkChunksIterator(instanceId, true);

		while (reducedChunks.hasNext()) {
			WorkChunk reducedChunk = reducedChunks.next();
			assertThat(chunkIds).contains(reducedChunk.getId());
			assertEquals(WorkChunkStatusEnum.COMPLETED, reducedChunk.getStatus());
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
			String instanceId = mySvc.storeNewInstance(newSrd(), instance);

			JobInstance foundInstance = mySvc.fetchInstance(instanceId).orElseThrow(IllegalStateException::new);

			assertEquals(expectedTriggeringUserName, foundInstance.getTriggeringUsername());

		} finally {
			myInterceptorRegistry.unregisterInterceptor(prestorageBatchJobCreateInterceptor);
		}

	}

	@Test
	public void testPostStorageInterceptor_hasJobInstanceId_preStorageHasNot() {
		IAnonymousInterceptor poststorageBatchJobCreateInterceptor = (pointcut, params) -> {
			JobInstance jobInstance = params.get(JobInstance.class);
			assertNotNull(jobInstance.getInstanceId());
		};
		IAnonymousInterceptor prestorageBatchJobCreateInterceptor = (pointcut, params) -> {
			JobInstance jobInstance = params.get(JobInstance.class);
			assertNull(jobInstance.getInstanceId());
		};

		try{
			myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_POSTSTORAGE_BATCH_JOB_CREATE, poststorageBatchJobCreateInterceptor);
			myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRESTORAGE_BATCH_JOB_CREATE, prestorageBatchJobCreateInterceptor);
			JobInstance instance = createInstance();
			mySvc.storeNewInstance(newSrd(), instance);
		} finally {
			myInterceptorRegistry.unregisterInterceptor(poststorageBatchJobCreateInterceptor);
			myInterceptorRegistry.unregisterInterceptor(prestorageBatchJobCreateInterceptor);
		}

	}

	@Test
	public void testFetchInstanceAndWorkChunkStatus() {
		// Setup

		Date date1 = new Date();
		Date date2 = new Date();



		List<String> chunkIds = new ArrayList<>();
		JobInstance instance = createInstance();
		String instanceId = mySvc.storeNewInstance(newSrd(), instance);
		for (int i = 0; i < 5; i++) {
			chunkIds.add(storeWorkChunk(JOB_DEFINITION_ID, FIRST_STEP_ID, instanceId, i, JsonUtil.serialize(new NdJsonFileJson().setNdJsonText("{}")), false));
		}

		runInTransaction(() -> {
				myWorkChunkRepository.updateChunkStatus(chunkIds.get(0), WorkChunkStatusEnum.READY, WorkChunkStatusEnum.COMPLETED);
				myWorkChunkRepository.updateChunkStatus(chunkIds.get(1), WorkChunkStatusEnum.READY, WorkChunkStatusEnum.COMPLETED);
			});

		// Execute
		BatchInstanceStatusDTO istatus = mySvc.fetchBatchInstanceStatus(instanceId);
		assertEquals(instanceId, istatus.id);
		assertEquals(StatusEnum.QUEUED, istatus.status);

		List<BatchWorkChunkStatusDTO> result = mySvc.fetchWorkChunkStatusForInstance(instanceId);
		assertThat(result).hasSize(2);
		BatchWorkChunkStatusDTO result0 = result.get(0);
		assertEquals(WorkChunkStatusEnum.COMPLETED, result0.status);
		assertEquals(2, result0.totalChunks);

		BatchWorkChunkStatusDTO result1 = result.get(1);
		assertEquals(WorkChunkStatusEnum.READY, result1.status);
		assertEquals(3, result1.totalChunks);
	}

	private WorkChunk freshFetchWorkChunk(String chunkId) {
		return runInTransaction(() ->
			myWorkChunkRepository.findById(chunkId)
				.map(JobInstanceUtil::fromEntityToWorkChunk)
				.orElseThrow(IllegalArgumentException::new));
	}

	private JobInstance createInstance() {
		return createInstance(false, false);
	}

	/**
	 * Creates a gated job instance with a unique job definition ID for SMILE-11603 race condition tests.
	 * Uses a separate ID to avoid test pollution with other tests that register the same JOB_DEFINITION_ID.
	 */
	@Nonnull
	private JobInstance createGatedInstanceForRaceTest() {
		JobInstance instance = new JobInstance();
		instance.setJobDefinitionId(GATING_RACE_JOB_DEF_ID);
		instance.setStatus(StatusEnum.QUEUED);
		instance.setJobDefinitionVersion(JOB_DEF_VER);
		instance.setParameters(CHUNK_DATA);
		instance.setReport("TEST");

		JobDefinition<?> jobDef = TestJobDefinitionUtils.buildGatedJobDefinition(
			GATING_RACE_JOB_DEF_ID,
			(step, sink) -> {
				sink.accept(new FirstStepOutput());
				return RunOutcome.SUCCESS;
			},
			(step, sink) -> RunOutcome.SUCCESS,
			theDetails -> {}
		);
		instance.setCurrentGatedStepId(jobDef.getFirstStepId());

		if (myJobDefinitionRegistry.getJobDefinition(jobDef.getJobDefinitionId(), jobDef.getJobDefinitionVersion()).isEmpty()) {
			myJobDefinitionRegistry.addJobDefinition(jobDef);
		}

		return instance;
	}

	/**
	 * Creates a gated job instance with a reduction step for SMILE-11603 race condition tests.
	 */
	@Nonnull
	private JobInstance createReductionInstanceForRaceTest() {
		JobInstance instance = new JobInstance();
		instance.setJobDefinitionId(REDUCTION_RACE_JOB_DEF_ID);
		instance.setStatus(StatusEnum.QUEUED);
		instance.setJobDefinitionVersion(JOB_DEF_VER);
		instance.setParameters(CHUNK_DATA);
		instance.setReport("TEST");

		IReductionStepWorker<TestJobParameters, FirstStepOutput, VoidModel> reductionWorker =
			new IReductionStepWorker<>() {
				@Nonnull
				@Override
				public ChunkOutcome consume(ca.uhn.fhir.batch2.api.ChunkExecutionDetails<TestJobParameters, FirstStepOutput> theChunkDetails) {
					return ChunkOutcome.SUCCESS();
				}

				@Nonnull
				@Override
				public RunOutcome run(@Nonnull ca.uhn.fhir.batch2.api.StepExecutionDetails<TestJobParameters, FirstStepOutput> theStepExecutionDetails,
						@Nonnull ca.uhn.fhir.batch2.api.IJobDataSink<VoidModel> theDataSink) {
					return RunOutcome.SUCCESS;
				}

				@Override
				public IReductionStepWorker<TestJobParameters, FirstStepOutput, VoidModel> newInstance() {
					return this;
				}
			};

		JobDefinition<?> jobDef = TestJobDefinitionUtils.buildGatedJobDefinitionWithReductionStep(
			REDUCTION_RACE_JOB_DEF_ID,
			(step, sink) -> {
				sink.accept(new FirstStepOutput());
				return RunOutcome.SUCCESS;
			},
			reductionWorker,
			theDetails -> {}
		);
		instance.setCurrentGatedStepId(jobDef.getFirstStepId());

		if (myJobDefinitionRegistry.getJobDefinition(jobDef.getJobDefinitionId(), jobDef.getJobDefinitionVersion()).isEmpty()) {
			myJobDefinitionRegistry.addJobDefinition(jobDef);
		}

		return instance;
	}

	@Nonnull
	private JobInstance createInstance(boolean theCreateJobDefBool, boolean theCreateGatedJob) {
		JobInstance instance = new JobInstance();
		instance.setJobDefinitionId(JOB_DEFINITION_ID);
		instance.setStatus(StatusEnum.QUEUED);
		instance.setJobDefinitionVersion(JOB_DEF_VER);
		instance.setParameters(CHUNK_DATA);
		instance.setReport("TEST");

		if (theCreateJobDefBool) {
			JobDefinition<?> jobDef;

			if (theCreateGatedJob) {
				jobDef = TestJobDefinitionUtils.buildGatedJobDefinition(
					JOB_DEFINITION_ID,
					(step, sink) -> {
						sink.accept(new FirstStepOutput());
						return RunOutcome.SUCCESS;
					},
					(step, sink) -> {
						return RunOutcome.SUCCESS;
					},
					theDetails -> {

					}
				);
				instance.setCurrentGatedStepId(jobDef.getFirstStepId());
			} else {
				jobDef = TestJobDefinitionUtils.buildJobDefinition(
					JOB_DEFINITION_ID,
					(step, sink) -> {
						sink.accept(new FirstStepOutput());
						return RunOutcome.SUCCESS;
					},
					(step, sink) -> {
						return RunOutcome.SUCCESS;
					},
					theDetails -> {

					}
				);
			}
			if (myJobDefinitionRegistry.getJobDefinition(jobDef.getJobDefinitionId(), jobDef.getJobDefinitionVersion()).isEmpty()) {
				myJobDefinitionRegistry.addJobDefinition(jobDef);
			}
		}

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

		final String id = mySvc.storeNewInstance(newSrd(), jobInstance);

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

	// ---- Tests for SMILE-11603: Late-arriving GATE_WAITING chunks after step advancement ----

	/**
	 * Simulates the multi-server race condition where a WorkChunk in GATE_WAITING status
	 * is created AFTER a maintenance pass has already advanced the gate and bulk-updated
	 * all existing GATE_WAITING chunks for that step to READY.
	 *
	 * The race scenario:
	 * 1. A gated job's step 1 chunk is processed to completion (dequeue, execute, complete)
	 * 2. A maintenance pass sees step 1 fully COMPLETED, advances the gate to step 2,
	 *    and enqueues the existing step 2 chunks (GATE_WAITING → READY → QUEUED)
	 * 3. A slow worker on another server creates a NEW output chunk for step 2
	 *    with GATE_WAITING status (because isGatedExecution=true)
	 * 4. A subsequent maintenance pass should transition this late chunk to QUEUED —
	 *    but currently it does not, leaving the chunk stuck in GATE_WAITING.
	 */
	@Test
	void gatedJob_lateChunkCreatedAfterMaintenanceAdvancesGate_shouldBeQueuedBySubsequentMaintenance() throws InterruptedException {
		// setup
		boolean isGatedExecution = true;
		JobInstance instance = createInstance(true, isGatedExecution);
		myMaintenanceService.enableMaintenancePass(false);
		String instanceId = mySvc.storeNewInstance(newSrd(), instance);
		PointcutLatch latch = new PointcutLatch("senderlatch");
		doAnswer(a -> {
			latch.call(1);
			return Void.class;
		}).when(myBatchSender).sendWorkChannelMessage(any(JobWorkNotification.class));
		latch.setExpectedCount(1);

		// Create the first step chunk (non-gated first chunk) and
		// an "early" output chunk for step 2 (gated, so GATE_WAITING)
		String step1ChunkId = storeFirstWorkChunk(JOB_DEFINITION_ID, FIRST_STEP_ID, instanceId, 0, CHUNK_DATA);
		String earlyStep2ChunkId = storeWorkChunk(JOB_DEFINITION_ID, LAST_STEP_ID, instanceId, 0, CHUNK_DATA, isGatedExecution);

		// Verify initial states
		runInTransaction(() -> {
			assertThat(findChunkByIdOrThrow(step1ChunkId).getStatus()).isEqualTo(WorkChunkStatusEnum.READY);
			assertThat(findChunkByIdOrThrow(earlyStep2ChunkId).getStatus()).isEqualTo(WorkChunkStatusEnum.GATE_WAITING);
		});

		// First maintenance pass: enqueues step 1 chunk (READY → QUEUED), step 2 stays GATE_WAITING
		myBatch2JobHelper.runMaintenancePass();
		runInTransaction(() -> {
			assertThat(findChunkByIdOrThrow(step1ChunkId).getStatus()).isEqualTo(WorkChunkStatusEnum.QUEUED);
			assertThat(findChunkByIdOrThrow(earlyStep2ChunkId).getStatus()).isEqualTo(WorkChunkStatusEnum.GATE_WAITING);
		});
		latch.awaitExpected();

		// Simulate worker processing step 1: dequeue → in progress → completed
		WorkChunk chunk = mySvc.onWorkChunkDequeue(step1ChunkId).orElseThrow(IllegalArgumentException::new);
		assertThat(chunk.getStatus()).isEqualTo(WorkChunkStatusEnum.IN_PROGRESS);
		mySvc.onWorkChunkCompletion(new WorkChunkCompletionEvent(step1ChunkId, 50, 0));

		runInTransaction(() -> {
			assertThat(findChunkByIdOrThrow(step1ChunkId).getStatus()).isEqualTo(WorkChunkStatusEnum.COMPLETED);
			// step 2 chunk still waiting for the gate to open
			assertThat(findChunkByIdOrThrow(earlyStep2ChunkId).getStatus()).isEqualTo(WorkChunkStatusEnum.GATE_WAITING);
		});

		// Second maintenance pass: sees step 1 all COMPLETED, advances gate to step 2,
		// flips early step 2 chunk GATE_WAITING → READY → QUEUED
		latch.setExpectedCount(1);
		myBatch2JobHelper.runMaintenancePass();
		runInTransaction(() -> {
			assertThat(findChunkByIdOrThrow(earlyStep2ChunkId).getStatus()).isEqualTo(WorkChunkStatusEnum.QUEUED);
			assertThat(findInstanceByIdOrThrow(instanceId).getCurrentGatedStepId()).isEqualTo(LAST_STEP_ID);
		});
		latch.awaitExpected();
		clearInvocations(myBatchSender);

		// ---- Simulate Server B: slow worker creates a "late" chunk AFTER gate advancement ----
		// In production, this happens when a step 1 worker on another server was still running
		// and calls dataSink.accept(), which inserts a new GATE_WAITING chunk for step 2.
		String lateStep2ChunkId = storeWorkChunk(JOB_DEFINITION_ID, LAST_STEP_ID, instanceId, 1, CHUNK_DATA, isGatedExecution);
		runInTransaction(() -> {
			assertThat(findChunkByIdOrThrow(lateStep2ChunkId).getStatus()).isEqualTo(WorkChunkStatusEnum.GATE_WAITING);
		});

		// Third maintenance pass: should detect and transition the late chunk.
		myBatch2JobHelper.runMaintenancePass();

		// The late chunk should now be QUEUED (GATE_WAITING → READY → QUEUED).
		// This assertion currently FAILS — demonstrating the race condition bug.
		runInTransaction(() -> {
			assertThat(findChunkByIdOrThrow(lateStep2ChunkId).getStatus())
				.as("Late-arriving chunk for an already-advanced step should be QUEUED after maintenance")
				.isEqualTo(WorkChunkStatusEnum.QUEUED);
		});
	}

	/**
	 * Simulates the more realistic multi-server race condition: a 3-step gated job
	 * where step 2 has multiple chunks processed in parallel across servers.
	 * A slow worker on one server creates a late output chunk for step 3 AFTER
	 * maintenance has already advanced the gate from step 2 → step 3.
	 *
	 * The race scenario:
	 * 1. Step 1 completes, gate advances to step 2
	 * 2. Step 2 has 3 chunks processed across servers, each producing output for step 3
	 * 3. All 3 step 2 chunks complete, maintenance advances gate to step 3,
	 *    flipping existing step 3 GATE_WAITING chunks → READY → QUEUED
	 * 4. A slow worker on another server creates one more output chunk for step 3
	 *    with GATE_WAITING status — this chunk is stuck.
	 */
	@Test
	void gatedJob_multiChunkStep_lateChunkCreatedAfterAdvancement_shouldBeQueuedBySubsequentMaintenance() throws InterruptedException {
		// setup - register a 3-step gated job definition
		JobDefinition<?> threeStepJobDef = buildThreeStepGatedJobDefinition();
		myMaintenanceService.enableMaintenancePass(false);

		JobInstance instance = new JobInstance();
		instance.setJobDefinitionId(THREE_STEP_JOB_DEF_ID);
		instance.setStatus(StatusEnum.QUEUED);
		instance.setJobDefinitionVersion(JOB_DEF_VER);
		instance.setParameters(CHUNK_DATA);
		instance.setCurrentGatedStepId(FIRST_STEP_ID);
		String instanceId = mySvc.storeNewInstance(newSrd(), instance);

		PointcutLatch latch = new PointcutLatch("senderlatch");
		doAnswer(a -> {
			latch.call(1);
			return Void.class;
		}).when(myBatchSender).sendWorkChannelMessage(any(JobWorkNotification.class));

		// === STEP 1: single chunk, process to completion ===
		String step1ChunkId = storeFirstWorkChunk(THREE_STEP_JOB_DEF_ID, FIRST_STEP_ID, instanceId, 0, CHUNK_DATA);

		// Maintenance enqueues step 1 chunk
		latch.setExpectedCount(1);
		myBatch2JobHelper.runMaintenancePass();
		runInTransaction(() ->
			assertThat(findChunkByIdOrThrow(step1ChunkId).getStatus()).isEqualTo(WorkChunkStatusEnum.QUEUED));
		latch.awaitExpected();

		// Worker processes step 1: dequeue → complete
		mySvc.onWorkChunkDequeue(step1ChunkId);
		mySvc.onWorkChunkCompletion(new WorkChunkCompletionEvent(step1ChunkId, 1, 0));
		runInTransaction(() ->
			assertThat(findChunkByIdOrThrow(step1ChunkId).getStatus()).isEqualTo(WorkChunkStatusEnum.COMPLETED));

		// Step 1 worker produced 3 output chunks for step 2 (all GATE_WAITING)
		String step2Chunk1 = storeWorkChunk(THREE_STEP_JOB_DEF_ID, SECOND_STEP_ID, instanceId, 0, CHUNK_DATA, true);
		String step2Chunk2 = storeWorkChunk(THREE_STEP_JOB_DEF_ID, SECOND_STEP_ID, instanceId, 1, CHUNK_DATA, true);
		String step2Chunk3 = storeWorkChunk(THREE_STEP_JOB_DEF_ID, SECOND_STEP_ID, instanceId, 2, CHUNK_DATA, true);

		// === GATE ADVANCE: step 1 → step 2 ===
		// Maintenance sees step 1 COMPLETED, advances gate, enqueues step 2 chunks
		latch.setExpectedCount(3);
		myBatch2JobHelper.runMaintenancePass();
		runInTransaction(() -> {
			assertThat(findInstanceByIdOrThrow(instanceId).getCurrentGatedStepId()).isEqualTo(SECOND_STEP_ID);
			assertThat(findChunkByIdOrThrow(step2Chunk1).getStatus()).isEqualTo(WorkChunkStatusEnum.QUEUED);
			assertThat(findChunkByIdOrThrow(step2Chunk2).getStatus()).isEqualTo(WorkChunkStatusEnum.QUEUED);
			assertThat(findChunkByIdOrThrow(step2Chunk3).getStatus()).isEqualTo(WorkChunkStatusEnum.QUEUED);
		});
		latch.awaitExpected();

		// === STEP 2: 3 chunks processed across multiple servers ===
		// Each worker dequeues, produces output for step 3, and completes

		// Server A processes chunk 1 → creates output for step 3
		mySvc.onWorkChunkDequeue(step2Chunk1);
		String step3ChunkA = storeWorkChunk(THREE_STEP_JOB_DEF_ID, LAST_STEP_ID, instanceId, 0, CHUNK_DATA, true);
		mySvc.onWorkChunkCompletion(new WorkChunkCompletionEvent(step2Chunk1, 10, 0));

		// Server B processes chunk 2 → creates output for step 3
		mySvc.onWorkChunkDequeue(step2Chunk2);
		String step3ChunkB = storeWorkChunk(THREE_STEP_JOB_DEF_ID, LAST_STEP_ID, instanceId, 1, CHUNK_DATA, true);
		mySvc.onWorkChunkCompletion(new WorkChunkCompletionEvent(step2Chunk2, 10, 0));

		// Server C processes chunk 3 → completes but hasn't created output yet (slow dataSink.accept)
		mySvc.onWorkChunkDequeue(step2Chunk3);
		mySvc.onWorkChunkCompletion(new WorkChunkCompletionEvent(step2Chunk3, 10, 0));

		// Verify all step 2 chunks are COMPLETED, step 3 chunks are GATE_WAITING
		runInTransaction(() -> {
			assertThat(findChunkByIdOrThrow(step2Chunk1).getStatus()).isEqualTo(WorkChunkStatusEnum.COMPLETED);
			assertThat(findChunkByIdOrThrow(step2Chunk2).getStatus()).isEqualTo(WorkChunkStatusEnum.COMPLETED);
			assertThat(findChunkByIdOrThrow(step2Chunk3).getStatus()).isEqualTo(WorkChunkStatusEnum.COMPLETED);
			assertThat(findChunkByIdOrThrow(step3ChunkA).getStatus()).isEqualTo(WorkChunkStatusEnum.GATE_WAITING);
			assertThat(findChunkByIdOrThrow(step3ChunkB).getStatus()).isEqualTo(WorkChunkStatusEnum.GATE_WAITING);
		});

		// === GATE ADVANCE: step 2 → step 3 ===
		// Maintenance sees all step 2 COMPLETED, advances gate, flips existing step 3 chunks
		latch.setExpectedCount(2);
		clearInvocations(myBatchSender);
		myBatch2JobHelper.runMaintenancePass();
		runInTransaction(() -> {
			assertThat(findInstanceByIdOrThrow(instanceId).getCurrentGatedStepId()).isEqualTo(LAST_STEP_ID);
			assertThat(findChunkByIdOrThrow(step3ChunkA).getStatus()).isEqualTo(WorkChunkStatusEnum.QUEUED);
			assertThat(findChunkByIdOrThrow(step3ChunkB).getStatus()).isEqualTo(WorkChunkStatusEnum.QUEUED);
		});
		latch.awaitExpected();
		clearInvocations(myBatchSender);

		// === THE RACE: Server C's slow worker finally creates its step 3 output ===
		// In production, Server C's dataSink.accept() commits in a REQUIRES_NEW transaction
		// that happens to land just after the gate advancement bulk UPDATE.
		String lateStep3Chunk = storeWorkChunk(THREE_STEP_JOB_DEF_ID, LAST_STEP_ID, instanceId, 2, CHUNK_DATA, true);
		runInTransaction(() ->
			assertThat(findChunkByIdOrThrow(lateStep3Chunk).getStatus()).isEqualTo(WorkChunkStatusEnum.GATE_WAITING));

		// Subsequent maintenance pass: should detect and transition the late chunk
		myBatch2JobHelper.runMaintenancePass();

		// The late chunk should now be QUEUED.
		// This assertion currently FAILS — demonstrating the race condition bug.
		runInTransaction(() -> {
			assertThat(findChunkByIdOrThrow(lateStep3Chunk).getStatus())
				.as("Late-arriving step 3 chunk should be QUEUED after maintenance, but is stuck in GATE_WAITING")
				.isEqualTo(WorkChunkStatusEnum.QUEUED);
		});
	}

	@Nonnull
	private JobDefinition<?> buildThreeStepGatedJobDefinition() {
		JobDefinition<TestJobParameters> jobDef = JobDefinition.newBuilder()
			.setJobDefinitionId(THREE_STEP_JOB_DEF_ID)
			.setJobDescription("three step gated test job")
			.setJobDefinitionVersion(JOB_DEF_VER)
			.setParametersType(TestJobParameters.class)
			.addFirstStep(
				FIRST_STEP_ID,
				"Test first step",
				FirstStepOutput.class,
				(step, sink) -> {
					sink.accept(new FirstStepOutput());
					return RunOutcome.SUCCESS;
				}
			)
			.addIntermediateStep(
				SECOND_STEP_ID,
				"Test second step",
				FirstStepOutput.class,
				(step, sink) -> {
					sink.accept(new FirstStepOutput());
					return RunOutcome.SUCCESS;
				}
			)
			.addLastStep(
				LAST_STEP_ID,
				"Test last step",
				(step, sink) -> RunOutcome.SUCCESS
			)
			.completionHandler(theDetails -> {})
			.gatedExecution()
			.build();

		if (myJobDefinitionRegistry.getJobDefinition(jobDef.getJobDefinitionId(), jobDef.getJobDefinitionVersion()).isEmpty()) {
			myJobDefinitionRegistry.addJobDefinition(jobDef);
		}
		return jobDef;
	}

	// ---- End of SMILE-11603 tests ----

	private Batch2JobInstanceEntity findInstanceByIdOrThrow(String instanceId) {
		return myJobInstanceRepository.findById(instanceId).orElseThrow(IllegalStateException::new);
	}

	private Batch2WorkChunkEntity findChunkByIdOrThrow(String secondChunkId) {
		return myWorkChunkRepository.findById(secondChunkId).orElseThrow(IllegalArgumentException::new);
	}
}
