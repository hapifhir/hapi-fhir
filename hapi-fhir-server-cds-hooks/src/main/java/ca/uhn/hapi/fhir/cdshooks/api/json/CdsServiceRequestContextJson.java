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
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CdsServiceRequestContextJson extends BaseCdsServiceJson implements IModelJson {

	@JsonAnyGetter
	private Map<String, Object> myMap;

	public String getString(String theKey) {
		if (myMap == null) {
			return null;
		}
		return (String) myMap.get(theKey);
	}

	public List<String> getArray(String theKey) {
		if (myMap == null) {
			return Collections.emptyList();
		}
		return (List<String>) myMap.get(theKey);
	}

	public IBaseResource getResource(String theKey) {
		if (myMap == null) {
			return null;
		}
		return (IBaseResource) myMap.get(theKey);
	}

	public void put(String theKey, Object theValue) {
		if (myMap == null) {
			myMap = new LinkedHashMap<>();
		}
		myMap.put(theKey, theValue);
	}

	public Set<String> getKeys() {
		if (myMap == null) {
			return Collections.emptySet();
		}
		return myMap.keySet();
	}

	public Object get(String theKey) {
		if (myMap == null) {
			return null;
		}
		return myMap.get(theKey);
	}

	public boolean containsKey(String theKey) {
		if (myMap == null) {
			return false;
		}
		return myMap.containsKey(theKey);
	}
}
