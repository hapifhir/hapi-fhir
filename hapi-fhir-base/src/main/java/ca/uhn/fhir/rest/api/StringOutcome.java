/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.rest.api;

import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Response of a http request which can return a String payload
 */
public class StringOutcome {

	private String myPayload;
	private IBaseOperationOutcome myOperationOutcome;
	private int myResponseStatus;
	private Map<String, List<String>> myResponseHeaders;

	public StringOutcome(int theResponseStatus, String thePayload, Map<String, List<String>> theHeaders) {
		myResponseStatus = theResponseStatus;
		myPayload = thePayload;
		myResponseHeaders = theHeaders;
	}

	public StringOutcome() {

	}

	/**
	 * Returns the {@link String} payload to return to the client or <code>null</code> if none.
	 *
	 * @return This method <b>will return null</b>, unlike many methods in the API.
	 */
	public String getPayload() {
		return myPayload;
	}

	/**
	 * Sets the {@link String} payload to return to the client or <code>null</code> if none.
	 */
	public void setPayload(String thePayload) {
		myPayload = thePayload;
	}

	/**
	 * Returns the {@link IBaseOperationOutcome} resource to return to the client or <code>null</code> if none.
	 *
	 * @return This method <b>will return null</b>, unlike many methods in the API.
	 */
	public IBaseOperationOutcome getOperationOutcome() {
		return myOperationOutcome;
	}

	/**
	 * Sets the {@link IBaseOperationOutcome} resource to return to the client. Set to <code>null</code> (which is the default) if none.
	 *
	 * @return a reference to <code>this</code> for easy method chaining
	 */
	public StringOutcome setOperationOutcome(IBaseOperationOutcome theBaseOperationOutcome) {
		myOperationOutcome = theBaseOperationOutcome;
		return this;
	}

	/**
	 * Gets the headers for the HTTP response
	 */
	public Map<String, List<String>> getResponseHeaders() {
		if (myResponseHeaders == null) {
			myResponseHeaders = new HashMap<>();
		}
		return myResponseHeaders;
	}

	/**
	 * Adds a header to the response
	 */
	public void addHeader(String theHeaderKey, String theHeaderValue) {
		myResponseHeaders.getOrDefault(theHeaderKey, new ArrayList<>()).add(theHeaderValue);
	}

	/**
	 * Gets the HTTP response status
	 */
	public int getResponseStatus() {
		return myResponseStatus;
	}
}
