package ca.uhn.fhir.jpa.topic;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.searchparam.matcher.SearchParamMatcher;
import org.springframework.context.annotation.Bean;

public class SubscriptionTopicConfig {
	@Bean
	public SubscriptionTopicMatchingSubscriber subscriptionTopicMatchingSubscriber(FhirContext theFhirContext) {
		return new SubscriptionTopicMatchingSubscriber(theFhirContext);
	}

	@Bean
	public SubscriptionTopicPayloadBuilder subscriptionTopicPayloadBuilder(FhirContext theFhirContext) {
		return new SubscriptionTopicPayloadBuilder(theFhirContext);
	}

	@Bean
	public SubscriptionTopicRegistry subscriptionTopicRegistry() {
		return new SubscriptionTopicRegistry();
	}

	@Bean
	public SubscriptionTopicSupport subscriptionTopicSupport(FhirContext theFhirContext, DaoRegistry theDaoRegistry, SearchParamMatcher theSearchParamMatcher) {
		return new SubscriptionTopicSupport(theFhirContext, theDaoRegistry, theSearchParamMatcher);
	}

	@Bean
	public SubscriptionTopicLoader subscriptionTopicLoader() {
		return new SubscriptionTopicLoader();
	}
}
