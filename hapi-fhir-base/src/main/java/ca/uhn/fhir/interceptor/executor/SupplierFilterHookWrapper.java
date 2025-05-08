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
package ca.uhn.fhir.interceptor.executor;

import ca.uhn.fhir.interceptor.api.IBaseInterceptorBroadcaster.IInterceptorFilterHook;

import java.util.function.Supplier;

/**
 * Wraps a Supplier with advice from a filter hook.
 */
public class SupplierFilterHookWrapper<T> implements Supplier<T> {
	private final IInterceptorFilterHook myAdvice;
	private final Supplier<T> myTarget;
	private final Supplier<String> myMessageSupplier;

	public SupplierFilterHookWrapper(
			Supplier<T> theTarget, IInterceptorFilterHook theAdvice, Supplier<String> theCauseDescriptionSupplier) {
		myAdvice = theAdvice;
		myTarget = theTarget;
		myMessageSupplier = theCauseDescriptionSupplier;
	}

	@Override
	public T get() {
		SupplierRunnable<T> trackingSupplierWrapper = new SupplierRunnable<>(myTarget);

		myAdvice.wrapCall(trackingSupplierWrapper);

		if (!trackingSupplierWrapper.wasExecuted()) {
			throw new IllegalStateException(
					"Supplier was not executed in filter produced by " + myMessageSupplier.get());
		}

		return trackingSupplierWrapper.getResult();
	}

	/**
	 * Adapt a Supplier to Runnable.
	 * We use a Runnable for a simpler api for callers.
	 * @param <T>
	 */
	static class SupplierRunnable<T> implements Runnable {
		private final Supplier<T> myTarget;
		private boolean myExecutedFlag = false;
		private T myResult = null;

		SupplierRunnable(Supplier<T> theTarget) {
			myTarget = theTarget;
		}

		public void run() {
			myExecutedFlag = true;
			myResult = myTarget.get();
		}

		public boolean wasExecuted() {
			return myExecutedFlag;
		}

		public T getResult() {
			return myResult;
		}
	}
}
