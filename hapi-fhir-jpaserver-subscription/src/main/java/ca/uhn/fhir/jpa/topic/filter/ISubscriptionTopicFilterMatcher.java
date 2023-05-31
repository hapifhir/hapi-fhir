package ca.uhn.fhir.jpa.topic.filter;

import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.subscription.model.CanonicalTopicSubscriptionFilter;
import org.hl7.fhir.instance.model.api.IBaseResource;

public interface ISubscriptionTopicFilterMatcher {
	/**
	 * Match a resource against a single subscription topic filter
	 * @param theCanonicalTopicSubscriptionFilter
	 * @param theIBaseResource
	 * @return
	 */
	InMemoryMatchResult match(CanonicalTopicSubscriptionFilter theCanonicalTopicSubscriptionFilter, IBaseResource theIBaseResource);
}
