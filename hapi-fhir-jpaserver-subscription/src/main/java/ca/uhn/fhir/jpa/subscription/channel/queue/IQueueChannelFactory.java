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

import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;


/**
 * This interface is the factory for Queue Channels, which are the low level abstraction over a
 * queue (e.g. memory queue, JMS queue, Kafka stream, etc.) for any purpose.
 */
public interface IQueueChannelFactory {

	/**
	 * Create a channel that is used to receive messages from the queue.
	 *
	 * <p>
	 * Implementations can choose to return the same object for multiple invocations of this method (and {@link #getOrCreateReceiver(String, Class, int)}
	 * when invoked with the same {@literal theChannelName} if they need to, or they can create a new instance.
	 * </p>
	 */
	SubscribableChannel getOrCreateReceiver(String theChannelName, Class<?> theMessageType, int theConcurrentConsumers);

	/**
	 * Create a channel that is used to send messages to the queue.
	 *
	 * <p>
	 * Implementations can choose to return the same object for multiple invocations of this method (and {@link #getOrCreateReceiver(String, Class, int)}
	 * when invoked with the same {@literal theChannelName} if they need to, or they can create a new instance.
	 * </p>
	 */
	MessageChannel getOrCreateSender(String theChannelName, Class<?> theMessageType, int theConcurrentConsumers);

}
