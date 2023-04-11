package ca.uhn.fhir.jpa.topic;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.subscription.match.matcher.subscriber.SubscriptionMatchDeliverer;
import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionRegistry;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import org.hl7.fhir.instance.model.api.IBaseResource;
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

	private final FhirContext myFhirContext;
	@Autowired
	SubscriptionTopicSupport mySubscriptionTopicSupport;
	@Autowired
	SubscriptionTopicRegistry mySubscriptionTopicRegistry;
	@Autowired
	SubscriptionRegistry mySubscriptionRegistry;
	@Autowired
	SubscriptionMatchDeliverer mySubscriptionMatchDeliverer;
	@Autowired
	SubscriptionTopicPayloadBuilder mySubscriptionTopicPayloadBuilder;

	public SubscriptionTopicMatchingSubscriber(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

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
			if (result.matched()) {
				ourLog.info("Matched topic {} to message {}", topic.getIdElement().toUnqualifiedVersionless(), theMsg);
				deliverToTopicSubscriptions(theMsg, topic, result);
			}
		}
	}

	private void deliverToTopicSubscriptions(ResourceModifiedMessage theMsg, SubscriptionTopic topic, InMemoryMatchResult result) {
		List<ActiveSubscription> topicSubscriptions = mySubscriptionRegistry.getTopicSubscriptionsByUrl(topic.getUrl());
		if (!topicSubscriptions.isEmpty()) {
			IBaseResource matchedResource = theMsg.getNewPayload(myFhirContext);

			for (ActiveSubscription activeSubscription : topicSubscriptions) {
				// WIP STR5 apply subscription filter
				IBaseResource payload = mySubscriptionTopicPayloadBuilder.buildPayload(matchedResource, theMsg, activeSubscription, topic);
				mySubscriptionMatchDeliverer.deliverPayload(payload, theMsg, activeSubscription, result);
			}
		}
	}
}
