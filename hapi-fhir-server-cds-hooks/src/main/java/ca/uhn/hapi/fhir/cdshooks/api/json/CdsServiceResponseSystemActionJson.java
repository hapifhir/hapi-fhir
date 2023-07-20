/*-
 * #%L
 * HAPI FHIR - CDS Hooks
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
package ca.uhn.hapi.fhir.cdshooks.api.json;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 * Represents a CDS Service Response System Action
 */
public class CdsServiceResponseSystemActionJson extends BaseCdsServiceJson implements IModelJson {
	@JsonProperty(value = "type", required = true)
	String myType;

	@JsonProperty(value = "description", required = true)
	String myDescription;

	@JsonProperty(value = "resource")
	IBaseResource myResource;

	public String getType() {
		return myType;
	}

	public CdsServiceResponseSystemActionJson setType(String theType) {
		myType = theType;
		return this;
	}

	public String getDescription() {
		return myDescription;
	}

	public CdsServiceResponseSystemActionJson setDescription(String theDescription) {
		myDescription = theDescription;
		return this;
	}

	public IBaseResource getResource() {
		return myResource;
	}

	public CdsServiceResponseSystemActionJson setResource(IBaseResource theResource) {
		myResource = theResource;
		return this;
	}
}
