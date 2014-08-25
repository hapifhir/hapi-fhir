package ca.uhn.fhir.rest.server.interceptor;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 University Health Network
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.rest.method.Request;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;

/**
 * Provides methods to intercept requests and responses
 */
public interface IServerInterceptor {

	/**
	 * This method is called before any other processing takes place for each incoming request. It may be used to
	 * provide alternate handling for some requests, or to screen requests before they are handled, etc.
	 * <p>
	 * Note that any exceptions thrown by this method will not be trapped by HAPI (they will be passed up to the server)
	 * </p>
	 * 
	 * @param theRequest
	 *            The incoming request
	 * @param theResponse
	 *            The response. Note that interceptors may choose to provide a response (i.e. by calling
	 *            {@link HttpServletResponse#getWriter()}) but in that case it is important to return <code>true</code>
	 * @return Return <code>true</code> if processing should continue normally. This is generally the right thing to do.
	 *         If your interceptor is providing a response rather than letting HAPI handle the response normally, you
	 *         must return <code>false</code>. In this case, no further processing will occur and no further
	 *         interceptors will be called.
	 */
	public boolean incomingRequest(HttpServletRequest theRequest, HttpServletResponse theResponse);

	/**
	 * This method is called just before the actual implementing server method is invoked
	 * 
	 * @param theRequestDetails
	 *            A bean containing details about the request that is about to be processed, including
	 * @param theRequest
	 *            The incoming request
	 * @param theResponse
	 *            The response. Note that interceptors may choose to provide a response (i.e. by calling
	 *            {@link HttpServletResponse#getWriter()}) but in that case it is important to return <code>true</code>
	 * @return Return <code>true</code> if processing should continue normally. This is generally the right thing to do.
	 *         If your interceptor is providing a response rather than letting HAPI handle the response normally, you
	 *         must return <code>false</code>. In this case, no further processing will occur and no further
	 *         interceptors will be called.
	 * @throws AuthenticationException
	 *             This exception may be thrown to indicate that the interceptor has detected an unauthorized access
	 *             attempt
	 */
	public boolean incomingRequest(RequestDetails theRequestDetails, HttpServletRequest theRequest, HttpServletResponse theResponse) throws AuthenticationException;

	/**
	 * This method is called after the server implementation method has been called, but before any attempt to stream
	 * the response back to the client
	 * 
	 * @param theRequestDetails
	 *            A bean containing details about the request that is about to be processed, including
	 * @param theResponseObject
	 *            The actual object which is being streamed to the client as a response
	 * @param theRequest
	 *            The incoming request
	 * @param theResponse
	 *            The response. Note that interceptors may choose to provide a response (i.e. by calling
	 *            {@link HttpServletResponse#getWriter()}) but in that case it is important to return <code>true</code>
	 * @return Return <code>true</code> if processing should continue normally. This is generally the right thing to do.
	 *         If your interceptor is providing a response rather than letting HAPI handle the response normally, you
	 *         must return <code>false</code>. In this case, no further processing will occur and no further
	 *         interceptors will be called.
	 * @throws AuthenticationException
	 *             This exception may be thrown to indicate that the interceptor has detected an unauthorized access
	 *             attempt
	 */
	public boolean outgoingResponse(RequestDetails theRequestDetails, TagList theResponseObject, HttpServletRequest theServletRequest, HttpServletResponse theServletResponse) throws AuthenticationException;


	/**
	 * This method is called after the server implementation method has been called, but before any attempt to stream
	 * the response back to the client
	 * 
	 * @param theRequestDetails
	 *            A bean containing details about the request that is about to be processed, including
	 * @param theResponseObject
	 *            The actual object which is being streamed to the client as a response
	 * @param theRequest
	 *            The incoming request
	 * @param theResponse
	 *            The response. Note that interceptors may choose to provide a response (i.e. by calling
	 *            {@link HttpServletResponse#getWriter()}) but in that case it is important to return <code>true</code>
	 * @return Return <code>true</code> if processing should continue normally. This is generally the right thing to do.
	 *         If your interceptor is providing a response rather than letting HAPI handle the response normally, you
	 *         must return <code>false</code>. In this case, no further processing will occur and no further
	 *         interceptors will be called.
	 * @throws AuthenticationException
	 *             This exception may be thrown to indicate that the interceptor has detected an unauthorized access
	 *             attempt
	 */
	public boolean outgoingResponse(RequestDetails theRequestDetails, Bundle theResponseObject, HttpServletRequest theServletRequest, HttpServletResponse theServletResponse) throws AuthenticationException;

	/**
	 * This method is called after the server implementation method has been called, but before any attempt to stream
	 * the response back to the client
	 * 
	 * @param theRequestDetails
	 *            A bean containing details about the request that is about to be processed, including
	 * @param theResponseObject
	 *            The actual object which is being streamed to the client as a response
	 * @param theRequest
	 *            The incoming request
	 * @param theResponse
	 *            The response. Note that interceptors may choose to provide a response (i.e. by calling
	 *            {@link HttpServletResponse#getWriter()}) but in that case it is important to return <code>true</code>
	 * @return Return <code>true</code> if processing should continue normally. This is generally the right thing to do.
	 *         If your interceptor is providing a response rather than letting HAPI handle the response normally, you
	 *         must return <code>false</code>. In this case, no further processing will occur and no further
	 *         interceptors will be called.
	 * @throws AuthenticationException
	 *             This exception may be thrown to indicate that the interceptor has detected an unauthorized access
	 *             attempt
	 */
	public boolean outgoingResponse(RequestDetails theRequestDetails, IResource theResponseObject, HttpServletRequest theServletRequest, HttpServletResponse theServletResponse) throws AuthenticationException;

	/**
	 * This method is called after the server implementation method has been called, but before any attempt to stream
	 * the response back to the client
	 * 
	 * @param theRequestDetails
	 *            A bean containing details about the request that is about to be processed, including
	 * @param theResponseObject
	 *            The actual object which is being streamed to the client as a response
	 * @param theRequest
	 *            The incoming request
	 * @param theResponse
	 *            The response. Note that interceptors may choose to provide a response (i.e. by calling
	 *            {@link HttpServletResponse#getWriter()}) but in that case it is important to return <code>true</code>
	 * @return Return <code>true</code> if processing should continue normally. This is generally the right thing to do.
	 *         If your interceptor is providing a response rather than letting HAPI handle the response normally, you
	 *         must return <code>false</code>. In this case, no further processing will occur and no further
	 *         interceptors will be called.
	 * @throws AuthenticationException
	 *             This exception may be thrown to indicate that the interceptor has detected an unauthorized access
	 *             attempt
	 */
	public boolean outgoingResponse(RequestDetails theRequestDetails, HttpServletRequest theServletRequest, HttpServletResponse theServletResponse) throws AuthenticationException;

}
