/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.rest.server.method;

import org.springframework.http.HttpStatus;

/**
 * This Class is an <code>Optional</code> return type from an <code>Interceptor</code> call to the
 * <code>Hook</code> called {@code ca.uhn.fhir.interceptor.api.Pointcut.SERVER_OUTGOING_FAILURE_OPERATIONOUTCOME}.
 * Only valid HttpResponseCode values will be accepted and stored in this container class.
 */
public class ServerOutgoingFailureOperationOutcomeHttpResponseCode {
	private final HttpStatus myResponseStatusCode;

	/**
	 * Make the default ctor private so it has to be created using a valid Http Response Code
	 */
	private ServerOutgoingFailureOperationOutcomeHttpResponseCode() {
		// Adding this initialization to avoid a compilation ERROR about an uninitialized final member variable
		myResponseStatusCode = HttpStatus.valueOf(HttpStatus.CREATED.value());
	}

	/**
	 * The <code>public</code> Constructor will only accept <code>int</code> values that are part of the valid set of
	 * HttpResponseCodes found in the class {@code org.springframework.http.HttpStatus}
	 */
	public ServerOutgoingFailureOperationOutcomeHttpResponseCode(int theResponseStatusCode)
			throws IllegalArgumentException {
		myResponseStatusCode = HttpStatus.valueOf(theResponseStatusCode);
	}

	public int getMyResponseStatusCode() {
		return myResponseStatusCode.value();
	}

	public String getMyResponseStatusCodeReason() {
		return myResponseStatusCode.getReasonPhrase();
	}
}
