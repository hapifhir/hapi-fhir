/*
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.jpa.packages;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Schema(description = "Represents an NPM package installation response")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(
		creatorVisibility = JsonAutoDetect.Visibility.NONE,
		fieldVisibility = JsonAutoDetect.Visibility.NONE,
		getterVisibility = JsonAutoDetect.Visibility.NONE,
		isGetterVisibility = JsonAutoDetect.Visibility.NONE,
		setterVisibility = JsonAutoDetect.Visibility.NONE)
public class PackageInstallOutcomeJson {

	@JsonProperty("messages")
	private List<String> myMessage;

	@JsonProperty("resourcesInstalled")
	private Map<String, Integer> myResourcesInstalled;

	@Schema(description = "This map is populated only in the case of a dry-run and contains a mapping of resource type to unique identifier (for most resources, this will be url or url + version (if MULTI_VERSION mode is specified)) for resources that would be overwritten or updated should a real run be done.")
	@JsonProperty("replacedResourceTypeToUniqueIdentifier")
	private Map<String, List<String>> myReplacedResourceToUniqueIdentifier;

	@Schema(description = "This map is populated only in the case of a dry-run and contains a mapping of resource type to unique identifier for resources that would be created anew should a real run be done.")
	@JsonProperty("addedResourceTypeToUniqueIdentifier")
	private Map<String, List<String>> myAddedResourceTypeToUniqueIdentifier;

	public List<String> getMessage() {
		if (myMessage == null) {
			myMessage = new ArrayList<>();
		}
		return myMessage;
	}

	public Map<String, Integer> getResourcesInstalled() {
		if (myResourcesInstalled == null) {
			myResourcesInstalled = new HashMap<>();
		}
		return myResourcesInstalled;
	}

	public Map<String, List<String>> getAddedResourceTypeToUniqueIdentifier() {
		if (myAddedResourceTypeToUniqueIdentifier == null) {
			myAddedResourceTypeToUniqueIdentifier = new HashMap<>();
		}
		return myAddedResourceTypeToUniqueIdentifier;
	}

	public void addResourceTypeToBeAdded(String theResourceType, String theUniqeId) {
		Map<String, List<String>> map = getAddedResourceTypeToUniqueIdentifier();
		if (!map.containsKey(theResourceType)) {
			map.put(theResourceType, new ArrayList<>());
		}
		map.get(theResourceType).add(theUniqeId);
	}

	public Map<String, List<String>> getReplacedResourceToUniqueIdentifier() {
		if (myReplacedResourceToUniqueIdentifier == null) {
			myReplacedResourceToUniqueIdentifier = new HashMap<>();
		}
		return myReplacedResourceToUniqueIdentifier;
	}

	public void addExistingResource(String theResourceType, String theUniqueId) {
		Map<String, List<String>> map = getReplacedResourceToUniqueIdentifier();
		if (!map.containsKey(theResourceType)) {
			map.put(theResourceType, new ArrayList<>());
		}
		map.get(theResourceType)
			.add(theUniqueId);
	}

	public void incrementResourcesInstalled(String theResourceType) {
		Integer existing = getResourcesInstalled().get(theResourceType);
		if (existing == null) {
			getResourcesInstalled().put(theResourceType, 1);
		} else {
			getResourcesInstalled().put(theResourceType, existing + 1);
		}
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("message", myMessage)
				.append("resourcesInstalled", myResourcesInstalled)
				.toString();
	}
}
