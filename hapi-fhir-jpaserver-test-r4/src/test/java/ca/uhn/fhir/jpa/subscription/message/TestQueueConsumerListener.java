package ca.uhn.fhir.jpa.subscription.message;

import ca.uhn.fhir.broker.api.IMessageListener;
import ca.uhn.fhir.rest.server.messaging.IMessage;
import org.slf4j.Logger;
import org.springframework.messaging.MessagingException;

import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class TestQueueConsumerListener<T> implements IMessageListener<T> {
	private static final Logger ourLog = getLogger(TestQueueConsumerListener.class);
	List<T> myPayloads;

	@Override
	public void handleMessage(IMessage<T> message) throws MessagingException {
		getPayloads().add(message.getPayload());
		ourLog.info("Received message: {}", message);
	}
	public void clearMessages() {
		myPayloads.clear();
	}

	public List<T> getPayloads() {
		if (myPayloads == null) {
			myPayloads = new ArrayList<>();
		}
		return myPayloads;
	}
}
