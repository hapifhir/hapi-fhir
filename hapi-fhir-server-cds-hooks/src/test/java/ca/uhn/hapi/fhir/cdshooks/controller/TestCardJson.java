package ca.uhn.hapi.fhir.cdshooks.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TestCardJson {
	@JsonProperty("summary")
	String mySummary;

	@JsonProperty("indicator")
	String myIndicator;

	@JsonProperty("source")
	TestCardSourceJson mySource;

	@JsonProperty("detail")
	String myDetail;
}
