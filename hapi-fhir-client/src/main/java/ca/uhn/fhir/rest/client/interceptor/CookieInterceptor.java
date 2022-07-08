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

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.api.*;

/**
 * HTTP interceptor to be used for adding Cookie to requests.
 * <p>
 * This interceptor adds a header resembling the following:<br>
 * &nbsp;&nbsp;&nbsp;<code>Cookie: [key]=[value]</code><br>
 * </p>
 */

public class CookieInterceptor implements IClientInterceptor {
	private final String sessionCookie;

	public CookieInterceptor(String sessionCookie) {
		this.sessionCookie = sessionCookie;
	}

	@Override
	public void interceptRequest(IHttpRequest theRequest) {
		theRequest.addHeader(Constants.HEADER_COOKIE, sessionCookie); //$NON-NLS-1$
	}

	@Override
	public void interceptResponse(IHttpResponse theResponse) {
		// nothing
	}
}
