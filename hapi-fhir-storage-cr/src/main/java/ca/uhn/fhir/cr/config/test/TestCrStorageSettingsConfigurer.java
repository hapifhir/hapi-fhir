package ca.uhn.fhir.cr.config.test;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;

public class TestCrStorageSettingsConfigurer {

	private final JpaStorageSettings myStorageSettings;

	public TestCrStorageSettingsConfigurer(JpaStorageSettings theStorageSettings) {
		myStorageSettings = theStorageSettings;
	}

	public void setUpConfiguration() {
		myStorageSettings.setAllowExternalReferences(true);
		myStorageSettings.setEnforceReferentialIntegrityOnWrite(false);
		myStorageSettings.setEnforceReferenceTargetTypes(false);
		myStorageSettings.setResourceClientIdStrategy(JpaStorageSettings.ClientIdStrategyEnum.ANY);
	}

}
