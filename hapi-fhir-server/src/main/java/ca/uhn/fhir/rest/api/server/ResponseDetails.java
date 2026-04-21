/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.rest.api.server;

import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 * The body and status code of a FHIR response.
 * Both the status code and response resource are mutable.
 * The resource may be replaced entirely.
 * @see ca.uhn.fhir.rest.server.interceptor.IServerInterceptor
 */
public class ResponseDetails {

	private IBaseResource myResponseResource;
	private int myResponseCode;

	/**
	 * empty constructor left for backwards compatibility.
	 * @deprecated Use the two-arg constructor to initialize the fields.
	 */
	@Deprecated
	public ResponseDetails() {
		// empty
	}

	public ResponseDetails(int theStatusCode, IBaseResource theResponseResource) {
		myResponseResource = theResponseResource;
		myResponseCode = theStatusCode;
	}

	/**
	 * Constructor
	 */
	public ResponseDetails(IBaseResource theResponseResource) {
		setResponseResource(theResponseResource);
	}

	public int getResponseCode() {
		return myResponseCode;
	}

	public void setResponseCode(int theResponseCode) {
		myResponseCode = theResponseCode;
	}

	/**
	 * Get the resource which will be returned to the client
	 */
	public IBaseResource getResponseResource() {
		return myResponseResource;
	}

	/**
	 * Set the resource which will be returned to the client
	 */
	public void setResponseResource(IBaseResource theResponseResource) {
		myResponseResource = theResponseResource;
	}
}
