/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.batch2.model;

import ca.uhn.fhir.rest.server.messaging.json.BaseJsonMessage;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NotNull;

public class JobWorkNotificationJsonMessage extends BaseJsonMessage<JobWorkNotification> {

	@JsonProperty("payload")
	private JobWorkNotification myPayload;

	/**
	 * Constructor
	 */
	public JobWorkNotificationJsonMessage() {
		super();
	}

	@Override
	@Nonnull
	public @NotNull String getMessageKey() {
		// We assume that preserving chunk order does not matter, so we want to spread work as much across the cluster
		// as possible even if it means chunks are processed out of order. If we wanted to preserve order, we would
		// use instanceId instead. This may need to change by job type in the future...
		if (myPayload.getChunkId() != null) {
			return myPayload.getChunkId();
		}
		return super.getMessageKey();
	}

	/**
	 * Constructor
	 */
	public JobWorkNotificationJsonMessage(JobWorkNotification thePayload) {
		myPayload = thePayload;
	}

	@Override
	@Nonnull
	public JobWorkNotification getPayload() {
		return myPayload;
	}

	public void setPayload(JobWorkNotification thePayload) {
		myPayload = thePayload;
	}
}
