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
package ca.uhn.fhir.batch2.jobs.parameters;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.ResourceSearch;
import ca.uhn.fhir.rest.api.server.RequestDetails;

public class UrlPartitioner {
	private final MatchUrlService myMatchUrlService;
	private final IRequestPartitionHelperSvc myRequestPartitionHelperSvc;

	public UrlPartitioner(MatchUrlService theMatchUrlService, IRequestPartitionHelperSvc theRequestPartitionHelperSvc) {
		myMatchUrlService = theMatchUrlService;
		myRequestPartitionHelperSvc = theRequestPartitionHelperSvc;
	}

	public PartitionedUrl partitionUrl(String theUrl, RequestDetails theRequestDetails) {
		ResourceSearch resourceSearch = myMatchUrlService.getResourceSearch(theUrl);
		RequestPartitionId requestPartitionId =
				myRequestPartitionHelperSvc.determineReadPartitionForRequestForSearchType(
						theRequestDetails, resourceSearch.getResourceName(), resourceSearch.getSearchParameterMap());
		PartitionedUrl retval = new PartitionedUrl();
		retval.setUrl(theUrl);
		retval.setRequestPartitionId(requestPartitionId);
		return retval;
	}
}
