/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.rest.server.servlet;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.PreferHeader;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public class ServletRequestDetails extends RequestDetails {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ServletRequestDetails.class);

	private RestfulServer myServer;
	private HttpServletRequest myServletRequest;
	private HttpServletResponse myServletResponse;
	private ListMultimap<String, String> myHeaders;

	/**
	 * Constructor for testing only
	 */
	public ServletRequestDetails() {
		this((IInterceptorBroadcaster) null);
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
			throw new InvalidRequestException(
					Msg.code(308) + String.format("Could not load request resource: %s", e.getMessage()));
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
		// For efficiency, we only make a copy of the request headers if we need to
		// modify them
		if (myHeaders != null) {
			List<String> values = myHeaders.get(name);
			if (values.isEmpty()) {
				return null;
			} else {
				return values.get(0);
			}
		}
		return getServletRequest().getHeader(name);
	}

	@Override
	public List<String> getHeaders(String name) {
		// For efficiency, we only make a copy of the request headers if we need to
		// modify them
		if (myHeaders != null) {
			return myHeaders.get(name);
		}
		Enumeration<String> headers = getServletRequest().getHeaders(name);
		return headers == null
				? Collections.emptyList()
				: Collections.list(getServletRequest().getHeaders(name));
	}

	@Override
	public void addHeader(String theName, String theValue) {
		initHeaders();
		myHeaders.put(theName, theValue);
	}

	@Override
	public void setHeaders(String theName, List<String> theValue) {
		initHeaders();
		myHeaders.removeAll(theName);
		myHeaders.putAll(theName, theValue);
	}

	private void initHeaders() {
		if (myHeaders == null) {
			// Make sure we are case-insensitive for header names
			myHeaders = MultimapBuilder.treeKeys(String.CASE_INSENSITIVE_ORDER)
					.arrayListValues()
					.build();

			Enumeration<String> headerNames = getServletRequest().getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String nextName = headerNames.nextElement();
				Enumeration<String> values = getServletRequest().getHeaders(nextName);
				while (values.hasMoreElements()) {
					myHeaders.put(nextName, values.nextElement());
				}
			}
		}
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

	public ServletRequestDetails setServletRequest(@Nonnull HttpServletRequest myServletRequest) {
		this.myServletRequest = myServletRequest;

		// TODO KHS move a bunch of other initialization from RestfulServer into this method
		if ("true".equals(myServletRequest.getHeader(Constants.HEADER_REWRITE_HISTORY))) {
			setRewriteHistory(true);
		}
		setRetryFields(myServletRequest);
		return this;
	}

	private void setRetryFields(HttpServletRequest theRequest) {
		if (theRequest == null) {
			return;
		}
		Enumeration<String> headers = theRequest.getHeaders(Constants.HEADER_RETRY_ON_VERSION_CONFLICT);
		if (headers != null) {
			Iterator<String> headerIterator = headers.asIterator();
			while (headerIterator.hasNext()) {
				String headerValue = headerIterator.next();
				if (isNotBlank(headerValue)) {
					StringTokenizer tok = new StringTokenizer(headerValue, ";");
					while (tok.hasMoreTokens()) {
						String next = trim(tok.nextToken());
						if (next.equals(Constants.HEADER_RETRY)) {
							setRetry(true);
						} else if (next.startsWith(Constants.HEADER_MAX_RETRIES + "=")) {
							String val = trim(next.substring((Constants.HEADER_MAX_RETRIES + "=").length()));
							int maxRetries = Integer.parseInt(val);
							setMaxRetries(maxRetries);
						}
					}
				}
			}
		}
	}

	public void setServletResponse(HttpServletResponse myServletResponse) {
		this.myServletResponse = myServletResponse;
	}

	public Map<String, List<String>> getHeaders() {
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

	/**
	 * Returns true if the `Prefer` header contains a value of `respond-async`
	 */
	public boolean isPreferRespondAsync() {
		String preferHeader = getHeader(Constants.HEADER_PREFER);
		PreferHeader prefer = RestfulServerUtils.parsePreferHeader(null, preferHeader);
		return prefer.getRespondAsync();
	}
}
