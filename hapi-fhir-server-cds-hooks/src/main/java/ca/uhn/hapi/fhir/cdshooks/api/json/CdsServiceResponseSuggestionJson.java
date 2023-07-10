package ca.uhn.hapi.fhir.cdshooks.api.json;


import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Allows a service to suggest a set of changes in the context of the current activity
 */
public class CdsServiceResponseSuggestionJson implements IModelJson {
	@JsonProperty(value = "label", required = true)
	String myLabel;

	@JsonProperty("uuid")
	String myUuid;

	@JsonProperty("isRecommended")
	Boolean myRecommended;

	@JsonProperty("actions")
	List<CdsServiceResponseSuggestionActionJson> myActions;

	public String getLabel() {
		return myLabel;
	}

	public CdsServiceResponseSuggestionJson setLabel(String theLabel) {
		myLabel = theLabel;
		return this;
	}

	public String getUuid() {
		return myUuid;
	}

	public CdsServiceResponseSuggestionJson setUuid(String theUuid) {
		myUuid = theUuid;
		return this;
	}

	public Boolean getRecommended() {
		return myRecommended;
	}

	public CdsServiceResponseSuggestionJson setRecommended(Boolean theRecommended) {
		myRecommended = theRecommended;
		return this;
	}

	public List<CdsServiceResponseSuggestionActionJson> getActions() {
		return myActions;
	}

	public void addAction(CdsServiceResponseSuggestionActionJson theAction) {
		if (myActions == null) {
			myActions = new ArrayList<>();
		}
		myActions.add(theAction);
	}
}
