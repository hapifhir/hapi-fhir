package ca.uhn.fhir.rest.server.servlet;

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

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.ParseAction;
import ca.uhn.fhir.rest.server.RestfulResponse;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseBinary;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map.Entry;
import java.util.zip.GZIPOutputStream;

public class ServletRestfulResponse extends RestfulResponse<ServletRequestDetails> {

	/**
	 * Constructor
	 */
	public ServletRestfulResponse(ServletRequestDetails servletRequestDetails) {
		super(servletRequestDetails);
	}

	@Override
	public OutputStream sendAttachmentResponse(IBaseBinary theBinary, int theStatusCode, String contentType) throws IOException {
		addHeaders();
		HttpServletResponse theHttpResponse = getRequestDetails().getServletResponse();
		theHttpResponse.setStatus(theStatusCode);
		theHttpResponse.setContentType(contentType);
		theHttpResponse.setCharacterEncoding(null);
		if (theBinary.getContent() == null || theBinary.getContent().length == 0) {
			return theHttpResponse.getOutputStream();
		}
		theHttpResponse.setContentLength(theBinary.getContent().length);
		ServletOutputStream oos = theHttpResponse.getOutputStream();
		oos.write(theBinary.getContent());
		return oos;
	}

	@Override
	public Writer getResponseWriter(int theStatusCode, String theStatusMessage, String theContentType, String theCharset, boolean theRespondGzip) throws IOException {
		addHeaders();
		HttpServletResponse theHttpResponse = getRequestDetails().getServletResponse();
		theHttpResponse.setCharacterEncoding(theCharset);
		theHttpResponse.setStatus(theStatusCode);
		theHttpResponse.setContentType(theContentType);
		if (theRespondGzip) {
			theHttpResponse.addHeader(Constants.HEADER_CONTENT_ENCODING, Constants.ENCODING_GZIP);
			return new OutputStreamWriter(new GZIPOutputStream(theHttpResponse.getOutputStream()), StandardCharsets.UTF_8);
		}
		return theHttpResponse.getWriter();
	}

	private void addHeaders() {
		HttpServletResponse theHttpResponse = getRequestDetails().getServletResponse();
		getRequestDetails().getServer().addHeadersToResponse(theHttpResponse);
		for (Entry<String, List<String>> header : getHeaders().entrySet()) {
			String key = header.getKey();
			key = sanitizeHeaderField(key);
			boolean first = true;
			for (String value : header.getValue()) {
				value = sanitizeHeaderField(value);

				// existing headers should be overridden
				if (first) {
					theHttpResponse.setHeader(key, value);
					first = false;
				} else {
					theHttpResponse.addHeader(key, value);
				}
			}
		}
	}

	static String sanitizeHeaderField(String theKey) {
		return StringUtils.replaceChars(theKey, "\r\n", null);
	}

	@Override
	public final Writer sendWriterResponse(int theStatus, String theContentType, String theCharset, Writer theWriter) {
		return theWriter;
	}

	@Override
	public Object returnResponse(ParseAction<?> outcome, int operationStatus, boolean allowPrefer, MethodOutcome response, String resourceName) throws IOException {
		addHeaders();
		return getRequestDetails().getServer().returnResponse(getRequestDetails(), outcome, operationStatus, allowPrefer, response, resourceName);
	}
}
