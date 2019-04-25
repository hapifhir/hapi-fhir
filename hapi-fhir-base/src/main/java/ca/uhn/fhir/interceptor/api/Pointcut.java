package ca.uhn.fhir.interceptor.api;

/*-
 * #%L
 * HAPI FHIR - Core Library
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

import ca.uhn.fhir.model.base.resource.BaseOperationOutcome;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Value for {@link Hook#value()}
 * <p>
 * Hook pointcuts are divided into several broad categories:
 * <ul>
 * <li>INTERCEPTOR_xxx: Hooks on the interceptor infrastructure itself</li>
 * <li>CLIENT_xxx: Hooks on the HAPI FHIR Client framework</li>
 * <li>SERVER_xxx: Hooks on the HAPI FHIR Server framework</li>
 * <li>SUBSCRIPTION_xxx: Hooks on the HAPI FHIR Subscription framework</li>
 * <li>STORAGE_xxx: Hooks on the storage engine</li>
 * <li>JPA_PERFTRACE_xxx: Performance tracing hooks on the JPA server</li>
 * </ul>
 * </p>
 */
public enum Pointcut {

	/**
	 * <b>Registry Hook: </b>
	 * This pointcut will be called once when a given interceptor is registered
	 */
	INTERCEPTOR_REGISTERED(void.class),

	/**
	 * <b>Client Hook:</b>
	 * This hook is called before an HTTP client request is sent
	 * <p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>
	 * ca.uhn.fhir.rest.client.api.IHttpRequest - The details of the request
	 * </li>
	 * </ul>
	 * </p>
	 * Hook methods must return <code>void</code>.
	 */
	CLIENT_REQUEST(void.class,
		"ca.uhn.fhir.rest.client.api.IHttpRequest"
	),

	/**
	 * <b>Client Hook:</b>
	 * This hook is called after an HTTP client request has completed, prior to returning
	 * the results to the calling code. Hook methods may modify the response.
	 * <p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>
	 * ca.uhn.fhir.rest.client.api.IHttpRequest - The details of the request
	 * ca.uhn.fhir.rest.client.api.IHttpRequest - The details of the response
	 * </li>
	 * </ul>
	 * </p>
	 * Hook methods must return <code>void</code>.
	 */
	CLIENT_RESPONSE(void.class,
		"ca.uhn.fhir.rest.client.api.IHttpRequest",
		"ca.uhn.fhir.rest.client.api.IHttpResponse"
	),

	/**
	 * <b>Server Hook: </b>
	 * This hook is called before any other processing takes place for each incoming request. It may be used to provide
	 * alternate handling for some requests, or to screen requests before they are handled, etc.
	 * <p>
	 * Note that any exceptions thrown by this method will not be trapped by HAPI (they will be passed up to the server)
	 * </p>
	 * <p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>
	 * javax.servlet.http.HttpServletRequest - The servlet request, when running in a servlet environment
	 * </li>
	 * <li>
	 * javax.servlet.http.HttpServletResponse - The servlet response, when running in a servlet environment
	 * </li>
	 * </ul>
	 * </p>
	 * Hook methods may return <code>true</code> or <code>void</code> if processing should continue normally.
	 * This is generally the right thing to do. If your interceptor is providing a response rather than
	 * letting HAPI handle the response normally, you must return <code>false</code>. In this case,
	 * no further processing will occur and no further interceptors will be called.
	 */
	SERVER_INCOMING_REQUEST_PRE_PROCESSED(boolean.class,
		"javax.servlet.http.HttpServletRequest",
		"javax.servlet.http.HttpServletResponse"
	),

	/**
	 * <b>Server Hook: </b>
	 * This hook is invoked upon any exception being thrown within the server's request processing code. This includes
	 * any exceptions thrown within resource provider methods (e.g. {@link Search} and {@link Read} methods) as well as
	 * any runtime exceptions thrown by the server itself. This also includes any {@link AuthenticationException}s
	 * thrown.
	 * <p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>
	 * ca.uhn.fhir.rest.api.server.RequestDetails - A bean containing details about the request that is about to be processed, including details such as the
	 * resource type and logical ID (if any) and other FHIR-specific aspects of the request which have been
	 * pulled out of the servlet request. Note that the bean
	 * properties are not all guaranteed to be populated, depending on how early during processing the
	 * exception occurred.
	 * </li>
	 * <li>
	 * ca.uhn.fhir.rest.server.servlet.ServletRequestDetails - A bean containing details about the request that is about to be processed, including details such as the
	 * resource type and logical ID (if any) and other FHIR-specific aspects of the request which have been
	 * pulled out of the servlet request. Note that the bean
	 * properties are not all guaranteed to be populated, depending on how early during processing the
	 * exception occurred. This parameter is identical to the RequestDetails parameter above but will
	 * only be populated when operating in a RestfulServer implementation. It is provided as a convenience.
	 * </li>
	 * <li>
	 * javax.servlet.http.HttpServletRequest - The servlet request, when running in a servlet environment
	 * </li>
	 * <li>
	 * javax.servlet.http.HttpServletResponse - The servlet response, when running in a servlet environment
	 * </li>
	 * <li>
	 * ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException - The exception that was thrown
	 * </li>
	 * </ul>
	 * </p>
	 * <p>
	 * Implementations of this method may choose to ignore/log/count/etc exceptions, and return <code>true</code> or
	 * <code>void</code>. In
	 * this case, processing will continue, and the server will automatically generate an {@link BaseOperationOutcome
	 * OperationOutcome}. Implementations may also choose to provide their own response to the client. In this case, they
	 * should return <code>false</code>, to indicate that they have handled the request and processing should stop.
	 * </p>
	 */
	SERVER_HANDLE_EXCEPTION(boolean.class,
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails",
		"javax.servlet.http.HttpServletRequest",
		"javax.servlet.http.HttpServletResponse",
		"ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException"
	),

	/**
	 * <b>Server Hook:</b>
	 * This method is called just before the actual implementing server method is invoked.
	 * <p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>
	 * ca.uhn.fhir.rest.api.server.RequestDetails - A bean containing details about the request that is about to be processed, including details such as the
	 * resource type and logical ID (if any) and other FHIR-specific aspects of the request which have been
	 * pulled out of the servlet request. Note that the bean
	 * properties are not all guaranteed to be populated, depending on how early during processing the
	 * exception occurred.
	 * </li>
	 * <li>
	 * ca.uhn.fhir.rest.server.servlet.ServletRequestDetails - A bean containing details about the request that is about to be processed, including details such as the
	 * resource type and logical ID (if any) and other FHIR-specific aspects of the request which have been
	 * pulled out of the servlet request. This parameter is identical to the RequestDetails parameter above but will
	 * only be populated when operating in a RestfulServer implementation. It is provided as a convenience.
	 * </li>
	 * <li>
	 * javax.servlet.http.HttpServletRequest - The servlet request, when running in a servlet environment
	 * </li>
	 * <li>
	 * javax.servlet.http.HttpServletResponse - The servlet response, when running in a servlet environment
	 * </li>
	 * </ul>
	 * <p>
	 * Hook methods may return <code>true</code> or <code>void</code> if processing should continue normally.
	 * This is generally the right thing to do.
	 * If your interceptor is providing an HTTP response rather than letting HAPI handle the response normally, you
	 * must return <code>false</code>. In this case, no further processing will occur and no further interceptors
	 * will be called.
	 * </p>
	 * <p>
	 * Hook methods may also throw {@link AuthenticationException} if they would like. This exception may be thrown
	 * to indicate that the interceptor has detected an unauthorized access
	 * attempt. If thrown, processing will stop and an HTTP 401 will be returned to the client.
	 */
	SERVER_INCOMING_REQUEST_POST_PROCESSED(boolean.class,
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails",
		"javax.servlet.http.HttpServletRequest",
		"javax.servlet.http.HttpServletResponse"
	),


	/**
	 * <b>Server Hook:</b>
	 * This hook is invoked before an incoming request is processed. Note that this method is called
	 * after the server has begin preparing the response to the incoming client request.
	 * As such, it is not able to supply a response to the incoming request in the way that
	 * SERVER_INCOMING_REQUEST_PRE_HANDLED and
	 * {@link #SERVER_INCOMING_REQUEST_POST_PROCESSED}
	 * are.
	 * <p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>
	 * ca.uhn.fhir.rest.api.RestOperationTypeEnum - The type of operation that the FHIR server has determined that the client is trying to invoke
	 * </li>
	 * <li>
	 * ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails - An object which will be populated with the details which were extracted from the raw request by the
	 * server, e.g. the FHIR operation type and the parsed resource body (if any).
	 * </li>
	 * </ul>
	 * </p>
	 * <p>
	 * Hook methods must return <code>void</code>
	 * </p>
	 * <p>
	 * Hook methods method may throw a subclass of {@link BaseServerResponseException}, and processing
	 * will be aborted with an appropriate error returned to the client.
	 * </p>
	 */
	SERVER_INCOMING_REQUEST_PRE_HANDLED(void.class,
		"ca.uhn.fhir.rest.api.RestOperationTypeEnum",
		"ca.uhn.fhir.rest.server.interceptor.IServerInterceptor$ActionRequestDetails"
	),

	/**
	 * <b>Server Hook:</b>
	 * This method is called upon any exception being thrown within the server's request processing code. This includes
	 * any exceptions thrown within resource provider methods (e.g. {@link Search} and {@link Read} methods) as well as
	 * any runtime exceptions thrown by the server itself. This hook method is invoked for each interceptor (until one of them
	 * returns a non-<code>null</code> response or the end of the list is reached), after which
	 * {@link #SERVER_HANDLE_EXCEPTION} is
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
	 * <p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>
	 * ca.uhn.fhir.rest.api.server.RequestDetails - A bean containing details about the request that is about to be processed, including details such as the
	 * resource type and logical ID (if any) and other FHIR-specific aspects of the request which have been
	 * pulled out of the servlet request. Note that the bean
	 * properties are not all guaranteed to be populated, depending on how early during processing the
	 * exception occurred.
	 * </li>
	 * <li>
	 * ca.uhn.fhir.rest.server.servlet.ServletRequestDetails - A bean containing details about the request that is about to be processed, including details such as the
	 * resource type and logical ID (if any) and other FHIR-specific aspects of the request which have been
	 * pulled out of the servlet request. This parameter is identical to the RequestDetails parameter above but will
	 * only be populated when operating in a RestfulServer implementation. It is provided as a convenience.
	 * </li>
	 * <li>
	 * java.lang.Throwable - The exception that was thrown. This will often be an instance of
	 * {@link BaseServerResponseException} but will not necessarily be one (e.g. it could be a
	 * {@link NullPointerException} in the case of a bug being triggered.
	 * </li>
	 * <li>
	 * javax.servlet.http.HttpServletRequest - The servlet request, when running in a servlet environment
	 * </li>
	 * <li>
	 * javax.servlet.http.HttpServletResponse - The servlet response, when running in a servlet environment
	 * </li>
	 * </ul>
	 * <p>
	 * Hook methods may return a new exception to use for processing, or <code>null</code> if this interceptor is not trying to
	 * modify the exception. For example, if this interceptor has nothing to do with exception processing, it
	 * should always return <code>null</code>. If this interceptor adds an OperationOutcome to the exception, it
	 * should return an exception.
	 * </p>
	 */
	SERVER_PRE_PROCESS_OUTGOING_EXCEPTION(BaseServerResponseException.class,
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails",
		"java.lang.Throwable",
		"javax.servlet.http.HttpServletRequest",
		"javax.servlet.http.HttpServletResponse"
	),

	/**
	 * <b>Server Hook:</b>
	 * This method is called after the server implementation method has been called, but before any attempt
	 * to stream the response back to the client. Interceptors may examine or modify the response before it
	 * is returned, or even prevent the response.
	 * <p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>
	 * ca.uhn.fhir.rest.api.server.RequestDetails - A bean containing details about the request that is about to be processed, including details such as the
	 * resource type and logical ID (if any) and other FHIR-specific aspects of the request which have been
	 * pulled out of the servlet request.
	 * </li>
	 * <li>
	 * ca.uhn.fhir.rest.server.servlet.ServletRequestDetails - A bean containing details about the request that is about to be processed, including details such as the
	 * resource type and logical ID (if any) and other FHIR-specific aspects of the request which have been
	 * pulled out of the servlet request. This parameter is identical to the RequestDetails parameter above but will
	 * only be populated when operating in a RestfulServer implementation. It is provided as a convenience.
	 * </li>
	 * <li>
	 * org.hl7.fhir.instance.model.api.IBaseResource - The resource that will be returned. This parameter may be <code>null</code> for some responses.
	 * </li>
	 * <li>
	 * ca.uhn.fhir.rest.api.server.ResponseDetails - This object contains details about the response, including the contents. Hook methods may modify this object to change or replace the response.
	 * </li>
	 * <li>
	 * javax.servlet.http.HttpServletRequest - The servlet request, when running in a servlet environment
	 * </li>
	 * <li>
	 * javax.servlet.http.HttpServletResponse - The servlet response, when running in a servlet environment
	 * </li>
	 * </ul>
	 * </p>
	 * <p>
	 * Hook methods may return <code>true</code> or <code>void</code> if processing should continue normally.
	 * This is generally the right thing to do. If your interceptor is providing a response rather than
	 * letting HAPI handle the response normally, you must return <code>false</code>. In this case,
	 * no further processing will occur and no further interceptors will be called.
	 * </p>
	 * <p>
	 * Hook methods may also throw {@link AuthenticationException} to indicate that the interceptor
	 * has detected an unauthorized access attempt. If thrown, processing will stop and an HTTP 401
	 * will be returned to the client.
	 */
	SERVER_OUTGOING_RESPONSE(boolean.class,
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails",
		"org.hl7.fhir.instance.model.api.IBaseResource",
		"ca.uhn.fhir.rest.api.server.ResponseDetails",
		"javax.servlet.http.HttpServletRequest",
		"javax.servlet.http.HttpServletResponse"
	),

	/**
	 * <b>Server Hook:</b>
	 * This method is called after all processing is completed for a request, but only if the
	 * request completes normally (i.e. no exception is thrown).
	 * <p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>
	 * ca.uhn.fhir.rest.api.server.RequestDetails - A bean containing details about the request that is about to be processed, including details such as the
	 * resource type and logical ID (if any) and other FHIR-specific aspects of the request which have been
	 * pulled out of the servlet request.
	 * </li>
	 * <li>
	 * ca.uhn.fhir.rest.server.servlet.ServletRequestDetails - A bean containing details about the request that is about to be processed, including details such as the
	 * resource type and logical ID (if any) and other FHIR-specific aspects of the request which have been
	 * pulled out of the request. This will be null if the server is not deployed to a RestfulServer environment.
	 * </li>
	 * </ul>
	 * </p>
	 * <p>
	 * This method must return <code>void</code>
	 * </p>
	 * <p>
	 * This method should not throw any exceptions. Any exception that is thrown by this
	 * method will be logged, but otherwise not acted upon (i.e. even if a hook method
	 * throws an exception, processing will continue and other interceptors will be
	 * called). Therefore it is considered a bug to throw an exception from hook methods using this
	 * pointcut.
	 * </p>
	 */
	SERVER_PROCESSING_COMPLETED_NORMALLY(
		void.class,
		new ExceptionHandlingSpec()
			.addLogAndSwallow(Throwable.class),
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails"
	),

	/**
	 * Invoked whenever a persisted resource has been modified and is being submitted to the
	 * subscription processing pipeline. This method is called before the resource is placed
	 * on any queues for processing and executes synchronously during the resource modification
	 * operation itself, so it should return quickly.
	 * <p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>ca.uhn.fhir.jpa.subscription.module.ResourceModifiedMessage - Hooks may modify this parameter. This will affect the checking process.</li>
	 * </ul>
	 * </p>
	 * <p>
	 * Hooks may return <code>void</code> or may return a <code>boolean</code>. If the method returns
	 * <code>void</code> or <code>true</code>, processing will continue normally. If the method
	 * returns <code>false</code>, subscription processing will not proceed for the given resource;
	 * </p>
	 */
	SUBSCRIPTION_RESOURCE_MODIFIED(boolean.class, "ca.uhn.fhir.jpa.subscription.module.ResourceModifiedMessage"),


	/**
	 * Invoked any time that a resource is matched by an individual subscription, and
	 * is about to be queued for delivery.
	 * <p>
	 * Hooks may make changes to the delivery payload, or make changes to the
	 * canonical subscription such as adding headers, modifying the channel
	 * endpoint, etc.
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription</li>
	 * <li>ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceDeliveryMessage</li>
	 * <li>ca.uhn.fhir.jpa.subscription.module.matcher.SubscriptionMatchResult</li>
	 * </ul>
	 * <p>
	 * Hooks may return <code>void</code> or may return a <code>boolean</code>. If the method returns
	 * <code>void</code> or <code>true</code>, processing will continue normally. If the method
	 * returns <code>false</code>, delivery will be aborted.
	 * </p>
	 */
	SUBSCRIPTION_RESOURCE_MATCHED(boolean.class, "ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription", "ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceDeliveryMessage", "ca.uhn.fhir.jpa.subscription.module.matcher.SubscriptionMatchResult"),


	/**
	 * Invoked whenever a persisted resource was checked against all active subscriptions, and did not
	 * match any.
	 * <p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>ca.uhn.fhir.jpa.subscription.module.ResourceModifiedMessage - Hooks should not modify this parameter as changes will not have any effect.</li>
	 * </ul>
	 * </p>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	SUBSCRIPTION_RESOURCE_DID_NOT_MATCH_ANY_SUBSCRIPTIONS(void.class, "ca.uhn.fhir.jpa.subscription.module.ResourceModifiedMessage"),

	/**
	 * Invoked immediately before the delivery of a subscription, and right before any channel-specific
	 * hooks are invoked (e.g. {@link #SUBSCRIPTION_BEFORE_REST_HOOK_DELIVERY}.
	 * <p>
	 * Hooks may make changes to the delivery payload, or make changes to the
	 * canonical subscription such as adding headers, modifying the channel
	 * endpoint, etc.
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription</li>
	 * <li>ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceDeliveryMessage</li>
	 * </ul>
	 * <p>
	 * Hooks may return <code>void</code> or may return a <code>boolean</code>. If the method returns
	 * <code>void</code> or <code>true</code>, processing will continue normally. If the method
	 * returns <code>false</code>, processing will be aborted.
	 * </p>
	 */
	SUBSCRIPTION_BEFORE_DELIVERY(boolean.class, "ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription", "ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceDeliveryMessage"),

	/**
	 * Invoked immediately after the delivery of a subscription, and right before any channel-specific
	 * hooks are invoked (e.g. {@link #SUBSCRIPTION_AFTER_REST_HOOK_DELIVERY}.
	 * <p>
	 * Hooks may accept the following parameters:
	 * </p>
	 * <ul>
	 * <li>ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription</li>
	 * <li>ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceDeliveryMessage</li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	SUBSCRIPTION_AFTER_DELIVERY(void.class, "ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription", "ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceDeliveryMessage"),

	/**
	 * Invoked immediately after the attempted delivery of a subscription, if the delivery
	 * failed.
	 * <p>
	 * Hooks may accept the following parameters:
	 * </p>
	 * <ul>
	 * <li>java.lang.Exception - The exception that caused the failure.  Note this could be an exception thrown by a SUBSCRIPTION_BEFORE_DELIVERY or SUBSCRIPTION_AFTER_DELIVERY interceptor</li>
	 * <li>ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceDeliveryMessage - the message that triggered the exception</li>
	 * <li>java.lang.Exception</li>
	 * </ul>
	 * <p>
	 * Hooks may return <code>void</code> or may return a <code>boolean</code>. If the method returns
	 * <code>void</code> or <code>true</code>, processing will continue normally, meaning that
	 * an exception will be thrown by the delivery mechanism. This typically means that the
	 * message will be returned to the processing queue. If the method
	 * returns <code>false</code>, processing will be aborted and no further action will be
	 * taken for the delivery.
	 * </p>
	 */
	SUBSCRIPTION_AFTER_DELIVERY_FAILED(boolean.class, "ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceDeliveryMessage", "java.lang.Exception"),

	/**
	 * Invoked immediately after the delivery of a REST HOOK subscription.
	 * <p>
	 * When this hook is called, all processing is complete so this hook should not
	 * make any changes to the parameters.
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription</li>
	 * <li>ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceDeliveryMessage</li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	SUBSCRIPTION_AFTER_REST_HOOK_DELIVERY(void.class, "ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription", "ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceDeliveryMessage"),

	/**
	 * Invoked immediately before the delivery of a REST HOOK subscription.
	 * <p>
	 * Hooks may make changes to the delivery payload, or make changes to the
	 * canonical subscription such as adding headers, modifying the channel
	 * endpoint, etc.
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription</li>
	 * <li>ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceDeliveryMessage</li>
	 * </ul>
	 * <p>
	 * Hooks may return <code>void</code> or may return a <code>boolean</code>. If the method returns
	 * <code>void</code> or <code>true</code>, processing will continue normally. If the method
	 * returns <code>false</code>, processing will be aborted.
	 * </p>
	 */
	SUBSCRIPTION_BEFORE_REST_HOOK_DELIVERY(boolean.class, "ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription", "ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceDeliveryMessage"),


	/**
	 * Invoked whenever a persisted resource (a resource that has just been stored in the
	 * database via a create/update/patch/etc.) is about to be checked for whether any subscriptions
	 * were triggered as a result of the operation.
	 * <p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>ca.uhn.fhir.jpa.subscription.module.ResourceModifiedMessage - Hooks may modify this parameter. This will affect the checking process.</li>
	 * </ul>
	 * </p>
	 * <p>
	 * Hooks may return <code>void</code> or may return a <code>boolean</code>. If the method returns
	 * <code>void</code> or <code>true</code>, processing will continue normally. If the method
	 * returns <code>false</code>, processing will be aborted.
	 * </p>
	 */
	SUBSCRIPTION_BEFORE_PERSISTED_RESOURCE_CHECKED(boolean.class, "ca.uhn.fhir.jpa.subscription.module.ResourceModifiedMessage"),

	/**
	 * Invoked whenever a persisted resource (a resource that has just been stored in the
	 * database via a create/update/patch/etc.) has been checked for whether any subscriptions
	 * were triggered as a result of the operation.
	 * <p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>ca.uhn.fhir.jpa.subscription.module.ResourceModifiedMessage - This parameter should not be modified as processing is complete when this hook is invoked.</li>
	 * </ul>
	 * </p>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	SUBSCRIPTION_AFTER_PERSISTED_RESOURCE_CHECKED(void.class, "ca.uhn.fhir.jpa.subscription.module.ResourceModifiedMessage"),

	/**
	 * Invoked immediately after an active subscription is "registered". In HAPI FHIR, when
	 * a subscription
	 * <p>
	 * Hooks may make changes to the canonicalized subscription and this will have an effect
	 * on processing across this server. Note however that timing issues may occur, since the
	 * subscription is already technically live by the time this hook is called.
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription</li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	SUBSCRIPTION_AFTER_ACTIVE_SUBSCRIPTION_REGISTERED(void.class, "ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription"),

	/**
	 * Invoked when a resource may be returned to the user, whether as a part of a READ,
	 * a SEARCH, or even as the response to a CREATE/UPDATE, etc.
	 * <p>
	 * This hook is invoked when a resource has been loaded by the storage engine and
	 * is being returned to the HTTP stack for response. This is not a guarantee that the
	 * client will ultimately see it, since filters/headers/etc may affect what
	 * is returned but if a resource is loaded it is likely to be used.
	 * Note also that caching may affect whether this pointcut is invoked.
	 * </p>
	 * <p>
	 * Hooks will have access to the contents of the resource being returned
	 * and may choose to make modifications. These changes will be reflected in
	 * returned resource but have no effect on storage.
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>org.hl7.fhir.instance.model.api.IBaseResource - The resource being returned</li>
	 * <li>
	 * ca.uhn.fhir.rest.api.server.RequestDetails - A bean containing details about the request that is about to be processed, including details such as the
	 * resource type and logical ID (if any) and other FHIR-specific aspects of the request which have been
	 * pulled out of the servlet request. Note that the bean
	 * properties are not all guaranteed to be populated, depending on how early during processing the
	 * exception occurred. <b>Note that this parameter may be null in contexts where the request is not
	 * known, such as while processing searches</b>
	 * </li>
	 * <li>
	 * ca.uhn.fhir.rest.server.servlet.ServletRequestDetails - A bean containing details about the request that is about to be processed, including details such as the
	 * resource type and logical ID (if any) and other FHIR-specific aspects of the request which have been
	 * pulled out of the servlet request. This parameter is identical to the RequestDetails parameter above but will
	 * only be populated when operating in a RestfulServer implementation. It is provided as a convenience.
	 * </li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	STORAGE_PREACCESS_RESOURCE(void.class,
		"org.hl7.fhir.instance.model.api.IBaseResource",
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails"
	),

	/**
	 * Invoked before a resource will be created, immediately before the resource
	 * is persisted to the database.
	 * <p>
	 * Hooks will have access to the contents of the resource being created
	 * and may choose to make modifications to it. These changes will be
	 * reflected in permanent storage.
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>org.hl7.fhir.instance.model.api.IBaseResource</li>
	 * <li>
	 * ca.uhn.fhir.rest.api.server.RequestDetails - A bean containing details about the request that is about to be processed, including details such as the
	 * resource type and logical ID (if any) and other FHIR-specific aspects of the request which have been
	 * pulled out of the servlet request. Note that the bean
	 * properties are not all guaranteed to be populated, depending on how early during processing the
	 * exception occurred.
	 * </li>
	 * <li>
	 * ca.uhn.fhir.rest.server.servlet.ServletRequestDetails - A bean containing details about the request that is about to be processed, including details such as the
	 * resource type and logical ID (if any) and other FHIR-specific aspects of the request which have been
	 * pulled out of the servlet request. This parameter is identical to the RequestDetails parameter above but will
	 * only be populated when operating in a RestfulServer implementation. It is provided as a convenience.
	 * </li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	STORAGE_PRESTORAGE_RESOURCE_CREATED(void.class,
		"org.hl7.fhir.instance.model.api.IBaseResource",
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails"
		),

	/**
	 * Invoked before a resource will be created, immediately before the transaction
	 * is committed (after all validation and other business rules have successfully
	 * completed, and any other database activity is complete.
	 * <p>
	 * Hooks will have access to the contents of the resource being created
	 * but should generally not make any
	 * changes as storage has already occurred. Changes will not be reflected
	 * in storage, but may be reflected in the HTTP response.
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>org.hl7.fhir.instance.model.api.IBaseResource</li>
	 * <li>
	 * ca.uhn.fhir.rest.api.server.RequestDetails - A bean containing details about the request that is about to be processed, including details such as the
	 * resource type and logical ID (if any) and other FHIR-specific aspects of the request which have been
	 * pulled out of the servlet request. Note that the bean
	 * properties are not all guaranteed to be populated, depending on how early during processing the
	 * exception occurred.
	 * </li>
	 * <li>
	 * ca.uhn.fhir.rest.server.servlet.ServletRequestDetails - A bean containing details about the request that is about to be processed, including details such as the
	 * resource type and logical ID (if any) and other FHIR-specific aspects of the request which have been
	 * pulled out of the servlet request. This parameter is identical to the RequestDetails parameter above but will
	 * only be populated when operating in a RestfulServer implementation. It is provided as a convenience.
	 * </li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	STORAGE_PRECOMMIT_RESOURCE_CREATED(void.class,
		"org.hl7.fhir.instance.model.api.IBaseResource",
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails"
		),

	/**
	 * Invoked before a resource will be created
	 * <p>
	 * Hooks will have access to the contents of the resource being deleted
	 * but should not make any changes as storage has already occurred
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>org.hl7.fhir.instance.model.api.IBaseResource - The resource being deleted</li>
	 * <li>
	 * ca.uhn.fhir.rest.api.server.RequestDetails - A bean containing details about the request that is about to be processed, including details such as the
	 * resource type and logical ID (if any) and other FHIR-specific aspects of the request which have been
	 * pulled out of the servlet request. Note that the bean
	 * properties are not all guaranteed to be populated, depending on how early during processing the
	 * exception occurred.
	 * </li>
	 * <li>
	 * ca.uhn.fhir.rest.server.servlet.ServletRequestDetails - A bean containing details about the request that is about to be processed, including details such as the
	 * resource type and logical ID (if any) and other FHIR-specific aspects of the request which have been
	 * pulled out of the servlet request. This parameter is identical to the RequestDetails parameter above but will
	 * only be populated when operating in a RestfulServer implementation. It is provided as a convenience.
	 * </li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	STORAGE_PRECOMMIT_RESOURCE_DELETED(void.class,
		"org.hl7.fhir.instance.model.api.IBaseResource",
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails"
		),


	/**
	 * Invoked before a resource will be updated, immediately before the transaction
	 * is committed (after all validation and other business rules have successfully
	 * completed, and any other database activity is complete.
	 * <p>
	 * Hooks will have access to the contents of the resource being updated
	 * (both the previous and new contents) but should generally not make any
	 * changes as storage has already occurred. Changes will not be reflected
	 * in storage, but may be reflected in the HTTP response.
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>org.hl7.fhir.instance.model.api.IBaseResource - The previous contents of the resource</li>
	 * <li>org.hl7.fhir.instance.model.api.IBaseResource - The proposed new new contents of the resource</li>
	 * <li>
	 * ca.uhn.fhir.rest.api.server.RequestDetails - A bean containing details about the request that is about to be processed, including details such as the
	 * resource type and logical ID (if any) and other FHIR-specific aspects of the request which have been
	 * pulled out of the servlet request. Note that the bean
	 * properties are not all guaranteed to be populated, depending on how early during processing the
	 * exception occurred.
	 * </li>
	 * <li>
	 * ca.uhn.fhir.rest.server.servlet.ServletRequestDetails - A bean containing details about the request that is about to be processed, including details such as the
	 * resource type and logical ID (if any) and other FHIR-specific aspects of the request which have been
	 * pulled out of the servlet request. This parameter is identical to the RequestDetails parameter above but will
	 * only be populated when operating in a RestfulServer implementation. It is provided as a convenience.
	 * </li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	STORAGE_PRECOMMIT_RESOURCE_UPDATED(void.class,
		"org.hl7.fhir.instance.model.api.IBaseResource",
		"org.hl7.fhir.instance.model.api.IBaseResource",
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails"
		),

	/**
	 * Invoked before a resource will be updated, immediately before the resource
	 * is persisted to the database.
	 * <p>
	 * Hooks will have access to the contents of the resource being updated
	 * (both the previous and new contents) and may choose to make modifications
	 * to the new contents of the resource. These changes will be reflected in
	 * permanent storage.
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>org.hl7.fhir.instance.model.api.IBaseResource - The previous contents of the resource being updated</li>
	 * <li>org.hl7.fhir.instance.model.api.IBaseResource - The new contents of the resource being updated</li>
	 * <li>
	 * ca.uhn.fhir.rest.api.server.RequestDetails - A bean containing details about the request that is about to be processed, including details such as the
	 * resource type and logical ID (if any) and other FHIR-specific aspects of the request which have been
	 * pulled out of the servlet request. Note that the bean
	 * properties are not all guaranteed to be populated, depending on how early during processing the
	 * exception occurred.
	 * </li>
	 * <li>
	 * ca.uhn.fhir.rest.server.servlet.ServletRequestDetails - A bean containing details about the request that is about to be processed, including details such as the
	 * resource type and logical ID (if any) and other FHIR-specific aspects of the request which have been
	 * pulled out of the servlet request. This parameter is identical to the RequestDetails parameter above but will
	 * only be populated when operating in a RestfulServer implementation. It is provided as a convenience.
	 * </li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	STORAGE_PRESTORAGE_RESOURCE_UPDATED(void.class,
		"org.hl7.fhir.instance.model.api.IBaseResource",
		"org.hl7.fhir.instance.model.api.IBaseResource",
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails"
		),

	/**
	 * Invoked before a resource will be created, immediately before the resource
	 * is persisted to the database.
	 * <p>
	 * Hooks will have access to the contents of the resource being created
	 * and may choose to make modifications to it. These changes will be
	 * reflected in permanent storage.
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>org.hl7.fhir.instance.model.api.IBaseResource - The resource being deleted</li>
	 * <li>
	 * ca.uhn.fhir.rest.api.server.RequestDetails - A bean containing details about the request that is about to be processed, including details such as the
	 * resource type and logical ID (if any) and other FHIR-specific aspects of the request which have been
	 * pulled out of the servlet request. Note that the bean
	 * properties are not all guaranteed to be populated, depending on how early during processing the
	 * exception occurred.
	 * </li>
	 * <li>
	 * ca.uhn.fhir.rest.server.servlet.ServletRequestDetails - A bean containing details about the request that is about to be processed, including details such as the
	 * resource type and logical ID (if any) and other FHIR-specific aspects of the request which have been
	 * pulled out of the servlet request. This parameter is identical to the RequestDetails parameter above but will
	 * only be populated when operating in a RestfulServer implementation. It is provided as a convenience.
	 * </li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	STORAGE_PRESTORAGE_RESOURCE_DELETED(void.class,
		"org.hl7.fhir.instance.model.api.IBaseResource",
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails"
		),

	/**
	 * Note that this is a performance tracing hook. Use with caution in production
	 * systems, since calling it may (or may not) carry a cost.
	 * <p>
	 * This hook is invoked when any informational or warning messages generated by the
	 * SearchCoordinator are created. It is typically used to provide logging
	 * or capture details related to a specific request.
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>
	 * ca.uhn.fhir.jpa.model.search.StorageProcessingMessage - Contains the message
	 * </li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	STORAGE_PROCESSING_MESSAGE(void.class,
		"ca.uhn.fhir.jpa.model.search.StorageProcessingMessage"
		),

	/**
	 * Note that this is a performance tracing hook. Use with caution in production
	 * systems, since calling it may (or may not) carry a cost.
	 * <p>
	 * This hook is invoked when a search has returned the very first result
	 * from the database. The timing on this call can be a good indicator of how
	 * performant a query is in general.
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>
	 * ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails - Contains details about the search being
	 * performed. Hooks should not modify this object.
	 * </li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	JPA_PERFTRACE_SEARCH_FIRST_RESULT_LOADED(void.class, "ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails"),

	/**
	 * Note that this is a performance tracing hook. Use with caution in production
	 * systems, since calling it may (or may not) carry a cost.
	 * <p>
	 * This hook is invoked when an individual search query SQL SELECT statement
	 * has completed and no more results are available from that query. Note that this
	 * doesn't necessarily mean that no more matching results exist in the database,
	 * since HAPI FHIR JPA batch loads results in to the query cache in chunks in order
	 * to provide predicable results without overloading memory or the database.
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>
	 * ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails - Contains details about the search being
	 * performed. Hooks should not modify this object.
	 * </li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	JPA_PERFTRACE_SEARCH_SELECT_COMPLETE(void.class, "ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails"),

	/**
	 * Note that this is a performance tracing hook. Use with caution in production
	 * systems, since calling it may (or may not) carry a cost.
	 * <p>
	 * This hook is invoked when a search has failed for any reason. When this pointcut
	 * is invoked, the search has completed unsuccessfully and will not be continued.
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>
	 * ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails - Contains details about the search being
	 * performed. Hooks should not modify this object.
	 * </li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	JPA_PERFTRACE_SEARCH_FAILED(void.class, "ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails"),

	/**
	 * Note that this is a performance tracing hook. Use with caution in production
	 * systems, since calling it may (or may not) carry a cost.
	 * <p>
	 * This hook is invoked when a search has failed for any reason. When this pointcut
	 * is invoked, a pass in the Search Coordinator has completed successfully, but
	 * not all possible resources have been loaded yet so a future paging request
	 * may trigger a new task that will load further resources.
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>
	 * ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails - Contains details about the search being
	 * performed. Hooks should not modify this object.
	 * </li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	JPA_PERFTRACE_SEARCH_PASS_COMPLETE(void.class, "ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails"),

	/**
	 * Note that this is a performance tracing hook. Use with caution in production
	 * systems, since calling it may (or may not) carry a cost.
	 * <p>
	 * This hook is invoked when a search has failed for any reason. When this pointcut
	 * is invoked, a pass in the Search Coordinator has completed successfully, and all
	 * possible results have been fetched and loaded into the query cache.
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>
	 * ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails - Contains details about the search being
	 * performed. Hooks should not modify this object.
	 * </li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	JPA_PERFTRACE_SEARCH_COMPLETE(void.class, "ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails"),


	/**
	 * This pointcut is used only for unit tests. Do not use in production code as it may be changed or
	 * removed at any time.
	 */
	TEST_RB(
		boolean.class,
		new ExceptionHandlingSpec().addLogAndSwallow(IllegalStateException.class),
		String.class.getName(),
		String.class.getName()),

	/**
	 * This pointcut is used only for unit tests. Do not use in production code as it may be changed or
	 * removed at any time.
	 */
	TEST_RO(BaseServerResponseException.class, String.class.getName(), String.class.getName());

	private final List<String> myParameterTypes;
	private final Class<?> myReturnType;
	private final ExceptionHandlingSpec myExceptionHandlingSpec;

	Pointcut(@Nonnull Class<?> theReturnType, String... theParameterTypes) {
		this(theReturnType, new ExceptionHandlingSpec(), theParameterTypes);
	}

	Pointcut(@Nonnull Class<?> theReturnType, @Nonnull ExceptionHandlingSpec theExceptionHandlingSpec, String... theParameterTypes) {
		myReturnType = theReturnType;
		myExceptionHandlingSpec = theExceptionHandlingSpec;
		myParameterTypes = Collections.unmodifiableList(Arrays.asList(theParameterTypes));
	}

	public boolean isShouldLogAndSwallowException(@Nonnull Throwable theException) {
		for (Class<? extends Throwable> next : myExceptionHandlingSpec.myTypesToLogAndSwallow) {
			if (next.isAssignableFrom(theException.getClass())) {
				return true;
			}
		}
		return false;
	}

	@Nonnull
	public Class<?> getReturnType() {
		return myReturnType;
	}

	@Nonnull
	public List<String> getParameterTypes() {
		return myParameterTypes;
	}

	private static class ExceptionHandlingSpec {

		private final Set<Class<? extends Throwable>> myTypesToLogAndSwallow = new HashSet<>();

		ExceptionHandlingSpec addLogAndSwallow(@Nonnull Class<? extends Throwable> theType) {
			myTypesToLogAndSwallow.add(theType);
			return this;
		}

	}

}
