package ca.uhn.fhir.jpa.search.builder.models;

import ca.uhn.fhir.rest.api.SortSpec;

public class SearchQueryProperties {

	/**
	 * True if this query is only to fetch the count (and not any results).
	 *
	 * True means this is a count only query
	 */
	private boolean myDoCountOnlyFlag;
	/**
	 * Whether or not we do deduplication of results in memory
	 * (using a hashset, etc), or push this to the database
	 * (using GROUP BY, etc).
	 *
	 * True means use the database
	 */
	private boolean myDeduplicateInDBFlag;

	/**
	 * The maximum number of results to fetch (when we want it limited).
	 * Can be null if we are fetching everything or paging.
	 */
	private Integer myMaxResultsRequested;
	/**
	 * The offset for the results to fetch.
	 *
	 * null if the first page, some number if it's a later page
	 */
	private Integer myOffset;

	/**
	 * The sort spec for this search
	 */
	private SortSpec mySortSpec;

	public boolean isDoCountOnlyFlag() {
		return myDoCountOnlyFlag;
	}

	public SearchQueryProperties setDoCountOnlyFlag(boolean theDoCountOnlyFlag) {
		myDoCountOnlyFlag = theDoCountOnlyFlag;
		return this;
	}

	public boolean isDeduplicateInDBFlag() {
		return myDeduplicateInDBFlag;
	}

	public SearchQueryProperties setDeduplicateInDBFlag(boolean theDeduplicateInDBFlag) {
		myDeduplicateInDBFlag = theDeduplicateInDBFlag;
		return this;
	}

	public Integer getMaxResultsRequested() {
		return myMaxResultsRequested;
	}

	public SearchQueryProperties setMaxResultsRequested(Integer theMaxResultsRequested) {
		myMaxResultsRequested = theMaxResultsRequested;
		return this;
	}

	public Integer getOffset() {
		return myOffset;
	}

	public boolean hasOffset() {
		return myOffset != null;
	}

	public SearchQueryProperties setOffset(Integer theOffset) {
		myOffset = theOffset;
		return this;
	}

	public SortSpec getSortSpec() {
		return mySortSpec;
	}

	public boolean hasSort() {
		return mySortSpec != null;
	}

	public SearchQueryProperties setSortSpec(SortSpec theSortSpec) {
		mySortSpec = theSortSpec;
		return this;
	}
}
