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
package ca.uhn.fhir.batch2.jobs.export.models;

import ca.uhn.fhir.batch2.jobs.models.BatchResourceId;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ResourceIdList extends BulkExportJobBase {

	/**
	 * List of Id objects for serialization
	 */
	@JsonProperty("ids")
	private List<BatchResourceId> myBatchResourceIds;

	@JsonProperty("resourceType")
	private String myResourceType;

	public List<BatchResourceId> getIds() {
		return myBatchResourceIds;
	}

	public void setIds(List<BatchResourceId> theBatchResourceIds) {
		myBatchResourceIds = theBatchResourceIds;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}
}
