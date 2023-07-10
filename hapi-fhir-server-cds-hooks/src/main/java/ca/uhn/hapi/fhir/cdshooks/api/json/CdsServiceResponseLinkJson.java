package ca.uhn.hapi.fhir.cdshooks.api.json;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Link used within a Cds Service Response
 */
public class CdsServiceResponseLinkJson implements IModelJson {
	@JsonProperty(value = "label", required = true)
	String myLabel;

	@JsonProperty(value = "url", required = true)
	String myUrl;

	@JsonProperty(value = "type", required = true)
	String myType;

	@JsonProperty(value = "appContext")
	String myAppContext;

	public String getLabel() {
		return myLabel;
	}

	public CdsServiceResponseLinkJson setLabel(String theLabel) {
		myLabel = theLabel;
		return this;
	}

	public String getUrl() {
		return myUrl;
	}

	public CdsServiceResponseLinkJson setUrl(String theUrl) {
		myUrl = theUrl;
		return this;
	}

	public String getType() {
		return myType;
	}

	public CdsServiceResponseLinkJson setType(String theType) {
		myType = theType;
		return this;
	}

	public String getAppContext() {
		return myAppContext;
	}

	public CdsServiceResponseLinkJson setAppContext(String theAppContext) {
		myAppContext = theAppContext;
		return this;
	}
}
