package ca.uhn.fhir.rest.server.messaging.json;

/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import javax.annotation.Nullable;

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
		return myHeaders;
	}


	public void setHeaders(HapiMessageHeaders theHeaders) {
		myHeaders = theHeaders;
	}

	@Nullable
    public String getMessageKeyOrNull() {
		return null;
    }
}
