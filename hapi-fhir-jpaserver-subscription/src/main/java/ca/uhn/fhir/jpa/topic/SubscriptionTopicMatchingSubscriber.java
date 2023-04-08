package ca.uhn.fhir.jpa.topic;

import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
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
	SubscriptionTopicSupport mySubscriptionTopicSupport;
	@Autowired
	SubscriptionTopicRegistry mySubscriptionTopicRegistry;

	@Override
	public void handleMessage(@Nonnull Message<?> theMessage) throws MessagingException {
		// FIXME KHS
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
			if (matcher.matches(theMsg)) {
				ourLog.info("Matched topic {} to message {}", topic.getIdElement().toUnqualifiedVersionless(), theMsg);
				// FIXME KHS do the thing
			}
		}
	}
}
