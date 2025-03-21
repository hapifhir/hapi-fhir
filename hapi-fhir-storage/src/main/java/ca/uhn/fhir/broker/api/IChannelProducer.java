package ca.uhn.fhir.broker.api;

import ca.uhn.fhir.rest.server.messaging.IMessage;

/**
 * @param <T> The type of messages sent by this producer
 */
public interface IChannelProducer<T> {

	/**
	 * @return the name of the topic or queue that this consumer is consuming from
	 */
	String getChannelName();

	/**
	 * Send a message to the broker.
	 *
	 * @param theMessage the message to send
	 * @return the result of the send operation
	 */
	ISendResult send(IMessage<T> theMessage);
}
