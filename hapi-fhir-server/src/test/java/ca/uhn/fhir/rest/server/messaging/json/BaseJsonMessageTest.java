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
package ca.uhn.fhir.rest.server.messaging.json;

import ca.uhn.fhir.rest.server.messaging.IHasPayloadMessageKey;
import jakarta.annotation.Nullable;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.MessageHeaders;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Created by Claude 3.7 Sonnet
class BaseJsonMessageTest {

	@Test
	void testDefaultConstructor() {
		// When
		TestJsonMessage message = new TestJsonMessage();
		
		// Then
		assertNotNull(message.getHeaders());
		assertNotNull(message.getHapiHeaders());
		assertEquals(0, message.getHapiHeaders().getRetryCount());
		assertNull(message.getHapiHeaders().getFirstFailureTimestamp());
		assertNull(message.getHapiHeaders().getLastFailureTimestamp());
	}
	
	@Test
	void testHeaderOperations() {
		// Given
		TestJsonMessage message = new TestJsonMessage();
		HapiMessageHeaders headers = new HapiMessageHeaders();
		headers.setRetryCount(5);
		headers.setFirstFailureTimestamp(1000L);
		headers.setLastFailureTimestamp(2000L);
		
		// When
		message.setHeaders(headers);
		
		// Then
		assertEquals(5, message.getHapiHeaders().getRetryCount());
		assertEquals(1000L, message.getHapiHeaders().getFirstFailureTimestamp());
		assertEquals(2000L, message.getHapiHeaders().getLastFailureTimestamp());
		
		// Check Spring MessageHeaders conversion
		MessageHeaders springHeaders = message.getHeaders();
		assertEquals(5, springHeaders.get(HapiMessageHeaders.RETRY_COUNT_KEY));
		assertEquals(1000L, springHeaders.get(HapiMessageHeaders.FIRST_FAILURE_KEY));
		assertEquals(2000L, springHeaders.get(HapiMessageHeaders.LAST_FAILURE_KEY));
	}
	
	@Test
	void testCustomHeaders() {
		// Given
		TestJsonMessage message = new TestJsonMessage();
		HapiMessageHeaders headers = new HapiMessageHeaders();
		Map<String, Object> customHeaders = headers.getCustomHeaders();
		customHeaders.put("testKey", "testValue");
		
		// When
		message.setHeaders(headers);
		
		// Then
		MessageHeaders springHeaders = message.getHeaders();
		assertEquals("testValue", springHeaders.get("testKey"));
		
		Optional<String> headerValue = message.getHeader("testKey");
		assertTrue(headerValue.isPresent());
		assertEquals("testValue", headerValue.get());
	}
	
	@Test
	void testGetHeaderWithMissingKey() {
		// Given
		TestJsonMessage message = new TestJsonMessage();
		
		// When/Then
		Optional<String> headerValue = message.getHeader("nonExistentKey");
		assertTrue(headerValue.isEmpty());
	}
	
	@Test
	void testGetMessageKeyWithoutPayloadKey() {
		// Given
		TestJsonMessage message = new TestJsonMessage();
		TestPayload payload = new TestPayload();
		message.setPayload(payload);
		
		// When
		String messageKey = message.getMessageKey();
		
		// Then
		assertNotNull(messageKey);
		// Should be a UUID format since we're using the default implementation
		UUID.fromString(messageKey); // This will throw if not a valid UUID
	}
	
	@Test
	void testGetMessageKeyWithPayloadKey() {
		// Given
		TestJsonMessage message = new TestJsonMessage();
		TestPayload payload = new TestPayload();
		payload.setPayloadMessageKey("test-payload-key");
		message.setPayload(payload);
		
		// When
		String messageKey = message.getMessageKey();
		
		// Then
		assertEquals("test-payload-key", messageKey);
	}
	
	@Test
	void testNullHeadersHandling() {
		// Given
		TestJsonMessage message = new TestJsonMessage();
		message.setHeaders(null);
		
		// When
		HapiMessageHeaders headers = message.getHapiHeaders();
		
		// Then
		assertNotNull(headers);
		assertEquals(0, headers.getRetryCount());
	}
	
	/**
	 * Concrete implementation of BaseJsonMessage for testing
	 */
	static class TestJsonMessage extends BaseJsonMessage<TestPayload> {
		private TestPayload myPayload;
		
		@Override
		public TestPayload getPayload() {
			return myPayload;
		}
		
		public void setPayload(TestPayload thePayload) {
			myPayload = thePayload;
		}
	}
	
	/**
	 * Test payload class for BaseJsonMessage
	 */
	static class TestPayload implements IHasPayloadMessageKey {
		private String myPayloadMessageKey;
		
		TestPayload() {
			// Default constructor
		}
		
		public void setPayloadMessageKey(String thePayloadMessageKey) {
			myPayloadMessageKey = thePayloadMessageKey;
		}
		
		@Nullable
		@Override
		public String getPayloadMessageKey() {
			return myPayloadMessageKey;
		}
	}
}
