package ca.uhn.fhir.jpa.packages;

/*
 * #%L
 * HAPI FHIR JPA Server
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
@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class PackageInstallOutcomeJson {

	@JsonProperty("messages")
	private List<String> myMessage;

	@JsonProperty("resourcesInstalled")
	private Map<String, Integer> myResourcesInstalled;

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
