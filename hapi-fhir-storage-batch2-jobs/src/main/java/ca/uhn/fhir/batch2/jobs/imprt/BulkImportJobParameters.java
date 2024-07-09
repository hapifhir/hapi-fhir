/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
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
package ca.uhn.fhir.batch2.jobs.imprt;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.model.api.annotation.SensitiveNoDisplay;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is the parameters model object for starting a
 * bulk import job.
 */
@JsonFilter(IModelJson.SENSITIVE_DATA_FILTER_NAME) // TODO GGG eventually consider pushing this up once we have more
// experience using it.
public class BulkImportJobParameters implements IModelJson {

	@JsonProperty(value = "ndJsonUrls", required = true)
	@Size(min = 1, message = "At least one NDJSON URL must be provided")
	@NotEmpty(message = "At least one NDJSON URL must be provided")
	private List<@Pattern(regexp = "^http[s]?://.*", message = "Must be a valid URL") String> myNdJsonUrls;

	@JsonProperty(value = "httpBasicCredentials", required = false)
	@Nullable
	@SensitiveNoDisplay
	private String myHttpBasicCredentials;

	@JsonProperty(value = "maxBatchResourceCount", required = false)
	@Min(1)
	@Nullable
	private Integer myMaxBatchResourceCount;

	@JsonProperty(value = "partitionId", required = false)
	@Nullable
	private RequestPartitionId myPartitionId;

	public List<String> getNdJsonUrls() {
		if (myNdJsonUrls == null) {
			myNdJsonUrls = new ArrayList<>();
		}
		return myNdJsonUrls;
	}

	public String getHttpBasicCredentials() {
		return myHttpBasicCredentials;
	}

	public BulkImportJobParameters setHttpBasicCredentials(String theHttpBasicCredentials) {
		myHttpBasicCredentials = theHttpBasicCredentials;
		return this;
	}

	@Nullable
	public Integer getMaxBatchResourceCount() {
		return myMaxBatchResourceCount;
	}

	public BulkImportJobParameters setMaxBatchResourceCount(@Nullable Integer theMaxBatchResourceCount) {
		myMaxBatchResourceCount = theMaxBatchResourceCount;
		return this;
	}

	public BulkImportJobParameters addNdJsonUrl(String theUrl) {
		Validate.notBlank(theUrl, "theUrl must not be blank or null");
		getNdJsonUrls().add(theUrl);
		return this;
	}

	@Nullable
	public RequestPartitionId getPartitionId() {
		return myPartitionId;
	}

	public BulkImportJobParameters setPartitionId(@Nullable RequestPartitionId thePartitionId) {
		myPartitionId = thePartitionId;
		return this;
	}
}
