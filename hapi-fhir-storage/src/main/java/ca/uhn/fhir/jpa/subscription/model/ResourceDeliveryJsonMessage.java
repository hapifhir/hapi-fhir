package ca.uhn.fhir.jpa.subscription.model;

/*-
 * #%L
 * HAPI FHIR Storage api
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

import ca.uhn.fhir.rest.server.messaging.json.BaseJsonMessage;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ResourceDeliveryJsonMessage extends BaseJsonMessage<ResourceDeliveryMessage> {
	private static final ObjectMapper ourObjectMapper = new ObjectMapper().registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

	@JsonProperty("payload")
	private ResourceDeliveryMessage myPayload;

	/**
	 * Constructor
	 */
	public ResourceDeliveryJsonMessage() {
		super();
	}

	/**
	 * Constructor
	 */
	public ResourceDeliveryJsonMessage(ResourceDeliveryMessage thePayload) {
		myPayload = thePayload;
	}

	@Override
	public ResourceDeliveryMessage getPayload() {
		return myPayload;
	}

	public void setPayload(ResourceDeliveryMessage thePayload) {
		myPayload = thePayload;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("myPayload", myPayload)
			.toString();
	}

	public static ResourceDeliveryJsonMessage fromJson(String theJson) throws JsonProcessingException {
		return ourObjectMapper.readValue(theJson, ResourceDeliveryJsonMessage.class);
	}

	public String asJson() throws JsonProcessingException {
		return ourObjectMapper.writeValueAsString(this);
	}
}
