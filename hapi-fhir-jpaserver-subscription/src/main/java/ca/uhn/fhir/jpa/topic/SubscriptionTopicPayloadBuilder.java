package ca.uhn.fhir.jpa.topic;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.util.BundleBuilder;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.Reference;
import org.hl7.fhir.r5.model.SubscriptionStatus;
import org.hl7.fhir.r5.model.SubscriptionTopic;

public class SubscriptionTopicPayloadBuilder {
	private final FhirContext myFhirContext;

	public SubscriptionTopicPayloadBuilder(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	public IBaseBundle buildPayload(IBaseResource theMatchedResource, ResourceModifiedMessage theMsg, ActiveSubscription theActiveSubscription, SubscriptionTopic theTopic) {
		BundleBuilder bundleBuilder = new BundleBuilder(myFhirContext);

		// WIP STR5 set eventsSinceSubscriptionStart from the database
		int eventsSinceSubscriptionStart = 1;
		IBaseResource subscriptionStatus = buildSubscriptionStatus(theMatchedResource, theActiveSubscription, theTopic, eventsSinceSubscriptionStart);

		FhirVersionEnum fhirVersion = myFhirContext.getVersion().getVersion();

		// WIP STR5 add support for notificationShape include, revinclude

		if (fhirVersion == FhirVersionEnum.R4B) {
			bundleBuilder.setType(Bundle.BundleType.HISTORY.toCode());
			String serializedSubscriptionStatus = FhirContext.forR5Cached().newJsonParser().encodeResourceToString(subscriptionStatus);
			subscriptionStatus = myFhirContext.newJsonParser().parseResource(org.hl7.fhir.r4b.model.SubscriptionStatus.class, serializedSubscriptionStatus);
			// WIP STR5 VersionConvertorFactory_43_50 when it supports SubscriptionStatus
			// track here: https://github.com/hapifhir/org.hl7.fhir.core/issues/1212
//			subscriptionStatus = (SubscriptionStatus) VersionConvertorFactory_43_50.convertResource((org.hl7.fhir.r4b.model.SubscriptionStatus) subscriptionStatus);
		} else if (fhirVersion == FhirVersionEnum.R5) {
			bundleBuilder.setType(Bundle.BundleType.SUBSCRIPTIONNOTIFICATION.toCode());
		} else {
			throw new IllegalStateException(Msg.code(2331) + "SubscriptionTopic subscriptions are not supported on FHIR version: " + fhirVersion);
		}
		// WIP STR5 is this the right type of entry? see http://hl7.org/fhir/subscriptionstatus-examples.html
		// WIP STR5 Also see http://hl7.org/fhir/R4B/notification-full-resource.json.html need to conform to these
		bundleBuilder.addCollectionEntry(subscriptionStatus);
		switch (theMsg.getOperationType()) {
			case CREATE:
				bundleBuilder.addTransactionCreateEntry(theMatchedResource);
				break;
			case UPDATE:
				bundleBuilder.addTransactionUpdateEntry(theMatchedResource);
				break;
			case DELETE:
				bundleBuilder.addTransactionDeleteEntry(theMatchedResource);
				break;
		}
		return bundleBuilder.getBundle();
	}

	private SubscriptionStatus buildSubscriptionStatus(IBaseResource theMatchedResource, ActiveSubscription theActiveSubscription, SubscriptionTopic theTopic, int theEventsSinceSubscriptionStart) {
		SubscriptionStatus subscriptionStatus = new SubscriptionStatus();
		subscriptionStatus.setStatus(Enumerations.SubscriptionStatusCodes.ACTIVE);
		subscriptionStatus.setType(SubscriptionStatus.SubscriptionNotificationType.EVENTNOTIFICATION);
		// WIP STR5 count events since subscription start and set eventsSinceSubscriptionStart
		// store counts by subscription id
		subscriptionStatus.setEventsSinceSubscriptionStart(theEventsSinceSubscriptionStart);
		subscriptionStatus.addNotificationEvent().setEventNumber(theEventsSinceSubscriptionStart).setFocus(new Reference(theMatchedResource.getIdElement()));
		subscriptionStatus.setSubscription(new Reference(theActiveSubscription.getSubscription().getIdElement(myFhirContext)));
		subscriptionStatus.setTopic(theTopic.getUrl());
		return subscriptionStatus;
	}
}
