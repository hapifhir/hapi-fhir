package ca.uhn.fhir.jpa.subscription.config;

import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestSubscriptionConfig {
	@Bean
	public ModelConfig modelConfig() {
		return new ModelConfig();
	}
}
