package ca.uhn.fhir.jaxrs.server.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;

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
 * @author Peter Van Houte
 */
public class JaxRsRequest extends RequestDetails {

	/**
	 * An implementation of the builder pattern for the JaxRsRequest
	 */
	public static class Builder {
		private String theResource;
		private AbstractJaxRsProvider theServer;
		private RequestTypeEnum theRequestType;
		private RestOperationTypeEnum theRestOperation;
		private String theId;
		private String theVersion;
		private String theCompartment;

		public Builder(AbstractJaxRsProvider theServer, RequestTypeEnum theRequestType,
				RestOperationTypeEnum theRestOperation) {
			this.theServer = theServer;
			this.theRequestType = theRequestType;
			this.theRestOperation = theRestOperation;
		}

		/**
		 * Set the resource
		 * @param resource the body contents of an http method 
		 * @return the builder
		 */
		public Builder resource(String resource) {
			this.theResource = resource;
			return this;
		}

		/**
		 * Set the id
		 * @param id the resource id
		 * @return the builder
		 */
		public Builder id(String id) {
			this.theId = id;
			return this;
		}

		/**
		 * Set the id version
		 * @param version the version of the resource
		 * @return the builder
		 */
		public Builder version(String version) {
			this.theVersion = version;
			return this;
		}

		/**
		 * Set the compartment
		 * @param compartment the compartment
		 * @return the builder
		 */
		public Builder compartment(String compartment) {
			this.theCompartment = compartment;
			return this;
		}

		/**
		 * Create the jax-rs request
		 * @return the jax-rs request
		 */
		public JaxRsRequest build() {
			JaxRsRequest result = new JaxRsRequest(theServer, theResource, theRequestType, theRestOperation);
			if ((StringUtils.isNotBlank(theVersion) || StringUtils.isNotBlank(theCompartment))
					&& StringUtils.isBlank(theId)) {
				throw new InvalidRequestException("Don't know how to handle request path: "
						+ theServer.getUriInfo().getRequestUri().toASCIIString());
			}

			if (StringUtils.isNotBlank(theVersion)) {
				result.setId(
						new IdDt(theServer.getBaseForRequest(), UrlUtil.unescape(theId), UrlUtil.unescape(theVersion)));
			} else if (StringUtils.isNotBlank(theId)) {
				result.setId(new IdDt(theServer.getBaseForRequest(), UrlUtil.unescape(theId)));
			}

			if (theRestOperation == RestOperationTypeEnum.UPDATE) {
				String contentLocation = result.getHeader(Constants.HEADER_CONTENT_LOCATION);
				if (contentLocation != null) {
					result.setId(new IdDt(contentLocation));
				}
			}
			
			result.setCompartmentName(theCompartment);			
			
			return result;
		}
	}

	private String theResourceString;
	private HttpHeaders headers;
	private AbstractJaxRsProvider myServer;

	public JaxRsRequest(AbstractJaxRsProvider server, String resourceString, RequestTypeEnum requestType,
			RestOperationTypeEnum restOperation) {
		this.headers = server.getHeaders();
		this.theResourceString = resourceString;
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
		return StringUtils.defaultIfEmpty(theResourceString, "")
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
}