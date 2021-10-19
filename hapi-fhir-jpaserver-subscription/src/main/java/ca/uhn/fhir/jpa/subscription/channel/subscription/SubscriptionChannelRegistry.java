package ca.uhn.fhir.jpa.subscription.channel.subscription;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.subscription.channel.api.ChannelConsumerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelProducerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelProducer;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelReceiver;
import ca.uhn.fhir.jpa.subscription.channel.models.ProducingChannelParameters;
import ca.uhn.fhir.jpa.subscription.channel.models.ReceivingChannelParameters;
import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionRegistry;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscriptionChannelType;
import ca.uhn.fhir.jpa.subscription.model.ChannelRetryConfiguration;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SubscriptionChannelRegistry {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionRegistry.class);

	private final SubscriptionChannelCache myDeliveryReceiverChannels = new SubscriptionChannelCache();
	// This map is a reference count so we know to destroy the channel when there are no more active subscriptions using it
	// Key Channel Name, Value Subscription Id
	private final Multimap<String, String> myActiveSubscriptionByChannelName = MultimapBuilder.hashKeys().arrayListValues().build();
	private final Map<String, IChannelProducer> myChannelNameToSender = new ConcurrentHashMap<>();

	@Autowired
	private SubscriptionDeliveryHandlerFactory mySubscriptionDeliveryHandlerFactory;
	@Autowired
	private SubscriptionChannelFactory mySubscriptionDeliveryChannelFactory;

	public synchronized void add(ActiveSubscription theActiveSubscription) {
		String channelName = theActiveSubscription.getChannelName();
		ourLog.info("Adding subscription {} to channel {}", theActiveSubscription.getId(), channelName);
		myActiveSubscriptionByChannelName.put(channelName, theActiveSubscription.getId());

		if (myDeliveryReceiverChannels.containsKey(channelName)) {
			ourLog.info("Channel {} already exists.  Not creating.", channelName);
			return;
		}

		// the receiving channel
		ReceivingChannelParameters receivingParameters = new ReceivingChannelParameters(channelName);
		IChannelReceiver channelReceiver = newReceivingChannel(receivingParameters);
		Optional<MessageHandler> deliveryHandler = mySubscriptionDeliveryHandlerFactory.createDeliveryHandler(theActiveSubscription.getChannelType());

		SubscriptionChannelWithHandlers subscriptionChannelWithHandlers = new SubscriptionChannelWithHandlers(channelName, channelReceiver);
		deliveryHandler.ifPresent(subscriptionChannelWithHandlers::addHandler);
		myDeliveryReceiverChannels.put(channelName, subscriptionChannelWithHandlers);

		// create the producing channel.
		// this is the channel that will send the messages out
		// to subscribers
		ChannelRetryConfiguration retryConfigParameters = theActiveSubscription.getRetryConfigurationParameters();
		ProducingChannelParameters producingChannelParameters = new ProducingChannelParameters(channelName);
		producingChannelParameters.setRetryConfiguration(retryConfigParameters);

		IChannelProducer sendingChannel = newSendingChannel(producingChannelParameters);
		myChannelNameToSender.put(channelName, sendingChannel);
	}

	protected IChannelReceiver newReceivingChannel(String theChannelName) {
		return newReceivingChannel(new ReceivingChannelParameters(theChannelName));
	}

	protected IChannelReceiver newReceivingChannel(ReceivingChannelParameters theParameters) {
		ChannelConsumerSettings settings = new ChannelConsumerSettings();
		return mySubscriptionDeliveryChannelFactory.newDeliveryReceivingChannel(theParameters.getChannelName(),
			settings);
	}

	protected IChannelProducer newSendingChannel(ProducingChannelParameters theParameters) {
		ChannelProducerSettings settings = new ChannelProducerSettings();
		settings.setRetryConfiguration(theParameters.getRetryConfiguration());
		return mySubscriptionDeliveryChannelFactory.newDeliverySendingChannel(theParameters.getChannelName(),
			settings);
	}

	public synchronized void remove(ActiveSubscription theActiveSubscription) {
		String channelName = theActiveSubscription.getChannelName();
		ourLog.info("Removing subscription {} from channel {}", theActiveSubscription.getId(), channelName);
		boolean removed = myActiveSubscriptionByChannelName.remove(channelName, theActiveSubscription.getId());
		ChannelRetryConfiguration retryConfig = theActiveSubscription.getRetryConfigurationParameters();

		if (!removed) {
			ourLog.warn("Failed to remove subscription {} from channel {}", theActiveSubscription.getId(), channelName);
		}

		// This was the last one.  Close and remove the channel
		if (!myActiveSubscriptionByChannelName.containsKey(channelName)) {
			SubscriptionChannelWithHandlers channel = myDeliveryReceiverChannels.get(channelName);
			if (channel != null) {
				channel.close();
			}
			myDeliveryReceiverChannels.closeAndRemove(channelName);
			myChannelNameToSender.remove(channelName);
		}

	}

	public synchronized SubscriptionChannelWithHandlers getDeliveryReceiverChannel(String theChannelName) {
		return myDeliveryReceiverChannels.get(theChannelName);
	}

	public synchronized MessageChannel getDeliverySenderChannel(String theChannelName) {
		return myChannelNameToSender.get(theChannelName);
	}

	public synchronized int size() {
		return myDeliveryReceiverChannels.size();
	}
}
