package ca.uhn.fhir.jpa.subscription.module.cache;

import ca.uhn.fhir.jpa.subscription.module.LinkedBlockingQueueSubscribableChannel;
import org.springframework.messaging.SubscribableChannel;

import java.util.concurrent.LinkedBlockingQueue;

public class LinkedBlockingQueueSubscribableChannelFactory implements ISubscribableChannelFactory {
	@Override
	public SubscribableChannel createSubscribableChannel(String theThreadPattern) {
		return new LinkedBlockingQueueSubscribableChannel(new LinkedBlockingQueue<>(SubscriptionConstants.DELIVERY_EXECUTOR_QUEUE_SIZE), theThreadPattern);
	}
}
