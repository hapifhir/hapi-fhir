package ca.uhn.fhir.rest.client.interceptor;

/*-
 * #%L
 * HAPI FHIR - Client Framework
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This interceptor adds arbitrary header values to requests made by the client.
 */
public class AdditionalRequestHeadersInterceptor implements IClientInterceptor {
	private final Map<String, List<String>> additionalHttpHeaders = new HashMap<>();

	public AdditionalRequestHeadersInterceptor() {
		this(new HashMap<String, List<String>>());
	}

	public AdditionalRequestHeadersInterceptor(Map<String, List<String>> additionalHttpHeaders) {
		super();
		if (additionalHttpHeaders != null) {
			this.additionalHttpHeaders.putAll(additionalHttpHeaders);
		}
	}

	/**
	 * Adds the given header value.
	 * Note that {@code headerName} and {@code headerValue} cannot be null.
	 * @param headerName the name of the header
	 * @param headerValue the value to add for the header
	 * @throws NullPointerException if either parameter is {@code null}
	 */
	public void addHeaderValue(String headerName, String headerValue) {
		Objects.requireNonNull(headerName, "headerName cannot be null");
		Objects.requireNonNull(headerValue, "headerValue cannot be null");

		getHeaderValues(headerName).add(headerValue);
	}

	/**
	 * Adds the list of header values for the given header.
	 * Note that {@code headerName} and {@code headerValues} cannot be null.
	 * @param headerName the name of the header
	 * @param headerValues the list of values to add for the header
	 * @throws NullPointerException if either parameter is {@code null}
	 */
	public void addAllHeaderValues(String headerName, List<String> headerValues) {
		Objects.requireNonNull(headerName, "headerName cannot be null");
		Objects.requireNonNull(headerValues, "headerValues cannot be null");

		getHeaderValues(headerName).addAll(headerValues);
	}

	/**
	 * Gets the header values list for a given header.
	 * If the header doesn't have any values, an empty list will be returned.
	 * @param headerName the name of the header
	 * @return the list of values for the header
	 */
	private List<String> getHeaderValues(String headerName) {
		if (additionalHttpHeaders.get(headerName) == null) {
			additionalHttpHeaders.put(headerName, new ArrayList<String>());
		}
		return additionalHttpHeaders.get(headerName);
	}

	/**
	 * Adds the additional header values to the HTTP request.
	 * @param theRequest the HTTP request
	 */
	@Override
	public void interceptRequest(IHttpRequest theRequest) {
		for (Map.Entry<String, List<String>> header : additionalHttpHeaders.entrySet()) {
			for (String headerValue : header.getValue()) {
				if (headerValue != null) {
					theRequest.addHeader(header.getKey(), headerValue);
				}
			}
		}
	}

	/**
	 * Does nothing since this interceptor is not concerned with the response.
	 * @param theResponse the HTTP response
	 * @throws IOException
	 */
	@Override
	public void interceptResponse(IHttpResponse theResponse) throws IOException {
		// Do nothing. This interceptor is not concerned with the response.
	}
}
