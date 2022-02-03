package ca.uhn.fhir.rest.server.method;

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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.annotation.Metadata;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.IRestfulServer;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.hl7.fhir.instance.model.api.IBaseConformance;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class ConformanceMethodBinding extends BaseResourceReturningMethodBinding {
	public static final String CACHE_THREAD_PREFIX = "capabilitystatement-cache-";
	/*
	 * Note: This caching mechanism should probably be configurable and maybe
	 * even applicable to other bindings. It's particularly important for this
	 * operation though, so a one-off is fine for now
	 */
	private final AtomicReference<IBaseConformance> myCachedResponse = new AtomicReference<>();
	private final AtomicLong myCachedResponseExpires = new AtomicLong(0L);
	private final ExecutorService myThreadPool;
	private long myCacheMillis = 60 * 1000;

	ConformanceMethodBinding(Method theMethod, FhirContext theContext, Object theProvider) {
		super(theMethod.getReturnType(), theMethod, theContext, theProvider);

		MethodReturnTypeEnum methodReturnType = getMethodReturnType();
		Class<?> genericReturnType = (Class<?>) theMethod.getGenericReturnType();
		if (methodReturnType != MethodReturnTypeEnum.RESOURCE || !IBaseConformance.class.isAssignableFrom(genericReturnType)) {
			throw new ConfigurationException(Msg.code(387) + "Conformance resource provider method '" + theMethod.getName() + "' should return a Conformance resource class, returns: " + theMethod.getReturnType());
		}

		Metadata metadata = theMethod.getAnnotation(Metadata.class);
		if (metadata != null) {
			setCacheMillis(metadata.cacheMillis());
		}

		ThreadFactory threadFactory = r -> {
			Thread t = new Thread(r);
			t.setName(CACHE_THREAD_PREFIX + t.getId());
			t.setDaemon(false);
			return t;
		};
		myThreadPool = new ThreadPoolExecutor(1, 1,
			0L, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<>(1),
			threadFactory,
			new ThreadPoolExecutor.DiscardOldestPolicy());
	}

	/**
	 * Returns the number of milliseconds to cache the generated CapabilityStatement for. Default is one minute, and can be
	 * set to 0 to never cache.
	 *
	 * @see #setCacheMillis(long)
	 * @see Metadata#cacheMillis()
	 * @since 4.1.0
	 */
	private long getCacheMillis() {
		return myCacheMillis;
	}

	/**
	 * Returns the number of milliseconds to cache the generated CapabilityStatement for. Default is one minute, and can be
	 * set to 0 to never cache.
	 *
	 * @see #getCacheMillis()
	 * @see Metadata#cacheMillis()
	 * @since 4.1.0
	 */
	public void setCacheMillis(long theCacheMillis) {
		myCacheMillis = theCacheMillis;
	}

	@Override
	public ReturnTypeEnum getReturnType() {
		return ReturnTypeEnum.RESOURCE;
	}

	@Override
	public void close() {
		super.close();

		myThreadPool.shutdown();
	}

	@Override
	public IBundleProvider invokeServer(IRestfulServer<?> theServer, RequestDetails theRequest, Object[] theMethodParams) throws BaseServerResponseException {
		IBaseConformance conf;

		CacheControlDirective cacheControlDirective = new CacheControlDirective().parse(theRequest.getHeaders(Constants.HEADER_CACHE_CONTROL));

		if (cacheControlDirective.isNoCache())
			conf = null;
		else {
			conf = myCachedResponse.get();
			if ("true".equals(System.getProperty("test"))) {
				conf = null;
			}
			if (conf != null) {
				long expires = myCachedResponseExpires.get();
				if (expires < System.currentTimeMillis()) {
					myCachedResponseExpires.set(System.currentTimeMillis() + getCacheMillis());
					myThreadPool.submit(() -> createCapabilityStatement(theRequest, theMethodParams));
				}
			}
		}
		if (conf != null) {
			// Handle server action interceptors
			RestOperationTypeEnum operationType = getRestOperationType(theRequest);
			if (operationType != null) {
				IServerInterceptor.ActionRequestDetails details = new IServerInterceptor.ActionRequestDetails(theRequest);
				populateActionRequestDetailsForInterceptor(theRequest, details, theMethodParams);
				// Interceptor hook: SERVER_INCOMING_REQUEST_PRE_HANDLED
				if (theRequest.getInterceptorBroadcaster() != null) {
					HookParams preHandledParams = new HookParams();
					preHandledParams.add(RestOperationTypeEnum.class, theRequest.getRestOperationType());
					preHandledParams.add(RequestDetails.class, theRequest);
					preHandledParams.addIfMatchesType(ServletRequestDetails.class, theRequest);
					preHandledParams.add(IServerInterceptor.ActionRequestDetails.class, details);
					theRequest
						.getInterceptorBroadcaster()
						.callHooks(Pointcut.SERVER_INCOMING_REQUEST_PRE_HANDLED, preHandledParams);
				}
			}
		}

		if (conf == null) {
			conf = createCapabilityStatement(theRequest, theMethodParams);
		}

		return new SimpleBundleProvider(conf);
	}

	private IBaseConformance createCapabilityStatement(RequestDetails theRequest, Object[] theMethodParams) {
		IBaseConformance conf = (IBaseConformance) invokeServerMethod(theRequest, theMethodParams);

		// Interceptor hook: SERVER_CAPABILITY_STATEMENT_GENERATED
		if (theRequest.getInterceptorBroadcaster() != null) {
			HookParams params = new HookParams();
			params.add(IBaseConformance.class, conf);
			params.add(RequestDetails.class, theRequest);
			params.addIfMatchesType(ServletRequestDetails.class, theRequest);
			IBaseConformance outcome = (IBaseConformance) theRequest
				.getInterceptorBroadcaster()
				.callHooksAndReturnObject(Pointcut.SERVER_CAPABILITY_STATEMENT_GENERATED, params);
			if (outcome != null) {
				conf = outcome;
			}
		}

		if (myCacheMillis > 0) {
			myCachedResponse.set(conf);
			myCachedResponseExpires.set(System.currentTimeMillis() + getCacheMillis());
		}
		
		return conf;
	}

	@Override
	public MethodMatchEnum incomingServerRequestMatchesMethod(RequestDetails theRequest) {
		if (theRequest.getRequestType() == RequestTypeEnum.OPTIONS) {
			if (theRequest.getOperation() == null && theRequest.getResourceName() == null) {
				return MethodMatchEnum.EXACT;
			}
		}

		if (theRequest.getResourceName() != null) {
			return MethodMatchEnum.NONE;
		}

		if ("metadata".equals(theRequest.getOperation())) {
			if (theRequest.getRequestType() == RequestTypeEnum.GET) {
				return MethodMatchEnum.EXACT;
			}
			throw new MethodNotAllowedException(Msg.code(388) + "/metadata request must use HTTP GET", RequestTypeEnum.GET);
		}

		return MethodMatchEnum.NONE;
	}

	@Nonnull
	@Override
	public RestOperationTypeEnum getRestOperationType() {
		return RestOperationTypeEnum.METADATA;
	}

	@Override
	protected BundleTypeEnum getResponseBundleType() {
		return null;
	}

	/**
	 * Create and return the server's CapabilityStatement
	 */
	public IBaseConformance provideCapabilityStatement(RestfulServer theServer, RequestDetails theRequest) {
		Object[] params = createMethodParams(theRequest);
		IBundleProvider resultObj = invokeServer(theServer, theRequest, params);
		return (IBaseConformance) resultObj.getResources(0,1).get(0);
	}

}
