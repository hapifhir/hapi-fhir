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

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class BaseJsonMessage<T> implements Message<T>, IModelJson {

	private static final long serialVersionUID = 1L;
	@JsonProperty("headers")
	private HapiMessageHeaders myHeaders;

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
	public static class HapiMessageHeaders implements Map<String, Object>{

		private final Map<String, Object> headers;

		public HapiMessageHeaders(Map<String, Object> theHeaders) {
			headers = theHeaders;
		}

		public HapiMessageHeaders() {
			headers = new HashMap<>();
		}


		@Override
		public int size() {
			return this.headers.size();
		}

		@Override
		public boolean isEmpty() {
			return this.headers.isEmpty();
		}

		@Override
		public boolean containsKey(Object key) {
			return this.headers.containsKey(key);
		}

		@Override
		public boolean containsValue(Object value) {
			return this.headers.containsValue(value);
		}

		@Override
		public Object get(Object key) {
			 return this.headers.get(key);
		}

		@Override
		public Object put(String key, Object value) {
			return this.headers.put(key, value);
		}

		@Override
		public Object remove(Object key) {
			return this.headers.remove(key);
		}

		@Override
		public void putAll(Map<? extends String, ?> m) {
			this.headers.putAll(m);
		}

		@Override
		public void clear() {
			this.headers.clear();
		}

		@Override
		public Set<String> keySet() {
			return this.headers.keySet();
		}

		@Override
		public Collection<Object> values() {
			return this.headers.values();
		}

		@Override
		public Set<Entry<String, Object>> entrySet() {
			return this.headers.entrySet();
		}
	}

	protected void setDefaultRetryHeaders() {
		Map<String, Object> headers = new HashMap<>();
		headers.put(RETRY_COUNT_HEADER, 0);
		headers.put(FIRST_FAILURE_HEADER, null);
		headers.put(LAST_FAILURE_HEADER, null);
		HapiMessageHeaders messageHeaders = new HapiMessageHeaders(headers);
		setHeaders(messageHeaders);
	}

	@Override
	public MessageHeaders getHeaders() {
		return new MessageHeaders(myHeaders);
	}

	public HapiMessageHeaders getHapiHeaders() {
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

	public void setHeaders(HapiMessageHeaders theHeaders) {
		myHeaders = theHeaders;
	}
}
