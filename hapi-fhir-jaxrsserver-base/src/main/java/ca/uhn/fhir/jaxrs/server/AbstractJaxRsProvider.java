package ca.uhn.fhir.jaxrs.server;

import java.io.IOException;
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
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jaxrs.server.interceptor.JaxRsExceptionInterceptor;
import ca.uhn.fhir.jaxrs.server.interceptor.JaxRsResponseException;
import ca.uhn.fhir.jaxrs.server.util.JaxRsRequest;
import ca.uhn.fhir.jaxrs.server.util.JaxRsRequest.Builder;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.server.AddProfileTagEnum;
import ca.uhn.fhir.rest.server.ETagSupportEnum;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.HardcodedServerAddressStrategy;
import ca.uhn.fhir.rest.server.IRestfulServerDefaults;
import ca.uhn.fhir.rest.server.IServerAddressStrategy;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.util.OperationOutcomeUtil;

/**
 * This is the abstract superclass for all jaxrs providers. It contains some defaults implementing
 * the IRestfulServerDefaults interface and exposes the uri and headers.
 *
 * @author Peter Van Houte | peter.vanhoute@agfa.com | Agfa Healthcare
 */
public abstract class AbstractJaxRsProvider implements IRestfulServerDefaults {

    private final FhirContext CTX;

    private static final String PROCESSING = "processing";
    private static final String ERROR = "error";

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
     * Default is DSTU2.  Use {@link AbstractJaxRsProvider#AbstractJaxRsProvider(FhirContext)} to specify a DSTU3 context.
     */
    protected AbstractJaxRsProvider() {
        CTX = FhirContext.forDstu2();
    }

    /**
     *
     * @param ctx the {@link FhirContext} to support.
     */
    protected AbstractJaxRsProvider(final FhirContext ctx) {
        CTX = ctx;
    }

    /**
     * This method returns the query parameters
     * @return the query parameters
     */
    public Map<String, String[]> getParameters() {
        final MultivaluedMap<String, String> queryParameters = getUriInfo().getQueryParameters();
        final HashMap<String, String[]> params = new HashMap<String, String[]>();
        for (final Entry<String, List<String>> paramEntry : queryParameters.entrySet()) {
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
        final HardcodedServerAddressStrategy addressStrategy = new HardcodedServerAddressStrategy();
        addressStrategy.setValue(getBaseForRequest());
        return addressStrategy;
    }

    /**
     * This method returns the server base, independent of the request or resource.
     * @see javax.ws.rs.core.UriInfo#getBaseUri()
     * @return the ascii string for the server base
     */
    public String getBaseForServer() {
        final String url = getUriInfo().getBaseUri().toASCIIString();
        return StringUtils.isNotBlank(url) && url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
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
     * Default: an empty list of interceptors (Interceptors are not yet supported 
     * in the JAX-RS server). Please get in touch if you'd like to help!
     * 
     * @see ca.uhn.fhir.rest.server.IRestfulServer#getInterceptors()
     */
    @Override
    public List<IServerInterceptor> getInterceptors() {
        return Collections.emptyList();
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
    public void setUriInfo(final UriInfo uriInfo) {
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
    public void setHeaders(final HttpHeaders headers) {
        this.theHeaders = headers;
    }

    /**
     * Return the requestbuilder for the server
     * @param requestType the type of the request
     * @param restOperation the rest operation type
     * @param theResourceName the resource name
     * @return the requestbuilder
     */
    public Builder getRequest(final RequestTypeEnum requestType, final RestOperationTypeEnum restOperation, final String theResourceName) {
        return new JaxRsRequest.Builder(this, requestType, restOperation, theUriInfo.getRequestUri().toString(), theResourceName);
    }

    /**
     * Return the requestbuilder for the server
     * @param requestType the type of the request
     * @param restOperation the rest operation type
     * @return the requestbuilder
     */
    public Builder getRequest(final RequestTypeEnum requestType, final RestOperationTypeEnum restOperation) {
        return getRequest(requestType, restOperation, null);
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

    /**
     * DEFAULT = false
     */
    public boolean withStackTrace() {
        return false;
    }

    /**
     * Convert an exception to a response
     * @param theRequest the incoming request
     * @param theException the exception to convert
     * @return response
     * @throws IOException
     */
    public Response handleException(final JaxRsRequest theRequest, final Throwable theException)
            throws IOException {
        if (theException instanceof JaxRsResponseException) {
            return new JaxRsExceptionInterceptor().convertExceptionIntoResponse(theRequest, (JaxRsResponseException) theException);
        } else if (theException instanceof DataFormatException) {
            return new JaxRsExceptionInterceptor().convertExceptionIntoResponse(theRequest, new JaxRsResponseException(
                    new InvalidRequestException(theException.getMessage(), createOutcome((DataFormatException) theException))));
        } else {
            return new JaxRsExceptionInterceptor().convertExceptionIntoResponse(theRequest,
                    new JaxRsExceptionInterceptor().convertException(this, theException));
        }
    }

    private IBaseOperationOutcome createOutcome(final DataFormatException theException) {
        final IBaseOperationOutcome oo = OperationOutcomeUtil.newInstance(getFhirContext());
        final String detailsValue = theException.getMessage() + "\n\n" + ExceptionUtils.getStackTrace(theException);
        OperationOutcomeUtil.addIssue(getFhirContext(), oo, ERROR, detailsValue, null, PROCESSING);
        return oo;
    }
}
