package ca.uhn.fhir.jpa.subscription.model;

import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.server.messaging.json.BaseJsonMessage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PayloadPOJO extends BaseJsonMessage<AfterPOJO> {
	@JsonProperty("after")
	AfterPOJO after;

	public PayloadPOJO() {
	}

	@Override
	public AfterPOJO getPayload(){
		return after;
	}

	public AfterPOJO getAfter() {
		return after;
	}

	void setAfter(AfterPOJO theAfter) {
		after = theAfter;
	}
}
