package ca.uhn.hapi.fhir.cdshooks.api.json;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the list of cards accepted in CdsServiceFeedback
 */
public class CdsServiceAcceptedSuggestionJson implements IModelJson {
	@JsonProperty(value = "id", required = true)
	String myId;

	public String getId() {
		return myId;
	}

	public CdsServiceAcceptedSuggestionJson setId(String theId) {
		myId = theId;
		return this;
	}
}
