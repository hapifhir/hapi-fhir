package ca.uhn.fhir.jpa.subscription.channel.subscription;

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

import ca.uhn.fhir.jpa.subscription.channel.api.IChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelReceiver;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelProducer;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelConsumerSettings;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionConstants;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.AbstractSubscribableChannel;
import org.springframework.messaging.support.ChannelInterceptor;

public class SubscriptionChannelFactory {

	private final IChannelFactory myQueueChannelFactory;

	/**
	 * Constructor
	 */
	public SubscriptionChannelFactory(IChannelFactory theQueueChannelFactory) {
		Validate.notNull(theQueueChannelFactory);
		myQueueChannelFactory = theQueueChannelFactory;
	}

	public IChannelProducer newDeliverySendingChannel(String theChannelName, ChannelConsumerSettings theOptions) {
		ChannelConsumerSettings config = newConfigForDeliveryChannel(theOptions);
		return myQueueChannelFactory.getOrCreateProducer(theChannelName, ResourceDeliveryJsonMessage.class, config);
	}

	public IChannelReceiver newDeliveryReceivingChannel(String theChannelName, ChannelConsumerSettings theOptions) {
		ChannelConsumerSettings config = newConfigForDeliveryChannel(theOptions);
		IChannelReceiver channel = myQueueChannelFactory.getOrCreateReceiver(theChannelName, ResourceDeliveryJsonMessage.class, config);
		return new BroadcastingSubscribableChannelWrapper(channel);
	}

	public IChannelProducer newMatchingSendingChannel(String theChannelName, ChannelConsumerSettings theOptions) {
		ChannelConsumerSettings config = newConfigForMatchingChannel(theOptions);
		return myQueueChannelFactory.getOrCreateProducer(theChannelName, ResourceModifiedJsonMessage.class, config);
	}

	public IChannelReceiver newMatchingReceivingChannel(String theChannelName, ChannelConsumerSettings theOptions) {
		ChannelConsumerSettings config = newConfigForMatchingChannel(theOptions);
		IChannelReceiver channel = myQueueChannelFactory.getOrCreateReceiver(theChannelName, ResourceModifiedJsonMessage.class, config);
		return new BroadcastingSubscribableChannelWrapper(channel);
	}

	protected ChannelConsumerSettings newConfigForDeliveryChannel(ChannelConsumerSettings theOptions) {
		ChannelConsumerSettings config = new ChannelConsumerSettings();
		config.setConcurrentConsumers(getDeliveryChannelConcurrentConsumers());
		return config;
	}

	protected ChannelConsumerSettings newConfigForMatchingChannel(ChannelConsumerSettings theOptions) {
		ChannelConsumerSettings config = new ChannelConsumerSettings();
		config.setConcurrentConsumers(getMatchingChannelConcurrentConsumers());
		return config;
	}

	public int getDeliveryChannelConcurrentConsumers() {
		return SubscriptionConstants.DELIVERY_CHANNEL_CONCURRENT_CONSUMERS;
	}

	public int getMatchingChannelConcurrentConsumers() {
		return SubscriptionConstants.MATCHING_CHANNEL_CONCURRENT_CONSUMERS;
	}

	public static class BroadcastingSubscribableChannelWrapper extends AbstractSubscribableChannel implements IChannelReceiver, DisposableBean {

		private final IChannelReceiver myWrappedChannel;

		public BroadcastingSubscribableChannelWrapper(IChannelReceiver theChannel) {
			theChannel.subscribe(message -> send(message));
			myWrappedChannel = theChannel;
		}

		public SubscribableChannel getWrappedChannel() {
			return myWrappedChannel;
		}

		@Override
		protected boolean sendInternal(Message<?> theMessage, long timeout) {
			for (MessageHandler next : getSubscribers()) {
				next.handleMessage(theMessage);
			}
			return true;
		}

		@Override
		public void destroy() throws Exception {
			if (myWrappedChannel instanceof DisposableBean) {
				((DisposableBean) myWrappedChannel).destroy();
			}
		}

		@Override
		public void addInterceptor(ChannelInterceptor interceptor) {
			super.addInterceptor(interceptor);
			myWrappedChannel.addInterceptor(interceptor);
		}


	}
}
