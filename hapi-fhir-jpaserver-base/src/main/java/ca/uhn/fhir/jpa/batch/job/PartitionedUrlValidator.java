package ca.uhn.fhir.jpa.batch.job;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.batch.job.model.PartitionedUrl;
import ca.uhn.fhir.jpa.batch.job.model.RequestListJson;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.ResourceSearch;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PartitionedUrlValidator {
	@Autowired
	MatchUrlService myMatchUrlService;
	@Autowired
	IRequestPartitionHelperSvc myRequestPartitionHelperSvc;
	@Autowired
	FhirContext myFhirContext;

	public PartitionedUrlValidator() {
	}

	/**
	 * This method will throw an exception if the user is not allowed to access the requested resource type on the partition determined by the request
	 */

	public RequestListJson buildRequestListJson(RequestDetails theRequest, List<String> theUrlsToProcess) {
		List<PartitionedUrl> partitionedUrls = new ArrayList<>();
		for (String url : theUrlsToProcess) {
			ResourceSearch resourceSearch = myMatchUrlService.getResourceSearch(url);
			RequestPartitionId requestPartitionId = myRequestPartitionHelperSvc.determineReadPartitionForRequestForSearchType(theRequest, resourceSearch.getResourceName(), resourceSearch.getSearchParameterMap(), null);
			partitionedUrls.add(new PartitionedUrl(url, requestPartitionId));
		}
		RequestListJson retval = new RequestListJson();
		retval.setPartitionedUrls(partitionedUrls);
		return retval;
	}

	public RequestPartitionId requestPartitionIdFromRequest(RequestDetails theRequest) {
		Set<String> allResourceNames = myFhirContext.getResourceTypes();
		SearchParameterMap map = SearchParameterMap.newSynchronous();
		// Verify that the user has access to every resource type on the server:
		for (String resourceName : allResourceNames) {
			myRequestPartitionHelperSvc.determineReadPartitionForRequestForSearchType(theRequest, resourceName, map, null);
		}
		// Then return the partition for the Patient resource type.  Note Patient was an arbitrary choice here.
		return myRequestPartitionHelperSvc.determineReadPartitionForRequestForSearchType(theRequest, "Patient", map, null);
	}
}
