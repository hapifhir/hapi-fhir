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

import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.fhirpath.IFhirPathEvaluationContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.messaging.BaseResourceMessage;
import ca.uhn.fhir.storage.PreviousVersionReader;
import ca.uhn.fhir.util.Logs;
import com.google.common.base.Strings;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.BooleanType;
import org.hl7.fhir.r5.model.Enumeration;
import org.hl7.fhir.r5.model.SubscriptionTopic;
import org.slf4j.Logger;
import org.slf4j.helpers.MessageFormatter;

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
	private final MemoryCacheService myMemoryCacheService;

	public SubscriptionTriggerMatcher(
			SubscriptionTopicSupport theSubscriptionTopicSupport,
			ResourceModifiedMessage theMsg,
			SubscriptionTopic.SubscriptionTopicResourceTriggerComponent theTrigger,
			MemoryCacheService theMemoryCacheService) {
		mySubscriptionTopicSupport = theSubscriptionTopicSupport;
		myOperation = theMsg.getOperationType();
		myResource = theMsg.getPayload(theSubscriptionTopicSupport.getFhirContext());
		myResourceName = myResource.fhirType();
		myDao = mySubscriptionTopicSupport.getDaoRegistry().getResourceDao(myResourceName);
		myTrigger = theTrigger;
		myPreviousVersionReader = new PreviousVersionReader(myDao);
		mySrd = new SystemRequestDetails();
		myMemoryCacheService = theMemoryCacheService;
	}

	public InMemoryMatchResult match() {
		List<Enumeration<SubscriptionTopic.InteractionTrigger>> supportedInteractions =
				myTrigger.getSupportedInteraction();
		if (SubscriptionTopicUtil.matches(myOperation, supportedInteractions)) {
			SubscriptionTopic.SubscriptionTopicResourceTriggerQueryCriteriaComponent queryCriteria =
					myTrigger.getQueryCriteria();
			String fhirPathCriteria = myTrigger.getFhirPathCriteria();
			return match(queryCriteria, fhirPathCriteria);
		}
		return InMemoryMatchResult.noMatch();
	}

	private InMemoryMatchResult match(
			SubscriptionTopic.SubscriptionTopicResourceTriggerQueryCriteriaComponent theQueryCriteria,
			String theFhirPathCriteria) {
		String previousCriteria = theQueryCriteria.getPrevious();
		String currentCriteria = theQueryCriteria.getCurrent();
		InMemoryMatchResult previousMatches = InMemoryMatchResult.fromBoolean(previousCriteria == null);
		InMemoryMatchResult currentMatches = InMemoryMatchResult.fromBoolean(currentCriteria == null);

		InMemoryMatchResult fhirPathCriteriaEvaluationResult = evaluateFhirPathCriteria(theFhirPathCriteria);

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
			return InMemoryMatchResult.and(
					InMemoryMatchResult.and(previousMatches, currentMatches), fhirPathCriteriaEvaluationResult);
		} else {
			return InMemoryMatchResult.and(
					InMemoryMatchResult.or(previousMatches, currentMatches), fhirPathCriteriaEvaluationResult);
		}
	}

	private InMemoryMatchResult evaluateFhirPathCriteria(String theFhirPathCriteria) {
		if (!Strings.isNullOrEmpty(theFhirPathCriteria)) {
			IFhirPath fhirPathEngine =
					mySubscriptionTopicSupport.getFhirContext().newFhirPath();
			fhirPathEngine.setEvaluationContext(new IFhirPathEvaluationContext() {

				@Override
				public List<IBase> resolveConstant(Object appContext, String name, boolean beforeContext) {
					if ("current".equalsIgnoreCase(name)) return List.of(myResource);

					if ("previous".equalsIgnoreCase(name)) {
						Optional previousResource = myPreviousVersionReader.readPreviousVersion(myResource);
						if (previousResource.isPresent()) return List.of((IBase) previousResource.get());
					}

					return null;
				}
			});
			try {
				IFhirPath.IParsedExpression expression = myMemoryCacheService.get(
						MemoryCacheService.CacheEnum.FHIRPATH_EXPRESSION, theFhirPathCriteria, exp -> {
							try {
								return fhirPathEngine.parse(exp);
							} catch (FHIRException e) {
								throw e;
							} catch (Exception e) {
								throw new RuntimeException(Msg.code(2534) + e.getMessage(), e);
							}
						});

				List<IBase> result = fhirPathEngine.evaluate(myResource, expression, IBase.class);

				return parseResult(theFhirPathCriteria, result);

			} catch (FHIRException fhirException) {
				ourLog.warn(
						"Subscription topic {} has a fhirPathCriteria that is not valid: {}",
						myTrigger.getId(),
						theFhirPathCriteria,
						fhirException);
				return InMemoryMatchResult.unsupportedFromReason(fhirException.getMessage());
			}
		}
		return InMemoryMatchResult.fromBoolean(true);
	}

	private InMemoryMatchResult parseResult(String theFhirPathCriteria, List<IBase> result) {
		if (result == null) {
			return InMemoryMatchResult.unsupportedFromReason(MessageFormatter.format(
							"FhirPath evaluation criteria '{}' from Subscription topic: '{}' resulted in null results.",
							theFhirPathCriteria,
							myTrigger.getId())
					.getMessage());
		}

		if (result.size() != 1) {
			return InMemoryMatchResult.unsupportedFromReason(MessageFormatter.arrayFormat(
							"FhirPath evaluation criteria '{}' from Subscription topic: '{}' resulted in '{}' results. Expected 1.",
							new String[] {theFhirPathCriteria, myTrigger.getId(), String.valueOf(result.size())})
					.getMessage());
		}

		if (!(result.get(0) instanceof BooleanType)) {
			return InMemoryMatchResult.unsupportedFromReason(MessageFormatter.arrayFormat(
							"FhirPath evaluation criteria '{}' from Subscription topic: '{}' resulted in a non-boolean result: '{}'",
							new String[] {
								theFhirPathCriteria,
								myTrigger.getId(),
								result.get(0).getClass().getName()
							})
					.getMessage());
		}
		return InMemoryMatchResult.fromBoolean(((BooleanType) result.get(0)).booleanValue());
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
