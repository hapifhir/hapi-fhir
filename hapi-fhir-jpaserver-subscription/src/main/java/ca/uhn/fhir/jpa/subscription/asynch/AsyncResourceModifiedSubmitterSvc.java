package ca.uhn.fhir.jpa.subscription.asynch;

import ca.uhn.fhir.jpa.subscription.match.matcher.matching.IResourceModifiedConsumer;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.subscription.api.ISubscriptionMessagePersistenceSvc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;


public class AsyncResourceModifiedSubmitterSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(AsyncResourceModifiedSubmitterSvc.class);
	@Autowired
	private ISubscriptionMessagePersistenceSvc mySubscriptionMessagePersistenceSvc;

	@Autowired
	private PlatformTransactionManager myTxManager;

	@Autowired
	private IResourceModifiedConsumer myIResourceModifiedConsumer;

	public void runDeliveryPass() {
		List<Long> allIds = mySubscriptionMessagePersistenceSvc.findAllIds();
		ourLog.info("Attempting to submit {} resources to consumer channel.", allIds.size());

		for (Long anId : allIds){
			Optional<ResourceModifiedMessage> optionalMessage = mySubscriptionMessagePersistenceSvc.findById(anId);

			if(optionalMessage.isEmpty()){
				continue;
			}

			boolean wasProcessed = processResourceModified(optionalMessage.get());

			if(!wasProcessed){
				break;
			}
		}

	}
	private boolean processResourceModified(ResourceModifiedMessage theResourceModifiedMessage){
		TransactionTemplate txTemplate = new TransactionTemplate(myTxManager);

		return (boolean) txTemplate.execute(doProcessResourceModified(theResourceModifiedMessage));

	}

	private TransactionCallback doProcessResourceModified(ResourceModifiedMessage theResourceModifiedMessage) {
		return theStatus -> {
			boolean processed = true;

			try {
				// we start by deleting the entity to lock the table.
				mySubscriptionMessagePersistenceSvc.delete(theResourceModifiedMessage);

				myIResourceModifiedConsumer.processResourceModified(theResourceModifiedMessage);
			} catch (EntityNotFoundException theEntityNotFoundException) {
				// all good, another thread may have sent the message already
			} catch (Throwable theThrowable) {
				// we encountered an issue (again) when trying to send the message so mark the transaction for rollback
				ourLog.warn("Channel submission failed for resource with id {} matching subscription with id {}.  Further attempts will be performed at later time.", theResourceModifiedMessage.getPayloadId(), theResourceModifiedMessage.getSubscriptionId());
				processed = false;
				theStatus.setRollbackOnly();
			}

			return processed;
		};

	}

}
