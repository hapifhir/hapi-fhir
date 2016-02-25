package ca.uhn.fhir.jaxrs.server.util;

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

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;

import ca.uhn.fhir.jaxrs.server.AbstractJaxRsProvider;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.param.ResourceParameter;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.IRestfulResponse;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.UrlUtil;

/**
 * The JaxRsRequest is a jax-rs specific implementation of the RequestDetails. 
 * 
 * @author Peter Van Houte | peter.vanhoute@agfa.com | Agfa Healthcare
 */
public class JaxRsRequest extends RequestDetails {

	/**
	 * An implementation of the builder pattern for the JaxRsRequest
	 */
	public static class Builder {
		private String myResource;
		private AbstractJaxRsProvider myServer;
		private RequestTypeEnum myRequestType;
		private RestOperationTypeEnum myRestOperation;
		private String myId;
		private String myVersion;
		private String myCompartment;
		private String myRequestUrl;

		/**
		 * Utility Constructor
		 * @param theServer the server
		 * @param theRequestType the request type
		 * @param theRestOperation the rest operation
		 * @param theRequestUrl 
		 */
		public Builder(AbstractJaxRsProvider theServer, RequestTypeEnum theRequestType,
				RestOperationTypeEnum theRestOperation, String theRequestUrl) {
			this.myServer = theServer;
			this.myRequestType = theRequestType;
			this.myRestOperation = theRestOperation;
			this.myRequestUrl = theRequestUrl;
		}

		/**
		 * Set the resource
		 * @param resource the body contents of an http method 
		 * @return the builder
		 */
		public Builder resource(String resource) {
			this.myResource = resource;
			return this;
		}

		/**
		 * Set the id
		 * @param id the resource id
		 * @return the builder
		 */
		public Builder id(String id) {
			this.myId = id;
			return this;
		}

		/**
		 * Set the id version
		 * @param version the version of the resource
		 * @return the builder
		 */
		public Builder version(String version) {
			this.myVersion = version;
			return this;
		}

		/**
		 * Set the compartment
		 * @param compartment the compartment
		 * @return the builder
		 */
		public Builder compartment(String compartment) {
			this.myCompartment = compartment;
			return this;
		}

		/**
		 * Create the jax-rs request
		 * @return the jax-rs request
		 */
		public JaxRsRequest build() {
			JaxRsRequest result = new JaxRsRequest(myServer, myResource, myRequestType, myRestOperation);
			if ((StringUtils.isNotBlank(myVersion) || StringUtils.isNotBlank(myCompartment))
					&& StringUtils.isBlank(myId)) {
				throw new InvalidRequestException("Don't know how to handle request path: "
						+ myServer.getUriInfo().getRequestUri().toASCIIString());
			}

			if (StringUtils.isNotBlank(myVersion)) {
				result.setId(
						new IdDt(myServer.getBaseForRequest(), UrlUtil.unescape(myId), UrlUtil.unescape(myVersion)));
			} else if (StringUtils.isNotBlank(myId)) {
				result.setId(new IdDt(myServer.getBaseForRequest(), UrlUtil.unescape(myId)));
			}

			if (myRestOperation == RestOperationTypeEnum.UPDATE) {
				String contentLocation = result.getHeader(Constants.HEADER_CONTENT_LOCATION);
				if (contentLocation != null) {
					result.setId(new IdDt(contentLocation));
				}
			}
			
			result.setCompartmentName(myCompartment);			
			result.setCompleteUrl(myRequestUrl);
			
			return result;
		}
	}

	private String myResourceString;
	private HttpHeaders myHeaders;
	private AbstractJaxRsProvider myServer;

	/**
	 * Utility Constructor
	 * @param server the server
	 * @param resourceString the resource body
	 * @param requestType the request type
	 * @param restOperation the operation type
	 */
	public JaxRsRequest(AbstractJaxRsProvider server, String resourceString, RequestTypeEnum requestType,
			RestOperationTypeEnum restOperation) {
		this.myHeaders = server.getHeaders();
		this.myResourceString = resourceString;
		this.setRestOperationType(restOperation);
		setServer(server);
		setFhirServerBase(server.getBaseForServer());
		setParameters(server.getParameters());
		setRequestType(requestType);
	}

	@Override
	public AbstractJaxRsProvider getServer() {
		return myServer;
	}

	/**
	 * Set the server
	 * @param theServer the server to set
	 */
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
		List<String> requestHeader = myHeaders.getRequestHeader(name);
		return requestHeader == null ? Collections.<String> emptyList() : requestHeader;
	}

	@Override
	public String getServerBaseForRequest() {
		return getServer().getServerAddressStrategy().determineServerBase(null, null);
	}

	@Override
	protected byte[] getByteStreamRequestContents() {
		return StringUtils.defaultIfEmpty(myResourceString, "")
				.getBytes(ResourceParameter.determineRequestCharset(this));
	}

	@Override
	public IRestfulResponse getResponse() {
		if (super.getResponse() == null) {
			setResponse(new JaxRsResponse(this));
		}
		return super.getResponse();
	}

	@Override
	public Reader getReader() throws IOException {
		// not yet implemented
		throw new UnsupportedOperationException();
	}

	@Override
	public InputStream getInputStream() {
		// not yet implemented
		throw new UnsupportedOperationException();
	}

	@Override
	public Charset getCharset() {
		String charset = null;
		
		if(myHeaders.getMediaType() != null && myHeaders.getMediaType().getParameters() != null) {
			charset = myHeaders.getMediaType().getParameters().get(MediaType.CHARSET_PARAMETER);
		}
		if(charset != null) {
			return Charset.forName(charset);
		} else {
			return null;
		}
	}
}
