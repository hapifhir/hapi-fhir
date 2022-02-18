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
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.util.StopWatch;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Map;

/**
 * An interface around the HTTP Response.
 */
public interface IHttpResponse {

	/**
	 * Buffer the message entity data.
	 * <p>
	 * In case the message entity is backed by an unconsumed entity input stream,
	 * all the bytes of the original entity input stream are read and stored in a
	 * local buffer. The original entity input stream is consumed.
	 * </p>
	 * <p>
	 * In case the response entity instance is not backed by an unconsumed input stream
	 * an invocation of {@code bufferEntity} method is ignored and the method returns.
	 * </p>
	 * <p>
	 * This operation is idempotent, i.e. it can be invoked multiple times with
	 * the same effect which also means that calling the {@code bufferEntity()}
	 * method on an already buffered (and thus closed) message instance is legal
	 * and has no further effect.
	 * </p>
	 * <p>
	 * Buffering the message entity data allows for multiple invocations of
	 * {@code readEntity(...)} methods on the response instance.
	 *
	 * @since 2.2
	 */
	void bufferEntity() throws IOException;

	/**
	 * Close the response
	 */
	void close();

	/**
	 * Returns a reader for the response entity
	 */
	Reader createReader() throws IOException;

	/**
	 * Get map of the response headers and corresponding string values.
	 *
	 * @return response headers as a map header keys and they values.
	 */
	Map<String, List<String>> getAllHeaders();

	/**
	 * Return all headers in the response with the given type
	 */
	List<String> getHeaders(String theName);

	/**
	 * Extracts {@code Content-Type} value from the response exactly as
	 * specified by the {@code Content-Type} header. Returns {@code null}
	 * if not specified.
	 */
	String getMimeType();

	/**
	 * @return Returns a StopWatch that was started right before
	 * the client request was started. The time returned by this
	 * client includes any time that was spent within the HTTP
	 * library (possibly including waiting for a connection, and
	 * any network activity)
	 */
	StopWatch getRequestStopWatch();

	/**
	 * @return the native response, depending on the client library used
	 */
	Object getResponse();

	/**
	 * Get the status code associated with the response.
	 *
	 * @return the response status code.
	 */
	int getStatus();

	/**
	 * Get the response status information reason phrase associated with the response.
	 *
	 * @return the reason phrase.
	 */
	String getStatusInfo();

	/**
	 * Read the message entity input stream as an InputStream.
	 */
	InputStream readEntity() throws IOException;
}
