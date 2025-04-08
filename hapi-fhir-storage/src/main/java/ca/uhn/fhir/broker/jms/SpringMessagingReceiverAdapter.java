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

import ca.uhn.fhir.broker.api.IChannelConsumer;
import ca.uhn.fhir.broker.api.IMessageListener;
import ca.uhn.fhir.broker.util.CloseUtil;
import ca.uhn.fhir.rest.server.messaging.IMessage;
import org.springframework.messaging.MessageHandler;

public class SpringMessagingReceiverAdapter<T> implements IChannelConsumer<T> {
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
			CloseUtil.close(myMessageHandler);
		}
		CloseUtil.close(mySpringMessagingChannelReceiver);
		CloseUtil.close(myMessageListener);
	}

	@Override
	public boolean isClosed() {
		return myClosed;
	}

	@Override
	public String getChannelName() {
		return mySpringMessagingChannelReceiver.getName();
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
		mySpringMessagingChannelReceiver.stop();
	}

	@Override
	public void resume() {
		checkState();
		mySpringMessagingChannelReceiver.start();
	}
}
