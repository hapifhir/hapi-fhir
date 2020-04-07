package ca.uhn.fhir.jpa.subscription.channel.api;

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

/**
 * This interface is the factory for Queue Channels, which are the low level abstraction over a
 * queue (e.g. memory queue, JMS queue, Kafka stream, etc.) for any purpose.
 */
public interface IChannelFactory {

	/**
	 * Create a channel that is used to receive messages from the queue.
	 *
	 * <p>
	 * Implementations can choose to return the same object for multiple invocations of this method (and {@link #getOrCreateReceiver(String, Class, ChannelConsumerSettings)}
	 * when invoked with the same {@literal theChannelName} if they need to, or they can create a new instance.
	 * </p>
	 *
	 * @param theChannelName The actual underlying queue name
	 * @param theMessageType The object type that will be placed on this queue. Objects will be Jackson-annotated structures.
	 * @param theConfig      Contains the configuration for subscribers. Note that this parameter is provided for
	 *                       both {@link #getOrCreateReceiver} and
	 *                       {@link #getOrCreateProducer(String, Class, ChannelConsumerSettings)}
	 *                       even though this object is used to configure the sender only. We do this because the factory
	 *                       may want to create a single object to be used for both the sender and receiver, so this allows
	 *                       the config details to be known regardless of which method is returned first.
	 */
	IChannelReceiver getOrCreateReceiver(String theChannelName, Class<?> theMessageType, ChannelConsumerSettings theConfig);

	/**
	 * Create a channel that is used to send messages to the queue.
	 *
	 * <p>
	 * Implementations can choose to return the same object for multiple invocations of this method (and {@link #getOrCreateReceiver(String, Class, ChannelConsumerSettings)}
	 * when invoked with the same {@literal theChannelName} if they need to, or they can create a new instance.
	 * </p>
	 *
	 * @param theChannelName The actual underlying queue name
	 * @param theMessageType The object type that will be placed on this queue. Objects will be Jackson-annotated structures.
	 * @param theConfig      Contains the configuration for subscribers. Note that this parameter is provided for
	 *                       both {@link #getOrCreateReceiver} and
	 *                       {@link #getOrCreateProducer(String, Class, ChannelConsumerSettings)}
	 *                       even though this object is used to configure the sender only. We do this because the factory
	 *                       may want to create a single object to be used for both the sender and receiver, so this allows
	 *                       the config details to be known regardless of which method is returned first.
	 */
	IChannelProducer getOrCreateProducer(String theChannelName, Class<?> theMessageType, ChannelConsumerSettings theConfig);

}
