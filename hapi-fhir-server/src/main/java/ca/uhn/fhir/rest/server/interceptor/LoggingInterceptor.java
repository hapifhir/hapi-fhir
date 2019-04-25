package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.RestfulServerUtils.ResponseEncoding;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.UrlUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.text.StringSubstitutor;
import org.apache.commons.text.lookup.StringLookup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map.Entry;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/*
 * #%L
 * HAPI FHIR - Server Framework
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

/**
 * Server interceptor which logs each request using a defined format
 * <p>
 * The following substitution variables are supported:
 * </p>
 * <table summary="Substitution variables supported by this class">
 * <tr>
 * <td>${id}</td>
 * <td>The resource ID associated with this request (or "" if none)</td>
 * </tr>
 * <tr>
 * <td>${idOrResourceName}</td>
 * <td>The resource ID associated with this request, or the resource name if the request applies to a type but not an
 * instance, or "" otherwise</td>
 * </tr>
 * <tr>
 * <td>${operationName}</td>
 * <td>If the request is an extended operation (e.g. "$validate") this value will be the operation name, or ""
 * otherwise</td>
 * </tr>
 * <tr>
 * <td>${operationType}</td>
 * <td>A code indicating the operation type for this request, e.g. "read", "history-instance",
 * "extended-operation-instance", etc.)</td>
 * </tr>
 * <tr>
 * <td>${remoteAddr}</td>
 * <td>The originating IP of the request</td>
 * </tr>
 * <tr>
 * <td>${requestHeader.XXXX}</td>
 * <td>The value of the HTTP request header named XXXX. For example, a substitution variable named
 * "${requestHeader.x-forwarded-for} will yield the value of the first header named "x-forwarded-for
 * ", or "" if none.</td>
 * </tr>
 * <tr>
 * <td>${requestParameters}</td>
 * <td>The HTTP request parameters (or "")</td>
 * </tr>
 * <tr>
 * <td>${responseEncodingNoDefault}</td>
 * <td>The encoding format requested by the client via the _format parameter or the Accept header. Value will be "json"
 * or "xml", or "" if the client did not explicitly request a format</td>
 * </tr>
 * <tr>
 * <td>${servletPath}</td>
 * <td>The part of thre requesting URL that corresponds to the particular Servlet being called (see
 * {@link HttpServletRequest#getServletPath()})</td>
 * </tr>
 * <tr>
 * <td>${requestBodyFhir}</td>
 * <td>The complete body of the request if the request has a FHIR content-type (this can be quite large!). Will emit an
 * empty string if the content type is not a FHIR content type</td>
 * </tr>
 * <tr>
 * <td>${requestUrl}</td>
 * <td>The complete URL of the request</td>
 * </tr>
 * <tr>
 * <td>${requestVerb}</td>
 * <td>The HTTP verb of the request</td>
 * </tr>
 * <tr>
 * <td>${exceptionMessage}</td>
 * <td>Applies only to an error message: The message from {@link Exception#getMessage()}</td>
 * </tr>
 * <tr>
 * <td>${processingTimeMillis}</td>
 * <td>The number of milliseconds spent processing this request</td>
 * </tr>
 * </table>
 */
@Interceptor
public class LoggingInterceptor {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(LoggingInterceptor.class);

	private String myErrorMessageFormat = "ERROR - ${operationType} - ${idOrResourceName}";
	private boolean myLogExceptions = true;
	private Logger myLogger = ourLog;
	private String myMessageFormat = "${operationType} - ${idOrResourceName}";

	/**
	 * Constructor for server logging interceptor
	 */
	public LoggingInterceptor() {
		super();
	}
	
	/**
	 * Get the log message format to be used when logging exceptions
	 */
	public String getErrorMessageFormat() {
		return myErrorMessageFormat;
	}

	@Hook(Pointcut.SERVER_HANDLE_EXCEPTION)
	public boolean handleException(RequestDetails theRequestDetails, BaseServerResponseException theException, HttpServletRequest theServletRequest, HttpServletResponse theServletResponse) throws ServletException, IOException {
		if (myLogExceptions) {
			// Perform any string substitutions from the message format
			StringLookup lookup = new MyLookup(theServletRequest, theException, theRequestDetails);
			StringSubstitutor subs = new StringSubstitutor(lookup, "${", "}", '\\');

			// Actuall log the line
			String line = subs.replace(myErrorMessageFormat);
			myLogger.info(line);

		}
		return true;
	}


	@Hook(Pointcut.SERVER_PROCESSING_COMPLETED_NORMALLY)
	public void processingCompletedNormally(ServletRequestDetails theRequestDetails) {
		// Perform any string substitutions from the message format
		StringLookup lookup = new MyLookup(theRequestDetails.getServletRequest(), theRequestDetails);
		StringSubstitutor subs = new StringSubstitutor(lookup, "${", "}", '\\');

		// Actually log the line
		String line = subs.replace(myMessageFormat);
		myLogger.info(line);
	}

	/**
	 * Should exceptions be logged by this logger
	 */
	public boolean isLogExceptions() {
		return myLogExceptions;
	}

	/**
	 * Set the log message format to be used when logging exceptions
	 */
	public void setErrorMessageFormat(String theErrorMessageFormat) {
		Validate.notBlank(theErrorMessageFormat, "Message format can not be null/empty");
		myErrorMessageFormat = theErrorMessageFormat;
	}

	/**
	 * Should exceptions be logged by this logger
	 */
	public void setLogExceptions(boolean theLogExceptions) {
		myLogExceptions = theLogExceptions;
	}

	public void setLogger(Logger theLogger) {
		Validate.notNull(theLogger, "Logger can not be null");
		myLogger = theLogger;
	}

	public void setLoggerName(String theLoggerName) {
		Validate.notBlank(theLoggerName, "Logger name can not be null/empty");
		myLogger = LoggerFactory.getLogger(theLoggerName);

	}

	/**
	 * Sets the message format itself. See the {@link LoggingInterceptor class documentation} for information on the
	 * format
	 */
	public void setMessageFormat(String theMessageFormat) {
		Validate.notBlank(theMessageFormat, "Message format can not be null/empty");
		myMessageFormat = theMessageFormat;
	}

	private static final class MyLookup implements StringLookup {
		private final Throwable myException;
		private final HttpServletRequest myRequest;
		private final RequestDetails myRequestDetails;

		private MyLookup(HttpServletRequest theRequest, RequestDetails theRequestDetails) {
			myRequest = theRequest;
			myRequestDetails = theRequestDetails;
			myException = null;
		}

		MyLookup(HttpServletRequest theServletRequest, BaseServerResponseException theException, RequestDetails theRequestDetails) {
			myException = theException;
			myRequestDetails = theRequestDetails;
			myRequest = theServletRequest;
		}

		@Override
		public String lookup(String theKey) {

			/*
			 * TODO: this method could be made more efficient through some sort of lookup map
			 */

			if ("operationType".equals(theKey)) {
				if (myRequestDetails.getRestOperationType() != null) {
					return myRequestDetails.getRestOperationType().getCode();
				}
				return "";
			} else if ("operationName".equals(theKey)) {
				if (myRequestDetails.getRestOperationType() != null) {
					switch (myRequestDetails.getRestOperationType()) {
					case EXTENDED_OPERATION_INSTANCE:
					case EXTENDED_OPERATION_SERVER:
					case EXTENDED_OPERATION_TYPE:
						return myRequestDetails.getOperation();
					default:
						return "";
					}
				}
					return "";
			} else if ("id".equals(theKey)) {
				if (myRequestDetails.getId() != null) {
					return myRequestDetails.getId().getValue();
				}
				return "";
			} else if ("servletPath".equals(theKey)) {
				return StringUtils.defaultString(myRequest.getServletPath());
			} else if ("idOrResourceName".equals(theKey)) {
				if (myRequestDetails.getId() != null) {
					return myRequestDetails.getId().getValue();
				}
				if (myRequestDetails.getResourceName() != null) {
					return myRequestDetails.getResourceName();
				}
				return "";
			} else if (theKey.equals("requestParameters")) {
				StringBuilder b = new StringBuilder();
				for (Entry<String, String[]> next : myRequestDetails.getParameters().entrySet()) {
					for (String nextValue : next.getValue()) {
						if (b.length() == 0) {
							b.append('?');
						} else {
							b.append('&');
						}
						b.append(UrlUtil.escapeUrlParam(next.getKey()));
						b.append('=');
						b.append(UrlUtil.escapeUrlParam(nextValue));
					}
				}
				return b.toString();
			} else if (theKey.startsWith("requestHeader.")) {
				String val = myRequest.getHeader(theKey.substring("requestHeader.".length()));
				return StringUtils.defaultString(val);
			} else if (theKey.startsWith("remoteAddr")) {
				return StringUtils.defaultString(myRequest.getRemoteAddr());
			} else if (theKey.equals("responseEncodingNoDefault")) {
				ResponseEncoding encoding = RestfulServerUtils.determineResponseEncodingNoDefault(myRequestDetails, myRequestDetails.getServer().getDefaultResponseEncoding());
				if (encoding != null) {
					return encoding.getEncoding().name();
				}
				return "";
			} else if (theKey.equals("exceptionMessage")) {
				return myException != null ? myException.getMessage() : null;
			} else if (theKey.equals("requestUrl")) {
				return myRequest.getRequestURL().toString();
			} else if (theKey.equals("requestVerb")) {
				return myRequest.getMethod();
			} else if (theKey.equals("requestBodyFhir")) {
				String contentType = myRequest.getContentType();
				if (isNotBlank(contentType)) {
					int colonIndex = contentType.indexOf(';');
					if (colonIndex != -1) {
						contentType = contentType.substring(0, colonIndex);
					}
					contentType = contentType.trim();

					EncodingEnum encoding = EncodingEnum.forContentType(contentType);
					if (encoding != null) {
						byte[] requestContents = myRequestDetails.loadRequestContents();
						return new String(requestContents, Constants.CHARSET_UTF8);
					}
				}
				return "";
			} else if ("processingTimeMillis".equals(theKey)) {
				Date startTime = (Date) myRequest.getAttribute(RestfulServer.REQUEST_START_TIME);
				if (startTime != null) {
					long time = System.currentTimeMillis() - startTime.getTime();
					return Long.toString(time);
				}
			}

			return "!VAL!";
		}
	}

}
