/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.batch2.api;

import ca.uhn.fhir.batch2.jobs.chunk.ChunkRangeJson;
import ca.uhn.fhir.batch2.jobs.parameters.PartitionedUrl;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.rest.api.server.RequestDetails;

import java.util.Date;
import java.util.List;

import static org.apache.commons.lang3.ObjectUtils.getIfNull;

/**
 * Resolves FHIR search URLs into partition-aware {@link PartitionedUrl} entries for batch2 bulk
 * operations ({@code $reindex}, {@code $delete-expunge}, {@code $mdm-submit}, etc.).
 *
 * <p>Implementations determine which partition each URL targets and may expand a single
 * {@link RequestPartitionId#isAllPartitions() allPartitions} entry into one {@link PartitionedUrl}
 * per concrete partition, so that downstream job steps can process each partition independently.</p>
 *
 * @see ca.uhn.fhir.batch2.coordinator.DefaultJobPartitionProvider
 */
public interface IJobPartitionProvider {

	/**
	 * Resolves each FHIR search URL to its target partition based on the caller's request context.
	 * If a URL resolves to {@link RequestPartitionId#isAllPartitions() allPartitions}, the implementation
	 * may expand it into one {@link PartitionedUrl} per concrete partition, so the returned list can be
	 * larger than the input.
	 *
	 * @param theRequestDetails the request that initiated the bulk operation
	 * @param theUrls FHIR search URLs in {@code ResourceType?params} form (must not be empty)
	 * @return partition-aware entries — one per (url, partition) pair
	 */
	default List<PartitionedUrl> getPartitionedUrls(RequestDetails theRequestDetails, List<String> theUrls) {
		return theUrls.stream().map(url -> new PartitionedUrl().setUrl(url)).toList();
	}

	/**
	 * Returns the concrete partition list representing "all partitions" in this system.
	 * Used during all-partitions expansion in {@link #getPartitionedUrls}.
	 */
	List<RequestPartitionId> getAllPartitions();

	/**
	 * Splits a single {@link RequestPartitionId} (which may represent one partition, several, or
	 * all partitions) into individual entries that should each become a separate work chunk.
	 *
	 * @param theInputPartition the partition to split; {@code null} is treated as all partitions
	 * @return one entry per work chunk
	 * @since 8.8.0
	 */
	default List<RequestPartitionId> splitPartitionsForJobExecution(RequestPartitionId theInputPartition) {
		return List.of(getIfNull(theInputPartition, RequestPartitionId.allPartitions()));
	}

	/**
	 * Converts a {@link PartitionedUrl} into {@link ChunkRangeJson} entries spanning the given date range.
	 * The default produces a single chunk; implementations may split further (e.g. per-shard).
	 *
	 * @param thePartitionedUrl the URL and its resolved partition
	 * @param theStart chunk range start (inclusive)
	 * @param theEnd chunk range end (inclusive)
	 * @return one or more chunk ranges to process
	 */
	default List<ChunkRangeJson> toChunkRanges(PartitionedUrl thePartitionedUrl, Date theStart, Date theEnd) {
		RequestPartitionId requestPartitionId = thePartitionedUrl.getRequestPartitionId();
		requestPartitionId = getIfNull(requestPartitionId, RequestPartitionId.allPartitions());

		ChunkRangeJson chunkRangeJson = new ChunkRangeJson(theStart, theEnd)
				.setUrl(thePartitionedUrl.getUrl())
				.setPartitionId(requestPartitionId);
		return List.of(chunkRangeJson);
	}
}
