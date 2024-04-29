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
package ca.uhn.fhir.rest.server.servlet;

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.BaseRestfulResponse;
import ca.uhn.fhir.util.IoUtil;
import jakarta.annotation.Nonnull;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map.Entry;
import java.util.zip.GZIPOutputStream;

public class ServletRestfulResponse extends BaseRestfulResponse<ServletRequestDetails> {

	private Writer myWriter;
	private OutputStream myOutputStream;

	/**
	 * Constructor
	 */
	public ServletRestfulResponse(ServletRequestDetails servletRequestDetails) {
		super(servletRequestDetails);
	}

	@Nonnull
	@Override
	public OutputStream getResponseOutputStream(int theStatusCode, String theContentType, Integer theContentLength)
			throws IOException {
		Validate.isTrue(myWriter == null, "getResponseOutputStream() called multiple times");
		Validate.isTrue(myOutputStream == null, "getResponseOutputStream() called after getResponseWriter()");

		addHeaders();
		HttpServletResponse httpResponse = getRequestDetails().getServletResponse();
		httpResponse.setStatus(theStatusCode);
		httpResponse.setContentType(theContentType);
		httpResponse.setCharacterEncoding(null);
		if (theContentLength != null) {
			httpResponse.setContentLength(theContentLength);
		}
		myOutputStream = httpResponse.getOutputStream();
		return myOutputStream;
	}

	@Nonnull
	@Override
	public Writer getResponseWriter(int theStatusCode, String theContentType, String theCharset, boolean theRespondGzip)
			throws IOException {
		Validate.isTrue(myOutputStream == null, "getResponseWriter() called after getResponseOutputStream()");

		addHeaders();
		HttpServletResponse theHttpResponse = getRequestDetails().getServletResponse();
		theHttpResponse.setCharacterEncoding(theCharset);
		theHttpResponse.setStatus(theStatusCode);
		theHttpResponse.setContentType(theContentType);
		if (theRespondGzip) {
			theHttpResponse.addHeader(Constants.HEADER_CONTENT_ENCODING, Constants.ENCODING_GZIP);
			ServletOutputStream outputStream = theHttpResponse.getOutputStream();
			myWriter = new OutputStreamWriter(new GZIPOutputStream(outputStream), StandardCharsets.UTF_8);
			return myWriter;
		}

		myWriter = theHttpResponse.getWriter();
		return myWriter;
	}

	private void addHeaders() {
		HttpServletResponse httpResponse = getRequestDetails().getServletResponse();
		getRequestDetails().getServer().addHeadersToResponse(httpResponse);
		for (Entry<String, List<String>> header : getHeaders().entrySet()) {
			String key = header.getKey();
			key = sanitizeHeaderField(key);
			boolean first = true;
			for (String value : header.getValue()) {
				value = sanitizeHeaderField(value);

				// existing headers should be overridden
				if (first) {
					httpResponse.setHeader(key, value);
					first = false;
				} else {
					httpResponse.addHeader(key, value);
				}
			}
		}
	}

	@Override
	public final Object commitResponse(@Nonnull Closeable theWriterOrOutputStream) {
		IoUtil.closeQuietly(theWriterOrOutputStream);
		return null;
	}

	static String sanitizeHeaderField(String theKey) {
		return StringUtils.replaceChars(theKey, "\r\n", null);
	}
}
