package ca.uhn.fhir.broker.api;

import ca.uhn.fhir.rest.server.messaging.json.BaseJsonMessage;
import ca.uhn.fhir.rest.server.messaging.json.HapiMessageHeaders;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class StringMessage extends BaseJsonMessage<String> {
	@JsonProperty("payload")
	private String myPayload;

	public StringMessage() {}

	public StringMessage(String thePayload) {
		myPayload = thePayload;
	}

	public StringMessage(String thePayload, Map<String, Object> theHeaders) {
		myPayload = thePayload;
		super.setHeaders(new HapiMessageHeaders(theHeaders));
	}

	@Override
	public String getPayload() {
		return myPayload;
	}

	public void setPayload(String thePayload) {
		myPayload = thePayload;
	}

	@Override
	public String toString() {
		throw new UnsupportedOperationException();
	}
}
