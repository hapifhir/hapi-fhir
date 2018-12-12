package ca.uhn.fhir.jpa.subscription.module.cache;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.springframework.messaging.MessageHandler;

import java.util.ArrayList;
import java.util.Collection;

public class SubscriptionDeliveryHandlerCache {
	private Multimap<String, MessageHandler> myIdToDeliveryHandler = Multimaps.synchronizedListMultimap(ArrayListMultimap.create());

	public void put(String theSubscriptionId, MessageHandler theHandler) {
		myIdToDeliveryHandler.put(theSubscriptionId, theHandler);
	}

	public Collection<MessageHandler> getCollection(String theSubscriptionId) {
		return new ArrayList<>(myIdToDeliveryHandler.get(theSubscriptionId));
	}
}
