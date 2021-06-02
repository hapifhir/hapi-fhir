package ca.uhn.fhir.jpa.delete.model;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * Serialize a list of URLs so Spring Batch can store it as a String
 */
public class RequestListJson implements IModelJson {
	static final ObjectMapper ourObjectMapper = new ObjectMapper();

	@JsonProperty("urls")
	List<String> myUrls;

	@JsonProperty("requestPartitionIds")
	List<RequestPartitionId> myRequestPartitionIds;

	public static RequestListJson fromUrlStringsAndRequestPartitionIds(List<String> elements, List<RequestPartitionId> theRequestPartitionIds) {
		return new RequestListJson().setUrls(elements).setRequestPartitionIds(theRequestPartitionIds);
	}

	public static RequestListJson fromJson(String theJson) {
		try {
			return ourObjectMapper.readValue(theJson, RequestListJson.class);
		} catch (JsonProcessingException e) {
			throw new InternalErrorException("Failed to decode " + RequestListJson.class);
		}
	}

	public List<String> getUrls() {
		return myUrls;
	}

	public RequestListJson setUrls(List<String> theUrls) {
		myUrls = theUrls;
		return this;
	}

	public List<RequestPartitionId> getRequestPartitionIds() {
		return myRequestPartitionIds;
	}

	public RequestListJson setRequestPartitionIds(List<RequestPartitionId> theRequestPartitionIds) {
		myRequestPartitionIds = theRequestPartitionIds;
		return this;
	}

	@Override
	public String toString() {
		try {
			return ourObjectMapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			throw new InvalidRequestException("Failed to encode " + RequestListJson.class, e);
		}
	}
}
