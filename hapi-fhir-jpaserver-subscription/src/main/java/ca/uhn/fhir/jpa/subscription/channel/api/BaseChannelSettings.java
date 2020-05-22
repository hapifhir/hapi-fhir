package ca.uhn.fhir.jpa.subscription.channel.api;

public abstract class BaseChannelSettings implements IChannelSettings {
	private boolean myQualifyChannelName = true;

	/**
	 * Default true.  Used by IChannelNamer to decide how to qualify the channel name.
	 */
	@Override
	public boolean isQualifyChannelName() {
		return myQualifyChannelName;
	}

	/**
	 * Default true.  Used by IChannelNamer to decide how to qualify the channel name.
	 */
	public void setQualifyChannelName(boolean theQualifyChannelName) {
		myQualifyChannelName = theQualifyChannelName;
	}
}
