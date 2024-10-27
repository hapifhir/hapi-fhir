/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
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
package ca.uhn.fhir.batch2.coordinator;

import ca.uhn.fhir.batch2.model.WorkChunk;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class ReductionStepChunkProcessingResponse {

	private List<String> mySuccessfulChunkIds;
	private List<String> myFailedChunksIds;
	private boolean myIsSuccessful;

	public ReductionStepChunkProcessingResponse(boolean theDefaultSuccessValue) {
		mySuccessfulChunkIds = new ArrayList<>();
		myFailedChunksIds = new ArrayList<>();
		myIsSuccessful = theDefaultSuccessValue;
	}

	public List<String> getSuccessfulChunkIds() {
		return mySuccessfulChunkIds;
	}

	public boolean hasSuccessfulChunksIds() {
		return !CollectionUtils.isEmpty(mySuccessfulChunkIds);
	}

	public void addSuccessfulChunkId(WorkChunk theWorkChunk) {
		mySuccessfulChunkIds.add(theWorkChunk.getId());
	}

	public List<String> getFailedChunksIds() {
		return myFailedChunksIds;
	}

	public boolean hasFailedChunkIds() {
		return !CollectionUtils.isEmpty(myFailedChunksIds);
	}

	public void addFailedChunkId(WorkChunk theWorChunk) {
		myFailedChunksIds.add(theWorChunk.getId());
	}

	public boolean isSuccessful() {
		return myIsSuccessful;
	}

	public void setSuccessful(boolean theSuccessValue) {
		myIsSuccessful = theSuccessValue;
	}
}
