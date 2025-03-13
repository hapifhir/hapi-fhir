package ca.uhn.fhir.broker.api;

/**
 * @param <T> The type of messages sent by this producer
 * @param <S> The type of the result of sending a message
 */
public interface IChannelProducer<T, S> extends AutoCloseable {
	/**
	 * @return the name of the consumer
	 */
	String getProducerName();

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
	S send(T theMessage);
}
