package ca.uhn.fhir.jpa.subscription.channel.subscription;

/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.subscription.channel.api.IChannelReceiver;
import org.apache.commons.lang3.Validate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.AbstractSubscribableChannel;
import org.springframework.messaging.support.ChannelInterceptor;

import java.util.Set;

public class BroadcastingSubscribableChannelWrapper extends AbstractSubscribableChannel implements IChannelReceiver {

	private final IChannelReceiver myWrappedChannel;
	private final MessageHandler myHandler;

	public BroadcastingSubscribableChannelWrapper(IChannelReceiver theChannel) {
		myHandler = message -> send(message);
		theChannel.subscribe(myHandler);
		myWrappedChannel = theChannel;
	}

	public SubscribableChannel getWrappedChannel() {
		return myWrappedChannel;
	}

	@Override
	protected boolean sendInternal(Message<?> theMessage, long timeout) {
		Set<MessageHandler> subscribers = getSubscribers();
		Validate.isTrue(subscribers.size() > 0, "Channel has zero subscribers");
		for (MessageHandler next : subscribers) {
			next.handleMessage(theMessage);
		}
		return true;
	}

	@Override
	public void destroy() throws Exception {
		myWrappedChannel.destroy();
		myWrappedChannel.unsubscribe(myHandler);
	}

	@Override
	public void addInterceptor(ChannelInterceptor interceptor) {
		super.addInterceptor(interceptor);
		myWrappedChannel.addInterceptor(interceptor);
	}

	@Override
	public String getName() {
		return myWrappedChannel.getName();
	}
}
