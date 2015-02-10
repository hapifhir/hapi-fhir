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

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.HttpEntityWrapper;
import org.slf4j.Logger;

import ca.uhn.fhir.rest.client.IClientInterceptor;
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
	public void interceptRequest(HttpRequestBase theRequest) {
		if (myLogRequestSummary) {
			myLog.info("Client request: {}", theRequest);
		}

		if (myLogRequestHeaders) {
			StringBuilder b = new StringBuilder();
			for (int i = 0; i < theRequest.getAllHeaders().length; i++) {
				Header next = theRequest.getAllHeaders()[i];
				b.append(next.getName() + ": " + next.getValue());
				if (i + 1 < theRequest.getAllHeaders().length) {
					b.append('\n');
				}
			}
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
	public void interceptResponse(HttpResponse theResponse) throws IOException {
		if (myLogResponseSummary) {
			String message = "HTTP " + theResponse.getStatusLine().getStatusCode() + " " + theResponse.getStatusLine().getReasonPhrase();
			myLog.info("Client response: {}", message);
		}

		if (myLogResponseHeaders) {
			StringBuilder b = new StringBuilder();
			if (theResponse.getAllHeaders() != null) {
				for (int i = 0; i < theResponse.getAllHeaders().length; i++) {
					Header next = theResponse.getAllHeaders()[i];
					b.append(next.getName() + ": " + next.getValue());
					if (i + 1 < theResponse.getAllHeaders().length) {
						b.append('\n');
					}
				}
			}
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
			HttpEntity respEntity = theResponse.getEntity();
			if (respEntity != null) {
			final byte[] bytes;
			try {
				bytes = IOUtils.toByteArray(respEntity.getContent());
			} catch (IllegalStateException e) {
				throw new InternalErrorException(e);
			}

			myLog.info("Client response body:\n{}", new String(bytes));
			theResponse.setEntity(new MyEntityWrapper(respEntity, bytes));
			} else {
				myLog.info("Client response body: (none)");
			}
		}
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

	private static class MyEntityWrapper extends HttpEntityWrapper {

		private byte[] myBytes;

		public MyEntityWrapper(HttpEntity theWrappedEntity, byte[] theBytes) {
			super(theWrappedEntity);
			myBytes = theBytes;
		}

		@Override
		public InputStream getContent() throws IOException {
			return new ByteArrayInputStream(myBytes);
		}

		@Override
		public void writeTo(OutputStream theOutstream) throws IOException {
			theOutstream.write(myBytes);
		}

	}

}
