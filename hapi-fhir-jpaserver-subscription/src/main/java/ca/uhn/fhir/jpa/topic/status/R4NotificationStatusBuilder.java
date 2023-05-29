package ca.uhn.fhir.jpa.topic.status;

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

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class R4NotificationStatusBuilder implements INotificationStatusBuilder<Parameters> {
	private final FhirContext myFhirContext;

	public R4NotificationStatusBuilder(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	public Parameters buildNotificationStatus(List<IBaseResource> theResources, ActiveSubscription theActiveSubscription, String theTopicUrl) {
		Long eventNumber = theActiveSubscription.getDeliveriesCount();

		// See http://build.fhir.org/ig/HL7/fhir-subscription-backport-ig/Parameters-r4-notification-status.json.html
		// and http://build.fhir.org/ig/HL7/fhir-subscription-backport-ig/StructureDefinition-backport-subscription-status-r4.html
		Parameters parameters = new Parameters();
		parameters.getMeta().addProfile(SubscriptionConstants.SUBSCRIPTION_TOPIC_STATUS);
		parameters.setId(UUID.randomUUID().toString());
		parameters.addParameter("subscription", new Reference(theActiveSubscription.getSubscription().getIdElement(myFhirContext)));
		parameters.addParameter("topic", new CanonicalType(theTopicUrl));
		parameters.addParameter("status", new CodeType(Subscription.SubscriptionStatus.ACTIVE.toCode()));
		parameters.addParameter("type", new CodeType(SubscriptionStatus.SubscriptionNotificationType.EVENTNOTIFICATION.toCode()));
		// WIP STR5 events-since-subscription-start should be read from the database
		parameters.addParameter("events-since-subscription-start", eventNumber.toString());
		Parameters.ParametersParameterComponent notificationEvent = parameters.addParameter();
		notificationEvent.setName("notification-event");
		notificationEvent.addPart().setName("event-number").setValue(new StringType(eventNumber.toString()));
		notificationEvent.addPart().setName("timestamp").setValue(new DateType(new Date()));
		if (theResources.size() > 0) {
			IBaseResource firstResource = theResources.get(0);
			notificationEvent.addPart().setName("focus").setValue(new Reference(firstResource.getIdElement().toUnqualifiedVersionless()));
		}

		return parameters;
	}
}
