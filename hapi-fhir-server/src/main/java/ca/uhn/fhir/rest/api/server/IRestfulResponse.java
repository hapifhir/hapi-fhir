package ca.uhn.fhir.rest.api.server;

/*
 * #%L
 * HAPI FHIR - Server Framework
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.List;
import java.util.Map;

/**
 * Implementations of this interface represent a response back to the client from the server. It is
 * conceptually similar to {@link javax.servlet.http.HttpServletResponse} but intended to be agnostic
 * of the server framework being used.
 */
public interface IRestfulResponse {

	/**
	 * Initiate a new textual response. The Writer returned by this method must be finalized by
	 * calling {@link #commitResponse()} later.
	 *
	 * @param theStatusCode  The HTTP status code.
	 * @param theContentType The HTTP response content type.
	 * @param theCharset     The HTTP response charset.
	 * @param theRespondGzip Should the response be GZip encoded?
	 * @return Returns a {@link Writer} that can accept the response body.
	 */
	@Nonnull
	Writer getResponseWriter(int theStatusCode, String theContentType, String theCharset, boolean theRespondGzip) throws IOException;

	/**
	 * Initiate a new binary response. The OutputStream returned by this method must be finalized by
	 * calling {@link #commitResponse()} later. This method should only be used for non-textual
	 * responses, for those use {@link #getResponseWriter(int, String, String, boolean)}.
	 *
	 * @param theStatusCode    The HTTP status code.
	 * @param theContentType   The HTTP response content type.
	 * @param theContentLength If known, the number of bytes that will be written. {@literal null} otherwise.
	 * @return Returns an {@link OutputStream} that can accept the response body.
	 */
	@Nonnull
	OutputStream getResponseOutputStream(int theStatusCode, String theContentType, @Nullable Integer theContentLength) throws IOException;

	/**
	 * Finalizes the response streaming using the writer that was returned by calling either
	 * {@link #getResponseWriter(int, String, String, boolean)} or
	 * {@link #getResponseOutputStream(int, String, Integer)}.
	 *
	 * @return If the server style requires a returned response object (i.e. JAX-RS Server), this method
	 * returns that object. If the server style does not require one (i.e. {@link ca.uhn.fhir.rest.server.RestfulServer}),
	 * this method returns {@literal null}.
	 */
	Object commitResponse() throws IOException;

	void addHeader(String headerKey, String headerValue);


	Map<String, List<String>> getHeaders();

}
