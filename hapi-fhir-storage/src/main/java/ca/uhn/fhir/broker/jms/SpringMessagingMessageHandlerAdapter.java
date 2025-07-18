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
package ca.uhn.fhir.broker.jms;

import ca.uhn.fhir.broker.api.IMessageListener;
import ca.uhn.fhir.broker.api.RawStringMessage;
import ca.uhn.fhir.rest.server.messaging.IMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.GenericMessage;

/**
 * Adapt a {@link IMessageListener} to Spring Messaging (JMS)
 *
 * @param <T> the type of payload this message handler is expecting to receive
 */
public class SpringMessagingMessageHandlerAdapter<T> implements MessageHandler {
	private static final Logger ourLog = LoggerFactory.getLogger(SpringMessagingMessageHandlerAdapter.class);
	private final IMessageListener<T> myMessageListener;
	private final Class<? extends IMessage<T>> myMessageType;

	public SpringMessagingMessageHandlerAdapter(
			Class<? extends IMessage<T>> theMessageType, IMessageListener<T> theMessageListener) {
		myMessageListener = theMessageListener;
		myMessageType = theMessageType;
	}

	@Override
	public void handleMessage(Message<?> theMessage) throws MessagingException {
		IMessage<?> message;

		if (theMessage instanceof GenericMessage genericMessage) {
			// When receiving a message from an external queue, it will likely arrive as a GenericMessage
			Object payload = genericMessage.getPayload();
			if (payload instanceof byte[] bytes) {
				message = new RawStringMessage(new String(bytes), genericMessage.getHeaders());
			} else {
				message = new RawStringMessage(payload.toString(), genericMessage.getHeaders());
			}
		} else if (IMessage.class.isAssignableFrom(theMessage.getClass())) {
			message = (IMessage<T>) theMessage;
		} else {
			// Wrong message types should never happen.  If it does, we should quietly fail so it doesn't
			// clog up the channel.
			ourLog.warn(
					"Received unexpected message type. Expecting message of type {}, but received message of type {}. Skipping message.",
					IMessage.class,
					theMessage.getClass());
			return;
		}

		if (!getMessageType().isAssignableFrom(message.getClass())) {
			// Wrong message types should never happen.  If it does, we should quietly fail so it doesn't
			// clog up the channel.
			ourLog.warn(
					"Received unexpected message type. Expecting message of type {}, but received message of type {}. Skipping message.",
					getMessageType(),
					theMessage.getClass());
			return;
		}

		myMessageListener.handleMessage((IMessage<T>) message);
	}

	private Class<? extends IMessage<T>> getMessageType() {
		return myMessageType;
	}
}
