package ca.uhn.fhir.jpa.topic;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import ca.uhn.fhir.subscription.SubscriptionConstants;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Subscription;
import org.hl7.fhir.r5.model.SubscriptionStatus;

import javax.annotation.Nonnull;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class R4NotificationStatusBuilder {
	private static FhirContext ourFhirContext = null;

	public static Parameters buildNotificationStatus(List<IBaseResource> theResources, ActiveSubscription theActiveSubscription, String theTopicUrl, @Nonnull Integer theEventsSinceSubscriptionStart) {
		if (ourFhirContext == null) {
			// Lazy load for non R4 context
			ourFhirContext = FhirContext.forR4();
		}

		// See http://build.fhir.org/ig/HL7/fhir-subscription-backport-ig/Parameters-r4-notification-status.json.html
		Parameters parameters = new Parameters();
		parameters.getMeta().addProfile(SubscriptionConstants.SUBSCRIPTION_TOPIC_STATUS);
		parameters.setId(UUID.randomUUID().toString());
		parameters.addParameter("subscription", new Reference(theActiveSubscription.getSubscription().getIdElement(ourFhirContext)));
		parameters.addParameter("status", new CodeType(Subscription.SubscriptionStatus.ACTIVE.toCode()));
		parameters.addParameter("type", new CodeType(SubscriptionStatus.SubscriptionNotificationType.EVENTNOTIFICATION.toCode()));
		parameters.addParameter("topic", new CanonicalType(theTopicUrl));
		// WIP STR5 count events since subscription start and set eventsSinceSubscriptionStart. store counts by subscription id
		parameters.addParameter("events-since-subscription-start", theEventsSinceSubscriptionStart.toString());
		Parameters.ParametersParameterComponent notificationEvent = parameters.addParameter();
		notificationEvent.setName("notification-event");
		notificationEvent.addPart().setName("event-number").setValue(new StringType(theEventsSinceSubscriptionStart.toString()));
		notificationEvent.addPart().setName("timestamp").setValue(new DateType(new Date()));

		return parameters;
	}
}
