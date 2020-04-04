package ca.uhn.fhir.empi.rules.config;

import ca.uhn.fhir.empi.rules.json.EmpiRulesJson;
import ca.uhn.fhir.util.JsonUtil;

import java.io.IOException;

public class EmpiConfigImpl implements IEmpiConfig {

	private boolean myEnabled;
	private int myConsumerCount = EMPI_DEFAULT_CONSUMER_COUNT;
	private String myScriptText;
	private EmpiRulesJson myEmpiRules;

	@Override
	public boolean isEnabled() {
		return myEnabled;
	}

	public EmpiConfigImpl setEnabled(boolean theEnabled) {
		myEnabled = theEnabled;
		return this;
	}

	@Override
	public int getConsumerCount() {
		return myConsumerCount;
	}

	public EmpiConfigImpl setConsumerCount(int theConsumerCount) {
		myConsumerCount = theConsumerCount;
		return this;
	}

	public String getScriptText() {
		return myScriptText;
	}

	public EmpiConfigImpl setScriptText(String theScriptText) throws IOException {
		myScriptText = theScriptText;
		myEmpiRules = JsonUtil.deserialize(theScriptText, EmpiRulesJson.class);
		return this;
	}

	@Override
	public EmpiRulesJson getEmpiRules() {
		return myEmpiRules;
	}

	public EmpiConfigImpl setEmpiRules(EmpiRulesJson theEmpiRules) {
		myEmpiRules = theEmpiRules;
		return this;
	}
}
