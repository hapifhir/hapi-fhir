package ca.uhn.fhir.jpa.subscription.channel.subscription;

/*-
 * #%L
 * HAPI FHIR Subscription Server
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

import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import com.google.common.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;

import java.io.Closeable;
import java.util.Collection;
import java.util.HashSet;

public class SubscriptionChannelWithHandlers implements Closeable {
	private static final Logger ourLog = LoggerFactory.getLogger(ActiveSubscription.class);

	private final String myChannelName;
	private final SubscribableChannel mySubscribableChannel;
	private final Collection<MessageHandler> myDeliveryHandlerSet = new HashSet<>();

	public SubscriptionChannelWithHandlers(String theChannelName, SubscribableChannel theSubscribableChannel) {
		myChannelName = theChannelName;
		mySubscribableChannel = theSubscribableChannel;
	}

	public void addHandler(MessageHandler theHandler) {
		mySubscribableChannel.subscribe(theHandler);
		myDeliveryHandlerSet.add(theHandler);
	}

	public void removeHandler(MessageHandler theMessageHandler) {
		if (mySubscribableChannel != null) {
			mySubscribableChannel.unsubscribe(theMessageHandler);
		}
		if (theMessageHandler instanceof DisposableBean) {
			try {
				((DisposableBean) theMessageHandler).destroy();
			} catch (Exception e) {
				ourLog.warn("Could not destroy {} handler for {}", theMessageHandler.getClass().getSimpleName(), myChannelName, e);
			}
		}
	}

	@VisibleForTesting
	public MessageHandler getDeliveryHandlerForUnitTest() {
		return myDeliveryHandlerSet.iterator().next();
	}

	@Override
	public void close() {
		for (MessageHandler messageHandler : myDeliveryHandlerSet) {
			removeHandler(messageHandler);
		}
		if (mySubscribableChannel instanceof DisposableBean) {
			tryDestroyChannel((DisposableBean) mySubscribableChannel);
		}
	}

	private void tryDestroyChannel(DisposableBean theSubscribableChannel) {
		try {
			ourLog.info("Destroying channel {}", myChannelName);
			theSubscribableChannel.destroy();
		} catch (Exception e) {
			ourLog.error("Failed to destroy channel bean", e);
		}
	}

	public MessageChannel getChannel() {
		return mySubscribableChannel;
	}
}
