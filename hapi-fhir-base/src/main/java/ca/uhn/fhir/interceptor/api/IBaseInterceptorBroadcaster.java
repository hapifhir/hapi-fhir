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

import java.util.function.Supplier;

public interface IBaseInterceptorBroadcaster<POINTCUT extends IPointcut> {

	/**
	 * Invoke registered interceptor hook methods for the given Pointcut.
	 *
	 * @return Returns <code>false</code> if any of the invoked hook methods returned
	 * <code>false</code>, and returns <code>true</code> otherwise.
	 */
	boolean callHooks(POINTCUT thePointcut, HookParams theParams);

	/**
	 * A supplier-based callHooks() for lazy construction of the HookParameters.
	 * @return false if any hook methods return false, return true otherwise.
	 */
	default boolean ifHasCallHooks(POINTCUT thePointcut, Supplier<HookParams> theParamsSupplier) {
		if (hasHooks(thePointcut)) {
			HookParams params = theParamsSupplier.get();
			return callHooks(thePointcut, params);
		}
		return true; // callHooks returns true when none present;
	}

	/**
	 * Invoke registered interceptor hook methods for the given Pointcut. This method
	 * should only be called for pointcuts that return a type other than
	 * <code>void</code> or <code>boolean</code>
	 *
	 * @return Returns the object returned by the first hook method that did not return <code>null</code>
	 */
	Object callHooksAndReturnObject(POINTCUT thePointcut, HookParams theParams);

	/**
	 * A supplier-based version of callHooksAndReturnObject for lazy construction of the params.
	 *
	 * @return Returns the object returned by the first hook method that did not return <code>null</code> or <code>null</code>
	 */
	default Object ifHasCallHooksAndReturnObject(POINTCUT thePointcut, Supplier<HookParams> theParams) {
		if (hasHooks(thePointcut)) {
			HookParams params = theParams.get();
			return callHooksAndReturnObject(thePointcut, params);
		}
		return null;
	}

	/**
	 * Does this broadcaster have any hooks for the given pointcut?
	 *
	 * @param thePointcut The poointcut
	 * @return Does this broadcaster have any hooks for the given pointcut?
	 * @since 4.0.0
	 */
	boolean hasHooks(POINTCUT thePointcut);
}
