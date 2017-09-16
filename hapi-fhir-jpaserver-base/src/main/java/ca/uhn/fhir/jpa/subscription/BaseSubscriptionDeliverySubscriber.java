package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import org.hl7.fhir.r4.model.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;

public abstract class BaseSubscriptionDeliverySubscriber extends BaseSubscriptionSubscriber {
	private static final Logger ourLog = LoggerFactory.getLogger(BaseSubscriptionDeliverySubscriber.class);

	public BaseSubscriptionDeliverySubscriber(IFhirResourceDao<?> theSubscriptionDao, Subscription.SubscriptionChannelType theChannelType, BaseSubscriptionInterceptor theSubscriptionInterceptor) {
		super(theSubscriptionDao, theChannelType, theSubscriptionInterceptor);
	}

	@Override
	public void handleMessage(Message<?> theMessage) throws MessagingException {
		if (!(theMessage.getPayload() instanceof ResourceDeliveryMessage)) {
			return;
		}
		try {
			ResourceDeliveryMessage msg = (ResourceDeliveryMessage) theMessage.getPayload();
			if (!subscriptionTypeApplies(getContext(), msg.getSubscription().getBackingSubscription())) {
				return;
			}

			CanonicalSubscription updatedSubscription = (CanonicalSubscription)getSubscriptionInterceptor().getIdToSubscription().get(msg.getSubscription().getIdElement().getIdPart());
			if (updatedSubscription != null) {
				msg.setSubscription(updatedSubscription);
			}

			handleMessage(msg);
		} catch (Exception e) {
			ourLog.error("Failure handling subscription payload", e);
			throw new MessagingException(theMessage, "Failure handling subscription payload", e);
		}
	}

	public abstract void handleMessage(ResourceDeliveryMessage theMessage) throws Exception;

}
