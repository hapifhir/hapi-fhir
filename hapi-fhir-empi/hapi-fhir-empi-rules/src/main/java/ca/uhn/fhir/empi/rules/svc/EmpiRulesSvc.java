package ca.uhn.fhir.empi.rules.svc;

import ca.uhn.fhir.empi.rules.json.EmpiRulesJson;

public class EmpiRulesSvc {
	private final EmpiRulesJson myEmpiRulesJson;

	public EmpiRulesSvc(EmpiRulesJson theEmpiRulesJson) {
		myEmpiRulesJson = theEmpiRulesJson;
	}

	public EmpiRulesJson getEmpiRules() {
		return myEmpiRulesJson;
	}
}
