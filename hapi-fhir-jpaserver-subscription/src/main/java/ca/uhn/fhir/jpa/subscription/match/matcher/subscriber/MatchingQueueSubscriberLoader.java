/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
import ca.uhn.fhir.broker.api.ChannelConsumerSettings;
import ca.uhn.fhir.broker.api.IChannelConsumer;
import ca.uhn.fhir.broker.impl.MultiplexingListener;
import ca.uhn.fhir.broker.util.CloseUtil;
import ca.uhn.fhir.jpa.model.config.SubscriptionSettings;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelFactory;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.topic.SubscriptionTopicMatchingListener;
import ca.uhn.fhir.jpa.topic.SubscriptionTopicRegisteringListener;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;

import static ca.uhn.fhir.jpa.subscription.match.matcher.subscriber.SubscriptionMatchingListener.SUBSCRIPTION_MATCHING_CHANNEL_NAME;

public class MatchingQueueSubscriberLoader {
	private static final Logger ourLog = LoggerFactory.getLogger(MatchingQueueSubscriberLoader.class);

	@Autowired
	private SubscriptionMatchingListener mySubscriptionMatchingListener;

	@Autowired
	private SubscriptionActivatingListener mySubscriptionActivatingListener;

	@Autowired(required = false)
	private SubscriptionTopicMatchingListener mySubscriptionTopicMatchingListener;

	@Autowired
	private SubscriptionRegisteringListener mySubscriptionRegisteringListener;

	@Autowired
	private SubscriptionChannelFactory mySubscriptionChannelFactory;

	@Autowired(required = false)
	private SubscriptionTopicRegisteringListener mySubscriptionTopicRegisteringListener;

	@Autowired
	private SubscriptionSettings mySubscriptionSettings;

	protected IChannelConsumer<ResourceModifiedMessage> myMatchingConsumer;
	private MultiplexingListener<ResourceModifiedMessage> myMultiplexingListener;

	@EventListener(ContextRefreshedEvent.class)
	@Order(IHapiBootOrder.SUBSCRIPTION_MATCHING_CHANNEL_HANDLER)
	public void subscribeToMatchingChannel() {
		if (myMatchingConsumer == null) {
			myMultiplexingListener = new MultiplexingListener<>(ResourceModifiedMessage.class);

			myMatchingConsumer = mySubscriptionChannelFactory.newMatchingConsumer(
					SUBSCRIPTION_MATCHING_CHANNEL_NAME, myMultiplexingListener, getChannelConsumerSettings());
			myMultiplexingListener.addListener(mySubscriptionMatchingListener);
			myMultiplexingListener.addListener(mySubscriptionActivatingListener);
			myMultiplexingListener.addListener(mySubscriptionRegisteringListener);
			if (mySubscriptionTopicMatchingListener != null) {
				ourLog.info("Starting SubscriptionTopic Matching Subscriber");
				myMultiplexingListener.addListener(mySubscriptionTopicMatchingListener);
			}
			if (mySubscriptionTopicRegisteringListener != null) {
				myMultiplexingListener.addListener(mySubscriptionTopicRegisteringListener);
			}
			if (myMatchingConsumer != null) { // can be null in mock tests
				ourLog.info(
						"Subscription Matching Subscriber subscribed to Matching Channel {} with name {}",
						myMatchingConsumer.getClass().getName(),
						SUBSCRIPTION_MATCHING_CHANNEL_NAME);
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
		if (myMatchingConsumer != null) {
			ourLog.info(
					"Destroying matching Channel {} with name {}",
					myMatchingConsumer.getClass().getName(),
					SUBSCRIPTION_MATCHING_CHANNEL_NAME);
			CloseUtil.close(myMatchingConsumer);
		}
		if (myMultiplexingListener != null) {
			CloseUtil.close(myMultiplexingListener);
		}
	}
}
