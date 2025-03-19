package ca.uhn.fhir.broker.api;

import ca.uhn.fhir.rest.server.messaging.IMessage;

public interface IMessageListener<T> {
	/**
	 * This method is called whenever a new message is received.
	 *
	 * @param theMessage the message that was received
	 */
	void handleMessage(IMessage<T> theMessage);

	Class<T> getMessageType();
}
