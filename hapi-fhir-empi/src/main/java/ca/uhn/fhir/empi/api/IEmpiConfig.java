package ca.uhn.fhir.empi.api;

import ca.uhn.fhir.empi.rules.json.EmpiRulesJson;

public interface IEmpiConfig {
	String EMPI_MATCHING_CHANNEL_NAME = "empi-matching";
	int EMPI_DEFAULT_CONSUMER_COUNT = 5;

	boolean isEnabled();

	int getConsumerCount();

	EmpiRulesJson getEmpiRules();
}
