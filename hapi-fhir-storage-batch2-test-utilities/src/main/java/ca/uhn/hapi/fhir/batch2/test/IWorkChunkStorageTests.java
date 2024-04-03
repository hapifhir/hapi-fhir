package ca.uhn.hapi.fhir.batch2.test;

import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkCompletionEvent;
import ca.uhn.fhir.batch2.model.WorkChunkErrorEvent;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import ca.uhn.hapi.fhir.batch2.test.support.JobMaintenanceStateInformation;
import ca.uhn.test.concurrency.PointcutLatch;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public interface IWorkChunkStorageTests extends IWorkChunkCommon, WorkChunkTestConstants {

	@Test
	default void testStoreAndFetchWorkChunk_NoData() {
		JobInstance instance = createInstance();
		String instanceId = getSvc().storeNewInstance(instance);

		String id = storeWorkChunk(JOB_DEFINITION_ID, TARGET_STEP_ID, instanceId, 0, null, false);

		runInTransaction(() -> {
			WorkChunk chunk = freshFetchWorkChunk(id);
			assertNull(chunk.getData());
		});
	}

	@ParameterizedTest
	@CsvSource({
		"false, READY",
		"true, GATE_WAITING"
	})
	default void testWorkChunkCreate_inExpectedStatus(boolean theGatedExecution, WorkChunkStatusEnum expectedStatus) {
		JobInstance instance = createInstance();
		String instanceId = getSvc().storeNewInstance(instance);

		enableMaintenanceRunner(false);

		String id = storeWorkChunk(JOB_DEFINITION_ID, TARGET_STEP_ID, instanceId, 0, CHUNK_DATA, theGatedExecution);
		assertNotNull(id);

		runInTransaction(() -> assertEquals(expectedStatus, freshFetchWorkChunk(id).getStatus()));
	}

	@Test
	default void testNonGatedWorkChunkInReady_IsQueuedDuringMaintenance() throws InterruptedException {
		// setup
		int expectedCalls = 1;
		enableMaintenanceRunner(false);
		PointcutLatch sendingLatch = disableWorkChunkMessageHandler();
		sendingLatch.setExpectedCount(expectedCalls);
		String state = "1|READY,1|QUEUED";
		JobDefinition<?> jobDefinition = withJobDefinition(false);
		String instanceId = createAndStoreJobInstance(jobDefinition);
		JobMaintenanceStateInformation stateInformation = new JobMaintenanceStateInformation(instanceId, jobDefinition, state);

		createChunksInStates(stateInformation);
		String id = stateInformation.getInitialWorkChunks().stream().findFirst().orElseThrow().getId();

		// verify created in ready
		runInTransaction(() -> assertEquals(WorkChunkStatusEnum.READY, freshFetchWorkChunk(id).getStatus()));

		// test
		runMaintenancePass();

		// verify it's in QUEUED now
		stateInformation.verifyFinalStates(getSvc());
		verifyWorkChunkMessageHandlerCalled(sendingLatch, expectedCalls);
	}

	@Test
	default void testStoreAndFetchWorkChunk_WithData() {
		// setup
		disableWorkChunkMessageHandler();
		enableMaintenanceRunner(false);
		JobDefinition<?> jobDefinition = withJobDefinition(false);
		JobInstance instance = createInstance();
		String instanceId = getSvc().storeNewInstance(instance);

		// we're not transitioning this state; we're just checking storage of data
		JobMaintenanceStateInformation info = new JobMaintenanceStateInformation(instanceId, jobDefinition, "1|QUEUED");
		info.addWorkChunkModifier((chunk) -> {
			chunk.setData(CHUNK_DATA);
		});

		createChunksInStates(info);
		String id = info.getInitialWorkChunks().stream().findFirst().orElseThrow().getId();

		// verify created in QUEUED
		runInTransaction(() -> assertEquals(WorkChunkStatusEnum.QUEUED, freshFetchWorkChunk(id).getStatus()));

		// test; manually dequeue chunk
		WorkChunk chunk = getSvc().onWorkChunkDequeue(id).orElseThrow(IllegalArgumentException::new);

		// verify
		assertEquals(36, chunk.getInstanceId().length());
		assertEquals(JOB_DEFINITION_ID, chunk.getJobDefinitionId());
		assertEquals(JOB_DEF_VER, chunk.getJobDefinitionVersion());
		assertEquals(WorkChunkStatusEnum.IN_PROGRESS, chunk.getStatus());
		assertEquals(CHUNK_DATA, chunk.getData());

		runInTransaction(() -> assertEquals(WorkChunkStatusEnum.IN_PROGRESS, freshFetchWorkChunk(id).getStatus()));
	}

	@Test
	default void testMarkChunkAsCompleted_Success() {
		// setup
		String state = "2|IN_PROGRESS,2|COMPLETED";
		disableWorkChunkMessageHandler();
		enableMaintenanceRunner(false);

		JobDefinition<?> jobDefinition = withJobDefinition(false);
		String instanceId = createAndStoreJobInstance(jobDefinition);
		JobMaintenanceStateInformation info = new JobMaintenanceStateInformation(instanceId, jobDefinition, state);
		info.addWorkChunkModifier(chunk -> {
			chunk.setCreateTime(new Date());
			chunk.setData(CHUNK_DATA);
		});
		createChunksInStates(info);

		String chunkId = info.getInitialWorkChunks().stream().findFirst().orElseThrow().getId();

		// run test
		runInTransaction(() -> getSvc().onWorkChunkCompletion(new WorkChunkCompletionEvent(chunkId, 50, 0)));

		// verify
		info.verifyFinalStates(getSvc());
		WorkChunk entity = freshFetchWorkChunk(chunkId);
		assertEquals(WorkChunkStatusEnum.COMPLETED, entity.getStatus());
		assertEquals(50, entity.getRecordsProcessed());
		assertNotNull(entity.getCreateTime());
		assertNull(entity.getData());
	}

	@Test
	default void testMarkChunkAsCompleted_Error() {
		// setup
		String state = "1|IN_PROGRESS,1|ERRORED";
		disableWorkChunkMessageHandler();
		enableMaintenanceRunner(false);
		JobDefinition<?> jobDef = withJobDefinition(false);
		String instanceId = createAndStoreJobInstance(jobDef);
		JobMaintenanceStateInformation info = new JobMaintenanceStateInformation(
			instanceId, jobDef, state
		);
		createChunksInStates(info);
		String chunkId = info.getInitialWorkChunks().stream().findFirst().orElseThrow().getId();

		// test
		WorkChunkErrorEvent request = new WorkChunkErrorEvent(chunkId, ERROR_MESSAGE_A);
		getSvc().onWorkChunkError(request);
		runInTransaction(() -> {
			WorkChunk entity = freshFetchWorkChunk(chunkId);
			assertEquals(WorkChunkStatusEnum.ERRORED, entity.getStatus());
			assertEquals(ERROR_MESSAGE_A, entity.getErrorMessage());
			assertEquals(1, entity.getErrorCount());
		});

		// Mark errored again

		WorkChunkErrorEvent request2 = new WorkChunkErrorEvent(chunkId, "This is an error message 2");
		getSvc().onWorkChunkError(request2);
		runInTransaction(() -> {
			WorkChunk entity = freshFetchWorkChunk(chunkId);
			assertEquals(WorkChunkStatusEnum.ERRORED, entity.getStatus());
			assertEquals("This is an error message 2", entity.getErrorMessage());
			assertEquals(2, entity.getErrorCount());
		});

		List<WorkChunk> chunks = ImmutableList.copyOf(getSvc().fetchAllWorkChunksIterator(instanceId, true));
		assertEquals(1, chunks.size());
		assertEquals(2, chunks.get(0).getErrorCount());

		info.verifyFinalStates(getSvc());
	}

	@Test
	default void testMarkChunkAsCompleted_Fail() {
		// setup
		String state = "1|IN_PROGRESS,1|FAILED";
		disableWorkChunkMessageHandler();
		enableMaintenanceRunner(false);
		JobDefinition<?> jobDef = withJobDefinition(false);
		String instanceId = createAndStoreJobInstance(jobDef);
		JobMaintenanceStateInformation info = new JobMaintenanceStateInformation(
			instanceId, jobDef, state
		);
		createChunksInStates(info);
		String chunkId = info.getInitialWorkChunks().stream().findFirst().orElseThrow().getId();

		// test
		getSvc().onWorkChunkFailed(chunkId, "This is an error message");

		// verify
		runInTransaction(() -> {
			WorkChunk entity = freshFetchWorkChunk(chunkId);
			assertEquals(WorkChunkStatusEnum.FAILED, entity.getStatus());
			assertEquals("This is an error message", entity.getErrorMessage());
		});

		info.verifyFinalStates(getSvc());
	}

	@Test
	default void markWorkChunksWithStatusAndWipeData_marksMultipleChunksWithStatus_asExpected() {
		// setup
		String state = """
   			1|IN_PROGRESS,1|COMPLETED
   			1|ERRORED,1|COMPLETED
   			1|QUEUED,1|COMPLETED
   			1|IN_PROGRESS,1|COMPLETED
		""";
		disableWorkChunkMessageHandler();
		enableMaintenanceRunner(false);
		JobDefinition<?> jobDef = withJobDefinition(false);
		String instanceId = createAndStoreJobInstance(jobDef);
		JobMaintenanceStateInformation info = new JobMaintenanceStateInformation(
			instanceId, jobDef, state
		);
		createChunksInStates(info);
		List<String> chunkIds = info.getInitialWorkChunks().stream().map(WorkChunk::getId)
			.collect(Collectors.toList());

		runInTransaction(() -> getSvc().markWorkChunksWithStatusAndWipeData(instanceId, chunkIds, WorkChunkStatusEnum.COMPLETED, null));

		Iterator<WorkChunk> reducedChunks = getSvc().fetchAllWorkChunksIterator(instanceId, true);

		while (reducedChunks.hasNext()) {
			WorkChunk reducedChunk = reducedChunks.next();
			assertTrue(chunkIds.contains(reducedChunk.getId()));
			assertEquals(WorkChunkStatusEnum.COMPLETED, reducedChunk.getStatus());
		}
	}
}
