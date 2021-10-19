package ca.uhn.fhir.jpa.subscription.channel.models;

public class ProducingChannelParameters extends BaseChannelParameters {

	/**
	 * Constructor
	 *
	 * Producing channels are sending channels. They send data to topics/queues.
	 *
	 * @param theChannelName
	 */
	public ProducingChannelParameters(String theChannelName) {
		super(theChannelName);
	}

}
