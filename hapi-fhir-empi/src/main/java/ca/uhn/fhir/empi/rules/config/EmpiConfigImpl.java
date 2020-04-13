package ca.uhn.fhir.empi.rules.config;

import ca.uhn.fhir.empi.api.IEmpiConfig;
import ca.uhn.fhir.empi.rules.json.EmpiRulesJson;
import ca.uhn.fhir.util.JsonUtil;

import java.io.IOException;

public class EmpiConfigImpl implements IEmpiConfig {
	private boolean myEnabled;
	private int myConcurrentConsumers = EMPI_DEFAULT_CONCURRENT_CONSUMERS;
	private String myScriptText;
	private EmpiRulesJson myEmpiRules;

	@Override
	public boolean isEnabled() {
		return myEnabled;
	}

	@Override
	public EmpiConfigImpl setEnabled(boolean theEnabled) {
		myEnabled = theEnabled;
		return this;
	}

	@Override
	public int getConcurrentConsumers() {
		return myConcurrentConsumers;
	}

	public EmpiConfigImpl setConcurrentConsumers(int theConcurrentConsumers) {
		myConcurrentConsumers = theConcurrentConsumers;
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
