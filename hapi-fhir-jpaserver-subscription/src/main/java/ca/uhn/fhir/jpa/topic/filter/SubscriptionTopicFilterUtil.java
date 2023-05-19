package ca.uhn.fhir.jpa.topic.filter;

import ca.uhn.fhir.jpa.subscription.model.CanonicalTopicSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalTopicSubscriptionFilter;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;

public final class SubscriptionTopicFilterUtil {
	private SubscriptionTopicFilterUtil() {
	}

	public static boolean matchFilters(List<IBaseResource> theResources, ISubscriptionTopicFilterMatcher theSubscriptionTopicFilterMatcher, CanonicalTopicSubscription topicSubscription) {
		boolean match = true;
		if (theResources.size() > 0) {
			IBaseResource resourceToMatch = theResources.get(0);
			for (CanonicalTopicSubscriptionFilter filter : topicSubscription.getFilters()) {
				if (!theSubscriptionTopicFilterMatcher.match(filter, resourceToMatch).matched()) {
					match = false;
					break;
				}
			}
		}
		return match;
	}
}
