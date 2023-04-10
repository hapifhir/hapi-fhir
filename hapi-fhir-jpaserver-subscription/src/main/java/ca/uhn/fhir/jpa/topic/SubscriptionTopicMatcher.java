package ca.uhn.fhir.jpa.topic;

import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.SubscriptionTopic;

import java.util.List;

public class SubscriptionTopicMatcher {
	private final SubscriptionTopicSupport mySubscriptionTopicSupport;
	private final SubscriptionTopic myTopic;

	public SubscriptionTopicMatcher(SubscriptionTopicSupport theSubscriptionTopicSupport, SubscriptionTopic theTopic) {
		mySubscriptionTopicSupport = theSubscriptionTopicSupport;
		myTopic = theTopic;
	}

	public InMemoryMatchResult match(ResourceModifiedMessage theMsg) {
		IBaseResource resource = theMsg.getPayload(mySubscriptionTopicSupport.getFhirContext());
		String resourceName = resource.fhirType();

		List<SubscriptionTopic.SubscriptionTopicResourceTriggerComponent> triggers = myTopic.getResourceTrigger();
		for (SubscriptionTopic.SubscriptionTopicResourceTriggerComponent next : triggers) {
			if (resourceName.equals(next.getResource())) {
				SubscriptionTriggerMatcher matcher = new SubscriptionTriggerMatcher(mySubscriptionTopicSupport, theMsg, next);
				InMemoryMatchResult result = matcher.match();
				if (result.matched()) {
					return result;
				}
				// WIP STR5 should we check the other triggers?
			}
		}
		// WIP STR5 add support for event triggers
		return InMemoryMatchResult.noMatch();
	}
}
