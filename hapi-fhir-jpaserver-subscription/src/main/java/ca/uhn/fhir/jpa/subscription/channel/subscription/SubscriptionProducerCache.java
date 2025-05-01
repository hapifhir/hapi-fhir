package ca.uhn.fhir.jpa.subscription.channel.subscription;

import ca.uhn.fhir.broker.api.IChannelProducer;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage;
import ca.uhn.fhir.util.IoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SubscriptionProducerCache {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionProducerCache.class);
	private final Map<String, IChannelProducer<ResourceDeliveryMessage>> myChannelProducers = new ConcurrentHashMap<>();

	public void put(String theChannelName, IChannelProducer<ResourceDeliveryMessage> theProducer) {
		myChannelProducers.put(theChannelName, theProducer);
	}

	public void closeAndRemove(String theChannelName) {
		IChannelProducer<ResourceDeliveryMessage> producer = myChannelProducers.get(theChannelName);
		if (producer instanceof AutoCloseable) {
			IoUtils.closeQuietly((AutoCloseable) producer, ourLog);
		}
		myChannelProducers.remove(theChannelName);
	}

	public IChannelProducer<ResourceDeliveryMessage> get(String theChannelName) {
		return myChannelProducers.get(theChannelName);
	}
}
