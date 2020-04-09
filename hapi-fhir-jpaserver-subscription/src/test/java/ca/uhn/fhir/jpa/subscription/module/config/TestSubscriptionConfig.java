package ca.uhn.fhir.jpa.subscription.module.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.ISubscriptionMatcher;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.InMemorySubscriptionMatcher;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
	public InMemorySubscriptionMatcher inMemorySubscriptionMatcher() {
		return new InMemorySubscriptionMatcher();
	}

}
