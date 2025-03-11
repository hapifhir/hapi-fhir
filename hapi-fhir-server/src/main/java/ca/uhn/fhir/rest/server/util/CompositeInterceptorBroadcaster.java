/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.rest.server.util;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.executor.BaseInterceptorService;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

/**
 * This is an {@link IInterceptorBroadcaster} which combines multiple interceptor
 * broadcasters. Hook methods are called across all broadcasters, respecting
 * the {@link Hook#order()} across all broadcasters.
 */
public class CompositeInterceptorBroadcaster implements IInterceptorBroadcaster {

	private final List<IInterceptorBroadcaster> myServices;

	/**
	 * Constructor
	 */
	private CompositeInterceptorBroadcaster(Collection<IInterceptorBroadcaster> theServices) {
		myServices = theServices.stream().filter(t -> t != null).collect(Collectors.toList());
	}

	@Override
	public boolean callHooks(Pointcut thePointcut, HookParams theParams) {
		assert BaseInterceptorService.haveAppropriateParams(thePointcut, theParams);
		assert thePointcut.getReturnType() == void.class
				|| thePointcut.getReturnType() == thePointcut.getBooleanReturnTypeForEnum();

		List<IInvoker> invokers = getInvokersForPointcut(thePointcut);
		Object retVal = BaseInterceptorService.callInvokers(thePointcut, theParams, invokers);
		retVal = defaultIfNull(retVal, true);
		return (Boolean) retVal;
	}

	@Override
	public Object callHooksAndReturnObject(Pointcut thePointcut, HookParams theParams) {
		assert BaseInterceptorService.haveAppropriateParams(thePointcut, theParams);
		assert thePointcut.getReturnType() != void.class;

		List<IInvoker> invokers = getInvokersForPointcut(thePointcut);
		return BaseInterceptorService.callInvokers(thePointcut, theParams, invokers);
	}

	@Override
	@Nonnull
	public List<IInvoker> getInvokersForPointcut(Pointcut thePointcut) {
		List<IInvoker> invokers = new ArrayList<>();
		for (IInterceptorBroadcaster services : myServices) {
			if (services.hasHooks(thePointcut)) {
				List<IInvoker> serviceInvokers = services.getInvokersForPointcut(thePointcut);
				assert serviceInvokers != null;
				invokers.addAll(serviceInvokers);
			}
		}
		invokers.sort(Comparator.naturalOrder());
		return invokers;
	}

	@Override
	public boolean hasHooks(Pointcut thePointcut) {
		for (IInterceptorBroadcaster service : myServices) {
			if (service.hasHooks(thePointcut)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @since 8.0.0
	 */
	public static IInterceptorBroadcaster newCompositeBroadcaster(IInterceptorBroadcaster... theServices) {
		return new CompositeInterceptorBroadcaster(Arrays.asList(theServices));
	}

	/**
	 * @since 5.5.0
	 */
	public static IInterceptorBroadcaster newCompositeBroadcaster(
			@Nonnull IInterceptorBroadcaster theInterceptorBroadcaster, @Nullable RequestDetails theRequestDetails) {
		if (theRequestDetails != null) {
			IInterceptorBroadcaster requestBroadcaster = theRequestDetails.getInterceptorBroadcaster();
			if (requestBroadcaster != null) {
				return newCompositeBroadcaster(theInterceptorBroadcaster, requestBroadcaster);
			}
		}

		return newCompositeBroadcaster(theInterceptorBroadcaster);
	}
}
