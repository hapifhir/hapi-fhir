package ca.uhn.fhir.jpa.subscription.model;

/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.messaging.BaseResourceModifiedMessage;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 * Most of this class has been moved to ResourceModifiedMessage in the hapi-fhir-server project, for a reusable channel ResourceModifiedMessage
 * that doesn't require knowledge of subscriptions.
 */
public class ResourceModifiedMessage extends BaseResourceModifiedMessage {

	/**
	 * This will only be set if the resource is being triggered for a specific
	 * subscription
	 */
	@JsonProperty(value = "subscriptionId", required = false)
	private String mySubscriptionId;

	@JsonProperty(value = "partitionId", required = false)
	private RequestPartitionId myPartitionId;


	/**
	 * Constructor
	 */
	public ResourceModifiedMessage() {
		super();
	}

	public ResourceModifiedMessage(FhirContext theFhirContext, IBaseResource theResource, OperationTypeEnum theOperationType) {
		super(theFhirContext, theResource, theOperationType);
		myPartitionId = RequestPartitionId.defaultPartition();
	}

	public ResourceModifiedMessage(FhirContext theFhirContext, IBaseResource theNewResource, OperationTypeEnum theOperationType, RequestDetails theRequest) {
		super(theFhirContext, theNewResource, theOperationType, theRequest);
		myPartitionId = RequestPartitionId.defaultPartition();
	}

	public ResourceModifiedMessage(FhirContext theFhirContext, IBaseResource theNewResource, OperationTypeEnum theOperationType, RequestDetails theRequest, RequestPartitionId theRequestPartitionId) {
		super(theFhirContext, theNewResource, theOperationType, theRequest);
		myPartitionId = theRequestPartitionId;
	}


	public String getSubscriptionId() {
		return mySubscriptionId;
	}

	public void setSubscriptionId(String theSubscriptionId) {
		mySubscriptionId = theSubscriptionId;
	}

	public RequestPartitionId getPartitionId() {
		return myPartitionId;
	}

	public void setPartitionId(RequestPartitionId thePartitionId) {
		myPartitionId = thePartitionId;
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
}
