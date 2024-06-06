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

import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkCompletionEvent;
import ca.uhn.fhir.batch2.model.WorkChunkErrorEvent;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import ca.uhn.hapi.fhir.batch2.test.support.JobMaintenanceStateInformation;
import ca.uhn.test.concurrency.PointcutLatch;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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

@Nested
public interface IWorkChunkStorageTests extends IWorkChunkCommon, WorkChunkTestConstants {

	@BeforeEach
	default void before() {
		getTestManager().enableMaintenanceRunner(false);
	}

	@Test
	default void testStoreAndFetchWorkChunk_NoData() {
		JobInstance instance = createInstance();
		String instanceId = getTestManager().getSvc().storeNewInstance(instance);

		String id = getTestManager().storeWorkChunk(JOB_DEFINITION_ID, FIRST_STEP_ID, instanceId, 0, null, false);

		getTestManager().runInTransaction(() -> {
			WorkChunk chunk = getTestManager().freshFetchWorkChunk(id);
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
		String instanceId = getTestManager().getSvc().storeNewInstance(instance);

		String id = getTestManager().storeWorkChunk(JOB_DEFINITION_ID, FIRST_STEP_ID, instanceId, 0, CHUNK_DATA, theGatedExecution);
		assertNotNull(id);

		getTestManager().runInTransaction(() -> assertEquals(expectedStatus, getTestManager().freshFetchWorkChunk(id).getStatus()));
	}

	@Test
	default void testNonGatedWorkChunkInReady_IsQueuedDuringMaintenance() throws InterruptedException {
		// setup
		int expectedCalls = 1;
		PointcutLatch sendingLatch = getTestManager().disableWorkChunkMessageHandler();
		sendingLatch.setExpectedCount(expectedCalls);
		String state = "1|READY,1|QUEUED";
		JobDefinition<?> jobDefinition = getTestManager().withJobDefinition(false);
		String instanceId = getTestManager().createAndStoreJobInstance(jobDefinition);
		JobMaintenanceStateInformation stateInformation = new JobMaintenanceStateInformation(instanceId, jobDefinition, state);

		getTestManager().createChunksInStates(stateInformation);
		String id = stateInformation.getInitialWorkChunks().stream().findFirst().orElseThrow().getId();

		// verify created in ready
		getTestManager().runInTransaction(() -> assertEquals(WorkChunkStatusEnum.READY, getTestManager().freshFetchWorkChunk(id).getStatus()));

		// test
		getTestManager().runMaintenancePass();

		// verify it's in QUEUED now
		stateInformation.verifyFinalStates(getTestManager().getSvc());
		getTestManager().verifyWorkChunkMessageHandlerCalled(sendingLatch, expectedCalls);
	}

	@Test
	default void testStoreAndFetchWorkChunk_WithData() {
		// setup
		getTestManager().disableWorkChunkMessageHandler();
		JobDefinition<?> jobDefinition = getTestManager().withJobDefinition(false);
		JobInstance instance = createInstance();
		String instanceId = getTestManager().getSvc().storeNewInstance(instance);

		// we're not transitioning this state; we're just checking storage of data
		JobMaintenanceStateInformation info = new JobMaintenanceStateInformation(instanceId, jobDefinition, "1|QUEUED");
		info.addWorkChunkModifier((chunk) -> {
			chunk.setData(CHUNK_DATA);
		});

		getTestManager().createChunksInStates(info);
		String id = info.getInitialWorkChunks().stream().findFirst().orElseThrow().getId();

		// verify created in QUEUED
		getTestManager().runInTransaction(() -> assertEquals(WorkChunkStatusEnum.QUEUED, getTestManager().freshFetchWorkChunk(id).getStatus()));

		// test; manually dequeue chunk
		WorkChunk chunk = getTestManager().getSvc().onWorkChunkDequeue(id).orElseThrow(IllegalArgumentException::new);

		// verify
		assertEquals(36, chunk.getInstanceId().length());
		assertEquals(JOB_DEFINITION_ID, chunk.getJobDefinitionId());
		assertEquals(JOB_DEF_VER, chunk.getJobDefinitionVersion());
		assertEquals(WorkChunkStatusEnum.IN_PROGRESS, chunk.getStatus());
		assertEquals(CHUNK_DATA, chunk.getData());

		getTestManager().runInTransaction(() -> assertEquals(WorkChunkStatusEnum.IN_PROGRESS, getTestManager().freshFetchWorkChunk(id).getStatus()));
	}

	@Test
	default void testMarkChunkAsCompleted_Success() {
		// setup
		String state = "2|IN_PROGRESS,2|COMPLETED";
		getTestManager().disableWorkChunkMessageHandler();

		JobDefinition<?> jobDefinition = getTestManager().withJobDefinition(false);
		String instanceId = getTestManager().createAndStoreJobInstance(jobDefinition);
		JobMaintenanceStateInformation info = new JobMaintenanceStateInformation(instanceId, jobDefinition, state);
		info.addWorkChunkModifier(chunk -> {
			chunk.setCreateTime(new Date());
			chunk.setData(CHUNK_DATA);
		});
		getTestManager().createChunksInStates(info);

		String chunkId = info.getInitialWorkChunks().stream().findFirst().orElseThrow().getId();

		// run test
		getTestManager().runInTransaction(() -> getTestManager().getSvc().onWorkChunkCompletion(new WorkChunkCompletionEvent(chunkId, 50, 0)));

		// verify
		info.verifyFinalStates(getTestManager().getSvc());
		WorkChunk entity = getTestManager().freshFetchWorkChunk(chunkId);
		assertEquals(WorkChunkStatusEnum.COMPLETED, entity.getStatus());
		assertEquals(50, entity.getRecordsProcessed());
		assertNotNull(entity.getCreateTime());
		assertNull(entity.getData());
	}

	@Test
	default void testMarkChunkAsCompleted_Error() {
		// setup
		String state = "1|IN_PROGRESS,1|ERRORED";
		getTestManager().disableWorkChunkMessageHandler();
		JobDefinition<?> jobDef = getTestManager().withJobDefinition(false);
		String instanceId = getTestManager().createAndStoreJobInstance(jobDef);
		JobMaintenanceStateInformation info = new JobMaintenanceStateInformation(
			instanceId, jobDef, state
		);
		getTestManager().createChunksInStates(info);
		String chunkId = info.getInitialWorkChunks().stream().findFirst().orElseThrow().getId();

		// test
		WorkChunkErrorEvent request = new WorkChunkErrorEvent(chunkId, ERROR_MESSAGE_A);
		getTestManager().getSvc().onWorkChunkError(request);
		getTestManager().runInTransaction(() -> {
			WorkChunk entity = getTestManager().freshFetchWorkChunk(chunkId);
			assertEquals(WorkChunkStatusEnum.ERRORED, entity.getStatus());
			assertEquals(ERROR_MESSAGE_A, entity.getErrorMessage());
			assertEquals(1, entity.getErrorCount());
		});

		// Mark errored again

		WorkChunkErrorEvent request2 = new WorkChunkErrorEvent(chunkId, "This is an error message 2");
		getTestManager().getSvc().onWorkChunkError(request2);
		getTestManager().runInTransaction(() -> {
			WorkChunk entity = getTestManager().freshFetchWorkChunk(chunkId);
			assertEquals(WorkChunkStatusEnum.ERRORED, entity.getStatus());
			assertEquals("This is an error message 2", entity.getErrorMessage());
			assertEquals(2, entity.getErrorCount());
		});

		List<WorkChunk> chunks = ImmutableList.copyOf(getTestManager().getSvc().fetchAllWorkChunksIterator(instanceId, true));
		assertEquals(1, chunks.size());
		assertEquals(2, chunks.get(0).getErrorCount());

		info.verifyFinalStates(getTestManager().getSvc());
	}

	@Test
	default void testMarkChunkAsCompleted_Fail() {
		// setup
		String state = "1|IN_PROGRESS,1|FAILED";
		getTestManager().disableWorkChunkMessageHandler();
		JobDefinition<?> jobDef = getTestManager().withJobDefinition(false);
		String instanceId = getTestManager().createAndStoreJobInstance(jobDef);
		JobMaintenanceStateInformation info = new JobMaintenanceStateInformation(
			instanceId, jobDef, state
		);
		getTestManager().createChunksInStates(info);
		String chunkId = info.getInitialWorkChunks().stream().findFirst().orElseThrow().getId();

		// test
		getTestManager().getSvc().onWorkChunkFailed(chunkId, "This is an error message");

		// verify
		getTestManager().runInTransaction(() -> {
			WorkChunk entity = getTestManager().freshFetchWorkChunk(chunkId);
			assertEquals(WorkChunkStatusEnum.FAILED, entity.getStatus());
			assertEquals("This is an error message", entity.getErrorMessage());
		});

		info.verifyFinalStates(getTestManager().getSvc());
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
		getTestManager().disableWorkChunkMessageHandler();
		JobDefinition<?> jobDef = getTestManager().withJobDefinition(false);
		String instanceId = getTestManager().createAndStoreJobInstance(jobDef);
		JobMaintenanceStateInformation info = new JobMaintenanceStateInformation(
			instanceId, jobDef, state
		);
		getTestManager().createChunksInStates(info);
		List<String> chunkIds = info.getInitialWorkChunks().stream().map(WorkChunk::getId)
			.collect(Collectors.toList());

		getTestManager().runInTransaction(() -> getTestManager().getSvc().markWorkChunksWithStatusAndWipeData(instanceId, chunkIds, WorkChunkStatusEnum.COMPLETED, null));

		Iterator<WorkChunk> reducedChunks = getTestManager().getSvc().fetchAllWorkChunksIterator(instanceId, true);

		while (reducedChunks.hasNext()) {
			WorkChunk reducedChunk = reducedChunks.next();
			assertTrue(chunkIds.contains(reducedChunk.getId()));
			assertEquals(WorkChunkStatusEnum.COMPLETED, reducedChunk.getStatus());
		}
	}
}
