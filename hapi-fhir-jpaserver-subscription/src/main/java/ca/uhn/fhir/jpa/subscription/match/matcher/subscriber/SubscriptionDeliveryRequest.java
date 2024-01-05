/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.subscription.match.matcher.subscriber;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.topic.SubscriptionTopicDispatchRequest;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.server.messaging.BaseResourceModifiedMessage;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

public class SubscriptionDeliveryRequest {
	// One of these two will be populated
	private final IBaseResource myPayload;
	private final IIdType myPayloadId;
	private final ActiveSubscription myActiveSubscription;
	private final RestOperationTypeEnum myRestOperationType;
	private final RequestPartitionId myRequestPartitionId;
	private final String myTransactionId;

	public SubscriptionDeliveryRequest(
			IBaseBundle theBundlePayload,
			ActiveSubscription theActiveSubscription,
			SubscriptionTopicDispatchRequest theSubscriptionTopicDispatchRequest) {
		myPayload = theBundlePayload;
		myPayloadId = null;
		myActiveSubscription = theActiveSubscription;
		myRestOperationType = theSubscriptionTopicDispatchRequest.getRequestType();
		myRequestPartitionId = theSubscriptionTopicDispatchRequest.getRequestPartitionId();
		myTransactionId = theSubscriptionTopicDispatchRequest.getTransactionId();
	}

	public SubscriptionDeliveryRequest(
			@Nonnull IBaseResource thePayload,
			@Nonnull ResourceModifiedMessage theMsg,
			@Nonnull ActiveSubscription theActiveSubscription) {
		myPayload = thePayload;
		myPayloadId = null;
		myActiveSubscription = theActiveSubscription;
		myRestOperationType = theMsg.getOperationType().asRestOperationType();
		myRequestPartitionId = theMsg.getPartitionId();
		myTransactionId = theMsg.getTransactionId();
	}

	public SubscriptionDeliveryRequest(
			@Nonnull IIdType thePayloadId,
			@Nonnull ResourceModifiedMessage theMsg,
			@Nonnull ActiveSubscription theActiveSubscription) {
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
