/*-
 * #%L
 * HAPI FHIR - Master Data Management
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
package ca.uhn.fhir.mdm.model.mdmevents;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MdmEventResource implements IModelJson {
	/**
	 * The id of the resource that's part of this event.
	 */
	@JsonProperty("id")
	private String myId;

	/**
	 * The resource type.
	 */
	@JsonProperty("resourceType")
	private String myResourceType;

	/**
	 * True if this is a golden resource; false otherwise.
	 */
	@JsonProperty("isGoldenResource")
	private boolean myIsGoldenResource;

	public String getId() {
		return myId;
	}

	public void setId(String theId) {
		myId = theId;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}

	public boolean isGoldenResource() {
		return myIsGoldenResource;
	}

	public void setIsGoldenResource(boolean theGoldenResource) {
		myIsGoldenResource = theGoldenResource;
	}
}
