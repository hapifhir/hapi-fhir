/*-
 * #%L
 * HAPI FHIR - Docs
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.hapi.fhir.docs.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.binary.BinarySecurityContextInterceptor;

/**
 * This class is mostly intended as an example implementation of the
 * {@link BinarySecurityContextInterceptor} although it could be used if
 * you wanted its specific rules.
 */
public class HeaderBasedBinarySecurityContextInterceptor extends BinarySecurityContextInterceptor {

	/**
	 * Header name
	 */
	public static final String X_SECURITY_CONTEXT_ALLOWED_IDENTIFIER = "X-SecurityContext-Allowed-Identifier";

	/**
	 * Constructor
	 *
	 * @param theFhirContext The FHIR context
	 */
	public HeaderBasedBinarySecurityContextInterceptor(FhirContext theFhirContext) {
		super(theFhirContext);
	}

	/**
	 * This method should be overridden in order to determine whether the security
	 * context identifier is allowed for the user.
	 *
	 * @param theSecurityContextSystem The <code>Binary.securityContext.identifier.system</code> value
	 * @param theSecurityContextValue  The <code>Binary.securityContext.identifier.value</code> value
	 * @param theRequestDetails        The request details associated with this request
	 */
	@Override
	protected boolean securityContextIdentifierAllowed(
			String theSecurityContextSystem, String theSecurityContextValue, RequestDetails theRequestDetails) {

		// In our simple example, we will use an incoming header called X-SecurityContext-Allowed-Identifier
		// to determine whether the security context is allowed. This is typically not what you
		// would want, since this is trusting the client to tell us what they are allowed
		// to see. You would typically verify an access token or user session with something
		// external, but this is a simple demonstration.
		String actualHeaderValue = theRequestDetails.getHeader(X_SECURITY_CONTEXT_ALLOWED_IDENTIFIER);
		String expectedHeaderValue = theSecurityContextSystem + "|" + theSecurityContextValue;
		return expectedHeaderValue.equals(actualHeaderValue);
	}
}
