package ca.uhn.fhir.jpa.subscription.module.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.subscription.module.matcher.ISubscriptionMatcher;
import ca.uhn.fhir.jpa.subscription.module.matcher.InMemorySubscriptionMatcher;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.test.utilities.UnregisterScheduledProcessor;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;

@Configuration
@TestPropertySource(properties = {
	"scheduling_disabled=true"
})
public class TestSubscriptionConfig {

	@Bean
	public ModelConfig modelConfig() {
		return new ModelConfig();
	}

	@Bean
	public IGenericClient fhirClient(FhirContext theFhirContext) {
        return Mockito.mock(IGenericClient.class);
	};

	@Bean
	public ISubscriptionMatcher inMemorySubscriptionMatcher() {
		return new InMemorySubscriptionMatcher();
	}

	@Bean
	public UnregisterScheduledProcessor unregisterScheduledProcessor(Environment theEnv) {
		return new UnregisterScheduledProcessor(theEnv);
	}
}
