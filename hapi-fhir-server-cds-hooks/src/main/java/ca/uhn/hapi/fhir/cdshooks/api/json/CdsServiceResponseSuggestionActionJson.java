/*-
 * #%L
 * HAPI FHIR - CDS Hooks
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
package ca.uhn.hapi.fhir.cdshooks.api.json;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 * A Suggested Action
 */
public class CdsServiceResponseSuggestionActionJson extends BaseCdsServiceJson implements IModelJson {
	@JsonProperty(value = "type", required = true)
	String myType;

	@JsonProperty("description")
	String myDescription;

	@JsonProperty("resource")
	IBaseResource myResource;

	public String getType() {
		return myType;
	}

	public CdsServiceResponseSuggestionActionJson setType(String theType) {
		myType = theType;
		return this;
	}

	public String getDescription() {
		return myDescription;
	}

	public CdsServiceResponseSuggestionActionJson setDescription(String theDescription) {
		myDescription = theDescription;
		return this;
	}

	public IBaseResource getResource() {
		return myResource;
	}

	public CdsServiceResponseSuggestionActionJson setResource(IBaseResource theResource) {
		myResource = theResource;
		return this;
	}
}
