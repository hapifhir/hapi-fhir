package ca.uhn.fhir.rest.client.interceptor;

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

import ca.uhn.fhir.rest.client.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

/**
 * HTTP interceptor to be used for adding HTTP basic auth username/password tokens
 * to requests
 * <p>
 * See the <a href="http://jamesagnew.github.io/hapi-fhir/doc_rest_client_interceptor.html#Security_HTTP_Basic_Authorization">HAPI Documentation</a>
 * for information on how to use this class.
 * </p>
 */
public class BasicAuthInterceptor implements IClientInterceptor {

	private String myUsername;
	private String myPassword;

    public BasicAuthInterceptor(String theUsername, String thePassword) {
		super();
		myUsername = theUsername;
		myPassword = thePassword;
	}

	@Override
	public void interceptRequest(IHttpRequest theRequest) {
		String authorizationUnescaped = StringUtils.defaultString(myUsername) + ":" + StringUtils.defaultString(myPassword);
        String encoded;
        try {
                encoded = Base64.encodeBase64String(authorizationUnescaped.getBytes("ISO-8859-1"));
        } catch (UnsupportedEncodingException e) {
                throw new InternalErrorException("Could not find US-ASCII encoding. This shouldn't happen!");
        }
        theRequest.addHeader(Constants.HEADER_AUTHORIZATION, ("Basic " + encoded));
	}

	@Override
	public void interceptResponse(IHttpResponse theResponse) throws IOException {
		// nothing
	}

	

}
