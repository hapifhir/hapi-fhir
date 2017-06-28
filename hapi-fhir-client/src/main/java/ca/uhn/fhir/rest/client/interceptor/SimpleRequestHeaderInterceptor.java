package ca.uhn.fhir.rest.client.interceptor;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.IOException;

import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;

/**
 * This interceptor adds an arbitrary header to requests made by this client. Both the
 * header name and the header value are specified by the calling code.
 */
public class SimpleRequestHeaderInterceptor implements IClientInterceptor {

	private String myHeaderName;
	private String myHeaderValue;

	/**
	 * Constructor
	 */
	public SimpleRequestHeaderInterceptor() {
		this(null, null);
	}

	/**
	 * Constructor
	 */
	public SimpleRequestHeaderInterceptor(String theHeaderName, String theHeaderValue) {
		super();
		myHeaderName = theHeaderName;
		myHeaderValue = theHeaderValue;
	}

	public String getHeaderName() {
		return myHeaderName;
	}

	public String getHeaderValue() {
		return myHeaderValue;
	}

	@Override
	public void interceptRequest(IHttpRequest theRequest) {
		if (isNotBlank(getHeaderName())) {
			theRequest.addHeader(getHeaderName(), getHeaderValue());
		}
	}

	@Override
	public void interceptResponse(IHttpResponse theResponse) throws IOException {
		// nothing
	}

	public void setHeaderName(String theHeaderName) {
		myHeaderName = theHeaderName;
	}

	public void setHeaderValue(String theHeaderValue) {
		myHeaderValue = theHeaderValue;
	}

}
