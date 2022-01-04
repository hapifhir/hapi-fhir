package ca.uhn.fhir.rest.client.interceptor;

/*
 * #%L
 * HAPI FHIR - Client Framework
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Interceptor
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
	 * Constructor for client logging interceptor
	 */
	public LoggingInterceptor() {
		super();
	}

	/**
	 * Constructor for client logging interceptor
	 *
	 * @param theVerbose If set to true, all logging is enabled
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
	@Hook(value = Pointcut.CLIENT_REQUEST, order = InterceptorOrders.LOGGING_INTERCEPTOR_RESPONSE)
	public void interceptRequest(IHttpRequest theRequest) {
		if (myLogRequestSummary) {
			myLog.info("Client request: {}", theRequest);
		}

		if (myLogRequestHeaders) {
			StringBuilder b = headersToString(theRequest.getAllHeaders());
			myLog.info("Client request headers:\n{}", b.toString());
		}

		if (myLogRequestBody) {
			try {
				String content = theRequest.getRequestBodyFromStream();
				if (content != null) {
					myLog.info("Client request body:\n{}", content);
				}
			} catch (IllegalStateException | IOException e) {
				myLog.warn("Failed to replay request contents (during logging attempt, actual FHIR call did not fail)", e);
			}
		}
	}

	@Override
	@Hook(value = Pointcut.CLIENT_RESPONSE, order = InterceptorOrders.LOGGING_INTERCEPTOR_REQUEST)
	public void interceptResponse(IHttpResponse theResponse) throws IOException {
		if (myLogResponseSummary) {
			String message = "HTTP " + theResponse.getStatus() + " " + theResponse.getStatusInfo();
			String respLocation = "";

			/*
			 * Add response location
			 */
			List<String> locationHeaders = theResponse.getHeaders(Constants.HEADER_LOCATION);
			if (locationHeaders == null || locationHeaders.isEmpty()) {
				locationHeaders = theResponse.getHeaders(Constants.HEADER_CONTENT_LOCATION);
			}
			if (locationHeaders != null && locationHeaders.size() > 0) {
				String locationValue = locationHeaders.get(0);
				IdDt locationValueId = new IdDt(locationValue);
				if (locationValueId.hasBaseUrl() && locationValueId.hasIdPart()) {
					locationValue = locationValueId.toUnqualified().getValue();
				}
				respLocation = " (" + locationValue + ")";
			}

			String timing = " in " + theResponse.getRequestStopWatch().toString();
			myLog.info("Client response: {}{}{}", message, respLocation, timing);
		}

		if (myLogResponseHeaders) {
			StringBuilder b = headersToString(theResponse.getAllHeaders());
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
			theResponse.bufferEntity();
			try (InputStream respEntity = theResponse.readEntity()) {
				if (respEntity != null) {
					final byte[] bytes;
					try {
						bytes = IOUtils.toByteArray(respEntity);
					} catch (IllegalStateException e) {
						throw new InternalErrorException(Msg.code(1405) + e);
					}
					myLog.info("Client response body:\n{}", new String(bytes, StandardCharsets.UTF_8));
				} else {
					myLog.info("Client response body: (none)");
				}
			}
		}
	}

	private StringBuilder headersToString(Map<String, List<String>> theHeaders) {
		StringBuilder b = new StringBuilder();
		if (theHeaders != null && !theHeaders.isEmpty()) {
			Iterator<String> nameEntries = theHeaders.keySet().iterator();
			while (nameEntries.hasNext()) {
				String key = nameEntries.next();
				Iterator<String> values = theHeaders.get(key).iterator();
				while (values.hasNext()) {
					String value = values.next();
					b.append(key);
					b.append(": ");
					b.append(value);
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
	 * @param theLogger The logger to use. Must not be null.
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
