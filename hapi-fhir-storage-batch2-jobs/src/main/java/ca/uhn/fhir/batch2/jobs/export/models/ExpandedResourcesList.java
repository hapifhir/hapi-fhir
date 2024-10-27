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

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ExpandedResourcesList extends BulkExportJobBase {

	/**
	 * List of stringified resources ready for writing
	 * to a file/binary.
	 */
	@JsonProperty("resources")
	private List<String> myStringifiedResources;

	/**
	 * only a single resource type per batch step
	 */
	@JsonProperty("resourceType")
	private String myResourceType;

	public List<String> getStringifiedResources() {
		return myStringifiedResources;
	}

	public void setStringifiedResources(List<String> theStringifiedResources) {
		myStringifiedResources = theStringifiedResources;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}
}
