package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.SubscribableChannel;

import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unchecked")
public class SubscriptionActivatingSubscriber extends BaseSubscriptionSubscriber {
	private Logger ourLog = LoggerFactory.getLogger(SubscriptionActivatingSubscriber.class);

	/**
	 * Constructor
	 */
	public SubscriptionActivatingSubscriber(IFhirResourceDao<? extends IBaseResource> theSubscriptionDao, ConcurrentHashMap<String, IBaseResource> theIdToSubscription, Subscription.SubscriptionChannelType theChannelType, SubscribableChannel theProcessingChannel) {
		super(theSubscriptionDao, theIdToSubscription, theChannelType, theProcessingChannel);
	}

	private void handleCreate(ResourceModifiedMessage theMsg) {
		if (!theMsg.getId().getResourceType().equals("Subscription")) {
			return;
		}

		boolean subscriptionTypeApplies = subscriptionTypeApplies(theMsg);
		if (subscriptionTypeApplies == false) {
			return;
		}

		FhirContext ctx = getSubscriptionDao().getContext();
		IBaseResource subscription = theMsg.getNewPayload();
		IPrimitiveType<?> status = ctx.newTerser().getSingleValueOrNull(subscription, SUBSCRIPTION_STATUS, IPrimitiveType.class);
		String statusString = status.getValueAsString();

		String oldStatus = Subscription.SubscriptionStatus.REQUESTED.toCode();
		if (oldStatus.equals(statusString)) {
			String newStatus = Subscription.SubscriptionStatus.ACTIVE.toCode();
			status.setValueAsString(newStatus);
			ourLog.info("Activating subscription {} from status {} to {}", subscription.getIdElement().toUnqualifiedVersionless().getValue(), oldStatus, newStatus);
			getSubscriptionDao().update(subscription);
			getIdToSubscription().put(subscription.getIdElement().getIdPart(), subscription);
		}
	}

	@Override
	public void handleMessage(Message<?> theMessage) throws MessagingException {

		if (!(theMessage.getPayload() instanceof ResourceModifiedMessage)) {
			return;
		}

		ResourceModifiedMessage msg = (ResourceModifiedMessage) theMessage.getPayload();
		IIdType id = msg.getId();

		switch (msg.getOperationType()) {
			case DELETE:
				getIdToSubscription().remove(id.getIdPart());
				return;
			case CREATE:
				handleCreate(msg);
				break;
			case UPDATE:
				handleUpdate(msg);
				break;
		}

	}

	private void handleUpdate(ResourceModifiedMessage theMsg) {
		if (!theMsg.getId().getResourceType().equals("Subscription")) {
			return;
		}

		boolean subscriptionTypeApplies = subscriptionTypeApplies(theMsg);
		if (subscriptionTypeApplies == false) {
			return;
		}

		FhirContext ctx = getSubscriptionDao().getContext();
		IBaseResource subscription = theMsg.getNewPayload();
		IPrimitiveType<?> status = ctx.newTerser().getSingleValueOrNull(subscription, SUBSCRIPTION_STATUS, IPrimitiveType.class);
		String statusString = status.getValueAsString();

		ourLog.info("Subscription {} has status {}", subscription.getIdElement().toUnqualifiedVersionless().getValue(), statusString);

		if (Subscription.SubscriptionStatus.ACTIVE.toCode().equals(statusString)) {
			getIdToSubscription().put(theMsg.getId().getIdPart(), theMsg.getNewPayload());
		}
	}
}
