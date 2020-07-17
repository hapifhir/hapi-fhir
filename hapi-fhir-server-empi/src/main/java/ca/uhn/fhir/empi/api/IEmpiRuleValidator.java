package ca.uhn.fhir.empi.api;

import ca.uhn.fhir.empi.rules.json.EmpiRulesJson;

public interface IEmpiRuleValidator {
	void validate(EmpiRulesJson theEmpiRules);
}
