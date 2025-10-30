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

import ca.uhn.fhir.broker.api.IChannelProducer;
import ca.uhn.fhir.broker.api.ISendResult;
import ca.uhn.fhir.broker.impl.SpringMessagingSendResult;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.server.messaging.IMessage;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.ChannelInterceptor;

/**
 * Adapt a Spring Messaging (JMS) Queue to {@link IChannelProducer}
 *
 * @param <T> the type of payload this message producer is expecting to send
 */
public class SpringMessagingProducerAdapter<T> implements IChannelProducer<T> {
	private final Class<? extends IMessage<T>> myMessageType;
	private final ISpringMessagingChannelProducer mySpringMessagingChannelProducer;

	public SpringMessagingProducerAdapter(
			Class<? extends IMessage<T>> theMessageType,
			ISpringMessagingChannelProducer theSpringMessagingChannelProducer) {
		myMessageType = theMessageType;
		mySpringMessagingChannelProducer = theSpringMessagingChannelProducer;
	}

	@Override
	public String getChannelName() {
		return "unknown legacy channel name";
	}

	@Override
	public ISendResult send(IMessage<T> theMessage) {
		if (!myMessageType.isAssignableFrom(theMessage.getClass())) {
			throw new ConfigurationException(Msg.code(2665) + "Expecting message of type " + myMessageType
					+ ". But received message of type: " + theMessage.getClass());
		}

		if (!Message.class.isAssignableFrom(theMessage.getClass())) {
			throw new ConfigurationException(Msg.code(2664) + "Expecting message of type " + Message.class
					+ ". But received message of type: " + theMessage.getClass());
		}
		Message<?> message = (Message<?>) theMessage;

		return new SpringMessagingSendResult(mySpringMessagingChannelProducer.send(message));
	}

	public void addInterceptor(ChannelInterceptor theInterceptor) {
		mySpringMessagingChannelProducer.addInterceptor(theInterceptor);
	}

	public ISpringMessagingChannelProducer getSpringMessagingProducer() {
		return mySpringMessagingChannelProducer;
	}

	public Class<? extends IMessage<T>> getMessageType() {
		return myMessageType;
	}
}
