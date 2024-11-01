package ca.uhn.fhirtest.config;

import ca.uhn.fhir.jpa.search.HapiHSearchAnalysisConfigurers;
import org.hibernate.search.backend.lucene.cfg.LuceneBackendSettings;
import org.hibernate.search.backend.lucene.cfg.LuceneIndexSettings;
import org.hibernate.search.engine.cfg.BackendSettings;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public abstract class BaseConfig {

	protected void configureLuceneProperties(Properties extraProperties, String fhirLuceneLocation) {
		extraProperties.put(BackendSettings.backendKey(BackendSettings.TYPE), "lucene");
		extraProperties.put(
				BackendSettings.backendKey(LuceneBackendSettings.ANALYSIS_CONFIGURER),
				HapiHSearchAnalysisConfigurers.HapiLuceneAnalysisConfigurer.class.getName());
		extraProperties.put(BackendSettings.backendKey(LuceneIndexSettings.DIRECTORY_TYPE), "local-filesystem");
		extraProperties.put(BackendSettings.backendKey(LuceneIndexSettings.DIRECTORY_ROOT), fhirLuceneLocation);
		extraProperties.put(BackendSettings.backendKey(LuceneBackendSettings.LUCENE_VERSION), "LUCENE_CURRENT");
	}
}
