/*
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
package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.rest.api.server.IRestfulResponse;
import ca.uhn.fhir.rest.api.server.RequestDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseRestfulResponse<T extends RequestDetails> implements IRestfulResponse {
	private final Map<String, List<String>> myHeaders = new HashMap<>();
	private T myRequestDetails;

	public BaseRestfulResponse(T theRequestDetails) {
		this.myRequestDetails = theRequestDetails;
	}

	@Override
	public void addHeader(String headerKey, String headerValue) {
		this.getHeaders().computeIfAbsent(headerKey, k -> new ArrayList<>()).add(headerValue);
	}

	/**
	 * Get the http headers
	 *
	 * @return the headers
	 */
	@Override
	public Map<String, List<String>> getHeaders() {
		return myHeaders;
	}

	/**
	 * Get the requestDetails
	 *
	 * @return the requestDetails
	 */
	public T getRequestDetails() {
		return myRequestDetails;
	}

	/**
	 * Set the requestDetails
	 *
	 * @param requestDetails the requestDetails to set
	 */
	public void setRequestDetails(T requestDetails) {
		this.myRequestDetails = requestDetails;
	}
}
