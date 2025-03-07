/*-
 * #%L
 * HAPI FHIR Test Utilities
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
package ca.uhn.fhir.test.utilities;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IBaseInterceptorBroadcaster;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class MockInvoker implements IBaseInterceptorBroadcaster.IInvoker {

	private final Function<HookParams, Object> myFunction;

	private MockInvoker(Consumer<HookParams> theRunnable) {
		this(param -> { theRunnable.accept(param); return null; });
	}

	private MockInvoker(Function<HookParams, Object> theFunction) {
		myFunction = theFunction;
	}

	@Override
	public Object invoke(HookParams theParams) {
		return myFunction.apply(theParams);
	}

	@Override
	public int getOrder() {
		return 0;
	}

	@Override
	public Object getInterceptor() {
		return new Object();
	}

	@Override
	public int compareTo(@Nonnull IBaseInterceptorBroadcaster.IInvoker o) {
		return 0;
	}

	public static List<IBaseInterceptorBroadcaster.IInvoker> list(Consumer<HookParams> theRunnable) {
		return List.of(new MockInvoker(theRunnable));
	}

	public static List<IBaseInterceptorBroadcaster.IInvoker> list(Function<HookParams, Object> theRunnable) {
		return List.of(new MockInvoker(theRunnable));
	}

}
