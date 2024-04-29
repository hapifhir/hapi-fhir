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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.SubscriptionMatchingStrategy;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.SubscriptionQueryValidator;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.Logs;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.SubscriptionTopic;
import org.slf4j.Logger;

public class SubscriptionTopicValidatingInterceptor {
	private static final Logger ourLog = Logs.getSubscriptionTopicLog();

	private final FhirContext myFhirContext;
	private final SubscriptionQueryValidator mySubscriptionQueryValidator;

	public SubscriptionTopicValidatingInterceptor(
			FhirContext theFhirContext, SubscriptionQueryValidator theSubscriptionQueryValidator) {
		myFhirContext = theFhirContext;
		mySubscriptionQueryValidator = theSubscriptionQueryValidator;
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED)
	public void resourcePreCreate(
			IBaseResource theResource, RequestDetails theRequestDetails, RequestPartitionId theRequestPartitionId) {
		validateSubmittedSubscriptionTopic(
				theResource, theRequestDetails, theRequestPartitionId, Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED);
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED)
	public void resourceUpdated(
			IBaseResource theOldResource,
			IBaseResource theResource,
			RequestDetails theRequestDetails,
			RequestPartitionId theRequestPartitionId) {
		validateSubmittedSubscriptionTopic(
				theResource, theRequestDetails, theRequestPartitionId, Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED);
	}

	@VisibleForTesting
	void validateSubmittedSubscriptionTopic(
			IBaseResource theSubscription,
			RequestDetails theRequestDetails,
			RequestPartitionId theRequestPartitionId,
			Pointcut thePointcut) {
		if (Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED != thePointcut
				&& Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED != thePointcut) {
			throw new UnprocessableEntityException(Msg.code(2340)
					+ "Expected Pointcut to be either STORAGE_PRESTORAGE_RESOURCE_CREATED or STORAGE_PRESTORAGE_RESOURCE_UPDATED but was: "
					+ thePointcut);
		}

		if (!"SubscriptionTopic".equals(myFhirContext.getResourceType(theSubscription))) {
			return;
		}

		SubscriptionTopic subscriptionTopic =
				SubscriptionTopicCanonicalizer.canonicalizeTopic(myFhirContext, theSubscription);

		boolean finished = false;
		if (subscriptionTopic.getStatus() == null) {
			throw new UnprocessableEntityException(
					Msg.code(2338)
							+ "Can not process submitted SubscriptionTopic - SubscriptionTopic.status must be populated on this server");
		}

		switch (subscriptionTopic.getStatus()) {
			case ACTIVE:
				break;
			default:
				finished = true;
				break;
		}

		// WIP STR5 add cross-partition support like in SubscriptionValidatingInterceptor

		// WIP STR5 warn if the SubscriptionTopic criteria can't be evaluated in memory?  Do we want to annotate the
		//  strategy with an extension like Subscription?

		if (!finished) {
			subscriptionTopic.getResourceTrigger().stream().forEach(t -> validateQueryCriteria(t.getQueryCriteria()));
		}
	}

	private void validateQueryCriteria(
			SubscriptionTopic.SubscriptionTopicResourceTriggerQueryCriteriaComponent theQueryCriteria) {
		if (theQueryCriteria.getPrevious() != null) {
			validateCriteria(
					theQueryCriteria.getPrevious(), "SubscriptionTopic.resourceTrigger.queryCriteria.previous");
		}
		if (theQueryCriteria.getCurrent() != null) {
			validateCriteria(theQueryCriteria.getCurrent(), "SubscriptionTopic.resourceTrigger.queryCriteria.current");
		}
	}

	public void validateCriteria(String theCriteria, String theFieldName) {
		try {
			mySubscriptionQueryValidator.validateCriteria(theCriteria, theFieldName);
			SubscriptionMatchingStrategy strategy = mySubscriptionQueryValidator.determineStrategy(theCriteria);
			if (strategy != SubscriptionMatchingStrategy.IN_MEMORY) {
				ourLog.warn(
						"Warning: Query Criteria '{}' in {} cannot be evaluated in-memory", theCriteria, theFieldName);
			}
		} catch (InvalidRequestException | DataFormatException e) {
			throw new UnprocessableEntityException(Msg.code(2339) + "Invalid SubscriptionTopic criteria '" + theCriteria
					+ "' in " + theFieldName + ": " + e.getMessage());
		}
	}
}
