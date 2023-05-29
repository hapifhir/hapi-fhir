package ca.uhn.fhir.jpa.topic.filter;

import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.searchparam.matcher.SearchParamMatcher;
import ca.uhn.fhir.jpa.subscription.model.CanonicalTopicSubscriptionFilter;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;

public class InMemoryTopicFilterMatcher implements ISubscriptionTopicFilterMatcher {
	private final SearchParamMatcher mySearchParamMatcher;

	public InMemoryTopicFilterMatcher(SearchParamMatcher theSearchParamMatcher) {
		mySearchParamMatcher = theSearchParamMatcher;
	}

	@Override
	public InMemoryMatchResult match(CanonicalTopicSubscriptionFilter theCanonicalTopicSubscriptionFilter, IBaseResource theResource) {
		return mySearchParamMatcher.match(theCanonicalTopicSubscriptionFilter.asCriteriaString(), theResource, new SystemRequestDetails());
	}
}
