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

public interface IInterceptorBroadcaster {

	/**
	 * Invoke registered interceptor hook methods for the given Pointcut.
	 *
	 * @return Returns <code>false</code> if any of the invoked hook methods returned
	 * <code>false</code>, and returns <code>true</code> otherwise.
	 */
	boolean callHooks(Pointcut thePointcut, HookParams theParams);

	/**
	 * Invoke registered interceptor hook methods for the given Pointcut. This method
	 * should only be called for pointcuts that return a type other than
	 * <code>void</code> or <code>boolean</code>
	 *
	 * @return Returns the object returned by the first hook method that did not return <code>null</code>
	 */
	Object callHooksAndReturnObject(Pointcut thePointcut, HookParams theParams);

}
