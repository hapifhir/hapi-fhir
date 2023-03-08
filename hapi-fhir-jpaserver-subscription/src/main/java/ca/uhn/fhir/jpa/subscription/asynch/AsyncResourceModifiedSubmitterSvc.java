package ca.uhn.fhir.jpa.subscription.asynch;

import ca.uhn.fhir.jpa.subscription.match.matcher.matching.IResourceModifiedConsumer;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.subscription.api.ISubscriptionMessagePersistence;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;


public class AsyncResourceModifiedSubmitterSvc {

	@Autowired
	private ISubscriptionMessagePersistence mySubscriptionMessagePersistenceSvc;

	@Autowired
	private IResourceModifiedConsumer myIResourceModifiedConsumer;

	public void runDeliveryPass() {

		doRunDeliveryPass();

	}

	private void doRunDeliveryPass() {
		List<Long> allIds = mySubscriptionMessagePersistenceSvc.findAllIds();

		for (Long anId : allIds){
			Optional<ResourceModifiedMessage> optionalMessage = mySubscriptionMessagePersistenceSvc.findById(anId);
			optionalMessage.ifPresent(this::processResourceModified);
		}
	}



	private void processResourceModified(ResourceModifiedMessage theResourceModifiedMessage){
		// start a transaction
		try {
			mySubscriptionMessagePersistenceSvc.delete(theResourceModifiedMessage);
			myIResourceModifiedConsumer.processResourceModified(theResourceModifiedMessage);
		}catch(EntityNotFoundException theEntityNotFoundException){
			// all good, another thread my have sent the message already
		}catch(Throwable theThrowable){
			// we encountered an issue (again) when trying to send the message.
		}
		// end transaction
	}

}
