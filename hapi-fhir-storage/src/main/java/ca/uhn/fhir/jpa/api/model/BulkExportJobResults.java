/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.jpa.api.model;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BulkExportJobResults implements IModelJson {

	@JsonProperty("resourceType2BinaryIds")
	private Map<String, List<String>> myResourceTypeToBinaryIds;

	@JsonProperty("reportMessage")
	private String myReportMsg;

	@JsonProperty("originalRequestUrl")
	private String myOriginalRequestUrl;

	public BulkExportJobResults() {}

	public Map<String, List<String>> getResourceTypeToBinaryIds() {
		if (myResourceTypeToBinaryIds == null) {
			myResourceTypeToBinaryIds = new HashMap<>();
		}
		return myResourceTypeToBinaryIds;
	}

	public String getOriginalRequestUrl() {
		return myOriginalRequestUrl;
	}

	public void setOriginalRequestUrl(String theOriginalRequestUrl) {
		myOriginalRequestUrl = theOriginalRequestUrl;
	}

	public void setResourceTypeToBinaryIds(Map<String, List<String>> theResourceTypeToBinaryIds) {
		myResourceTypeToBinaryIds = theResourceTypeToBinaryIds;
	}

	public String getReportMsg() {
		return myReportMsg;
	}

	public void setReportMsg(String theReportMsg) {
		myReportMsg = theReportMsg;
	}
}
