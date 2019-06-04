package ca.uhn.fhir.jaxrs.server.util;

/*
 * #%L
 * HAPI FHIR JAX-RS Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.jaxrs.server.AbstractJaxRsProvider;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IRestfulResponse;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.method.ResourceParameter;
import ca.uhn.fhir.util.UrlUtil;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The JaxRsRequest is a jax-rs specific implementation of the RequestDetails.
 *
 * @author Peter Van Houte | peter.vanhoute@agfa.com | Agfa Healthcare
 */
public class JaxRsRequest extends RequestDetails {

	private HttpHeaders myHeaders;
	private String myResourceString;
	private AbstractJaxRsProvider myServer;
	private Map<String, Object> myAttributes = new HashMap<>();

	/**
	 * Utility Constructor
	 *
	 * @param server         the server
	 * @param resourceString the resource body
	 * @param requestType    the request type
	 * @param restOperation  the operation type
	 */
	public JaxRsRequest(AbstractJaxRsProvider server, String resourceString, RequestTypeEnum requestType,
							  RestOperationTypeEnum restOperation) {
		super(server.getInterceptorService());
		this.myHeaders = server.getHeaders();
		this.myResourceString = resourceString;
		this.setRestOperationType(restOperation);
		setServer(server);
		setFhirServerBase(server.getBaseForServer());
		setParameters(server.getParameters());
		setRequestType(requestType);
	}

	@Override
	protected byte[] getByteStreamRequestContents() {
		return StringUtils.defaultString(myResourceString, "")
			.getBytes(ResourceParameter.determineRequestCharset(this));
	}

	@Override
	public Charset getCharset() {
		String charset = null;

		if (myHeaders.getMediaType() != null && myHeaders.getMediaType().getParameters() != null) {
			charset = myHeaders.getMediaType().getParameters().get(MediaType.CHARSET_PARAMETER);
		}
		if (charset != null) {
			return Charset.forName(charset);
		} else {
			return null;
		}
	}

	@Override
	public FhirContext getFhirContext() {
		return myServer.getFhirContext();
	}

	@Override
	public String getHeader(String headerKey) {
		List<String> requestHeader = getHeaders(headerKey);
		return requestHeader.isEmpty() ? null : requestHeader.get(0);
	}

	@Override
	public List<String> getHeaders(String name) {
		List<String> requestHeader = myHeaders.getRequestHeader(name);
		return requestHeader == null ? Collections.<String>emptyList() : requestHeader;
	}

	@Override
	public Object getAttribute(String theAttributeName) {
		return myAttributes.get(theAttributeName);
	}

	@Override
	public void setAttribute(String theAttributeName, Object theAttributeValue) {
		myAttributes.put(theAttributeName, theAttributeValue);
	}

	@Override
	public InputStream getInputStream() {
		// not yet implemented
		throw new UnsupportedOperationException();
	}

	@Override
	public Reader getReader() throws IOException {
		// not yet implemented
		throw new UnsupportedOperationException();
	}

	@Override
	public IRestfulResponse getResponse() {
		if (super.getResponse() == null) {
			setResponse(new JaxRsResponse(this));
		}
		return super.getResponse();
	}

	@Override
	public AbstractJaxRsProvider getServer() {
		return myServer;
	}

	/**
	 * Set the server
	 *
	 * @param theServer the server to set
	 */
	public void setServer(AbstractJaxRsProvider theServer) {
		this.myServer = theServer;
	}

	@Override
	public String getServerBaseForRequest() {
		return getServer().getServerAddressStrategy().determineServerBase(null, null);
	}

	/**
	 * An implementation of the builder pattern for the JaxRsRequest
	 */
	public static class Builder {
		private final String myResourceName;
		private String myCompartment;
		private String myId;
		private RequestTypeEnum myRequestType;
		private String myRequestUrl;
		private String myResource;
		private RestOperationTypeEnum myRestOperation;
		private AbstractJaxRsProvider myServer;
		private String myVersion;

		/**
		 * Utility Constructor
		 *
		 * @param theServer        the server
		 * @param theRequestType   the request type
		 * @param theRestOperation the rest operation
		 * @param theRequestUrl
		 */
		public Builder(AbstractJaxRsProvider theServer, RequestTypeEnum theRequestType,
							RestOperationTypeEnum theRestOperation, String theRequestUrl, String theResourceName) {
			this.myServer = theServer;
			this.myRequestType = theRequestType;
			this.myRestOperation = theRestOperation;
			this.myRequestUrl = theRequestUrl;
			this.myResourceName = theResourceName;
		}

		/**
		 * Create the jax-rs request
		 *
		 * @return the jax-rs request
		 */
		public JaxRsRequest build() {
			JaxRsRequest result = new JaxRsRequest(myServer, myResource, myRequestType, myRestOperation);
			if ((StringUtils.isNotBlank(myVersion) || StringUtils.isNotBlank(myCompartment))
				&& StringUtils.isBlank(myId)) {
				throw new InvalidRequestException("Don't know how to handle request path: "
					+ myServer.getUriInfo().getRequestUri().toASCIIString());
			}

			FhirVersionEnum fhirContextVersion = myServer.getFhirContext().getVersion().getVersion();

			if (StringUtils.isNotBlank(myVersion)) {
				switch (fhirContextVersion) {
					case R4:
						result.setId(new org.hl7.fhir.r4.model.IdType(myServer.getBaseForRequest(), UrlUtil.unescape(myId), UrlUtil.unescape(myVersion)));
						break;
					case DSTU3:
						result.setId(new org.hl7.fhir.dstu3.model.IdType(myServer.getBaseForRequest(), UrlUtil.unescape(myId), UrlUtil.unescape(myVersion)));
						break;
					case DSTU2_1:
						result.setId(new org.hl7.fhir.dstu2016may.model.IdType(myServer.getBaseForRequest(), UrlUtil.unescape(myId), UrlUtil.unescape(myVersion)));
						break;
					case DSTU2_HL7ORG:
						result.setId(new org.hl7.fhir.instance.model.IdType(myServer.getBaseForRequest(), UrlUtil.unescape(myId), UrlUtil.unescape(myVersion)));
						break;
					case DSTU2:
						result.setId(new ca.uhn.fhir.model.primitive.IdDt(myServer.getBaseForRequest(), UrlUtil.unescape(myId), UrlUtil.unescape(myVersion)));
						break;
					default:
						throw new ConfigurationException("Unsupported Fhir version: " + fhirContextVersion);
				}
			} else if (StringUtils.isNotBlank(myId)) {
				switch (fhirContextVersion) {
					case R4:
						result.setId(new org.hl7.fhir.r4.model.IdType(myServer.getBaseForRequest(), UrlUtil.unescape(myId)));
						break;
					case DSTU3:
						result.setId(new org.hl7.fhir.dstu3.model.IdType(myServer.getBaseForRequest(), UrlUtil.unescape(myId)));
						break;
					case DSTU2_1:
						result.setId(new org.hl7.fhir.dstu2016may.model.IdType(myServer.getBaseForRequest(), UrlUtil.unescape(myId)));
						break;
					case DSTU2_HL7ORG:
						result.setId(new org.hl7.fhir.instance.model.IdType(myServer.getBaseForRequest(), UrlUtil.unescape(myId)));
						break;
					case DSTU2:
						result.setId(new ca.uhn.fhir.model.primitive.IdDt(myServer.getBaseForRequest(), UrlUtil.unescape(myId)));
						break;
					default:
						throw new ConfigurationException("Unsupported Fhir version: " + fhirContextVersion);
				}
			}

			if (myRestOperation == RestOperationTypeEnum.UPDATE) {
				String contentLocation = result.getHeader(Constants.HEADER_CONTENT_LOCATION);
				if (contentLocation != null) {
					switch (fhirContextVersion) {
						case R4:
							result.setId(new org.hl7.fhir.r4.model.IdType(contentLocation));
							break;
						case DSTU3:
							result.setId(new org.hl7.fhir.dstu3.model.IdType(contentLocation));
							break;
						case DSTU2_1:
							result.setId(new org.hl7.fhir.dstu2016may.model.IdType(contentLocation));
							break;
						case DSTU2_HL7ORG:
							result.setId(new org.hl7.fhir.instance.model.IdType(contentLocation));
							break;
						case DSTU2:
							result.setId(new ca.uhn.fhir.model.primitive.IdDt(contentLocation));
							break;
						default:
							throw new ConfigurationException("Unsupported Fhir version: " + fhirContextVersion);
					}
				}
			}

			result.setCompartmentName(myCompartment);
			result.setCompleteUrl(myRequestUrl);
			result.setResourceName(myResourceName);

			return result;
		}

		/**
		 * Set the compartment
		 *
		 * @param compartment the compartment
		 * @return the builder
		 */
		public Builder compartment(String compartment) {
			this.myCompartment = compartment;
			return this;
		}

		/**
		 * Set the id
		 *
		 * @param id the resource id
		 * @return the builder
		 */
		public Builder id(String id) {
			this.myId = id;
			return this;
		}

		/**
		 * Set the resource
		 *
		 * @param resource the body contents of an http method
		 * @return the builder
		 */
		public Builder resource(String resource) {
			this.myResource = resource;
			return this;
		}

		/**
		 * Set the id version
		 *
		 * @param version the version of the resource
		 * @return the builder
		 */
		public Builder version(String version) {
			this.myVersion = version;
			return this;
		}
	}
}
