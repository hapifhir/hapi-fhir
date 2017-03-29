package ca.uhn.fhir.rest.server.interceptor;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.base.resource.BaseOperationOutcome;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.IRestfulServerDefaults;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;

/**
 * Provides methods to intercept requests and responses. Note that implementations of this interface may wish to use
 * {@link InterceptorAdapter} in order to not need to implement every method.
 * <p>
 * <b>See:</b> See the <a href="http://jamesagnew.github.io/hapi-fhir/doc_rest_server_interceptor.html">server
 * interceptor documentation</a> for more information on how to use this class.
 * </p>
 */
public interface IServerInterceptor {

	/**
	 * This method is called upon any exception being thrown within the server's request processing code. This includes
	 * any exceptions thrown within resource provider methods (e.g. {@link Search} and {@link Read} methods) as well as
	 * any runtime exceptions thrown by the server itself. This also includes any {@link AuthenticationException}s
	 * thrown.
	 * <p>
	 * Implementations of this method may choose to ignore/log/count/etc exceptions, and return <code>true</code>. In
	 * this case, processing will continue, and the server will automatically generate an {@link BaseOperationOutcome
	 * OperationOutcome}. Implementations may also choose to provide their own response to the client. In this case, they
	 * should return <code>false</code>, to indicate that they have handled the request and processing should stop.
	 * </p>
	 * 
	 *
	 * @param theRequestDetails
	 *           A bean containing details about the request that is about to be processed, including details such as the
	 *           resource type and logical ID (if any) and other FHIR-specific aspects of the request which have been
	 *           pulled out of the {@link javax.servlet.http.HttpServletRequest servlet request}. Note that the bean
	 *           properties are not all guaranteed to be populated, depending on how early during processing the
	 *           exception occurred.
	 * @param theServletRequest
	 *           The incoming request
	 * @param theServletResponse
	 *           The response. Note that interceptors may choose to provide a response (i.e. by calling
	 *           {@link javax.servlet.http.HttpServletResponse#getWriter()}) but in that case it is important to return
	 *           <code>false</code> to indicate that the server itself should not also provide a response.
	 * @return Return <code>true</code> if processing should continue normally. This is generally the right thing to do.
	 *         If your interceptor is providing a response rather than letting HAPI handle the response normally, you
	 *         must return <code>false</code>. In this case, no further processing will occur and no further interceptors
	 *         will be called.
	 * @throws ServletException
	 *            If this exception is thrown, it will be re-thrown up to the container for handling.
	 * @throws IOException
	 *            If this exception is thrown, it will be re-thrown up to the container for handling.
	 */
	boolean handleException(RequestDetails theRequestDetails, BaseServerResponseException theException, HttpServletRequest theServletRequest, HttpServletResponse theServletResponse)
			throws ServletException, IOException;

	/**
	 * This method is called just before the actual implementing server method is invoked.
	 * <p>
	 * Note about exceptions:
	 * </p>
	 * 
	 * @param theRequestDetails
	 *           A bean containing details about the request that is about to be processed, including details such as the
	 *           resource type and logical ID (if any) and other FHIR-specific aspects of the request which have been
	 *           pulled out of the {@link HttpServletRequest servlet request}.
	 * @param theRequest
	 *           The incoming request
	 * @param theResponse
	 *           The response. Note that interceptors may choose to provide a response (i.e. by calling
	 *           {@link HttpServletResponse#getWriter()}) but in that case it is important to return <code>false</code>
	 *           to indicate that the server itself should not also provide a response.
	 * @return Return <code>true</code> if processing should continue normally. This is generally the right thing to do.
	 *         If your interceptor is providing a response rather than letting HAPI handle the response normally, you
	 *         must return <code>false</code>. In this case, no further processing will occur and no further interceptors
	 *         will be called.
	 * @throws AuthenticationException
	 *            This exception may be thrown to indicate that the interceptor has detected an unauthorized access
	 *            attempt. If thrown, processing will stop and an HTTP 401 will be returned to the client.
	 */
	boolean incomingRequestPostProcessed(RequestDetails theRequestDetails, HttpServletRequest theRequest, HttpServletResponse theResponse) throws AuthenticationException;

	/**
	 * Invoked before an incoming request is processed. Note that this method is called
	 * after the server has begin preparing the response to the incoming client request.
	 * As such, it is not able to supply a response to the incoming request in the way that
	 * {@link #incomingRequestPreHandled(RestOperationTypeEnum, ActionRequestDetails)} and
	 * {@link #incomingRequestPostProcessed(RequestDetails, HttpServletRequest, HttpServletResponse)}
	 * are.
	 * <p>
	 * This method may however throw a subclass of {@link BaseServerResponseException}, and processing
	 * will be aborted with an appropriate error returned to the client.
	 * </p>
	 * 
	 * @param theServletRequest
	 *           The incoming servlet request as provided by the servlet container
	 * @param theOperation
	 *           The type of operation that the FHIR server has determined that the client is trying to invoke
	 * @param theProcessedRequest
	 *           An object which will be populated with the details which were extracted from the raw request by the
	 *           server, e.g. the FHIR operation type and the parsed resource body (if any).
	 */
	void incomingRequestPreHandled(RestOperationTypeEnum theOperation, ActionRequestDetails theProcessedRequest);

	/**
	 * This method is called before any other processing takes place for each incoming request. It may be used to provide
	 * alternate handling for some requests, or to screen requests before they are handled, etc.
	 * <p>
	 * Note that any exceptions thrown by this method will not be trapped by HAPI (they will be passed up to the server)
	 * </p>
	 * 
	 * @param theRequest
	 *           The incoming request
	 * @param theResponse
	 *           The response. Note that interceptors may choose to provide a response (i.e. by calling
	 *           {@link HttpServletResponse#getWriter()}) but in that case it is important to return <code>false</code>
	 *           to indicate that the server itself should not also provide a response.
	 * @return Return <code>true</code> if processing should continue normally. This is generally the right thing to do.
	 *         If your interceptor is providing a response rather than letting HAPI handle the response normally, you
	 *         must return <code>false</code>. In this case, no further processing will occur and no further interceptors
	 *         will be called.
	 */
	boolean incomingRequestPreProcessed(HttpServletRequest theRequest, HttpServletResponse theResponse);

	/**
	 * This method is called after the server implementation method has been called, but before any attempt to stream the
	 * response back to the client
	 * 
	 * @param theRequestDetails
	 *           A bean containing details about the request that is about to be processed, including details such as the
	 *           resource type and logical ID (if any) and other FHIR-specific aspects of the request which have been
	 *           pulled out of the {@link HttpServletRequest servlet request}.
	 * @return Return <code>true</code> if processing should continue normally. This is generally the right thing to do.
	 *         If your interceptor is providing a response rather than letting HAPI handle the response normally, you
	 *         must return <code>false</code>. In this case, no further processing will occur and no further interceptors
	 *         will be called.
	 * @throws AuthenticationException
	 *            This exception may be thrown to indicate that the interceptor has detected an unauthorized access
	 *            attempt. If thrown, processing will stop and an HTTP 401 will be returned to the client.
	 */
	boolean outgoingResponse(RequestDetails theRequestDetails);

	/**
	 * This method is called after the server implementation method has been called, but before any attempt to stream the
	 * response back to the client
	 * 
	 * @param theRequestDetails
	 *           A bean containing details about the request that is about to be processed, including
	 * @param theResponseObject
	 *           The actual object which is being streamed to the client as a response
	 * @return Return <code>true</code> if processing should continue normally. This is generally the right thing to do.
	 *         If your interceptor is providing a response rather than letting HAPI handle the response normally, you
	 *         must return <code>false</code>. In this case, no further processing will occur and no further interceptors
	 *         will be called.
	 * @throws AuthenticationException
	 *            This exception may be thrown to indicate that the interceptor has detected an unauthorized access
	 *            attempt. If thrown, processing will stop and an HTTP 401 will be returned to the client.
	 */
	boolean outgoingResponse(RequestDetails theRequest, Bundle theResponseObject);

	/**
	 * This method is called after the server implementation method has been called, but before any attempt to stream the
	 * response back to the client
	 * 
	 * @param theRequestDetails
	 *           A bean containing details about the request that is about to be processed, including
	 * @param theResponseObject
	 *           The actual object which is being streamed to the client as a response
	 * @param theServletRequest
	 *           The incoming request
	 * @param theServletResponse
	 *           The response. Note that interceptors may choose to provide a response (i.e. by calling
	 *           {@link HttpServletResponse#getWriter()}) but in that case it is important to return <code>false</code>
	 *           to indicate that the server itself should not also provide a response.
	 * @return Return <code>true</code> if processing should continue normally. This is generally the right thing to do.
	 *         If your interceptor is providing a response rather than letting HAPI handle the response normally, you
	 *         must return <code>false</code>. In this case, no further processing will occur and no further interceptors
	 *         will be called.
	 * @throws AuthenticationException
	 *            This exception may be thrown to indicate that the interceptor has detected an unauthorized access
	 *            attempt. If thrown, processing will stop and an HTTP 401 will be returned to the client.
	 */
	boolean outgoingResponse(RequestDetails theRequestDetails, Bundle theResponseObject, HttpServletRequest theServletRequest, HttpServletResponse theServletResponse) throws AuthenticationException;

	/**
	 * This method is called after the server implementation method has been called, but before any attempt to stream the
	 * response back to the client
	 * 
	 * @param theRequestDetails
	 *           A bean containing details about the request that is about to be processed, including details such as the
	 *           resource type and logical ID (if any) and other FHIR-specific aspects of the request which have been
	 *           pulled out of the {@link HttpServletRequest servlet request}.
	 * @param theServletRequest
	 *           The incoming request
	 * @param theServletResponse
	 *           The response. Note that interceptors may choose to provide a response (i.e. by calling
	 *           {@link HttpServletResponse#getWriter()}) but in that case it is important to return <code>false</code>
	 *           to indicate that the server itself should not also provide a response.
	 * @return Return <code>true</code> if processing should continue normally. This is generally the right thing to do.
	 *         If your interceptor is providing a response rather than letting HAPI handle the response normally, you
	 *         must return <code>false</code>. In this case, no further processing will occur and no further interceptors
	 *         will be called.
	 * @throws AuthenticationException
	 *            This exception may be thrown to indicate that the interceptor has detected an unauthorized access
	 *            attempt. If thrown, processing will stop and an HTTP 401 will be returned to the client.
	 */
	boolean outgoingResponse(RequestDetails theRequestDetails, HttpServletRequest theServletRequest, HttpServletResponse theServletResponse) throws AuthenticationException;

	/**
	 * This method is called after the server implementation method has been called, but before any attempt to stream the
	 * response back to the client
	 * 
	 * @param theRequestDetails
	 *           A bean containing details about the request that is about to be processed, including details such as the
	 *           resource type and logical ID (if any) and other FHIR-specific aspects of the request which have been
	 *           pulled out of the {@link HttpServletRequest servlet request}.
	 * @param theResponseObject
	 *           The actual object which is being streamed to the client as a response
	 * @return Return <code>true</code> if processing should continue normally. This is generally the right thing to do.
	 *         If your interceptor is providing a response rather than letting HAPI handle the response normally, you
	 *         must return <code>false</code>. In this case, no further processing will occur and no further interceptors
	 *         will be called.
	 * @throws AuthenticationException
	 *            This exception may be thrown to indicate that the interceptor has detected an unauthorized access
	 *            attempt. If thrown, processing will stop and an HTTP 401 will be returned to the client.
	 */
	boolean outgoingResponse(RequestDetails theRequestDetails, IBaseResource theResponseObject);

	/**
	 * This method is called after the server implementation method has been called, but before any attempt to stream the
	 * response back to the client
	 * 
	 * @param theRequestDetails
	 *           A bean containing details about the request that is about to be processed, including details such as the
	 *           resource type and logical ID (if any) and other FHIR-specific aspects of the request which have been
	 *           pulled out of the {@link HttpServletRequest servlet request}.
	 * @param theResponseObject
	 *           The actual object which is being streamed to the client as a response
	 * @param theServletRequest
	 *           The incoming request
	 * @param theServletResponse
	 *           The response. Note that interceptors may choose to provide a response (i.e. by calling
	 *           {@link HttpServletResponse#getWriter()}) but in that case it is important to return <code>false</code>
	 *           to indicate that the server itself should not also provide a response.
	 * @return Return <code>true</code> if processing should continue normally. This is generally the right thing to do.
	 *         If your interceptor is providing a response rather than letting HAPI handle the response normally, you
	 *         must return <code>false</code>. In this case, no further processing will occur and no further interceptors
	 *         will be called.
	 * @throws AuthenticationException
	 *            This exception may be thrown to indicate that the interceptor has detected an unauthorized access
	 *            attempt. If thrown, processing will stop and an HTTP 401 will be returned to the client.
	 */
	boolean outgoingResponse(RequestDetails theRequestDetails, IBaseResource theResponseObject, HttpServletRequest theServletRequest, HttpServletResponse theServletResponse)
			throws AuthenticationException;

	/**
	 * This method is called after the server implementation method has been called, but before any attempt to stream the
	 * response back to the client
	 * 
	 * @param theRequestDetails
	 *           A bean containing details about the request that is about to be processed, including details such as the
	 *           resource type and logical ID (if any) and other FHIR-specific aspects of the request which have been
	 *           pulled out of the {@link HttpServletRequest servlet request}.
	 * @param theResponseObject
	 *           The actual object which is being streamed to the client as a response
	 * @return Return <code>true</code> if processing should continue normally. This is generally the right thing to do.
	 *         If your interceptor is providing a response rather than letting HAPI handle the response normally, you
	 *         must return <code>false</code>. In this case, no further processing will occur and no further interceptors
	 *         will be called.
	 * @throws AuthenticationException
	 *            This exception may be thrown to indicate that the interceptor has detected an unauthorized access
	 *            attempt. If thrown, processing will stop and an HTTP 401 will be returned to the client.
	 */
	boolean outgoingResponse(RequestDetails theRequestDetails, TagList theResponseObject);

	/**
	 * This method is called after the server implementation method has been called, but before any attempt to stream the
	 * response back to the client
	 * 
	 * @param theRequestDetails
	 *           A bean containing details about the request that is about to be processed, including details such as the
	 *           resource type and logical ID (if any) and other FHIR-specific aspects of the request which have been
	 *           pulled out of the {@link HttpServletRequest servlet request}.
	 * @param theResponseObject
	 *           The actual object which is being streamed to the client as a response
	 * @param theServletRequest
	 *           The incoming request
	 * @param theServletResponse
	 *           The response. Note that interceptors may choose to provide a response (i.e. by calling
	 *           {@link HttpServletResponse#getWriter()}) but in that case it is important to return <code>false</code>
	 *           to indicate that the server itself should not also provide a response.
	 * @return Return <code>true</code> if processing should continue normally. This is generally the right thing to do.
	 *         If your interceptor is providing a response rather than letting HAPI handle the response normally, you
	 *         must return <code>false</code>. In this case, no further processing will occur and no further interceptors
	 *         will be called.
	 * @throws AuthenticationException
	 *            This exception may be thrown to indicate that the interceptor has detected an unauthorized access
	 *            attempt. If thrown, processing will stop and an HTTP 401 will be returned to the client.
	 */
	boolean outgoingResponse(RequestDetails theRequestDetails, TagList theResponseObject, HttpServletRequest theServletRequest, HttpServletResponse theServletResponse) throws AuthenticationException;

	/**
	 * This method is called upon any exception being thrown within the server's request processing code. This includes
	 * any exceptions thrown within resource provider methods (e.g. {@link Search} and {@link Read} methods) as well as
	 * any runtime exceptions thrown by the server itself. This method is invoked for each interceptor (until one of them
	 * returns a non-<code>null</code> response or the end of the list is reached), after which
	 * {@link #handleException(RequestDetails, BaseServerResponseException, HttpServletRequest, HttpServletResponse)} is
	 * called for each interceptor.
	 * <p>
	 * This may be used to add an OperationOutcome to a response, or to convert between exception types for any reason.
	 * </p>
	 * <p>
	 * Implementations of this method may choose to ignore/log/count/etc exceptions, and return <code>null</code>. In
	 * this case, processing will continue, and the server will automatically generate an {@link BaseOperationOutcome
	 * OperationOutcome}. Implementations may also choose to provide their own response to the client. In this case, they
	 * should return a non-<code>null</code>, to indicate that they have handled the request and processing should stop.
	 * </p>
	 * 
	 * @return Returns the new exception to use for processing, or <code>null</code> if this interceptor is not trying to
	 *         modify the exception. For example, if this interceptor has nothing to do with exception processing, it
	 *         should always return <code>null</code>. If this interceptor adds an OperationOutcome to the exception, it
	 *         should return an exception.
	 */
	BaseServerResponseException preProcessOutgoingException(RequestDetails theRequestDetails, Throwable theException, HttpServletRequest theServletRequest) throws ServletException;

	/**
	 * This method is called after all processing is completed for a request, but only if the
	 * request completes normally (i.e. no exception is thrown).
	 * <p>
	 * Note that this individual interceptors will have this method called in the reverse order from the order in 
	 * which the interceptors were registered with the server. 
	 * </p>
	 * @param theRequestDetails
	 *           The request itself
	 */
	void processingCompletedNormally(ServletRequestDetails theRequestDetails);

	public static class ActionRequestDetails {
		private final FhirContext myContext;
		private final IIdType myId;
		private RequestDetails myRequestDetails;
		private IBaseResource myResource;
		private final String myResourceType;

		public ActionRequestDetails(RequestDetails theRequestDetails) {
			myId = theRequestDetails.getId();
			myResourceType = theRequestDetails.getResourceName();
			myContext = theRequestDetails.getServer().getFhirContext();
			myRequestDetails = theRequestDetails;
		}

		public ActionRequestDetails(RequestDetails theRequestDetails, FhirContext theContext, IBaseResource theResource) {
			this(theRequestDetails, theContext, theContext.getResourceDefinition(theResource).getName(), theResource.getIdElement());
			myResource = theResource;
		}

		public ActionRequestDetails(RequestDetails theRequestDetails, FhirContext theContext, String theResourceType, IIdType theId) {
			myId = theId;
			myResourceType = theResourceType;
			myContext = theContext;
			myRequestDetails = theRequestDetails;
		}

		public ActionRequestDetails(RequestDetails theRequestDetails, IBaseResource theResource) {
			this(theRequestDetails, theRequestDetails.getServer().getFhirContext().getResourceDefinition(theResource).getName(), theResource.getIdElement());
			myResource = theResource;
		}

		public ActionRequestDetails(RequestDetails theRequestDetails, IBaseResource theResource, String theResourceType, IIdType theId) {
			this(theRequestDetails, theResourceType, theId);
			myResource = theResource;
		}

		/**
		 * Constructor
		 * 
		 * @param theRequestDetails
		 *           The request details to wrap
		 * @param theId
		 *           The ID of the resource being created (note that the ID should have the resource type populated)
		 */
		public ActionRequestDetails(RequestDetails theRequestDetails, IIdType theId) {
			this(theRequestDetails, theId.getResourceType(), theId);
		}

		public ActionRequestDetails(RequestDetails theRequestDetails, String theResourceType, IIdType theId) {
			this(theRequestDetails, theRequestDetails.getServer().getFhirContext(), theResourceType, theId);
		}

		public FhirContext getContext() {
			return myContext;
		}

		/**
		 * Returns the ID of the incoming request (typically this is from the request URL)
		 */
		public IIdType getId() {
			return myId;
		}

		/**
		 * Returns the request details associated with this request
		 */
		public RequestDetails getRequestDetails() {
			return myRequestDetails;
		}

		/**
		 * For requests where a resource is passed from the client to the server (e.g. create, update, etc.) this method
		 * will return the resource which was provided by the client. Otherwise, this method will return <code>null</code>
		 * .
		 * <p>
		 * Note that this method is currently only populated if the handling method has a parameter annotated with the
		 * {@link ResourceParam} annotation.
		 * </p>
		 */
		public IBaseResource getResource() {
			return myResource;
		}

		/**
		 * Returns the resource type this request pertains to, or <code>null</code> if this request is not type specific
		 * (e.g. server-history)
		 */
		public String getResourceType() {
			return myResourceType;
		}

		/**
		 * Returns the same map which was
		 */
		public Map<Object, Object> getUserData() {
			if (myRequestDetails == null) {
				/*
				 * Technically this shouldn't happen.. But some of the unit tests use old IXXXDao methods that don't
				 * take in a RequestDetails object. Eventually I guess we should clean that up.
				 */
				return Collections.emptyMap();
			}
			return myRequestDetails.getUserData();
		}

		/**
		 * This method may be invoked by user code to notify interceptors that a nested
		 * operation is being invoked which is denoted by this request details.
		 */
		public void notifyIncomingRequestPreHandled(RestOperationTypeEnum theOperationType) {
			RequestDetails requestDetails = getRequestDetails();
			if (requestDetails == null) {
				return;
			}
			IRestfulServerDefaults server = requestDetails.getServer();
			if (server == null) {
				return;
			}
			List<IServerInterceptor> interceptors = server.getInterceptors();
			for (IServerInterceptor next : interceptors) {
				next.incomingRequestPreHandled(theOperationType, this);
			}
		}

		/**
		 * This method should not be called by client code
		 */
		public void setResource(IBaseResource theObject) {
			myResource = theObject;
		}

	}

}
