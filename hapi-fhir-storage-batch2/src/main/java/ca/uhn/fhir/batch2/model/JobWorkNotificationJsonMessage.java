/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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

public class JobWorkNotificationJsonMessage extends BaseJsonMessage<JobWorkNotification> {

	@JsonProperty("payload")
	private JobWorkNotification myPayload;

	/**
	 * Constructor
	 */
	public JobWorkNotificationJsonMessage() {
		super();
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
