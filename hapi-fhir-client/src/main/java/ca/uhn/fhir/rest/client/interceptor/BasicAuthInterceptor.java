package ca.uhn.fhir.rest.client.interceptor;

/*
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.api.*;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.apache.commons.lang3.Validate;

/**
 * HTTP interceptor to be used for adding HTTP basic auth username/password tokens
 * to requests
 * <p>
 * See the <a href="https://hapifhir.io/hapi-fhir/docs/interceptors/built_in_client_interceptors.html">HAPI Documentation</a>
 * for information on how to use this class.
 * </p>
 */
public class BasicAuthInterceptor implements IClientInterceptor {

	private String myUsername;
	private String myPassword;
	private String myHeaderValue;

	/**
	 * @param theUsername The username
	 * @param thePassword The password
	 */
	public BasicAuthInterceptor(String theUsername, String thePassword) {
		this(StringUtils.defaultString(theUsername) + ":" + StringUtils.defaultString(thePassword));
	}

	/**
	 * @param theCredentialString A credential string in the format <code>username:password</code>
	 */
	public BasicAuthInterceptor(String theCredentialString) {
		Validate.notBlank(theCredentialString, "theCredentialString must not be null or blank");
		Validate.isTrue(theCredentialString.contains(":"), "theCredentialString must be in the format 'username:password'");
		String encoded = Base64.encodeBase64String(theCredentialString.getBytes(Constants.CHARSET_US_ASCII));
		myHeaderValue = "Basic " + encoded;
	}

	@Override
	public void interceptRequest(IHttpRequest theRequest) {
		theRequest.addHeader(Constants.HEADER_AUTHORIZATION, myHeaderValue);
	}

	@Override
	public void interceptResponse(IHttpResponse theResponse) throws IOException {
		// nothing
	}

}
