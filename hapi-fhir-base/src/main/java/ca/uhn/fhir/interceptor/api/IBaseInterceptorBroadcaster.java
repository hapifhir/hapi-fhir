/*-
 * #%L
 * HAPI FHIR - Core Library
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
package ca.uhn.fhir.interceptor.api;

import ca.uhn.fhir.interceptor.executor.SupplierFilterHookWrapper;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.function.Function;
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
		return true; // callHooks returns true when none present
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

	default void runWithFilterHooks(POINTCUT thePointcut, HookParams theHookParams, Runnable theRunnable) {
		runWithFilterHooks(thePointcut, theHookParams, () -> {
			theRunnable.run();
			return null;
		});
	}

	default <T> T runWithFilterHooks(POINTCUT thePointcut, HookParams theHookParams, Supplier<T> theSupplier) {
		List<IInvoker> invokers = getInvokersForPointcut(thePointcut);

		Supplier<T> runnable = theSupplier;

		// Build a linked list of wrappers.
		// We traverse the invokers in reverse order because the first wrapper will be called last in sequence.
		for (IInvoker nextInvoker : Lists.reverse(invokers)) {
			@SuppressWarnings("unchecked")
			IInterceptorFilterHook<T> filter = (IInterceptorFilterHook<T>) nextInvoker.invoke(theHookParams);
			runnable = new SupplierFilterHookWrapper<>(runnable, filter, nextInvoker::getHookDescription);
		}

		return runnable.get();
	}

	/**
	 * Does this broadcaster have any hooks for the given pointcut?
	 *
	 * @param thePointcut The poointcut
	 * @return Does this broadcaster have any hooks for the given pointcut?
	 * @since 4.0.0
	 */
	boolean hasHooks(POINTCUT thePointcut);

	List<IInvoker> getInvokersForPointcut(POINTCUT thePointcut);

	interface IInvoker extends Comparable<IInvoker> {

		Object invoke(HookParams theParams);

		int getOrder();

		Object getInterceptor();

		default String getHookDescription() {
			return toString();
		}
	}
	/**
	 * A filter hook is a hook that wraps a runnable and allows the interceptor to
	 * run code before or after the runnable before it is executed.
	 * Filter hooks must call the runnable passed in themselves, just like Java Servlet Filters.
	 *
	 * @see IInterceptorBroadcaster#runWithFilterHooks(IPointcut, HookParams, Supplier)
	 */
	@FunctionalInterface
	interface IInterceptorFilterHook<T> extends Function<Supplier<T>, T> {}
}
