package ca.uhn.fhir.jpa.subscription.module.cache;

import ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.module.subscriber.SubscriptionDeliveringRestHookSubscriber;
import ca.uhn.fhir.jpa.subscription.module.subscriber.email.IEmailSender;
import ca.uhn.fhir.jpa.subscription.module.subscriber.email.SubscriptionDeliveringEmailSubscriber;
import org.hl7.fhir.r4.model.Subscription;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public abstract class SubscriptionDeliveryHandlerFactory {
	private IEmailSender myEmailSender;

	@Lookup
	protected abstract SubscriptionDeliveringEmailSubscriber getSubscriptionDeliveringEmailSubscriber(IEmailSender myEmailSender);
	@Lookup
	protected abstract SubscriptionDeliveringRestHookSubscriber getSubscriptionDeliveringRestHookSubscriber();

	public Optional<MessageHandler> createDeliveryHandler(CanonicalSubscription theSubscription) {
		if (theSubscription.getChannelType() == Subscription.SubscriptionChannelType.EMAIL) {
			return Optional.of(getSubscriptionDeliveringEmailSubscriber(myEmailSender));
		} else if (theSubscription.getChannelType() == Subscription.SubscriptionChannelType.RESTHOOK) {
			return Optional.of(getSubscriptionDeliveringRestHookSubscriber());
		} else {
			return Optional.empty();
		}
	}

	public void setEmailSender(IEmailSender theEmailSender) {
		myEmailSender = theEmailSender;
	}
}
