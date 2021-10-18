package ca.uhn.fhir.jpa.subscription.model;

public class ChannelRetryConfiguration {
	/**
	 * Number of times to retry a failed message.
	 */
	private Integer myRetryCount;

	/**
	 * If a retry count is specified,
	 * a dead letter queue prefix can be specified.
	 * Messages that fail delivery > myRetryCount will
	 * be put onto a dlq using this prefix.
	 *
	 * While called a 'prefix', whether it is used
	 * as such is up to implementation of message queues.
	 * ActiveMQ will use this as a prefix.
	 * Kafka will use this as a prefix. //todo - check
	 *
	 * If no dlq prefix is specified, they are simply lost.
	 *
	 * If retry count is not provided, dlq prefix is ignored
	 */
	private String myDeadLetterQueuePrefix;

	public void setRetryCount(int theRetryCount) {
		myRetryCount = theRetryCount;
	}

	public Integer getRetryCount() {
		return myRetryCount;
	}

	public void setDeadLetterQueuePrefix(String theDlqName) {
		myDeadLetterQueuePrefix = theDlqName;
	}

	public String getDeadLetterQueuePrefix() {
		return myDeadLetterQueuePrefix;
	}

	public boolean hasDeadLetterQueuePrefix() {
		return myDeadLetterQueuePrefix != null
			&& !myDeadLetterQueuePrefix.trim().equals("");
	}
}
