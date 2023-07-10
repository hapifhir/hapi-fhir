package ca.uhn.hapi.fhir.cdshooks.api.json;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 * A Suggested Action
 */
public class CdsServiceResponseSuggestionActionJson extends BaseCdsServiceJson implements IModelJson {
	@JsonProperty(value = "type", required = true)
	String myType;

	@JsonProperty("description")
	String myDescription;

	@JsonProperty("resource")
	IBaseResource myResource;

	public String getType() {
		return myType;
	}

	public CdsServiceResponseSuggestionActionJson setType(String theType) {
		myType = theType;
		return this;
	}

	public String getDescription() {
		return myDescription;
	}

	public CdsServiceResponseSuggestionActionJson setDescription(String theDescription) {
		myDescription = theDescription;
		return this;
	}

	public IBaseResource getResource() {
		return myResource;
	}

	public CdsServiceResponseSuggestionActionJson setResource(IBaseResource theResource) {
		myResource = theResource;
		return this;
	}
}
