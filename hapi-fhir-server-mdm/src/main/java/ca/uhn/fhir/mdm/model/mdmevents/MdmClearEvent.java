/*-
 * #%L
 * HAPI FHIR - Master Data Management
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
package ca.uhn.fhir.mdm.model.mdmevents;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class MdmClearEvent implements IModelJson {

	@JsonProperty("resourceTypes")
	private List<String> myResourceTypes;

	/**
	 * True if this submit was done asynchronously
	 * (ie, was submitted as a batch job).
	 * False if submitted directly to mdm.
	 */
	@JsonProperty("batchSize")
	private Long myBatchSize;

	public Long getBatchSize() {
		return myBatchSize;
	}

	public void setBatchSize(Long theBatchSize) {
		myBatchSize = theBatchSize;
	}

	public List<String> getResourceTypes() {
		if (myResourceTypes == null) {
			myResourceTypes = new ArrayList<>();
		}
		return myResourceTypes;
	}

	public void setResourceTypes(List<String> theResourceTypes) {
		myResourceTypes = theResourceTypes;
	}
}
