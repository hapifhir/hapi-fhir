package ca.uhn.fhir.empi.rules.json;

/*-
 * #%L
 * HAPI FHIR - Enterprise Master Patient Index
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
public class EmpiResourceSearchParamJson implements IModelJson, Iterable<String> {
	@JsonProperty(value = "resourceType", required = true)
	String myResourceType;
	@JsonProperty(value = "searchParams", required = true)
	List<String> mySearchParams;

	public String getResourceType() {
		return myResourceType;
	}

	public EmpiResourceSearchParamJson setResourceType(String theResourceType) {
		myResourceType = theResourceType;
		return this;
	}

	public Iterator<String> iterator() {
		return getSearchParams().iterator();
	}

	public EmpiResourceSearchParamJson addSearchParam(String theSearchParam) {
		getSearchParams().add(theSearchParam);
		return this;
	}

	private List<String> getSearchParams() {
		if (mySearchParams == null) {
			mySearchParams = new ArrayList<>();
		}
		return mySearchParams;
	}
}
