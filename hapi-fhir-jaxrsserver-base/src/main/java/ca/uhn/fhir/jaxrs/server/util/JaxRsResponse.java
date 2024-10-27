/*
 * #%L
 * HAPI FHIR JAX-RS Server
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
package ca.uhn.fhir.jaxrs.server.util;

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.BaseRestfulResponse;
import ca.uhn.fhir.util.IoUtil;
import jakarta.annotation.Nonnull;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map.Entry;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * The JaxRsResponse is a jax-rs specific implementation of the RestfulResponse.
 *
 * @author Peter Van Houte | peter.vanhoute@agfa.com | Agfa Healthcare
 */
public class JaxRsResponse extends BaseRestfulResponse<JaxRsRequest> {

	private StringWriter myWriter;
	private int myStatusCode;
	private String myContentType;
	private String myCharset;
	private ByteArrayOutputStream myOutputStream;

	/**
	 * The constructor
	 *
	 * @param request the JaxRs Request
	 */
	public JaxRsResponse(JaxRsRequest request) {
		super(request);
	}

	/**
	 * The response writer is a simple String Writer. All output is configured
	 * by the server.
	 */
	@Nonnull
	@Override
	public Writer getResponseWriter(
			int theStatusCode, String theContentType, String theCharset, boolean theRespondGzip) {
		Validate.isTrue(myWriter == null, "getResponseWriter() called multiple times");
		Validate.isTrue(myOutputStream == null, "getResponseWriter() called after getResponseOutputStream()");
		myWriter = new StringWriter();
		myStatusCode = theStatusCode;
		myContentType = theContentType;
		myCharset = theCharset;
		return myWriter;
	}

	@Nonnull
	@Override
	public OutputStream getResponseOutputStream(int theStatusCode, String theContentType, Integer theContentLength) {
		Validate.isTrue(myWriter == null, "getResponseOutputStream() called multiple times");
		Validate.isTrue(myOutputStream == null, "getResponseOutputStream() called after getResponseWriter()");

		myOutputStream = new ByteArrayOutputStream();
		myStatusCode = theStatusCode;
		myContentType = theContentType;

		return myOutputStream;
	}

	@Override
	public Response commitResponse(@Nonnull Closeable theWriterOrOutputStream) {
		IoUtil.closeQuietly(theWriterOrOutputStream);

		ResponseBuilder builder = buildResponse(myStatusCode);
		if (isNotBlank(myContentType)) {
			if (myWriter != null) {
				String charContentType = myContentType + "; charset="
						+ StringUtils.defaultIfBlank(myCharset, Constants.CHARSET_NAME_UTF8);
				builder.header(Constants.HEADER_CONTENT_TYPE, charContentType);
				builder.entity(myWriter.toString());
			} else {
				byte[] byteArray = myOutputStream.toByteArray();
				if (byteArray.length > 0) {
					builder.header(Constants.HEADER_CONTENT_TYPE, myContentType);
					builder.entity(byteArray);
				}
			}
		}

		Response retVal = builder.build();
		return retVal;
	}

	private ResponseBuilder buildResponse(int statusCode) {
		ResponseBuilder response = Response.status(statusCode);
		for (Entry<String, List<String>> header : getHeaders().entrySet()) {
			final String key = header.getKey();
			for (String value : header.getValue()) {
				response.header(key, value);
			}
		}
		return response;
	}
}
