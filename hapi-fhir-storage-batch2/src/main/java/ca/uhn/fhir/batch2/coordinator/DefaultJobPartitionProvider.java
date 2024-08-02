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

import ca.uhn.fhir.batch2.api.IJobPartitionProvider;
import ca.uhn.fhir.batch2.jobs.parameters.PartitionedUrl;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.ResourceSearch;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Default implementation which provides the {@link PartitionedUrl} list for a certain operation request.
 */
public class DefaultJobPartitionProvider implements IJobPartitionProvider {
	protected final IRequestPartitionHelperSvc myRequestPartitionHelper;
	protected FhirContext myFhirContext;
	private MatchUrlService myMatchUrlService;

	public DefaultJobPartitionProvider(IRequestPartitionHelperSvc theRequestPartitionHelperSvc) {
		myRequestPartitionHelper = theRequestPartitionHelperSvc;
	}

	public DefaultJobPartitionProvider(
			FhirContext theFhirContext,
			IRequestPartitionHelperSvc theRequestPartitionHelperSvc,
			MatchUrlService theMatchUrlService) {
		myFhirContext = theFhirContext;
		myRequestPartitionHelper = theRequestPartitionHelperSvc;
		myMatchUrlService = theMatchUrlService;
	}

	/**
	 * Provides the list of {@link PartitionedUrl} to run job steps against, based on the request that initiates the job.
	 * @param theRequestDetails the requestDetails
	 * @param theOperation the operation being run which corresponds to the job
	 * @return the list of {@link PartitionedUrl}
	 */
	public List<RequestPartitionId> getPartitions(RequestDetails theRequestDetails, String theOperation) {
		RequestPartitionId partitionId = myRequestPartitionHelper.determineReadPartitionForRequestForServerOperation(
				theRequestDetails, theOperation);
		return List.of(partitionId);
	}

	/**
	 * Provides the list of {@link PartitionedUrl} to run job steps against, based on the request that initiates the job
	 * and the urls that it's configured with.
	 * @param theRequestDetails the requestDetails
	 * @param theUrls the urls to run the job against
	 * @return the list of {@link PartitionedUrl}
	 */
	@Override
	public List<PartitionedUrl> getPartitionedUrls(RequestDetails theRequestDetails, @NotNull List<String> theUrls) {
		List<String> urls = theUrls;

		// if the url list is empty, use all the supported resource types to build the url list
		if (theUrls.isEmpty()) {
			urls = myFhirContext.getResourceTypes().stream()
					.map(resourceType -> resourceType + "?")
					.collect(Collectors.toList());
		}

		// determine the partition associated with each of the urls
		List<PartitionedUrl> partitionedUrls = new ArrayList<>();
		for (String s : urls) {
			ResourceSearch resourceSearch = myMatchUrlService.getResourceSearch(s);
			RequestPartitionId partitionId = myRequestPartitionHelper.determineReadPartitionForRequestForSearchType(
					theRequestDetails, resourceSearch.getResourceName(), resourceSearch.getSearchParameterMap());
			partitionedUrls.add(new PartitionedUrl().setUrl(s).setRequestPartitionId(partitionId));
		}

		// handle (bulk) system operations that are typically configured with RequestPartitionId.allPartitions()
		// populate the actual list of all partitions, if that is supported
		List<RequestPartitionId> allPartitions = getAllPartitions();
		if (!allPartitions.isEmpty()) {
			allPartitions.add(RequestPartitionId.defaultPartition());
		}
		List<PartitionedUrl> retVal = new ArrayList<>();
		for (PartitionedUrl partitionedUrl : partitionedUrls) {
			String url = partitionedUrl.getUrl();
			RequestPartitionId partitionId = partitionedUrl.getRequestPartitionId();
			if (partitionId != null && partitionId.isAllPartitions() && !allPartitions.isEmpty()) {
				List<PartitionedUrl> megaScalePartitionedUrls = allPartitions.stream()
						.map(p -> (new PartitionedUrl().setUrl(url).setRequestPartitionId(p)))
						.collect(Collectors.toList());
				retVal.addAll(megaScalePartitionedUrls);
			} else {
				retVal.add(partitionedUrl);
			}
		}

		return retVal;
	}

	public List<RequestPartitionId> getAllPartitions() {
		// likely allPartitions is not supported in this path
		return List.of();
	}
}
