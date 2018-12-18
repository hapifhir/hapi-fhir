package ca.uhn.fhir.jpa.subscription.module;

import ca.uhn.fhir.jpa.subscription.module.cache.SubscriptionConstants;

import java.util.concurrent.LinkedBlockingQueue;

public class LinkedBlockingQueueSubscriptionChannel extends SubscriptionChannel {

	public LinkedBlockingQueueSubscriptionChannel(String theThreadNamingPattern) {
		super(new LinkedBlockingQueue<>(SubscriptionConstants.DELIVERY_EXECUTOR_QUEUE_SIZE), theThreadNamingPattern);
	}
}
