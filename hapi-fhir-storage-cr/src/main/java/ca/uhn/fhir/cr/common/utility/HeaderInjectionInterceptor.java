package ca.uhn.fhir.cr.common.utility;

/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
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

import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HeaderInjectionInterceptor implements IClientInterceptor {

	private Map<String, String> myHeaders;

	/**
	 * Instantiates a new header injection interception.
	 *
	 * @param headerKey   the header key
	 * @param headerValue the header value
	 */
	public HeaderInjectionInterceptor(String headerKey, String headerValue) {
		super();
		this.myHeaders = new HashMap<>();
		this.myHeaders.put(headerKey, headerValue);
	}

	/**
	 * Instantiates a new header injection interception.
	 *
	 * @param headers the headers
	 */
	public HeaderInjectionInterceptor(Map<String, String> headers) {
		super();
		this.myHeaders = headers;
	}

	@Override
	public void interceptRequest(IHttpRequest theRequest) {

		for (Map.Entry<String, String> entry : this.myHeaders.entrySet()) {
			theRequest.addHeader(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public void interceptResponse(IHttpResponse theResponse) throws IOException {
		// nothing
	}
}
