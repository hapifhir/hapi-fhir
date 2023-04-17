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
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.SubscriptionTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubscriptionTopicValidatingInterceptor {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionTopicValidatingInterceptor.class);
	private final FhirContext myFhirContext;
	private final SubscriptionQueryValidator mySubscriptionQueryValidator;

	public SubscriptionTopicValidatingInterceptor(FhirContext theFhirContext, SubscriptionQueryValidator theSubscriptionQueryValidator) {
		myFhirContext = theFhirContext;
		mySubscriptionQueryValidator = theSubscriptionQueryValidator;
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED)
	public void resourcePreCreate(IBaseResource theResource, RequestDetails theRequestDetails, RequestPartitionId theRequestPartitionId) {
		validateSubmittedSubscriptionTopic(theResource, theRequestDetails, theRequestPartitionId, Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED);
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED)
	public void resourceUpdated(IBaseResource theOldResource, IBaseResource theResource, RequestDetails theRequestDetails, RequestPartitionId theRequestPartitionId) {
		validateSubmittedSubscriptionTopic(theResource, theRequestDetails, theRequestPartitionId, Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED);
	}

	@VisibleForTesting
	void validateSubmittedSubscriptionTopic(IBaseResource theSubscription,
														 RequestDetails theRequestDetails,
														 RequestPartitionId theRequestPartitionId,
														 Pointcut thePointcut) {
		if (Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED != thePointcut && Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED != thePointcut) {
			throw new UnprocessableEntityException(Msg.code(2267) + "Expected Pointcut to be either STORAGE_PRESTORAGE_RESOURCE_CREATED or STORAGE_PRESTORAGE_RESOURCE_UPDATED but was: " + thePointcut);
		}

		if (!"SubscriptionTopic".equals(myFhirContext.getResourceType(theSubscription))) {
			return;
		}

		SubscriptionTopic subscriptionTopic = SubscriptionTopicCanonicalizer.canonicalize(myFhirContext, theSubscription);

		boolean finished = false;
		if (subscriptionTopic.getStatus() == null) {
			// FIXME code
			throw new UnprocessableEntityException("Can not process submitted SubscriptionTopic - SubscriptionTopic.status must be populated on this server");
		}

		switch (subscriptionTopic.getStatus()) {
			case ACTIVE:
				break;
			default:
				finished = true;
				break;
		}

		// WIP STR5 add cross-partition support like in SubscriptionValidatingInterceptor

		// WIP STR5 warn if can't be evaluated in memory?

		if (!finished) {
			subscriptionTopic.getResourceTrigger().stream()
				.forEach(t -> validateQueryCriteria(t.getQueryCriteria()));
		}
	}

	private void validateQueryCriteria(SubscriptionTopic.SubscriptionTopicResourceTriggerQueryCriteriaComponent theQueryCriteria) {
		if (theQueryCriteria.getPrevious() != null) {
			validateCriteria(theQueryCriteria.getPrevious(), "SubscriptionTopic.resourceTrigger.queryCriteria.previous");
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
				ourLog.warn("Warning: Query Criteria '{}' in {} cannot be evaluated in-memory", theCriteria, theFieldName);
			}
		} catch (InvalidRequestException | DataFormatException e) {
			// FIXME code
			throw new UnprocessableEntityException("Invalid SubscriptionTopic criteria '" + theCriteria + "' in " + theFieldName + ": " + e.getMessage());
		}
	}
}
