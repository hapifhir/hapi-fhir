package ca.uhn.fhir.jpa.subscription.model;

public class ChannelRetryConfiguration {
	/**
	 * Number of times to retry a failed message.
	 */
	private Integer myRetryCount;

	/**
	 * If a retry count is specified,
	 * a dead letter queue can be specified.
	 * Messages that fail delivery > myRetryCount will
	 * be put onto this queue. If not specified, they are simply lost.
	 *
	 * If retry count is not provided, dlq is ignored
	 */
	private String myDeadLetterQueueName;

	public void setRetryCount(int theRetryCount) {
		myRetryCount = theRetryCount;
	}

	public Integer getRetryCount() {
		return myRetryCount;
	}

	public void setDeadLetterQueueName(String theDlqName) {
		myDeadLetterQueueName = theDlqName;
	}

	public String getDeadLetterQueueName() {
		return myDeadLetterQueueName;
	}

	public boolean hasDeadLetterQueue() {
		return myDeadLetterQueueName != null
			&& !myDeadLetterQueueName.trim().equals("");
	}
}
