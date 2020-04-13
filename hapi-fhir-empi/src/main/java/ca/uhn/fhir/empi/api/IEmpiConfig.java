package ca.uhn.fhir.empi.api;

import ca.uhn.fhir.empi.rules.config.EmpiConfigImpl;
import ca.uhn.fhir.empi.rules.json.EmpiRulesJson;

public interface IEmpiConfig {
	String EMPI_MATCHING_CHANNEL_NAME = "empi-matching";
	int EMPI_DEFAULT_CONCURRENT_CONSUMERS = 5;

	boolean isEnabled();

	EmpiConfigImpl setEnabled(boolean theEnabled);

	int getConcurrentConsumers();

	EmpiRulesJson getEmpiRules();
}
