package ca.uhn.fhir.jpa.searchparam.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.searchparam.matcher.SearchParamMatcher;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.provider.HashMapResourceProvider;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SearchableHashMapResourceProvider<T extends IBaseResource> extends HashMapResourceProvider<T> {
	private final SearchParamMatcher mySearchParamMatcher;

	/**
	 * Constructor
	 *
	 * @param theFhirContext  The FHIR context
	 * @param theResourceType The resource type to support
	 */
	public SearchableHashMapResourceProvider(FhirContext theFhirContext, Class<T> theResourceType, SearchParamMatcher theSearchParamMatcher) {
		super(theFhirContext, theResourceType);
		mySearchParamMatcher = theSearchParamMatcher;
	}

	public List<T> searchByCriteria(String theCriteria) {
		return searchBy(resource -> mySearchParamMatcher.match(theCriteria, resource));

	}

	public List<T> searchByParams(SearchParameterMap theSearchParams) {
		return searchBy(resource -> mySearchParamMatcher.match(theSearchParams.toNormalizedQueryString(getFhirContext()), resource));
	}

	private List<T> searchBy(Function<IBaseResource, InMemoryMatchResult> theMatcher) {
		List<T> allEResources = searchAll();
		List<T> matches = new ArrayList<>();
		for (T resource : allEResources) {
			InMemoryMatchResult result = theMatcher.apply(resource);
			if (!result.supported()) {
				throw new InvalidRequestException("Search not supported by in-memory matcher: "+result.getUnsupportedReason());
			}
			if (result.matched()) {
				matches.add(resource);
			}
		}
		return matches;
	}
}
