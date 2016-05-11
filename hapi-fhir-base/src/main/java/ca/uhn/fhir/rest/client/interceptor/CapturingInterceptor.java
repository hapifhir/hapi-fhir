package ca.uhn.fhir.rest.client.interceptor;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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

import ca.uhn.fhir.rest.client.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;

/**
 * Client interceptor which simply captures request and response objects and stores them so that they can be inspected after a client
 * call has returned
 */
public class CapturingInterceptor implements IClientInterceptor {

	private IHttpRequest myLastRequest;
	private IHttpResponse myLastResponse;

	/**
	 * Clear the last request and response values
	 */
	public void clear() {
		myLastRequest = null;
		myLastResponse = null;
	}

	public IHttpRequest getLastRequest() {
		return myLastRequest;
	}

	public IHttpResponse getLastResponse() {
		return myLastResponse;
	}

	@Override
	public void interceptRequest(IHttpRequest theRequest) {
		myLastRequest = theRequest;
	}

	@Override
	public void interceptResponse(IHttpResponse theRequest) {
		myLastResponse = theRequest;
	}

}
