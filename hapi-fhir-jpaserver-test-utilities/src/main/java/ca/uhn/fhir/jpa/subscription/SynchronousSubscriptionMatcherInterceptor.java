package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.jpa.subscription.asynch.AsyncResourceModifiedProcessingSchedulerSvc;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.IResourceModifiedConsumer;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.SubscriptionMatcherInterceptor;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * The purpose of this interceptor is to provide the capability to submit ResourceModifiedMessage to the
 * subscription processing pipeline.  It is ment to replace the SubscriptionMatcherInterceptor in
 * integrated tests where scheduling is disabled.  See {@link AsyncResourceModifiedProcessingSchedulerSvc}
 * for further details on asynchronous submissions.
 */
public class SynchronousSubscriptionMatcherInterceptor extends SubscriptionMatcherInterceptor {

	private final IResourceModifiedConsumer myResourceModifiedConsumer;

	public SynchronousSubscriptionMatcherInterceptor(IResourceModifiedConsumer theResourceModifiedConsumer) {
		myResourceModifiedConsumer = theResourceModifiedConsumer;
	}

	@Override
	protected void processResourceModified(IBaseResource theNewResource, ResourceModifiedMessage.OperationTypeEnum theOperationType, RequestDetails theRequest) {
		ResourceModifiedMessage msg = createResourceModifiedMessage(theNewResource, theOperationType, theRequest);

		if (TransactionSynchronizationManager.isSynchronizationActive()) {
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
				@Override
				public int getOrder() {
					return 0;
				}

				@Override
				public void afterCommit() {
					myResourceModifiedConsumer.submitResourceModified(msg);
				}
			});
		} else {
			myResourceModifiedConsumer.submitResourceModified(msg);
		}

	}


}
