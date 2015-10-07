package ca.uhn.fhir.jaxrs.server.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;

import ca.uhn.fhir.jaxrs.server.AbstractJaxRsRestServer;
import ca.uhn.fhir.jaxrs.server.JaxRsRestfulResponse;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.param.ResourceParameter;

public class JaxRsRequestDetails extends RequestDetails {

    private String theResourceString;
    private HttpHeaders headers;
    private AbstractJaxRsRestServer myServer;

    public AbstractJaxRsRestServer getServer() {
        return myServer;
    }

    public void setServer(AbstractJaxRsRestServer theServer) {
        this.myServer = theServer;
    }

    public JaxRsRequestDetails(HttpHeaders headers, String resourceString) {
        this.headers = headers;
        this.theResourceString = resourceString;
        setResponse(new JaxRsRestfulResponse(resourceString, this));
    }

    @Override
    public String getHeader(String headerIfNoneExist) {
        List<String> requestHeader = headers.getRequestHeader(headerIfNoneExist);
        return (requestHeader == null || requestHeader.size() == 0) ? null : requestHeader.get(0);
    }

    @Override
    public List<String> getHeaders(String name) {
        List<String> requestHeader = headers.getRequestHeader(name);
        return requestHeader == null ? Collections.<String> emptyList() : requestHeader;
    }

    @Override
    public String getServerBaseForRequest() {
        return getServer().getServerAddressStrategy().determineServerBase(null, null);
    }

	@Override
	protected byte[] getByteStreamRequestContents() {
	     return theResourceString.getBytes(ResourceParameter.determineRequestCharset(this));
	}
	
    @Override
    public Reader getReader()
            throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public InputStream getInputStream() {
        throw new UnsupportedOperationException();
    }	
}