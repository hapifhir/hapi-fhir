package ca.uhn.fhir.jaxrs.server;

/*
 * #%L
 * HAPI FHIR JAX-RS Server
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jaxrs.server.util.JaxRsRequest;
import ca.uhn.fhir.jaxrs.server.util.JaxRsRequest.Builder;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.server.AddProfileTagEnum;
import ca.uhn.fhir.rest.server.ETagSupportEnum;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.HardcodedServerAddressStrategy;
import ca.uhn.fhir.rest.server.IRestfulServerDefaults;
import ca.uhn.fhir.rest.server.IServerAddressStrategy;

/**
 * This is the abstract superclass for all jaxrs providers. It contains some defaults implementing
 * the IRestfulServerDefaults interface and exposes the uri and headers.
 * 
 * @author Peter Van Houte | peter.vanhoute@agfa.com | Agfa Healthcare
 */
public abstract class AbstractJaxRsProvider implements IRestfulServerDefaults {

    /** a static initialization for the fhircontext. Only DSTU2 is supported */
   private static final FhirContext CTX = FhirContext.forDstu2();

    /** the uri info */
    @Context
    private UriInfo theUriInfo;
    /** the http headers */
    @Context
    private HttpHeaders theHeaders;

    @Override
	public FhirContext getFhirContext() {
        return CTX;
    }

    /**
     * This method returns the query parameters
     * @return the query parameters
     */
    public Map<String, String[]> getParameters() {
        MultivaluedMap<String, String> queryParameters = getUriInfo().getQueryParameters();
        HashMap<String, String[]> params = new HashMap<String, String[]>();
        for (Entry<String, List<String>> paramEntry : queryParameters.entrySet()) {
            params.put(paramEntry.getKey(), paramEntry.getValue().toArray(new String[paramEntry.getValue().size()]));
        }
        return params;
    }
    
    /**
     * This method returns the default server address strategy. The default strategy return the 
     * base uri for the request {@link AbstractJaxRsProvider#getBaseForRequest() getBaseForRequest()} 
     * @return
     */
    public IServerAddressStrategy getServerAddressStrategy() {
        HardcodedServerAddressStrategy addressStrategy = new HardcodedServerAddressStrategy();
        addressStrategy.setValue(getBaseForRequest());
        return addressStrategy; 
    }

    /**
     * This method returns the server base, independent of the request or resource.
     * @see javax.ws.rs.core.UriInfo#getBaseUri()
     * @return the ascii string for the server base
     */ 
    public String getBaseForServer() {
        return getUriInfo().getBaseUri().toASCIIString();
    }
    
    /**
     * This method returns the server base, including the resource path.
     * {@link javax.ws.rs.core.UriInfo#getBaseUri() UriInfo#getBaseUri()} 
     * @return the ascii string for the base resource provider path
     */    
    public String getBaseForRequest() {
        return getBaseForServer();
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
	 * Return the requestbuilder for the server
	 * @param requestType the type of the request
	 * @param restOperation the rest operation type
	 * @return the requestbuilder
	 */
	public Builder getRequest(RequestTypeEnum requestType, RestOperationTypeEnum restOperation) {
		return new JaxRsRequest.Builder(this, requestType, restOperation, theUriInfo.getRequestUri().toString());
	}	

    /**
     * DEFAULT = EncodingEnum.JSON
     */
    @Override
	public EncodingEnum getDefaultResponseEncoding() {
        return EncodingEnum.JSON;
    }
    
    /**
     * DEFAULT = true
     */
    @Override
	public boolean isDefaultPrettyPrint() {
		return true;
	}

    /**
     * DEFAULT = ETagSupportEnum.DISABLED
     */
	@Override
	public ETagSupportEnum getETagSupport() {
		return ETagSupportEnum.DISABLED;
	}

    /**
     * DEFAULT = AddProfileTagEnum.NEVER
     */
	@Override
	public AddProfileTagEnum getAddProfileTag() {
		return AddProfileTagEnum.NEVER;
	}

	   /**
     * DEFAULT = false
     */
	@Override
	public boolean isUseBrowserFriendlyContentTypes() {
		return true;
	}

}
