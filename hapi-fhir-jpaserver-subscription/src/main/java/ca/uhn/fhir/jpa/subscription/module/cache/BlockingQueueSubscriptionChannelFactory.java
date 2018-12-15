package ca.uhn.fhir.jpa.subscription.module.cache;

import ca.uhn.fhir.jpa.subscription.module.SubscriptionChannel;
import ca.uhn.fhir.jpa.subscription.module.LinkedBlockingQueueSubscriptionChannel;

public class BlockingQueueSubscriptionChannelFactory implements ISubscriptionChannelFactory {

	@Override
	public SubscriptionChannel newDeliveryChannel(String theSubscriptionId, String theChannelType) {
		String threadName = "subscription-delivery-" +
			theChannelType +
			"-" +
			theSubscriptionId +
			"-%d";
		return new LinkedBlockingQueueSubscriptionChannel(threadName);
	}

	@Override
	public SubscriptionChannel newProcessingChannel(String theChannelName) {
		return new LinkedBlockingQueueSubscriptionChannel(theChannelName + "-%d");
	}
}
