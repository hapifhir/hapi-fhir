package ca.uhn.fhir.batch2.jobs.reindex;

/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
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

import ca.uhn.fhir.batch2.jobs.parameters.PartitionedUrlListJobParameters;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

public class ReindexJobParameters extends PartitionedUrlListJobParameters {

	public static final String OPTIMIZE_STORAGE = "optimizeStorage";
	public static final String REINDEX_SEARCH_PARAMETERS = "reindexSearchParameters";

	@JsonProperty(value = OPTIMIZE_STORAGE, defaultValue = "false", required = false)
	@Nullable
	private Boolean myOptimizeStorage;
	@JsonProperty(value = REINDEX_SEARCH_PARAMETERS, defaultValue = "true", required = false)
	@Nullable
	private Boolean myReindexSearchParameters;

	@Nullable
	public boolean isOptimizeStorage() {
		return defaultIfNull(myOptimizeStorage, Boolean.FALSE);
	}

	public ReindexJobParameters setOptimizeStorage(boolean myOptimizeStorage) {
		this.myOptimizeStorage = myOptimizeStorage;
		return this;
	}

	public boolean isReindexSearchParameters() {
		return defaultIfNull(myReindexSearchParameters, Boolean.TRUE);
	}

	public ReindexJobParameters setReindexSearchParameters(boolean myReindexSearchParameters) {
		this.myReindexSearchParameters = myReindexSearchParameters;
		return this;
	}

}
