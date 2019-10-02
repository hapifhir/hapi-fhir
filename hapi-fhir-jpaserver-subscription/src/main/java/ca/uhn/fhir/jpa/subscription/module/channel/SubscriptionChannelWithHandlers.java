package ca.uhn.fhir.jpa.subscription.module.channel;

import ca.uhn.fhir.jpa.subscription.module.cache.ActiveSubscription;
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

	public String getChannelName() {
		return myChannelName;
	}
}
