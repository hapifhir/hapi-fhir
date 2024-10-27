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
 * Represents a CDS Hooks Service Response Card Source
 */
public class CdsServiceResponseCardSourceJson implements IModelJson {
	@JsonProperty(value = "label", required = true)
	String myLabel;

	@JsonProperty("url")
	String myUrl;

	@JsonProperty("icon")
	String myIcon;

	@JsonProperty("topic")
	CdsServiceResponseCodingJson myTopic;

	public String getLabel() {
		return myLabel;
	}

	public CdsServiceResponseCardSourceJson setLabel(String theLabel) {
		myLabel = theLabel;
		return this;
	}

	public String getUrl() {
		return myUrl;
	}

	public CdsServiceResponseCardSourceJson setUrl(String theUrl) {
		myUrl = theUrl;
		return this;
	}

	public String getIcon() {
		return myIcon;
	}

	public CdsServiceResponseCardSourceJson setIcon(String theIcon) {
		myIcon = theIcon;
		return this;
	}

	public CdsServiceResponseCodingJson getTopic() {
		return myTopic;
	}

	public CdsServiceResponseCardSourceJson setTopic(CdsServiceResponseCodingJson theTopic) {
		myTopic = theTopic;
		return this;
	}
}
