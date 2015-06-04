package ca.uhn.fhir.rest.server.interceptor;

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

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.base.resource.BaseOperationOutcome;
import ca.uhn.fhir.model.base.resource.BaseOperationOutcome.BaseIssue;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.RestfulServer.NarrativeModeEnum;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public class ExceptionHandlingInterceptor extends InterceptorAdapter {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ExceptionHandlingInterceptor.class);
	private Class<?>[] myReturnStackTracesForExceptionTypes;

	/**
	 * If any server methods throw an exception which extends any of the given exception types, the exception 
	 * stack trace will be returned to the user. This can be useful for helping to diagnose issues, but may
	 * not be desirable for production situations.
	 * 
	 * @param theExceptionTypes The exception types for which to return the stack trace to the user.
	 * @return Returns an instance of this interceptor, to allow for easy method chaining.
	 */
	public ExceptionHandlingInterceptor setReturnStackTracesForExceptionTypes(Class<?>... theExceptionTypes) {
		myReturnStackTracesForExceptionTypes = theExceptionTypes;
		return this;
	}

	@Override
	public boolean handleException(RequestDetails theRequestDetails, Throwable theException, HttpServletRequest theRequest, HttpServletResponse theResponse) throws ServletException, IOException {
		BaseOperationOutcome oo = null;
		int statusCode = Constants.STATUS_HTTP_500_INTERNAL_ERROR;

		FhirContext ctx = theRequestDetails.getServer().getFhirContext();

		if (theException instanceof BaseServerResponseException) {
			oo = ((BaseServerResponseException) theException).getOperationOutcome();
			statusCode = ((BaseServerResponseException) theException).getStatusCode();
		}

		/*
		 * Generate an OperationOutcome to return, unless the exception throw by the resource provider had one
		 */
		if (oo == null) {
			try {
				oo = (BaseOperationOutcome) ctx.getResourceDefinition("OperationOutcome").getImplementingClass().newInstance();
			} catch (Exception e1) {
				ourLog.error("Failed to instantiate OperationOutcome resource instance", e1);
				throw new ServletException("Failed to instantiate OperationOutcome resource instance", e1);
			}

			BaseIssue issue = oo.addIssue();
			issue.getSeverityElement().setValue("error");
			if (theException instanceof InternalErrorException) {
				ourLog.error("Failure during REST processing", theException);
				populateDetails(theException, issue);
			} else if (theException instanceof BaseServerResponseException) {
				ourLog.warn("Failure during REST processing: {}", theException);
				BaseServerResponseException baseServerResponseException = (BaseServerResponseException) theException;
				statusCode = baseServerResponseException.getStatusCode();
				populateDetails(theException, issue);
				if (baseServerResponseException.getAdditionalMessages() != null) {
					for (String next : baseServerResponseException.getAdditionalMessages()) {
						BaseIssue issue2 = oo.addIssue();
						issue2.getSeverityElement().setValue("error");
						issue2.setDetails(next);
					}
				}
			} else {
				ourLog.error("Failure during REST processing: " + theException.toString(), theException);
				populateDetails(theException, issue);
				statusCode = Constants.STATUS_HTTP_500_INTERNAL_ERROR;
			}
		} else {
			ourLog.error("Unknown error during processing", theException);
		}

		// Add headers associated with the specific error code
		if (theException instanceof BaseServerResponseException) {
			Map<String, String[]> additional = ((BaseServerResponseException) theException).getAssociatedHeaders();
			if (additional != null) {
				for (Entry<String, String[]> next : additional.entrySet()) {
					if (isNotBlank(next.getKey()) && next.getValue() != null) {
						String nextKey = next.getKey();
						for (String nextValue : next.getValue()) {
							theResponse.addHeader(nextKey, nextValue);
						}
					}
				}
			}
		}

		boolean requestIsBrowser = RestfulServer.requestIsBrowser(theRequest);
		String fhirServerBase = theRequestDetails.getFhirServerBase();
		RestfulServerUtils.streamResponseAsResource(theRequestDetails.getServer(), theResponse, oo, RestfulServerUtils.determineResponseEncodingNoDefault(theRequest), true, requestIsBrowser,
				NarrativeModeEnum.NORMAL, statusCode, false, fhirServerBase, false);

//		theResponse.setStatus(statusCode);
//		theRequestDetails.getServer().addHeadersToResponse(theResponse);
//		theResponse.setContentType("text/plain");
//		theResponse.setCharacterEncoding("UTF-8");
//		theResponse.getWriter().append(theException.getMessage());
//		theResponse.getWriter().close();

		return false;
	}

	private void populateDetails(Throwable theException, BaseIssue issue) {
		if (myReturnStackTracesForExceptionTypes != null) {
			for (Class<?> next : myReturnStackTracesForExceptionTypes) {
				if (next.isAssignableFrom(theException.getClass())) {
					issue.getDetailsElement().setValue(theException.getMessage() + "\n\n" + ExceptionUtils.getStackTrace(theException));
					return;
				}
			}
		}

		issue.getDetailsElement().setValue(theException.getMessage());
	}

}
