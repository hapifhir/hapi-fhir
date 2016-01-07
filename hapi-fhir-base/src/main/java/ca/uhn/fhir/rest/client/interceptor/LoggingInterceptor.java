package ca.uhn.fhir.rest.client.interceptor;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.HttpEntityWrapper;
import org.slf4j.Logger;

import ca.uhn.fhir.rest.api.IHttpRequestBase;
import ca.uhn.fhir.rest.client.IClientInterceptor;
import ca.uhn.fhir.rest.client.IHttpResponse;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public class LoggingInterceptor implements IClientInterceptor {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(LoggingInterceptor.class);

	private Logger myLog = ourLog;
	private boolean myLogRequestBody = false;
	private boolean myLogRequestHeaders = false;
	private boolean myLogRequestSummary = true;
	private boolean myLogResponseBody = false;
	private boolean myLogResponseHeaders = false;
	private boolean myLogResponseSummary = true;

	/**
	 * Constructor
	 */
	public LoggingInterceptor() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param theVerbose
	 *            If set to true, all logging is enabled
	 */
	public LoggingInterceptor(boolean theVerbose) {
		if (theVerbose) {
			setLogRequestBody(true);
			setLogRequestSummary(true);
			setLogResponseBody(true);
			setLogResponseSummary(true);
			setLogRequestHeaders(true);
			setLogResponseHeaders(true);
		}
	}

	@Override
	public void interceptRequest(IHttpRequestBase theRequest) {
		if (myLogRequestSummary) {
			myLog.info("Client request: {}", theRequest);
		}

		if (myLogRequestHeaders) {
			StringBuilder b = getHeaderString(theRequest.getAllHeaders());
			myLog.info("Client request headers:\n{}", b.toString());
		}

		if (myLogRequestBody) {
			if (theRequest instanceof HttpEntityEnclosingRequest) {
				HttpEntity entity = ((HttpEntityEnclosingRequest) theRequest).getEntity();
				if (entity.isRepeatable()) {
					try {
						String content = IOUtils.toString(entity.getContent());
						myLog.info("Client request body:\n{}", content);
					} catch (IllegalStateException e) {
						myLog.warn("Failed to replay request contents (during logging attempt, actual FHIR call did not fail)", e);
					} catch (IOException e) {
						myLog.warn("Failed to replay request contents (during logging attempt, actual FHIR call did not fail)", e);
					}
				}
			}
		}

	}

	@Override
	public void interceptResponse(IHttpResponse theResponse) throws IOException {
		if (myLogResponseSummary) {
			String message = "HTTP " + theResponse.getStatus() + " " + theResponse.getStatusInfo();
			myLog.info("Client response: {}", message);
		}

		if (myLogResponseHeaders) {
			StringBuilder b = getHeaderString(theResponse.getAllHeaders());
			// if (theResponse.getEntity() != null && theResponse.getEntity().getContentEncoding() != null) {
			// Header next = theResponse.getEntity().getContentEncoding();
			// b.append(next.getName() + ": " + next.getValue());
			// }
			// if (theResponse.getEntity() != null && theResponse.getEntity().getContentType() != null) {
			// Header next = theResponse.getEntity().getContentType();
			// b.append(next.getName() + ": " + next.getValue());
			// }
			if (b.length() == 0) {
				myLog.info("Client response headers: (none)");
			} else {
				myLog.info("Client response headers:\n{}", b.toString());
			}
		}

		if (myLogResponseBody) {
			theResponse.bufferEntitity();
			InputStream respEntity = theResponse.readEntity();
			if (respEntity != null) {
				final byte[] bytes;
				try {
					bytes = IOUtils.toByteArray(respEntity);
				} catch (IllegalStateException e) {
					throw new InternalErrorException(e);
				}
				myLog.info("Client response body:\n{}", new String(bytes, "UTF-8"));
			} else {
				myLog.info("Client response body: (none)");
			}
		}
	}

	private StringBuilder getHeaderString(Map<String, List<String>> allHeaders) {
		StringBuilder b = new StringBuilder();
		if (allHeaders != null && !allHeaders.isEmpty()) {
			Iterator<String> nameEntries = allHeaders.keySet().iterator();
			while(nameEntries.hasNext()) {
				String key = nameEntries.next();
				Iterator<String> values = allHeaders.get(key).iterator();
				while(values.hasNext()) {
					String value = values.next();
						b.append(key + ": " + value);
						if (nameEntries.hasNext() || values.hasNext()) {
							b.append('\n');
						}
					}
			}
		}
		return b;
	}

	/**
	 * Sets a logger to use to log messages (default is a logger with this class' name). This can be used to redirect
	 * logs to a differently named logger instead.
	 * 
	 * @param theLogger
	 *            The logger to use. Must not be null.
	 */
	public void setLogger(Logger theLogger) {
		Validate.notNull(theLogger, "theLogger can not be null");
		myLog = theLogger;
	}

	/**
	 * Should a summary (one line) for each request be logged, containing the URL and other information
	 */
	public void setLogRequestBody(boolean theValue) {
		myLogRequestBody = theValue;
	}

	/**
	 * Should headers for each request be logged, containing the URL and other information
	 */
	public void setLogRequestHeaders(boolean theValue) {
		myLogRequestHeaders = theValue;
	}

	/**
	 * Should a summary (one line) for each request be logged, containing the URL and other information
	 */
	public void setLogRequestSummary(boolean theValue) {
		myLogRequestSummary = theValue;
	}

	/**
	 * Should a summary (one line) for each request be logged, containing the URL and other information
	 */
	public void setLogResponseBody(boolean theValue) {
		myLogResponseBody = theValue;
	}

	/**
	 * Should headers for each request be logged, containing the URL and other information
	 */
	public void setLogResponseHeaders(boolean theValue) {
		myLogResponseHeaders = theValue;
	}

	/**
	 * Should a summary (one line) for each request be logged, containing the URL and other information
	 */
	public void setLogResponseSummary(boolean theValue) {
		myLogResponseSummary = theValue;
	}

}
