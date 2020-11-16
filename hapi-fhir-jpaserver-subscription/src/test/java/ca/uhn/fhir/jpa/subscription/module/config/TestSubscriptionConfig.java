package ca.uhn.fhir.jpa.subscription.module.config;

import ca.uhn.fhir.jpa.cache.IResourceChangeListenerRegistry;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.InMemorySubscriptionMatcher;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;

import static org.mockito.Mockito.mock;

@Configuration
@TestPropertySource(properties = {
	"scheduling_disabled=true"
})
public class TestSubscriptionConfig {

	@Bean
	public PartitionSettings partitionSettings() {
		return new PartitionSettings();
	}

	@Bean
	public ModelConfig modelConfig() {
		return new ModelConfig();
	}

	@Bean
	public IGenericClient fhirClient() {
        return mock(IGenericClient.class);
	};

	@Bean
	public InMemorySubscriptionMatcher inMemorySubscriptionMatcher() {
		return new InMemorySubscriptionMatcher();
	}

	@Bean
	public IResourceChangeListenerRegistry resourceChangeListenerRegistry() {
		return mock(IResourceChangeListenerRegistry.class);
	}

}
