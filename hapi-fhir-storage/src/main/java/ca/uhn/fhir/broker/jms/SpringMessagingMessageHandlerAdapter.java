package ca.uhn.fhir.broker.jms;

import ca.uhn.fhir.broker.api.IMessageListener;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.messaging.IMessage;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

public class SpringMessagingMessageHandlerAdapter<T> implements MessageHandler {
	private final IMessageListener<T> myMessageListener;

	public SpringMessagingMessageHandlerAdapter(IMessageListener<T> theMessageListener) {
		myMessageListener = theMessageListener;
	}

	@Override
	public void handleMessage(Message<?> theMessage) throws MessagingException {
		Class<?> messageClass = theMessage.getPayload().getClass();
		if (!getMessageType().isAssignableFrom(messageClass)) {
			throw new InternalErrorException("Expecting message of type " + getMessageType()
					+ ". But received message of type: " + messageClass);
		}
		IMessage<T> message = new SpringMessagingMessage<>((Message<T>) theMessage);
		myMessageListener.handleMessage(message);
	}

	private Class<T> getMessageType() {
		return myMessageListener.getMessageType();
	}
}
