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

/**
 * Link used within a Cds Service Response
 */
public class CdsServiceResponseLinkJson implements IModelJson {
	@JsonProperty(value = "label", required = true)
	String myLabel;

	@JsonProperty(value = "url", required = true)
	String myUrl;

	@JsonProperty(value = "type", required = true)
	String myType;

	@JsonProperty(value = "appContext")
	String myAppContext;

	public String getLabel() {
		return myLabel;
	}

	public CdsServiceResponseLinkJson setLabel(String theLabel) {
		myLabel = theLabel;
		return this;
	}

	public String getUrl() {
		return myUrl;
	}

	public CdsServiceResponseLinkJson setUrl(String theUrl) {
		myUrl = theUrl;
		return this;
	}

	public String getType() {
		return myType;
	}

	public CdsServiceResponseLinkJson setType(String theType) {
		myType = theType;
		return this;
	}

	public String getAppContext() {
		return myAppContext;
	}

	public CdsServiceResponseLinkJson setAppContext(String theAppContext) {
		myAppContext = theAppContext;
		return this;
	}
}
