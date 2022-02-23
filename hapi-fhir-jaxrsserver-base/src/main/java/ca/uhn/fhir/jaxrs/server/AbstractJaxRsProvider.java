package ca.uhn.fhir.jaxrs.server;

import java.io.IOException;
/*
 * #%L
 * HAPI FHIR JAX-RS Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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
import java.util.*;
import java.util.Map.Entry;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import ca.uhn.fhir.interceptor.api.IInterceptorService;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.commons.lang3.StringUtils;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.api.AddProfileTagEnum;
import ca.uhn.fhir.jaxrs.server.interceptor.JaxRsExceptionInterceptor;
import ca.uhn.fhir.jaxrs.server.interceptor.JaxRsResponseException;
import ca.uhn.fhir.jaxrs.server.util.JaxRsRequest;
import ca.uhn.fhir.jaxrs.server.util.JaxRsRequest.Builder;
import ca.uhn.fhir.rest.api.*;
import ca.uhn.fhir.rest.server.*;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;

/**
 * This is the abstract superclass for all jaxrs providers. It contains some defaults implementing
 * the IRestfulServerDefaults interface and exposes the uri and headers.
 *
 * @author Peter Van Houte | peter.vanhoute@agfa.com | Agfa Healthcare
 */
public abstract class AbstractJaxRsProvider implements IRestfulServerDefaults {

	private static final String ERROR = "error";

	private static final String PROCESSING = "processing";

	private final FhirContext CTX;
	/** the http headers */
	@Context
	private HttpHeaders myHeaders;

	/** the uri info */
	@Context
	private UriInfo myUriInfo;

	/**
	 * Default is DSTU2. Use {@link AbstractJaxRsProvider#AbstractJaxRsProvider(FhirContext)} to specify a DSTU3 context.
	 */
	protected AbstractJaxRsProvider() {
		CTX = FhirContext.forDstu2();
	}

	/**
	 *
	 * @param ctx
	 *           the {@link FhirContext} to support.
	 */
	protected AbstractJaxRsProvider(final FhirContext ctx) {
		CTX = ctx;
	}

	@Override
	public IInterceptorService getInterceptorService() {
		return null;
	}

	/**
	 * DEFAULT = AddProfileTagEnum.NEVER
	 */
	@Override
	public AddProfileTagEnum getAddProfileTag() {
		return AddProfileTagEnum.NEVER;
	}

	/**
	 * This method returns the server base, including the resource path.
	 * {@link UriInfo#getBaseUri() UriInfo#getBaseUri()}
	 * 
	 * @return the ascii string for the base resource provider path
	 */
	public String getBaseForRequest() {
		return getBaseForServer();
	}

	/**
	 * This method returns the server base, independent of the request or resource.
	 * 
	 * @see javax.ws.rs.core.UriInfo#getBaseUri()
	 * @return the ascii string for the server base
	 */
	public String getBaseForServer() {
		final String url = getUriInfo().getBaseUri().toASCIIString();
		return StringUtils.isNotBlank(url) && url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
	}

	/**
	 * DEFAULT = EncodingEnum.JSON
	 */
	@Override
	public EncodingEnum getDefaultResponseEncoding() {
		return EncodingEnum.JSON;
	}

	/**
	 * DEFAULT = ETagSupportEnum.DISABLED
	 */
	@Override
	public ETagSupportEnum getETagSupport() {
		return ETagSupportEnum.DISABLED;
	}

	/**
	 * DEFAULT = {@link ElementsSupportEnum#STANDARD}
	 */
	@Override
	public ElementsSupportEnum getElementsSupport() {
		return ElementsSupportEnum.STANDARD;
	}

	@Override
	public FhirContext getFhirContext() {
		return CTX;
	}

	/**
	 * Get the headers
	 * 
	 * @return the headers
	 */
	public HttpHeaders getHeaders() {
		return this.myHeaders;
	}

	/**
	 * Default: an empty list of interceptors (Interceptors are not yet supported
	 * in the JAX-RS server). Please get in touch if you'd like to help!
	 * 
	 * @see ca.uhn.fhir.rest.server.IRestfulServerDefaults#getInterceptors_()
	 */
	@Override
	public List<IServerInterceptor> getInterceptors_() {
		return Collections.emptyList();
	}

	/**
	 * By default, no paging provider is used
	 */
	@Override
	public IPagingProvider getPagingProvider() {
		return null;
	}

	/**
	 * This method returns the query parameters
	 * 
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
	 * Return the requestbuilder for the server
	 * 
	 * @param requestType
	 *           the type of the request
	 * @param restOperation
	 *           the rest operation type
	 * @return the requestbuilder
	 */
	public Builder getRequest(final RequestTypeEnum requestType, final RestOperationTypeEnum restOperation) {
		return getRequest(requestType, restOperation, null);
	}

	/**
	 * Return the requestbuilder for the server
	 * 
	 * @param requestType
	 *           the type of the request
	 * @param restOperation
	 *           the rest operation type
	 * @param theResourceName
	 *           the resource name
	 * @return the requestbuilder
	 */
	public Builder getRequest(final RequestTypeEnum requestType, final RestOperationTypeEnum restOperation, final String theResourceName) {
		return new JaxRsRequest.Builder(this, requestType, restOperation, myUriInfo.getRequestUri().toString(), theResourceName);
	}

	/**
	 * This method returns the default server address strategy. The default strategy return the
	 * base uri for the request {@link AbstractJaxRsProvider#getBaseForRequest() getBaseForRequest()}
	 * 
	 * @return
	 */
	public IServerAddressStrategy getServerAddressStrategy() {
		final HardcodedServerAddressStrategy addressStrategy = new HardcodedServerAddressStrategy();
		addressStrategy.setValue(getBaseForRequest());
		return addressStrategy;
	}

	/**
	 * Get the uriInfo
	 * 
	 * @return the uri info
	 */
	public UriInfo getUriInfo() {
		return this.myUriInfo;
	}

	/**
	 * Convert an exception to a response
	 * 
	 * @param theRequest
	 *           the incoming request
	 * @param theException
	 *           the exception to convert
	 * @return response
	 * @throws IOException
	 */
	public Response handleException(final JaxRsRequest theRequest, final Throwable theException)
			throws IOException {
		if (theException instanceof JaxRsResponseException) {
			return new JaxRsExceptionInterceptor().convertExceptionIntoResponse(theRequest, (JaxRsResponseException) theException);
		} else {
			return new JaxRsExceptionInterceptor().convertExceptionIntoResponse(theRequest,
					new JaxRsExceptionInterceptor().convertException(this, theException));
		}
	}

	/**
	 * DEFAULT = true
	 */
	@Override
	public boolean isDefaultPrettyPrint() {
		return true;
	}

	/**
	 * Set the headers
	 * 
	 * @param headers
	 *           the headers to set
	 */
	public void setHeaders(final HttpHeaders headers) {
		this.myHeaders = headers;
	}

	/**
	 * Set the Uri Info
	 * 
	 * @param uriInfo
	 *           the uri info
	 */
	public void setUriInfo(final UriInfo uriInfo) {
		this.myUriInfo = uriInfo;
	}

	/**
	 * DEFAULT = false
	 */
	public boolean withStackTrace() {
		return false;
	}
}
