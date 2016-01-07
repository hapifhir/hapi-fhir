package ca.uhn.fhir.jaxrs.server.interceptor;

/*
 * #%L
 * HAPI FHIR JAX-RS Server
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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
 * 
 * @author Peter Van Houte | peter.vanhoute@agfa.com | Agfa Healthcare
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
	 * @throws IOException
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
