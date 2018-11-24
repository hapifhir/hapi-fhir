package ca.uhn.fhir.jpa.subscription.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamProvider;
import ca.uhn.fhir.jpa.subscription.FhirClientSearchParamProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "ca.uhn.fhir.jpa")
public abstract class BaseSubscriptionConfig {
	public abstract FhirContext fhirContext();

	@Bean
	protected ISearchParamProvider searchParamProvider() {
		return new FhirClientSearchParamProvider();
	}
}
