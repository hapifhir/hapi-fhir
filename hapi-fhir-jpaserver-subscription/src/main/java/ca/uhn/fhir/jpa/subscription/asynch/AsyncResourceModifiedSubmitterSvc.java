package ca.uhn.fhir.jpa.subscription.asynch;

import ca.uhn.fhir.jpa.model.entity.ResourceModifiedEntityPK;
import ca.uhn.fhir.subscription.api.IResourceModifiedConsumerWithRetry;
import ca.uhn.fhir.subscription.api.IResourceModifiedMessagePersistenceSvc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class AsyncResourceModifiedSubmitterSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(AsyncResourceModifiedSubmitterSvc.class);
	private IResourceModifiedMessagePersistenceSvc mySubscriptionMessagePersistenceSvc;
	private IResourceModifiedConsumerWithRetry myIResourceModifiedConsumer;

	public AsyncResourceModifiedSubmitterSvc(IResourceModifiedMessagePersistenceSvc theSubscriptionMessagePersistenceSvc, IResourceModifiedConsumerWithRetry theIResourceModifiedConsumer) {
		mySubscriptionMessagePersistenceSvc = theSubscriptionMessagePersistenceSvc;
		myIResourceModifiedConsumer = theIResourceModifiedConsumer;
	}

	public void runDeliveryPass() {
		List<ResourceModifiedEntityPK> allIds = mySubscriptionMessagePersistenceSvc.findAllIds();
		ourLog.info("Attempting to submit {} resources to consumer channel.", allIds.size());

		for (ResourceModifiedEntityPK anId : allIds){

			boolean wasProcessed = myIResourceModifiedConsumer.processResourceModified(anId);

			if(!wasProcessed){
				break;
			}
		}

	}


}
