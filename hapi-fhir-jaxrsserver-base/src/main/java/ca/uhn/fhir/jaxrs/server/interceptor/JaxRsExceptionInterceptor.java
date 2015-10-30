package ca.uhn.fhir.jaxrs.server.interceptor;

import java.io.IOException;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.servlet.ServletException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jaxrs.server.AbstractJaxRsProvider;
import ca.uhn.fhir.jaxrs.server.util.JaxRsRequest;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.interceptor.ExceptionHandlingInterceptor;

public class JaxRsExceptionInterceptor {

    private ExceptionHandlingInterceptor exceptionHandler;
    @Context
    private UriInfo info;
    @Context
    private HttpHeaders headers;    
    
    AbstractJaxRsProvider theServer;
    
    FhirContext fhirContext = AbstractJaxRsProvider.CTX;
    
    public JaxRsExceptionInterceptor() {
    	this.exceptionHandler = new ExceptionHandlingInterceptor();
    }    
    
    public JaxRsExceptionInterceptor(ExceptionHandlingInterceptor exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}
    
	@AroundInvoke
    public Object intercept(final InvocationContext ctx) throws Throwable {
        try {
            return ctx.proceed();
        } catch(final Exception theException) {
        	theServer = (AbstractJaxRsProvider) ctx.getTarget();
        	throw convertException(theServer, theException);
        }
    }

	public BaseServerRuntimeResponseException convertException(final AbstractJaxRsProvider theServer, final Exception theException) {
		JaxRsRequest requestDetails = new JaxRsRequest(theServer, null, null, null);
		BaseServerResponseException convertedException = preprocessException(theException, requestDetails);
		return new BaseServerRuntimeResponseException(convertedException);
	}
	
	public Response handleException(JaxRsRequest theRequest, BaseServerRuntimeResponseException theException)
			throws IOException {
		return handleExceptionWithoutServletError(theRequest, theException);
	}	

	private BaseServerResponseException preprocessException(final Exception theException, JaxRsRequest requestDetails) {
		try {
			return exceptionHandler.preProcessOutgoingException(requestDetails, theException, null);
		} catch(ServletException e) {
			return new InternalErrorException(e);
		}
	}

	private Response handleExceptionWithoutServletError(JaxRsRequest theRequest, BaseServerResponseException theException) throws IOException {
		try {
			return (Response) exceptionHandler.handleException(theRequest, theException);
		} catch (ServletException e) {
			BaseServerResponseException newException = preprocessException(new InternalErrorException(e), theRequest);
			return handleExceptionWithoutServletError(theRequest, newException);
		}
	}    
}
