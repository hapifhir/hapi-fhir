/*-
 * #%L
 * HAPI FHIR Storage api
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
import ca.uhn.fhir.jpa.subscription.channel.api.ILegacyChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.api.ILegacyChannelProducer;
import ca.uhn.fhir.jpa.subscription.channel.api.ILegacyChannelReceiver;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.subscription.SubscriptionConstants;
import org.apache.commons.lang3.Validate;

public class SubscriptionChannelFactory {
	private final ILegacyChannelFactory myChannelFactory;

	/**
	 * Constructor
	 */
	public SubscriptionChannelFactory(ILegacyChannelFactory theChannelFactory) {
		Validate.notNull(theChannelFactory);
		myChannelFactory = theChannelFactory;
	}

	public ILegacyChannelProducer newDeliverySendingChannel(
			String theChannelName, ChannelProducerSettings theChannelSettings) {
		ChannelProducerSettings config = newProducerConfigForDeliveryChannel(theChannelSettings);
		config.setRetryConfiguration(theChannelSettings.getRetryConfigurationParameters());
		return myChannelFactory.getOrCreateProducer(theChannelName, ResourceDeliveryJsonMessage.class, config);
	}

	public ILegacyChannelReceiver newDeliveryReceivingChannel(
			String theChannelName, ChannelConsumerSettings theChannelSettings) {
		ChannelConsumerSettings config = newConsumerConfigForDeliveryChannel(theChannelSettings);
		ILegacyChannelReceiver channel =
				myChannelFactory.getOrCreateReceiver(theChannelName, ResourceDeliveryJsonMessage.class, config);
		return new BroadcastingSubscribableChannelWrapper(channel);
	}

	public ILegacyChannelProducer newMatchingSendingChannel(
			String theChannelName, ChannelProducerSettings theChannelSettings) {
		ChannelProducerSettings config = newProducerConfigForMatchingChannel(theChannelSettings);
		return myChannelFactory.getOrCreateProducer(theChannelName, ResourceModifiedJsonMessage.class, config);
	}

	public ILegacyChannelReceiver newMatchingReceivingChannel(
			String theChannelName, ChannelConsumerSettings theChannelSettings) {
		ChannelConsumerSettings config = newConsumerConfigForMatchingChannel(theChannelSettings);
		ILegacyChannelReceiver channel =
				myChannelFactory.getOrCreateReceiver(theChannelName, ResourceModifiedJsonMessage.class, config);
		return new BroadcastingSubscribableChannelWrapper(channel);
	}

	protected ChannelProducerSettings newProducerConfigForDeliveryChannel(ChannelProducerSettings theOptions) {
		ChannelProducerSettings config = new ChannelProducerSettings();
		config.setConcurrentConsumers(getDeliveryChannelConcurrentConsumers());
		config.setRetryConfiguration(theOptions.getRetryConfigurationParameters());
		return config;
	}

	protected ChannelConsumerSettings newConsumerConfigForDeliveryChannel(ChannelConsumerSettings theOptions) {
		ChannelConsumerSettings config = new ChannelConsumerSettings();
		config.setConcurrentConsumers(getDeliveryChannelConcurrentConsumers());
		if (theOptions != null) {
			config.setRetryConfiguration(theOptions.getRetryConfigurationParameters());
		}
		return config;
	}

	protected ChannelProducerSettings newProducerConfigForMatchingChannel(ChannelProducerSettings theOptions) {
		ChannelProducerSettings config = new ChannelProducerSettings();
		if (theOptions != null) {
			config.setRetryConfiguration(theOptions.getRetryConfigurationParameters());
			config.setQualifyChannelName(theOptions.isQualifyChannelName());
		}
		config.setConcurrentConsumers(getMatchingChannelConcurrentConsumers());
		return config;
	}

	protected ChannelConsumerSettings newConsumerConfigForMatchingChannel(ChannelConsumerSettings theOptions) {
		ChannelConsumerSettings config = new ChannelConsumerSettings();
		config.setConcurrentConsumers(getMatchingChannelConcurrentConsumers());
		if (theOptions != null) {
			config.setQualifyChannelName(theOptions.isQualifyChannelName());
			config.setRetryConfiguration(theOptions.getRetryConfigurationParameters());
		}
		return config;
	}

	public int getDeliveryChannelConcurrentConsumers() {
		return SubscriptionConstants.DELIVERY_CHANNEL_CONCURRENT_CONSUMERS;
	}

	public int getMatchingChannelConcurrentConsumers() {
		return SubscriptionConstants.MATCHING_CHANNEL_CONCURRENT_CONSUMERS;
	}

	public ILegacyChannelFactory getChannelFactory() {
		return myChannelFactory;
	}
}
