/*
 * #%L
 * HAPI FHIR - Server Framework
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
package ca.uhn.fhir.rest.api.server;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.List;
import java.util.Map;

/**
 * Implementations of this interface represent a response back to the client from the server. It is
 * conceptually similar to {@link jakarta.servlet.http.HttpServletResponse} but intended to be agnostic
 * of the server framework being used.
 * <p>
 * This class is a bit of an awkward abstraction given the two styles of servers it supports.
 * Servlets work by writing to a servlet response that is provided as a parameter by the container. JAX-RS
 * works by returning an object via a method return back to the containing framework. However using it correctly should
 * make for compatible code across both approaches.
 * </p>
 */
public interface IRestfulResponse {

	/**
	 * Initiate a new textual response. The Writer returned by this method must be finalized by
	 * calling {@link #commitResponse(Closeable)} later.
	 * <p>
	 * Note that the caller should not close the returned object, but should instead just
	 * return it to {@link #commitResponse(Closeable)} upon successful completion. This is
	 * different from normal Java practice where you would request it in a <code>try with resource</code>
	 * block, since in Servlets you are not actually required to close the writer/stream, and
	 * doing so automatically may prevent you from correctly handling exceptions.
	 * </p>
	 *
	 * @param theStatusCode  The HTTP status code.
	 * @param theContentType The HTTP response content type.
	 * @param theCharset     The HTTP response charset.
	 * @param theRespondGzip Should the response be GZip encoded?
	 * @return Returns a {@link Writer} that can accept the response body.
	 */
	@Nonnull
	Writer getResponseWriter(int theStatusCode, String theContentType, String theCharset, boolean theRespondGzip)
			throws IOException;

	/**
	 * Initiate a new binary response. The OutputStream returned by this method must be finalized by
	 * calling {@link #commitResponse(Closeable)} later. This method should only be used for non-textual
	 * responses, for those use {@link #getResponseWriter(int, String, String, boolean)}.
	 * <p>
	 * Note that the caller should not close the returned object, but should instead just
	 * return it to {@link #commitResponse(Closeable)} upon successful completion. This is
	 * different from normal Java practice where you would request it in a <code>try with resource</code>
	 * block, since in Servlets you are not actually required to close the writer/stream, and
	 * doing so automatically may prevent you from correctly handling exceptions.
	 * </p>
	 *
	 * @param theStatusCode    The HTTP status code.
	 * @param theContentType   The HTTP response content type.
	 * @param theContentLength If known, the number of bytes that will be written. {@literal null} otherwise.
	 * @return Returns an {@link OutputStream} that can accept the response body.
	 */
	@Nonnull
	OutputStream getResponseOutputStream(int theStatusCode, String theContentType, @Nullable Integer theContentLength)
			throws IOException;

	/**
	 * Finalizes the response streaming using the writer that was returned by calling either
	 * {@link #getResponseWriter(int, String, String, boolean)} or
	 * {@link #getResponseOutputStream(int, String, Integer)}. This method should only be
	 * called if the response writing/streaming actually completed successfully. If an error
	 * occurred you do not need to commit the response.
	 *
	 * @param theWriterOrOutputStream The {@link Writer} or {@link OutputStream} that was returned by this object, or a Writer/OutputStream
	 *                                which decorates the one returned by this object.
	 * @return If the server style requires a returned response object (i.e. JAX-RS Server), this method
	 * returns that object. If the server style does not require one (i.e. {@link ca.uhn.fhir.rest.server.RestfulServer}),
	 * this method returns {@literal null}.
	 */
	Object commitResponse(@Nonnull Closeable theWriterOrOutputStream) throws IOException;

	/**
	 * Adds a response header. This method must be called prior to calling
	 * {@link #getResponseWriter(int, String, String, boolean)} or {@link #getResponseOutputStream(int, String, Integer)}.
	 *
	 * @param headerKey   The header name
	 * @param headerValue The header value
	 */
	void addHeader(String headerKey, String headerValue);

	/**
	 * Returns the headers added to this response
	 */
	Map<String, List<String>> getHeaders();
}
