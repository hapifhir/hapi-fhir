package ca.uhn.fhir.jpa.topic.status;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.Reference;
import org.hl7.fhir.r5.model.SubscriptionStatus;

import java.util.List;
import java.util.UUID;

public class R5NotificationStatusBuilder implements INotificationStatusBuilder<SubscriptionStatus> {
	private final FhirContext myFhirContext;

	public R5NotificationStatusBuilder(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	@Override
	public SubscriptionStatus buildNotificationStatus(List<IBaseResource> theResources, ActiveSubscription theActiveSubscription, String theTopicUrl) {
		long eventNumber = theActiveSubscription.getDeliveriesCount();

		SubscriptionStatus subscriptionStatus = new SubscriptionStatus();
		subscriptionStatus.setId(UUID.randomUUID().toString());
		subscriptionStatus.setStatus(Enumerations.SubscriptionStatusCodes.ACTIVE);
		subscriptionStatus.setType(SubscriptionStatus.SubscriptionNotificationType.EVENTNOTIFICATION);
		// WIP STR5 events-since-subscription-start should be read from the database
		subscriptionStatus.setEventsSinceSubscriptionStart(eventNumber);
		SubscriptionStatus.SubscriptionStatusNotificationEventComponent event = subscriptionStatus.addNotificationEvent();
		event.setEventNumber(eventNumber);
		if (theResources.size() > 0) {
			event.setFocus(new Reference(theResources.get(0).getIdElement()));
		}
		subscriptionStatus.setSubscription(new Reference(theActiveSubscription.getSubscription().getIdElement(myFhirContext)));
		subscriptionStatus.setTopic(theTopicUrl);
		return subscriptionStatus;
	}
}
