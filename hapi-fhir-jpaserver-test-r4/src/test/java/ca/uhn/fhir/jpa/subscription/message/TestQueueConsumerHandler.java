package ca.uhn.fhir.jpa.subscription.message;

import org.slf4j.Logger;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class TestQueueConsumerHandler<T> implements MessageHandler {
	private static final Logger ourLog = getLogger(TestQueueConsumerHandler.class);
	List<T> myMessages;
	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		getMessages().add((T)message);
		ourLog.info("Received message: {}", message);
	}
	public void clearMessages() {
		myMessages.clear();;
	}

	public List<T> getMessages() {
		if (myMessages == null) {
			myMessages = new ArrayList<>();
		}
		return myMessages;
	}
}
