package ca.uhn.fhir.rest.server.method;

/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.hl7.fhir.instance.model.api.IBaseConformance;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.IRestfulServer;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;

import javax.annotation.Nonnull;

public class ConformanceMethodBinding extends BaseResourceReturningMethodBinding {
	private static final long CACHE_MILLIS = 60 * 1000;

	/*
	 * Note: This caching mechanism should probably be configurable and maybe
	 * even applicable to other bindings. It's particularly important for this
	 * operation though, so a one-off is fine for now
	 */
	private final AtomicReference<IBaseResource> myCachedResponse = new AtomicReference<>();
	private final AtomicLong myCachedResponseExpires = new AtomicLong(0L);


	ConformanceMethodBinding(Method theMethod, FhirContext theContext, Object theProvider) {
		super(theMethod.getReturnType(), theMethod, theContext, theProvider);

		MethodReturnTypeEnum methodReturnType = getMethodReturnType();
		Class<?> genericReturnType = (Class<?>) theMethod.getGenericReturnType();
		if (methodReturnType != MethodReturnTypeEnum.RESOURCE || !IBaseConformance.class.isAssignableFrom(genericReturnType)) {
			throw new ConfigurationException("Conformance resource provider method '" + theMethod.getName() + "' should return a Conformance resource class, returns: " + theMethod.getReturnType());
		}

	}

	@Override
	public ReturnTypeEnum getReturnType() {
		return ReturnTypeEnum.RESOURCE;
	}

	@Override
	public IBundleProvider invokeServer(IRestfulServer<?> theServer, RequestDetails theRequest, Object[] theMethodParams) throws BaseServerResponseException {
		IBaseResource conf;

		conf = myCachedResponse.get();
		if ("true".equals(System.getProperty("test"))) {
			conf = null;
		}
		if (conf != null) {
			long expires = myCachedResponseExpires.get();
			if (expires < System.currentTimeMillis()) {
				conf = null;
			}
		}
		if (conf != null) {
			// Handle server action interceptors
			RestOperationTypeEnum operationType = getRestOperationType(theRequest);
			if (operationType != null) {
				IServerInterceptor.ActionRequestDetails details = new IServerInterceptor.ActionRequestDetails(theRequest);
				populateActionRequestDetailsForInterceptor(theRequest, details, theMethodParams);
				HookParams preHandledParams = new HookParams();
				preHandledParams.add(RestOperationTypeEnum.class, theRequest.getRestOperationType());
				preHandledParams.add(RequestDetails.class, theRequest);
				preHandledParams.addIfMatchesType(ServletRequestDetails.class, theRequest);
				preHandledParams.add(IServerInterceptor.ActionRequestDetails.class, details);
				if (theRequest.getInterceptorBroadcaster() != null) {
					theRequest
						.getInterceptorBroadcaster()
						.callHooks(Pointcut.SERVER_INCOMING_REQUEST_PRE_HANDLED, preHandledParams);
				}
			}
		}

		if (conf == null) {
			conf = (IBaseResource) invokeServerMethod(theServer, theRequest, theMethodParams);
			myCachedResponse.set(conf);
			myCachedResponseExpires.set(System.currentTimeMillis() + CACHE_MILLIS);
		}

		return new SimpleBundleProvider(conf);
	}

	@Override
	public boolean incomingServerRequestMatchesMethod(RequestDetails theRequest) {
		if (theRequest.getRequestType() == RequestTypeEnum.OPTIONS) {
			if (theRequest.getOperation() == null && theRequest.getResourceName() == null) {
				return true;
			}
		}

		if (theRequest.getResourceName() != null) {
			return false;
		}

		if ("metadata".equals(theRequest.getOperation())) {
			if (theRequest.getRequestType() == RequestTypeEnum.GET) {
				return true;
			}
			throw new MethodNotAllowedException("/metadata request must use HTTP GET", RequestTypeEnum.GET);
		}

		return false;
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

}
