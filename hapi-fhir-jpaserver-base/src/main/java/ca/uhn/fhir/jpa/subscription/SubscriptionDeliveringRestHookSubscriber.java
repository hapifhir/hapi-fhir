package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.gclient.IClientExecutable;
import org.apache.commons.lang3.ObjectUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.SubscribableChannel;

import java.util.concurrent.ConcurrentHashMap;

public class SubscriptionDeliveringRestHookSubscriber extends BaseSubscriptionSubscriber {
	private Logger ourLog = LoggerFactory.getLogger(SubscriptionDeliveringRestHookSubscriber.class);

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

		// Grab the endpoint from the subscription
		IPrimitiveType<?> endpoint = getContext().newTerser().getSingleValueOrNull(subscription, BaseSubscriptionInterceptor.SUBSCRIPTION_ENDPOINT, IPrimitiveType.class);
		String endpointUrl = endpoint.getValueAsString();

		// Grab the payload type (encoding mimetype ) from the subscription
		IPrimitiveType<?> payload = getContext().newTerser().getSingleValueOrNull(subscription, BaseSubscriptionInterceptor.SUBSCRIPTION_PAYLOAD, IPrimitiveType.class);
		String payloadString = payload.getValueAsString();
		if (payloadString.contains(";")) {
			payloadString = payloadString.substring(0, payloadString.indexOf(';'));
		}
		payloadString = payloadString.trim();
		EncodingEnum payloadType = EncodingEnum.forContentType(payloadString);
		payloadType = ObjectUtils.defaultIfNull(payloadType, EncodingEnum.XML);

		getContext().getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		IGenericClient client = getContext().newRestfulGenericClient(endpointUrl);

		IBaseResource payloadResource = msg.getPayoad();

		IClientExecutable<?, ?> operation;
		switch (operationType) {
			case CREATE:
				operation = client.create().resource(payloadResource);
				break;
			case UPDATE:
				operation = client.update().resource(payloadResource);
				break;
			case DELETE:
				operation = client.delete().resourceById(msg.getPayloadId());
				break;
			default:
				ourLog.warn("Ignoring delivery message of type: {}", msg.getOperationType());
				return;
		}

		operation.encoded(payloadType);

		ourLog.info("Delivering {} rest-hook payload {} for {}", operationType, payloadResource.getIdElement().toUnqualified().getValue(), subscription.getIdElement().toUnqualifiedVersionless().getValue());

		operation.execute();

	}
}
