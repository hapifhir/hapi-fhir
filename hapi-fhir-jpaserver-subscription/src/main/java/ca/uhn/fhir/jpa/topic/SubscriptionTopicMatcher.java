package ca.uhn.fhir.jpa.topic;

import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4b.model.SubscriptionTopic;

import java.util.List;

public class SubscriptionTopicMatcher {
	private final SubscriptionTopicSupport mySubscriptionTopicSupport;
	private final SubscriptionTopic myTopic;

	public SubscriptionTopicMatcher(SubscriptionTopicSupport theSubscriptionTopicSupport, SubscriptionTopic theTopic) {
		mySubscriptionTopicSupport = theSubscriptionTopicSupport;
		myTopic = theTopic;
	}

	public boolean matches(ResourceModifiedMessage theMsg) {
		IBaseResource resource = theMsg.getPayload(mySubscriptionTopicSupport.getFhirContext());
		String resourceName = resource.fhirType();

		List<SubscriptionTopic.SubscriptionTopicResourceTriggerComponent> triggers = myTopic.getResourceTrigger();
		for (SubscriptionTopic.SubscriptionTopicResourceTriggerComponent next : triggers) {
			if (resourceName.equals(next.getResource())) {
				SubscriptionTriggerMatcher matcher = new SubscriptionTriggerMatcher(mySubscriptionTopicSupport, theMsg, next);
				if (matcher.matches()) {
					// FIXME KHS should we check the other triggers?
					return true;
				}
			}
		}
		// TODO KHS add support for event triggers
		return false;
	}
}
