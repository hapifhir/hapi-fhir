package ca.uhn.fhir.rest.server.servlet;

/*
 * #%L
 * HAPI FHIR - Server Framework
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.*;
import java.util.zip.GZIPInputStream;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ServletRequestDetails extends RequestDetails {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ServletRequestDetails.class);

	private RestfulServer myServer;
	private HttpServletRequest myServletRequest;
	private HttpServletResponse myServletResponse;

	/**
	 * Constructor for testing only
	 */
	public ServletRequestDetails() {
		this((IInterceptorBroadcaster)null);
	}

	/**
	 * Constructor
	 */
	public ServletRequestDetails(IInterceptorBroadcaster theInterceptorBroadcaster) {
		super(theInterceptorBroadcaster);
		setResponse(new ServletRestfulResponse(this));
	}

	/**
	 * Copy constructor
	 */
	public ServletRequestDetails(ServletRequestDetails theRequestDetails) {
		super(theRequestDetails);

		myServer = theRequestDetails.getServer();
		myServletRequest = theRequestDetails.getServletRequest();
		myServletResponse = theRequestDetails.getServletResponse();
	}

	@Override
	protected byte[] getByteStreamRequestContents() {
		try {
			InputStream inputStream = getInputStream();
			byte[] requestContents = IOUtils.toByteArray(inputStream);

			if (myServer.isUncompressIncomingContents()) {
				String contentEncoding = myServletRequest.getHeader(Constants.HEADER_CONTENT_ENCODING);
				if ("gzip".equals(contentEncoding)) {
					ourLog.debug("Uncompressing (GZip) incoming content");
					if (requestContents.length > 0) {
						GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(requestContents));
						requestContents = IOUtils.toByteArray(gis);
					}
				}
			}
			return requestContents;
		} catch (IOException e) {
			ourLog.error("Could not load request resource", e);
			throw new InvalidRequestException(Msg.code(308) + String.format("Could not load request resource: %s", e.getMessage()));
		}
	}

	@Override
	public Charset getCharset() {
		Charset charset = null;

		String charsetString = myServletRequest.getCharacterEncoding();
		if (isNotBlank(charsetString)) {
			charset = Charset.forName(charsetString);
		}

		return charset;
	}

	@Override
	public FhirContext getFhirContext() {
		return getServer().getFhirContext();
	}

	@Override
	public String getHeader(String name) {
		return getServletRequest().getHeader(name);
	}

	@Override
	public List<String> getHeaders(String name) {
		Enumeration<String> headers = getServletRequest().getHeaders(name);
		return headers == null ? Collections.emptyList() : Collections.list(getServletRequest().getHeaders(name));
	}

	@Override
	public Object getAttribute(String theAttributeName) {
		Validate.notBlank(theAttributeName, "theAttributeName must not be null or blank");
		return getServletRequest().getAttribute(theAttributeName);
	}

	@Override
	public void setAttribute(String theAttributeName, Object theAttributeValue) {
		Validate.notBlank(theAttributeName, "theAttributeName must not be null or blank");
		getServletRequest().setAttribute(theAttributeName, theAttributeValue);
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return getServletRequest().getInputStream();
	}

	@Override
	public Reader getReader() throws IOException {
		return getServletRequest().getReader();
	}

	@Override
	public RestfulServer getServer() {
		return myServer;
	}

	@Override
	public String getServerBaseForRequest() {
		return getServer().getServerBaseForRequest(this);
	}

	public HttpServletRequest getServletRequest() {
		return myServletRequest;
	}

	public HttpServletResponse getServletResponse() {
		return myServletResponse;
	}

	public void setServer(RestfulServer theServer) {
		this.myServer = theServer;
	}

	public ServletRequestDetails setServletRequest(HttpServletRequest myServletRequest) {
		this.myServletRequest = myServletRequest;
		return this;
	}

	public void setServletResponse(HttpServletResponse myServletResponse) {
		this.myServletResponse = myServletResponse;
	}

	public Map<String,List<String>> getHeaders() {
		Map<String, List<String>> retVal = new HashMap<>();
		Enumeration<String> names = myServletRequest.getHeaderNames();
		while (names.hasMoreElements()) {
			String nextName = names.nextElement();
			ArrayList<String> headerValues = new ArrayList<>();
			retVal.put(nextName, headerValues);
			Enumeration<String> valuesEnum = myServletRequest.getHeaders(nextName);
			while (valuesEnum.hasMoreElements()) {
				headerValues.add(valuesEnum.nextElement());
			}
		}
		return Collections.unmodifiableMap(retVal);
	}
}
