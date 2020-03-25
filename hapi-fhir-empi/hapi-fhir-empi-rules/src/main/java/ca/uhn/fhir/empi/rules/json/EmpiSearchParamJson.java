package ca.uhn.fhir.empi.rules.json;

import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EmpiSearchParamJson implements IModelJson {
	@JsonProperty("resourceType")
	String myResourceType;
	@JsonProperty("searchParam")
	String mySearchParam;
	@JsonProperty("searchParamMatchType")
	SearchParamMatchTypeEnum myMatchType;
	@JsonProperty("qualifier")
	TokenParamModifier myTokenParamModifier;
	@JsonProperty("fixedValue")
	String myFixedValue;

	public String getResourceType() {
		return myResourceType;
	}

	public EmpiSearchParamJson setResourceType(String theResourceType) {
		myResourceType = theResourceType;
		return this;
	}

	public String getSearchParam() {
		return mySearchParam;
	}

	public EmpiSearchParamJson setSearchParam(String theSearchParam) {
		mySearchParam = theSearchParam;
		return this;
	}

	public SearchParamMatchTypeEnum getMatchType() {
		return myMatchType;
	}

	public EmpiSearchParamJson setMatchType(SearchParamMatchTypeEnum theMatchType) {
		myMatchType = theMatchType;
		return this;
	}

	public TokenParamModifier getTokenParamModifier() {
		return myTokenParamModifier;
	}

	public EmpiSearchParamJson setTokenParamModifier(TokenParamModifier theTokenParamModifier) {
		myTokenParamModifier = theTokenParamModifier;
		return this;
	}

	public String getFixedValue() {
		return myFixedValue;
	}

	public EmpiSearchParamJson setFixedValue(String theFixedValue) {
		myFixedValue = theFixedValue;
		return this;
	}
}
