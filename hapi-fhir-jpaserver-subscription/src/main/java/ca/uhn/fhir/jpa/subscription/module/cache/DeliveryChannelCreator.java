package ca.uhn.fhir.jpa.subscription.module.cache;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.module.SubscriptionChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Service;

import java.util.concurrent.LinkedBlockingQueue;

@Service
public class DeliveryChannelCreator {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(DeliveryChannelCreator.class);

	@Autowired
	FhirContext myFhirContext;

	protected SubscribableChannel createDeliveryChannel(CanonicalSubscription theSubscription) {
		String subscriptionId = theSubscription.getIdElement(myFhirContext).getIdPart();

		LinkedBlockingQueue<Runnable> executorQueue = new LinkedBlockingQueue<>(SubscriptionConstants.DELIVERY_EXECUTOR_QUEUE_SIZE);

		return new SubscriptionChannel(executorQueue, "subscription-delivery-" + subscriptionId + "-%d");
	}

}
