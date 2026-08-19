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
package ca.uhn.fhir.broker.jms;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.InterceptableChannel;

import java.util.List;
import java.util.stream.Collectors;

public interface ISpringMessagingChannelProducer extends MessageChannel, InterceptableChannel {

	/**
	 * Send a batch of messages to this channel.
	 * <p>
	 * The default implementation simply sends each message in turn, which preserves the semantics of
	 * {@link MessageChannel#send(Message)} exactly. Implementations backed by a broker that can amortize the cost of
	 * synchronizing a batch (for example a single transacted JMS session) are expected to override this method so that
	 * the whole batch costs one round trip instead of one round trip per message.
	 * </p>
	 *
	 * @param theMessages the messages to send, in the order in which they should be sent
	 * @return whether each message was sent, in the same order as <code>theMessages</code>
	 * @since 8.12.0
	 */
	default List<Boolean> sendAll(List<Message<?>> theMessages) {
		return theMessages.stream().map(this::send).collect(Collectors.toList());
	}
}
