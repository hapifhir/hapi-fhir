package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.jpa.subscription.asynch.AsyncResourceModifiedProcessingSchedulerSvc;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.IResourceModifiedConsumer;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.SubscriptionMatcherInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * The purpose of this interceptor is to synchronously submit ResourceModifiedMessage to the
 * subscription processing pipeline, ie, as part of processing the operation on a resource.
 * It is meant to replace the SubscriptionMatcherInterceptor in integrated tests where
 * scheduling is disabled.  See {@link AsyncResourceModifiedProcessingSchedulerSvc}
 * for further details on asynchronous submissions.
 */
public class SynchronousSubscriptionMatcherInterceptor extends SubscriptionMatcherInterceptor {

	@Autowired
	private IResourceModifiedConsumer myResourceModifiedConsumer;

	@Override
	protected void processResourceModifiedMessage(ResourceModifiedMessage theResourceModifiedMessage) {
		if (TransactionSynchronizationManager.isSynchronizationActive()) {
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
				@Override
				public int getOrder() {
					return 0;
				}

				@Override
				public void afterCommit() {
					myResourceModifiedConsumer.submitResourceModified(theResourceModifiedMessage);
				}
			});
		} else {
			myResourceModifiedConsumer.submitResourceModified(theResourceModifiedMessage);
		}
	}

}
