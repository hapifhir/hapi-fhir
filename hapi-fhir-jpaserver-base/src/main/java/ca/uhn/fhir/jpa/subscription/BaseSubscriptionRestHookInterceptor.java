package ca.uhn.fhir.jpa.subscription;

public abstract class BaseSubscriptionRestHookInterceptor extends BaseSubscriptionInterceptor {
	private SubscriptionDeliveringRestHookSubscriber mySubscriptionDeliverySubscriber;

	@Override
	protected void registerDeliverySubscriber() {
		if (mySubscriptionDeliverySubscriber == null) {
			mySubscriptionDeliverySubscriber = new SubscriptionDeliveringRestHookSubscriber(getSubscriptionDao(), getIdToSubscription(), getChannelType(), getProcessingChannel());
		}
		getProcessingChannel().subscribe(mySubscriptionDeliverySubscriber);
	}


	@Override
	protected void unregisterDeliverySubscriber() {
		getProcessingChannel().unsubscribe(mySubscriptionDeliverySubscriber);
	}
}
