/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
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
package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.ResourceSearch;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.IRestfulServerDefaults;
import ca.uhn.fhir.rest.server.method.SortParameter;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ArrayUtils.EMPTY_STRING_ARRAY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

/**
 * Simplistic implementation of FHIR queries.
 */
public class TestDaoSearch {

	@Configuration
	public static class Config {
		@Bean
		TestDaoSearch testDaoSearch(
				@Autowired FhirContext theFhirContext,
				@Autowired DaoRegistry theDaoRegistry,
				@Autowired MatchUrlService theMatchUrlService,
				@Autowired ISearchParamRegistry theSearchParamRegistry) {

			return new TestDaoSearch(theFhirContext, theDaoRegistry, theMatchUrlService, theSearchParamRegistry);
		}
	}

	@Autowired(required = false)
	private IFulltextSearchSvc myFulltextSearchSvc;

	final FhirContext myFhirCtx;
	final DaoRegistry myDaoRegistry;
	final MatchUrlService myMatchUrlService;
	final ISearchParamRegistry mySearchParamRegistry;

	public TestDaoSearch(
			FhirContext theFhirCtx,
			DaoRegistry theDaoRegistry,
			MatchUrlService theMatchUrlService,
			ISearchParamRegistry theSearchParamRegistry) {
		myMatchUrlService = theMatchUrlService;
		myDaoRegistry = theDaoRegistry;
		myFhirCtx = theFhirCtx;
		mySearchParamRegistry = theSearchParamRegistry;
	}

	public ISearchParamRegistry getSearchParamRegistry() {
		return mySearchParamRegistry;
	}

	/**
	 * Assert that the FHIR search has theIds in the search results.
	 * @param theReason junit reason message
	 * @param theQueryUrl FHIR query - e.g. /Patient?name=kelly
	 * @param theIds the resource ids to expect.
	 */
	public void assertSearchFinds(String theReason, String theQueryUrl, String... theIds) {
		assertSearchResultIds(theQueryUrl, theReason, theIds);
	}

	public void assertSearchFinds(String theReason, String theQueryUrl, List<String> theIds) {
		assertSearchFinds(theReason, theQueryUrl, theIds.toArray(EMPTY_STRING_ARRAY));
	}

	/**
	 * Assert that the FHIR search has theIds in the search results.
	 * @param theReason junit reason message
	 * @param theQueryUrl FHIR query - e.g. /Patient?name=kelly
	 * @param theIds the id-part of the resource ids to expect.
	 */
	public void assertSearchFinds(String theReason, String theQueryUrl, IIdType... theIds) {
		String[] bareIds = idTypeToIdParts(theIds);

		assertSearchResultIds(theQueryUrl, theReason, bareIds);
	}

	public void assertSearchFindsInOrder(String theReason, String theQueryUrl, String... theIds) {
		List<String> ids = searchForIds(theQueryUrl);

		assertThat(ids).as(theReason).containsExactly(theIds);
	}

	public void assertSearchFindsInOrder(String theReason, String theQueryUrl, List<String> theIds) {
		assertSearchFindsInOrder(theReason, theQueryUrl, theIds.toArray(EMPTY_STRING_ARRAY));
	}

	public void assertSearchFindsOnly(String theReason, String theQueryUrl, String... theIds) {
		assertSearchIdsMatch(theReason, theQueryUrl, theIds);
	}

	public void assertSearchIdsMatch(String theReason, String theQueryUrl, String... theIds) {
		List<String> ids = searchForIds(theQueryUrl);
		assertThat(ids).as(theReason).containsExactlyInAnyOrder(theIds);
	}

	public void assertSearchResultIds(String theQueryUrl, String theReason, String... theExpectedIds) {
		List<String> ids = searchForIds(theQueryUrl);
		assertThat(ids).as(theReason).contains(theExpectedIds);
	}

	/**
	 * Assert that the FHIR search does not have theIds in the search results.
	 * @param theReason junit reason message
	 * @param theQueryUrl FHIR query - e.g. /Patient?name=kelly
	 * @param theIds the id-part of the resource ids to not-expect.
	 */
	public void assertSearchNotFound(String theReason, String theQueryUrl, IIdType... theIds) {
		List<String> ids = searchForIds(theQueryUrl);
		assertThat(ids).as(theReason).doesNotContain(idTypeToIdParts(theIds));
	}

	@Nonnull
	private String[] idTypeToIdParts(IIdType[] theIds) {
		String[] bareIds = new String[theIds.length];
		for (int i = 0; i < theIds.length; i++) {
			bareIds[i] = theIds[i].getIdPart();
		}
		return bareIds;
	}

	public List<IBaseResource> searchForResources(String theQueryUrl) {
		IBundleProvider result = searchForBundleProvider(theQueryUrl);
		return result.getAllResources();
	}

	public List<String> searchForIds(String theQueryUrl) {
		// fake out the server url parsing
		IBundleProvider result = searchForBundleProvider(theQueryUrl);

		// getAllResources is not safe as size is not always set
		return result.getResources(0, Integer.MAX_VALUE).stream()
				.map(resource -> resource.getIdElement().getIdPart())
				.collect(Collectors.toList());
	}

	public IBundleProvider searchForBundleProvider(String theQueryUrl, boolean theSynchronousMode) {
		ResourceSearch search = myMatchUrlService.getResourceSearch(theQueryUrl);
		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(search.getResourceName());

		SearchParameterMap map = search.getSearchParameterMap();
		map.setLoadSynchronous(theSynchronousMode);
		SortSpec sort = (SortSpec) new SortParameter(myFhirCtx)
				.translateQueryParametersIntoServerArgument(fakeRequestDetailsFromUrl(theQueryUrl), null);
		if (sort != null) {
			map.setSort(sort);
		}

		// for asynchronous mode, we also need to make the request paginated ar synchronous is forced
		SystemRequestDetails reqDetails = theSynchronousMode
				? fakeRequestDetailsFromUrl(theQueryUrl)
				: fakePaginatedRequestDetailsFromUrl(theQueryUrl);
		return dao.search(map, reqDetails);
	}

	public IBundleProvider searchForBundleProvider(String theQueryUrl) {
		return searchForBundleProvider(theQueryUrl, true);
	}

	public SearchParameterMap toSearchParameters(String theQueryUrl) {
		ResourceSearch search = myMatchUrlService.getResourceSearch(theQueryUrl);

		SearchParameterMap map = search.getSearchParameterMap();
		map.setLoadSynchronous(true);
		SortSpec sort = (SortSpec) new SortParameter(myFhirCtx)
				.translateQueryParametersIntoServerArgument(fakeRequestDetailsFromUrl(theQueryUrl), null);
		if (sort != null) {
			map.setSort(sort);
		}
		return map;
	}

	@Nonnull
	private SystemRequestDetails fakeRequestDetailsFromUrl(String theQueryUrl) {
		SystemRequestDetails request = new SystemRequestDetails();
		UriComponents uriComponents =
				UriComponentsBuilder.fromUriString(theQueryUrl).build();
		uriComponents.getQueryParams().forEach((key, value) -> request.addParameter(key, value.toArray(new String[0])));
		return request;
	}

	@Nonnull
	private SystemRequestDetails fakePaginatedRequestDetailsFromUrl(String theQueryUrl) {
		SystemRequestDetails spiedReqDetails = spy(SystemRequestDetails.class);
		UriComponents uriComponents =
				UriComponentsBuilder.fromUriString(theQueryUrl).build();
		uriComponents
				.getQueryParams()
				.forEach((key, value) -> spiedReqDetails.addParameter(key, value.toArray(new String[0])));

		IPagingProvider mockPagingProvider = mock(IPagingProvider.class);
		IRestfulServerDefaults mockServerDfts = mock(IRestfulServerDefaults.class);
		doReturn(mockServerDfts).when(spiedReqDetails).getServer();
		doReturn(mockPagingProvider).when(mockServerDfts).getPagingProvider();
		return spiedReqDetails;
	}
}
