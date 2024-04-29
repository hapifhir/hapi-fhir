/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
	public static final ReindexSearchParametersEnum REINDEX_SEARCH_PARAMETERS_DEFAULT = ReindexSearchParametersEnum.ALL;
	public static final String REINDEX_SEARCH_PARAMETERS_DEFAULT_STRING = "ALL";
	public static final boolean OPTIMISTIC_LOCK_DEFAULT = true;
	public static final OptimizeStorageModeEnum OPTIMIZE_STORAGE_DEFAULT = OptimizeStorageModeEnum.NONE;
	public static final String OPTIMIZE_STORAGE_DEFAULT_STRING = "NONE";
	private ReindexSearchParametersEnum myReindexSearchParameters = REINDEX_SEARCH_PARAMETERS_DEFAULT;
	private OptimizeStorageModeEnum myOptimizeStorage = OPTIMIZE_STORAGE_DEFAULT;
	private boolean myOptimisticLock = OPTIMISTIC_LOCK_DEFAULT;

	public boolean isOptimisticLock() {
		return myOptimisticLock;
	}

	public ReindexParameters setOptimisticLock(boolean theOptimisticLock) {
		myOptimisticLock = theOptimisticLock;
		return this;
	}

	public ReindexSearchParametersEnum getReindexSearchParameters() {
		return myReindexSearchParameters;
	}

	public ReindexParameters setReindexSearchParameters(ReindexSearchParametersEnum theReindexSearchParameters) {
		myReindexSearchParameters = theReindexSearchParameters;
		return this;
	}

	public OptimizeStorageModeEnum getOptimizeStorage() {
		return myOptimizeStorage;
	}

	public ReindexParameters setOptimizeStorage(OptimizeStorageModeEnum theOptimizeStorage) {
		myOptimizeStorage = theOptimizeStorage;
		return this;
	}

	public enum ReindexSearchParametersEnum {
		ALL,
		NONE
	}

	public enum OptimizeStorageModeEnum {
		NONE,
		CURRENT_VERSION,
		ALL_VERSIONS
	}
}
