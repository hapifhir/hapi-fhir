package ca.uhn.fhir.jpa.subscription.config;

import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamProvider;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParamRegistryDstu3;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(TestSubscriptionConfig.class)
public class TestSubscriptionDstu3Config extends SubscriptionDstu3Config {
	@Bean
	@Override
	public ISearchParamProvider searchParamProvider() {
		return new MockSearchParamProvider();
	}

	@Bean
	@Override
	public ISearchParamRegistry searchParamRegistry() {
		return new SearchParamRegistryDstu3(searchParamProvider());
	}

}
