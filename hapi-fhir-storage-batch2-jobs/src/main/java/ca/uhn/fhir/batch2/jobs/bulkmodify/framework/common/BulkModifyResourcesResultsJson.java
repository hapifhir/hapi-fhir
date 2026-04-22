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
package ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

@Schema(
		name = "BulkModifyResourcesResults",
		description = "Contains details about the outcome of a bulk modification operation")
public class BulkModifyResourcesResultsJson implements IModelJson {

	@JsonProperty("report")
	private String myReport;

	@JsonProperty("resourcesChangedCount")
	private Integer myResourcesChangedCount;

	@JsonProperty("resourcesChanged")
	@JsonRawValue
	@JsonDeserialize(using = ArrayOfRawJsonObjectsAsStringsDeserializer.class)
	private List<String> myResourcesChangedBodies;

	@JsonProperty("resourcesUnchangedCount")
	private Integer myResourcesUnchangedCount;

	@JsonProperty("resourcesFailedCount")
	private Integer myResourcesFailedCount;

	@JsonProperty("resourcesDeleted")
	private List<String> myResourcesDeletedIds;

	public Integer getResourcesFailedCount() {
		return myResourcesFailedCount;
	}

	public void setResourcesFailedCount(Integer theResourcesFailedCount) {
		myResourcesFailedCount = theResourcesFailedCount;
	}

	public Integer getResourcesUnchangedCount() {
		return myResourcesUnchangedCount;
	}

	public void setResourcesUnchangedCount(Integer theResourcesUnchangedCount) {
		myResourcesUnchangedCount = theResourcesUnchangedCount;
	}

	public Integer getResourcesChangedCount() {
		return myResourcesChangedCount;
	}

	public void setResourcesChangedCount(Integer theResourcesChangedCount) {
		myResourcesChangedCount = theResourcesChangedCount;
	}

	public String getReport() {
		return myReport;
	}

	public void setReport(String theReport) {
		myReport = theReport;
	}

	public List<String> getResourcesChangedBodies() {
		if (myResourcesChangedBodies == null) {
			myResourcesChangedBodies = new ArrayList<>();
		}
		return myResourcesChangedBodies;
	}

	public void setResourcesChangedBodies(List<String> theResourcesChangedBodies) {
		assert theResourcesChangedBodies.stream().allMatch(t -> t.startsWith("{") && t.endsWith("}"))
				: "All resources must be JSON: " + theResourcesChangedBodies;
		myResourcesChangedBodies = theResourcesChangedBodies;
	}

	public void setResourcesDeletedIds(List<String> theResourcesDeletedIds) {
		myResourcesDeletedIds = theResourcesDeletedIds;
	}

	public List<String> getResourcesDeletedIds() {
		return myResourcesDeletedIds;
	}
}
