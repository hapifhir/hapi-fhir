/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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

import ca.uhn.fhir.batch2.jobs.parameters.PartitionedUrl;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.rest.api.server.RequestDetails;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides the list of {@link PartitionedUrl} that a job should run against.
 */
public interface IJobPartitionProvider {

	/**
	 * Provides the list of partitions to run job steps against, based on the request that initiates the job.
	 * @param theRequestDetails the requestDetails
	 * @param theOperation the operation being run which corresponds to the job
	 * @return the list of partitions
	 */
	List<RequestPartitionId> getPartitions(RequestDetails theRequestDetails, String theOperation);

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
}
