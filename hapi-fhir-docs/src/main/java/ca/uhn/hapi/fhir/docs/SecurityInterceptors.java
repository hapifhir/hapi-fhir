package ca.uhn.hapi.fhir.docs;

/*-
 * #%L
 * HAPI FHIR - Docs
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import org.apache.commons.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SecurityInterceptors {

	public void basicAuthInterceptorRealm() {
		//START SNIPPET: basicAuthInterceptorRealm
		AuthenticationException ex = new AuthenticationException();
		ex.addAuthenticateHeaderForRealm("myRealm");
		throw ex;
		//END SNIPPET: basicAuthInterceptorRealm
	}

	// START SNIPPET: basicAuthInterceptorExample
	@Interceptor
	public class BasicSecurityInterceptor {

		/**
		 * This interceptor implements HTTP Basic Auth, which specifies that
		 * a username and password are provided in a header called Authorization.
		 */
		@Hook(Pointcut.SERVER_INCOMING_REQUEST_POST_PROCESSED)
		public boolean incomingRequestPostProcessed(RequestDetails theRequestDetails, HttpServletRequest theRequest, HttpServletResponse theResponse) throws AuthenticationException {
			String authHeader = theRequest.getHeader("Authorization");

			// The format of the header must be:
			// Authorization: Basic [base64 of username:password]
			if (authHeader == null || authHeader.startsWith("Basic ") == false) {
				throw new AuthenticationException(Msg.code(642) + "Missing or invalid Authorization header");
			}

			String base64 = authHeader.substring("Basic ".length());
			String base64decoded = new String(Base64.decodeBase64(base64));
			String[] parts = base64decoded.split(":");

			String username = parts[0];
			String password = parts[1];

			/*
			 * Here we test for a hardcoded username & password. This is
			 * not typically how you would implement this in a production
			 * system of course..
			 */
			if (!username.equals("someuser") || !password.equals("thepassword")) {
				throw new AuthenticationException(Msg.code(643) + "Invalid username or password");
			}

			// Return true to allow the request to proceed
			return true;
		}


	}
	//END SNIPPET: basicAuthInterceptorExample

}
