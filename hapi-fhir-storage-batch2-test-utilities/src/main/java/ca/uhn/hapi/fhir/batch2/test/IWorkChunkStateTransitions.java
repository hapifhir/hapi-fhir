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

import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

public interface IWorkChunkStateTransitions extends IWorkChunkCommon, WorkChunkTestConstants {

	Logger ourLog = LoggerFactory.getLogger(IWorkChunkStateTransitions.class);

	@Test
	default void chunkReceived_queuedToInProgress() throws InterruptedException {
		String jobInstanceId = getTestManager().createAndStoreJobInstance(null);
		String myChunkId = getTestManager().createChunk(jobInstanceId);

		getTestManager().runMaintenancePass();
		// the worker has received the chunk, and marks it started.
		WorkChunk chunk = getTestManager().getSvc().onWorkChunkDequeue(myChunkId).orElseThrow(IllegalArgumentException::new);

		assertEquals(WorkChunkStatusEnum.IN_PROGRESS, chunk.getStatus());
		assertEquals(CHUNK_DATA, chunk.getData());

		// verify the db was updated too
		WorkChunk fetchedWorkChunk = getTestManager().freshFetchWorkChunk(myChunkId);
		assertEquals(WorkChunkStatusEnum.IN_PROGRESS, fetchedWorkChunk.getStatus());
	}
}
