/*-
 * #%L
 * HAPI-FHIR Storage Batch2 Jobs
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
package ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base;

import ca.uhn.fhir.batch2.jobs.parameters.PartitionedUrlJobParameters;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Base class for bulk modification job parameters objects
 */
public abstract class BaseBulkModifyJobParameters extends PartitionedUrlJobParameters {

	@JsonProperty("dryRun")
	private Boolean myDryRun;

	@JsonProperty("dryRunMode")
	private DryRunMode myDryRunMode;

	@JsonProperty("limitResourceVersionCount")
	private Integer myLimitResourceVersionCount;

	public DryRunMode getDryRunMode() {
		return myDryRunMode;
	}

	public BaseBulkModifyJobParameters setDryRunMode(DryRunMode theDryRunMode) {
		myDryRunMode = theDryRunMode;
		return this;
	}

	public Integer getLimitResourceVersionCount() {
		return myLimitResourceVersionCount;
	}

	public void setLimitResourceVersionCount(Integer theLimitResourceVersionCount) {
		myLimitResourceVersionCount = theLimitResourceVersionCount;
	}

	public boolean isDryRun() {
		return Boolean.TRUE.equals(myDryRun);
	}

	public BaseBulkModifyJobParameters setDryRun(boolean theDryRun) {
		myDryRun = theDryRun;
		return this;
	}

	public enum DryRunMode {
		COUNT,
		COLLECT_CHANGED
	}

	/**
	 * Concrete implementation of this class which can be used to deserialize
	 * any subclass of {@link BaseBulkModifyJobParameters}
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class DeserializingImpl extends BaseBulkModifyJobParameters implements IModelJson {
		// nothing
	}
}
