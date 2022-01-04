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

public interface IBaseInterceptorBroadcaster<POINTCUT extends IPointcut> {

	/**
	 * Invoke registered interceptor hook methods for the given Pointcut.
	 *
	 * @return Returns <code>false</code> if any of the invoked hook methods returned
	 * <code>false</code>, and returns <code>true</code> otherwise.
	 */
	boolean callHooks(POINTCUT thePointcut, HookParams theParams);

	/**
	 * Invoke registered interceptor hook methods for the given Pointcut. This method
	 * should only be called for pointcuts that return a type other than
	 * <code>void</code> or <code>boolean</code>
	 *
	 * @return Returns the object returned by the first hook method that did not return <code>null</code>
	 */
	Object callHooksAndReturnObject(POINTCUT thePointcut, HookParams theParams);

	/**
	 * Does this broadcaster have any hooks for the given pointcut?
	 *
	 * @param thePointcut The poointcut
	 * @return Does this broadcaster have any hooks for the given pointcut?
	 * @since 4.0.0
	 */
	boolean hasHooks(POINTCUT thePointcut);
}
