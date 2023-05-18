package ca.uhn.fhir.jpa.subscription.match.matcher.subscriber;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.server.messaging.BaseResourceModifiedMessage;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SubscriptionDeliveryRequest {
	// One of these two will be populated
	private final IBaseResource myPayload;
	private final IIdType myPayloadId;
	@Nonnull
	private final ActiveSubscription myActiveSubscription;
	@Nonnull
	private final RestOperationTypeEnum myRestOperationType;
	@Nullable
	private final RequestPartitionId myRequestPartitionId;
	@Nullable
	private final String myTransactionId;

	public SubscriptionDeliveryRequest(@Nonnull IBaseBundle theBundlePayload, @Nonnull ActiveSubscription theActiveSubscription, @Nonnull RestOperationTypeEnum theOperationType, @Nullable RequestPartitionId theRequestPartitionId, @Nullable String theTransactionId) {
		myPayload = theBundlePayload;
		myPayloadId = null;
		myActiveSubscription = theActiveSubscription;
		myRestOperationType = theOperationType;
		myRequestPartitionId = theRequestPartitionId;
		myTransactionId = theTransactionId;
	}

	public SubscriptionDeliveryRequest(IBaseResource thePayload, ResourceModifiedMessage theMsg, ActiveSubscription theActiveSubscription) {
		myPayload = thePayload;
		myPayloadId = null;
		myActiveSubscription = theActiveSubscription;
		myRestOperationType = theMsg.getOperationType().asRestOperationType();
		myRequestPartitionId = theMsg.getPartitionId();
		myTransactionId = theMsg.getTransactionId();
	}

	public SubscriptionDeliveryRequest(IIdType thePayloadId, ResourceModifiedMessage theMsg, ActiveSubscription theActiveSubscription) {
		myPayload = null;
		myPayloadId = thePayloadId;
		myActiveSubscription = theActiveSubscription;
		myRestOperationType = theMsg.getOperationType().asRestOperationType();
		myRequestPartitionId = theMsg.getPartitionId();
		myTransactionId = theMsg.getTransactionId();
	}

	public IBaseResource getPayload() {
		return myPayload;
	}

	public ActiveSubscription getActiveSubscription() {
		return myActiveSubscription;
	}

	public RestOperationTypeEnum getRestOperationType() {
		return myRestOperationType;
	}

	public BaseResourceModifiedMessage.OperationTypeEnum getOperationType() {
		return BaseResourceModifiedMessage.OperationTypeEnum.from(myRestOperationType);
	}

	public RequestPartitionId getRequestPartitionId() {
		return myRequestPartitionId;
	}

	public String getTransactionId() {
		return myTransactionId;
	}

	public CanonicalSubscription getSubscription() {
		return myActiveSubscription.getSubscription();
	}

	public IIdType getPayloadId() {
		return myPayloadId;
	}

	public boolean hasPayload() {
		return myPayload != null;
	}
}
