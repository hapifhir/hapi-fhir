package ca.uhn.fhir.jaxrs.server.interceptor;

import java.io.IOException;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.servlet.ServletException;
import javax.ws.rs.core.Response;

import ca.uhn.fhir.jaxrs.server.AbstractJaxRsProvider;
import ca.uhn.fhir.jaxrs.server.util.JaxRsRequest;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.interceptor.ExceptionHandlingInterceptor;

/**
 * An interceptor that catches the jax-rs exceptions
 * @author Peter Van Houte 
 */
public class JaxRsExceptionInterceptor {

    /** the existing exception handler which is able to convert exception into responses*/
    private ExceptionHandlingInterceptor exceptionHandler;
    
    /**
     * The default constructor
     */
    public JaxRsExceptionInterceptor() {
    	this.exceptionHandler = new ExceptionHandlingInterceptor();
    }    
    
    /**
     * A utility constructor for unit testing
     * @param exceptionHandler the handler for the exception conversion
     */
    JaxRsExceptionInterceptor(ExceptionHandlingInterceptor exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}
    
	/**
	 * This interceptor will catch all exception and convert them using the exceptionhandler
	 * @param ctx the invocation context
	 * @return the result
	 * @throws JaxRsResponseException an exception that can be handled by a jee container
	 */
	@AroundInvoke
    public Object intercept(final InvocationContext ctx) throws JaxRsResponseException {
        try {
            return ctx.proceed();
        } catch(final Exception theException) {
        	AbstractJaxRsProvider theServer = (AbstractJaxRsProvider) ctx.getTarget();
        	throw convertException(theServer, theException);
        }
    }

	private JaxRsResponseException convertException(final AbstractJaxRsProvider theServer, final Exception theException) {
		JaxRsRequest requestDetails = theServer.getRequest(null, null).build();
		BaseServerResponseException convertedException = preprocessException(theException, requestDetails);
		return new JaxRsResponseException(convertedException);
	}
	
	/**
	 * This method converts an exception into a response
	 * @param theRequest the request
	 * @param theException the thrown exception
	 * @return the response describing the error
	 */
	public Response convertExceptionIntoResponse(JaxRsRequest theRequest, JaxRsResponseException theException)
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
