/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
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
package ca.uhn.fhir.jpa.test;

import ca.uhn.fhir.interceptor.api.IInterceptorService;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This test extension makes sure that tests don't leave any interceptors
 * registered that weren't there before they started.
 */
public class PreventDanglingInterceptorsExtension implements BeforeEachCallback, AfterEachCallback {

	private static final Logger ourLog = LoggerFactory.getLogger(PreventDanglingInterceptorsExtension.class);
	private final Supplier<IInterceptorService> myInterceptorServiceSuplier;
	private List<Object> myBeforeInterceptors;

	public PreventDanglingInterceptorsExtension(Supplier<IInterceptorService> theInterceptorServiceSuplier) {
		myInterceptorServiceSuplier = theInterceptorServiceSuplier;
	}

	@Override
	public void beforeEach(ExtensionContext theExtensionContext) throws Exception {
		myBeforeInterceptors = myInterceptorServiceSuplier.get().getAllRegisteredInterceptors();

		ourLog.info("Registered interceptors:\n * " + myBeforeInterceptors.stream().map(t -> t.toString()).collect(Collectors.joining("\n * ")));
	}

	@Override
	public void afterEach(ExtensionContext theExtensionContext) throws Exception {
		List<Object> afterInterceptors = myInterceptorServiceSuplier.get().getAllRegisteredInterceptors();
		Map<Object, Object> delta = new IdentityHashMap<>();
		afterInterceptors.forEach(t -> delta.put(t, t));
		myBeforeInterceptors.forEach(t -> delta.remove(t));
		delta.keySet().forEach(t->myInterceptorServiceSuplier.get().unregisterInterceptor(t));
		assertThat(delta.isEmpty()).as(() -> "Test added interceptor(s) and did not clean them up:\n * " + delta.keySet().stream().map(t -> t.toString()).collect(Collectors.joining("\n * "))).isTrue();

	}
}
