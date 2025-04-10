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
package ca.uhn.fhir.broker.api;

import ca.uhn.fhir.rest.server.messaging.IMessage;

public interface IBrokerClient {
	/**
	 * Create a channel that is used to receive messages from the queue.
	 *
	 * <p>
	 * Implementations can choose to return the same object for multiple invocations of this method
	 * when invoked with the same {@literal theChannelName} if they need to, or they can create a new instance.
	 * </p>
	 *
	 * @param theChannelName             The actual underlying queue name
	 * @param theMessageType             The object type that will be placed on this queue. Objects will usually be Jackson-annotated structures.
	 * @param theChannelConsumerSettings Contains the configuration for subscribers.
	 */
	<T> IChannelConsumer<T> getOrCreateConsumer(
			String theChannelName,
			Class<? extends IMessage<T>> theMessageType,
			IMessageListener<T> theMessageListener,
			ChannelConsumerSettings theChannelConsumerSettings);

	/**
	 * Create a channel that is used to send messages to the queue.
	 *
	 * <p>
	 * Implementations can choose to return the same object for multiple invocations of this method
	 * when invoked with the same {@literal theChannelName} if they need to, or they can create a new instance.
	 * </p>
	 *
	 * @param theChannelName             The actual underlying queue name
	 * @param theMessageType             The object type that will be placed on this queue. Objects will be Jackson-annotated structures.
	 * @param theChannelProducerSettings Contains the configuration for senders.
	 */
	<T> IChannelProducer<T> getOrCreateProducer(
			String theChannelName,
			Class<? extends IMessage<T>> theMessageType,
			ChannelProducerSettings theChannelProducerSettings);

	/**
	 * @return the IChannelNamer used by this factory
	 */
	IChannelNamer getChannelNamer();
}
