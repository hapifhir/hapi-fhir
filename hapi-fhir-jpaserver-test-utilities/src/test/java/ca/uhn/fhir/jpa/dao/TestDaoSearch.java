package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.ResourceSearch;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.server.method.SortParameter;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.in;
import static org.hamcrest.Matchers.not;

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
			@Autowired MatchUrlService theMatchUrlService
		) {
			return new TestDaoSearch(theFhirContext, theDaoRegistry, theMatchUrlService);
		}
	}

	final MatchUrlService myMatchUrlService;
	final DaoRegistry myDaoRegistry;
	final FhirContext myFhirCtx;

	public TestDaoSearch(FhirContext theFhirCtx, DaoRegistry theDaoRegistry, MatchUrlService theMatchUrlService) {
		myMatchUrlService = theMatchUrlService;
		myDaoRegistry = theDaoRegistry;
		myFhirCtx = theFhirCtx;
	}

	/**
	 * Assert that the FHIR search has theIds in the search results.
	 * @param theReason junit reason message
	 * @param theQueryUrl FHIR query - e.g. /Patient?name=kelly
	 * @param theIds the resource ids to expect.
	 */
	public void assertSearchFinds(String theReason, String theQueryUrl,  String ...theIds) {
		assertSearchResultIds(theQueryUrl, theReason, hasItems(theIds));
	}

	/**
	 * Assert that the FHIR search has theIds in the search results.
	 * @param theReason junit reason message
	 * @param theQueryUrl FHIR query - e.g. /Patient?name=kelly
	 * @param theIds the id-part of the resource ids to expect.
	 */
	public void assertSearchFinds(String theReason, String theQueryUrl, IIdType...theIds) {
		String[] bareIds = idTypeToIdParts(theIds);

		assertSearchResultIds(theQueryUrl, theReason, hasItems(bareIds));
	}

	public void assertSearchResultIds(String theQueryUrl, String theReason, Matcher<Iterable<String>> matcher) {
		List<String> ids = searchForIds(theQueryUrl);

		MatcherAssert.assertThat(theReason, ids, matcher);
	}

	/**
	 * Assert that the FHIR search does not have theIds in the search results.
	 * @param theReason junit reason message
	 * @param theQueryUrl FHIR query - e.g. /Patient?name=kelly
	 * @param theIds the id-part of the resource ids to not-expect.
	 */
	public void assertSearchNotFound(String theReason, String theQueryUrl, IIdType ...theIds) {
		List<String> ids = searchForIds(theQueryUrl);

		MatcherAssert.assertThat(theReason, ids, everyItem(not(in(idTypeToIdParts(theIds)))));
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

	public List<String>  searchForIds(String theQueryUrl) {
		// fake out the server url parsing
		IBundleProvider result = searchForBundleProvider(theQueryUrl);

		// getAllResources is not safe as size is not always set
		List<String> resourceIds = result.getResources(0, Integer.MAX_VALUE)
					.stream().map(resource -> resource.getIdElement().getIdPart()).collect(Collectors.toList());
		return resourceIds;
	}

	public IBundleProvider searchForBundleProvider(String theQueryUrl) {
		ResourceSearch search = myMatchUrlService.getResourceSearch(theQueryUrl);
		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(search.getResourceName());

		SearchParameterMap map = search.getSearchParameterMap();
		map.setLoadSynchronous(true);
		SortSpec sort = (SortSpec) new SortParameter(myFhirCtx).translateQueryParametersIntoServerArgument(fakeRequestDetailsFromUrl(theQueryUrl), null);
		if (sort != null) {
			map.setSort(sort);
		}

		IBundleProvider result = dao.search(map, fakeRequestDetailsFromUrl(theQueryUrl));
		return result;
	}

	public SearchParameterMap toSearchParameters(String theQueryUrl) {
		ResourceSearch search = myMatchUrlService.getResourceSearch(theQueryUrl);

		SearchParameterMap map = search.getSearchParameterMap();
		map.setLoadSynchronous(true);
		SortSpec sort = (SortSpec) new SortParameter(myFhirCtx).translateQueryParametersIntoServerArgument(fakeRequestDetailsFromUrl(theQueryUrl), null);
		if (sort != null) {
			map.setSort(sort);
		}
		return map;
	}

	@Nonnull
	private SystemRequestDetails fakeRequestDetailsFromUrl(String theQueryUrl) {
		SystemRequestDetails request = new SystemRequestDetails();
		UriComponents uriComponents = UriComponentsBuilder.fromUriString(theQueryUrl).build();
		uriComponents.getQueryParams()
			.forEach((key, value) -> request.addParameter(key, value.toArray(new String[0])));
		return request;
	}
}
