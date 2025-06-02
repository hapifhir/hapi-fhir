/*-
 * #%L
 * HAPI FHIR - Server Framework
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
package ca.uhn.fhir.rest.server.messaging.json;

import ca.uhn.fhir.model.api.HeaderConstants;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.messaging.MessageHeaders;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

/**
 * This class is for holding headers for BaseJsonMessages. Any serializable data can be thrown into
 * the header map. There are also three special headers, defined by the constants in this class, which are for use
 * in message handling retrying. There are also matching helper functions for fetching those special variables; however
 * they can also be accessed in standard map fashion with a `get` on the map.
 */
public class HapiMessageHeaders implements IModelJson {

	@JsonProperty(HeaderConstants.MESSAGE_RETRY_COUNT)
	private Integer myRetryCount = 0;

	@JsonProperty(HeaderConstants.MESSAGE_FIRST_FAILURE)
	private Long myFirstFailureTimestamp;

	@JsonProperty(HeaderConstants.LAST_FAILURE_KEY)
	private Long myLastFailureTimestamp;

	@JsonProperty("customHeaders")
	private final Map<String, Object> headers;

	public HapiMessageHeaders(Map<String, Object> theHeaders) {
		headers = theHeaders;
	}

	public HapiMessageHeaders() {
		headers = new HashMap<>();
	}

	public Integer getRetryCount() {
		if (isNull(this.myRetryCount)) {
			return 0;
		}
		return this.myRetryCount;
	}

	public Long getFirstFailureTimestamp() {
		return this.myFirstFailureTimestamp;
	}

	public Long getLastFailureTimestamp() {
		return this.myLastFailureTimestamp;
	}

	public void setRetryCount(Integer theRetryCount) {
		this.myRetryCount = theRetryCount;
	}

	public void setLastFailureTimestamp(Long theLastFailureTimestamp) {
		this.myLastFailureTimestamp = theLastFailureTimestamp;
	}

	public void setFirstFailureTimestamp(Long theFirstFailureTimestamp) {
		this.myFirstFailureTimestamp = theFirstFailureTimestamp;
	}

	public Map<String, Object> getCustomHeaders() {
		if (this.headers == null) {
			return new HashMap<>();
		}
		return this.headers;
	}

	public MessageHeaders toMessageHeaders() {
		Map<String, Object> returnedHeaders = new HashMap<>(this.headers);
		if (myRetryCount != null) {
			returnedHeaders.put(HeaderConstants.MESSAGE_RETRY_COUNT, myRetryCount);
		}
		if (myFirstFailureTimestamp != null) {
			returnedHeaders.put(HeaderConstants.MESSAGE_FIRST_FAILURE, myFirstFailureTimestamp);
		}
		if (myLastFailureTimestamp != null) {
			returnedHeaders.put(HeaderConstants.LAST_FAILURE_KEY, myLastFailureTimestamp);
		}
		return new MessageHeaders(returnedHeaders);
	}
}
