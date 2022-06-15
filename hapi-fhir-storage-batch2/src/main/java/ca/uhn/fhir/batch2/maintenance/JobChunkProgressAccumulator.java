package ca.uhn.fhir.batch2.maintenance;

/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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


import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

/**
 * While performing cleanup, the cleanup job loads all of the known
 * work chunks to examine their status. This bean collects the counts that
 * are found, so that they can be reused for maintenance jobs without
 * needing to hit the database a second time.
 */
public class JobChunkProgressAccumulator {

	private final Set<String> myConsumedInstanceAndChunkIds = new HashSet<>();
	private final Multimap<String, ChunkStatusCountKey> myInstanceIdToChunkStatuses = ArrayListMultimap.create();

	int countChunksWithStatus(String theInstanceId, String theStepId, Set<StatusEnum> theStatuses) {
		return getChunkIdsWithStatus(theInstanceId, theStepId, theStatuses).size();
	}

	public List<String> getChunkIdsWithStatus(String theInstanceId, String theStepId, Set<StatusEnum> theStatuses) {
		return getChunkStatuses(theInstanceId).stream().filter(t -> t.myStepId.equals(theStepId)).filter(t -> theStatuses.contains(t.myStatus)).map(t -> t.myChunkId).collect(Collectors.toList());
	}

	@Nonnull
	private Collection<ChunkStatusCountKey> getChunkStatuses(String theInstanceId) {
		Collection<ChunkStatusCountKey> chunkStatuses = myInstanceIdToChunkStatuses.get(theInstanceId);
		chunkStatuses = defaultIfNull(chunkStatuses, emptyList());
		return chunkStatuses;
	}

	public void addChunk(WorkChunk theChunk) {
		String instanceId = theChunk.getInstanceId();
		String chunkId = theChunk.getId();
		// Note: If chunks are being written while we're executing, we may see the same chunk twice. This
		// check avoids adding it twice.
		if (myConsumedInstanceAndChunkIds.add(instanceId + " " + chunkId)) {
			myInstanceIdToChunkStatuses.put(instanceId, new ChunkStatusCountKey(chunkId, theChunk.getTargetStepId(), theChunk.getStatus()));
		}
	}

	private static class ChunkStatusCountKey {
		public final String myChunkId;
		public final String myStepId;
		public final StatusEnum myStatus;

		private ChunkStatusCountKey(String theChunkId, String theStepId, StatusEnum theStatus) {
			myChunkId = theChunkId;
			myStepId = theStepId;
			myStatus = theStatus;
		}
	}


}
