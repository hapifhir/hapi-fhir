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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Nonnull;
import java.util.List;

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

	public List<String> searchForIds(String theQueryUrl) {
		// fake out the server url parsing
		IBundleProvider result = searchForBundleProvider(theQueryUrl);

		List<String> resourceIds = result.getAllResourceIds();
		return resourceIds;
	}

	public IBundleProvider searchForBundleProvider(String theQueryUrl) {
		ResourceSearch search = myMatchUrlService.getResourceSearch(theQueryUrl);
		SearchParameterMap map = search.getSearchParameterMap();
		map.setLoadSynchronous(true);
		SystemRequestDetails request = fakeRequestDetailsFromUrl(theQueryUrl);
		SortSpec sort = (SortSpec) new SortParameter(myFhirCtx).translateQueryParametersIntoServerArgument(request, null);
		if (sort != null) {
			map.setSort(sort);
		}

		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(search.getResourceName());
		IBundleProvider result = dao.search(map, request);
		return result;
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
