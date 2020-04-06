package ca.uhn.fhir.empi.rules.json;

import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This SearchParamJson, unlike EmpiREsourceSearchParamJson, is responsible for doing inclusions during empi
 * candidate searching. e.g. When doing candidate matching, only consider candidates that match all EmpiFilterSearchParams.
 */
public class EmpiFilterSearchParamJson implements IModelJson {
	@JsonProperty("resourceType")
	String myResourceType;
	@JsonProperty("searchParam")
	String mySearchParam;
	@JsonProperty("qualifier")
	TokenParamModifier myTokenParamModifier;
	@JsonProperty("fixedValue")
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
