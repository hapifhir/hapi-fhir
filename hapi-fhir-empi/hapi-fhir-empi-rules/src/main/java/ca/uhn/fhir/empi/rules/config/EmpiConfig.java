package ca.uhn.fhir.empi.rules.config;

import ca.uhn.fhir.empi.rules.json.EmpiRulesJson;
import ca.uhn.fhir.util.JsonUtil;

import java.io.IOException;

// FIXME KHS collapse these two projects into hapi-fhir-empi
public class EmpiConfig {
	private boolean myEnabled;
	private int myConsumerCount;
	private String myScriptText;
	private EmpiRulesJson myEmpiRules;

	public boolean isEnabled() {
		return myEnabled;
	}

	public EmpiConfig setEnabled(boolean theEnabled) {
		myEnabled = theEnabled;
		return this;
	}

	public int getConsumerCount() {
		return myConsumerCount;
	}

	public EmpiConfig setConsumerCount(int theConsumerCount) {
		myConsumerCount = theConsumerCount;
		return this;
	}

	public String getScriptText() {
		return myScriptText;
	}

	public EmpiConfig setScriptText(String theScriptText) throws IOException {
		myScriptText = theScriptText;
		myEmpiRules = JsonUtil.deserialize(theScriptText, EmpiRulesJson.class);
		return this;
	}

	public EmpiRulesJson getEmpiRules() {
		return myEmpiRules;
	}

	public EmpiConfig setEmpiRules(EmpiRulesJson theEmpiRules) {
		myEmpiRules = theEmpiRules;
		return this;
	}
}
