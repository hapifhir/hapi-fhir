package ca.uhn.fhir.interceptor.executor;

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

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.Validate;

import java.lang.reflect.Method;
import java.util.Optional;

public class InterceptorService extends BaseInterceptorService<Pointcut> implements IInterceptorService, IInterceptorBroadcaster {

	/**
	 * Constructor which uses a default name of "default"
	 */
	public InterceptorService() {
		this("default");
	}

	/**
	 * Constructor
	 *
	 * @param theName The name for this registry (useful for troubleshooting)
	 */
	public InterceptorService(String theName) {
		super(theName);
	}

	@Override
	protected Optional<HookDescriptor> scanForHook(Method nextMethod) {
		return findAnnotation(nextMethod, Hook.class).map(t -> new HookDescriptor(t.value(), t.order()));
	}


	@Override
	@VisibleForTesting
	public void registerAnonymousInterceptor(Pointcut thePointcut, IAnonymousInterceptor theInterceptor) {
		registerAnonymousInterceptor(thePointcut, Interceptor.DEFAULT_ORDER, theInterceptor);
	}

	@Override
	public void registerAnonymousInterceptor(Pointcut thePointcut, int theOrder, IAnonymousInterceptor theInterceptor) {
		Validate.notNull(thePointcut);
		Validate.notNull(theInterceptor);
		BaseInvoker invoker = new AnonymousLambdaInvoker(thePointcut, theInterceptor, theOrder);
		registerAnonymousInterceptor(thePointcut, theInterceptor, invoker);
	}


	private class AnonymousLambdaInvoker extends BaseInvoker {
		private final IAnonymousInterceptor myHook;
		private final Pointcut myPointcut;

		public AnonymousLambdaInvoker(Pointcut thePointcut, IAnonymousInterceptor theHook, int theOrder) {
			super(theHook, theOrder);
			myHook = theHook;
			myPointcut = thePointcut;
		}

		@Override
		Object invoke(HookParams theParams) {
			myHook.invoke(myPointcut, theParams);
			return true;
		}
	}


}
