package ca.uhn.fhir.jpa.topic.status;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_43_50;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4b.model.SubscriptionStatus;

import java.util.List;

public class R4BNotificationStatusBuilder implements INotificationStatusBuilder<SubscriptionStatus> {
	private final R5NotificationStatusBuilder myR5NotificationStatusBuilder;

	public R4BNotificationStatusBuilder(FhirContext theFhirContext) {
		myR5NotificationStatusBuilder = new R5NotificationStatusBuilder(theFhirContext);
	}

	@Override
	public SubscriptionStatus buildNotificationStatus(List<IBaseResource> theResources, ActiveSubscription theActiveSubscription, String theTopicUrl) {
		org.hl7.fhir.r5.model.SubscriptionStatus subscriptionStatus = myR5NotificationStatusBuilder.buildNotificationStatus(theResources, theActiveSubscription, theTopicUrl);
		return (SubscriptionStatus) VersionConvertorFactory_43_50.convertResource(subscriptionStatus);
	}
}
