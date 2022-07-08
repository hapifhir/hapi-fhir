package ca.uhn.fhir.jpa.subscription.submit.interceptor;

/*-
 * #%L
 * HAPI FHIR Subscription Server
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
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.SubscriptionMatchingStrategy;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.SubscriptionStrategyEvaluator;
import ca.uhn.fhir.jpa.subscription.match.matcher.subscriber.SubscriptionCriteriaParser;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionCanonicalizer;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscriptionChannelType;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.HapiExtensions;
import ca.uhn.fhir.util.SubscriptionUtil;
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
	private DaoConfig myDaoConfig;
	@Autowired
	private SubscriptionStrategyEvaluator mySubscriptionStrategyEvaluator;
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private IRequestPartitionHelperSvc myRequestPartitionHelperSvc;

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED)
	public void resourcePreCreate(IBaseResource theResource, RequestDetails theRequestDetails) {
		validateSubmittedSubscription(theResource, theRequestDetails);
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED)
	public void resourcePreCreate(IBaseResource theOldResource, IBaseResource theResource, RequestDetails theRequestDetails) {
		validateSubmittedSubscription(theResource, theRequestDetails);
	}

	@VisibleForTesting
	public void setFhirContextForUnitTest(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	@Deprecated
	public void validateSubmittedSubscription(IBaseResource theSubscription) {
		validateSubmittedSubscription(theSubscription, null);
	}

	public void validateSubmittedSubscription(IBaseResource theSubscription, RequestDetails theRequestDetails) {
		if (!"Subscription".equals(myFhirContext.getResourceType(theSubscription))) {
			return;
		}

		CanonicalSubscription subscription = mySubscriptionCanonicalizer.canonicalize(theSubscription);
		boolean finished = false;
		if (subscription.getStatus() == null) {
			throw new UnprocessableEntityException(Msg.code(8) + "Can not process submitted Subscription - Subscription.status must be populated on this server");
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

		validatePermissions(theSubscription, subscription, theRequestDetails);

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
				throw new UnprocessableEntityException(Msg.code(9) + "Invalid subscription criteria submitted: " + subscription.getCriteriaString() + " " + e.getMessage());
			}

			if (subscription.getChannelType() == null) {
				throw new UnprocessableEntityException(Msg.code(10) + "Subscription.channel.type must be populated on this server");
			} else if (subscription.getChannelType() == CanonicalSubscriptionChannelType.MESSAGE) {
				validateMessageSubscriptionEndpoint(subscription.getEndpointUrl());
			}


		}
	}

	protected void validatePermissions(IBaseResource theSubscription, CanonicalSubscription theCanonicalSubscription, RequestDetails theRequestDetails) {
		// If the subscription has the cross partition tag
		if (SubscriptionUtil.isCrossPartition(theSubscription) && !(theRequestDetails instanceof SystemRequestDetails)) {
			if (!myDaoConfig.isCrossPartitionSubscription()){
				throw new UnprocessableEntityException(Msg.code(2009) + "Cross partition subscription is not enabled on this server");
			}

			if (!determinePartition(theRequestDetails, theSubscription).isDefaultPartition()) {
				throw new UnprocessableEntityException(Msg.code(2010) + "Cross partition subscription must be created on the default partition");
			}
		}
	}

	private RequestPartitionId determinePartition(RequestDetails theRequestDetails, IBaseResource theResource) {
		switch (theRequestDetails.getRestOperationType()) {
			case CREATE:
				return myRequestPartitionHelperSvc.determineCreatePartitionForRequest(theRequestDetails, theResource, "Subscription");
			case UPDATE:
				return myRequestPartitionHelperSvc.determineReadPartitionForRequestForRead(theRequestDetails, "Subscription", theResource.getIdElement());
			default:
				return null;
		}
	}

	public void validateQuery(String theQuery, String theFieldName) {
		if (isBlank(theQuery)) {
			throw new UnprocessableEntityException(Msg.code(11) + theFieldName + " must be populated");
		}

		SubscriptionCriteriaParser.SubscriptionCriteria parsedCriteria = SubscriptionCriteriaParser.parse(theQuery);
		if (parsedCriteria == null) {
			throw new UnprocessableEntityException(Msg.code(12) + theFieldName + " can not be parsed");
		}

		if (parsedCriteria.getType() == SubscriptionCriteriaParser.TypeEnum.STARTYPE_EXPRESSION) {
			return;
		}

		for (String next : parsedCriteria.getApplicableResourceTypes()) {
			if (!myDaoRegistry.isResourceTypeSupported(next)) {
				throw new UnprocessableEntityException(Msg.code(13) + theFieldName + " contains invalid/unsupported resource type: " + next);
			}
		}

		if (parsedCriteria.getType() != SubscriptionCriteriaParser.TypeEnum.SEARCH_EXPRESSION) {
			return;
		}

		int sep = theQuery.indexOf('?');
		if (sep <= 1) {
			throw new UnprocessableEntityException(Msg.code(14) + theFieldName + " must be in the form \"{Resource Type}?[params]\"");
		}

		String resType = theQuery.substring(0, sep);
		if (resType.contains("/")) {
			throw new UnprocessableEntityException(Msg.code(15) + theFieldName + " must be in the form \"{Resource Type}?[params]\"");
		}

	}

	public void validateMessageSubscriptionEndpoint(String theEndpointUrl) {
		if (theEndpointUrl == null) {
			throw new UnprocessableEntityException(Msg.code(16) + "No endpoint defined for message subscription");
		}

		try {
			URI uri = new URI(theEndpointUrl);

			if (!"channel".equals(uri.getScheme())) {
				throw new UnprocessableEntityException(Msg.code(17) + "Only 'channel' protocol is supported for Subscriptions with channel type 'message'");
			}
			String channelName = uri.getSchemeSpecificPart();
			if (isBlank(channelName)) {
				throw new UnprocessableEntityException(Msg.code(18) + "A channel name must appear after channel: in a message Subscription endpoint");
			}
		} catch (URISyntaxException e) {
			throw new UnprocessableEntityException(Msg.code(19) + "Invalid subscription endpoint uri " + theEndpointUrl, e);
		}
	}

	@SuppressWarnings("WeakerAccess")
	protected void validateChannelType(CanonicalSubscription theSubscription) {
		if (theSubscription.getChannelType() == null) {
			throw new UnprocessableEntityException(Msg.code(20) + "Subscription.channel.type must be populated");
		} else if (theSubscription.getChannelType() == CanonicalSubscriptionChannelType.RESTHOOK) {
			validateChannelPayload(theSubscription);
			validateChannelEndpoint(theSubscription);
		}
	}

	@SuppressWarnings("WeakerAccess")
	protected void validateChannelEndpoint(CanonicalSubscription theResource) {
		if (isBlank(theResource.getEndpointUrl())) {
			throw new UnprocessableEntityException(Msg.code(21) + "Rest-hook subscriptions must have Subscription.channel.endpoint defined");
		}
	}

	@SuppressWarnings("WeakerAccess")
	protected void validateChannelPayload(CanonicalSubscription theResource) {
		if (!isBlank(theResource.getPayloadString()) && EncodingEnum.forContentType(theResource.getPayloadString()) == null) {
			throw new UnprocessableEntityException(Msg.code(1985) + "Invalid value for Subscription.channel.payload: " + theResource.getPayloadString());
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
	public void setDaoConfigForUnitTest(DaoConfig theDaoConfig) {
		myDaoConfig = theDaoConfig;
	}

	@VisibleForTesting
	public void setRequestPartitionHelperSvcForUnitTest(IRequestPartitionHelperSvc theRequestPartitionHelperSvc) {
		myRequestPartitionHelperSvc = theRequestPartitionHelperSvc;
	}


	@VisibleForTesting
	@SuppressWarnings("WeakerAccess")
	public void setSubscriptionStrategyEvaluatorForUnitTest(SubscriptionStrategyEvaluator theSubscriptionStrategyEvaluator) {
		mySubscriptionStrategyEvaluator = theSubscriptionStrategyEvaluator;
	}

}
