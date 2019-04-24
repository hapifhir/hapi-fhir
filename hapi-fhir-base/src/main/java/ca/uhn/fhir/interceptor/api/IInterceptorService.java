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

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

public interface IInterceptorService extends IInterceptorBroadcaster {

	/**
	 * Register an interceptor that will be used in a {@link ThreadLocal} context.
	 * This means that events will only be broadcast to the given interceptor if
	 * they were fired from the current thread.
	 * <p>
	 * Note that it is almost always desirable to call this method with a
	 * try-finally statement that removes the interceptor afterwards, since
	 * this can lead to memory leakage, poor performance due to ever-increasing
	 * numbers of interceptors, etc.
	 * </p>
	 * <p>
	 * Note that most methods such as {@link #getAllRegisteredInterceptors()} and
	 * {@link #unregisterAllInterceptors()} do not affect thread local interceptors
	 * as they are kept in a separate list.
	 * </p>
	 *
	 * @param theInterceptor The interceptor
	 * @return Returns <code>true</code> if at least one valid hook method was found on this interceptor
	 */
	boolean registerThreadLocalInterceptor(Object theInterceptor);

	/**
	 * Unregisters a ThreadLocal interceptor
	 *
	 * @param theInterceptor The interceptor
	 * @see #registerThreadLocalInterceptor(Object)
	 */
	void unregisterThreadLocalInterceptor(Object theInterceptor);

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

	void registerAnonymousInterceptor(Pointcut thePointcut, IAnonymousInterceptor theInterceptor);

	void registerAnonymousInterceptor(Pointcut thePointcut, int theOrder, IAnonymousInterceptor theInterceptor);

	/**
	 * Returns all currently registered interceptors (excluding any thread local interceptors).
	 */
	List<Object> getAllRegisteredInterceptors();

	/**
	 * Unregisters all registered interceptors. Note that this method does not unregister
	 * any {@link #registerThreadLocalInterceptor(Object) thread local interceptors}.
	 */
	void unregisterAllInterceptors();

	void unregisterInterceptors(@Nullable Collection<?> theInterceptors);

	void registerInterceptors(@Nullable Collection<?> theInterceptors);

}
