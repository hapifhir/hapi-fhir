package ca.uhn.fhir.jpa.search.cache;

import ca.uhn.fhir.jpa.entity.Search;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ISearchResultCacheSvc {
	/**
	 * @param theSearch                       The search - This method is not required to persist any chances to the Search object, it is only provided here for identification
	 * @param thePreviouslyStoredResourcePids A list of resource PIDs that have previously been saved to this search
	 * @param theNewResourcePids              A list of new resoure PIDs to add to this search (these ones have not been previously saved)
	 */
	void storeResults(Search theSearch, List<Long> thePreviouslyStoredResourcePids, List<Long> theNewResourcePids);

	/**
	 * Fetch a sunset of the search result IDs from the cache
	 *
	 * @param theSearch The search to fetch IDs for
	 * @param theFrom   The starting index (inclusive)
	 * @param theTo     The ending index (exclusive)
	 * @return A list of resource PIDs
	 */
	List<Long> fetchResultPids(Search theSearch, int theFrom, int theTo);

	/**
	 * Fetch all result PIDs for a given search with no particular order required
	 * @param theSearch
	 * @return
	 */
	List<Long> fetchAllResultPids(Search theSearch);
}
