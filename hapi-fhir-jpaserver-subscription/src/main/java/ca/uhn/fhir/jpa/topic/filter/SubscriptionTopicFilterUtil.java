package ca.uhn.fhir.jpa.topic.filter;

import ca.uhn.fhir.jpa.subscription.model.CanonicalTopicSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalTopicSubscriptionFilter;
import ca.uhn.fhir.util.Logs;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;

import javax.annotation.Nonnull;

public final class SubscriptionTopicFilterUtil {
	private static final Logger ourLog = Logs.getSubscriptionTopicLog();
	private SubscriptionTopicFilterUtil() {
	}

	public static boolean matchFilters(@Nonnull IBaseResource theResource, @Nonnull String theResourceType, @Nonnull ISubscriptionTopicFilterMatcher theSubscriptionTopicFilterMatcher, @Nonnull CanonicalTopicSubscription topicSubscription) {
		boolean match = true;
			for (CanonicalTopicSubscriptionFilter filter : topicSubscription.getFilters()) {
				if (filter.getResourceType() == null || "Resource".equals(filter.getResourceType()) || !filter.getResourceType().equals(theResourceType)) {
					continue;
				}
				if (!theSubscriptionTopicFilterMatcher.match(filter, theResource).matched()) {
					match = false;
					ourLog.debug("Resource {} did not match filter {}.  Skipping remaining filters.", theResource.getIdElement().toUnqualifiedVersionless().getValue(), filter.asCriteriaString());
					break;
				}
				ourLog.debug("Resource {} matches filter {}", theResource.getIdElement().toUnqualifiedVersionless().getValue(), filter.asCriteriaString());
		}
		return match;
	}
}
