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

import ca.uhn.fhir.broker.api.ChannelConsumerStartFailureException;
import ca.uhn.fhir.broker.api.IChannelConsumer;
import ca.uhn.fhir.broker.api.IMessageListener;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.messaging.IMessage;
import ca.uhn.fhir.util.IoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.messaging.MessageHandler;

/**
 * Adapt a Spring Messaging (JMS) Queue to {@link IChannelConsumer}
 *
 * @param <T> the type of payload this message consumer is expecting to receive
 */
public class SpringMessagingReceiverAdapter<T> implements IChannelConsumer<T> {
	private static final Logger ourLog = LoggerFactory.getLogger(SpringMessagingReceiverAdapter.class);
	private final Class<? extends IMessage<T>> myMessageType;
	private final ISpringMessagingChannelReceiver mySpringMessagingChannelReceiver;
	private final IMessageListener<T> myMessageListener;
	private MessageHandler myMessageHandler;
	private boolean myClosed;

	public SpringMessagingReceiverAdapter(
			Class<? extends IMessage<T>> theMessageType,
			ISpringMessagingChannelReceiver theSpringMessagingChannelReceiver,
			IMessageListener<T> theMessageListener) {
		myMessageType = theMessageType;
		mySpringMessagingChannelReceiver = theSpringMessagingChannelReceiver;
		myMessageListener = theMessageListener;
	}

	public void subscribe(MessageHandler theMessageHandler) {
		checkState();
		if (myMessageHandler != null) {
			throw new IllegalArgumentException("Only one subscriber allowed");
		}
		myMessageHandler = theMessageHandler;
		mySpringMessagingChannelReceiver.subscribe(theMessageHandler);
	}

	@Override
	public void close() {
		myClosed = true;
		if (myMessageHandler != null) {
			mySpringMessagingChannelReceiver.unsubscribe(myMessageHandler);
			closeAndDestroyQuietly(myMessageHandler);
		}
		destroyQuietly(mySpringMessagingChannelReceiver);
		closeQuietly(myMessageListener);
	}

	private void closeAndDestroyQuietly(MessageHandler theMessageHandler) {
		if (theMessageHandler instanceof AutoCloseable) {
			IoUtils.closeQuietly((AutoCloseable) theMessageHandler, ourLog);
		}
		if (theMessageHandler instanceof DisposableBean) {
			try {
				((DisposableBean)theMessageHandler).destroy();
			} catch (Exception e) {
				throw new InternalErrorException("Failed to destroy MessageHandler", e);
			}
		}
	}

	private void destroyQuietly(ISpringMessagingChannelReceiver theSpringMessagingChannelReceiver) {
		try {
			theSpringMessagingChannelReceiver.destroy();
		} catch (Exception e) {
			ourLog.error("Error destroying Spring Messaging ChannelReceiver", e);
		}
	}

	private void closeQuietly(IMessageListener<T> theMessageListener) {
		if (theMessageListener instanceof AutoCloseable) {
			IoUtils.closeQuietly((AutoCloseable) theMessageListener, ourLog);
		}
	}

	@Override
	public boolean isClosed() {
		return myClosed;
	}

	@Override
	public String getChannelName() {
		return mySpringMessagingChannelReceiver.getChannelName();
	}

	@Override
	public void start() throws ChannelConsumerStartFailureException {
		checkState();
		mySpringMessagingChannelReceiver.start();
	}

	public ISpringMessagingChannelReceiver getSpringMessagingChannelReceiver() {
		return mySpringMessagingChannelReceiver;
	}

	@Override
	public Class<? extends IMessage<T>> getMessageType() {
		return myMessageType;
	}

	@Override
	public IMessageListener<T> getMessageListener() {
		return myMessageListener;
	}

	@Override
	public void pause() {
		mySpringMessagingChannelReceiver.pause();
	}

	@Override
	public void resume() {
		checkState();
		mySpringMessagingChannelReceiver.resume();
	}
}
