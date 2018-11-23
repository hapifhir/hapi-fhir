package ca.uhn.fhir.jpa.subscription.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.searchparam.config.SearchParamConfig;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamProvider;
import ca.uhn.fhir.jpa.subscription.FhirClientSearchParamProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(SearchParamConfig.class)
@ComponentScan(basePackages = "ca.uhn.fhir.jpa.subscription")
public abstract class BaseSubscriptionConfig {
	public abstract FhirContext fhirContext();

	@Bean
	protected ISearchParamProvider searchParamProvider() {
		return new FhirClientSearchParamProvider();
	}
}
