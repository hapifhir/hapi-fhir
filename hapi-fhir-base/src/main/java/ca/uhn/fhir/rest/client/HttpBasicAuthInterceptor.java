package ca.uhn.fhir.rest.client;

/*
 * #%L
 * HAPI FHIR Library
 * %%
 * Copyright (C) 2014 University Health Network
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

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.protocol.HttpContext;

/**
 * HTTP interceptor to be used for adding HTTP basic auth username/password tokens
 * to requests
 * <p>
 * See the 
 * </p>
 */
public class HttpBasicAuthInterceptor implements HttpRequestInterceptor {

	private String myUsername;
	private String myPassword;
	
    public HttpBasicAuthInterceptor(String theUsername, String thePassword) {
		super();
		myUsername = theUsername;
		myPassword = thePassword;
	}

	@Override
	public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
        AuthState authState = (AuthState) context.getAttribute(HttpClientContext.TARGET_AUTH_STATE);

        if (authState.getAuthScheme() == null) {
            Credentials creds = new UsernamePasswordCredentials(myUsername, myPassword);
            authState.update(new BasicScheme(), creds);
        }

    }

}
