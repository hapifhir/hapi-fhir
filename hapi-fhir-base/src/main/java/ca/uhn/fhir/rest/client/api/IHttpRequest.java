package ca.uhn.fhir.rest.client.api;

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

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Http Request. Allows addition of headers and execution of the request.
 */
public interface IHttpRequest {

	/**
	 * Add a header to the request
	 *
	 * @param theName  the header name
	 * @param theValue the header value
	 */
	void addHeader(String theName, String theValue);

	/**
	 * Execute the request
	 *
	 * @return the response
	 */
	IHttpResponse execute() throws IOException;

	/**
	 * @return all request headers in lower case. Note that this method
	 * returns an <b>immutable</b> Map
	 */
	Map<String, List<String>> getAllHeaders();

	/**
	 * Return the request body as a string.
	 * If this is not supported by the underlying technology, null is returned
	 *
	 * @return a string representation of the request or null if not supported or empty.
	 */
	String getRequestBodyFromStream() throws IOException;

	/**
	 * Return the request URI, or null
	 *
	 * @see #getUri()
	 */
	String getUri();

	/**
	 * Modify the request URI, or null
	 *
	 * @see #setUrlSource(UrlSourceEnum)
	 */
	void setUri(String theUrl);

	/**
	 * Return the HTTP verb (e.g. "GET")
	 */
	String getHttpVerbName();

	/**
	 * Remove any headers matching the given name
	 *
	 * @param theHeaderName The header name, e.g. "Accept" (must not be null or blank)
	 */
	void removeHeaders(String theHeaderName);

	/**
	 * Where was the URL from?
	 *
	 * @since 5.0.0
	 */
	UrlSourceEnum getUrlSource();

	/**
	 * Where was the URL from?
	 *
	 * @since 5.0.0
	 */
	void setUrlSource(UrlSourceEnum theUrlSource);

}
