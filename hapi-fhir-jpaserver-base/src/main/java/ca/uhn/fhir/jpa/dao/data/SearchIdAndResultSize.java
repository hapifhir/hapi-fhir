package ca.uhn.fhir.jpa.dao.data;

import java.util.Objects;

/**
 * Record for search result returning the PK of a Search, and the number of associated SearchResults
 */
public class SearchIdAndResultSize {
	/** Search PK */
	public final long searchId;
	/** Number of SearchResults attached */
	public final int size;

	public SearchIdAndResultSize(long theSearchId, Integer theSize) {
		searchId = theSearchId;
		size = Objects.requireNonNullElse(theSize, 0);
	}
}
