/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.parser;

import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

/**
 * Thrown when client-supplied data cannot be parsed (invalid date, malformed JSON/XML, etc.).
 * Extends {@link InvalidRequestException} so the HTTP 400 status code is preserved by
 * status-aware exception interceptors — including those that wrap or replace exceptions before
 * HAPI's {@code ExceptionHandlingInterceptor} runs (e.g. CDR's
 * {@code ExceptionDetailsSuppressingInterceptor}). Without this hierarchy such interceptors had
 * no status code to read and fell back to HTTP 500 for invalid client input.
 */
public class DataFormatException extends InvalidRequestException {

	private static final long serialVersionUID = 1L;

	public DataFormatException() {
		super((String) null);
	}

	public DataFormatException(String theMessage) {
		super(theMessage);
	}

	public DataFormatException(String theMessage, Throwable theCause) {
		super(theMessage, theCause);
	}

	public DataFormatException(Throwable theCause) {
		super(theCause);
	}
}
