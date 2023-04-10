package ca.uhn.fhir.jpa.topic;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.subscription.match.matcher.subscriber.SubscriptionMatchDeliverer;
import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionRegistry;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.util.BundleBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.Reference;
import org.hl7.fhir.r5.model.SubscriptionStatus;
import org.hl7.fhir.r5.model.SubscriptionTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

public class SubscriptionTopicMatchingSubscriber implements MessageHandler {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionTopicMatchingSubscriber.class);

	@Autowired
	FhirContext myFhirContext;
	@Autowired
	SubscriptionTopicSupport mySubscriptionTopicSupport;
	@Autowired
	SubscriptionTopicRegistry mySubscriptionTopicRegistry;
	@Autowired
	SubscriptionRegistry mySubscriptionRegistry;
	@Autowired
	SubscriptionMatchDeliverer mySubscriptionMatchDeliverer;

	@Override
	public void handleMessage(@Nonnull Message<?> theMessage) throws MessagingException {
		ourLog.trace("Handling resource modified message: {}", theMessage);

		if (!(theMessage instanceof ResourceModifiedJsonMessage)) {
			ourLog.warn("Unexpected message payload type: {}", theMessage);
			return;
		}

		ResourceModifiedMessage msg = ((ResourceModifiedJsonMessage) theMessage).getPayload();
		matchActiveSubscriptionTopicsAndDeliver(msg);
	}

	private void matchActiveSubscriptionTopicsAndDeliver(ResourceModifiedMessage theMsg) {

		Collection<SubscriptionTopic> topics = mySubscriptionTopicRegistry.getAll();
		for (SubscriptionTopic topic : topics) {
			SubscriptionTopicMatcher matcher = new SubscriptionTopicMatcher(mySubscriptionTopicSupport, topic);
			InMemoryMatchResult result = matcher.match(theMsg);
			IBaseResource matchedResource = theMsg.getNewPayload(myFhirContext);
			if (result.matched()) {
				ourLog.info("Matched topic {} to message {}", topic.getIdElement().toUnqualifiedVersionless(), theMsg);
				// WIP STR5 deliver a topic match bundle per http://hl7.org/fhir/uv/subscriptions-backport/STU1.1/notifications.html
				List<ActiveSubscription> topicSubscriptions = mySubscriptionRegistry.getTopicSubscriptionsForUrl(topic.getUrl());
				for (ActiveSubscription activeSubscription : topicSubscriptions) {
					IBaseResource payload = getPayload(matchedResource, theMsg, activeSubscription, topic);
					mySubscriptionMatchDeliverer.deliverPayload(payload, theMsg, activeSubscription, result);
				}
			}
		}
	}

	private IBaseResource getPayload(IBaseResource theMatchedResource, ResourceModifiedMessage theMsg, ActiveSubscription theActiveSubscription, SubscriptionTopic theTopic) {
		BundleBuilder bundleBuilder = new BundleBuilder(myFhirContext);

		// WIP STR5 set eventsSinceSubscriptionStart from the database
		int eventsSinceSubscriptionStart = 1;
		IBaseResource subscriptionStatus = buildSubscriptionStatus(theMatchedResource, theActiveSubscription, theTopic, eventsSinceSubscriptionStart);

		FhirVersionEnum fhirVersion = myFhirContext.getVersion().getVersion();

		if (fhirVersion == FhirVersionEnum.R4B) {
			bundleBuilder.setType(Bundle.BundleType.HISTORY.toCode());
			String serializedSubscriptionStatus = FhirContext.forR5Cached().newJsonParser().encodeResourceToString(subscriptionStatus);
			subscriptionStatus = myFhirContext.newJsonParser().parseResource(org.hl7.fhir.r4b.model.SubscriptionStatus.class, serializedSubscriptionStatus);
			// WIP STR5 VersionConvertorFactory_43_50 when it supports SubscriptionStatus
//			subscriptionStatus = (SubscriptionStatus) VersionConvertorFactory_43_50.convertResource((org.hl7.fhir.r4b.model.SubscriptionStatus) subscriptionStatus);
		} else if (fhirVersion == FhirVersionEnum.R5) {
			bundleBuilder.setType(Bundle.BundleType.SUBSCRIPTIONNOTIFICATION.toCode());
		} else {
			throw new IllegalStateException(Msg.code(2331) + "SubscriptionTopic subscriptions are not supported on FHIR version: " + fhirVersion);
		}
		// WIP STR5 is this the right type of entry?
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
		subscriptionStatus.setEventsSinceSubscriptionStart(theEventsSinceSubscriptionStart);
		subscriptionStatus.addNotificationEvent().setEventNumber(theEventsSinceSubscriptionStart).setFocus(new Reference(theMatchedResource.getIdElement()));
		subscriptionStatus.setSubscription(new Reference(theActiveSubscription.getSubscription().getIdElement(myFhirContext)));
		subscriptionStatus.setTopic(theTopic.getUrl());
		return subscriptionStatus;
	}
}
