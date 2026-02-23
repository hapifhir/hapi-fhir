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
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ObjectUtils.getIfNull;

/**
 * Provides the list of {@link PartitionedUrl} that a job should run against.
 */
public interface IJobPartitionProvider {

	/**
	 * Provides the list of {@link PartitionedUrl} to run job steps against, based on the request that initiates the job
	 * and the urls that it's configured with.
	 * @param theRequestDetails the requestDetails
	 * @param theUrls the urls to run the job against
	 * @return the list of {@link PartitionedUrl}
	 */
	default List<PartitionedUrl> getPartitionedUrls(RequestDetails theRequestDetails, List<String> theUrls) {
		return theUrls.stream().map(url -> new PartitionedUrl().setUrl(url)).collect(Collectors.toList());
	}

	/**
	 * Returns a collection of partitions representing "all partitions" for each shard
	 */
	List<RequestPartitionId> getAllPartitions();

	/**
	 * Given a partition (which might be an individual partition or list of partitions, or even
	 * ALL partitions), separates it into a list of partitions that should be executed in separate
	 * work chunks.
	 *
	 * @since 8.8.0
	 */
	default List<RequestPartitionId> splitPartitionsForJobExecution(RequestPartitionId theInputPartition) {
		return List.of(getIfNull(theInputPartition, RequestPartitionId.allPartitions()));
	}

	/**
	 * Converts a {@link PartitionedUrl} into a list of {@link ChunkRangeJson}s for the given
	 * start and end date. By default, a single URL is turned into a single chunk range.
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
