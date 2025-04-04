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
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.messaging.IMessage;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

public class SpringMessagingMessageHandlerAdapter<T> implements MessageHandler {
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
			throw new InternalErrorException("Expecting message of type " + IMessage.class
					+ ". But received message of type: " + theMessage.getClass());
		}

		if (!getMessageType().isAssignableFrom(theMessage.getClass())) {
			throw new InternalErrorException("Expecting message payload of type " + getMessageType()
					+ ". But received message of type: " + theMessage.getClass());
		}

		IMessage<T> message = (IMessage<T>) theMessage;
		myMessageListener.handleMessage(message);
	}

	private Class<? extends IMessage<T>> getMessageType() {
		return myMessageType;
	}
}
