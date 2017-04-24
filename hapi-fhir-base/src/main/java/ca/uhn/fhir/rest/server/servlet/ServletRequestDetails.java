package ca.uhn.fhir.rest.server.servlet;

/*
 * #%L
 * HAPI FHIR - Core Library
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
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.http.entity.ContentType;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.rest.method.BaseMethodBinding;
import ca.uhn.fhir.rest.method.BaseMethodBinding.IRequestReader;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class ServletRequestDetails extends RequestDetails {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ServletRequestDetails.class);
	/**
	 * @see BaseMethodBinding#loadRequestContents(RequestDetails)
	 */
	private static volatile IRequestReader ourRequestReader;
	private RestfulServer myServer;
	private HttpServletRequest myServletRequest;
	private HttpServletResponse myServletResponse;
	private byte[] requestContents;

	public ServletRequestDetails() {
		super();
		setResponse(new ServletRestfulResponse(this));
	}

	@Override
	protected byte[] getByteStreamRequestContents() {
		/*
		 * This is weird, but this class is used both in clients and in servers, and we want to avoid needing to depend on
		 * servlet-api in clients since there is no point. So we dynamically load a class that does the servlet processing
		 * in servers. Down the road it may make sense to just split the method binding classes into server and client
		 * versions, but this isn't actually a huge deal I don't think.
		 */
		IRequestReader reader = ourRequestReader;
		if (reader == null) {
			try {
				Class.forName("javax.servlet.ServletInputStream");
				String className = BaseMethodBinding.class.getName() + "$" + "ActiveRequestReader";
				try {
					reader = (IRequestReader) Class.forName(className).newInstance();
				} catch (Exception e1) {
					throw new ConfigurationException("Failed to instantiate class " + className, e1);
				}
			} catch (ClassNotFoundException e) {
				String className = BaseMethodBinding.class.getName() + "$" + "InactiveRequestReader";
				try {
					reader = (IRequestReader) Class.forName(className).newInstance();
				} catch (Exception e1) {
					throw new ConfigurationException("Failed to instantiate class " + className, e1);
				}
			}
			ourRequestReader = reader;
		}

		try {
			InputStream inputStream = reader.getInputStream(this);
			requestContents = IOUtils.toByteArray(inputStream);

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
			// FIXME resource leak
			return requestContents;
		} catch (IOException e) {
			ourLog.error("Could not load request resource", e);
			throw new InvalidRequestException(String.format("Could not load request resource: %s", e.getMessage()));
		}
	}

	@Override
	public String getHeader(String name) {
		return getServletRequest().getHeader(name);
	}

	@Override
	public List<String> getHeaders(String name) {
		Enumeration<String> headers = getServletRequest().getHeaders(name);
		return headers == null ? Collections.<String> emptyList() : Collections.list(getServletRequest().getHeaders(name));
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
		return getServer().getServerBaseForRequest(getServletRequest());
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

	public void setServletRequest(HttpServletRequest myServletRequest) {
		this.myServletRequest = myServletRequest;
	}

	public void setServletResponse(HttpServletResponse myServletResponse) {
		this.myServletResponse = myServletResponse;
	}

	@Override
	public Charset getCharset() {
		String ct = getHeader(Constants.HEADER_CONTENT_TYPE);

		Charset charset = null;
		if (isNotBlank(ct)) {
			ContentType parsedCt = ContentType.parse(ct);
			charset = parsedCt.getCharset();
		}
		return charset;
	}

}
