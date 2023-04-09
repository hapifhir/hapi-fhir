package ca.uhn.fhir.jpa.topic;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.searchparam.matcher.SearchParamMatcher;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelRegistry;
import ca.uhn.fhir.jpa.subscription.match.matcher.subscriber.SubscriptionMatchDeliverer;
import org.springframework.context.annotation.Bean;

public class SubscriptionTopicConfig {
	@Bean
	public SubscriptionMatchDeliverer subscriptionMatchDeliverer(FhirContext theFhirContext, IInterceptorBroadcaster theInterceptorBroadcaster, SubscriptionChannelRegistry theSubscriptionChannelRegistry) {
		return new SubscriptionMatchDeliverer(theFhirContext, theInterceptorBroadcaster, theSubscriptionChannelRegistry);
	}

	@Bean
	public SubscriptionTopicMatchingSubscriber subscriptionTopicMatchingSubscriber() {
		return new SubscriptionTopicMatchingSubscriber();
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
