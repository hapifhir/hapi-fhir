package ca.uhn.fhirtest.config;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;

public class CommonJpaStorageSettingsConfigurer {
	public CommonJpaStorageSettingsConfigurer(JpaStorageSettings theStorageSettings) {
		theStorageSettings.setHibernateSearchIndexFullText(false);
		theStorageSettings.setHibernateSearchIndexSearchParams(false);

		theStorageSettings.setIndexOnUpliftedRefchains(true);
		theStorageSettings.setMarkResourcesForReindexingUponSearchParameterChange(false);
		theStorageSettings.setLanguageSearchParameterEnabled(true);
	}
}
