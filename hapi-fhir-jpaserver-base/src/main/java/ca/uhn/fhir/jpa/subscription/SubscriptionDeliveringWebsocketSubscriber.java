package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.jpa.dao.IDao;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.dao.data.ISubscriptionFlaggedResourceDataDao;
import ca.uhn.fhir.jpa.dao.data.ISubscriptionTableDao;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.entity.SubscriptionFlaggedResource;
import ca.uhn.fhir.jpa.entity.SubscriptionTable;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.gclient.IClientExecutable;
import org.apache.commons.lang3.ObjectUtils;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.ConcurrentHashMap;

public class SubscriptionDeliveringWebsocketSubscriber extends BaseSubscriptionSubscriber {
	private final PlatformTransactionManager myTxManager;
	private final ISubscriptionFlaggedResourceDataDao mySubscriptionFlaggedResourceDao;
	private final ISubscriptionTableDao mySubscriptionTableDao;
	private final IResourceTableDao myResourceTableDao;
	private Logger ourLog = LoggerFactory.getLogger(SubscriptionDeliveringWebsocketSubscriber.class);

	public SubscriptionDeliveringWebsocketSubscriber(IFhirResourceDao theSubscriptionDao, ConcurrentHashMap<String, IBaseResource> theIdToSubscription, Subscription.SubscriptionChannelType theChannelType, SubscribableChannel theProcessingChannel, PlatformTransactionManager theTxManager, ISubscriptionFlaggedResourceDataDao theSubscriptionFlaggedResourceDataDao, ISubscriptionTableDao theSubscriptionTableDao, IResourceTableDao theResourceTableDao) {
		super(theSubscriptionDao, theIdToSubscription, theChannelType, theProcessingChannel);

		myTxManager = theTxManager;
		mySubscriptionFlaggedResourceDao = theSubscriptionFlaggedResourceDataDao;
		mySubscriptionTableDao = theSubscriptionTableDao;
		myResourceTableDao = theResourceTableDao;
	}


	@Override
	public void handleMessage(final Message<?> theMessage) throws MessagingException {
		if (!(theMessage.getPayload() instanceof ResourceDeliveryMessage)) {
			return;
		}

		final ResourceDeliveryMessage msg = (ResourceDeliveryMessage) theMessage.getPayload();

		if (!subscriptionTypeApplies(getContext(), msg.getSubscription())) {
			return;
		}

		TransactionTemplate txTemplate = new TransactionTemplate(myTxManager);
		txTemplate.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRED);
		txTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				IBaseResource payload = msg.getPayoad();
				Long payloadPid = extractResourcePid(payload);
				ResourceTable payloadTable = myResourceTableDao.findOne(payloadPid);

				IBaseResource subscription = msg.getSubscription();
				Long subscriptionPid = extractResourcePid(subscription);
				SubscriptionTable subscriptionTable = mySubscriptionTableDao.findOneByResourcePid(subscriptionPid);

				ourLog.info("Adding new resource {} for subscription: {}", payload.getIdElement().toUnqualified().getValue(), subscription.getIdElement().toUnqualifiedVersionless().getValue());

				SubscriptionFlaggedResource candidate = new SubscriptionFlaggedResource();
				candidate.setResource(payloadTable);
				candidate.setSubscription(subscriptionTable);
				candidate.setVersion(payload.getIdElement().getVersionIdPartAsLong());

				mySubscriptionFlaggedResourceDao.save(candidate);
			}
		});

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

	private Long extractResourcePid(IBaseResource thePayoad) {
		Long pid;
		if (thePayoad instanceof IResource) {
         pid = IDao.RESOURCE_PID.get((IResource) thePayoad);
      } else {
         pid = IDao.RESOURCE_PID.get((IAnyResource) thePayoad);
      }
		return pid;
	}
}
