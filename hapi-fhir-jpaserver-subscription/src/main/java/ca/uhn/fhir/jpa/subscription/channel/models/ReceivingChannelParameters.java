package ca.uhn.fhir.jpa.subscription.channel.models;

import ca.uhn.fhir.jpa.subscription.model.ChannelRetryConfiguration;

public class ReceivingChannelParameters {
	private final String myChannelName;
	private ChannelRetryConfiguration myRetryConfiguration;

	public ReceivingChannelParameters(String theChannelName) {
		myChannelName = theChannelName;
	}

	public String getChannelName() {
		return myChannelName;
	}

	public void setRetryConfiguration(ChannelRetryConfiguration theConfiguration) {
		myRetryConfiguration = theConfiguration;
	}

	public ChannelRetryConfiguration getRetryConfiguration() {
		return myRetryConfiguration;
	}
}
