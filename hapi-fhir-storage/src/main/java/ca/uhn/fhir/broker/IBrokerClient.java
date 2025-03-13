package ca.uhn.fhir.broker;

import ca.uhn.fhir.model.api.IModelJson;

public interface IBrokerClient {
	/**
	 * Create a channel that is used to receive messages from the queue.
	 *
	 * <p>
	 * Implementations can choose to return the same object for multiple invocations of this method
	 * when invoked with the same {@literal theChannelName} if they need to, or they can create a new instance.
	 * </p>
	 *
	 * @param theChannelName             The actual underlying queue name
	 * @param theMessageType             The object type that will be placed on this queue. Objects will be Jackson-annotated structures.
	 * @param theChannelConsumerSettings Contains the configuration for subscribers.
	 */
	IChannelConsumer getOrCreateConsumer(
		String theChannelName, Class<? extends IModelJson> theMessageType, ChannelConsumerSettings theChannelConsumerSettings);

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
	IChannelProducer getOrCreateProducer(
		String theChannelName, Class<? extends IModelJson> theMessageType, ChannelProducerSettings theChannelProducerSettings);

	/**
	 * @return the IChannelNamer used by this factory
	 */
	IChannelNamer getChannelNamer();
}
