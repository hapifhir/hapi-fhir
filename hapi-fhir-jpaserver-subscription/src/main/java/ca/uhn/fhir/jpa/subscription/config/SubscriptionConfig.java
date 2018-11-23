package ca.uhn.fhir.jpa.subscription.config;

import ca.uhn.fhir.jpa.searchparam.config.SearchParamConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(SearchParamConfig.class)
@ComponentScan(basePackages = "ca.uhn.fhir.jpa.subscription")
public class SubscriptionConfig {
}
