/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.api.dao;

public class ReindexParameters {
	public static final boolean REINDEX_SEARCH_PARAMETERS_DEFAULT = true;
	public static final boolean OPTIMISTIC_LOCK_DEFAULT = true;
	public static final boolean OPTIMIZE_STORAGE_DEFAULT = false;
	private boolean myReindexSearchParameters = REINDEX_SEARCH_PARAMETERS_DEFAULT;
	private boolean myOptimizeStorage = OPTIMIZE_STORAGE_DEFAULT;
	private boolean myOptimisticLock = OPTIMISTIC_LOCK_DEFAULT;

	public boolean isOptimisticLock() {
		return myOptimisticLock;
	}

	public ReindexParameters setOptimisticLock(boolean theOptimisticLock) {
		myOptimisticLock = theOptimisticLock;
		return this;
	}

	public boolean isReindexSearchParameters() {
		return myReindexSearchParameters;
	}

	public ReindexParameters setReindexSearchParameters(boolean theReindexSearchParameters) {
		myReindexSearchParameters = theReindexSearchParameters;
		return this;
	}

	public boolean isOptimizeStorage() {
		return myOptimizeStorage;
	}

	public ReindexParameters setOptimizeStorage(boolean theOptimizeStorage) {
		myOptimizeStorage = theOptimizeStorage;
		return this;
	}

}
