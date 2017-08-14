package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Subscription;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.SubscribableChannel;

import java.util.concurrent.ConcurrentHashMap;

public class SubscriptionDeliveringRestHookSubscriber extends BaseSubscriptionSubscriber {

	public SubscriptionDeliveringRestHookSubscriber(IFhirResourceDao theSubscriptionDao, ConcurrentHashMap<String, IBaseResource> theIdToSubscription, Subscription.SubscriptionChannelType theChannelType, SubscribableChannel theProcessingChannel) {
		super(theSubscriptionDao, theIdToSubscription, theChannelType, theProcessingChannel);
	}

	@Override
	public void handleMessage(Message<?> theMessage) throws MessagingException {
		if (!(theMessage.getPayload() instanceof ResourceDeliveryMessage)) {
			return;
		}

		ResourceDeliveryMessage msg = (ResourceDeliveryMessage) theMessage.getPayload();

		if (!subscriptionTypeApplies(getContext(), msg.getSubscription())) {
			return;
		}

		

	}
}
