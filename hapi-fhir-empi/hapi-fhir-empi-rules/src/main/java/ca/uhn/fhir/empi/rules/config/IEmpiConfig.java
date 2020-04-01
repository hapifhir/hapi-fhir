package ca.uhn.fhir.empi.rules.config;

import ca.uhn.fhir.empi.rules.json.EmpiRulesJson;

public interface IEmpiConfig {
	boolean isEnabled();

	int getConsumerCount();

	EmpiRulesJson getEmpiRules();
}
