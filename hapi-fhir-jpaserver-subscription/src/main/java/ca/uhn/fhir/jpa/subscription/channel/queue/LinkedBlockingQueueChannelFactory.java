package ca.uhn.fhir.jpa.subscription.channel.queue;

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

import ca.uhn.fhir.jpa.subscription.process.registry.SubscriptionConstants;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public class LinkedBlockingQueueChannelFactory implements IQueueChannelFactory {

	private Map<String, SubscribableChannel> myChannels = Collections.synchronizedMap(new HashMap<>());

	@Override
	public SubscribableChannel getOrCreateReceiver(String theChannelName, Class<?> theMessageType, int theConcurrentConsumers) {
		return getOrCreateChannel(theChannelName, theConcurrentConsumers);
	}

	@Override
	public MessageChannel getOrCreateSender(String theChannelName, Class<?> theMessageType, int theConcurrentConsumers) {
		return getOrCreateChannel(theChannelName, theConcurrentConsumers);
	}

	private SubscribableChannel getOrCreateChannel(String theChannelName, int theConcurrentConsumers) {
		return myChannels.computeIfAbsent(theChannelName, t ->
			new LinkedBlockingQueueChannel(new LinkedBlockingQueue<>(SubscriptionConstants.DELIVERY_EXECUTOR_QUEUE_SIZE), theChannelName + "-%d", theConcurrentConsumers));
	}

	@Override
	public int getDeliveryChannelConcurrentConsumers() {
		return SubscriptionConstants.DELIVERY_CHANNEL_CONCURRENT_CONSUMERS;
	}

	@Override
	public int getMatchingChannelConcurrentConsumers() {
		return SubscriptionConstants.MATCHING_CHANNEL_CONCURRENT_CONSUMERS;
	}
}
