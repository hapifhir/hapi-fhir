package ca.uhn.fhir.jpa.subscription.config;

import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamProvider;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParamRegistryDstu3;
import org.springframework.context.annotation.*;

@Profile("test")
@Configuration
@Import(TestSubscriptionConfig.class)
public class TestSubscriptionDstu3Config extends BaseSubscriptionDstu3Config {
	@Bean
	@Primary
	public ISearchParamProvider searchParamProviderMock() {
		return new MockSearchParamProvider();
	}

	@Bean
	@Primary
	public ISearchParamRegistry searchParamRegistryMock() {
		return new SearchParamRegistryDstu3(searchParamProviderMock());
	}

}
