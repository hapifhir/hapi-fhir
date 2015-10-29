package ca.uhn.fhir.jaxrs.server.util;

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

import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.method.ParseAction;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.RestfulResponse;
import ca.uhn.fhir.rest.server.RestfulServerUtils;

public class JaxRsResponse extends RestfulResponse<JaxRsRequest> {

	public JaxRsResponse(JaxRsRequest jaxRsRequestDetails) {
		super(jaxRsRequestDetails);
	}

	@Override
	public Writer getResponseWriter(int statusCode, String contentType, String charset, boolean respondGzip)
			throws UnsupportedEncodingException, IOException {
		return new StringWriter();
	}

	@Override
	public Response sendWriterResponse(int status, String contentType, String charset, Writer writer) {
		String charContentType = contentType + "; charset="
				+ StringUtils.defaultIfBlank(charset, Constants.CHARSET_NAME_UTF8);
		return buildResponse(status).header(Constants.HEADER_CONTENT_TYPE, charContentType).entity(writer.toString())
				.build();
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
			IParser parser = RestfulServerUtils.getNewParser(getRequestDetails().getServer().getFhirContext(),
					getRequestDetails());
			outcome.execute(parser, writer);
		}
		return sendWriterResponse(operationStatus, getParserType(), null, writer);
	}

	protected String getParserType() {
		EncodingEnum encodingEnum = RestfulServerUtils.determineResponseEncodingWithDefault(getRequestDetails());
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
