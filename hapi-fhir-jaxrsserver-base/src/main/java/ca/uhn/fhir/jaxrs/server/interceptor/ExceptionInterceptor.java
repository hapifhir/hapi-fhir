package ca.uhn.fhir.jaxrs.server.interceptor;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jaxrs.server.AbstractJaxRsRestServer;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.OperationOutcomeUtil;

public class ExceptionInterceptor {

    private static final org.slf4j.Logger ourLog = LoggerFactory.getLogger(ExceptionInterceptor.class);
    private Class<?>[] myReturnStackTracesForExceptionTypes;
    
    @Context
    private UriInfo info;
    @Context
    private HttpHeaders headers;    
    
    FhirContext fhirContext = AbstractJaxRsRestServer.getFhirContext();
    
    @AroundInvoke
    public Object intercept(final InvocationContext ctx) throws Exception {
        try {
            if(!ourLog.isDebugEnabled() || ctx.getMethod().getName().contains("getResourceType")) {
                return ctx.proceed();
            } else {
                ourLog.debug("METHOD_CALL : " + ctx.getMethod().getName() + " [ " + Arrays.asList(ctx.getParameters()) + "] ");
                Object proceed = ctx.proceed();
                ourLog.debug("RESULT : " + proceed.toString());
                return proceed;
            }
        } catch(final Exception theException) {
            return handleException(theException);
        }
    }

    public Response handleException(final Throwable theException) 
            {
        IBaseOperationOutcome oo = null;
        int statusCode = Constants.STATUS_HTTP_500_INTERNAL_ERROR;

        if (theException instanceof BaseServerResponseException) {
            oo = ((BaseServerResponseException) theException).getOperationOutcome();
            statusCode = ((BaseServerResponseException) theException).getStatusCode();
        }

        /*
         * Generate an OperationOutcome to return, unless the exception throw by the resource provider had one
         */
        if (oo == null) {
            try {
                final RuntimeResourceDefinition ooDef = fhirContext.getResourceDefinition("OperationOutcome");
                oo = (IBaseOperationOutcome) ooDef.getImplementingClass().newInstance();

                if (theException instanceof InternalErrorException) {
                    ourLog.error("Failure during REST processing", theException);
                    populateDetails(fhirContext, theException, oo);
                } else if (theException instanceof BaseServerResponseException) {
                    ourLog.warn("Failure during REST processing: {}", theException);
                    final BaseServerResponseException baseServerResponseException = (BaseServerResponseException) theException;
                    statusCode = baseServerResponseException.getStatusCode();
                    populateDetails(fhirContext, theException, oo);
                    if (baseServerResponseException.getAdditionalMessages() != null) {
                        for (final String next : baseServerResponseException.getAdditionalMessages()) {
                            OperationOutcomeUtil.addIssue(fhirContext, oo, "error", next);
                        }
                    }
                } else {
                    ourLog.error("Failure during REST processing: " + theException.toString(), theException);
                    populateDetails(fhirContext, theException, oo);
                    statusCode = Constants.STATUS_HTTP_500_INTERNAL_ERROR;
                }
            } catch (final Exception e1) {
                ourLog.error("Failed to instantiate OperationOutcome resource instance", e1);
                final ResponseBuilder result = Response.status(Constants.STATUS_HTTP_500_INTERNAL_ERROR);
                result.header(Constants.HEADER_CONTENT_TYPE, Constants.CT_TEXT_WITH_UTF8);
                result.header(Constants.HEADER_CONTENT_ENCODING, Constants.CHARSET_NAME_UTF8);
                result.entity(theException.getMessage());
                return result.build();
            }
        } else {
            ourLog.error("Unknown error during processing", theException);
        }

        // Add headers associated with the specific error code
        if (theException instanceof BaseServerResponseException) {
            final Map<String, String[]> additional = ((BaseServerResponseException) theException).getAssociatedHeaders();
            if (additional != null) {
                for (final Entry<String, String[]> next : additional.entrySet()) {
                    if (isNotBlank(next.getKey()) && next.getValue() != null) {
                        final String nextKey = next.getKey();
                        for (final String nextValue : next.getValue()) {
                            addHeader(nextKey, nextValue);
                        }
                    }
                }
            }
        }

        final boolean requestIsBrowser = false; // RestfulServer.requestIsBrowser(theRequest);
        final String fhirServerBase = ""; // theRequestDetails.getFhirServerBase();

        // theResponse.setStatus(statusCode);
        // theRequestDetails.getServer().addHeadersToResponse(theResponse);
        // theResponse.setContentType("text/plain");
        // theResponse.setCharacterEncoding("UTF-8");
        // theResponse.getWriter().append(theException.getMessage());
        // theResponse.getWriter().close();
                
        final ResponseBuilder result = Response.status(statusCode);
        //final String resName = ctx.getResourceDefinition(oo).getName();
        result.header(Constants.HEADER_CONTENT_TYPE, Constants.CT_TEXT_WITH_UTF8);
        result.entity(theException.getMessage());
        return result.build();
    }

    private void addHeader(final String nextKey, final String nextValue) {
        throw new UnsupportedOperationException();
    }

    private void populateDetails(final FhirContext theCtx, final Throwable theException, final IBaseOperationOutcome theOo) {
        if (myReturnStackTracesForExceptionTypes != null) {
            for (final Class<?> next : myReturnStackTracesForExceptionTypes) {
                if (next.isAssignableFrom(theException.getClass())) {
                    final String detailsValue = theException.getMessage() + "\n\n" + ExceptionUtils.getStackTrace(theException);
                    OperationOutcomeUtil.addIssue(theCtx, theOo, "error", detailsValue);
                    return;
                }
            }
        }

        OperationOutcomeUtil.addIssue(theCtx, theOo, "error", theException.getMessage());
    }

    /**
     * If any server methods throw an exception which extends any of the given exception types, the exception stack trace
     * will be returned to the user. This can be useful for helping to diagnose issues, but may not be desirable for
     * production situations.
     * 
     * @param theExceptionTypes
     *           The exception types for which to return the stack trace to the user.
     * @return Returns an instance of this interceptor, to allow for easy method chaining.
     */
    public ExceptionInterceptor setReturnStackTracesForExceptionTypes(final Class<?>... theExceptionTypes) {
        myReturnStackTracesForExceptionTypes = theExceptionTypes;
        return this;
    }
    
}
