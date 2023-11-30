package ca.uhn.fhir.jpa.test.config;

import ca.uhn.fhir.jpa.config.HapiJpaConfig;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.spy;

/**
 * This is a Test configuration class that allows spying underlying JpaConfigs beans
 */
@Configuration
public class TestHapiJpaConfig extends HapiJpaConfig {

	@Override
	@Bean
	public DatabaseBackedPagingProvider databaseBackedPagingProvider() {
		return spy(super.databaseBackedPagingProvider());
	}
}
