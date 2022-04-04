package ca.uhn.fhir.batch2.impl;

import ca.uhn.fhir.batch2.model.JobWorkNotification;
import ca.uhn.fhir.batch2.model.JobWorkNotificationJsonMessage;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BatchJobSender {
	private static final Logger ourLog = LoggerFactory.getLogger(BatchJobSender.class);
	private final IChannelProducer myWorkChannelProducer;

	public BatchJobSender(IChannelProducer theWorkChannelProducer) {
		myWorkChannelProducer = theWorkChannelProducer;
	}

	void sendWorkChannelMessage(JobWorkNotification theJobWorkNotification) {
		JobWorkNotificationJsonMessage message = new JobWorkNotificationJsonMessage();
		message.setPayload(theJobWorkNotification);

		ourLog.info("Sending work notification for {}", theJobWorkNotification);
		myWorkChannelProducer.send(message);
	}
}
