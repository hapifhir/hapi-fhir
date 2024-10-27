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
package ca.uhn.fhir.batch2.maintenance;

import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import ca.uhn.fhir.util.Logs;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

/**
 * While performing cleanup, the cleanup job loads all work chunks
 * to examine their status. This bean collects the counts that
 * are found, so that they can be reused for maintenance jobs without
 * needing to hit the database a second time.
 */
public class JobChunkProgressAccumulator {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();

	private final Set<String> myConsumedInstanceAndChunkIds = new HashSet<>();
	private final Multimap<String, ChunkStatusCountValue> myInstanceIdToChunkStatuses = ArrayListMultimap.create();

	int getTotalChunkCountForInstanceAndStep(String theInstanceId, String theStepId) {
		return myInstanceIdToChunkStatuses.get(theInstanceId).stream()
				.filter(chunkCount -> chunkCount.myStepId.equals(theStepId))
				.collect(Collectors.toList())
				.size();
	}

	public List<String> getChunkIdsWithStatus(
			String theInstanceId, String theStepId, WorkChunkStatusEnum... theStatuses) {
		return getChunkStatuses(theInstanceId).stream()
				.filter(t -> t.myStepId.equals(theStepId))
				.filter(t -> ArrayUtils.contains(theStatuses, t.myStatus))
				.map(t -> t.myChunkId)
				.collect(Collectors.toList());
	}

	@Nonnull
	private Collection<ChunkStatusCountValue> getChunkStatuses(String theInstanceId) {
		Collection<ChunkStatusCountValue> chunkStatuses = myInstanceIdToChunkStatuses.get(theInstanceId);
		chunkStatuses = defaultIfNull(chunkStatuses, emptyList());
		return chunkStatuses;
	}

	public void addChunk(WorkChunk theChunk) {
		String instanceId = theChunk.getInstanceId();
		String chunkId = theChunk.getId();
		// Note: If chunks are being written while we're executing, we may see the same chunk twice. This
		// check avoids adding it twice.
		if (myConsumedInstanceAndChunkIds.add(instanceId + " " + chunkId)) {
			ourLog.debug(
					"Adding chunk to accumulator. [chunkId={}, instanceId={}, status={}, step={}]",
					chunkId,
					instanceId,
					theChunk.getStatus(),
					theChunk.getTargetStepId());
			myInstanceIdToChunkStatuses.put(
					instanceId, new ChunkStatusCountValue(chunkId, theChunk.getTargetStepId(), theChunk.getStatus()));
		}
	}

	private static class ChunkStatusCountValue {
		public final String myChunkId;
		public final String myStepId;
		public final WorkChunkStatusEnum myStatus;

		private ChunkStatusCountValue(String theChunkId, String theStepId, WorkChunkStatusEnum theStatus) {
			myChunkId = theChunkId;
			myStepId = theStepId;
			myStatus = theStatus;
		}
	}
}
