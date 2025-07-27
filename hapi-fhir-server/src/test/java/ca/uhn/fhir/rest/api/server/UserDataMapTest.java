/*-
 * #%L
 * HAPI FHIR - Server Framework
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
package ca.uhn.fhir.rest.api.server;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
		@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
		UserDataMap userDataMap = new UserDataMap();
		
		// When/Then - should not throw NPE unlike ConcurrentHashMap
		assertThat(userDataMap.containsValue(null)).isFalse();
	}

	@Test
	void testPutAllWithNullValues_shouldHandleNullsConsistently() {
		// Given
		UserDataMap userDataMap = new UserDataMap();
		Map<String, String> inputMap = new HashMap<>();
		inputMap.put("key1", "value1");
		inputMap.put("key2", null);
		inputMap.put("key3", "value3");
		
		// When
		userDataMap.putAll(inputMap);
		
		// Then - null values should be handled consistently with put() behavior
		assertThat(userDataMap.get("key1")).isEqualTo("value1");
		assertThat(userDataMap.get("key2")).isNull();
		assertThat(userDataMap.containsKey("key2")).isFalse(); // Should be removed like put(key, null)
		assertThat(userDataMap.get("key3")).isEqualTo("value3");
		assertThat(userDataMap.size()).isEqualTo(2); // Only non-null entries
	}

	@Test
	void testThreadSafety_concurrentPutAndGet() throws InterruptedException {
		// Given
		UserDataMap userDataMap = new UserDataMap();
		int numThreads = 10;
		int operationsPerThread = 1000;

		ExecutorService executor = Executors.newFixedThreadPool(numThreads);
		try {
			CountDownLatch latch = new CountDownLatch(numThreads);
			AtomicInteger successCount = new AtomicInteger(0);

			// When - multiple threads perform concurrent operations
			for (int i = 0; i < numThreads; i++) {
				final int threadId = i;
				executor.submit(() -> {
					try {
						for (int j = 0; j < operationsPerThread; j++) {
							String key = "thread-" + threadId + "-key-" + j;
							String value = "value-" + j;

							userDataMap.put(key, value);
							String retrieved = (String) userDataMap.get(key);

							if (value.equals(retrieved)) {
								successCount.incrementAndGet();
							}
						}
					} finally {
						latch.countDown();
					}
				});
			}

			// Then - all operations should complete without data corruption
			assertThat(latch.await(30, TimeUnit.SECONDS)).isTrue();
			assertThat(successCount.get()).isEqualTo(numThreads * operationsPerThread);
			assertThat(userDataMap.size()).isEqualTo(numThreads * operationsPerThread);
		} finally {
			executor.shutdown();
		}
	}

	@Test
	void testConcurrentModification_iteratorShouldNotFailFast() {
		// Given
		UserDataMap userDataMap = new UserDataMap();
		userDataMap.put("key1", "value1");
		userDataMap.put("key2", "value2");
		userDataMap.put("key3", "value3");
		
		// When/Then - Unlike HashMap, ConcurrentHashMap-based map should not throw ConcurrentModificationException
		// This should complete without throwing an exception
		//noinspection Java8MapApi
		for (Object key : userDataMap.keySet()) {
			userDataMap.put(key, "newValue"); // Modify during iteration
		}
		// If we get here, no ConcurrentModificationException was thrown
		assertThat(userDataMap.size()).isEqualTo(3); // Should have added at least one new key
	}

	@Test
	void testHashMapVsConcurrentHashMapBehavior_nullValueHandling() {
		// setup
		UserDataMap userDataMap = new UserDataMap();
		Map<String, String> regularHashMap = new HashMap<>();
		
		// execute
		userDataMap.put("key", null);
		regularHashMap.put("key", null);

		// verify

		// Note our wrapper does not behave identically to HashMap, but that is what we want in this case.
		assertTrue(regularHashMap.containsKey("key"));
		assertFalse(userDataMap.containsKey("key"));
		assertEquals(1, regularHashMap.size());
		assertEquals(0, userDataMap.size());

		// This is the behaviour we care about and it is the same
		assertThat(userDataMap.get("key")).isEqualTo(regularHashMap.get("key")); // Should both return null
	}

	@Test
	void testConcurrentPutNullAndContainsKey() throws InterruptedException {
		// Given
		UserDataMap userDataMap = new UserDataMap();
		int numThreads = 5;

		ExecutorService executor = Executors.newFixedThreadPool(numThreads);
		try {
			CountDownLatch latch = new CountDownLatch(numThreads);

			// When - multiple threads put null and check containsKey
			for (int i = 0; i < numThreads; i++) {
				final int threadId = i;
				executor.submit(() -> {
					try {
						String key = "nullKey-" + threadId;
						userDataMap.put(key, "value");
						assertThat(userDataMap.containsKey(key)).isTrue();

						userDataMap.put(key, null); // Should remove the key
						assertThat(userDataMap.containsKey(key)).isFalse(); // Should be false after null put
					} finally {
						latch.countDown();
					}
				});
			}

			// Then
			assertThat(latch.await(10, TimeUnit.SECONDS)).isTrue();
			assertThat(userDataMap.size()).isEqualTo(0); // All entries should be removed

		} finally {
			executor.shutdown();
		}
	}

}
