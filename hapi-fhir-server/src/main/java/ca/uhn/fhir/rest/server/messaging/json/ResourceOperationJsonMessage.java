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

import ca.uhn.fhir.rest.server.messaging.ResourceOperationMessage;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ResourceOperationJsonMessage extends BaseJsonMessage<ResourceOperationMessage> {


	@JsonProperty("payload")
	private ResourceOperationMessage myPayload;

	/**
	 * Constructor
	 */
	public ResourceOperationJsonMessage() {
		super();
	}

	/**
	 * Constructor
	 */
	public ResourceOperationJsonMessage(ResourceOperationMessage thePayload) {
		myPayload = thePayload;
		setDefaultRetryHeaders();
	}

	public ResourceOperationJsonMessage(HapiMessageHeaders theRetryMessageHeaders, ResourceOperationMessage thePayload) {
		myPayload = thePayload;
		setHeaders(theRetryMessageHeaders);
	}


	@Override
	public ResourceOperationMessage getPayload() {
		return myPayload;
	}

	public void setPayload(ResourceOperationMessage thePayload) {
		myPayload = thePayload;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("myPayload", myPayload)
			.toString();
	}
}
