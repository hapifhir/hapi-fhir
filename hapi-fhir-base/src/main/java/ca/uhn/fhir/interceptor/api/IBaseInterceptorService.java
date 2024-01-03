/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.interceptor.api;

import jakarta.annotation.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public interface IBaseInterceptorService<POINTCUT extends IPointcut> extends IBaseInterceptorBroadcaster<POINTCUT> {

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
	 * @return Returns <code>true</code> if the interceptor was found and removed
	 */
	boolean unregisterInterceptor(Object theInterceptor);

	/**
	 * Returns all currently registered interceptors (excluding any thread local interceptors).
	 */
	List<Object> getAllRegisteredInterceptors();

	/**
	 * Unregisters all registered interceptors.
	 */
	void unregisterAllInterceptors();

	void unregisterInterceptors(@Nullable Collection<?> theInterceptors);

	void registerInterceptors(@Nullable Collection<?> theInterceptors);

	/**
	 * Unregisters all interceptors that are indicated by the given callback function returning <code>true</code>
	 */
	void unregisterInterceptorsIf(Predicate<Object> theShouldUnregisterFunction);

	/**
	 * Unregisters all anonymous interceptors (i.e. all interceptors registered with <code>registerAnonymousInterceptor</code>)
	 */
	void unregisterAllAnonymousInterceptors();
}
