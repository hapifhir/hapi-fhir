package ca.uhn.fhir.jpa.subscription.channel.models;

public class ReceivingChannelParameters extends BaseChannelParameters {

	/**
	 * Constructor
	 *
	 * Receiving channels are channels that receive data from topics/queues
	 *
	 * @param theChannelName
	 */
	public ReceivingChannelParameters(String theChannelName) {
		super(theChannelName);
	}
}
