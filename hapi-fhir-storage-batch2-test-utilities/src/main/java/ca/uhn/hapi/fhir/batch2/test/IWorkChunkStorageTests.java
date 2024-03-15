package ca.uhn.hapi.fhir.batch2.test;

import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkCompletionEvent;
import ca.uhn.fhir.batch2.model.WorkChunkCreateEvent;
import ca.uhn.fhir.batch2.model.WorkChunkErrorEvent;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public interface IWorkChunkStorageTests extends IWorkChunkCommon, WorkChunkTestConstants {

	@Test
	default void testStoreAndFetchWorkChunk_NoData() {
		JobInstance instance = createInstance();
		String instanceId = getSvc().storeNewInstance(instance);

		String id = storeWorkChunk(JOB_DEFINITION_ID, TARGET_STEP_ID, instanceId, 0, null);
		runMaintenancePass();

		WorkChunk chunk = getSvc().onWorkChunkDequeue(id).orElseThrow(IllegalArgumentException::new);
		assertNull(chunk.getData());
	}

	@Test
	default void testStoreAndFetchWorkChunk_WithData() {
		JobInstance instance = createInstance();
		String instanceId = getSvc().storeNewInstance(instance);

		String id = storeWorkChunk(JOB_DEFINITION_ID, TARGET_STEP_ID, instanceId, 0, CHUNK_DATA);
		assertNotNull(id);
		runMaintenancePass();
		runInTransaction(() -> assertEquals(WorkChunkStatusEnum.QUEUED, freshFetchWorkChunk(id).getStatus()));

		WorkChunk chunk = getSvc().onWorkChunkDequeue(id).orElseThrow(IllegalArgumentException::new);
		assertEquals(36, chunk.getInstanceId().length());
		assertEquals(JOB_DEFINITION_ID, chunk.getJobDefinitionId());
		assertEquals(JOB_DEF_VER, chunk.getJobDefinitionVersion());
		assertEquals(WorkChunkStatusEnum.IN_PROGRESS, chunk.getStatus());
		assertEquals(CHUNK_DATA, chunk.getData());

		runInTransaction(() -> assertEquals(WorkChunkStatusEnum.IN_PROGRESS, freshFetchWorkChunk(id).getStatus()));
	}

	@Test
	default void testMarkChunkAsCompleted_Success() {
		JobInstance instance = createInstance();
		String instanceId = getSvc().storeNewInstance(instance);
		String chunkId = storeWorkChunk(DEF_CHUNK_ID, TARGET_STEP_ID, instanceId, SEQUENCE_NUMBER, CHUNK_DATA);
		assertNotNull(chunkId);

		runMaintenancePass();
		runInTransaction(() -> assertEquals(WorkChunkStatusEnum.QUEUED, freshFetchWorkChunk(chunkId).getStatus()));
		sleepUntilTimeChanges();

		WorkChunk chunk = getSvc().onWorkChunkDequeue(chunkId).orElseThrow(IllegalArgumentException::new);
		assertEquals(SEQUENCE_NUMBER, chunk.getSequence());
		assertEquals(WorkChunkStatusEnum.IN_PROGRESS, chunk.getStatus());
		assertNotNull(chunk.getCreateTime());
		assertNotNull(chunk.getStartTime());
		assertNull(chunk.getEndTime());
		assertNull(chunk.getRecordsProcessed());
		assertNotNull(chunk.getData());
		runInTransaction(() -> assertEquals(WorkChunkStatusEnum.IN_PROGRESS, freshFetchWorkChunk(chunkId).getStatus()));

		sleepUntilTimeChanges();

		runInTransaction(() -> getSvc().onWorkChunkCompletion(new WorkChunkCompletionEvent(chunkId, 50, 0)));

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
	default void testMarkChunkAsCompleted_Error() {
		JobInstance instance = createInstance();
		String instanceId = getSvc().storeNewInstance(instance);
		String chunkId = storeWorkChunk(DEF_CHUNK_ID, TARGET_STEP_ID, instanceId, SEQUENCE_NUMBER, null);
		assertNotNull(chunkId);

		runInTransaction(() -> assertEquals(WorkChunkStatusEnum.READY, freshFetchWorkChunk(chunkId).getStatus()));
		runMaintenancePass();
		sleepUntilTimeChanges();

		WorkChunk chunk = getSvc().onWorkChunkDequeue(chunkId).orElseThrow(IllegalArgumentException::new);
		assertEquals(SEQUENCE_NUMBER, chunk.getSequence());
		assertEquals(WorkChunkStatusEnum.IN_PROGRESS, chunk.getStatus());

		sleepUntilTimeChanges();

		WorkChunkErrorEvent request = new WorkChunkErrorEvent(chunkId, ERROR_MESSAGE_A);
		getSvc().onWorkChunkError(request);
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
		getSvc().onWorkChunkError(request2);
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

		List<WorkChunk> chunks = ImmutableList.copyOf(getSvc().fetchAllWorkChunksIterator(instanceId, true));
		assertEquals(1, chunks.size());
		assertEquals(2, chunks.get(0).getErrorCount());
	}

	@Test
	default void testMarkChunkAsCompleted_Fail() {
		JobInstance instance = createInstance();
		String instanceId = getSvc().storeNewInstance(instance);
		String chunkId = storeWorkChunk(DEF_CHUNK_ID, TARGET_STEP_ID, instanceId, SEQUENCE_NUMBER, null);
		assertNotNull(chunkId);

		runInTransaction(() -> assertEquals(WorkChunkStatusEnum.READY, freshFetchWorkChunk(chunkId).getStatus()));
		runMaintenancePass();
		sleepUntilTimeChanges();

		WorkChunk chunk = getSvc().onWorkChunkDequeue(chunkId).orElseThrow(IllegalArgumentException::new);
		assertEquals(SEQUENCE_NUMBER, chunk.getSequence());
		assertEquals(WorkChunkStatusEnum.IN_PROGRESS, chunk.getStatus());

		sleepUntilTimeChanges();

		getSvc().onWorkChunkFailed(chunkId, "This is an error message");
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
	default void markWorkChunksWithStatusAndWipeData_marksMultipleChunksWithStatus_asExpected() {
		JobInstance instance = createInstance();
		String instanceId = getSvc().storeNewInstance(instance);
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
			String id = getSvc().onWorkChunkCreate(chunk);
			chunkIds.add(id);
		}

		runInTransaction(() -> getSvc().markWorkChunksWithStatusAndWipeData(instance.getInstanceId(), chunkIds, WorkChunkStatusEnum.COMPLETED, null));

		Iterator<WorkChunk> reducedChunks = getSvc().fetchAllWorkChunksIterator(instanceId, true);

		while (reducedChunks.hasNext()) {
			WorkChunk reducedChunk = reducedChunks.next();
			assertTrue(chunkIds.contains(reducedChunk.getId()));
			assertEquals(WorkChunkStatusEnum.COMPLETED, reducedChunk.getStatus());
		}
	}
}
