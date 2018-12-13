package ca.uhn.fhir.jpa.subscription.module.cache;

import ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.module.subscriber.SubscriptionDeliveringRestHookSubscriber;
import ca.uhn.fhir.jpa.subscription.module.subscriber.email.SubscriptionDeliveringEmailSubscriber;
import org.hl7.fhir.r4.model.Subscription;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public abstract class SubscriptionDeliveryHandlerFactory {
	@Lookup
	protected abstract SubscriptionDeliveringEmailSubscriber getSubscriptionDeliveringEmailSubscriber();
	@Lookup
	protected abstract SubscriptionDeliveringRestHookSubscriber getSubscriptionDeliveringRestHookSubscriber();

	public Optional<MessageHandler> createDeliveryHandler(CanonicalSubscription theSubscription) {
		if (theSubscription.getChannelType() == Subscription.SubscriptionChannelType.EMAIL) {
			return Optional.of(getSubscriptionDeliveringEmailSubscriber());
		} else if (theSubscription.getChannelType() == Subscription.SubscriptionChannelType.RESTHOOK) {
			return Optional.of(getSubscriptionDeliveringRestHookSubscriber());
		} else {
			return Optional.empty();
		}
	}
}
