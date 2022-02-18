package ca.uhn.fhir.rest.server.exceptions;

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.util.CoverageIgnore;

/*
 * #%L
 * HAPI FHIR - Core Library
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

/**
 * Represents an <b>HTTP 401 Client Unauthorized</b> response, which 
 * means that the client needs to provide credentials, or has 
 * provided invalid credentials.
 * <p>
 * For security failures, you should use
 * {@link AuthenticationException} if you want to indicate that the
 * user could not be authenticated (e.g. credential failures), also 
 * known as an <b>authentication</b> failure. 
 * You should use {@link ForbiddenOperationException} if you want to 
 * indicate that the authenticated user does not have permission to
 * perform the requested operation, also known as an <b>authorization</b>
 * failure.
 * </p>
 * <p>
 * Note that a complete list of RESTful exceptions is available in the <a href="./package-summary.html">Package
 * Summary</a>.
 * </p>
 
 */
@CoverageIgnore
public class AuthenticationException extends BaseServerResponseException {

	public static final int STATUS_CODE = Constants.STATUS_HTTP_401_CLIENT_UNAUTHORIZED;

	private static final long serialVersionUID = 1L;

	public AuthenticationException() {
		super(STATUS_CODE, "Client unauthorized");
	}

	public AuthenticationException(String theMessage) {
		super(STATUS_CODE, theMessage);
	}

	public AuthenticationException(String theMessage, Throwable theCause) {
		super(STATUS_CODE, theMessage, theCause);
	}
	
	/**
	 * Adds a <code>WWW-Authenticate</code> header to the response, of the form:<br/>
	 * <code>WWW-Authenticate: Basic realm="theRealm"</code> 
	 * 
	 * @return Returns a reference to <code>this</code> for easy method chaining
	 */
	public AuthenticationException addAuthenticateHeaderForRealm(String theRealm) {
		addResponseHeader("WWW-Authenticate", "Basic realm=\"" + theRealm + "\"");
		return this;
	}

}
