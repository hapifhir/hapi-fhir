package ca.uhn.hapi.fhir.cdshooks.api.json;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the reason a specific service suggestion was overridden
 */
public class CdsServiceOverrideReasonJson implements IModelJson {
	@JsonProperty("reason")
	CdsServiceResponseCodingJson myReason;

	@JsonProperty("userComment")
	String myUserComment;

	public CdsServiceResponseCodingJson getReason() {
		return myReason;
	}

	public CdsServiceOverrideReasonJson setReason(CdsServiceResponseCodingJson theReason) {
		myReason = theReason;
		return this;
	}

	public String getUserComment() {
		return myUserComment;
	}

	public CdsServiceOverrideReasonJson setUserComment(String theUserComment) {
		myUserComment = theUserComment;
		return this;
	}
}
