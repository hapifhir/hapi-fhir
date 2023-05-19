package ca.uhn.fhir.jpa.topic.filter;

import ca.uhn.fhir.jpa.subscription.model.CanonicalTopicSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalTopicSubscriptionFilter;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nonnull;

public final class SubscriptionTopicFilterUtil {
	private SubscriptionTopicFilterUtil() {
	}

	public static boolean matchFilters(@Nonnull IBaseResource theResource, @Nonnull String theResourceType, @Nonnull ISubscriptionTopicFilterMatcher theSubscriptionTopicFilterMatcher, @Nonnull CanonicalTopicSubscription topicSubscription) {
		boolean match = true;
			for (CanonicalTopicSubscriptionFilter filter : topicSubscription.getFilters()) {
				if (filter.getResourceType() == null || !filter.getResourceType().equals(theResourceType)) {
					continue;
				}
				if (!theSubscriptionTopicFilterMatcher.match(filter, theResource).matched()) {
					match = false;
					break;
				}
		}
		return match;
	}
}
