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
package ca.uhn.fhir.broker.impl;

import ca.uhn.fhir.broker.api.ChannelConsumerSettings;
import ca.uhn.fhir.broker.api.ChannelProducerSettings;
import ca.uhn.fhir.broker.api.IBrokerClient;
import ca.uhn.fhir.broker.api.IChannelConsumer;
import ca.uhn.fhir.broker.api.IChannelNamer;
import ca.uhn.fhir.broker.api.IChannelProducer;
import ca.uhn.fhir.broker.api.IMessageListener;
import ca.uhn.fhir.broker.jms.SpringMessagingMessageHandlerAdapter;
import ca.uhn.fhir.broker.jms.SpringMessagingProducerAdapter;
import ca.uhn.fhir.broker.jms.SpringMessagingReceiverAdapter;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannel;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannelFactory;
import ca.uhn.fhir.rest.server.messaging.IMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHandler;

public class LinkedBlockingBrokerClient implements IBrokerClient {
	private LinkedBlockingChannelFactory myLinkedBlockingChannelFactory;
	private final IChannelNamer myChannelNamer;

	public LinkedBlockingBrokerClient(IChannelNamer theChannelNamer) {
		myChannelNamer = theChannelNamer;
	}

	@Override
	public <T> IChannelConsumer<T> getOrCreateConsumer(
			String theChannelName,
			Class<? extends IMessage<T>> theMessageType,
			IMessageListener<T> theMessageListener,
			ChannelConsumerSettings theChannelConsumerSettings) {
		LinkedBlockingChannel springMessagingChannelReceiver =
				myLinkedBlockingChannelFactory.getOrCreateReceiver(theChannelName, theChannelConsumerSettings);
		SpringMessagingReceiverAdapter<T> retval =
				new SpringMessagingReceiverAdapter<>(theMessageType, springMessagingChannelReceiver, theMessageListener);
		MessageHandler handler = new SpringMessagingMessageHandlerAdapter<>(theMessageType, theMessageListener);
		retval.subscribe(handler);
		return retval;
	}

	@Override
	public <T> IChannelProducer<T> getOrCreateProducer(
			String theChannelName,
			Class<? extends IMessage<T>> theMessageType,
			ChannelProducerSettings theChannelProducerSettings) {
		return new SpringMessagingProducerAdapter<>(
				theMessageType,
				myLinkedBlockingChannelFactory.getOrCreateProducer(theChannelName, theChannelProducerSettings));
	}

	@Override
	public IChannelNamer getChannelNamer() {
		return myChannelNamer;
	}

	@Autowired
	public void setLinkedBlockingChannelFactory(LinkedBlockingChannelFactory theLinkedBlockingChannelFactory) {
		myLinkedBlockingChannelFactory = theLinkedBlockingChannelFactory;
	}
}
