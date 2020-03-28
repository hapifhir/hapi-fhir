package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.subscription.IResourceModifiedConsumer;
import ca.uhn.fhir.jpa.subscription.SubscriptionMatcherInterceptor;
import ca.uhn.fhir.jpa.subscription.module.LinkedBlockingQueueSubscribableChannel;
import ca.uhn.fhir.jpa.subscription.module.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.util.JpaInterceptorBroadcaster;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Nonnull;
import javax.annotation.PreDestroy;

// FIXME EMPI Now that this is used outside of Subscriptions, we should move it. Maybe to a `messaging` package? (see ResourceModifiedMessage)
public abstract class BaseResourceModifiedInterceptor implements IResourceModifiedConsumer {
	protected SubscribableChannel myMatchingChannel;

	private Logger ourLog = LoggerFactory.getLogger(SubscriptionMatcherInterceptor.class);
	@Autowired
	protected FhirContext myFhirContext;
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;


	public void start() {
		if (myMatchingChannel == null) {
			myMatchingChannel = createMatchingChannel();
		}

		myMatchingChannel.subscribe(getSubscriber());
		ourLog.info("Resource Matching Subscriber subscribed to Matching Channel {} with name {}", myMatchingChannel.getClass().getName(), getMatchingChannelName());

	}

	protected abstract String getMatchingChannelName();

	protected abstract MessageHandler getSubscriber();

	protected abstract SubscribableChannel createMatchingChannel();

	@SuppressWarnings("unused")
	@PreDestroy
	public void preDestroy() {

		if (myMatchingChannel != null) {
			myMatchingChannel.unsubscribe(getSubscriber());
		}
	}

	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED)
	public void resourceCreated(IBaseResource theResource, RequestDetails theRequest) {
		submitResourceModified(theResource, ResourceModifiedMessage.OperationTypeEnum.CREATE, theRequest);
	}

	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_DELETED)
	public void resourceDeleted(IBaseResource theResource, RequestDetails theRequest) {
		submitResourceModified(theResource, ResourceModifiedMessage.OperationTypeEnum.DELETE, theRequest);
	}

	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED)
	public void resourceUpdated(IBaseResource theOldResource, IBaseResource theNewResource, RequestDetails theRequest) {
		submitResourceModified(theNewResource, ResourceModifiedMessage.OperationTypeEnum.UPDATE, theRequest);
	}

	private void submitResourceModified(IBaseResource theNewResource, ResourceModifiedMessage.OperationTypeEnum theOperationType, RequestDetails theRequest) {
		ResourceModifiedMessage msg = new ResourceModifiedMessage(myFhirContext, theNewResource, theOperationType);
		HookParams params = new HookParams()
			.add(ResourceModifiedMessage.class, msg);
		boolean outcome = JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequest, getSubmitPointcut(), params);
		if (!outcome) {
			return;
		}

		submitResourceModified(msg);
	}

	@Nonnull
	protected abstract Pointcut getSubmitPointcut();

	protected void sendToProcessingChannel(final ResourceModifiedMessage theMessage) {
		ourLog.trace("Sending resource modified message to processing channel");
		Validate.notNull(myMatchingChannel, "A " + this.getClass().getName() + " has been registered without calling start() on it.");
		myMatchingChannel.send(new ResourceModifiedJsonMessage(theMessage));
	}

	public void setFhirContext(FhirContext theCtx) {
		myFhirContext = theCtx;
	}

	/**
	 * This is an internal API - Use with caution!
	 */
	@Override
	public void submitResourceModified(final ResourceModifiedMessage theMsg) {
		/*
		 * We only want to submit the message to the processing queue once the
		 * transaction is committed. We do this in order to make sure that the
		 * data is actually in the DB, in case it's the database matcher.
		 */
		if (TransactionSynchronizationManager.isSynchronizationActive()) {
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
				@Override
				public int getOrder() {
					return 0;
				}

				@Override
				public void afterCommit() {
					sendToProcessingChannel(theMsg);
				}
			});
		} else {
			sendToProcessingChannel(theMsg);
		}
	}

	@VisibleForTesting
	public LinkedBlockingQueueSubscribableChannel getProcessingChannelForUnitTest() {
		return (LinkedBlockingQueueSubscribableChannel) myMatchingChannel;
	}
}
