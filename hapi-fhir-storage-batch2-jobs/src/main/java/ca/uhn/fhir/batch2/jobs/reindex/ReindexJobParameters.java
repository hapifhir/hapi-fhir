/*-
 * #%L
 * HAPI-FHIR Storage Batch2 Jobs
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
package ca.uhn.fhir.batch2.jobs.reindex;

import ca.uhn.fhir.batch2.jobs.parameters.PartitionedUrlJobParameters;
import ca.uhn.fhir.jpa.api.dao.ReindexParameters;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;

import static org.apache.commons.lang3.ObjectUtils.getIfNull;

public class ReindexJobParameters extends PartitionedUrlJobParameters {

	public static final String OPTIMIZE_STORAGE = "optimizeStorage";
	public static final String REINDEX_SEARCH_PARAMETERS = "reindexSearchParameters";
	public static final String OPTIMISTIC_LOCK = "optimisticLock";
	public static final String INCLUDE_DELETED_RESOURCES = "includeDeletedResources";
	public static final String CORRECT_CURRENT_VERSION = "correctCurrentVersion";

	@JsonProperty(
			value = OPTIMIZE_STORAGE,
			defaultValue = ReindexParameters.OPTIMIZE_STORAGE_DEFAULT_STRING,
			required = false)
	@Nullable
	private ReindexParameters.OptimizeStorageModeEnum myOptimizeStorage;

	@JsonProperty(
			value = REINDEX_SEARCH_PARAMETERS,
			defaultValue = ReindexParameters.REINDEX_SEARCH_PARAMETERS_DEFAULT_STRING,
			required = false)
	@Nullable
	private ReindexParameters.ReindexSearchParametersEnum myReindexSearchParameters;

	@JsonProperty(
			value = OPTIMISTIC_LOCK,
			defaultValue = ReindexParameters.OPTIMISTIC_LOCK_DEFAULT + "",
			required = false)
	@Nullable
	private Boolean myOptimisticLock;

	@JsonProperty(
			value = CORRECT_CURRENT_VERSION,
			defaultValue = ReindexParameters.CORRECT_CURRENT_VERSION_DEFAULT_STRING,
			required = false)
	@Nullable
	private ReindexParameters.CorrectCurrentVersionModeEnum myCorrectCurrentVersion;

	@Nullable
	public ReindexParameters.CorrectCurrentVersionModeEnum getCorrectCurrentVersion() {
		return getIfNull(myCorrectCurrentVersion, ReindexParameters.CORRECT_CURRENT_VERSION_DEFAULT);
	}

	public void setCorrectCurrentVersion(
			@Nullable ReindexParameters.CorrectCurrentVersionModeEnum theCorrectCurrentVersion) {
		myCorrectCurrentVersion = theCorrectCurrentVersion;
	}

	public boolean getOptimisticLock() {
		return getIfNull(myOptimisticLock, ReindexParameters.OPTIMISTIC_LOCK_DEFAULT);
	}

	public ReindexJobParameters setOptimisticLock(boolean theOptimisticLock) {
		myOptimisticLock = theOptimisticLock;
		return this;
	}

	public ReindexParameters.OptimizeStorageModeEnum getOptimizeStorage() {
		return getIfNull(myOptimizeStorage, ReindexParameters.OPTIMIZE_STORAGE_DEFAULT);
	}

	public ReindexJobParameters setOptimizeStorage(ReindexParameters.OptimizeStorageModeEnum myOptimizeStorage) {
		this.myOptimizeStorage = myOptimizeStorage;
		return this;
	}

	public ReindexParameters.ReindexSearchParametersEnum getReindexSearchParameters() {
		return getIfNull(myReindexSearchParameters, ReindexParameters.REINDEX_SEARCH_PARAMETERS_DEFAULT);
	}

	public ReindexJobParameters setReindexSearchParameters(
			ReindexParameters.ReindexSearchParametersEnum theReindexSearchParameters) {
		this.myReindexSearchParameters = theReindexSearchParameters;
		return this;
	}
}
