package ca.uhn.fhir.jpa.subscription.process.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelRegistry;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionDeliveryChannelNamer;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionDeliveryHandlerFactory;
import ca.uhn.fhir.jpa.subscription.process.deliver.DaoResourceRetriever;
import ca.uhn.fhir.jpa.subscription.process.deliver.IResourceRetriever;
import ca.uhn.fhir.jpa.subscription.process.deliver.resthook.SubscriptionDeliveringRestHookSubscriber;
import ca.uhn.fhir.jpa.subscription.process.deliver.websocket.WebsocketConnectionValidator;
import ca.uhn.fhir.jpa.subscription.process.matcher.matching.CompositeInMemoryDaoSubscriptionMatcher;
import ca.uhn.fhir.jpa.subscription.process.matcher.matching.DaoSubscriptionMatcher;
import ca.uhn.fhir.jpa.subscription.process.matcher.matching.ISubscriptionMatcher;
import ca.uhn.fhir.jpa.subscription.process.matcher.matching.InMemorySubscriptionMatcher;
import ca.uhn.fhir.jpa.subscription.process.matcher.matching.SubscriptionStrategyEvaluator;
import ca.uhn.fhir.jpa.subscription.process.matcher.subscriber.MatchingQueueSubscriberLoader;
import ca.uhn.fhir.jpa.subscription.process.matcher.subscriber.SubscriptionActivatingSubscriber;
import ca.uhn.fhir.jpa.subscription.process.matcher.subscriber.SubscriptionMatchingSubscriber;
import ca.uhn.fhir.jpa.subscription.process.matcher.subscriber.SubscriptionRegisteringSubscriber;
import ca.uhn.fhir.jpa.subscription.process.registry.DaoSubscriptionProvider;
import ca.uhn.fhir.jpa.subscription.process.registry.ISubscriptionProvider;
import ca.uhn.fhir.jpa.subscription.process.registry.SubscriptionCanonicalizer;
import ca.uhn.fhir.jpa.subscription.process.registry.SubscriptionLoader;
import ca.uhn.fhir.jpa.subscription.process.registry.SubscriptionRegistry;
import ca.uhn.fhir.jpa.subscription.triggering.ISubscriptionTriggeringSvc;
import ca.uhn.fhir.jpa.subscription.triggering.SubscriptionTriggeringSvcImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

/**
 * This Spring config should be imported by a system that pulls messages off of the
 * matching queue for processing, and handles delivery
 */
public class SubscriptionProcessorConfig {

	@Bean
	public SubscriptionMatchingSubscriber subscriptionMatchingSubscriber() {
		return new SubscriptionMatchingSubscriber();
	}

	@Bean
	public SubscriptionActivatingSubscriber subscriptionActivatingSubscriber() {
		return new SubscriptionActivatingSubscriber();
	}

	@Bean
	public MatchingQueueSubscriberLoader subscriptionMatchingSubscriberLoader() {
		return new MatchingQueueSubscriberLoader();
	}

	@Bean
	public SubscriptionRegisteringSubscriber subscriptionRegisteringSubscriber() {
		return new SubscriptionRegisteringSubscriber();
	}

	@Bean
	public SubscriptionRegistry subscriptionRegistry() {
		return new SubscriptionRegistry();
	}

	@Bean
	public SubscriptionDeliveryChannelNamer subscriptionDeliveryChannelNamer() {
		return new SubscriptionDeliveryChannelNamer();
	}

	@Bean
	public ISubscriptionProvider subscriptionProvider() {
		return new DaoSubscriptionProvider();
	}

	@Bean
	public IResourceRetriever resourceRetriever() {
		return new DaoResourceRetriever();
	}

	@Bean
	public WebsocketConnectionValidator websocketConnectionValidator() {
		return new WebsocketConnectionValidator();
	}

	@Bean
	public SubscriptionStrategyEvaluator subscriptionStrategyEvaluator() {
		return new SubscriptionStrategyEvaluator();
	}

	@Bean
	public SubscriptionCanonicalizer subscriptionCanonicalizer(FhirContext theFhirContext) {
		return new SubscriptionCanonicalizer(theFhirContext);
	}

	@Bean
	public SubscriptionLoader subscriptionLoader() {
		return new SubscriptionLoader();
	}

	@Bean
	public SubscriptionChannelRegistry subscriptionChannelRegistry() {
		return new SubscriptionChannelRegistry();
	}

	@Bean
	public SubscriptionDeliveryHandlerFactory subscriptionDeliveryHandlerFactory() {
		return new SubscriptionDeliveryHandlerFactory();
	}

	@Bean
	@Lazy
	public ISubscriptionTriggeringSvc subscriptionTriggeringSvc() {
		return new SubscriptionTriggeringSvcImpl();
	}

	@Bean
	@Scope("prototype")
	public SubscriptionDeliveringRestHookSubscriber subscriptionDeliveringRestHookSubscriber() {
		return new SubscriptionDeliveringRestHookSubscriber();
	}

	@Bean
	public InMemorySubscriptionMatcher inMemorySubscriptionMatcher() {
		return new InMemorySubscriptionMatcher();
	}

	@Bean
	public DaoSubscriptionMatcher daoSubscriptionMatcher() {
		return new DaoSubscriptionMatcher();
	}

	@Bean
	@Primary
	public ISubscriptionMatcher subscriptionMatcher(DaoSubscriptionMatcher theDaoSubscriptionMatcher, InMemorySubscriptionMatcher theInMemorySubscriptionMatcher) {
		return new CompositeInMemoryDaoSubscriptionMatcher(theDaoSubscriptionMatcher, theInMemorySubscriptionMatcher);
	}

}
