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
import ca.uhn.fhir.rest.server.messaging.IMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

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
		if (!IMessage.class.isAssignableFrom(theMessage.getClass())) {
			// Wrong message types should never happen.  If it does, we should quietly fail so it doesn't
			// clog up the channel.
			ourLog.warn(
					"Received unexpected message type. Expecting message of type {}, but received message of type {}. Skipping message.",
					IMessage.class,
					theMessage.getClass());
			return;
		}

		if (!getMessageType().isAssignableFrom(theMessage.getClass())) {
			// Wrong message types should never happen.  If it does, we should quietly fail so it doesn't
			// clog up the channel.
			ourLog.warn(
					"Received unexpected message type. Expecting message of type {}, but received message of type {}. Skipping message.",
					getMessageType(),
					theMessage.getClass());
			return;
		}

		IMessage<T> message = (IMessage<T>) theMessage;
		myMessageListener.handleMessage(message);
	}

	private Class<? extends IMessage<T>> getMessageType() {
		return myMessageType;
	}
}
