package ca.uhn.fhir.jpa.subscription.module.cache;

import ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.module.subscriber.SubscriptionDeliveringRestHookSubscriber;
import ca.uhn.fhir.jpa.subscription.module.subscriber.email.SubscriptionDeliveringEmailSubscriber;
import org.hl7.fhir.r4.model.Subscription;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeliveryHandlerCreator {
	@Autowired
	BeanFactory myBeanFactory;

	public Optional<MessageHandler> createDeliveryHandler(CanonicalSubscription theSubscription) {
		if (theSubscription.getChannelType() == Subscription.SubscriptionChannelType.EMAIL) {
			return Optional.of(myBeanFactory.getBean(SubscriptionDeliveringEmailSubscriber.class));
		} else if (theSubscription.getChannelType() == Subscription.SubscriptionChannelType.RESTHOOK) {
			return Optional.of(myBeanFactory.getBean(SubscriptionDeliveringRestHookSubscriber.class));
		} else {
			return Optional.empty();
		}
	}
}
