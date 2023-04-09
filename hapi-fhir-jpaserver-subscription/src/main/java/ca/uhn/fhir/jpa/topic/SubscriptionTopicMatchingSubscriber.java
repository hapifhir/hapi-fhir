package ca.uhn.fhir.jpa.topic;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.subscription.match.matcher.subscriber.SubscriptionMatchDeliverer;
import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionRegistry;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.util.BundleBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4b.model.Bundle;
import org.hl7.fhir.r4b.model.Enumerations;
import org.hl7.fhir.r4b.model.Reference;
import org.hl7.fhir.r4b.model.SubscriptionStatus;
import org.hl7.fhir.r4b.model.SubscriptionTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import javax.annotation.Nonnull;
import java.util.Collection;

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
				// WIP SR4B deliver a topic match bundle per http://hl7.org/fhir/uv/subscriptions-backport/STU1.1/notifications.html
				mySubscriptionRegistry.getTopicSubscriptionsForUrl(topic.getUrl()).forEach(
					activeSubscription -> {
						IBaseResource payload = getPayload(matchedResource, theMsg, activeSubscription, topic);
						mySubscriptionMatchDeliverer.deliverPayload(payload, theMsg, activeSubscription, result);
					});
			}
		}
	}

	private IBaseResource getPayload(IBaseResource theMatchedResource, ResourceModifiedMessage theMsg, ActiveSubscription theActiveSubscription, SubscriptionTopic theTopic) {
		BundleBuilder bundleBuilder = new BundleBuilder(myFhirContext);
		// WIP SR4B for R5 this will be Bundle.BundleType.SUBSCRIPTIONNOTIFICATION
		bundleBuilder.setType(Bundle.BundleType.HISTORY.toCode());
		// WIP SR4B set eventsSinceSubscriptionStart from the database
		int eventsSinceSubscriptionStart = 1;
		SubscriptionStatus subscriptionStatus = buildSubscriptionStatus(theMatchedResource, theActiveSubscription, theTopic, eventsSinceSubscriptionStart);
		// WIP SR4B is this the right type of entry?
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
		subscriptionStatus.setStatus(Enumerations.SubscriptionStatus.ACTIVE);
		subscriptionStatus.setType(SubscriptionStatus.SubscriptionNotificationType.EVENTNOTIFICATION);
		// WIP SR4B count events since subscription start and set eventsSinceSubscriptionStart
		subscriptionStatus.setEventsSinceSubscriptionStart("" + theEventsSinceSubscriptionStart);
		subscriptionStatus.addNotificationEvent().setEventNumber("" + theEventsSinceSubscriptionStart).setFocus(new Reference(theMatchedResource.getIdElement()));
		subscriptionStatus.setSubscription(new Reference(theActiveSubscription.getSubscription().getIdElement(myFhirContext)));
		subscriptionStatus.setTopic(theTopic.getUrl());
		return subscriptionStatus;
	}
}
