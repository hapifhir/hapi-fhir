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
 * Coding using within CdsService responses
 */
public class CdsServiceResponseCodingJson implements IModelJson {
	@JsonProperty(value = "code", required = true)
	String myCode;

	@JsonProperty("system")
	String mySystem;

	@JsonProperty("display")
	String myDisplay;

	public String getCode() {
		return myCode;
	}

	public CdsServiceResponseCodingJson setCode(String theCode) {
		myCode = theCode;
		return this;
	}

	public String getSystem() {
		return mySystem;
	}

	public CdsServiceResponseCodingJson setSystem(String theSystem) {
		mySystem = theSystem;
		return this;
	}

	public String getDisplay() {
		return myDisplay;
	}

	public CdsServiceResponseCodingJson setDisplay(String theDisplay) {
		myDisplay = theDisplay;
		return this;
	}
}
