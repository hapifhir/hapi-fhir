package ca.uhn.fhir.jaxrs.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Map.Entry;
import java.util.zip.GZIPOutputStream;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.hl7.fhir.instance.model.api.IBaseBinary;

import ca.uhn.fhir.jaxrs.server.util.JaxRsRequestDetails;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.method.ParseAction;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.RestfulResponse;
import ca.uhn.fhir.rest.server.RestfulServerUtils;

public class JaxRsResponse extends RestfulResponse<JaxRsRequestDetails> {

    public JaxRsResponse(String resourceString, JaxRsRequestDetails jaxRsRequestDetails) {
        super(jaxRsRequestDetails);
    }

    @Override
    public Writer getResponseWriter(int statusCode, String contentType, String charset, boolean respondGzip)
            throws UnsupportedEncodingException, IOException {
        if (respondGzip) {
            addHeader(Constants.HEADER_CONTENT_ENCODING, Constants.ENCODING_GZIP);
            return new OutputStreamWriter(new GZIPOutputStream(new ByteArrayOutputStream()), Constants.CHARSET_NAME_UTF8);
        } else {
            return new StringWriter();
        }
    }

    @Override
    public Response sendWriterResponse(int status, String contentType, String charset, Writer writer) {
        return Response.status(status)/*.header(HttpHeaders.CONTENT_TYPE, charset)*/.header(Constants.HEADER_CONTENT_TYPE, contentType).entity(writer.toString()).build();
    }

    @Override
    public Object sendAttachmentResponse(IBaseBinary bin, int statusCode, String contentType) throws IOException {
        ResponseBuilder response = Response.status(statusCode);
        for (Entry<String, String> header : getHeaders().entrySet()) {
        	response.header(header.getKey(), header.getValue());
        }
        if (bin.getContent() != null && bin.getContent().length > 0) {
        	response.header(Constants.HEADER_CONTENT_TYPE, contentType).entity(bin.getContent());            
        }
        return response.build();
    }

    @Override
    public Object returnResponse(ParseAction<?> outcome, int operationStatus, boolean allowPrefer, MethodOutcome response,
            String resourceName)
                    throws IOException {
        Writer writer = new StringWriter();
        IParser parser = RestfulServerUtils.getNewParser(getRequestDetails().getServer().getFhirContext(), getRequestDetails());
        if(outcome != null) {
            outcome.execute(parser, writer);
        }
        return Response.status(operationStatus).header(Constants.HEADER_CONTENT_TYPE, getParserType()).entity(writer.toString()).build();
    }
    
    protected String getParserType() {
    	EncodingEnum encodingEnum = RestfulServerUtils.determineResponseEncodingWithDefault(getRequestDetails());
    	return encodingEnum == EncodingEnum.JSON ? MediaType.APPLICATION_JSON : MediaType.APPLICATION_XML;  
    }

}
