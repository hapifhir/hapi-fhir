package ca.uhn.fhir.jaxrs.server.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;

import ca.uhn.fhir.jaxrs.server.AbstractJaxRsProvider;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.param.ResourceParameter;
import ca.uhn.fhir.rest.server.IRestfulResponse;

public class JaxRsRequest extends RequestDetails {

    private String theResourceString;
    private HttpHeaders headers;
    private AbstractJaxRsProvider myServer;

	public JaxRsRequest() {
    }
    
    public JaxRsRequest(AbstractJaxRsProvider server, String resourceString,
			RequestTypeEnum requestType, RestOperationTypeEnum restOperation) {
    	this.headers = server.getHeaders();
        this.theResourceString = resourceString;
        this.setRestOperationType(restOperation);
        setServer(server);
        setFhirServerBase(server.getBaseUri());
        setParameters(server.getQueryMap());
        setRequestType(requestType);
	}

	@Override
	public AbstractJaxRsProvider getServer() {
        return myServer;
    }

    public void setServer(AbstractJaxRsProvider theServer) {
        this.myServer = theServer;
    }

    @Override
    public String getHeader(String headerKey) {
        List<String> requestHeader = getHeaders(headerKey);
        return requestHeader.isEmpty() ? null : requestHeader.get(0);
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
	public IRestfulResponse getResponse() {
		if(super.getResponse() == null) {
	        setResponse(new JaxRsResponse(this));
		}
		return super.getResponse();
	}	
	
    @Override
    public Reader getReader()
            throws IOException {
    	// not yet implemented
        throw new UnsupportedOperationException();
    }

    @Override
    public InputStream getInputStream() {
    	// not yet implemented    	
        throw new UnsupportedOperationException();
    }	
}