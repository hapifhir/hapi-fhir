package ca.uhn.fhir.jpa.subscription.channel.models;

import ca.uhn.fhir.jpa.subscription.model.ChannelRetryConfiguration;

public class ProducingChannelParameters extends BaseChannelParameters {
	private ChannelRetryConfiguration myRetryConfiguration;

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

	public void setRetryConfiguration(ChannelRetryConfiguration theConfiguration) {
		myRetryConfiguration = theConfiguration;
	}

	public ChannelRetryConfiguration getRetryConfiguration() {
		return myRetryConfiguration;
	}
}
