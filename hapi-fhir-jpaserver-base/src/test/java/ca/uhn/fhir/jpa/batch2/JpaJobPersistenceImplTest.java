package ca.uhn.fhir.jpa.batch2;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceParameter;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.jpa.dao.data.IBatch2JobInstanceRepository;
import ca.uhn.fhir.jpa.dao.data.IBatch2WorkChunkRepository;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.jpa.entity.Batch2JobInstanceEntity;
import ca.uhn.fhir.jpa.entity.Batch2WorkChunkEntity;
import com.github.jsonldjava.shaded.com.google.common.collect.Lists;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.in;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class JpaJobPersistenceImplTest extends BaseJpaR4Test {

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
			mySvc.storeWorkChunk("definition-id", 1, "step-id", instanceId, i, Collections.singletonMap("key", "value"));
		}

		// Execute

		mySvc.deleteInstanceAndChunks(instanceId);

		// Verify

		runInTransaction(()->{
			assertEquals(0, myJobInstanceRepository.findAll().size());
			assertEquals(0, myWorkChunkRepository.findAll().size());
		});
	}

	@Test
	public void testDeleteChunks() {
		// Setup

		JobInstance instance = createInstance();
		String instanceId = mySvc.storeNewInstance(instance);
		for (int i = 0; i < 10; i++) {
			mySvc.storeWorkChunk("definition-id", 1, "step-id", instanceId, i, Collections.singletonMap("key", "value"));
		}

		// Execute

		mySvc.deleteChunks(instanceId);

		// Verify

		runInTransaction(()->{
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
		assertEquals("definition-id", foundInstance.getJobDefinitionId());
		assertEquals(1, foundInstance.getJobDefinitionVersion());
		assertEquals(StatusEnum.IN_PROGRESS, foundInstance.getStatus());
		assertThat(foundInstance.getParameters(), contains(
			new JobInstanceParameter().setName("foo").setValue("bar"),
			new JobInstanceParameter().setName("foo").setValue("baz")
		));

		runInTransaction(() -> {
			Batch2JobInstanceEntity instanceEntity = myJobInstanceRepository.findById(instanceId).orElseThrow(() -> new IllegalStateException());
			assertEquals(StatusEnum.IN_PROGRESS, instanceEntity.getStatus());
		});
	}

	@Test
	public void testFetchInstanceAndMarkInProgress() {
		JobInstance instance = createInstance();
		String instanceId = mySvc.storeNewInstance(instance);

		JobInstance foundInstance = mySvc.fetchInstanceAndMarkInProgress(instanceId).orElseThrow(() -> new IllegalStateException());
		assertEquals(JpaJobPersistenceImpl.ID_LENGTH, foundInstance.getInstanceId().length());
		assertEquals("definition-id", foundInstance.getJobDefinitionId());
		assertEquals(1, foundInstance.getJobDefinitionVersion());
		assertEquals(StatusEnum.IN_PROGRESS, foundInstance.getStatus());
		assertThat(foundInstance.getParameters(), contains(
			new JobInstanceParameter().setName("foo").setValue("bar"),
			new JobInstanceParameter().setName("foo").setValue("baz")
		));
	}

	@Test
	public void testFetchChunks() {
		JobInstance instance = createInstance();
		String instanceId = mySvc.storeNewInstance(instance);

		List<String> ids =new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			String id = mySvc.storeWorkChunk("definition-id", 1, "step-id", instanceId, i, Collections.singletonMap("key", "value"));
			ids.add(id);
		}

		List<WorkChunk> chunks = mySvc.fetchWorkChunksWithoutData(instanceId, 3, 0);
		assertEquals(null, chunks.get(0).getData());
		assertEquals(null, chunks.get(1).getData());
		assertEquals(null, chunks.get(2).getData());
		assertThat(chunks.stream().map(t->t.getId()).collect(Collectors.toList()),
			contains(ids.get(0), ids.get(1), ids.get(2)));

		chunks = mySvc.fetchWorkChunksWithoutData(instanceId, 3, 1);
		assertThat(chunks.stream().map(t->t.getId()).collect(Collectors.toList()),
			contains(ids.get(3), ids.get(4), ids.get(5)));

		chunks = mySvc.fetchWorkChunksWithoutData(instanceId, 3, 2);
		assertThat(chunks.stream().map(t->t.getId()).collect(Collectors.toList()),
			contains(ids.get(6), ids.get(7), ids.get(8)));

		chunks = mySvc.fetchWorkChunksWithoutData(instanceId, 3, 3);
		assertThat(chunks.stream().map(t->t.getId()).collect(Collectors.toList()),
			contains(ids.get(9)));

		chunks = mySvc.fetchWorkChunksWithoutData(instanceId, 3, 4);
		assertThat(chunks.stream().map(t->t.getId()).collect(Collectors.toList()),
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

		String id = mySvc.storeWorkChunk("definition-id", 1, "step-id", instanceId, 0, null);

		WorkChunk chunk = mySvc.fetchWorkChunkAndMarkInProgress(id).orElseThrow(() -> new IllegalArgumentException());
		assertEquals(null, chunk.getData());
	}

	@Test
	public void testStoreAndFetchWorkChunk_WithData() {
		JobInstance instance = createInstance();
		String instanceId = mySvc.storeNewInstance(instance);

		Map<String, Object> data = new HashMap<>();
		data.put("string-key", "string-val");
		data.put("list-key", Lists.newArrayList("list-val-1", "list-val-2"));
		data.put("object-key", Collections.singletonMap("object-val-key", "object-val-val"));

		String id = mySvc.storeWorkChunk("definition-id", 1, "step-id", instanceId, 0, data);
		assertNotNull(id);
		runInTransaction(() -> assertEquals(StatusEnum.QUEUED, myWorkChunkRepository.findById(id).orElseThrow(() -> new IllegalArgumentException()).getStatus()));

		WorkChunk chunk = mySvc.fetchWorkChunkAndMarkInProgress(id).orElseThrow(() -> new IllegalArgumentException());
		assertEquals(JpaJobPersistenceImpl.ID_LENGTH, chunk.getInstanceId().length());
		assertEquals("definition-id", chunk.getJobDefinitionId());
		assertEquals(1, chunk.getJobDefinitionVersion());
		assertEquals(StatusEnum.IN_PROGRESS, chunk.getStatus());

		runInTransaction(() -> assertEquals(StatusEnum.IN_PROGRESS, myWorkChunkRepository.findById(id).orElseThrow(() -> new IllegalArgumentException()).getStatus()));
	}

	@Test
	public void testMarkChunkAsCompleted_Success() {
		JobInstance instance = createInstance();
		String instanceId = mySvc.storeNewInstance(instance);
		String chunkId = mySvc.storeWorkChunk("definition-chunkId", 1, "step-chunkId", instanceId, 1, Collections.singletonMap("key", "value"));
		assertNotNull(chunkId);

		runInTransaction(() -> assertEquals(StatusEnum.QUEUED, myWorkChunkRepository.findById(chunkId).orElseThrow(() -> new IllegalArgumentException()).getStatus()));

		sleepUntilTimeChanges();

		WorkChunk chunk = mySvc.fetchWorkChunkAndMarkInProgress(chunkId).orElseThrow(() -> new IllegalArgumentException());
		assertEquals(1, chunk.getSequence());
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
	public void testMarkChunkAsCompleted_Error() {
		JobInstance instance = createInstance();
		String instanceId = mySvc.storeNewInstance(instance);
		String chunkId = mySvc.storeWorkChunk("definition-chunkId", 1, "step-chunkId", instanceId, 1, null);
		assertNotNull(chunkId);

		runInTransaction(() -> assertEquals(StatusEnum.QUEUED, myWorkChunkRepository.findById(chunkId).orElseThrow(() -> new IllegalArgumentException()).getStatus()));

		sleepUntilTimeChanges();

		WorkChunk chunk = mySvc.fetchWorkChunkAndMarkInProgress(chunkId).orElseThrow(() -> new IllegalArgumentException());
		assertEquals(1, chunk.getSequence());
		assertEquals(StatusEnum.IN_PROGRESS, chunk.getStatus());

		sleepUntilTimeChanges();

		mySvc.markWorkChunkAsErrored(chunkId, "This is an error message");
		runInTransaction(() -> {
			Batch2WorkChunkEntity entity = myWorkChunkRepository.findById(chunkId).orElseThrow(() -> new IllegalArgumentException());
			assertEquals(StatusEnum.ERRORED, entity.getStatus());
			assertEquals("This is an error message", entity.getErrorMessage());
			assertNotNull(entity.getCreateTime());
			assertNotNull(entity.getStartTime());
			assertNotNull(entity.getEndTime());
			assertTrue(entity.getCreateTime().getTime() < entity.getStartTime().getTime());
			assertTrue(entity.getStartTime().getTime() < entity.getEndTime().getTime());
		});


	}

	@Test
	public void testMarkChunkAsCompleted_Fail() {
		JobInstance instance = createInstance();
		String instanceId = mySvc.storeNewInstance(instance);
		String chunkId = mySvc.storeWorkChunk("definition-chunkId", 1, "step-chunkId", instanceId, 1, null);
		assertNotNull(chunkId);

		runInTransaction(() -> assertEquals(StatusEnum.QUEUED, myWorkChunkRepository.findById(chunkId).orElseThrow(() -> new IllegalArgumentException()).getStatus()));

		sleepUntilTimeChanges();

		WorkChunk chunk = mySvc.fetchWorkChunkAndMarkInProgress(chunkId).orElseThrow(() -> new IllegalArgumentException());
		assertEquals(1, chunk.getSequence());
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

		runInTransaction(()->{
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
		instance.setCombinedRecordsProcessedPerSecond(22);
		instance.setWorkChunksPurged(true);
		instance.setProgress(0.5d);

		mySvc.updateInstance(instance);

		runInTransaction(()->{
			Batch2JobInstanceEntity entity = myJobInstanceRepository.findById(instanceId).orElseThrow(() -> new IllegalArgumentException());
			assertEquals(instance.getStartTime().getTime(), entity.getStartTime().getTime());
			assertEquals(instance.getEndTime().getTime(), entity.getEndTime().getTime());
		});

		JobInstance finalInstance = mySvc.fetchInstance(instanceId).orElseThrow(() -> new IllegalArgumentException());
		assertEquals(instanceId, finalInstance.getInstanceId());
		assertEquals(0.5d, finalInstance.getProgress());
		assertTrue(finalInstance.isWorkChunksPurged());
	}

	@Nonnull
	private JobInstance createInstance() {
		JobInstance instance = new JobInstance();
		instance.setJobDefinitionId("definition-id");
		instance.setStatus(StatusEnum.QUEUED);
		instance.setJobDefinitionVersion(1);
		instance.addParameter(new JobInstanceParameter().setName("foo").setValue("bar"));
		instance.addParameter(new JobInstanceParameter().setName("foo").setValue("baz"));
		return instance;
	}

}
