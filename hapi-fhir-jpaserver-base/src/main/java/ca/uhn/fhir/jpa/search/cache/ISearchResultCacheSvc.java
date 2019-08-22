package ca.uhn.fhir.jpa.search.cache;

import ca.uhn.fhir.jpa.entity.Search;

public interface ISearchResultCacheSvc {

	/**
	 * Places a new search of some sort in the cache.
	 *
	 * @param theSearch The search to store
	 * @return Returns a copy of the search as it was saved. Callers should use the returned Search object for any further processing.
	 */
	Search saveNew(Search theSearch);
}
