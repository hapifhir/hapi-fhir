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

/**
 * IBrokerClient implementations communicate with Message Brokers to exchange messages. HAPI-FHIR uses a Message Broker for asynchronous
 * processing tasks such as Batch Jobs, Subscription Processing, and MDM. HAPI-FHIR uses the term "Channel" to represent a Queue (JMS)
 * or Topic (Kafka etc.) Services that send messages create a {@link IChannelProducer}. Services that
 * receive messages from a channel create a {@link IChannelConsumer}. Each {@link IChannelConsumer} is created with a single
 * {@link IMessageListener} that defines how messages are handled. A {@link IChannelConsumer} creates threads that are notified by the broker
 * when new messages arrive so {@link IChannelConsumer} instances need to be properly closed when shutting down.
 */
public interface IBrokerClient {
	/**
	 * Create a consumer that receives messages from the channel.
	 *
	 * <p>
	 * Implementations can choose to return the same object for multiple invocations of this method
	 * when invoked with the same {@literal theChannelName} if they need to, or they can create a new instance.
	 * </p>
	 *
	 * @param theChannelName             The actual underlying channel name
	 * @param theMessageType             The object type that will be placed on this chanel. Objects will usually be a Jackson-annotated class.
	 * @param theMessageListener		 The message handler that will be called for each arriving message. If more than one message listeners is required for a single consumer, {@link ca.uhn.fhir.broker.impl.MultiplexingListener} can be used for the listener.
	 * @param theChannelConsumerSettings Defines the consumer configuration (e.g. number of listening threads)
	 */
	<T> IChannelConsumer<T> getOrCreateConsumer(
			String theChannelName,
			Class<? extends IMessage<T>> theMessageType,
			IMessageListener<T> theMessageListener,
			ChannelConsumerSettings theChannelConsumerSettings);

	/**
	 * Create a producer that is used to send messages to the channel.
	 *
	 * <p>
	 * Implementations can choose to return the same object for multiple invocations of this method
	 * when invoked with the same {@literal theChannelName} if they need to, or they can create a new instance.
	 * </p>
	 *
	 * @param theChannelName             The actual underlying channel name
	 * @param theMessageType             The object type that will be placed on this channel. Objects will be Jackson-annotated structures.
	 * @param theChannelProducerSettings Contains the configuration for senders.
	 */
	<T> IChannelProducer<T> getOrCreateProducer(
			String theChannelName,
			Class<? extends IMessage<T>> theMessageType,
			ChannelProducerSettings theChannelProducerSettings);

	/**
	 * @return the IChannelNamer used by this broker client.
	 */
	IChannelNamer getChannelNamer();
}
