package ca.uhn.fhir.empi.rules.json;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
public class EmpiResourceSearchParamJson implements IModelJson {
	@JsonProperty("resourceType")
	String myResourceType;
	@JsonProperty("searchParam")
	String mySearchParam;

	public String getResourceType() {
		return myResourceType;
	}

	public EmpiResourceSearchParamJson setResourceType(String theResourceType) {
		myResourceType = theResourceType;
		return this;
	}

	public String getSearchParam() {
		return mySearchParam;
	}

	public EmpiResourceSearchParamJson setSearchParam(String theSearchParam) {
		mySearchParam = theSearchParam;
		return this;
	}
}
