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
package ca.uhn.fhir.jpa.subscription.match.matcher.subscriber;

import ca.uhn.fhir.IHapiBootOrder;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.config.SubscriptionSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelConsumerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelReceiver;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelFactory;
import ca.uhn.fhir.jpa.topic.SubscriptionTopicMatchingSubscriber;
import ca.uhn.fhir.jpa.topic.SubscriptionTopicRegisteringSubscriber;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;

import static ca.uhn.fhir.jpa.subscription.match.matcher.subscriber.SubscriptionMatchingSubscriber.SUBSCRIPTION_MATCHING_CHANNEL_NAME;

public class MatchingQueueSubscriberLoader {
	protected IChannelReceiver myMatchingChannel;
	private static final Logger ourLog = LoggerFactory.getLogger(MatchingQueueSubscriberLoader.class);

	@Autowired
	FhirContext myFhirContext;

	@Autowired
	private SubscriptionMatchingSubscriber mySubscriptionMatchingSubscriber;

	@Autowired(required = false)
	private SubscriptionTopicMatchingSubscriber mySubscriptionTopicMatchingSubscriber;

	@Autowired
	private SubscriptionChannelFactory mySubscriptionChannelFactory;

	@Autowired
	private SubscriptionRegisteringSubscriber mySubscriptionRegisteringSubscriber;

	@Autowired(required = false)
	private SubscriptionTopicRegisteringSubscriber mySubscriptionTopicRegisteringSubscriber;

	@Autowired
	private SubscriptionActivatingSubscriber mySubscriptionActivatingSubscriber;

	@Autowired
	private SubscriptionSettings mySubscriptionSettings;

	@EventListener(ContextRefreshedEvent.class)
	@Order(IHapiBootOrder.SUBSCRIPTION_MATCHING_CHANNEL_HANDLER)
	public void subscribeToMatchingChannel() {
		if (myMatchingChannel == null) {
			myMatchingChannel = mySubscriptionChannelFactory.newMatchingReceivingChannel(
					SUBSCRIPTION_MATCHING_CHANNEL_NAME, getChannelConsumerSettings());
		}
		if (myMatchingChannel != null) {
			myMatchingChannel.subscribe(mySubscriptionMatchingSubscriber);
			myMatchingChannel.subscribe(mySubscriptionActivatingSubscriber);
			myMatchingChannel.subscribe(mySubscriptionRegisteringSubscriber);
			ourLog.info(
					"Subscription Matching Subscriber subscribed to Matching Channel {} with name {}",
					myMatchingChannel.getClass().getName(),
					SUBSCRIPTION_MATCHING_CHANNEL_NAME);
			if (mySubscriptionTopicMatchingSubscriber != null) {
				ourLog.info("Starting SubscriptionTopic Matching Subscriber");
				myMatchingChannel.subscribe(mySubscriptionTopicMatchingSubscriber);
			}
			if (mySubscriptionTopicRegisteringSubscriber != null) {
				myMatchingChannel.subscribe(mySubscriptionTopicRegisteringSubscriber);
			}
		}
	}

	private ChannelConsumerSettings getChannelConsumerSettings() {
		ChannelConsumerSettings channelConsumerSettings = new ChannelConsumerSettings();
		channelConsumerSettings.setQualifyChannelName(
				mySubscriptionSettings.isQualifySubscriptionMatchingChannelName());
		return channelConsumerSettings;
	}

	@SuppressWarnings("unused")
	@PreDestroy
	public void stop() throws Exception {
		if (myMatchingChannel != null) {
			ourLog.info(
					"Destroying matching Channel {} with name {}",
					myMatchingChannel.getClass().getName(),
					SUBSCRIPTION_MATCHING_CHANNEL_NAME);
			myMatchingChannel.destroy();
			myMatchingChannel.unsubscribe(mySubscriptionMatchingSubscriber);
			myMatchingChannel.unsubscribe(mySubscriptionActivatingSubscriber);
			myMatchingChannel.unsubscribe(mySubscriptionRegisteringSubscriber);
		}
	}
}
