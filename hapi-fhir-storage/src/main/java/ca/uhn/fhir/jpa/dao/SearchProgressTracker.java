/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.dao;

import java.util.Objects;

// FIXME: clean up class names, tostring, etc
public final class SearchProgressTracker {
	private final boolean haveMoreResults;
	private final int skippedCount;
	private final int nonSkippedCount;
	// FIXME: remove these if not used
	private final String myPreviousPageId;
	private final String myNextPageId;
	private final String myCurrentPageId;

	public SearchProgressTracker(boolean haveMoreResults, int skippedCount, int nonSkippedCount) {
		this(haveMoreResults, skippedCount, nonSkippedCount, null, null, null);
	}

	public SearchProgressTracker(
			boolean haveMoreResults,
			int skippedCount,
			int nonSkippedCount,
			String theCurrentPageId,
			String thePreviousPageId,
			String theNextPageId) {
		this.haveMoreResults = haveMoreResults;
		this.skippedCount = skippedCount;
		this.nonSkippedCount = nonSkippedCount;
		myCurrentPageId = theCurrentPageId;
		myPreviousPageId = thePreviousPageId;
		myNextPageId = theNextPageId;
	}

	public String getPreviousPageId() {
		return myPreviousPageId;
	}

	public String getNextPageId() {
		return myNextPageId;
	}

	public String getCurrentPageId() {
		return myCurrentPageId;
	}

	public boolean haveMoreResults() {
		return haveMoreResults;
	}

	public int skippedCount() {
		return skippedCount;
	}

	public int nonSkippedCount() {
		return nonSkippedCount;
	}

	public int totalCount() {
		return skippedCount + nonSkippedCount;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (SearchProgressTracker) obj;
		return this.haveMoreResults == that.haveMoreResults
				&& this.skippedCount == that.skippedCount
				&& this.nonSkippedCount == that.nonSkippedCount;
	}

	@Override
	public int hashCode() {
		return Objects.hash(haveMoreResults, skippedCount, nonSkippedCount);
	}

	@Override
	public String toString() {
		return "PerformSearchOutcome[" + "haveMoreResults="
				+ haveMoreResults + ", " + "skippedCount="
				+ skippedCount + ", " + "nonSkippedCount="
				+ nonSkippedCount + ']';
	}
}
