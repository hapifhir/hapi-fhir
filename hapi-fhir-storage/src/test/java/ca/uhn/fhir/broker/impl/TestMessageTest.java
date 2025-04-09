package ca.uhn.fhir.broker.impl;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.rest.server.messaging.BaseResourceMessage;
import org.hl7.fhir.r4.model.IdType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class TestMessageTest {

	@Test
	void constructor_withKeyAndValue_setsKeyAndValue() {
		// Given
		String key = "test-key";
		String payload = "test-payload";
		
		// When
		TestMessage<String> message = new TestMessage<>(key, payload);
		
		// Then
		assertThat(message.getMessageKey()).isEqualTo(key);
		assertThat(message.getPayload()).isEqualTo(payload);
	}
	
	@Test
	void constructor_withNullKey_setsHasKeyToFalse() {
		// Given
		String payload = "test-payload";
		
		// When
		TestMessage<String> message = new TestMessage<>(null, payload);
		
		// Then
		assertThat(message.getMessageKey()).isNull();
		assertThat(message.getPayload()).isEqualTo(payload);
	}
	
	@Test
	void constructor_withBaseJsonMessagePayload_extractsKeyFromPayload() {
		// Given
		ResourceModifiedMessage resourceMessage = new ResourceModifiedMessage();
		resourceMessage.setId(new IdType("Resource/123"));
		resourceMessage.setOperationType(BaseResourceMessage.OperationTypeEnum.CREATE);
		ResourceModifiedJsonMessage jsonMessage = new ResourceModifiedJsonMessage(resourceMessage);
		
		// When
		TestMessage<ResourceModifiedJsonMessage> message = new TestMessage<>(jsonMessage);
		
		// Then
		assertThat(message.getMessageKey()).isEqualTo(jsonMessage.getMessageKey());
		assertThat(message.getPayload()).isSameAs(jsonMessage);
	}
	
	@Test
	void constructor_withNonBaseJsonMessagePayload_setsKeyToNull() {
		// Given
		String payload = "test-payload";
		
		// When
		TestMessage<String> message = new TestMessage<>(payload);
		
		// Then
		assertThat(message.getMessageKey()).isNull();
		assertThat(message.getPayload()).isEqualTo(payload);
	}
	
	@Test
	void getHeaders_returnsEmptyMapInitially() {
		// Given
		TestMessage<String> message = new TestMessage<>("key", "value");
		
		// Then
		assertThat(message.getHeaders()).isEmpty();
	}
	
	@Test
	void getHeader_withNonExistentHeader_returnsEmptyOptional() {
		// Given
		TestMessage<String> message = new TestMessage<>("key", "value");
		
		// When
		Optional<String> header = message.getHeader("non-existent");
		
		// Then
		assertThat(header).isEmpty();
	}
	
	@Test
	void getHeader_withStringHeaderValue_returnsHeaderValue() {
		// Given
		TestMessage<String> message = new TestMessage<>("key", "value");
		String headerName = "test-header";
		String headerValue = "header-value";
		message.getHeaders().put(headerName, headerValue);
		
		// When
		Optional<String> header = message.getHeader(headerName);
		
		// Then
		assertThat(header).isPresent();
		assertThat(header.get()).isEqualTo(headerValue);
	}
	
	@Test
	void getHeader_withNonStringHeaderValue_returnsHeaderValue() {
		// Given
		TestMessage<String> message = new TestMessage<>("key", "value");
		String headerName = "count-header";
		Integer headerValue = 42;
		message.getHeaders().put(headerName, headerValue);
		
		// When
		Optional<Integer> header = message.getHeader(headerName);
		
		// Then
		assertThat(header).isPresent();
		assertThat(header.get()).isEqualTo(headerValue);
	}
}
