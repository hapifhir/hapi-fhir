package ca.uhn.fhir.jpa.subscription.module.channel;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.subscription.module.cache.ActiveSubscription;
import ca.uhn.fhir.jpa.subscription.module.cache.SubscriptionRegistry;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component
public class SubscriptionChannelRegistry {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionRegistry.class);

	private final SubscriptionChannelCache mySubscriptionChannelCache = new SubscriptionChannelCache();
	// This map is a reference count so we know to destroy the channel when there are no more active subscriptions using it
	// Key Channel Name, Value Subscription Id
	private final Multimap<String, String> myActiveSubscriptionByChannelName = MultimapBuilder.hashKeys().arrayListValues().build();

	@Autowired
	private SubscriptionDeliveryHandlerFactory mySubscriptionDeliveryHandlerFactory;
	@Autowired
	private SubscriptionChannelFactory mySubscriptionDeliveryChannelFactory;
	@Autowired
	private ModelConfig myModelConfig;

	public synchronized void add(ActiveSubscription theActiveSubscription) {
		if (!myModelConfig.isSubscriptionMatchingEnabled()) {
			return;
		}
		String channelName = theActiveSubscription.getChannelName();
		ourLog.info("Adding subscription {} to channel {}", theActiveSubscription.getId(), channelName);
		myActiveSubscriptionByChannelName.put(channelName, theActiveSubscription.getId());

		if (mySubscriptionChannelCache.containsKey(channelName)) {
			ourLog.info("Channel {} already exists.  Not creating.", channelName);
			return;
		}

		SubscribableChannel deliveryChannel;
		Optional<MessageHandler> deliveryHandler;

		deliveryChannel = mySubscriptionDeliveryChannelFactory.newDeliveryChannel(channelName);
		deliveryHandler = mySubscriptionDeliveryHandlerFactory.createDeliveryHandler(theActiveSubscription.getChannelType());

		SubscriptionChannelWithHandlers subscriptionChannelWithHandlers = new SubscriptionChannelWithHandlers(channelName, deliveryChannel);
		deliveryHandler.ifPresent(subscriptionChannelWithHandlers::addHandler);
		mySubscriptionChannelCache.put(channelName, subscriptionChannelWithHandlers);
	}

	public synchronized void remove(ActiveSubscription theActiveSubscription) {
		if (!myModelConfig.isSubscriptionMatchingEnabled()) {
			return;
		}
		String channelName = theActiveSubscription.getChannelName();
		ourLog.info("Removing subscription {} from channel {}", theActiveSubscription.getId() ,channelName);
		boolean removed = myActiveSubscriptionByChannelName.remove(channelName, theActiveSubscription.getId());
		if (!removed) {
			ourLog.warn("Failed to remove subscription {} from channel {}", theActiveSubscription.getId() ,channelName);
		}

		// This was the last one.  Close and remove the channel
		if (!myActiveSubscriptionByChannelName.containsKey(channelName)) {
			SubscriptionChannelWithHandlers channel = mySubscriptionChannelCache.get(channelName);
			if (channel != null) {
				channel.close();
			}
			mySubscriptionChannelCache.closeAndRemove(channelName);
		}
	}

	public synchronized SubscriptionChannelWithHandlers get(String theChannelName) {
		return mySubscriptionChannelCache.get(theChannelName);
	}

	public synchronized int size() {
		return mySubscriptionChannelCache.size();
	}

	@VisibleForTesting
	public void logForUnitTest() {
		ourLog.info("{} Channels: {}", this, size());
		mySubscriptionChannelCache.logForUnitTest();
		for (String key : myActiveSubscriptionByChannelName.keySet()) {
			Collection<String> list = myActiveSubscriptionByChannelName.get(key);
			for (String value : list) {
				ourLog.info("ActiveSubscriptionByChannelName {}: {}", key, value);
			}
		}
	}
}
