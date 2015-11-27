package ca.uhn.fhir.rest.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Set;

import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.method.ParseAction;

public interface IRestfulResponse {

    Object streamResponseAsResource(IBaseResource resource, boolean prettyPrint, Set<SummaryEnum> summaryMode, int operationStatus, boolean respondGzip, boolean addContentLocationHeader) 
    		throws IOException;
    Object streamResponseAsBundle(Bundle bundle, Set<SummaryEnum> summaryMode, boolean respondGzip, boolean requestIsBrowser) 
    		throws IOException;

    Object returnResponse(ParseAction<?> outcome, int operationStatus, boolean allowPrefer, MethodOutcome response, String resourceName) throws IOException;
    
    Writer getResponseWriter(int statusCode, String contentType, String charset, boolean respondGzip) throws UnsupportedEncodingException, IOException;
    Object sendWriterResponse(int status, String contentType, String charset, Writer writer) throws IOException;
    
    void addHeader(String headerKey, String headerValue);

    Object sendAttachmentResponse(IBaseBinary bin, int stausCode, String contentType) throws IOException;
}
