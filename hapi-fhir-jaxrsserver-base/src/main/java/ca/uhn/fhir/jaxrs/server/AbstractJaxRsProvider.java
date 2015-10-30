package ca.uhn.fhir.jaxrs.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jaxrs.server.util.JaxRsRequest;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.server.AddProfileTagEnum;
import ca.uhn.fhir.rest.server.ETagSupportEnum;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.HardcodedServerAddressStrategy;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.IRestfulServerDefaults;
import ca.uhn.fhir.rest.server.IServerAddressStrategy;

/**
 * Abstract Jax Rs Rest Server
 * @author Peter Van Houte
 *
 */
public abstract class AbstractJaxRsProvider implements IRestfulServerDefaults, IResourceProvider {

    public static FhirContext CTX = FhirContext.forDstu2();

    @Context
    private UriInfo theUriInfo;
    @Context
    private HttpHeaders theHeaders;

    @Override
	public FhirContext getFhirContext() {
        return CTX;
    }

    /** 
     * param and query methods 
     */
    public HashMap<String, String[]> getQueryMap() {
        MultivaluedMap<String, String> queryParameters = getUriInfo().getQueryParameters();
        HashMap<String, String[]> params = new HashMap<String, String[]>();
        for (Entry<String, List<String>> paramEntry : queryParameters.entrySet()) {
            params.put(paramEntry.getKey(), paramEntry.getValue().toArray(new String[paramEntry.getValue().size()]));
        }
        return params;
    }
    
    public IServerAddressStrategy getServerAddressStrategy() {
        HardcodedServerAddressStrategy addressStrategy = new HardcodedServerAddressStrategy();
        addressStrategy.setValue(getBaseForRequest());
        return addressStrategy; 
    }
    
    public String getBaseForServer() {
        return getUriInfo().getBaseUri().toASCIIString();
    }
    
    public String getBaseForRequest() {
        return getBaseForServer();
    }    

    protected JaxRsRequest createRequestDetails(final String resourceString, RequestTypeEnum requestType, RestOperationTypeEnum restOperation) {
        return new JaxRsRequest(this, resourceString, requestType, restOperation);
    }

	/**
	 * Get the uriInfo
	 * @return the uri info
	 */
	public UriInfo getUriInfo() {
		return this.theUriInfo;
	}
	
	/**
	 * Set the Uri Info
	 * @param uriInfo the uri info
	 */
	public void setUriInfo(UriInfo uriInfo) {
		this.theUriInfo = uriInfo;
	}	
	
	/**
	 * Get the headers
	 * @return the headers
	 */
	public HttpHeaders getHeaders() {
		return this.theHeaders;
	}
	
	/**
	 * Set the headers
	 * @param headers the headers to set 
	 */
	public void setHeaders(HttpHeaders headers) {
		this.theHeaders = headers;
	}

    /**
     * DEFAULT VALUES
     */
    @Override
	public EncodingEnum getDefaultResponseEncoding() {
        return EncodingEnum.JSON;
    }
    
    @Override
	public boolean isDefaultPrettyPrint() {
		return true;
	}

	@Override
	public ETagSupportEnum getETagSupport() {
		return ETagSupportEnum.DISABLED;
	}

	@Override
	public AddProfileTagEnum getAddProfileTag() {
		return AddProfileTagEnum.NEVER;
	}

	@Override
	public boolean isUseBrowserFriendlyContentTypes() {
		return true;
	}

}
