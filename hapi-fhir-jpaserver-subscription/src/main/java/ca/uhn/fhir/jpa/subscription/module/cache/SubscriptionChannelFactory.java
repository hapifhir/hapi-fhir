package ca.uhn.fhir.jpa.subscription.module.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionChannelFactory {

	private ISubscribableChannelFactory mySubscribableChannelFactory;

	@Autowired
	public SubscriptionChannelFactory(ISubscribableChannelFactory theSubscribableChannelFactory) {
		mySubscribableChannelFactory = theSubscribableChannelFactory;
	}

	public SubscribableChannel newDeliveryChannel(String theSubscriptionId, String theChannelType) {
		String threadName = "subscription-delivery-" +
			theChannelType +
			"-" +
			theSubscriptionId +
			"-%d";
		return mySubscribableChannelFactory.createSubscribableChannel(threadName);
	}

	public SubscribableChannel newMatchingChannel(String theChannelName) {
		return mySubscribableChannelFactory.createSubscribableChannel(theChannelName + "-%d");
	}
}
