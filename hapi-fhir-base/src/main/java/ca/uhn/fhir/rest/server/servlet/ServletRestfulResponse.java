package ca.uhn.fhir.rest.server.servlet;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Map.Entry;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.hl7.fhir.instance.model.api.IBaseBinary;

import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.method.ParseAction;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.RestfulResponse;

public class ServletRestfulResponse extends RestfulResponse<ServletRequestDetails> {

	public ServletRestfulResponse(ServletRequestDetails servletRequestDetails) {
		super(servletRequestDetails);
	}

	@Override
	public Object sendAttachmentResponse(IBaseBinary bin, int stausCode, String contentType) throws IOException {
		addHeaders();
		HttpServletResponse theHttpResponse = getRequestDetails().getServletResponse();
		theHttpResponse.setStatus(stausCode);
		theHttpResponse.setContentType(contentType);
		if (bin.getContent() == null || bin.getContent().length == 0) {
			return theHttpResponse.getOutputStream();
		}
		theHttpResponse.setContentLength(bin.getContent().length);
		ServletOutputStream oos = theHttpResponse.getOutputStream();
		oos.write(bin.getContent());
		return oos;
	}

	@Override
	public Writer getResponseWriter(int theStatusCode, String theStatusMessage, String theContentType, String theCharset, boolean theRespondGzip) throws UnsupportedEncodingException, IOException {
		addHeaders();
		HttpServletResponse theHttpResponse = getRequestDetails().getServletResponse();
		theHttpResponse.setCharacterEncoding(theCharset);
		theHttpResponse.setStatus(theStatusCode);
		theHttpResponse.setContentType(theContentType);
		if (theRespondGzip) {
			theHttpResponse.addHeader(Constants.HEADER_CONTENT_ENCODING, Constants.ENCODING_GZIP);
			return new OutputStreamWriter(new GZIPOutputStream(theHttpResponse.getOutputStream()), Constants.CHARSET_NAME_UTF8);
		}
		return theHttpResponse.getWriter();
	}

	private void addHeaders() {
		HttpServletResponse theHttpResponse = getRequestDetails().getServletResponse();
		getRequestDetails().getServer().addHeadersToResponse(theHttpResponse);
		for (Entry<String, String> header : getHeaders().entrySet()) {
			theHttpResponse.setHeader(header.getKey(), header.getValue());
		}
	}

	@Override
	public final Object sendWriterResponse(int theStatus, String theContentType, String theCharset, Writer theWriter) throws IOException {
		return theWriter;
	}

	@Override
	public Object returnResponse(ParseAction<?> outcome, int operationStatus, boolean allowPrefer, MethodOutcome response, String resourceName) throws IOException {
		addHeaders();
		return getRequestDetails().getServer().returnResponse(getRequestDetails(), outcome, operationStatus, allowPrefer, response, resourceName);
	}
}
