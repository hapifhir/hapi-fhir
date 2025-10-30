/*-
 * #%L
 * HAPI FHIR - Core Library
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
package ca.uhn.fhir.rest.gclient;

import ca.uhn.fhir.rest.api.CacheControlDirective;

/**
 * The non-FHIR bits of IClientExecutable.
 *
 * @param <T> the builder self type
 * @param <Y> the result type
 */
public interface IClientHttpExecutable<T extends IClientHttpExecutable<?, Y>, Y> {

	/**
	 * Sets the <code>Cache-Control</code> header value, which advises the server (or any cache in front of it)
	 * how to behave in terms of cached requests
	 */
	T cacheControl(CacheControlDirective theCacheControlDirective);

	/**
	 * Set an HTTP header. One
	 *
	 * <p>It is the responsibility of the caller to care for proper encoding of the header value, e.g.
	 * using Base64.</p>
	 * <p>This is a short-cut alternative to using a corresponding client interceptor</p>
	 * <p>It is a multi-valued header. Calling it multiple times will add to the list rather than replace the value</p>
	 *
	 * @param theHeaderName header name
	 * @param theHeaderValue header value
	 * @return
	 */
	T withAdditionalHeader(String theHeaderName, String theHeaderValue);

	/**
	 * Actually execute the client operation and return the result.
	 * @return The result of the HTTP operation
	 */
	Y execute();

	/**
	 * Specifies a custom <code>Accept</code> header that should be supplied with the
	 * request.
	 *
	 * @param theHeaderValue The header value, e.g. "application/json". If set to <code>null</code> or an empty string,
	 *                       the default Accept header will be used.
	 */
	T accept(String theHeaderValue);
}
