package ca.uhn.fhir.jpa.dao.search;

import ca.uhn.fhir.rest.api.SortSpec;
import org.hibernate.search.engine.search.sort.dsl.SearchSortFactory;
import org.hibernate.search.engine.search.sort.dsl.SortFinalStep;

/**
 * Helper for building freetext sort clauses
 */
public interface IHSearchSortHelper {

	SortFinalStep getSortClauses(SearchSortFactory theSortFactory, SortSpec theSort, String theResourceType);

}
