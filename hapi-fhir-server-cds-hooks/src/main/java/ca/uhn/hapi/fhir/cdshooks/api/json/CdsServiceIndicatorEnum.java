package ca.uhn.hapi.fhir.cdshooks.api.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum CdsServiceIndicatorEnum {
	@JsonProperty("info")
	INFO,

	@JsonProperty("warning")
	WARNING,

	@JsonProperty("critical")
	CRITICAL
}
