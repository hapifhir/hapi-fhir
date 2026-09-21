/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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

import java.util.List;
import java.util.stream.Collectors;

/**
 * Sends messages to a Message Broker.
 *
 * @param <T> The type of messages sent by this producer
 */
public interface IChannelProducer<T> {

	/**
	 * @return the name of the topic or queue that this producer is sending messages to
	 */
	String getChannelName();

	/**
	 * Send a message to the broker.
	 *
	 * @param theMessage the message to send
	 * @return the result of the send operation
	 */
	ISendResult send(IMessage<T> theMessage);

	/**
	 * Send a batch of messages to the broker.
	 * <p>
	 * The default implementation simply sends each message in turn, which preserves the semantics of
	 * {@link #send(IMessage)} exactly. Implementations backed by a broker that can amortize the cost of
	 * synchronizing a batch (for example a single transacted JMS session, or a single flush) are expected to
	 * override this method.
	 * </p>
	 *
	 * @param theMessages the messages to send, in the order in which they should be sent
	 * @return the result of each send operation, in the same order as <code>theMessages</code>
	 * @since 8.12.0
	 */
	default List<ISendResult> sendAll(List<IMessage<T>> theMessages) {
		return theMessages.stream().map(this::send).collect(Collectors.toList());
	}
}
