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
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.Enumeration;
import org.hl7.fhir.r5.model.SubscriptionTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SubscriptionTriggerMatcher {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionTriggerMatcher.class);
	private final SubscriptionTopicSupport mySubscriptionTopicSupport;
	private final BaseResourceMessage.OperationTypeEnum myOperation;
	private final SubscriptionTopic.SubscriptionTopicResourceTriggerComponent myTrigger;
	private final String myResourceName;
	private final IBaseResource myResource;
	private final IFhirResourceDao myDao;
	private final SystemRequestDetails mySrd;

	public SubscriptionTriggerMatcher(SubscriptionTopicSupport theSubscriptionTopicSupport, ResourceModifiedMessage theMsg, SubscriptionTopic.SubscriptionTopicResourceTriggerComponent theTrigger) {
		mySubscriptionTopicSupport = theSubscriptionTopicSupport;
		myOperation = theMsg.getOperationType();
		myResource = theMsg.getPayload(theSubscriptionTopicSupport.getFhirContext());
		myResourceName = myResource.fhirType();
		myDao = mySubscriptionTopicSupport.getDaoRegistry().getResourceDao(myResourceName);
		myTrigger = theTrigger;
		mySrd = new SystemRequestDetails();
	}

	public InMemoryMatchResult match() {
		List<Enumeration<SubscriptionTopic.InteractionTrigger>> supportedInteractions = myTrigger.getSupportedInteraction();
		if (SubscriptionTopicUtil.matches(myOperation, supportedInteractions)) {
			SubscriptionTopic.SubscriptionTopicResourceTriggerQueryCriteriaComponent queryCriteria = myTrigger.getQueryCriteria();
			InMemoryMatchResult result = match(queryCriteria);
			if (result.matched()) {
				return result;
			}
		}
		return InMemoryMatchResult.noMatch();
	}

	private InMemoryMatchResult match(SubscriptionTopic.SubscriptionTopicResourceTriggerQueryCriteriaComponent theQueryCriteria) {
		InMemoryMatchResult previousMatches = InMemoryMatchResult.successfulMatch();
		InMemoryMatchResult currentMatches = InMemoryMatchResult.successfulMatch();
		String previousCriteria = theQueryCriteria.getPrevious();
		String currentCriteria = theQueryCriteria.getCurrent();

		if (previousCriteria != null) {
			if (myOperation == ResourceModifiedMessage.OperationTypeEnum.UPDATE ||
				myOperation == ResourceModifiedMessage.OperationTypeEnum.DELETE) {
				Long currentVersion = myResource.getIdElement().getVersionIdPartAsLong();
				if (currentVersion > 1) {
					IIdType previousVersionId = myResource.getIdElement().withVersion("" + (currentVersion - 1));
					// WIP STR5 should we use the partition id from the resource?  Ideally we should have a "previous version" service we can use for this
					IBaseResource previousVersion = myDao.read(previousVersionId, new SystemRequestDetails());
					previousMatches = matchResource(previousVersion, previousCriteria);
				} else {
					ourLog.warn("Resource {} has a version of 1, which should not be the case for a create or delete operation", myResource.getIdElement().toUnqualifiedVersionless());
				}
			}
		}
		if (currentCriteria != null) {
			currentMatches = matchResource(myResource, currentCriteria);
		}
		// WIP STR5 is this the correct interpretation of requireBoth?
		if (theQueryCriteria.getRequireBoth()) {
			return InMemoryMatchResult.and(previousMatches, currentMatches);
		} else {
			return InMemoryMatchResult.or(previousMatches, currentMatches);
		}
	}

	private InMemoryMatchResult matchResource(IBaseResource theResource, String theCriteria) {
		InMemoryMatchResult result = mySubscriptionTopicSupport.getSearchParamMatcher().match(theCriteria, theResource, mySrd);
		if (!result.supported()) {
			ourLog.warn("Subscription topic {} has a query criteria that is not supported in-memory: {}", myTrigger.getId(), theCriteria);
		}
		return result;
	}
}
