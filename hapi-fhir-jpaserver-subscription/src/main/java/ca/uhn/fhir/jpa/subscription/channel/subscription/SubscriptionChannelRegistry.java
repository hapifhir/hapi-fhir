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
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelProducer;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelReceiver;
import ca.uhn.fhir.jpa.subscription.channel.models.ReceivingChannelParameters;
import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionRegistry;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscriptionChannelType;
import ca.uhn.fhir.jpa.subscription.model.ChannelRetryConfiguration;
import ca.uhn.fhir.util.StringUtil;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import org.apache.commons.codec.binary.StringUtils;
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

		ChannelRetryConfiguration retryConfigParameters = theActiveSubscription.getRetryConfigurationParameters();
		if (retryConfigParameters != null
				&& retryConfigParameters.getDeadLetterQueueName() != null
				&& !retryConfigParameters.getDeadLetterQueueName().trim().equals("")) {
			// create a dlq
			String name = retryConfigParameters.getDeadLetterQueueName();
			createDeadLetterQueue(name);
		}
		ReceivingChannelParameters parameters = new ReceivingChannelParameters(channelName);
		parameters.setRetryConfiguration(retryConfigParameters);

		IChannelReceiver channelReceiver = newReceivingChannel(parameters);
		Optional<MessageHandler> deliveryHandler = mySubscriptionDeliveryHandlerFactory.createDeliveryHandler(theActiveSubscription.getChannelType());

		SubscriptionChannelWithHandlers subscriptionChannelWithHandlers = new SubscriptionChannelWithHandlers(channelName, channelReceiver);
		deliveryHandler.ifPresent(subscriptionChannelWithHandlers::addHandler);
		myDeliveryReceiverChannels.put(channelName, subscriptionChannelWithHandlers);

		IChannelProducer sendingChannel = newSendingChannel(channelName);
		myChannelNameToSender.put(channelName, sendingChannel);
	}

	private void createDeadLetterQueue(String theDlqName) {
		ReceivingChannelParameters dlqParams = new ReceivingChannelParameters(theDlqName);
		IChannelReceiver dlq = newReceivingChannel(dlqParams);

		SubscriptionChannelWithHandlers dlqWithHandlers = new SubscriptionChannelWithHandlers(theDlqName, dlq);
		Optional<MessageHandler> dlqHandler = mySubscriptionDeliveryHandlerFactory.createDeliveryHandler(CanonicalSubscriptionChannelType.MESSAGE);

		dlqHandler.ifPresent(dlqWithHandlers::addHandler);
		myDeliveryReceiverChannels.put(theDlqName, dlqWithHandlers);
	}

	protected IChannelReceiver newReceivingChannel(String theChannelName) {
		return newReceivingChannel(new ReceivingChannelParameters(theChannelName));
	}

	protected IChannelReceiver newReceivingChannel(ReceivingChannelParameters theParameters) {
		ChannelConsumerSettings settings = new ChannelConsumerSettings();
		settings.setRetryConfiguration(theParameters.getRetryConfiguration());
		return mySubscriptionDeliveryChannelFactory.newDeliveryReceivingChannel(theParameters.getChannelName(),
			settings);
	}

	protected IChannelProducer newSendingChannel(String theChannelName) {
		return mySubscriptionDeliveryChannelFactory.newDeliverySendingChannel(theChannelName, null);
	}

	public synchronized void remove(ActiveSubscription theActiveSubscription) {
		String channelName = theActiveSubscription.getChannelName();
		ourLog.info("Removing subscription {} from channel {}", theActiveSubscription.getId(), channelName);
		boolean removed = myActiveSubscriptionByChannelName.remove(channelName, theActiveSubscription.getId());
		ChannelRetryConfiguration retryConfig = theActiveSubscription.getRetryConfigurationParameters();

		// if there are listening DLQs, we'll close them too
		if (removed && retryConfig != null
				&& retryConfig.hasDeadLetterQueue()) {
			myDeliveryReceiverChannels.closeAndRemove(retryConfig.getDeadLetterQueueName());
		}

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
