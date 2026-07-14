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

@FunctionalInterface
public interface ISearchResultConsumer<T> {

	Outcome CONTINUE = new Outcome(true);
	Outcome STOP = new Outcome(false);

	/**
	 * Consume a single search result from the search coordinator
	 */
	@Nonnull
	Outcome consume(SearchProgressTracker theProgressTracker, T theResult);

	class Outcome {

		public boolean isContinue() {
			return myContinue;
		}

		private boolean myContinue;

		private Outcome(boolean theContinue) {
			myContinue = theContinue;
		}
	}
}
