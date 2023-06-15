package ca.uhn.fhir.jpa.topic.filter;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.subscription.model.CanonicalTopicSubscriptionFilter;

public interface ISubscriptionTopicFilterMatcher {
    /**
     * Match a resource against a single subscription topic filter
     *
     * @param theCanonicalTopicSubscriptionFilter
     * @param theIBaseResource
     * @return
     */
    InMemoryMatchResult match(
            CanonicalTopicSubscriptionFilter theCanonicalTopicSubscriptionFilter,
            IBaseResource theIBaseResource);
}
