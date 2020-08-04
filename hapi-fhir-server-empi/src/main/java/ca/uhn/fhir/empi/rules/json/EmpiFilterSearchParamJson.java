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
import ca.uhn.fhir.rest.param.TokenParamModifier;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This SearchParamJson, unlike EmpiREsourceSearchParamJson, is responsible for doing inclusions during empi
 * candidate searching. e.g. When doing candidate matching, only consider candidates that match all EmpiFilterSearchParams.
 */
public class EmpiFilterSearchParamJson implements IModelJson {
	@JsonProperty(value = "resourceType", required = true)
	String myResourceType;
	@JsonProperty(value = "searchParam", required = true)
	String mySearchParam;
	@JsonProperty(value = "qualifier", required = true)
	TokenParamModifier myTokenParamModifier;
	@JsonProperty(value = "fixedValue", required = true)
	String myFixedValue;

	public String getResourceType() {
		return myResourceType;
	}

	public EmpiFilterSearchParamJson setResourceType(String theResourceType) {
		myResourceType = theResourceType;
		return this;
	}

	public String getSearchParam() {
		return mySearchParam;
	}

	public EmpiFilterSearchParamJson setSearchParam(String theSearchParam) {
		mySearchParam = theSearchParam;
		return this;
	}


	public TokenParamModifier getTokenParamModifier() {
		return myTokenParamModifier;
	}

	public EmpiFilterSearchParamJson setTokenParamModifier(TokenParamModifier theTokenParamModifier) {
		myTokenParamModifier = theTokenParamModifier;
		return this;
	}

	public String getFixedValue() {
		return myFixedValue;
	}

	public EmpiFilterSearchParamJson setFixedValue(String theFixedValue) {
		myFixedValue = theFixedValue;
		return this;
	}

    public String getTokenParamModifierAsString() {
		return myTokenParamModifier == null ? "" : myTokenParamModifier.getValue();
    }
}
