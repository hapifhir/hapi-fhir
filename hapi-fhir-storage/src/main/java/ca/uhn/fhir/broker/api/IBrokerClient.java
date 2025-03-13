package ca.uhn.fhir.broker.api;

import ca.uhn.fhir.model.api.IModelJson;

/**
 * @param <S> The type of the result of sending a message
 */
public interface IBrokerClient<S> {
	/**
	 * Create a channel that is used to receive messages from the queue.
	 *
	 * <p>
	 * Implementations can choose to return the same object for multiple invocations of this method
	 * when invoked with the same {@literal theChannelName} if they need to, or they can create a new instance.
	 * </p>
	 *
	 * @param theChannelName             The actual underlying queue name
	 * @param theMessageType             The object type that will be placed on this queue. Objects will usually be Jackson-annotated structures.
	 * @param theChannelConsumerSettings Contains the configuration for subscribers.
	 */
	<T> IChannelConsumer<T> getOrCreateConsumer(
		String theChannelName,
		Class<T> theMessageType,
		IMessageListener<T> theMessageListener,
		ChannelConsumerSettings theChannelConsumerSettings);

	/**
	 * Create a channel that is used to send messages to the queue.
	 *
	 * <p>
	 * Implementations can choose to return the same object for multiple invocations of this method
	 * when invoked with the same {@literal theChannelName} if they need to, or they can create a new instance.
	 * </p>
	 *
	 * @param theChannelName             The actual underlying queue name
	 * @param theMessageType             The object type that will be placed on this queue. Objects will be Jackson-annotated structures.
	 * @param theChannelProducerSettings Contains the configuration for senders.
	 */
	<T> IChannelProducer<T, S> getOrCreateProducer(
		String theChannelName, Class<T> theMessageType, ChannelProducerSettings theChannelProducerSettings);

	/**
	 * @return the IChannelNamer used by this factory
	 */
	IChannelNamer getChannelNamer();
}
