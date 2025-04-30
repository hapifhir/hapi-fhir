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
package ca.uhn.fhir.jpa.subscription.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.config.SubscriptionSettings;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionCanonicalizer;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscriptionChannelType;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.hapi.converters.canonical.SubscriptionTopicCanonicalizer;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseConformance;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.SubscriptionTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * This interceptor enforces various rules on Subscriptions, preventing them from being
 * registered if they don't meet the configured requirements.
 *
 * @since 8.2.0
 * @see #addAllowedCriteriaPattern(String)
 * @see #setValidateRestHookEndpointIsReachable(boolean)
 */
@Interceptor
public class SubscriptionRulesInterceptor {

	public static final String CRITERIA_WITH_AT_LEAST_ONE_PARAM = "^[A-Z][A-Za-z0-9]++\\?[a-z].*";
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionRulesInterceptor.class);
	private final List<Pattern> myAllowedCriteriaPatterns = new ArrayList<>();
	private final FhirContext myFhirContext;
	private final FhirVersionEnum myVersion;
	private final SubscriptionCanonicalizer mySubscriptionCanonicalizer;
	private boolean myValidateRestHookEndpointIsReachable;

	public SubscriptionRulesInterceptor(
		@Nonnull FhirContext theFhirContext, @Nonnull SubscriptionSettings theSubscriptionSettings,
		PartitionSettings thePartitionSettings) {
		Validate.notNull(theFhirContext, "FhirContext must not be null");
		Validate.notNull(theSubscriptionSettings, "SubscriptionSettings must not be null");

		myFhirContext = theFhirContext;
		myVersion = myFhirContext.getVersion().getVersion();
		mySubscriptionCanonicalizer = new SubscriptionCanonicalizer(myFhirContext, theSubscriptionSettings, thePartitionSettings);
	}

	/**
	 * Specifies a regular expression pattern which any Subscription (or SubscriptionTopic on R5+)
	 * must match. If more than one pattern is supplied, the pattern must match at least one
	 * pattern to be accepted, but does not need to match all of them.
	 */
	public void addAllowedCriteriaPattern(@Nonnull String theAllowedCriteriaPattern) {
		Validate.notBlank(theAllowedCriteriaPattern, "Allowed criteria pattern must not be null");
		myAllowedCriteriaPatterns.add(Pattern.compile(theAllowedCriteriaPattern));
	}

	/**
	 * If true, Subscriptions with a type of "rest-hook" will be tested to ensure that the
	 * endpoint is accessible. If it is not, the subscription will be blocked.
	 */
	public void setValidateRestHookEndpointIsReachable(boolean theValidateRestHookEndpointIsReachable) {
		myValidateRestHookEndpointIsReachable = theValidateRestHookEndpointIsReachable;
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED)
	public void validateCreate(IBaseResource theResource) {
		checkResource(theResource);
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED)
	public void validateUpdate(IBaseResource theOldResource, IBaseResource theResource) {
		checkResource(theResource);
	}

	private void checkResource(IBaseResource theResource) {

		// From R4B onward, SubscriptionTopic exists and houses the criteria
		if (myVersion.isEqualOrNewerThan(FhirVersionEnum.R4B)) {
			if ("SubscriptionTopic".equals(myFhirContext.getResourceType(theResource))) {
				validateSubscriptionTopic(theResource);
			}
		} else if (myVersion.equals(FhirVersionEnum.R4)) {
			if ("Basic".equals(myFhirContext.getResourceType(theResource))) {
				validateSubscriptionTopic(theResource);
			}
		}

		// As of R5, there is no longer a criteria defined on the Subscription itself
		if (myVersion.isOlderThan(FhirVersionEnum.R5)) {
			if ("Subscription".equals(myFhirContext.getResourceType(theResource))) {
				validateSubscription(theResource);
			}
		}
	}

	private void validateSubscriptionTopic(IBaseResource theResource) {
		SubscriptionTopic topic = SubscriptionTopicCanonicalizer.canonicalizeTopic(myFhirContext, theResource);

		if (topic != null) {
			ourLog.info("Validating SubscriptionTopic: {}", theResource.getIdElement());
			for (SubscriptionTopic.SubscriptionTopicResourceTriggerComponent resourceTrigger :
					topic.getResourceTrigger()) {
				String criteriaString = resourceTrigger.getQueryCriteria().getCurrent();
				validateCriteriaString(criteriaString);
			}
		}
	}

	private void validateSubscription(IBaseResource theResource) {
		ourLog.info("Validating Subscription: {}", theResource.getIdElement());

		CanonicalSubscription canonicalizedSubscription = mySubscriptionCanonicalizer.canonicalize(theResource);
		validateCriteriaString(canonicalizedSubscription.getCriteriaString());
		validateEndpointIsReachable(canonicalizedSubscription);
	}

	@SuppressWarnings("unchecked")
	private void validateEndpointIsReachable(CanonicalSubscription canonicalizedSubscription) {
		if (myValidateRestHookEndpointIsReachable
				&& canonicalizedSubscription.getChannelType() == CanonicalSubscriptionChannelType.RESTHOOK) {
			String endpointUrl = canonicalizedSubscription.getEndpointUrl();
			IGenericClient client = myFhirContext.newRestfulGenericClient(endpointUrl);
			Class<? extends IBaseConformance> capabilityStatement = (Class<? extends IBaseConformance>)
					myFhirContext.getResourceDefinition("CapabilityStatement").getImplementingClass();
			try {
				client.capabilities().ofType(capabilityStatement).execute();
			} catch (Exception e) {
				String message = "REST HOOK endpoint is not reachable: " + endpointUrl;
				ourLog.warn(message);
				throw new PreconditionFailedException(message);
			}
		}
	}

	private void validateCriteriaString(String theCriteriaString) {
		if (!myAllowedCriteriaPatterns.isEmpty()) {
			for (Pattern pattern : myAllowedCriteriaPatterns) {
				if (pattern.matcher(theCriteriaString).matches()) {
					return;
				}
			}
			String message = "Criteria is not permitted on this server: " + theCriteriaString;
			ourLog.warn(message);
			throw new PreconditionFailedException(message);
		}
	}
}
