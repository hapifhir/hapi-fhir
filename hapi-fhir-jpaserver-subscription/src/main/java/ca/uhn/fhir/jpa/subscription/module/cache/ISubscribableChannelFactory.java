package ca.uhn.fhir.jpa.subscription.module.cache;

import org.springframework.messaging.SubscribableChannel;

public interface ISubscribableChannelFactory {
	SubscribableChannel createSubscribableChannel(String theChannelName);
}
