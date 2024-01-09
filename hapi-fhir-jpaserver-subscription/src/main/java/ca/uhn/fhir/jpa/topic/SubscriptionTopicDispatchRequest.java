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
package ca.uhn.fhir.jpa.topic;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.topic.filter.ISubscriptionTopicFilterMatcher;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;

public class SubscriptionTopicDispatchRequest {
	@Nonnull
	private final String myTopicUrl;

	@Nonnull
	private final List<IBaseResource> myResources;

	@Nonnull
	private final ISubscriptionTopicFilterMatcher mySubscriptionTopicFilterMatcher;

	@Nonnull
	private final RestOperationTypeEnum myRequestType;

	@Nullable
	private final InMemoryMatchResult myInMemoryMatchResult;

	@Nullable
	private final RequestPartitionId myRequestPartitionId;

	@Nullable
	private final String myTransactionId;

	/**
	 * @param theTopicUrl                       Deliver to subscriptions for this topic
	 * @param theResources                      The list of resources to deliver.  The first resource will be the primary "focus" resource per the Subscription documentation.
	 *                                          This list should _not_ include the SubscriptionStatus.  The SubscriptionStatus will be added as the first element to
	 *                                          the delivered bundle.  The reason for this is that the SubscriptionStatus needs to reference the subscription ID, which is
	 *                                          not known until the bundle is delivered.
	 * @param theSubscriptionTopicFilterMatcher is used to match the primary "focus" resource against the subscription filters
	 * @param theRequestType                    The type of request that led to this dispatch.  This determines the request type of the bundle entries
	 * @param theInMemoryMatchResult            Information about the match event that led to this dispatch that is sent to SUBSCRIPTION_RESOURCE_MATCHED
	 * @param theRequestPartitionId             The request partitions of the request, if any.  This is used by subscriptions that need to perform repository
	 *                                          operations as a part of their delivery.  Those repository operations will be performed on the supplied request partitions
	 * @param theTransactionId                  The transaction ID of the request, if any.  This is used for logging.
	 *
	 */
	public SubscriptionTopicDispatchRequest(
			@Nonnull String theTopicUrl,
			@Nonnull List<IBaseResource> theResources,
			@Nonnull ISubscriptionTopicFilterMatcher theSubscriptionTopicFilterMatcher,
			@Nonnull RestOperationTypeEnum theRequestType,
			@Nullable InMemoryMatchResult theInMemoryMatchResult,
			@Nullable RequestPartitionId theRequestPartitionId,
			@Nullable String theTransactionId) {
		myTopicUrl = theTopicUrl;
		myResources = theResources;
		mySubscriptionTopicFilterMatcher = theSubscriptionTopicFilterMatcher;
		myRequestType = theRequestType;
		myInMemoryMatchResult = theInMemoryMatchResult;
		myRequestPartitionId = theRequestPartitionId;
		myTransactionId = theTransactionId;
	}

	public String getTopicUrl() {
		return myTopicUrl;
	}

	public List<IBaseResource> getResources() {
		return myResources;
	}

	public ISubscriptionTopicFilterMatcher getSubscriptionTopicFilterMatcher() {
		return mySubscriptionTopicFilterMatcher;
	}

	public RestOperationTypeEnum getRequestType() {
		return myRequestType;
	}

	public InMemoryMatchResult getInMemoryMatchResult() {
		return myInMemoryMatchResult;
	}

	public RequestPartitionId getRequestPartitionId() {
		return myRequestPartitionId;
	}

	public String getTransactionId() {
		return myTransactionId;
	}
}
