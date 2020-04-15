package ca.uhn.fhir.jpa.subscription.channel.subscription;

public interface IChannelNamer {
	/**
	 * Channel factories call this service to qualify the channel name before sending it to the channel factory.
	 *
	 * @param theNameComponent the component of the queue or topic name
	 * @return the fully qualified the channel factory will use to name the queue or topic
	 */
	String getChannelName(String theNameComponent);
}
