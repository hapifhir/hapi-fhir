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
		Set<String> resourceTypes = Set.of("Patient", "Observation");
		when(getFhirContext().getResourceTypes()).thenReturn(resourceTypes);
		List<PartitionedUrl> partitionedUrls = List.of(
				new PartitionedUrl().setUrl("Patient?").setRequestPartitionId(RequestPartitionId.fromPartitionId(1)),
				new PartitionedUrl().setUrl("Observation?").setRequestPartitionId(RequestPartitionId.fromPartitionId(2)),
				new PartitionedUrl().setUrl("Practitioner?"));

		verifyGetPartitionedUrls(requestDetails, List.of(), partitionedUrls);
	}

	@Test
	default void getPartitionedUrls_specificPartitionsForEachUrl_returnsCorrectly() {
		// setup
		SystemRequestDetails requestDetails = new SystemRequestDetails();

		setupResourceNameUrlWithPartition(requestDetails, "Patient", RequestPartitionId.fromPartitionId(1));
		setupResourceNameUrlWithPartition(requestDetails, "Observation", RequestPartitionId.fromPartitionId(2));
		setupResourceNameUrlWithPartition(requestDetails, "Practitioner", null);
		List<String> urls = List.of("Patient?", "Observation?", "Practitioner?");
		List<PartitionedUrl> partitionedUrls = List.of(
				new PartitionedUrl().setUrl("Patient?").setRequestPartitionId(RequestPartitionId.fromPartitionId(1)),
				new PartitionedUrl().setUrl("Observation?").setRequestPartitionId(RequestPartitionId.fromPartitionId(2)),
				new PartitionedUrl().setUrl("Practitioner?"));

		verifyGetPartitionedUrls(requestDetails, urls, partitionedUrls);
	}

	default void verifyGetPartitionedUrls(RequestDetails theRequestDetails, List<String> theUrls, List<PartitionedUrl> thePartitionedUrls) {
		// test
		List<PartitionedUrl> actualPartitionedUrls = getJobPartitionProvider().getPartitionedUrls(theRequestDetails, theUrls);

		// verify
		assertThat(actualPartitionedUrls).hasSize(thePartitionedUrls.size());
		for (int i = 0; i < actualPartitionedUrls.size(); i++) {
			PartitionedUrl actualPartitionedUrl = actualPartitionedUrls.get(i);
			PartitionedUrl expectedPartitionedUrl = thePartitionedUrls.get(i);
			assertThat(actualPartitionedUrl.getRequestPartitionId()).isEqualTo(expectedPartitionedUrl.getRequestPartitionId());
			assertThat(actualPartitionedUrl.getUrl()).isEqualTo(expectedPartitionedUrl.getUrl());
		}
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
}