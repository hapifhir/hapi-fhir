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

	private Function<JobWorkNotification, JobWorkNotificationJsonMessage> myMessageCreationFn;

	public BatchJobSender(@Nonnull IChannelProducer<JobWorkNotification> theWorkChannelProducer) {
		myWorkChannelProducer = theWorkChannelProducer;
		myMessageCreationFn = JobWorkNotificationJsonMessage::new;
	}

	public void sendWorkChannelMessage(JobWorkNotification theJobWorkNotification) {
		JobWorkNotificationJsonMessage message = myMessageCreationFn.apply(theJobWorkNotification);

		ourLog.info("Sending work notification for {}", theJobWorkNotification);
		myWorkChannelProducer.send(message);
	}

	@VisibleForTesting
	public void setMessageCreationFn(
			Function<JobWorkNotification, JobWorkNotificationJsonMessage> theMessageCreationFn) {
		myMessageCreationFn = theMessageCreationFn;
		if (myMessageCreationFn == null) {
			myMessageCreationFn = JobWorkNotificationJsonMessage::new;
		}
	}
}
