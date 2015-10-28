package ca.uhn.fhir.jaxrs.server;

import java.util.HashMap;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jaxrs.server.util.JaxRsRequest;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.server.AddProfileTagEnum;
import ca.uhn.fhir.rest.server.ETagSupportEnum;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.HardcodedServerAddressStrategy;
import ca.uhn.fhir.rest.server.IRestfulServerDefaults;
import ca.uhn.fhir.rest.server.IServerAddressStrategy;
import ca.uhn.fhir.rest.server.RestfulServerUtils;

/**
 * Abstract Jax Rs Rest Server
 * @author Peter Van Houte
 *
 */
public abstract class AbstractJaxRsProvider implements IRestfulServerDefaults {

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
        MultivaluedMap<String, String> queryParameters = getInfo().getQueryParameters();
        HashMap<String, String[]> params = new HashMap<String, String[]>();
        for (String key : queryParameters.keySet()) {
            params.put(key, queryParameters.get(key).toArray(new String[] {}));
        }
        return params;
    }
    
    public IServerAddressStrategy getServerAddressStrategy() {
        HardcodedServerAddressStrategy addressStrategy = new HardcodedServerAddressStrategy();
        addressStrategy.setValue(getBaseUri());
        return addressStrategy; 
    }    

    public String getBaseUri() {
        return getInfo().getBaseUri().toASCIIString();
    }

	/**
     * PARSING METHODS
     */
    public IParser getParser(JaxRsRequest theRequestDetails) {
    	return RestfulServerUtils.getNewParser(getFhirContext(), theRequestDetails);
    }

    protected JaxRsRequest createRequestDetails(final String resourceString, RequestTypeEnum requestType, RestOperationTypeEnum restOperation) {
        return new JaxRsRequest(this, resourceString, requestType, restOperation);
    }

	/**
	 * Get the info
	 * @return the info
	 */
	public UriInfo getInfo() {
		return theUriInfo;
	}
	
	/**
	 * Get the headers
	 * @return the headers
	 */
	public HttpHeaders getHeaders() {
		return theHeaders;
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
