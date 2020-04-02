package ca.uhn.fhir.jpa.subscription.module.config;

import ca.uhn.fhir.interceptor.executor.InterceptorService;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.subscription.module.matcher.ISubscriptionMatcher;
import ca.uhn.fhir.jpa.subscription.module.matcher.InMemorySubscriptionMatcher;
import ca.uhn.fhir.jpa.subscription.module.standalone.StandaloneSubscriptionConfig;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

@Configuration
@TestPropertySource(properties = {
	"scheduling_disabled=true"
})
@Import(StandaloneSubscriptionConfig.class)
public class TestSubscriptionConfig extends SubscriptionConfig {

	@Bean
	public ModelConfig modelConfig() {
		return new ModelConfig();
	}

	@Bean
	public IGenericClient fhirClient() {
        return Mockito.mock(IGenericClient.class);
	};

	@Bean
	public ISubscriptionMatcher inMemorySubscriptionMatcher() {
		return new InMemorySubscriptionMatcher();
	}

	@Bean
	public InterceptorService interceptorRegistry() {
		return new InterceptorService("hapi-fhir-jpa-subscription");
	}

}
