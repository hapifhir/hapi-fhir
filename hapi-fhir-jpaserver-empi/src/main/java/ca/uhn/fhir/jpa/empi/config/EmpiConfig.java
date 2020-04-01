package ca.uhn.fhir.jpa.empi.config;

public class EmpiConfig {
	private boolean myEmpiEnabled;
	private int myConcurrentConsumers;
	private String myScriptText;

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

	public EmpiConfig setScriptText(String theScriptText) {
		myScriptText = theScriptText;
		return this;
	}
}
