package ca.uhn.fhir.jpa.subscription.model;

public class ChannelRetryConfiguration {
	/**
	 * Number of times to retry a failed message.
	 */
	private Integer myRetryCount;

	public void setRetryCount(int theRetryCount) {
		myRetryCount = theRetryCount;
	}

	public Integer getRetryCount() {
		return myRetryCount;
	}
}
