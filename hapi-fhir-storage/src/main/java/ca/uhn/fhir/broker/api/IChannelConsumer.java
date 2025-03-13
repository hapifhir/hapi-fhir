package ca.uhn.fhir.broker.api;

import ca.uhn.fhir.model.api.IModelJson;

import java.io.Closeable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @param <T> The type of messages received by this consumer
 */
public interface IChannelConsumer<T> extends AutoCloseable {
	/**
	 * @return the name of the consumer
	 */
	String getConsumerName();

	/**
	 * @return the name of the topic or queue that this consumer is consuming from
	 */
	String getChannelName();

	/**
	 * Stop requesting new messages from the broker until resume() is called.
	 */
	void pause();

	/**
	 * Resume requesting messages from the broker.
	 */
	void resume();

	/**
	 * @return a single message
	 */
	Message<T> receive();

	/**
	 * @param timeout the maximum time to wait
	 * @param unit    the time unit of the timeout argument
	 * @return a single message or null if no message is received within the timeout period
	 */
	Message<T> receive(int timeout, TimeUnit unit);

	/**
	 * Retrieves a message when it will be available and completes CompletableFuture with received message.
	 * @return future that will be completed when the message is received
	 */
	CompletableFuture<Message<T>> receiveAsync();
}
