package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.dao.data.ISubscriptionFlaggedResourceDataDao;
import ca.uhn.fhir.jpa.dao.data.ISubscriptionTableDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;

public abstract class BaseSubscriptionWebsocketInterceptor extends BaseSubscriptionInterceptor {
	private SubscriptionDeliveringWebsocketSubscriber mySubscriptionDeliverySubscriber;

	@Autowired
	private ISubscriptionFlaggedResourceDataDao mySubscriptionFlaggedResourceDataDao;

	@Autowired
	private ISubscriptionTableDao mySubscriptionTableDao;

	@Autowired
	private PlatformTransactionManager myTxManager;

	@Autowired
	private IResourceTableDao myResourceTableDao;

	@Override
	protected void registerDeliverySubscriber() {
		if (mySubscriptionDeliverySubscriber == null) {
			mySubscriptionDeliverySubscriber = new SubscriptionDeliveringWebsocketSubscriber(getSubscriptionDao(), getIdToSubscription(), getChannelType(), getProcessingChannel(), myTxManager, mySubscriptionFlaggedResourceDataDao, mySubscriptionTableDao, myResourceTableDao);
		}
		getProcessingChannel().subscribe(mySubscriptionDeliverySubscriber);
	}


	@Override
	protected void unregisterDeliverySubscriber() {
		getProcessingChannel().unsubscribe(mySubscriptionDeliverySubscriber);
	}
}
