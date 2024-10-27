/*-
 * #%L
 * HAPI FHIR - Server Framework
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
package ca.uhn.fhir.rest.server.messaging.json;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.defaultString;

public abstract class BaseJsonMessage<T> implements Message<T>, IModelJson {

	private static final long serialVersionUID = 1L;

	@JsonProperty("headers")
	private HapiMessageHeaders myHeaders;

	/**
	 * Constructor
	 */
	public BaseJsonMessage() {
		super();
		setDefaultRetryHeaders();
	}

	protected void setDefaultRetryHeaders() {
		HapiMessageHeaders messageHeaders = new HapiMessageHeaders();
		setHeaders(messageHeaders);
	}

	@Override
	public MessageHeaders getHeaders() {
		return myHeaders.toMessageHeaders();
	}

	public HapiMessageHeaders getHapiHeaders() {
		if (isNull(myHeaders)) {
			setDefaultRetryHeaders();
		}
		return myHeaders;
	}

	public void setHeaders(HapiMessageHeaders theHeaders) {
		myHeaders = theHeaders;
	}

	@Deprecated
	@Nullable
	public String getMessageKeyOrNull() {
		return getMessageKey();
	}

	@Nullable
	public String getMessageKey() {
		return null;
	}

	/**
	 * Returns {@link #getMessageKey()} or {@link #getMessageKeyDefaultValue()} when {@link #getMessageKey()} returns null.
	 *
	 * @return the message key value or default
	 */
	@Nullable
	public String getMessageKeyOrDefault() {
		return defaultString(getMessageKey(), getMessageKeyDefaultValue());
	}

	/**
	 * Provides a fallback value when the value returned by {@link #getMessageKey()} is null.
	 *
	 * @return null by default
	 */
	@Nullable
	protected String getMessageKeyDefaultValue() {
		return null;
	}
}
