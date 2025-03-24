package ca.uhn.fhir.broker.api;

import ca.uhn.fhir.rest.server.messaging.IMessage;

/**
 * @param <T> The type of messages received by this consumer
 */
public interface IChannelConsumer<T> extends AutoCloseable {
	/**
	 * @return the name of the topic or queue that this consumer is consuming from
	 */
	String getChannelName();

	/**
	 * Close the consumer and release any resources.
	 */
	void close();

	Class<? extends IMessage<T>> getMessageType();

	IMessageListener<T> getMessageListener();
}
