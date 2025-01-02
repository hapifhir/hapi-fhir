/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 specification tests
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
package ca.uhn.hapi.fhir.batch2.test;

import ca.uhn.fhir.batch2.api.IJobPartitionProvider;
import ca.uhn.fhir.batch2.jobs.parameters.PartitionedUrl;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.ResourceSearch;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public interface IJobPartitionProviderTest {
	FhirContext getFhirContext();
	IRequestPartitionHelperSvc getRequestPartitionHelper();
	IJobPartitionProvider getJobPartitionProvider();
	MatchUrlService getMatchUrlService();

	@Test
	default void getPartitionedUrls_noUrls_returnsCorrectly() {
		// setup
		SystemRequestDetails requestDetails = new SystemRequestDetails();

		setupResourceNameUrlWithPartition(requestDetails, "Patient", RequestPartitionId.fromPartitionId(1));
		setupResourceNameUrlWithPartition(requestDetails, "Observation", RequestPartitionId.fromPartitionId(2));
		setupResourceNameUrlWithPartition(requestDetails, "Practitioner", null);
		setupResourceNameUrlWithPartition(requestDetails, "SearchParameter", RequestPartitionId.defaultPartition());

		Set<String> resourceTypes = Set.of("Patient", "Observation", "Practitioner", "SearchParameter");
		when(getFhirContext().getResourceTypes()).thenReturn(resourceTypes);

		// execute and verify
		List<PartitionedUrl> partitionedUrls = List.of(
				new PartitionedUrl().setUrl("Patient?").setRequestPartitionId(RequestPartitionId.fromPartitionId(1)),
				new PartitionedUrl().setUrl("Observation?").setRequestPartitionId(RequestPartitionId.fromPartitionId(2)),
				new PartitionedUrl().setUrl("Practitioner?"),
				new PartitionedUrl().setUrl("SearchParameter?").setRequestPartitionId(RequestPartitionId.defaultPartition()));

		executeAndVerifyGetPartitionedUrls(requestDetails, List.of(), partitionedUrls);
		executeAndVerifyGetPartitionedUrls(requestDetails, null, partitionedUrls);
	}

	@Test
	default void getPartitionedUrls_withUrls_returnsCorrectly() {
		// setup
		SystemRequestDetails requestDetails = new SystemRequestDetails();

		setupResourceNameUrlWithPartition(requestDetails, "Patient", RequestPartitionId.fromPartitionId(1));
		setupResourceNameUrlWithPartition(requestDetails, "Observation", RequestPartitionId.allPartitions());
		setupResourceNameUrlWithPartition(requestDetails, "Practitioner", null);

		// execute and verify
		List<String> urls = List.of("Patient?", "Observation?", "Practitioner?");
		List<PartitionedUrl> partitionedUrls = List.of(
				new PartitionedUrl().setUrl("Patient?").setRequestPartitionId(RequestPartitionId.fromPartitionId(1)),
				new PartitionedUrl().setUrl("Observation?").setRequestPartitionId(RequestPartitionId.allPartitions()),
				new PartitionedUrl().setUrl("Practitioner?"));
		executeAndVerifyGetPartitionedUrls(requestDetails, urls, partitionedUrls);
	}

	default void executeAndVerifyGetPartitionedUrls(RequestDetails theRequestDetails, List<String> theUrls, Collection<PartitionedUrl> thePartitionedUrls) {
		// test
		List<PartitionedUrl> actualPartitionedUrls = getJobPartitionProvider().getPartitionedUrls(theRequestDetails, theUrls);

		// verify
		assertThat(actualPartitionedUrls).hasSize(thePartitionedUrls.size()).containsExactlyInAnyOrder(thePartitionedUrls.toArray(new PartitionedUrl[0]));
	}

	default void setupResourceNameUrlWithPartition(RequestDetails theRequestDetails, String theResourceName, RequestPartitionId thePartitionId) {
		final String url = theResourceName + "?";
		ResourceSearch resourceSearch = mock(ResourceSearch.class);
		when(getMatchUrlService().getResourceSearch(url)).thenReturn(resourceSearch);
		when(resourceSearch.getResourceName()).thenReturn(theResourceName);
		SearchParameterMap searchParameterMap = mock(SearchParameterMap.class);
		when(resourceSearch.getSearchParameterMap()).thenReturn(searchParameterMap);

		when(getRequestPartitionHelper().determineReadPartitionForRequestForSearchType(theRequestDetails, theResourceName, searchParameterMap)).thenReturn(thePartitionId);
	}

	void setupPartitions(List<RequestPartitionId> thePartitionIds);
}
