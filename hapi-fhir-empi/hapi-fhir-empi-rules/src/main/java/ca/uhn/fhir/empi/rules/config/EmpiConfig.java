package ca.uhn.fhir.empi.rules.config;

import ca.uhn.fhir.empi.rules.json.EmpiRulesJson;
import ca.uhn.fhir.util.JsonUtil;

import java.io.IOException;

// FIXME KHS collapse these two projects into hapi-fhir-empi
public class EmpiConfig {
	private boolean myEmpiEnabled;
	private int myConcurrentConsumers;
	private String myScriptText;
	private EmpiRulesJson myEmpiRules;

	public boolean isEmpiEnabled() {
		return myEmpiEnabled;
	}

	public EmpiConfig setEmpiEnabled(boolean theEmpiEnabled) {
		myEmpiEnabled = theEmpiEnabled;
		return this;
	}

	public int getConcurrentConsumers() {
		return myConcurrentConsumers;
	}

	public EmpiConfig setConcurrentConsumers(int theConcurrentConsumers) {
		myConcurrentConsumers = theConcurrentConsumers;
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
