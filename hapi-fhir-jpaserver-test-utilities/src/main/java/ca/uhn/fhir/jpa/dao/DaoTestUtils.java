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
package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public final class DaoTestUtils {
	private static final Logger ourLog = LoggerFactory.getLogger(DaoTestUtils.class);

	private DaoTestUtils() {}

	public static void assertConflictException(ResourceVersionConflictException e) {
		assertThat(e.getMessage())
				.matches(
						Msg.code(550) + Msg.code(515)
								+ "Unable to delete [a-zA-Z]+/[0-9]+ because at least one resource has a reference to this resource. First reference found was resource [a-zA-Z]+/[0-9]+ in path [a-zA-Z]+.[a-zA-Z]+");
	}

	public static void logAllInterceptors(IInterceptorService theInterceptorRegistry) {
		List<Object> allInterceptors = theInterceptorRegistry.getAllRegisteredInterceptors();
		String interceptorList = allInterceptors.stream()
				.map(t -> t.getClass().toString())
				.sorted()
				.collect(Collectors.joining("\n * "));
		ourLog.info("Registered interceptors:\n * {}", interceptorList);
	}
}
