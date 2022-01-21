package ca.uhn.fhir.rest.server.interceptor;

/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.base.resource.BaseOperationOutcome;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.ResponseDetails;
import ca.uhn.fhir.rest.server.IRestfulServerDefaults;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Provides methods to intercept requests and responses. Note that implementations of this interface may wish to use
 * {@link InterceptorAdapter} in order to not need to implement every method.
 * <p>
 * <b>See:</b> See the <a href="https://hapifhir.io/hapi-fhir/docs/interceptors/">server
 * interceptor documentation</a> for more information on how to use this class.
 * </p>
 * Note that unless otherwise stated, it is possible to throw any subclass of
 * {@link BaseServerResponseException} from any interceptor method.
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
	 * @param theRequestDetails  A bean containing details about the request that is about to be processed, including details such as the
	 *                           resource type and logical ID (if any) and other FHIR-specific aspects of the request which have been
	 *                           pulled out of the {@link javax.servlet.http.HttpServletRequest servlet request}. Note that the bean
	 *                           properties are not all guaranteed to be populated, depending on how early during processing the
	 *                           exception occurred.
	 * @param theServletRequest  The incoming request
	 * @param theServletResponse The response. Note that interceptors may choose to provide a response (i.e. by calling
	 *                           {@link javax.servlet.http.HttpServletResponse#getWriter()}) but in that case it is important to return
	 *                           <code>false</code> to indicate that the server itself should not also provide a response.
	 * @return Return <code>true</code> if processing should continue normally. This is generally the right thing to do.
	 * If your interceptor is providing a response rather than letting HAPI handle the response normally, you
	 * must return <code>false</code>. In this case, no further processing will occur and no further interceptors
	 * will be called.
	 * @throws ServletException If this exception is thrown, it will be re-thrown up to the container for handling.
	 * @throws IOException      If this exception is thrown, it will be re-thrown up to the container for handling.
	 */
	@Hook(Pointcut.SERVER_HANDLE_EXCEPTION)
	boolean handleException(RequestDetails theRequestDetails, BaseServerResponseException theException, HttpServletRequest theServletRequest, HttpServletResponse theServletResponse)
		throws ServletException, IOException;

	/**
	 * This method is called just before the actual implementing server method is invoked.
	 *
	 * @param theRequestDetails A bean containing details about the request that is about to be processed, including details such as the
	 *                          resource type and logical ID (if any) and other FHIR-specific aspects of the request which have been
	 *                          pulled out of the {@link HttpServletRequest servlet request}.
	 * @param theRequest        The incoming request
	 * @param theResponse       The response. Note that interceptors may choose to provide a response (i.e. by calling
	 *                          {@link HttpServletResponse#getWriter()}) but in that case it is important to return <code>false</code>
	 *                          to indicate that the server itself should not also provide a response.
	 * @return Return <code>true</code> if processing should continue normally. This is generally the right thing to do.
	 * If your interceptor is providing a response rather than letting HAPI handle the response normally, you
	 * must return <code>false</code>. In this case, no further processing will occur and no further interceptors
	 * will be called.
	 * @throws AuthenticationException This exception may be thrown to indicate that the interceptor has detected an unauthorized access
	 *                                 attempt. If thrown, processing will stop and an HTTP 401 will be returned to the client.
	 */
	@Hook(Pointcut.SERVER_INCOMING_REQUEST_POST_PROCESSED)
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
	 * @param theOperation        The type of operation that the FHIR server has determined that the client is trying to invoke
	 * @param theProcessedRequest An object which will be populated with the details which were extracted from the raw request by the
	 *                            server, e.g. the FHIR operation type and the parsed resource body (if any).
	 */
	@Hook(Pointcut.SERVER_INCOMING_REQUEST_PRE_HANDLED)
	void incomingRequestPreHandled(RestOperationTypeEnum theOperation, ActionRequestDetails theProcessedRequest);

	/**
	 * This method is called before any other processing takes place for each incoming request. It may be used to provide
	 * alternate handling for some requests, or to screen requests before they are handled, etc.
	 * <p>
	 * Note that any exceptions thrown by this method will not be trapped by HAPI (they will be passed up to the server)
	 * </p>
	 *
	 * @param theRequest  The incoming request
	 * @param theResponse The response. Note that interceptors may choose to provide a response (i.e. by calling
	 *                    {@link HttpServletResponse#getWriter()}) but in that case it is important to return <code>false</code>
	 *                    to indicate that the server itself should not also provide a response.
	 * @return Return <code>true</code> if processing should continue normally. This is generally the right thing to do.
	 * If your interceptor is providing a response rather than letting HAPI handle the response normally, you
	 * must return <code>false</code>. In this case, no further processing will occur and no further interceptors
	 * will be called.
	 */
	@Hook(Pointcut.SERVER_INCOMING_REQUEST_PRE_PROCESSED)
	boolean incomingRequestPreProcessed(HttpServletRequest theRequest, HttpServletResponse theResponse);

	/**
	 * Use {@link #outgoingResponse(RequestDetails, IBaseResource, HttpServletRequest, HttpServletResponse)} instead
	 *
	 * @deprecated As of HAPI FHIR 3.2.0, this method is deprecated and will be removed in a future version of HAPI FHIR.
	 */
	@Deprecated
	@Hook(Pointcut.SERVER_OUTGOING_RESPONSE)
	boolean outgoingResponse(RequestDetails theRequestDetails);

	/**
	 * Use {@link #outgoingResponse(RequestDetails, IBaseResource, HttpServletRequest, HttpServletResponse)} instead
	 *
	 * @deprecated As of HAPI FHIR 3.2.0, this method is deprecated and will be removed in a future version of HAPI FHIR.
	 */
	@Deprecated
	@Hook(Pointcut.SERVER_OUTGOING_RESPONSE)
	boolean outgoingResponse(RequestDetails theRequestDetails, HttpServletRequest theServletRequest, HttpServletResponse theServletResponse) throws AuthenticationException;

	/**
	 * Use {@link #outgoingResponse(RequestDetails, IBaseResource, HttpServletRequest, HttpServletResponse)} instead
	 *
	 * @deprecated As of HAPI FHIR 3.2.0, this method is deprecated and will be removed in a future version of HAPI FHIR.
	 */
	@Deprecated
	@Hook(Pointcut.SERVER_OUTGOING_RESPONSE)
	boolean outgoingResponse(RequestDetails theRequestDetails, IBaseResource theResponseObject);

	/**
	 * This method is called after the server implementation method has been called, but before any attempt to stream the
	 * response back to the client.
	 *
	 * @param theRequestDetails  A bean containing details about the request that is about to be processed, including details such as the
	 *                           resource type and logical ID (if any) and other FHIR-specific aspects of the request which have been
	 *                           pulled out of the {@link HttpServletRequest servlet request}.
	 * @param theResponseObject  The actual object which is being streamed to the client as a response. This may be
	 *                           <code>null</code> if the response does not include a resource.
	 * @param theServletRequest  The incoming request
	 * @param theServletResponse The response. Note that interceptors may choose to provide a response (i.e. by calling
	 *                           {@link HttpServletResponse#getWriter()}) but in that case it is important to return <code>false</code>
	 *                           to indicate that the server itself should not also provide a response.
	 * @return Return <code>true</code> if processing should continue normally. This is generally the right thing to do.
	 * If your interceptor is providing a response rather than letting HAPI handle the response normally, you
	 * must return <code>false</code>. In this case, no further processing will occur and no further interceptors
	 * will be called.
	 * @throws AuthenticationException This exception may be thrown to indicate that the interceptor has detected an unauthorized access
	 *                                 attempt. If thrown, processing will stop and an HTTP 401 will be returned to the client.
	 * @deprecated As of HAPI FHIR 3.3.0, this method has been deprecated in
	 * favour of {@link #outgoingResponse(RequestDetails, ResponseDetails, HttpServletRequest, HttpServletResponse)}
	 * and will be removed in a future version of HAPI FHIR.
	 */
	@Deprecated
	@Hook(Pointcut.SERVER_OUTGOING_RESPONSE)
	boolean outgoingResponse(RequestDetails theRequestDetails, IBaseResource theResponseObject, HttpServletRequest theServletRequest, HttpServletResponse theServletResponse)
		throws AuthenticationException;

	/**
	 * This method is called after the server implementation method has been called, but before any attempt to stream the
	 * response back to the client.
	 *
	 * @param theRequestDetails  A bean containing details about the request that is about to be processed, including details such as the
	 *                           resource type and logical ID (if any) and other FHIR-specific aspects of the request which have been
	 *                           pulled out of the {@link HttpServletRequest servlet request}.
	 * @param theResponseDetails This object contains details about the response, including
	 *                           the actual payload that will be returned
	 * @param theServletRequest  The incoming request
	 * @param theServletResponse The response. Note that interceptors may choose to provide a response (i.e. by calling
	 *                           {@link HttpServletResponse#getWriter()}) but in that case it is important to return <code>false</code>
	 *                           to indicate that the server itself should not also provide a response.
	 * @return Return <code>true</code> if processing should continue normally. This is generally the right thing to do.
	 * If your interceptor is providing a response rather than letting HAPI handle the response normally, you
	 * must return <code>false</code>. In this case, no further processing will occur and no further interceptors
	 * will be called.
	 * @throws AuthenticationException This exception may be thrown to indicate that the interceptor has detected an unauthorized access
	 *                                 attempt. If thrown, processing will stop and an HTTP 401 will be returned to the client.
	 */
	@Hook(Pointcut.SERVER_OUTGOING_RESPONSE)
	boolean outgoingResponse(RequestDetails theRequestDetails, ResponseDetails theResponseDetails, HttpServletRequest theServletRequest, HttpServletResponse theServletResponse)
		throws AuthenticationException;


	/**
	 * Use {@link #outgoingResponse(RequestDetails, IBaseResource, HttpServletRequest, HttpServletResponse)} instead
	 *
	 * @deprecated As of HAPI FHIR 3.2.0, this method is deprecated and will be removed in a future version of HAPI FHIR.
	 */
	@Deprecated
	boolean outgoingResponse(RequestDetails theRequestDetails, TagList theResponseObject);

	/**
	 * Use {@link #outgoingResponse(RequestDetails, IBaseResource, HttpServletRequest, HttpServletResponse)} instead
	 *
	 * @deprecated As of HAPI FHIR 3.2.0, this method is deprecated and will be removed in a future version of HAPI FHIR.
	 */
	@Deprecated
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
	 * modify the exception. For example, if this interceptor has nothing to do with exception processing, it
	 * should always return <code>null</code>. If this interceptor adds an OperationOutcome to the exception, it
	 * should return an exception.
	 */
	@Hook(Pointcut.SERVER_PRE_PROCESS_OUTGOING_EXCEPTION)
	BaseServerResponseException preProcessOutgoingException(RequestDetails theRequestDetails, Throwable theException, HttpServletRequest theServletRequest) throws ServletException;

	/**
	 * This method is called after all processing is completed for a request, but only if the
	 * request completes normally (i.e. no exception is thrown).
	 * <p>
	 * This method should not throw any exceptions. Any exception that is thrown by this
	 * method will be logged, but otherwise not acted upon.
	 * </p>
	 * <p>
	 * Note that this individual interceptors will have this method called in the reverse order from the order in
	 * which the interceptors were registered with the server.
	 * </p>
	 *
	 * @param theRequestDetails The request itself
	 */
	@Hook(Pointcut.SERVER_PROCESSING_COMPLETED_NORMALLY)
	void processingCompletedNormally(ServletRequestDetails theRequestDetails);

	/**
	 * @deprecated This class doesn't bring anything that can't be done with {@link RequestDetails}. That
	 * class should be used instead. Deprecated in 4.0.0
	 */
	@Deprecated
	class ActionRequestDetails {
		private final FhirContext myContext;
		private final IIdType myId;
		private final String myResourceType;
		private RequestDetails myRequestDetails;
		private IBaseResource myResource;

		public ActionRequestDetails(RequestDetails theRequestDetails) {
			myId = theRequestDetails.getId();
			myResourceType = theRequestDetails.getResourceName();
			myContext = theRequestDetails.getServer().getFhirContext();
			myRequestDetails = theRequestDetails;
		}

		public ActionRequestDetails(RequestDetails theRequestDetails, FhirContext theContext, IBaseResource theResource) {
			this(theRequestDetails, theContext, theContext.getResourceType(theResource), theResource.getIdElement());
			myResource = theResource;
		}

		public ActionRequestDetails(RequestDetails theRequestDetails, FhirContext theContext, String theResourceType, IIdType theId) {
			if (theId != null && isBlank(theId.getValue())) {
				myId = null;
			} else {
				myId = theId;
			}
			myResourceType = theResourceType;
			myContext = theContext;
			myRequestDetails = theRequestDetails;
		}

		public ActionRequestDetails(RequestDetails theRequestDetails, IBaseResource theResource) {
			this(theRequestDetails, theRequestDetails.getServer().getFhirContext().getResourceType(theResource), theResource.getIdElement());
			myResource = theResource;
		}

		public ActionRequestDetails(RequestDetails theRequestDetails, IBaseResource theResource, String theResourceType, IIdType theId) {
			this(theRequestDetails, theResourceType, theId);
			myResource = theResource;
		}

		/**
		 * Constructor
		 *
		 * @param theRequestDetails The request details to wrap
		 * @param theId             The ID of the resource being created (note that the ID should have the resource type populated)
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
		 * This method should not be called by client code
		 */
		public void setResource(IBaseResource theObject) {
			myResource = theObject;
		}

		/**
		 * Returns the resource type this request pertains to, or <code>null</code> if this request is not type specific
		 * (e.g. server-history)
		 */
		public String getResourceType() {
			return myResourceType;
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("id", myId)
				.append("resourceType", myResourceType)
				.append("resource", myResource)
				.toString();
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

			IIdType previousRequestId = requestDetails.getId();
			requestDetails.setId(getId());

			IInterceptorService interceptorService = server.getInterceptorService();
			if (interceptorService == null) {
				return;
			}

			HookParams params = new HookParams();
			params.add(RestOperationTypeEnum.class, theOperationType);
			params.add(this);
			params.add(RequestDetails.class, this.getRequestDetails());
			params.addIfMatchesType(ServletRequestDetails.class, this.getRequestDetails());
			interceptorService.callHooks(Pointcut.SERVER_INCOMING_REQUEST_PRE_HANDLED, params);

			// Reset the request ID
			requestDetails.setId(previousRequestId);

		}

	}

}
