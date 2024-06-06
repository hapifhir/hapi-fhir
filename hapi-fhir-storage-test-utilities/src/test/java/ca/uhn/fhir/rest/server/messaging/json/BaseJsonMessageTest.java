package ca.uhn.fhir.rest.server.messaging.json;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.messaging.BaseResourceMessage;
import ca.uhn.fhir.rest.server.messaging.ResourceOperationMessage;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BaseJsonMessageTest {
	FhirContext ourFhirContext = FhirContext.forR4Cached();
	static final String RESOURCE_ID = "Patient/123";
	static final String MESSAGE_KEY = "MY_TEST_KEY";

	@Test
	void test_byDefaultMessageKeyIsResourceId_for_ResourceOperationJsonMessage(){
		ResourceOperationJsonMessage message = new ResourceOperationJsonMessage();
		IBaseResource patient = buildPatient();
		ResourceOperationMessage payload = new ResourceOperationMessage(ourFhirContext, patient, ResourceOperationMessage.OperationTypeEnum.CREATE);
		message.setPayload(payload);
		assertNull(message.getMessageKey());
		assertEquals(RESOURCE_ID, message.getMessageKeyOrDefault());
	}

	@Test
	void test_messageKeyIsMessageKey_whenSpecificallySet_for_ResourceOperationJsonMessage(){
		ResourceOperationJsonMessage message = new ResourceOperationJsonMessage();
		IBaseResource patient = buildPatient();
		ResourceOperationMessage payload = new ResourceOperationMessage(ourFhirContext, patient, ResourceOperationMessage.OperationTypeEnum.CREATE);
		payload.setMessageKey(MESSAGE_KEY);
		message.setPayload(payload);
		assertEquals(MESSAGE_KEY, message.getMessageKey());
		assertEquals(MESSAGE_KEY, message.getMessageKeyOrDefault());
	}

	@Test
	void test_byDefaultMessageKeyIsResourceId_for_ResourceDeliveryJsonMessage() {
		ResourceDeliveryJsonMessage message = new ResourceDeliveryJsonMessage();
		IBaseResource patient = buildPatient();
		ResourceDeliveryMessage payload = new ResourceDeliveryMessage();
		payload.setPayload(ourFhirContext, patient, EncodingEnum.JSON);
		message.setPayload(payload);
		assertNull(message.getMessageKey());
		assertEquals(RESOURCE_ID, message.getMessageKeyOrDefault());
	}

	@Test
	void test_messageKeyIsMessageKey_whenSpecificallySet_MdmResourceDeliveryJsonMessage() {
		ResourceDeliveryJsonMessage message = new ResourceDeliveryJsonMessage();
		IBaseResource patient = buildPatient();
		ResourceDeliveryMessage payload = new ResourceDeliveryMessage();
		payload.setPayload(ourFhirContext, patient, EncodingEnum.JSON);
		payload.setMessageKey(MESSAGE_KEY);
		message.setPayload(payload);
		assertEquals(MESSAGE_KEY, message.getMessageKey());
		assertEquals(MESSAGE_KEY, message.getMessageKeyOrDefault());
	}

	@Test
	void test_byDefaultMessageKeyIsResourceId_for_ResourceModifiedJsonMessage() {
		ResourceModifiedJsonMessage message = new ResourceModifiedJsonMessage();
		IBaseResource patient = buildPatient();
		ResourceModifiedMessage payload = new ResourceModifiedMessage(ourFhirContext, patient, BaseResourceMessage.OperationTypeEnum.CREATE);
		message.setPayload(payload);
		assertNull(message.getMessageKey());
		assertEquals(RESOURCE_ID, message.getMessageKeyOrDefault());
	}

	@Test
	void test_messageKeyIsMessageKey_whenSpecificallySet_for_ResourceModifiedJsonMessage() {
		ResourceModifiedJsonMessage message = new ResourceModifiedJsonMessage();
		IBaseResource patient = buildPatient();
		ResourceModifiedMessage payload = new ResourceModifiedMessage(ourFhirContext, patient, BaseResourceMessage.OperationTypeEnum.CREATE);
		payload.setMessageKey(MESSAGE_KEY);
		message.setPayload(payload);
		assertEquals(MESSAGE_KEY, message.getMessageKey());
		assertEquals(MESSAGE_KEY, message.getMessageKeyOrDefault());
	}

	@Test
	void test_resourceModifiedJsonMessage_getRetryCountOnNullHeaders_willReturnZero() {
		// Given
		ResourceModifiedJsonMessage message = new ResourceModifiedJsonMessage();
		message.setHeaders(null);
		// When
		HapiMessageHeaders headers = message.getHapiHeaders();
		// Then
		assertEquals(0, headers.getRetryCount());
	}

	@Test
	void test_getMessageKey_whenSetMessageKeyIsNotInvoked_willReturnNull(){
		// given
		IBaseResource patient = buildPatient();
		// when
		ResourceModifiedMessage payload = new ResourceModifiedMessage(ourFhirContext, patient, BaseResourceMessage.OperationTypeEnum.CREATE);
		// then
		assertNull(payload.getMessageKey());
	}

	@Nonnull
	private static IBaseResource buildPatient() {
		IBaseResource patient = new Patient();
		patient.setId(new IdDt("Patient", RESOURCE_ID, "1"));
		return patient;
	}
}
