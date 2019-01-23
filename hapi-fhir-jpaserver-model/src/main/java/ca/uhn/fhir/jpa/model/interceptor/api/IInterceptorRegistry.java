package ca.uhn.fhir.jpa.model.interceptor.api;

/*-
 * #%L
 * HAPI FHIR Model
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

import com.google.common.annotations.VisibleForTesting;

public interface IInterceptorRegistry {

	int DEFAULT_ORDER = 0;

	/**
	 * Register an interceptor. This method has no effect if the given interceptor is already registered.
	 *
	 * @param theInterceptor The interceptor to register
	 * @return Returns <code>true</code> if at least one valid hook method was found on this interceptor
	 */
	boolean registerInterceptor(Object theInterceptor);

	/**
	 * Unregister an interceptor. This method has no effect if the given interceptor is not already registered.
	 *
	 * @param theInterceptor The interceptor to unregister
	 */
	void unregisterInterceptor(Object theInterceptor);

	/**
	 * @deprecated to be removed
	 */
	@Deprecated
	boolean registerGlobalInterceptor(Object theInterceptor);

	/**
	 * @deprecated to be removed
	 */
	@Deprecated
	void unregisterGlobalInterceptor(Object theInterceptor);


	@VisibleForTesting
	void registerAnonymousHookForUnitTest(Pointcut thePointcut, IAnonymousLambdaHook theHook);

	@VisibleForTesting
	void registerAnonymousHookForUnitTest(Pointcut thePointcut, int theOrder, IAnonymousLambdaHook theHook);

	@VisibleForTesting
	void clearAnonymousHookForUnitTest();
}
