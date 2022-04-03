package ca.uhn.fhir.rest.client.interceptor;

/*-
 * #%L
 * HAPI FHIR - Client Framework
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

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.client.api.IHttpRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This interceptor adds arbitrary header values to requests made by the client.
 *
 * This is now also possible directly on the Fluent Client API by calling
 * {@link ca.uhn.fhir.rest.gclient.IClientExecutable#withAdditionalHeader(String, String)}
 */
public class AdditionalRequestHeadersInterceptor {
	private final Map<String, List<String>> myAdditionalHttpHeaders;

	/**
	 * Constructor
	 */
	public AdditionalRequestHeadersInterceptor() {
		myAdditionalHttpHeaders = new HashMap<>();
	}

	/**
	 * Constructor
	 *
	 * @param theHeaders The additional headers to add to every request
	 */
	public AdditionalRequestHeadersInterceptor(Map<String, List<String>> theHeaders) {
		this();
		if (theHeaders != null) {
			myAdditionalHttpHeaders.putAll(theHeaders);
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
		if (myAdditionalHttpHeaders.get(headerName) == null) {
			myAdditionalHttpHeaders.put(headerName, new ArrayList<>());
		}
		return myAdditionalHttpHeaders.get(headerName);
	}

	/**
	 * Adds the additional header values to the HTTP request.
	 * @param theRequest the HTTP request
	 */
	@Hook(Pointcut.CLIENT_REQUEST)
	public void interceptRequest(IHttpRequest theRequest) {
		for (Map.Entry<String, List<String>> header : myAdditionalHttpHeaders.entrySet()) {
			for (String headerValue : header.getValue()) {
				if (headerValue != null) {
					theRequest.addHeader(header.getKey(), headerValue);
				}
			}
		}
	}

}
