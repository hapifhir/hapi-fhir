package ca.uhn.fhir.jpa.batch2;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.impl.BatchWorkChunk;
import ca.uhn.fhir.batch2.jobs.imprt.NdJsonFileJson;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.jpa.dao.data.IBatch2JobInstanceRepository;
import ca.uhn.fhir.jpa.dao.data.IBatch2WorkChunkRepository;
import ca.uhn.fhir.jpa.entity.Batch2JobInstanceEntity;
import ca.uhn.fhir.jpa.entity.Batch2WorkChunkEntity;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.util.JsonUtil;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
			Batch2JobInstanceEntity instanceEntity = myJobInstanceRepository.findById(instanceId).orElseThrow(() -> new IllegalStateException());
			assertEquals(StatusEnum.QUEUED, instanceEntity.getStatus());
		});

		JobInstance foundInstance = mySvc.fetchInstanceAndMarkInProgress(instanceId).orElseThrow(() -> new IllegalStateException());
		assertEquals(instanceId, foundInstance.getInstanceId());
		assertEquals(JOB_DEFINITION_ID, foundInstance.getJobDefinitionId());
		assertEquals(JOB_DEF_VER, foundInstance.getJobDefinitionVersion());
		assertEquals(StatusEnum.IN_PROGRESS, foundInstance.getStatus());
		assertEquals(CHUNK_DATA, foundInstance.getParameters());

		runInTransaction(() -> {
			Batch2JobInstanceEntity instanceEntity = myJobInstanceRepository.findById(instanceId).orElseThrow(() -> new IllegalStateException());
			assertEquals(StatusEnum.IN_PROGRESS, instanceEntity.getStatus());
		});
	}

	@Test
	public void testCancelInstance() {
		JobInstance instance = createInstance();
		String instanceId = mySvc.storeNewInstance(instance);

		runInTransaction(() -> {
			Batch2JobInstanceEntity instanceEntity = myJobInstanceRepository.findById(instanceId).orElseThrow(() -> new IllegalStateException());
			assertEquals(StatusEnum.QUEUED, instanceEntity.getStatus());
			instanceEntity.setCancelled(true);
			myJobInstanceRepository.save(instanceEntity);
		});

		mySvc.cancelInstance(instanceId);

		JobInstance foundInstance = mySvc.fetchInstanceAndMarkInProgress(instanceId).orElseThrow(() -> new IllegalStateException());
		assertEquals(instanceId, foundInstance.getInstanceId());
		assertEquals(JOB_DEFINITION_ID, foundInstance.getJobDefinitionId());
		assertEquals(JOB_DEF_VER, foundInstance.getJobDefinitionVersion());
		assertEquals(StatusEnum.IN_PROGRESS, foundInstance.getStatus());
		assertTrue(foundInstance.isCancelled());
		assertEquals(CHUNK_DATA, foundInstance.getParameters());

	}

	@Test
	public void testFetchInstanceAndMarkInProgress() {
		JobInstance instance = createInstance();
		String instanceId = mySvc.storeNewInstance(instance);

		JobInstance foundInstance = mySvc.fetchInstanceAndMarkInProgress(instanceId).orElseThrow(() -> new IllegalStateException());
		assertEquals(36, foundInstance.getInstanceId().length());
		assertEquals(JOB_DEFINITION_ID, foundInstance.getJobDefinitionId());
		assertEquals(JOB_DEF_VER, foundInstance.getJobDefinitionVersion());
		assertEquals(StatusEnum.IN_PROGRESS, foundInstance.getStatus());
		assertEquals(CHUNK_DATA, foundInstance.getParameters());
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
		assertEquals(null, chunks.get(0).getData());
		assertEquals(null, chunks.get(1).getData());
		assertEquals(null, chunks.get(2).getData());
		assertThat(chunks.stream().map(t -> t.getId()).collect(Collectors.toList()),
			contains(ids.get(0), ids.get(1), ids.get(2)));

		chunks = mySvc.fetchWorkChunksWithoutData(instanceId, 3, 1);
		assertThat(chunks.stream().map(t -> t.getId()).collect(Collectors.toList()),
			contains(ids.get(3), ids.get(4), ids.get(5)));

		chunks = mySvc.fetchWorkChunksWithoutData(instanceId, 3, 2);
		assertThat(chunks.stream().map(t -> t.getId()).collect(Collectors.toList()),
			contains(ids.get(6), ids.get(7), ids.get(8)));

		chunks = mySvc.fetchWorkChunksWithoutData(instanceId, 3, 3);
		assertThat(chunks.stream().map(t -> t.getId()).collect(Collectors.toList()),
			contains(ids.get(9)));

		chunks = mySvc.fetchWorkChunksWithoutData(instanceId, 3, 4);
		assertThat(chunks.stream().map(t -> t.getId()).collect(Collectors.toList()),
			empty());
	}

	@Test
	public void testFetchUnknownWork() {
		assertFalse(myWorkChunkRepository.findById("FOO").isPresent());
	}

	@Test
	public void testStoreAndFetchWorkChunk_NoData() {
		JobInstance instance = createInstance();
		String instanceId = mySvc.storeNewInstance(instance);

		String id = storeWorkChunk(JOB_DEFINITION_ID, TARGET_STEP_ID, instanceId, 0, null);

		WorkChunk chunk = mySvc.fetchWorkChunkSetStartTimeAndMarkInProgress(id).orElseThrow(() -> new IllegalArgumentException());
		assertEquals(null, chunk.getData());
	}

	@Test
	public void testStoreAndFetchWorkChunk_WithData() {
		JobInstance instance = createInstance();
		String instanceId = mySvc.storeNewInstance(instance);

		String id = storeWorkChunk(JOB_DEFINITION_ID, TARGET_STEP_ID, instanceId, 0, CHUNK_DATA);
		assertNotNull(id);
		runInTransaction(() -> assertEquals(StatusEnum.QUEUED, myWorkChunkRepository.findById(id).orElseThrow(() -> new IllegalArgumentException()).getStatus()));

		WorkChunk chunk = mySvc.fetchWorkChunkSetStartTimeAndMarkInProgress(id).orElseThrow(() -> new IllegalArgumentException());
		assertEquals(36, chunk.getInstanceId().length());
		assertEquals(JOB_DEFINITION_ID, chunk.getJobDefinitionId());
		assertEquals(JOB_DEF_VER, chunk.getJobDefinitionVersion());
		assertEquals(StatusEnum.IN_PROGRESS, chunk.getStatus());
		assertEquals(CHUNK_DATA, chunk.getData());

		runInTransaction(() -> assertEquals(StatusEnum.IN_PROGRESS, myWorkChunkRepository.findById(id).orElseThrow(() -> new IllegalArgumentException()).getStatus()));
	}

	@Test
	public void testMarkChunkAsCompleted_Success() {
		JobInstance instance = createInstance();
		String instanceId = mySvc.storeNewInstance(instance);
		String chunkId = storeWorkChunk(DEF_CHUNK_ID, STEP_CHUNK_ID, instanceId, SEQUENCE_NUMBER, CHUNK_DATA);
		assertNotNull(chunkId);

		runInTransaction(() -> assertEquals(StatusEnum.QUEUED, myWorkChunkRepository.findById(chunkId).orElseThrow(() -> new IllegalArgumentException()).getStatus()));

		sleepUntilTimeChanges();

		WorkChunk chunk = mySvc.fetchWorkChunkSetStartTimeAndMarkInProgress(chunkId).orElseThrow(() -> new IllegalArgumentException());
		assertEquals(SEQUENCE_NUMBER, chunk.getSequence());
		assertEquals(StatusEnum.IN_PROGRESS, chunk.getStatus());
		assertNotNull(chunk.getCreateTime());
		assertNotNull(chunk.getStartTime());
		assertNull(chunk.getEndTime());
		assertNull(chunk.getRecordsProcessed());
		assertNotNull(chunk.getData());
		runInTransaction(() -> assertEquals(StatusEnum.IN_PROGRESS, myWorkChunkRepository.findById(chunkId).orElseThrow(() -> new IllegalArgumentException()).getStatus()));

		sleepUntilTimeChanges();

		mySvc.markWorkChunkAsCompletedAndClearData(chunkId, 50);
		runInTransaction(() -> {
			Batch2WorkChunkEntity entity = myWorkChunkRepository.findById(chunkId).orElseThrow(() -> new IllegalArgumentException());
			assertEquals(StatusEnum.COMPLETED, entity.getStatus());
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

		runInTransaction(() -> assertEquals(StatusEnum.QUEUED, myWorkChunkRepository.findById(chunkId).orElseThrow(() -> new IllegalArgumentException()).getStatus()));

		sleepUntilTimeChanges();

		WorkChunk chunk = mySvc.fetchWorkChunkSetStartTimeAndMarkInProgress(chunkId).orElseThrow(() -> new IllegalArgumentException());
		assertEquals(SEQUENCE_NUMBER, chunk.getSequence());
		assertEquals(StatusEnum.IN_PROGRESS, chunk.getStatus());

		sleepUntilTimeChanges();

		mySvc.markWorkChunkAsErroredAndIncrementErrorCount(chunkId, "This is an error message");
		runInTransaction(() -> {
			Batch2WorkChunkEntity entity = myWorkChunkRepository.findById(chunkId).orElseThrow(() -> new IllegalArgumentException());
			assertEquals(StatusEnum.ERRORED, entity.getStatus());
			assertEquals("This is an error message", entity.getErrorMessage());
			assertNotNull(entity.getCreateTime());
			assertNotNull(entity.getStartTime());
			assertNotNull(entity.getEndTime());
			assertEquals(1, entity.getErrorCount());
			assertTrue(entity.getCreateTime().getTime() < entity.getStartTime().getTime());
			assertTrue(entity.getStartTime().getTime() < entity.getEndTime().getTime());
		});

		// Mark errored again

		mySvc.markWorkChunkAsErroredAndIncrementErrorCount(chunkId, "This is an error message 2");
		runInTransaction(() -> {
			Batch2WorkChunkEntity entity = myWorkChunkRepository.findById(chunkId).orElseThrow(() -> new IllegalArgumentException());
			assertEquals(StatusEnum.ERRORED, entity.getStatus());
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

		runInTransaction(() -> assertEquals(StatusEnum.QUEUED, myWorkChunkRepository.findById(chunkId).orElseThrow(() -> new IllegalArgumentException()).getStatus()));

		sleepUntilTimeChanges();

		WorkChunk chunk = mySvc.fetchWorkChunkSetStartTimeAndMarkInProgress(chunkId).orElseThrow(() -> new IllegalArgumentException());
		assertEquals(SEQUENCE_NUMBER, chunk.getSequence());
		assertEquals(StatusEnum.IN_PROGRESS, chunk.getStatus());

		sleepUntilTimeChanges();

		mySvc.markWorkChunkAsFailed(chunkId, "This is an error message");
		runInTransaction(() -> {
			Batch2WorkChunkEntity entity = myWorkChunkRepository.findById(chunkId).orElseThrow(() -> new IllegalArgumentException());
			assertEquals(StatusEnum.FAILED, entity.getStatus());
			assertEquals("This is an error message", entity.getErrorMessage());
			assertNotNull(entity.getCreateTime());
			assertNotNull(entity.getStartTime());
			assertNotNull(entity.getEndTime());
			assertTrue(entity.getCreateTime().getTime() < entity.getStartTime().getTime());
			assertTrue(entity.getStartTime().getTime() < entity.getEndTime().getTime());
		});


	}

	@Test
	public void testMarkInstanceAsCompleted() {
		String instanceId = mySvc.storeNewInstance(createInstance());

		mySvc.markInstanceAsCompleted(instanceId);

		runInTransaction(() -> {
			Batch2JobInstanceEntity entity = myJobInstanceRepository.findById(instanceId).orElseThrow(() -> new IllegalArgumentException());
			assertEquals(StatusEnum.COMPLETED, entity.getStatus());
		});
	}

	@Test
	public void testUpdateInstance() {
		String instanceId = mySvc.storeNewInstance(createInstance());

		JobInstance instance = mySvc.fetchInstance(instanceId).orElseThrow(() -> new IllegalArgumentException());
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
			Batch2JobInstanceEntity entity = myJobInstanceRepository.findById(instanceId).orElseThrow(() -> new IllegalArgumentException());
			assertEquals(instance.getStartTime().getTime(), entity.getStartTime().getTime());
			assertEquals(instance.getEndTime().getTime(), entity.getEndTime().getTime());
		});

		JobInstance finalInstance = mySvc.fetchInstance(instanceId).orElseThrow(() -> new IllegalArgumentException());
		assertEquals(instanceId, finalInstance.getInstanceId());
		assertEquals(0.5d, finalInstance.getProgress());
		assertTrue(finalInstance.isWorkChunksPurged());
		assertEquals(3, finalInstance.getErrorCount());
		assertEquals(instance.getEstimatedTimeRemaining(), finalInstance.getEstimatedTimeRemaining());
	}

	@Nonnull
	private JobInstance createInstance() {
		JobInstance instance = new JobInstance();
		instance.setJobDefinitionId(JOB_DEFINITION_ID);
		instance.setStatus(StatusEnum.QUEUED);
		instance.setJobDefinitionVersion(JOB_DEF_VER);
		instance.setParameters(CHUNK_DATA);
		return instance;
	}

}
