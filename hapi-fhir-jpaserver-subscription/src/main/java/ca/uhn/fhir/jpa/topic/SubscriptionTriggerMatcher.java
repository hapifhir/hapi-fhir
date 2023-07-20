/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.messaging.BaseResourceMessage;
import ca.uhn.fhir.storage.PreviousVersionReader;
import ca.uhn.fhir.util.Logs;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.Enumeration;
import org.hl7.fhir.r5.model.SubscriptionTopic;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;

public class SubscriptionTriggerMatcher {
	private static final Logger ourLog = Logs.getSubscriptionTopicLog();

	private final SubscriptionTopicSupport mySubscriptionTopicSupport;
	private final BaseResourceMessage.OperationTypeEnum myOperation;
	private final SubscriptionTopic.SubscriptionTopicResourceTriggerComponent myTrigger;
	private final String myResourceName;
	private final IBaseResource myResource;
	private final IFhirResourceDao myDao;
	private final PreviousVersionReader myPreviousVersionReader;
	private final SystemRequestDetails mySrd;

	public SubscriptionTriggerMatcher(
			SubscriptionTopicSupport theSubscriptionTopicSupport,
			ResourceModifiedMessage theMsg,
			SubscriptionTopic.SubscriptionTopicResourceTriggerComponent theTrigger) {
		mySubscriptionTopicSupport = theSubscriptionTopicSupport;
		myOperation = theMsg.getOperationType();
		myResource = theMsg.getPayload(theSubscriptionTopicSupport.getFhirContext());
		myResourceName = myResource.fhirType();
		myDao = mySubscriptionTopicSupport.getDaoRegistry().getResourceDao(myResourceName);
		myTrigger = theTrigger;
		myPreviousVersionReader = new PreviousVersionReader(myDao);
		mySrd = new SystemRequestDetails();
	}

	public InMemoryMatchResult match() {
		List<Enumeration<SubscriptionTopic.InteractionTrigger>> supportedInteractions =
				myTrigger.getSupportedInteraction();
		if (SubscriptionTopicUtil.matches(myOperation, supportedInteractions)) {
			SubscriptionTopic.SubscriptionTopicResourceTriggerQueryCriteriaComponent queryCriteria =
					myTrigger.getQueryCriteria();
			InMemoryMatchResult result = match(queryCriteria);
			if (result.matched()) {
				return result;
			}
		}
		return InMemoryMatchResult.noMatch();
	}

	private InMemoryMatchResult match(
			SubscriptionTopic.SubscriptionTopicResourceTriggerQueryCriteriaComponent theQueryCriteria) {
		String previousCriteria = theQueryCriteria.getPrevious();
		String currentCriteria = theQueryCriteria.getCurrent();
		InMemoryMatchResult previousMatches = InMemoryMatchResult.fromBoolean(previousCriteria == null);
		InMemoryMatchResult currentMatches = InMemoryMatchResult.fromBoolean(currentCriteria == null);

		// WIP STR5 implement fhirPathCriteria per https://build.fhir.org/subscriptiontopic.html#fhirpath-criteria
		if (currentCriteria != null) {
			currentMatches = matchResource(myResource, currentCriteria);
		}
		if (myOperation == ResourceModifiedMessage.OperationTypeEnum.CREATE) {
			return currentMatches;
		}

		if (previousCriteria != null) {
			if (myOperation == ResourceModifiedMessage.OperationTypeEnum.UPDATE
					|| myOperation == ResourceModifiedMessage.OperationTypeEnum.DELETE) {

				Optional<IBaseResource> oPreviousVersion = myPreviousVersionReader.readPreviousVersion(myResource);
				if (oPreviousVersion.isPresent()) {
					previousMatches = matchResource(oPreviousVersion.get(), previousCriteria);
				} else {
					ourLog.warn(
							"Resource {} has a version of 1, which should not be the case for a create or delete operation",
							myResource.getIdElement().toUnqualifiedVersionless());
				}
			}
		}
		// WIP STR5 implement resultForCreate and resultForDelete
		if (theQueryCriteria.getRequireBoth()) {
			return InMemoryMatchResult.and(previousMatches, currentMatches);
		} else {
			return InMemoryMatchResult.or(previousMatches, currentMatches);
		}
	}

	private InMemoryMatchResult matchResource(IBaseResource theResource, String theCriteria) {
		InMemoryMatchResult result =
				mySubscriptionTopicSupport.getSearchParamMatcher().match(theCriteria, theResource, mySrd);
		if (!result.supported()) {
			ourLog.warn(
					"Subscription topic {} has a query criteria that is not supported in-memory: {}",
					myTrigger.getId(),
					theCriteria);
		}
		return result;
	}
}
