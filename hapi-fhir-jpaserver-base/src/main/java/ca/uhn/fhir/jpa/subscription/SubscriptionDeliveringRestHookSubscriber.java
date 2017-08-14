package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Subscription;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.SubscribableChannel;

import java.util.concurrent.ConcurrentHashMap;

public class SubscriptionDeliveringRestHookSubscriber extends BaseSubscriptionSubscriber {

	public SubscriptionDeliveringRestHookSubscriber(IFhirResourceDao theSubscriptionDao, ConcurrentHashMap<String, IBaseResource> theIdToSubscription, Subscription.SubscriptionChannelType theChannelType, SubscribableChannel theProcessingChannel) {
		super(theSubscriptionDao, theIdToSubscription, theChannelType, theProcessingChannel);
	}

	@Override
	public void handleMessage(Message<?> theMessage) throws MessagingException {
		if (!(theMessage.getPayload() instanceof ResourceDeliveryMessage)) {
			return;
		}

		ResourceDeliveryMessage msg = (ResourceDeliveryMessage) theMessage.getPayload();

		if (!subscriptionTypeApplies(getContext(), msg.getSubscription())) {
			return;
		}

		RestOperationTypeEnum operationType = msg.getOperationType();
		IBaseResource subscription = msg.getSubscription();
		RuntimeResourceDefinition def = getContext().getResourceDefinition(subscription);
		IPrimitiveType<?> endpoint = getContext().newTerser().getSingleValueOrNull(subscription, "Subscription.channel.endpoint", IPrimitiveType.class);
		String endpointUrl = endpoint.getValueAsString();

		getContext().getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		IGenericClient client = getContext().newRestfulGenericClient(endpointUrl);

		IBaseResource payload = msg.getPayoad();
		switch (msg.getOperationType()){
			case CREATE:
		}
	}
}
