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

/**
 * @see <a href=" https://cds-hooks.hl7.org/1.0/#extensions">For reading more about Extension support in CDS hooks</a>
 */
public abstract class BaseCdsServiceJson implements IModelJson {

	@JsonProperty(value = "extension", required = true)
	String myExtension;

	public String getExtension() {
		return myExtension;
	}

	public BaseCdsServiceJson setExtension(String theExtension) {
		this.myExtension = theExtension;
		return this;
	}
}
