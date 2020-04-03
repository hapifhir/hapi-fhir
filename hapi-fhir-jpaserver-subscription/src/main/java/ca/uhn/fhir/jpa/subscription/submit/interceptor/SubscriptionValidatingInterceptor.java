package ca.uhn.fhir.jpa.subscription.submit.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscriptionChannelType;
import ca.uhn.fhir.jpa.subscription.process.matcher.matching.SubscriptionMatchingStrategy;
import ca.uhn.fhir.jpa.subscription.process.matcher.matching.SubscriptionStrategyEvaluator;
import ca.uhn.fhir.jpa.subscription.process.registry.SubscriptionCanonicalizer;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;

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

	public void validateSubmittedSubscription(IBaseResource theSubscription) {
		if (!"Subscription".equals(myFhirContext.getResourceDefinition(theSubscription).getName())) {
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

			String query = subscription.getCriteriaString();
			if (isBlank(query)) {
				throw new UnprocessableEntityException("Subscription.criteria must be populated");
			}

			int sep = query.indexOf('?');
			if (sep <= 1) {
				throw new UnprocessableEntityException("Subscription.criteria must be in the form \"{Resource Type}?[params]\"");
			}

			String resType = query.substring(0, sep);
			if (resType.contains("/")) {
				throw new UnprocessableEntityException("Subscription.criteria must be in the form \"{Resource Type}?[params]\"");
			}

			if (subscription.getChannelType() == null) {
				throw new UnprocessableEntityException("Subscription.channel.type must be populated");
			} else if (subscription.getChannelType() == CanonicalSubscriptionChannelType.RESTHOOK) {
				validateChannelPayload(subscription);
				validateChannelEndpoint(subscription);
			}

			if (!myDaoRegistry.isResourceTypeSupported(resType)) {
				throw new UnprocessableEntityException("Subscription.criteria contains invalid/unsupported resource type: " + resType);
			}

			try {
				SubscriptionMatchingStrategy strategy = mySubscriptionStrategyEvaluator.determineStrategy(query);
				mySubscriptionCanonicalizer.setMatchingStrategyTag(theSubscription, strategy);
			} catch (InvalidRequestException | DataFormatException e) {
				throw new UnprocessableEntityException("Invalid subscription criteria submitted: " + query + " " + e.getMessage());
			}

			if (subscription.getChannelType() == null) {
				throw new UnprocessableEntityException("Subscription.channel.type must be populated on this server");
			}

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
