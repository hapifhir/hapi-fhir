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

import ca.uhn.fhir.jpa.subscription.channel.queue.IQueueChannelFactory;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.subscription.process.registry.SubscriptionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.AbstractSubscribableChannel;

public class SubscriptionChannelFactory {

	@Autowired
	private IQueueChannelFactory mySubscribableChannelFactory;

	public SubscribableChannel newDeliveryChannel(String theChannelName) {
		SubscribableChannel channel = mySubscribableChannelFactory.getOrCreateReceiver(theChannelName, ResourceDeliveryMessage.class, getDeliveryChannelConcurrentConsumers());
		return new BroadcastingSubscribableChannelWrapper(channel);
	}

	public MessageChannel newMatchingSendingChannel(String theChannelName) {
		return mySubscribableChannelFactory.getOrCreateSender(theChannelName, ResourceModifiedMessage.class, getMatchingChannelConcurrentConsumers());
	}

	public SubscribableChannel newMatchingReceivingChannel(String theChannelName) {
		SubscribableChannel channel = mySubscribableChannelFactory.getOrCreateReceiver(theChannelName, ResourceModifiedMessage.class, getMatchingChannelConcurrentConsumers());
		return new BroadcastingSubscribableChannelWrapper(channel);
	}

	public int getDeliveryChannelConcurrentConsumers() {
		return SubscriptionConstants.DELIVERY_CHANNEL_CONCURRENT_CONSUMERS;
	}

	public int getMatchingChannelConcurrentConsumers() {
		return SubscriptionConstants.MATCHING_CHANNEL_CONCURRENT_CONSUMERS;
	}


	private static class BroadcastingSubscribableChannelWrapper extends AbstractSubscribableChannel implements MessageHandler {

		public BroadcastingSubscribableChannelWrapper(SubscribableChannel theChannel) {
			theChannel.subscribe(this);
		}


		@Override
		protected boolean sendInternal(Message<?> theMessage, long timeout) {
			for (MessageHandler next : getSubscribers()) {
				next.handleMessage(theMessage);
			}
			return true;
		}

		@Override
		public void handleMessage(Message<?> message) throws MessagingException {
			send(message);
		}
	}


}
