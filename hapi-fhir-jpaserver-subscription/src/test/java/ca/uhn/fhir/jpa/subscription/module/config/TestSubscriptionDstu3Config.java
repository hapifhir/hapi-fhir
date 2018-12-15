package ca.uhn.fhir.jpa.subscription.module.config;

import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamProvider;
import ca.uhn.fhir.jpa.subscription.module.cache.ISubscriptionProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

@Configuration
@Import(TestSubscriptionConfig.class)
public class TestSubscriptionDstu3Config extends SubscriptionDstu3Config {
	@Bean
	@Primary
	public ISearchParamProvider searchParamProvider() {
		return new MockFhirClientSearchParamProvider();
	}

	@Bean
	@Primary
	public ISubscriptionProvider subsriptionProvider() { return new MockFhirClientSubscriptionProvider();}
}
