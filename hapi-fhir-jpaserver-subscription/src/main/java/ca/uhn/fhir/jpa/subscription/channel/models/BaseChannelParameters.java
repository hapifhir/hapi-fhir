package ca.uhn.fhir.jpa.subscription.channel.models;

public class BaseChannelParameters {

	private final String myChannelName;

	/**
	 * Constructor
	 */
	public BaseChannelParameters(String theChannelName) {
		myChannelName = theChannelName;
	}

	public String getChannelName() {
		return myChannelName;
	}

}
