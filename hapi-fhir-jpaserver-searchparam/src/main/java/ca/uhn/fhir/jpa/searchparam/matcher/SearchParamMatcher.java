package ca.uhn.fhir.jpa.searchparam.matcher;

import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchParamMatcher {
	@Autowired
	private IndexedSearchParamExtractor myIndexedSearchParamExtractor;
	@Autowired
	private InMemoryResourceMatcher myInMemoryResourceMatcher;

	public InMemoryMatchResult match(String theCriteria, IBaseResource theResource) {
		ResourceIndexedSearchParams resourceIndexedSearchParams = myIndexedSearchParamExtractor.extractIndexedSearchParams(theResource);
		return myInMemoryResourceMatcher.match(theCriteria, theResource, resourceIndexedSearchParams);
	}
}
