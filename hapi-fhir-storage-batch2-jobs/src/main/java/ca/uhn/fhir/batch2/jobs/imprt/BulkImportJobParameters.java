package ca.uhn.fhir.batch2.jobs.imprt;

/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nullable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is the parameters model object for starting a
 * bulk import job.
 */
public class BulkImportJobParameters implements IModelJson {

	@JsonProperty(value = "ndJsonUrls", required = true)
	@Size(min = 1, message = "At least one NDJSON URL must be provided")
	@NotNull(message = "At least one NDJSON URL must be provided")
	private List<@Pattern(regexp = "^http[s]?://.*", message = "Must be a valid URL") String> myNdJsonUrls;

	@JsonProperty(value = "httpBasicCredentials", access = JsonProperty.Access.WRITE_ONLY, required = false)
	@Nullable
	private String myHttpBasicCredentials;

	@JsonProperty(value = "maxBatchResourceCount", required = false)
	@Min(1)
	@Nullable
	private Integer myMaxBatchResourceCount;

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
}
