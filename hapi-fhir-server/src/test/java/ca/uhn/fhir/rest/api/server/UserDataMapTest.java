/*-
 * #%L
 * HAPI FHIR - Server Framework
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
package ca.uhn.fhir.rest.api.server;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// Created by Claude Sonnet 4
class UserDataMapTest {


	@Test
	void testPutWithNullValue_shouldRemoveEntry() {
		// Given
		UserDataMap userDataMap = new UserDataMap();
		userDataMap.put("key", "value");
		assertThat(userDataMap.containsKey("key")).isTrue();
		
		// When - putting null value should remove the entry
		userDataMap.put("key", null);
		
		// Then - entry should be removed
		assertThat(userDataMap.get("key")).isNull();
		assertThat(userDataMap.containsKey("key")).isFalse();
	}


	@Test
	void testContainsValueWithNull_shouldNotThrowException() {
		// Given
		UserDataMap userDataMap = new UserDataMap();
		
		// When/Then - should not throw NPE unlike ConcurrentHashMap
		assertThat(userDataMap.containsValue(null)).isFalse();
	}


}