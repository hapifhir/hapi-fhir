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
package ca.uhn.fhir.jpa.subscription.channel.subscription;

import ca.uhn.fhir.broker.api.ChannelConsumerSettings;
import ca.uhn.fhir.broker.api.ChannelProducerSettings;
import ca.uhn.fhir.broker.api.IChannelConsumer;
import ca.uhn.fhir.broker.api.IChannelProducer;
import ca.uhn.fhir.broker.api.IMessageListener;
import ca.uhn.fhir.broker.impl.MultiplexingListener;
import ca.uhn.fhir.jpa.subscription.api.ISubscriptionDeliveryValidator;
import ca.uhn.fhir.jpa.subscription.channel.models.ProducingChannelParameters;
import ca.uhn.fhir.jpa.subscription.channel.models.ReceivingChannelParameters;
import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionRegistry;
import ca.uhn.fhir.jpa.subscription.model.ChannelRetryConfiguration;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Multimaps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Nonnull;

public class SubscriptionChannelRegistry {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionRegistry.class);

	private final SubscriptionConsumerCache myDeliveryConsumerCache = new SubscriptionConsumerCache();
	// This map is a reference count so we know to destroy the channel when there are no more active subscriptions using
	// it
	// Key Channel Name, Value Subscription Id
	private final Multimap<String, String> myActiveSubscriptionByChannelName = Multimaps.synchronizedMultimap(
			MultimapBuilder.hashKeys().arrayListValues().build());

	@Autowired
	private SubscriptionDeliveryListenerFactory mySubscriptionDeliveryListenerFactory;

	@Autowired
	private SubscriptionChannelFactory mySubscriptionDeliveryChannelFactory;

	@Autowired
	private ISubscriptionDeliveryValidator mySubscriptionDeliveryValidator;

	public SubscriptionChannelRegistry() {}

	private final Map<String, IChannelProducer<ResourceDeliveryMessage>> myChannelProducers = new ConcurrentHashMap<>();

	public synchronized void add(ActiveSubscription theActiveSubscription) {
		String channelName = theActiveSubscription.getChannelName();
		ourLog.info("Adding subscription {} to channel {}", theActiveSubscription.getId(), channelName);
		myActiveSubscriptionByChannelName.put(channelName, theActiveSubscription.getId());

		if (myDeliveryConsumerCache.containsKey(channelName)) {
			ourLog.info("Channel {} already exists.  Not creating.", channelName);
			return;
		}

		// we get the retry configurations from the cannonicalized subscriber
		// these will be provided to both the producer and receiver channel
		ChannelRetryConfiguration retryConfigParameters = theActiveSubscription.getRetryConfigurationParameters();

		/*
		 * When we create a subscription, we create both
		 * a producing/sending channel and
		 * a receiving channel.
		 *
		 * Matched subscriptions are sent to the Sending channel
		 * and the sending channel sends to subscription matching service.
		 *
		 * Receiving channel will send it out to
		 * the subscriber hook (REST, email, etc).
		 */

		// the receiving channel
		// this sends to the hook (resthook/message/email/whatever)
		ReceivingChannelParameters receivingParameters = new ReceivingChannelParameters(channelName);
		receivingParameters.setRetryConfiguration(retryConfigParameters);

		SubscriptionResourceDeliveryMessageConsumer subscriptionResourceDeliveryMessageConsumer =
				buildSubscriptionResourceDeliveryMessageConsumer(theActiveSubscription, receivingParameters);
		myDeliveryConsumerCache.put(channelName, subscriptionResourceDeliveryMessageConsumer);

		// create the producing channel.
		// channel used for sending to subscription matcher
		ProducingChannelParameters producingChannelParameters = new ProducingChannelParameters(channelName);
		producingChannelParameters.setRetryConfiguration(retryConfigParameters);

		IChannelProducer<ResourceDeliveryMessage> producer = newProducer(producingChannelParameters);
		myChannelProducers.put(channelName, producer);
	}

	/**
	 * We package the consumer together with its listeners so it can be reused when possible without expensive teardown/rebuild.
	 * @param theActiveSubscription The active subscription being delivered to
	 * @param receivingParameters retry parameters
	 * @return a SubscriptionResourceDeliveryMessageConsumer that packages the consumer with listeners and an api to add/remove listeners
	 */
	@Nonnull
	private SubscriptionResourceDeliveryMessageConsumer buildSubscriptionResourceDeliveryMessageConsumer(
			ActiveSubscription theActiveSubscription, ReceivingChannelParameters receivingParameters) {
		MultiplexingListener<ResourceDeliveryMessage> multiplexingListener =
				new MultiplexingListener<>(ResourceDeliveryMessage.class);
		IChannelConsumer<ResourceDeliveryMessage> deliveryConsumer =
				newDeliveryConsumer(multiplexingListener, receivingParameters);
		Optional<IMessageListener<ResourceDeliveryMessage>> oDeliveryListener =
				mySubscriptionDeliveryListenerFactory.createDeliveryListener(theActiveSubscription.getChannelType());

		SubscriptionResourceDeliveryMessageConsumer subscriptionResourceDeliveryMessageConsumer =
				new SubscriptionResourceDeliveryMessageConsumer(deliveryConsumer);
		SubscriptionValidatingListener subscriptionValidatingListener =
				new SubscriptionValidatingListener(mySubscriptionDeliveryValidator, theActiveSubscription.getIdDt());
		subscriptionResourceDeliveryMessageConsumer.addListener(subscriptionValidatingListener);
		oDeliveryListener.ifPresent(subscriptionResourceDeliveryMessageConsumer::addListener);
		return subscriptionResourceDeliveryMessageConsumer;
	}

	protected IChannelConsumer<ResourceDeliveryMessage> newDeliveryConsumer(
			IMessageListener<ResourceDeliveryMessage> theListener, ReceivingChannelParameters theParameters) {
		ChannelConsumerSettings settings = new ChannelConsumerSettings();
		settings.setRetryConfiguration(theParameters.getRetryConfiguration());
		return mySubscriptionDeliveryChannelFactory.newDeliveryConsumer(
				theParameters.getChannelName(), theListener, settings);
	}

	protected IChannelProducer<ResourceDeliveryMessage> newProducer(ProducingChannelParameters theParameters) {
		ChannelProducerSettings settings = new ChannelProducerSettings();
		settings.setRetryConfiguration(theParameters.getRetryConfiguration());
		return mySubscriptionDeliveryChannelFactory.newDeliveryProducer(theParameters.getChannelName(), settings);
	}

	public void remove(ActiveSubscription theActiveSubscription) {
		String channelName = theActiveSubscription.getChannelName();
		ourLog.info("Removing subscription {} from channel {}", theActiveSubscription.getId(), channelName);
		boolean removed = myActiveSubscriptionByChannelName.remove(channelName, theActiveSubscription.getId());

		if (!removed) {
			ourLog.warn("Failed to remove subscription {} from channel {}", theActiveSubscription.getId(), channelName);
		}

		// This was the last one.  Close and remove the channel
		if (!myActiveSubscriptionByChannelName.containsKey(channelName)) {
			SubscriptionResourceDeliveryMessageConsumer deliveryConsumer = myDeliveryConsumerCache.get(channelName);
			if (deliveryConsumer != null) {
				deliveryConsumer.close();
			}
			myDeliveryConsumerCache.closeAndRemove(channelName);
			myChannelProducers.remove(channelName);
		}
	}

	public synchronized SubscriptionResourceDeliveryMessageConsumer getDeliveryConsumerWithListeners(
			String theChannelName) {
		return myDeliveryConsumerCache.get(theChannelName);
	}

	public synchronized IChannelProducer<ResourceDeliveryMessage> getDeliveryChannelProducer(String theChannelName) {
		return myChannelProducers.get(theChannelName);
	}

	public synchronized int size() {
		return myDeliveryConsumerCache.size();
	}

	@VisibleForTesting
	public void logForUnitTest() {
		myDeliveryConsumerCache.logForUnitTest();
	}
}
