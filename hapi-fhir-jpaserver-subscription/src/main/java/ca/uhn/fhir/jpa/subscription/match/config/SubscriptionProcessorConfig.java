/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.subscription.match.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.searchparam.matcher.SearchParamMatcher;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelRegistry;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionDeliveryChannelNamer;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionDeliveryHandlerFactory;
import ca.uhn.fhir.jpa.subscription.match.deliver.email.IEmailSender;
import ca.uhn.fhir.jpa.subscription.match.deliver.email.SubscriptionDeliveringEmailSubscriber;
import ca.uhn.fhir.jpa.subscription.match.deliver.message.SubscriptionDeliveringMessageSubscriber;
import ca.uhn.fhir.jpa.subscription.match.deliver.resthook.SubscriptionDeliveringRestHookSubscriber;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.CompositeInMemoryDaoSubscriptionMatcher;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.DaoSubscriptionMatcher;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.ISubscriptionMatcher;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.InMemorySubscriptionMatcher;
import ca.uhn.fhir.jpa.subscription.match.matcher.subscriber.MatchingQueueSubscriberLoader;
import ca.uhn.fhir.jpa.subscription.match.matcher.subscriber.SubscriptionActivatingSubscriber;
import ca.uhn.fhir.jpa.subscription.match.matcher.subscriber.SubscriptionMatchDeliverer;
import ca.uhn.fhir.jpa.subscription.match.matcher.subscriber.SubscriptionMatchingSubscriber;
import ca.uhn.fhir.jpa.subscription.match.matcher.subscriber.SubscriptionRegisteringSubscriber;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionLoader;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionRegistry;
import ca.uhn.fhir.jpa.subscription.model.config.SubscriptionModelConfig;
import ca.uhn.fhir.jpa.topic.SubscriptionTopicDispatcher;
import ca.uhn.fhir.jpa.topic.SubscriptionTopicPayloadBuilder;
import ca.uhn.fhir.jpa.topic.filter.InMemoryTopicFilterMatcher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

/**
 * This Spring config should be imported by a system that pulls messages off of the
 * matching queue for processing, and handles delivery
 */
@Import(SubscriptionModelConfig.class)
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
	public SubscriptionLoader subscriptionLoader() {
		return new SubscriptionLoader();
	}

	@Bean
	public SubscriptionChannelRegistry subscriptionChannelRegistry() {
		return new SubscriptionChannelRegistry();
	}

	@Bean
	public SubscriptionDeliveryHandlerFactory subscriptionDeliveryHandlerFactory(
			ApplicationContext theApplicationContext, IEmailSender theEmailSender) {
		return new SubscriptionDeliveryHandlerFactory(theApplicationContext, theEmailSender);
	}

	@Bean
	public SubscriptionMatchDeliverer subscriptionMatchDeliverer(
			FhirContext theFhirContext,
			IInterceptorBroadcaster theInterceptorBroadcaster,
			SubscriptionChannelRegistry theSubscriptionChannelRegistry) {
		return new SubscriptionMatchDeliverer(
				theFhirContext, theInterceptorBroadcaster, theSubscriptionChannelRegistry);
	}

	@Bean
	@Scope("prototype")
	public SubscriptionDeliveringRestHookSubscriber subscriptionDeliveringRestHookSubscriber() {
		return new SubscriptionDeliveringRestHookSubscriber();
	}

	@Bean
	@Scope("prototype")
	public SubscriptionDeliveringMessageSubscriber subscriptionDeliveringMessageSubscriber(
			IChannelFactory theChannelFactory) {
		return new SubscriptionDeliveringMessageSubscriber(theChannelFactory);
	}

	@Bean
	@Scope("prototype")
	public SubscriptionDeliveringEmailSubscriber subscriptionDeliveringEmailSubscriber(IEmailSender theEmailSender) {
		return new SubscriptionDeliveringEmailSubscriber(theEmailSender);
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
	public ISubscriptionMatcher subscriptionMatcher(
			DaoSubscriptionMatcher theDaoSubscriptionMatcher,
			InMemorySubscriptionMatcher theInMemorySubscriptionMatcher) {
		return new CompositeInMemoryDaoSubscriptionMatcher(theDaoSubscriptionMatcher, theInMemorySubscriptionMatcher);
	}

	@Lazy
	@Bean
	SubscriptionTopicPayloadBuilder subscriptionTopicPayloadBuilder(FhirContext theFhirContext) {
		switch (theFhirContext.getVersion().getVersion()) {
			case R4:
			case R4B:
			case R5:
				return new SubscriptionTopicPayloadBuilder(theFhirContext);
			default:
				return null;
		}
	}

	@Lazy
	@Bean
	SubscriptionTopicDispatcher subscriptionTopicDispatcher(
			FhirContext theFhirContext,
			SubscriptionRegistry theSubscriptionRegistry,
			SubscriptionMatchDeliverer theSubscriptionMatchDeliverer,
			SubscriptionTopicPayloadBuilder theSubscriptionTopicPayloadBuilder) {
		return new SubscriptionTopicDispatcher(
				theFhirContext,
				theSubscriptionRegistry,
				theSubscriptionMatchDeliverer,
				theSubscriptionTopicPayloadBuilder);
	}

	@Bean
	InMemoryTopicFilterMatcher inMemoryTopicFilterMatcher(SearchParamMatcher theSearchParamMatcher) {
		return new InMemoryTopicFilterMatcher(theSearchParamMatcher);
	}
}
