package ca.uhn.fhir.interceptor.api;

/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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
import ca.uhn.fhir.validation.ValidationResult;
import org.hl7.fhir.instance.model.api.IBaseConformance;

import javax.annotation.Nonnull;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
 * <li>VALIDATION_xxx: Hooks on the HAPI FHIR Validation framework</li>
 * <li>JPA_PERFTRACE_xxx: Performance tracing hooks on the JPA server</li>
 * </ul>
 * </p>
 */
public enum Pointcut implements IPointcut {

	/**
	 * <b>Interceptor Framework Hook:</b>
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
	 * <li>
	 *    ca.uhn.fhir.rest.client.api.IRestfulClient - The client object making the request
	 * </li>
	 * </ul>
	 * </p>
	 * Hook methods must return <code>void</code>.
	 */
	CLIENT_REQUEST(void.class,
		"ca.uhn.fhir.rest.client.api.IHttpRequest",
		"ca.uhn.fhir.rest.client.api.IRestfulClient"
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
	 * </li>
	 * <li>
	 * ca.uhn.fhir.rest.client.api.IHttpResponse - The details of the response
	 * </li>
	 * <li>
	 *    ca.uhn.fhir.rest.client.api.IRestfulClient - The client object making the request
	 * </li>
	 * </ul>
	 * </p>
	 * Hook methods must return <code>void</code>.
	 */
	CLIENT_RESPONSE(void.class,
		"ca.uhn.fhir.rest.client.api.IHttpRequest",
		"ca.uhn.fhir.rest.client.api.IHttpResponse",
		"ca.uhn.fhir.rest.client.api.IRestfulClient"
	),

	/**
	 * <b>Server Hook:</b>
	 * This hook is called when a server CapabilityStatement is generated for returning to a client.
	 * <p>
	 * This pointcut will not necessarily be invoked for every client request to the `/metadata` endpoint.
	 * If caching of the generated CapabilityStatement is enabled, a new CapabilityStatement will be
	 * generated periodically and this pointcut will be invoked at that time.
	 * </p>
	 * <p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>
	 * org.hl7.fhir.instance.model.api.IBaseConformance - The <code>CapabilityStatement</code> resource that will
	 * be returned to the client by the server. Interceptors may make changes to this resource. The parameter
	 * must be of type <code>IBaseConformance</code>, so it is the responsibility of the interceptor hook method
	 * code to cast to the appropriate version.
	 * </li>
	 * <li>
	 * ca.uhn.fhir.rest.api.server.RequestDetails - A bean containing details about the request that is about to
	 * be processed
	 * </li>
	 * <li>
	 * ca.uhn.fhir.rest.server.servlet.ServletRequestDetails - A bean containing details about the request that
	 * is about to be processed. This parameter is identical to the RequestDetails parameter above but will only
	 * be populated when operating in a RestfulServer implementation. It is provided as a convenience.
	 * </li>
	 * </ul>
	 * </p>
	 * Hook methods may an instance of a new <code>CapabilityStatement</code> resource which will replace the
	 * one that was supplied to the interceptor, or <code>void</code> to use the original one. If the interceptor
	 * chooses to modify the <code>CapabilityStatement</code> that was supplied to the interceptor, it is fine
	 * for your hook method to return <code>void</code> or <code>null</code>.
	 */
	SERVER_CAPABILITY_STATEMENT_GENERATED(IBaseConformance.class,
		"org.hl7.fhir.instance.model.api.IBaseConformance",
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails"
	),

	/**
	 * <b>Server Hook:</b>
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
	 * <b>Server Hook:</b>
	 * This hook is invoked upon any exception being thrown within the server's request processing code. This includes
	 * any exceptions thrown within resource provider methods (e.g. {@link Search} and {@link Read} methods) as well as
	 * any runtime exceptions thrown by the server itself. This also includes any {@link AuthenticationException}
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
	 * This method is immediately before the handling method is selected. Interceptors may make changes
	 * to the request that can influence which handler will ultimately be called.
	 * <p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>
	 * ca.uhn.fhir.rest.api.server.RequestDetails - A bean containing details about the request that is about to be processed, including details such as the
	 * resource type and logical ID (if any) and other FHIR-specific aspects of the request which have been
	 * pulled out of the servlet request.
	 * Note that the bean properties are not all guaranteed to be populated at the time this hook is called.
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
	 *
	 * @since 5.4.0
	 */
	SERVER_INCOMING_REQUEST_PRE_HANDLER_SELECTED(boolean.class,
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails",
		"javax.servlet.http.HttpServletRequest",
		"javax.servlet.http.HttpServletResponse"
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
	 * after the server has begun preparing the response to the incoming client request.
	 * As such, it is not able to supply a response to the incoming request in the way that
	 * SERVER_INCOMING_REQUEST_PRE_PROCESSED and
	 * {@link #SERVER_INCOMING_REQUEST_POST_PROCESSED}
	 * are.
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
	 * ca.uhn.fhir.rest.api.RestOperationTypeEnum - The type of operation that the FHIR server has determined that the client is trying to invoke
	 * </li>
	 * <li>
	 * ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails - This parameter is provided for legacy reasons only and will be removed in the future. Do not use.
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
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails",
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
	 * This method is called when a stream writer is generated that will be used to stream a non-binary response to
	 * a client. Hooks may return a wrapped writer which adds additional functionality as needed.
	 *
	 * <p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>
	 * java.io.Writer - The response writing Writer. Typically a hook will wrap this writer and layer additional functionality
	 * into the wrapping writer.
	 * </li>
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
	 * </ul>
	 * </p>
	 * <p>
	 * Hook methods should return a {@link Writer} instance that will be used to stream the response. Hook methods
	 * should not throw any exception.
	 * </p>
	 *
	 * @since 5.0.0
	 */
	SERVER_OUTGOING_WRITER_CREATED(Writer.class,
		"java.io.Writer",
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails"
	),


	/**
	 * <b>Server Hook:</b>
	 * This method is called after the server implementation method has been called, but before any attempt
	 * to stream the response back to the client, specifically for GraphQL requests (as these do not fit
	 * cleanly into the model provided by {@link #SERVER_OUTGOING_RESPONSE}).
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
	 * java.lang.String - The GraphQL query
	 * </li>
	 * <li>
	 * java.lang.String - The GraphQL response
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
	SERVER_OUTGOING_GRAPHQL_RESPONSE(boolean.class,
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails",
		"java.lang.String",
		"java.lang.String",
		"javax.servlet.http.HttpServletRequest",
		"javax.servlet.http.HttpServletResponse"
	),


	/**
	 * <b>Server Hook:</b>
	 * This method is called when an OperationOutcome is being returned in response to a failure.
	 * Hook methods may use this hook to modify the OperationOutcome being returned.
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
	 * org.hl7.fhir.instance.model.api.IBaseOperationOutcome - The OperationOutcome resource that will be
	 * returned.
	 * </ul>
	 * <p>
	 * Hook methods must return <code>void</code>
	 * </p>
	 */
	SERVER_OUTGOING_FAILURE_OPERATIONOUTCOME(
		void.class,
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails",
		"org.hl7.fhir.instance.model.api.IBaseOperationOutcome"
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
	 * <b>Server Hook:</b>
	 * This method is called after all processing is completed for a request, regardless of whether
	 * the request completed successfully or not. It is called after {@link #SERVER_PROCESSING_COMPLETED_NORMALLY}
	 * in the case of successful operations.
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
	SERVER_PROCESSING_COMPLETED(
		void.class,
		new ExceptionHandlingSpec()
			.addLogAndSwallow(Throwable.class),
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails"
	),

	/**
	 * <b>Subscription Hook:</b>
	 * Invoked whenever a persisted resource has been modified and is being submitted to the
	 * subscription processing pipeline. This method is called before the resource is placed
	 * on any queues for processing and executes synchronously during the resource modification
	 * operation itself, so it should return quickly.
	 * <p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage - Hooks may modify this parameter. This will affect the checking process.</li>
	 * </ul>
	 * </p>
	 * <p>
	 * Hooks may return <code>void</code> or may return a <code>boolean</code>. If the method returns
	 * <code>void</code> or <code>true</code>, processing will continue normally. If the method
	 * returns <code>false</code>, subscription processing will not proceed for the given resource;
	 * </p>
	 */
	SUBSCRIPTION_RESOURCE_MODIFIED(boolean.class, "ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage"),


	/**
	 * <b>Subscription Hook:</b>
	 * Invoked any time that a resource is matched by an individual subscription, and
	 * is about to be queued for delivery.
	 * <p>
	 * Hooks may make changes to the delivery payload, or make changes to the
	 * canonical subscription such as adding headers, modifying the channel
	 * endpoint, etc.
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription</li>
	 * <li>ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage</li>
	 * <li>ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult</li>
	 * </ul>
	 * <p>
	 * Hooks may return <code>void</code> or may return a <code>boolean</code>. If the method returns
	 * <code>void</code> or <code>true</code>, processing will continue normally. If the method
	 * returns <code>false</code>, delivery will be aborted.
	 * </p>
	 */
	SUBSCRIPTION_RESOURCE_MATCHED(boolean.class, "ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription", "ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage", "ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult"),

	/**
	 * <b>Subscription Hook:</b>
	 * Invoked whenever a persisted resource was checked against all active subscriptions, and did not
	 * match any.
	 * <p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage - Hooks should not modify this parameter as changes will not have any effect.</li>
	 * </ul>
	 * </p>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	SUBSCRIPTION_RESOURCE_DID_NOT_MATCH_ANY_SUBSCRIPTIONS(void.class, "ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage"),

	/**
	 * <b>Subscription Hook:</b>
	 * Invoked immediately before the delivery of a subscription, and right before any channel-specific
	 * hooks are invoked (e.g. {@link #SUBSCRIPTION_BEFORE_REST_HOOK_DELIVERY}.
	 * <p>
	 * Hooks may make changes to the delivery payload, or make changes to the
	 * canonical subscription such as adding headers, modifying the channel
	 * endpoint, etc.
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription</li>
	 * <li>ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage</li>
	 * </ul>
	 * <p>
	 * Hooks may return <code>void</code> or may return a <code>boolean</code>. If the method returns
	 * <code>void</code> or <code>true</code>, processing will continue normally. If the method
	 * returns <code>false</code>, processing will be aborted.
	 * </p>
	 */
	SUBSCRIPTION_BEFORE_DELIVERY(boolean.class, "ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription", "ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage"),

	/**
	 * <b>Subscription Hook:</b>
	 * Invoked immediately after the delivery of a subscription, and right before any channel-specific
	 * hooks are invoked (e.g. {@link #SUBSCRIPTION_AFTER_REST_HOOK_DELIVERY}.
	 * <p>
	 * Hooks may accept the following parameters:
	 * </p>
	 * <ul>
	 * <li>ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription</li>
	 * <li>ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage</li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	SUBSCRIPTION_AFTER_DELIVERY(void.class, "ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription", "ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage"),


	/**
	 * <b>Subscription Hook:</b>
	 * Invoked immediately after the attempted delivery of a subscription, if the delivery
	 * failed.
	 * <p>
	 * Hooks may accept the following parameters:
	 * </p>
	 * <ul>
	 * <li>java.lang.Exception - The exception that caused the failure.  Note this could be an exception thrown by a SUBSCRIPTION_BEFORE_DELIVERY or SUBSCRIPTION_AFTER_DELIVERY interceptor</li>
	 * <li>ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage - the message that triggered the exception</li>
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
	SUBSCRIPTION_AFTER_DELIVERY_FAILED(boolean.class, "ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage", "java.lang.Exception"),

	/**
	 * <b>Subscription Hook:</b>
	 * Invoked immediately after the delivery of a REST HOOK subscription.
	 * <p>
	 * When this hook is called, all processing is complete so this hook should not
	 * make any changes to the parameters.
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription</li>
	 * <li>ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage</li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	SUBSCRIPTION_AFTER_REST_HOOK_DELIVERY(void.class, "ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription", "ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage"),

	/**
	 * <b>Subscription Hook:</b>
	 * Invoked immediately before the delivery of a REST HOOK subscription.
	 * <p>
	 * Hooks may make changes to the delivery payload, or make changes to the
	 * canonical subscription such as adding headers, modifying the channel
	 * endpoint, etc.
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription</li>
	 * <li>ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage</li>
	 * </ul>
	 * <p>
	 * Hooks may return <code>void</code> or may return a <code>boolean</code>. If the method returns
	 * <code>void</code> or <code>true</code>, processing will continue normally. If the method
	 * returns <code>false</code>, processing will be aborted.
	 * </p>
	 */
	SUBSCRIPTION_BEFORE_REST_HOOK_DELIVERY(boolean.class, "ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription", "ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage"),

	/**
	 * <b>Subscription Hook:</b>
	 * Invoked immediately after the delivery of MESSAGE subscription.
	 * <p>
	 * When this hook is called, all processing is complete so this hook should not
	 * make any changes to the parameters.
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription</li>
	 * <li>ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage</li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	SUBSCRIPTION_AFTER_MESSAGE_DELIVERY(void.class, "ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription", "ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage"),

	/**
	 * <b>Subscription Hook:</b>
	 * Invoked immediately before the delivery of a MESSAGE subscription.
	 * <p>
	 * Hooks may make changes to the delivery payload, or make changes to the
	 * canonical subscription such as adding headers, modifying the channel
	 * endpoint, etc.
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription</li>
	 * <li>ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage</li>
	 * </ul>
	 * <p>
	 * Hooks may return <code>void</code> or may return a <code>boolean</code>. If the method returns
	 * <code>void</code> or <code>true</code>, processing will continue normally. If the method
	 * returns <code>false</code>, processing will be aborted.
	 * </p>
	 */
	SUBSCRIPTION_BEFORE_MESSAGE_DELIVERY(boolean.class, "ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription", "ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage"),


	/**
	 * <b>Subscription Hook:</b>
	 * Invoked whenever a persisted resource (a resource that has just been stored in the
	 * database via a create/update/patch/etc.) is about to be checked for whether any subscriptions
	 * were triggered as a result of the operation.
	 * <p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage - Hooks may modify this parameter. This will affect the checking process.</li>
	 * </ul>
	 * </p>
	 * <p>
	 * Hooks may return <code>void</code> or may return a <code>boolean</code>. If the method returns
	 * <code>void</code> or <code>true</code>, processing will continue normally. If the method
	 * returns <code>false</code>, processing will be aborted.
	 * </p>
	 */
	SUBSCRIPTION_BEFORE_PERSISTED_RESOURCE_CHECKED(boolean.class, "ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage"),


	/**
	 * <b>Subscription Hook:</b>
	 * Invoked whenever a persisted resource (a resource that has just been stored in the
	 * database via a create/update/patch/etc.) has been checked for whether any subscriptions
	 * were triggered as a result of the operation.
	 * <p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage - This parameter should not be modified as processing is complete when this hook is invoked.</li>
	 * </ul>
	 * </p>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	SUBSCRIPTION_AFTER_PERSISTED_RESOURCE_CHECKED(void.class, "ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage"),


	/**
	 * <b>Subscription Hook:</b>
	 * Invoked immediately after an active subscription is "registered". In HAPI FHIR, when
	 * a subscription
	 * <p>
	 * Hooks may make changes to the canonicalized subscription and this will have an effect
	 * on processing across this server. Note however that timing issues may occur, since the
	 * subscription is already technically live by the time this hook is called.
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription</li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	SUBSCRIPTION_AFTER_ACTIVE_SUBSCRIPTION_REGISTERED(void.class, "ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription"),

	/**
	 * <b>Subscription Hook:</b>
	 * Invoked immediately after an active subscription is "registered". In HAPI FHIR, when
	 * a subscription
	 * <p>
	 * Hooks may make changes to the canonicalized subscription and this will have an effect
	 * on processing across this server. Note however that timing issues may occur, since the
	 * subscription is already technically live by the time this hook is called.
	 * </p>
	 * No parameters are currently supported.
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	SUBSCRIPTION_AFTER_ACTIVE_SUBSCRIPTION_UNREGISTERED(void.class),

	/**
	 * <b>Storage Hook:</b>
	 * Invoked when a resource is being deleted in a cascaded delete. This means that
	 * some other resource is being deleted, but per use request or other
	 * policy, the given resource (the one supplied as a parameter to this hook)
	 * is also being deleted.
	 * <p>
	 * Hooks may accept the following parameters:
	 * </p>
	 * <ul>
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
	 * <li>
	 * ca.uhn.fhir.jpa.util.DeleteConflictList - Contains the details about the delete conflicts that are
	 * being resolved via deletion. The source resource is the resource that will be deleted, and
	 * is a cascade because the target resource is already being deleted.
	 * </li>
	 * <li>
	 * org.hl7.fhir.instance.model.api.IBaseResource - The actual resource that is about to be deleted via a cascading delete
	 * </li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>. They may choose to throw an exception however, in
	 * which case the delete should be rolled back.
	 * </p>
	 */
	STORAGE_CASCADE_DELETE(
		void.class,
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails",
		"ca.uhn.fhir.jpa.api.model.DeleteConflictList",
		"org.hl7.fhir.instance.model.api.IBaseResource"
	),


	/**
	 * <b>Storage Hook:</b>
	 * Invoked when a Bulk Export job is being kicked off. Hook methods may modify
	 * the request, or raise an exception to prevent it from being initiated.
	 * <p>
	 * Hooks may accept the following parameters:
	 * </p>
	 * <ul>
	 * <li>
	 * ca.uhn.fhir.jpa.bulk.export.api.BulkDataExportOptions - The details of the job being kicked off
	 * </li>
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
	 * Hooks should return <code>void</code>, and can throw exceptions.
	 * </p>
	 */
	STORAGE_INITIATE_BULK_EXPORT(
		void.class,
		"ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions",
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails"
	),
	/**
	 * <b>Storage Hook:</b>
	 * Invoked when a set of resources are about to be deleted and expunged via url like http://localhost/Patient?active=false&_expunge=true
	 * <p>
	 * Hooks may accept the following parameters:
	 * </p>
	 * <ul>
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
	 * <li>
	 * java.lang.String - Contains the url used to delete and expunge the resources
	 * </li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>. They may choose to throw an exception however, in
	 * which case the delete expunge will not occur.
	 * </p>
	 */

	STORAGE_PRE_DELETE_EXPUNGE(
		void.class,
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails",
		"java.lang.String"
	),

	/**
	 * <b>Storage Hook:</b>
	 * Invoked when a batch of resource pids are about to be deleted and expunged via url like http://localhost/Patient?active=false&_expunge=true
	 * <p>
	 * Hooks may accept the following parameters:
	 * </p>
	 * <ul>
	 * <li>
	 * java.lang.String - the name of the resource type being deleted
	 * </li>
	 * <li>
	 * java.util.List - the list of Long pids of the resources about to be deleted
	 * </li>
	 * <li>
	 * java.util.concurrent.atomic.AtomicLong - holds a running tally of all entities deleted so far.
	 * If the pointcut callback deletes any entities, then this parameter should be incremented by the total number
	 * of additional entities deleted.
	 * </li>
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
	 * <li>
	 * java.lang.String - Contains the url used to delete and expunge the resources
	 * </li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>. They may choose to throw an exception however, in
	 * which case the delete expunge will not occur.
	 * </p>
	 */

	STORAGE_PRE_DELETE_EXPUNGE_PID_LIST(
		void.class,
		"java.lang.String",
		"java.util.List",
		"java.util.concurrent.atomic.AtomicLong",
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails"
	),

	/**
	 * <b>Storage Hook:</b>
	 * Invoked when one or more resources may be returned to the user, whether as a part of a READ,
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
	 * <li>
	 * ca.uhn.fhir.rest.api.server.IPreResourceAccessDetails - Contains details about the
	 * specific resources being returned.
	 * </li>
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
	STORAGE_PREACCESS_RESOURCES(void.class,
		"ca.uhn.fhir.rest.api.server.IPreResourceAccessDetails",
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails"
	),

	/**
	 * <b>Storage Hook:</b>
	 * Invoked when the storage engine is about to check for the existence of a pre-cached search
	 * whose results match the given search parameters.
	 * <p>
	 * Hooks may accept the following parameters:
	 * </p>
	 * <ul>
	 * <li>
	 * ca.uhn.fhir.jpa.searchparam.SearchParameterMap - Contains the details of the search being checked
	 * </li>
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
	 * Hooks may return <code>boolean</code>. If the hook method returns
	 * <code>false</code>, the server will not attempt to check for a cached
	 * search no matter what.
	 * </p>
	 */
	STORAGE_PRECHECK_FOR_CACHED_SEARCH(boolean.class,
		"ca.uhn.fhir.jpa.searchparam.SearchParameterMap",
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails"
	),

	/**
	 * <b>Storage Hook:</b>
	 * Invoked when a search is starting, prior to creating a record for the search.
	 * <p>
	 * Hooks may accept the following parameters:
	 * </p>
	 * <ul>
	 * <li>
	 * ca.uhn.fhir.rest.server.util.ICachedSearchDetails - Contains the details of the search that
	 * is being created and initialized
	 * </li>
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
	 * <li>
	 * ca.uhn.fhir.jpa.searchparam.SearchParameterMap - Contains the details of the search being checked. This can be modified.
	 * </li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	STORAGE_PRESEARCH_REGISTERED(void.class,
		"ca.uhn.fhir.rest.server.util.ICachedSearchDetails",
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails",
		"ca.uhn.fhir.jpa.searchparam.SearchParameterMap"
	),

	/**
	 * <b>Storage Hook:</b>
	 * Invoked when one or more resources may be returned to the user, whether as a part of a READ,
	 * a SEARCH, or even as the response to a CREATE/UPDATE, etc.
	 * <p>
	 * This hook is invoked when a resource has been loaded by the storage engine and
	 * is being returned to the HTTP stack for response.
	 * This is not a guarantee that the
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
	 * <li>
	 * ca.uhn.fhir.rest.api.server.IPreResourceShowDetails - Contains the resources that
	 * will be shown to the user. This object may be manipulated in order to modify
	 * the actual resources being shown to the user (e.g. for masking)
	 * </li>
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
	STORAGE_PRESHOW_RESOURCES(void.class,
		"ca.uhn.fhir.rest.api.server.IPreResourceShowDetails",
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails"
	),

	/**
	 * <b>Storage Hook:</b>
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
	 * <li>
	 * ca.uhn.fhir.rest.api.server.storage.TransactionDetails - The outer transaction details object (since 5.0.0)
	 * </li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	STORAGE_PRESTORAGE_RESOURCE_CREATED(void.class,
		"org.hl7.fhir.instance.model.api.IBaseResource",
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails",
		"ca.uhn.fhir.rest.api.server.storage.TransactionDetails"
	),

	/**
	 * <b>Storage Hook:</b>
	 * Invoked before client-assigned id is created.
	 * <p>
	 * Hooks will have access to the contents of the resource being created
	 * so that client-assigned ids can be allowed/denied. These changes will
	 * be reflected in permanent storage.
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
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	STORAGE_PRESTORAGE_CLIENT_ASSIGNED_ID(void.class,
		"org.hl7.fhir.instance.model.api.IBaseResource",
		"ca.uhn.fhir.rest.api.server.RequestDetails"
	),

	/**
	 * <b>Storage Hook:</b>
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
	 * <li>
	 * ca.uhn.fhir.rest.api.server.storage.TransactionDetails - The outer transaction details object (since 5.0.0)
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
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails",
		"ca.uhn.fhir.rest.api.server.storage.TransactionDetails"
	),

	/**
	 * <b>Storage Hook:</b>
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
	 * <li>
	 * ca.uhn.fhir.rest.api.server.storage.TransactionDetails - The outer transaction details object (since 5.0.0)
	 * </li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	STORAGE_PRESTORAGE_RESOURCE_DELETED(void.class,
		"org.hl7.fhir.instance.model.api.IBaseResource",
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails",
		"ca.uhn.fhir.rest.api.server.storage.TransactionDetails"
	),


	/**
	 * <b>Storage Hook:</b>
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
	 * <li>
	 * ca.uhn.fhir.rest.api.server.storage.TransactionDetails - The outer transaction details object (since 5.0.0)
	 * </li>
	 * <li>
	 * Boolean - Whether this pointcut invocation was deferred or not(since 5.4.0)
	 * </li>
	 * <li>
	 * ca.uhn.fhir.rest.api.InterceptorInvocationTimingEnum - The timing at which the invocation of the interceptor took place. Options are ACTIVE and DEFERRED.
	 * </li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	STORAGE_PRECOMMIT_RESOURCE_CREATED(void.class,
		"org.hl7.fhir.instance.model.api.IBaseResource",
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails",
		"ca.uhn.fhir.rest.api.server.storage.TransactionDetails",
		"ca.uhn.fhir.rest.api.InterceptorInvocationTimingEnum"
	),

	/**
	 * <b>Storage Hook:</b>
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
	 * <li>org.hl7.fhir.instance.model.api.IBaseResource - The proposed new contents of the resource</li>
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
	 * ca.uhn.fhir.rest.api.server.storage.TransactionDetails - The outer transaction details object (since 5.0.0)
	 * </li>
	 * <li>
	 * ca.uhn.fhir.rest.api.InterceptorInvocationTimingEnum - The timing at which the invocation of the interceptor took place. Options are ACTIVE and DEFERRED.
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
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails",
		"ca.uhn.fhir.rest.api.server.storage.TransactionDetails",
		"ca.uhn.fhir.rest.api.InterceptorInvocationTimingEnum"
	),


	/**
	 * <b>Storage Hook:</b>
	 * Invoked before a resource will be deleted
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
	 * <li>
	 * ca.uhn.fhir.rest.api.server.storage.TransactionDetails - The outer transaction details object (since 5.0.0)
	 * </li>
	 * <li>
	 * ca.uhn.fhir.rest.api.InterceptorInvocationTimingEnum - The timing at which the invocation of the interceptor took place. Options are ACTIVE and DEFERRED.
	 * </li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	STORAGE_PRECOMMIT_RESOURCE_DELETED(void.class,
		"org.hl7.fhir.instance.model.api.IBaseResource",
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails",
		"ca.uhn.fhir.rest.api.server.storage.TransactionDetails",
		"ca.uhn.fhir.rest.api.InterceptorInvocationTimingEnum"
	),

	/**
	 * <b>Storage Hook:</b>
	 * Invoked after all entries in a transaction bundle have been executed
	 * <p>
	 * Hooks will have access to the original bundle, as well as all the deferred interceptor broadcasts related to the
	 * processing of the transaction bundle
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
	 * <li>
	 * ca.uhn.fhir.rest.api.server.storage.TransactionDetails - The outer transaction details object (since 5.0.0)
	 * </li>
	 * <li>
	 * ca.uhn.fhir.rest.api.server.storage.DeferredInterceptorBroadcasts- A collection of pointcut invocations and their parameters which were deferred.
	 * </li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	STORAGE_TRANSACTION_PROCESSED(void.class,
		"org.hl7.fhir.instance.model.api.IBaseBundle",
		"ca.uhn.fhir.rest.api.server.storage.DeferredInterceptorBroadcasts",
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails",
		"ca.uhn.fhir.rest.api.server.storage.TransactionDetails"
	),


	/**
	 * <b>Storage Hook:</b>
	 * Invoked during a FHIR transaction, immediately before processing all write operations (i.e. immediately
	 * before a database transaction will be opened)
	 * <p>
	 * Hooks may accept the following parameters:
	 * </p>
	 * <ul>
	 * <li>
	 * ca.uhn.fhir.interceptor.model.TransactionWriteOperationsDetails - Contains details about the transaction that is about to start
	 * </li>
	 * <li>
	 * ca.uhn.fhir.rest.api.server.storage.TransactionDetails - The outer transaction details object (since 5.0.0)
	 * </li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	STORAGE_TRANSACTION_WRITE_OPERATIONS_PRE(void.class,
		"ca.uhn.fhir.interceptor.model.TransactionWriteOperationsDetails",
		"ca.uhn.fhir.rest.api.server.storage.TransactionDetails"
	),

	/**
	 * <b>Storage Hook:</b>
	 * Invoked during a FHIR transaction, immediately after processing all write operations (i.e. immediately
	 * after the transaction has been committed or rolled back). This hook will always be called if
	 * {@link #STORAGE_TRANSACTION_WRITE_OPERATIONS_PRE} has been called, regardless of whether the operation
	 * succeeded or failed.
	 * <p>
	 * Hooks may accept the following parameters:
	 * </p>
	 * <ul>
	 * <li>
	 * ca.uhn.fhir.interceptor.model.TransactionWriteOperationsDetails - Contains details about the transaction that is about to start
	 * </li>
	 * <li>
	 * ca.uhn.fhir.rest.api.server.storage.TransactionDetails - The outer transaction details object (since 5.0.0)
	 * </li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	STORAGE_TRANSACTION_WRITE_OPERATIONS_POST(void.class,
		"ca.uhn.fhir.interceptor.model.TransactionWriteOperationsDetails",
		"ca.uhn.fhir.rest.api.server.storage.TransactionDetails"
	),

	/**
	 * <b>Storage Hook:</b>
	 * Invoked when a resource delete operation is about to fail due to referential integrity checks. Intended for use with {@literal ca.uhn.fhir.jpa.interceptor.CascadingDeleteInterceptor}.
	 * <p>
	 * Hooks will have access to the list of resources that have references to the resource being deleted.
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>ca.uhn.fhir.jpa.api.model.DeleteConflictList - The list of delete conflicts</li>
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
	 * ca.uhn.fhir.rest.api.server.storage.TransactionDetails - The outer transaction details object (since 5.0.0)
	 * </li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>ca.uhn.fhir.jpa.delete.DeleteConflictOutcome</code>.
	 * If the interceptor returns a non-null result, the DeleteConflictOutcome can be
	 * used to indicate a number of times to retry.
	 * </p>
	 */
	STORAGE_PRESTORAGE_DELETE_CONFLICTS(
		// Return type
		"ca.uhn.fhir.jpa.delete.DeleteConflictOutcome",
		// Params
		"ca.uhn.fhir.jpa.api.model.DeleteConflictList",
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails",
		"ca.uhn.fhir.rest.api.server.storage.TransactionDetails"
	),

	/**
	 * <b>Storage Hook:</b>
	 * Invoked before a resource is about to be expunged via the <code>$expunge</code> operation.
	 * <p>
	 * Hooks will be passed a reference to a counter containing the current number of records that have been deleted.
	 * If the hook deletes any records, the hook is expected to increment this counter by the number of records deleted.
	 * </p>
	 * <p>
	 * Hooks may accept the following parameters:
	 * </p>
	 * <ul>
	 * <li>java.util.concurrent.atomic.AtomicInteger - The counter holding the number of records deleted.</li>
	 * <li>org.hl7.fhir.instance.model.api.IIdType - The ID of the resource that is about to be deleted</li>
	 * <li>org.hl7.fhir.instance.model.api.IBaseResource - The resource that is about to be deleted</li>
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
	 * Hooks should return void.
	 * </p>
	 */
	STORAGE_PRESTORAGE_EXPUNGE_RESOURCE(
		// Return type
		void.class,
		// Params
		"java.util.concurrent.atomic.AtomicInteger",
		"org.hl7.fhir.instance.model.api.IIdType",
		"org.hl7.fhir.instance.model.api.IBaseResource",
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails"
	),

	/**
	 * <b>Storage Hook:</b>
	 * Invoked before an <code>$expunge</code> operation on all data (expungeEverything) is called.
	 * <p>
	 * Hooks will be passed a reference to a counter containing the current number of records that have been deleted.
	 * If the hook deletes any records, the hook is expected to increment this counter by the number of records deleted.
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>java.util.concurrent.atomic.AtomicInteger - The counter holding the number of records deleted.</li>
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
	 * Hooks should return void.
	 * </p>
	 */
	STORAGE_PRESTORAGE_EXPUNGE_EVERYTHING(
		// Return type
		void.class,
		// Params
		"java.util.concurrent.atomic.AtomicInteger",
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails"
	),

	/**
	 * <b>Storage Hook:</b>
	 * Invoked before FHIR <b>create</b> operation to request the identification of the partition ID to be associated
	 * with the resource being created. This hook will only be called if partitioning is enabled in the JPA
	 * server.
	 * <p>
	 * Hooks may accept the following parameters:
	 * </p>
	 * <ul>
	 * <li>
	 * org.hl7.fhir.instance.model.api.IBaseResource - The resource that will be created and needs a tenant ID assigned.
	 * </li>
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
	 * Hooks must return an instance of <code>ca.uhn.fhir.interceptor.model.RequestPartitionId</code>.
	 * </p>
	 */
	STORAGE_PARTITION_IDENTIFY_CREATE(
		// Return type
		"ca.uhn.fhir.interceptor.model.RequestPartitionId",
		// Params
		"org.hl7.fhir.instance.model.api.IBaseResource",
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails"
	),

	/**
	 * <b>Storage Hook:</b>
	 * Invoked before FHIR read/access operation (e.g. <b>read/vread</b>, <b>search</b>, <b>history</b>, etc.) operation to request the
	 * identification of the partition ID to be associated with the resource(s) being searched for, read, etc.
	 * <p>
	 * This hook will only be called if
	 * partitioning is enabled in the JPA server.
	 * </p>
	 * <p>
	 * Hooks may accept the following parameters:
	 * </p>
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
	 * <li>ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails - Contains details about what is being read</li>
	 * </ul>
	 * <p>
	 * Hooks must return an instance of <code>ca.uhn.fhir.interceptor.model.RequestPartitionId</code>.
	 * </p>
	 */
	STORAGE_PARTITION_IDENTIFY_READ(
		// Return type
		"ca.uhn.fhir.interceptor.model.RequestPartitionId",
		// Params
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails",
		"ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails"
	),

	/**
	 * <b>Storage Hook:</b>
	 * Invoked before any partition aware FHIR operation, when the selected partition has been identified (ie. after the
	 * {@link #STORAGE_PARTITION_IDENTIFY_CREATE} or {@link #STORAGE_PARTITION_IDENTIFY_READ} hook was called. This allows
	 * a separate hook to register, and potentially make decisions about whether the request should be allowed to proceed.
	 * <p>
	 * This hook will only be called if
	 * partitioning is enabled in the JPA server.
	 * </p>
	 * <p>
	 * Hooks may accept the following parameters:
	 * </p>
	 * <ul>
	 * <li>
	 * ca.uhn.fhir.interceptor.model.RequestPartitionId - The partition ID that was selected
	 * </li>
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
	 * ca.uhn.fhir.context.RuntimeResourceDefinition - the resource type being accessed
	 * </li>
	 * </ul>
	 * <p>
	 * Hooks must return void.
	 * </p>
	 */
	STORAGE_PARTITION_SELECTED(
		// Return type
		void.class,
		// Params
		"ca.uhn.fhir.interceptor.model.RequestPartitionId",
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails",
		"ca.uhn.fhir.context.RuntimeResourceDefinition"
	),

	/**
	 * <b>Storage Hook:</b>
	 * Invoked when a transaction has been rolled back as a result of a {@link ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException},
	 * meaning that a database constraint has been violated. This pointcut allows an interceptor to specify a resolution strategy
	 * other than simply returning the error to the client. This interceptor will be fired after the database transaction rollback
	 * has been completed.
	 * <p>
	 * Hooks may accept the following parameters:
	 * </p>
	 * <ul>
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
	 * Hooks should return <code>ca.uhn.fhir.jpa.api.model.ResourceVersionConflictResolutionStrategy</code>. Hooks should not
	 * throw any exception.
	 * </p>
	 */
	STORAGE_VERSION_CONFLICT(
		"ca.uhn.fhir.jpa.api.model.ResourceVersionConflictResolutionStrategy",
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails"
	),

	/**
	 * <b>Validation Hook:</b>
	 * This hook is called after validation has completed, regardless of whether the validation was successful or failed.
	 * Typically this is used to modify validation results.
	 * <p>
	 * <b>Note on validation Pointcuts:</b> The HAPI FHIR interceptor framework is a part of the client and server frameworks and
	 * not a part of the core FhirContext. Therefore this Pointcut is invoked by the
	 * </p>
	 * <p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>
	 * org.hl7.fhir.instance.model.api.IBaseResource - The resource being validated, if a parsed version is available (null otherwise)
	 * </li>
	 * <li>
	 * java.lang.String - The resource being validated, if a raw version is available (null otherwise)
	 * </li>
	 * <li>
	 * ca.uhn.fhir.validation.ValidationResult - The outcome of the validation. Hooks methods should not modify this object, but they can return a new one.
	 * </li>
	 * </ul>
	 * </p>
	 * Hook methods may return an instance of {@link ca.uhn.fhir.validation.ValidationResult} if they wish to override the validation results, or they may return <code>null</code> or <code>void</code> otherwise.
	 */
	VALIDATION_COMPLETED(ValidationResult.class,
		"org.hl7.fhir.instance.model.api.IBaseResource",
		"java.lang.String",
		"ca.uhn.fhir.validation.ValidationResult"
	),


	/**
	 * <b>MDM(EMPI) Hook:</b>
	 * Invoked whenever a persisted resource (a resource that has just been stored in the
	 * database via a create/update/patch/etc.) has been matched against related resources and MDM links have been updated.
	 * <p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>ca.uhn.fhir.rest.server.messaging.ResourceOperationMessage - This parameter should not be modified as processing is complete when this hook is invoked.</li>
	 * <li>ca.uhn.fhir.rest.server.TransactionLogMessages - This parameter is for informational messages provided by the MDM module during MDM processing.</li>
	 * <li>ca.uhn.fhir.mdm.api.MdmLinkChangeEvent - Contains information about the change event, including target and golden resource IDs and the operation type.</li>
	 * </ul>
	 * </p>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	MDM_AFTER_PERSISTED_RESOURCE_CHECKED(void.class,
		"ca.uhn.fhir.rest.server.messaging.ResourceOperationMessage",
		"ca.uhn.fhir.rest.server.TransactionLogMessages",
		"ca.uhn.fhir.mdm.api.MdmLinkEvent"),

	/**
	 * <b>Performance Tracing Hook:</b>
	 * This hook is invoked when any informational messages generated by the
	 * SearchCoordinator are created. It is typically used to provide logging
	 * or capture details related to a specific request.
	 * <p>
	 * Note that this is a performance tracing hook. Use with caution in production
	 * systems, since calling it may (or may not) carry a cost.
	 * </p>
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
	 * ca.uhn.fhir.jpa.model.search.StorageProcessingMessage - Contains the message
	 * </li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	JPA_PERFTRACE_INFO(void.class,
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails",
		"ca.uhn.fhir.jpa.model.search.StorageProcessingMessage"
	),

	/**
	 * <b>Performance Tracing Hook:</b>
	 * This hook is invoked when any warning messages generated by the
	 * SearchCoordinator are created. It is typically used to provide logging
	 * or capture details related to a specific request.
	 * <p>
	 * Note that this is a performance tracing hook. Use with caution in production
	 * systems, since calling it may (or may not) carry a cost.
	 * </p>
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
	 * ca.uhn.fhir.jpa.model.search.StorageProcessingMessage - Contains the message
	 * </li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	JPA_PERFTRACE_WARNING(void.class,
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails",
		"ca.uhn.fhir.jpa.model.search.StorageProcessingMessage"
	),

	/**
	 * <b>Performance Tracing Hook:</b>
	 * This hook is invoked when a search has returned the very first result
	 * from the database. The timing on this call can be a good indicator of how
	 * performant a query is in general.
	 * <p>
	 * Note that this is a performance tracing hook. Use with caution in production
	 * systems, since calling it may (or may not) carry a cost.
	 * </p>
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
	 * ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails - Contains details about the search being
	 * performed. Hooks should not modify this object.
	 * </li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	JPA_PERFTRACE_SEARCH_FIRST_RESULT_LOADED(void.class,
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails",
		"ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails"
	),

	/**
	 * <b>Performance Tracing Hook:</b>
	 * This hook is invoked when an individual search query SQL SELECT statement
	 * has completed and no more results are available from that query. Note that this
	 * doesn't necessarily mean that no more matching results exist in the database,
	 * since HAPI FHIR JPA batch loads results in to the query cache in chunks in order
	 * to provide predicable results without overloading memory or the database.
	 * <p>
	 * Note that this is a performance tracing hook. Use with caution in production
	 * systems, since calling it may (or may not) carry a cost.
	 * </p>
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
	 * ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails - Contains details about the search being
	 * performed. Hooks should not modify this object.
	 * </li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	JPA_PERFTRACE_SEARCH_SELECT_COMPLETE(void.class,
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails",
		"ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails"
	),


	/**
	 * <b>Performance Tracing Hook:</b>
	 * This hook is invoked when a search has failed for any reason. When this pointcut
	 * is invoked, the search has completed unsuccessfully and will not be continued.
	 * <p>
	 * Note that this is a performance tracing hook. Use with caution in production
	 * systems, since calling it may (or may not) carry a cost.
	 * </p>
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
	 * ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails - Contains details about the search being
	 * performed. Hooks should not modify this object.
	 * </li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	JPA_PERFTRACE_SEARCH_FAILED(void.class,
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails",
		"ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails"
	),

	/**
	 * <b>Performance Tracing Hook:</b>
	 * This hook is invoked when a search has completed. When this pointcut
	 * is invoked, a pass in the Search Coordinator has completed successfully, but
	 * not all possible resources have been loaded yet so a future paging request
	 * may trigger a new task that will load further resources.
	 * <p>
	 * Note that this is a performance tracing hook. Use with caution in production
	 * systems, since calling it may (or may not) carry a cost.
	 * </p>
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
	 * ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails - Contains details about the search being
	 * performed. Hooks should not modify this object.
	 * </li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	JPA_PERFTRACE_SEARCH_PASS_COMPLETE(void.class,
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails",
		"ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails"
	),

	/**
	 * <b>Performance Tracing Hook:</b>
	 * This hook is invoked when a query involving an external index (e.g. Elasticsearch) has completed. When this pointcut
	 * is invoked, an initial list of resource IDs has been generated which will be used as part of a subsequent database query.
	 * <p>
	 * Note that this is a performance tracing hook. Use with caution in production
	 * systems, since calling it may (or may not) carry a cost.
	 * </p>
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
	 * ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails - Contains details about the search being
	 * performed. Hooks should not modify this object.
	 * </li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	JPA_PERFTRACE_INDEXSEARCH_QUERY_COMPLETE(void.class,
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails",
		"ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails"
	),

	/**
	 * <b>Performance Tracing Hook:</b>
	 * Invoked when the storage engine is about to reuse the results of
	 * a previously cached search.
	 * <p>
	 * Note that this is a performance tracing hook. Use with caution in production
	 * systems, since calling it may (or may not) carry a cost.
	 * </p>
	 * <p>
	 * Hooks may accept the following parameters:
	 * </p>
	 * <ul>
	 * <li>
	 * ca.uhn.fhir.jpa.searchparam.SearchParameterMap - Contains the details of the search being checked
	 * </li>
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
	JPA_PERFTRACE_SEARCH_REUSING_CACHED(boolean.class,
		"ca.uhn.fhir.jpa.searchparam.SearchParameterMap",
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails"
	),

	/**
	 * <b>Performance Tracing Hook:</b>
	 * This hook is invoked when a search has failed for any reason. When this pointcut
	 * is invoked, a pass in the Search Coordinator has completed successfully, and all
	 * possible results have been fetched and loaded into the query cache.
	 * <p>
	 * Note that this is a performance tracing hook. Use with caution in production
	 * systems, since calling it may (or may not) carry a cost.
	 * </p>
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
	 * ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails - Contains details about the search being
	 * performed. Hooks should not modify this object.
	 * </li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	JPA_PERFTRACE_SEARCH_COMPLETE(void.class,
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails",
		"ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails"
	),


	/**
	 * <b>Performance Tracing Hook:</b>
	 * <p>
	 * This hook is invoked when a search has found an individual ID.
	 * </p>
	 * <p>
	 * THIS IS AN EXPERIMENTAL HOOK AND MAY BE REMOVED OR CHANGED WITHOUT WARNING.
	 * </p>
	 * <p>
	 * Note that this is a performance tracing hook. Use with caution in production
	 * systems, since calling it may (or may not) carry a cost.
	 * </p>
	 * <p>
	 * Hooks may accept the following parameters:
	 * </p>
	 * <ul>
	 * <li>
	 * java.lang.Integer - The query ID
	 * </li>
	 * <li>
	 * java.lang.Object - The ID
	 * </li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	JPA_PERFTRACE_SEARCH_FOUND_ID(void.class,
		"java.lang.Integer",
		"java.lang.Object"
	),


	/**
	 * <b>Performance Tracing Hook:</b>
	 * This hook is invoked when a query has executed, and includes the raw SQL
	 * statements that were executed against the database.
	 * <p>
	 * Note that this is a performance tracing hook. Use with caution in production
	 * systems, since calling it may (or may not) carry a cost.
	 * </p>
	 * <p>
	 * Hooks may accept the following parameters:
	 * </p>
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
	 * ca.uhn.fhir.jpa.util.SqlQueryList - Contains details about the raw SQL queries.
	 * </li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	JPA_PERFTRACE_RAW_SQL(void.class,
		"ca.uhn.fhir.rest.api.server.RequestDetails",
		"ca.uhn.fhir.rest.server.servlet.ServletRequestDetails",
		"ca.uhn.fhir.jpa.util.SqlQueryList"
	),

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

	Pointcut(@Nonnull String theReturnType, String... theParameterTypes) {
		this(toReturnTypeClass(theReturnType), new ExceptionHandlingSpec(), theParameterTypes);
	}

	Pointcut(@Nonnull Class<?> theReturnType, @Nonnull ExceptionHandlingSpec theExceptionHandlingSpec, String... theParameterTypes) {
		myReturnType = theReturnType;
		myExceptionHandlingSpec = theExceptionHandlingSpec;
		myParameterTypes = Collections.unmodifiableList(Arrays.asList(theParameterTypes));
	}

	Pointcut(@Nonnull Class<?> theReturnType, String... theParameterTypes) {
		this(theReturnType, new ExceptionHandlingSpec(), theParameterTypes);
	}

	@Override
	public boolean isShouldLogAndSwallowException(@Nonnull Throwable theException) {
		for (Class<? extends Throwable> next : myExceptionHandlingSpec.myTypesToLogAndSwallow) {
			if (next.isAssignableFrom(theException.getClass())) {
				return true;
			}
		}
		return false;
	}

	@Override
	@Nonnull
	public Class<?> getReturnType() {
		return myReturnType;
	}

	@Override
	@Nonnull
	public List<String> getParameterTypes() {
		return myParameterTypes;
	}

	private static class UnknownType {
	}

	private static class ExceptionHandlingSpec {

		private final Set<Class<? extends Throwable>> myTypesToLogAndSwallow = new HashSet<>();

		ExceptionHandlingSpec addLogAndSwallow(@Nonnull Class<? extends Throwable> theType) {
			myTypesToLogAndSwallow.add(theType);
			return this;
		}

	}

	private static Class<?> toReturnTypeClass(String theReturnType) {
		try {
			return Class.forName(theReturnType);
		} catch (ClassNotFoundException theE) {
			return UnknownType.class;
		}
	}

}
