/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.search.builder.models;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
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
	private boolean myDeduplicateInDB;

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

	public boolean isDeduplicateInDatabase() {
		return myDeduplicateInDB;
	}

	/**
	 * Set of PIDs of results that have already been returned in a search.
	 *
	 * Searches use pre-fetch thresholds to avoid returning every result in the db
	 * (see {@link JpaStorageSettings mySearchPreFetchThresholds}). These threshold values
	 * dictate the usage of this set.
	 *
	 * Results from searches returning *less* than a prefetch threshold are put into this set
	 * for 2 purposes:
	 * 1) skipping already seen resources. ie, client requesting next "page" of
	 *    results should skip previously returned results
	 * 2) deduplication of returned results. ie, searches can return duplicate resources (due to
	 *    sort and filter criteria), so this set will be used to avoid returning duplicate results.
	 *
	 * NOTE: if a client requests *more* resources than *all* prefetch thresholds,
	 * we push the work of "deduplication" to the database. No newly seen resource
	 * will be stored in this set (to avoid this set exploding in size and the JVM running out memory).
	 * We will, however, still use it to skip previously seen results.
	 */
	public SearchQueryProperties setDeduplicateInDatabase(boolean theDeduplicateInDBFlag) {
		myDeduplicateInDB = theDeduplicateInDBFlag;
		return this;
	}

	public Integer getMaxResultsRequested() {
		return myMaxResultsRequested;
	}

	public SearchQueryProperties setMaxResultsRequested(Integer theMaxResultsRequested) {
		myMaxResultsRequested = theMaxResultsRequested;
		return this;
	}

	public boolean hasMaxResultsRequested() {
		return myMaxResultsRequested != null;
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

	public SearchQueryProperties clone() {
		return new SearchQueryProperties()
				.setMaxResultsRequested(myMaxResultsRequested)
				.setSortSpec(mySortSpec)
				.setOffset(myOffset)
				.setDoCountOnlyFlag(myDoCountOnlyFlag)
				.setDeduplicateInDatabase(myDeduplicateInDB);
	}
}
