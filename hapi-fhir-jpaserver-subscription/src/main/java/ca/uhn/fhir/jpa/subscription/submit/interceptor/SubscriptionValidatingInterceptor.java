package ca.uhn.fhir.jpa.subscription.submit.interceptor;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.SubscriptionMatchingStrategy;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.SubscriptionStrategyEvaluator;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionCanonicalizer;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscriptionChannelType;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.HapiExtensions;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.net.URISyntaxException;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Interceptor
public class SubscriptionValidatingInterceptor {

	@Autowired
	private SubscriptionCanonicalizer mySubscriptionCanonicalizer;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private SubscriptionStrategyEvaluator mySubscriptionStrategyEvaluator;
	@Autowired
	private FhirContext myFhirContext;

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED)
	public void resourcePreCreate(IBaseResource theResource) {
		validateSubmittedSubscription(theResource);
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED)
	public void resourcePreCreate(IBaseResource theOldResource, IBaseResource theResource) {
		validateSubmittedSubscription(theResource);
	}

	@VisibleForTesting
	public void setFhirContextForUnitTest(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	public void validateSubmittedSubscription(IBaseResource theSubscription) {
		if (!"Subscription".equals(myFhirContext.getResourceType(theSubscription))) {
			return;
		}

		CanonicalSubscription subscription = mySubscriptionCanonicalizer.canonicalize(theSubscription);
		boolean finished = false;
		if (subscription.getStatus() == null) {
			throw new UnprocessableEntityException("Can not process submitted Subscription - Subscription.status must be populated on this server");
		}

		switch (subscription.getStatus()) {
			case REQUESTED:
			case ACTIVE:
				break;
			case ERROR:
			case OFF:
			case NULL:
				finished = true;
				break;
		}

		mySubscriptionCanonicalizer.setMatchingStrategyTag(theSubscription, null);

		if (!finished) {

			validateQuery(subscription.getCriteriaString(), "Subscription.criteria");

			if (subscription.getPayloadSearchCriteria() != null) {
				validateQuery(subscription.getPayloadSearchCriteria(), "Subscription.extension(url='" + HapiExtensions.EXT_SUBSCRIPTION_PAYLOAD_SEARCH_CRITERIA + "')");
			}

			validateChannelType(subscription);

			try {
				SubscriptionMatchingStrategy strategy = mySubscriptionStrategyEvaluator.determineStrategy(subscription.getCriteriaString());
				mySubscriptionCanonicalizer.setMatchingStrategyTag(theSubscription, strategy);
			} catch (InvalidRequestException | DataFormatException e) {
				throw new UnprocessableEntityException("Invalid subscription criteria submitted: " + subscription.getCriteriaString() + " " + e.getMessage());
			}

			if (subscription.getChannelType() == null) {
				throw new UnprocessableEntityException("Subscription.channel.type must be populated on this server");
			} else if (subscription.getChannelType() == CanonicalSubscriptionChannelType.MESSAGE) {
				validateMessageSubscriptionEndpoint(subscription.getEndpointUrl());
			}


		}
	}

	public void validateQuery(String theQuery, String theFieldName) {
		if (isBlank(theQuery)) {
			throw new UnprocessableEntityException(theFieldName + " must be populated");
		}

		int sep = theQuery.indexOf('?');
		if (sep <= 1) {
			throw new UnprocessableEntityException(theFieldName + " must be in the form \"{Resource Type}?[params]\"");
		}

		String resType = theQuery.substring(0, sep);
		if (resType.contains("/")) {
			throw new UnprocessableEntityException(theFieldName + " must be in the form \"{Resource Type}?[params]\"");
		}

		if (!myDaoRegistry.isResourceTypeSupported(resType)) {
			throw new UnprocessableEntityException(theFieldName + " contains invalid/unsupported resource type: " + resType);
		}
	}

	public void validateMessageSubscriptionEndpoint(String theEndpointUrl) {
		if (theEndpointUrl == null) {
			throw new UnprocessableEntityException("No endpoint defined for message subscription");
		}

		try {
			URI uri = new URI(theEndpointUrl);

			if (!"channel".equals(uri.getScheme())) {
				throw new UnprocessableEntityException("Only 'channel' protocol is supported for Subscriptions with channel type 'message'");
			}
			String channelName = uri.getSchemeSpecificPart();
			if (isBlank(channelName)) {
				throw new UnprocessableEntityException("A channel name must appear after channel: in a message Subscription endpoint");
			}
		} catch (URISyntaxException e) {
			throw new UnprocessableEntityException("Invalid subscription endpoint uri " + theEndpointUrl, e);
		}
	}

	@SuppressWarnings("WeakerAccess")
	protected void validateChannelType(CanonicalSubscription theSubscription) {
		if (theSubscription.getChannelType() == null) {
			throw new UnprocessableEntityException("Subscription.channel.type must be populated");
		} else if (theSubscription.getChannelType() == CanonicalSubscriptionChannelType.RESTHOOK) {
			validateChannelPayload(theSubscription);
			validateChannelEndpoint(theSubscription);
		}
	}

	@SuppressWarnings("WeakerAccess")
	protected void validateChannelEndpoint(CanonicalSubscription theResource) {
		if (isBlank(theResource.getEndpointUrl())) {
			throw new UnprocessableEntityException("Rest-hook subscriptions must have Subscription.channel.endpoint defined");
		}
	}

	@SuppressWarnings("WeakerAccess")
	protected void validateChannelPayload(CanonicalSubscription theResource) {
		if (!isBlank(theResource.getPayloadString()) && EncodingEnum.forContentType(theResource.getPayloadString()) == null) {
			throw new UnprocessableEntityException("Invalid value for Subscription.channel.payload: " + theResource.getPayloadString());
		}
	}

	@SuppressWarnings("WeakerAccess")
	@VisibleForTesting
	public void setSubscriptionCanonicalizerForUnitTest(SubscriptionCanonicalizer theSubscriptionCanonicalizer) {
		mySubscriptionCanonicalizer = theSubscriptionCanonicalizer;
	}

	@SuppressWarnings("WeakerAccess")
	@VisibleForTesting
	public void setDaoRegistryForUnitTest(DaoRegistry theDaoRegistry) {
		myDaoRegistry = theDaoRegistry;
	}


	@VisibleForTesting
	@SuppressWarnings("WeakerAccess")
	public void setSubscriptionStrategyEvaluatorForUnitTest(SubscriptionStrategyEvaluator theSubscriptionStrategyEvaluator) {
		mySubscriptionStrategyEvaluator = theSubscriptionStrategyEvaluator;
	}

}
