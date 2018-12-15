package ca.uhn.fhir.jpa.subscription.module;

import ca.uhn.fhir.jpa.subscription.module.cache.SubscriptionConstants;

import java.util.concurrent.LinkedBlockingQueue;

public class SubscriptionChannelLinkedBlockingQueue extends SubscriptionChannel {

	public SubscriptionChannelLinkedBlockingQueue(String theThreadNamingPattern) {
		super(new LinkedBlockingQueue<>(SubscriptionConstants.DELIVERY_EXECUTOR_QUEUE_SIZE), theThreadNamingPattern);
	}
}
