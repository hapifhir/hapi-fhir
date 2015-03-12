package ca.uhn.fhir.rest.server.interceptor;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.base.resource.BaseOperationOutcome;
import ca.uhn.fhir.model.base.resource.BaseOperationOutcome.BaseIssue;
import ca.uhn.fhir.rest.method.Request;
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
		ourLog.error("AA", theException);
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

		boolean requestIsBrowser = RestfulServer.requestIsBrowser(theRequest);
		String fhirServerBase = ((Request) theRequestDetails).getFhirServerBase();
		RestfulServerUtils.streamResponseAsResource(theRequestDetails.getServer(), theResponse, oo, RestfulServerUtils.determineResponseEncodingNoDefault(theRequest), true, requestIsBrowser,
				NarrativeModeEnum.NORMAL, statusCode, false, fhirServerBase);

		theResponse.setStatus(statusCode);
		theRequestDetails.getServer().addHeadersToResponse(theResponse);
		theResponse.setContentType("text/plain");
		theResponse.setCharacterEncoding("UTF-8");
		theResponse.getWriter().append(theException.getMessage());
		theResponse.getWriter().close();

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
