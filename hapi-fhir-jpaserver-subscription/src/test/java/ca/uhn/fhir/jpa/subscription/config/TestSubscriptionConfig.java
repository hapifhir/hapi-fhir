package ca.uhn.fhir.jpa.subscription.config;

import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParamRegistryConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestSubscriptionConfig {
	@Bean
	public ModelConfig modelConfig() {
		return new ModelConfig();
	}

	@Bean
	public SearchParamRegistryConfig searchParamConfig() {
		return new SearchParamRegistryConfig();
	}
}
