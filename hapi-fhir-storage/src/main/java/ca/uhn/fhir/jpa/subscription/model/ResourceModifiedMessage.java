/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.subscription.model;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.messaging.BaseResourceModifiedMessage;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Objects;

/**
 * Most of this class has been moved to ResourceModifiedMessage in the hapi-fhir-server project, for a reusable channel ResourceModifiedMessage
 * that doesn't require knowledge of subscriptions.
 */
// TODO KHS rename classes like this to ResourceModifiedPayload so they're not confused with the actual message wrapper
// class
public class ResourceModifiedMessage extends BaseResourceModifiedMessage {

	/**
	 * This will only be set if the resource is being triggered for a specific
	 * subscription
	 */
	@JsonProperty(value = "subscriptionId")
	private String mySubscriptionId;

	/**
	 * Constructor
	 */
	public ResourceModifiedMessage() {
		super();
	}

	public ResourceModifiedMessage(
			IIdType theIdType, OperationTypeEnum theOperationType, RequestPartitionId theRequestPartitionId) {
		super(theIdType, theOperationType);
		setPartitionId(theRequestPartitionId);
	}

	/**
	 * @deprecated use {@link ResourceModifiedMessage#ResourceModifiedMessage(FhirContext, IBaseResource, OperationTypeEnum, RequestDetails, RequestPartitionId)} instead
	 */
	@Deprecated
	public ResourceModifiedMessage(
			FhirContext theFhirContext, IBaseResource theResource, OperationTypeEnum theOperationType) {
		super(theFhirContext, theResource, theOperationType);
		setPartitionId(RequestPartitionId.defaultPartition());
	}

	public ResourceModifiedMessage(
			FhirContext theFhirContext,
			IBaseResource theResource,
			OperationTypeEnum theOperationType,
			RequestPartitionId theRequestPartitionId) {
		super(theFhirContext, theResource, theOperationType);
		setPartitionId(theRequestPartitionId);
	}

	public ResourceModifiedMessage(
			FhirContext theFhirContext,
			IBaseResource theNewResource,
			OperationTypeEnum theOperationType,
			RequestDetails theRequest,
			RequestPartitionId theRequestPartitionId) {
		super(theFhirContext, theNewResource, theOperationType, theRequest, theRequestPartitionId);
	}

	public String getSubscriptionId() {
		return mySubscriptionId;
	}

	public void setSubscriptionId(String theSubscriptionId) {
		mySubscriptionId = theSubscriptionId;
	}

	public void setPayloadToNull() {
		myPayload = null;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("operationType", myOperationType)
				.append("subscriptionId", mySubscriptionId)
				.append("payloadId", myPayloadId)
				.append("partitionId", myPartitionId)
				.toString();
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;
		if (theO == null || getClass() != theO.getClass()) return false;
		if (!super.equals(theO)) return false;
		ResourceModifiedMessage that = (ResourceModifiedMessage) theO;
		return Objects.equals(getSubscriptionId(), that.getSubscriptionId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getSubscriptionId());
	}
}
