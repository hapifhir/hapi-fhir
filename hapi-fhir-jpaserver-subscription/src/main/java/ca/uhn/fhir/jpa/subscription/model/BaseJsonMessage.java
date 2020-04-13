package ca.uhn.fhir.jpa.subscription.model;

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

public abstract class BaseJsonMessage<T> implements Message<T>, IModelJson {

	private static final long serialVersionUID = 1L;
	@JsonProperty("headers")
	private MessageHeaders myHeaders;

	/**
	 * Constructor
	 */
	public BaseJsonMessage() {
		super();
	}

	@Override
	public MessageHeaders getHeaders() {
		return myHeaders;
	}

	public void setHeaders(MessageHeaders theHeaders) {
		myHeaders = theHeaders;
	}
}
