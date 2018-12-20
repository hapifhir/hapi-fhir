package ca.uhn.fhir.jpa.subscription.module.cache;

import org.springframework.messaging.SubscribableChannel;

public interface ISubscribableChannelFactory {
	public SubscribableChannel createSubscribableChannel(String theThreadPattern);
}
