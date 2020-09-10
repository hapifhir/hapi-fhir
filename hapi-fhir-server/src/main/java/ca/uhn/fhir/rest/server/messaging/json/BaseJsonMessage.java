package ca.uhn.fhir.rest.server.messaging.json;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseJsonMessage<T> implements Message<T>, IModelJson {

	private static final long serialVersionUID = 1L;
	@JsonProperty("headers")
	private MessageHeaders myHeaders;

	private String RETRY_COUNT_HEADER = "retryCount";
	private String FIRST_FAILURE_HEADER = "firstFailure";
	private String LAST_FAILURE_HEADER = "lastFailure";

	/**
	 * Constructor
	 */
	public BaseJsonMessage() {
		super();
		setDefaultRetryHeaders();
	}

	protected void setDefaultRetryHeaders() {
		Map<String, Object> headers = new HashMap<>();
		headers.put(RETRY_COUNT_HEADER, 0);
		headers.put(FIRST_FAILURE_HEADER, null);
		headers.put(LAST_FAILURE_HEADER, null);
		MessageHeaders messageHeaders = new MessageHeaders(headers);
		setHeaders(messageHeaders);
	}

	@Override
	public MessageHeaders getHeaders() {
		return myHeaders;
	}
	public final Integer getRetryCount() {
		//TODO GGG this is not NPE-safe
		return (Integer)this.getHeaders().get(RETRY_COUNT_HEADER);
	}

	public final Date getFirstFailureDate() {
		//TODO GGG this is not NPE-safe
		return (Date)this.getHeaders().get(FIRST_FAILURE_HEADER);

	}

	public final Date getLastFailureDate() {
		//TODO GGG this is not NPE-safe
		return (Date)this.getHeaders().get(LAST_FAILURE_HEADER);

	}

	public void setHeaders(MessageHeaders theHeaders) {
		myHeaders = theHeaders;
	}
}
