/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.batch2.channel;

import ca.uhn.fhir.batch2.model.JobWorkNotification;
import ca.uhn.fhir.batch2.model.JobWorkNotificationJsonMessage;
import ca.uhn.fhir.broker.api.IChannelProducer;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class BatchJobSender {
	private static final Logger ourLog = LoggerFactory.getLogger(BatchJobSender.class);
	private final IChannelProducer<JobWorkNotification> myWorkChannelProducer;

	/**
	 * Function used to convert the JobWorkNotification into the JobWorkNotificationJsonMessage.
	 * We provide this as a function so that it can be overridden so that parts of the message itself
	 * can be overwritten.
	 *
	 * The most important of these is the getMessageKey method, which, for some message brokers (kafka),
	 * is used to control the partition to which the message is sent.
	 *
	 * Overwritting the key in this case allows for more deterministic tests where the test
	 * directly controls the partition instead of relying on whatever the randomly assigned
	 * guid hashes out to.
	 */
	private Function<JobWorkNotification, JobWorkNotificationJsonMessage> myMessageCreationFn =
			JobWorkNotificationJsonMessage::new;

	public BatchJobSender(@Nonnull IChannelProducer<JobWorkNotification> theWorkChannelProducer) {
		myWorkChannelProducer = theWorkChannelProducer;
	}

	public void sendWorkChannelMessage(JobWorkNotification theJobWorkNotification) {
		JobWorkNotificationJsonMessage message = myMessageCreationFn.apply(theJobWorkNotification);
		ourLog.info("Sending work notification for {}", theJobWorkNotification);
		myWorkChannelProducer.send(message);
	}

	/**
	 * Visible to allow overwriting JobWorkNotificationJsonMessage in testing.
	 * This should not be used in production code.
	 * Pass "null" to reset.
	 */
	@VisibleForTesting
	public void setMessageCreationFn(
			Function<JobWorkNotification, JobWorkNotificationJsonMessage> theMessageCreationFn) {
		myMessageCreationFn = theMessageCreationFn;
		if (myMessageCreationFn == null) {
			myMessageCreationFn = JobWorkNotificationJsonMessage::new;
		}
	}
}
