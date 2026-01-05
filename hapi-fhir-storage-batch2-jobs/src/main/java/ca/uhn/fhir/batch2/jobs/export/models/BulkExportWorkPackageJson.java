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
package ca.uhn.fhir.batch2.jobs.export.models;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class BulkExportWorkPackageJson implements IModelJson {
	@JsonProperty("resourceType")
	private String myResourceType;

	@JsonProperty("partitionId")
	private RequestPartitionId myPartitionId;

	@JsonProperty("patientIds")
	private List<String> myPatientIds;

	@JsonProperty("groupId")
	private String myGroupId;

	public String getResourceType() {
		return myResourceType;
	}

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}

	public RequestPartitionId getPartitionId() {
		return myPartitionId;
	}

	public void setPartitionId(RequestPartitionId thePartitionId) {
		myPartitionId = thePartitionId;
	}

	public void setPatientIds(List<String> thePatientIds) {
		myPatientIds = thePatientIds;
	}

	public List<String> getPatientIds() {
		return myPatientIds;
	}

	/**
	 * This will only be set when the export type is "GROUP" and {@link #getResourceType()}
	 * is "Group". For all other GROUP export resource types, this will be null and
	 * {@link #getPatientIds()} will be populated with the members of the group.
	 */
	public String getGroupId() {
		return myGroupId;
	}

	/**
	 * This will only be set when the export type is "GROUP" and {@link #getResourceType()}
	 * is "Group". For all other GROUP export resource types, this will be null and
	 * {@link #getPatientIds()} will be populated with the members of the group.
	 */
	public void setGroupId(String theGroupId) {
		myGroupId = theGroupId;
	}
}
