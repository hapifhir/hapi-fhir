package ca.uhn.fhir.jpa.subscription.channel.subscription;

import ca.uhn.fhir.broker.api.IRetryAwareMessageListener;
import ca.uhn.fhir.jpa.subscription.api.ISubscriptionDeliveryValidator;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage;
import ca.uhn.fhir.rest.server.messaging.IMessage;
import ca.uhn.fhir.rest.server.messaging.IMessageDeliveryContext;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IIdType;
import org.jetbrains.annotations.NotNull;

public class SubscriptionValidatingListener implements IRetryAwareMessageListener<ResourceDeliveryMessage> {
	private final ISubscriptionDeliveryValidator mySubscriptionDeliveryValidator;
	private final IIdType mySubscriptionId;

	public SubscriptionValidatingListener(
			ISubscriptionDeliveryValidator theSubscriptionDeliveryValidator, IIdType theSubscriptionId) {
		mySubscriptionDeliveryValidator = theSubscriptionDeliveryValidator;
		mySubscriptionId = theSubscriptionId;
	}

	@Override
	public void handleMessage(
			@Nullable IMessageDeliveryContext theMessageDeliveryContext,
			@NotNull IMessage<ResourceDeliveryMessage> theMessage) {
		if (theMessageDeliveryContext != null
				&& theMessageDeliveryContext.getRetryCount() > 0
				&& mySubscriptionDeliveryValidator != null) {
			mySubscriptionDeliveryValidator.validate(mySubscriptionId, theMessage.getPayload());
		}
	}

	@Override
	public Class<ResourceDeliveryMessage> getPayloadType() {
		return ResourceDeliveryMessage.class;
	}
}
