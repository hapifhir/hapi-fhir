package ca.uhn.fhir.jaxrs.server.util;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/*
 * #%L
 * HAPI FHIR JAX-RS Server
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
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Map.Entry;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseBinary;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.method.ParseAction;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.RestfulResponse;
import ca.uhn.fhir.rest.server.RestfulServerUtils;

/**
 * The JaxRsResponse is a jax-rs specific implementation of the RestfulResponse.
 * 
 * @author Peter Van Houte | peter.vanhoute@agfa.com | Agfa Healthcare
 */
public class JaxRsResponse extends RestfulResponse<JaxRsRequest> {

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
	@Override
	public Writer getResponseWriter(int theStatusCode, String theStatusMessage, String theContentType, String theCharset, boolean theRespondGzip)
			throws UnsupportedEncodingException, IOException {
		return new StringWriter();
	}

	@Override
	public Response sendWriterResponse(int theStatus, String theContentType, String theCharset, Writer theWriter) {
		ResponseBuilder builder = buildResponse(theStatus);
		if (isNotBlank(theContentType)) {
			String charContentType = theContentType + "; charset=" + StringUtils.defaultIfBlank(theCharset, Constants.CHARSET_NAME_UTF8);
			builder.header(Constants.HEADER_CONTENT_TYPE, charContentType);
		}
		builder.entity(theWriter.toString());
		Response retVal = builder.build();
		return retVal;
	}

	@Override
	public Response sendAttachmentResponse(IBaseBinary bin, int statusCode, String contentType) throws IOException {
		ResponseBuilder response = buildResponse(statusCode);
		if (bin.getContent() != null && bin.getContent().length > 0) {
			response.header(Constants.HEADER_CONTENT_TYPE, contentType).entity(bin.getContent());
		}
		return response.build();
	}

	@Override
	public Response returnResponse(ParseAction<?> outcome, int operationStatus, boolean allowPrefer,
			MethodOutcome response, String resourceName) throws IOException {
		StringWriter writer = new StringWriter();
		if (outcome != null) {
			FhirContext fhirContext = getRequestDetails().getServer().getFhirContext();
			IParser parser = RestfulServerUtils.getNewParser(fhirContext, getRequestDetails());
			outcome.execute(parser, writer);
		}
		return sendWriterResponse(operationStatus, getParserType(), null, writer);
	}

	protected String getParserType() {
		EncodingEnum encodingEnum = RestfulServerUtils.determineResponseEncodingWithDefault(getRequestDetails()).getEncoding();
		return encodingEnum == EncodingEnum.JSON ? MediaType.APPLICATION_JSON : MediaType.APPLICATION_XML;
	}

	private ResponseBuilder buildResponse(int statusCode) {
		ResponseBuilder response = Response.status(statusCode);
		for (Entry<String, String> header : getHeaders().entrySet()) {
			response.header(header.getKey(), header.getValue());
		}
		return response;
	}

}
