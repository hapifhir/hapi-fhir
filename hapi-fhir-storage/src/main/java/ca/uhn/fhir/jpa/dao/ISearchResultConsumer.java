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

import jakarta.annotation.Nonnull;

/**
 * This interface should be implemented by search result consumers. I.e. classes that are
 * responsible for processing search results as they are returned from the {@link ISearchBuilder}.
 */
@FunctionalInterface
public interface ISearchResultConsumer<T> {

	/**
	 * Return value for {@link #consume(SearchProgressTracker, Object)} indicating that the
	 * caller should continue to supply results.
	 */
	Outcome CONTINUE = new Outcome(true);

	/**
	 * Return value for {@link #consume(SearchProgressTracker, Object)} indicating that no
	 * further results are wanted.
	 */
	Outcome STOP = new Outcome(false);

	/**
	 * Consume a single search result from the search coordinator
	 */
	@Nonnull
	Outcome consume(SearchProgressTracker theProgressTracker, T theResult);

	/**
	 * Response type for {@link #consume(SearchProgressTracker, Object)}
	 */
	class Outcome {

		public boolean isContinue() {
			return myContinue;
		}

		private final boolean myContinue;

		private Outcome(boolean theContinue) {
			myContinue = theContinue;
		}
	}
}
