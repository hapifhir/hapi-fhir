package ca.uhn.fhir.rest.server.servlet;

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
            return null;
        } else {
            theHttpResponse.setContentLength(bin.getContent().length);
            ServletOutputStream oos = theHttpResponse.getOutputStream();
            oos.write(bin.getContent());
            oos.close();
            return null;
        }
    }    

    @Override
    public Writer getResponseWriter(int statusCode, String contentType, String charset, boolean theRespondGzip) throws UnsupportedEncodingException, IOException {
        addHeaders();
        HttpServletResponse theHttpResponse = getRequestDetails().getServletResponse();
        theHttpResponse.setCharacterEncoding(charset);
        theHttpResponse.setStatus(statusCode);
        theHttpResponse.setContentType(contentType);        
		if (theRespondGzip) {
			theHttpResponse.addHeader(Constants.HEADER_CONTENT_ENCODING, Constants.ENCODING_GZIP);
			return new OutputStreamWriter(new GZIPOutputStream(theHttpResponse.getOutputStream()), Constants.CHARSET_NAME_UTF8);
		} else {
			return theHttpResponse.getWriter();
		}
    }


    private void addHeaders() {
        HttpServletResponse theHttpResponse = getRequestDetails().getServletResponse();
        getRequestDetails().getServer().addHeadersToResponse(theHttpResponse);
        for (Entry<String, String> header : getHeaders().entrySet()) {
            theHttpResponse.setHeader(header.getKey(), header.getValue());
        }
    }
    
	public final Object sendWriterResponse(int status, String contentType, String charset, Writer writer) throws IOException {
        writer.close();
        return null;
    }

    @Override
    public Object returnResponse(ParseAction<?> outcome, int operationStatus, boolean allowPrefer, MethodOutcome response,
            String resourceName) throws IOException {
        return getRequestDetails().getServer().returnResponse(getRequestDetails(), outcome, operationStatus, allowPrefer, response, resourceName);
    }
}
