package ca.uhn.fhir.jpa.subscription.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class DebeziumDeserializer {
	@JsonProperty("payload")
	String payload;

	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public DebeziumDeserializer() {}

	public DebeziumDeserializer(String thePayload) {
		payload = thePayload;
	}

	public String getPayload() {
		return payload;
	}

	void setPayload(String thePayload) {
		payload = thePayload;
	}


}

