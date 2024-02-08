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
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Nonnull;

import static org.assertj.core.api.Assertions.assertThat;

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
		assertThat(message.getMessageKey()).isNull();
		assertThat(message.getMessageKeyOrDefault()).isEqualTo(RESOURCE_ID);
	}

	@Test
	void test_messageKeyIsMessageKey_whenSpecificallySet_for_ResourceOperationJsonMessage(){
		ResourceOperationJsonMessage message = new ResourceOperationJsonMessage();
		IBaseResource patient = buildPatient();
		ResourceOperationMessage payload = new ResourceOperationMessage(ourFhirContext, patient, ResourceOperationMessage.OperationTypeEnum.CREATE);
		payload.setMessageKey(MESSAGE_KEY);
		message.setPayload(payload);
		assertThat(message.getMessageKey()).isEqualTo(MESSAGE_KEY);
		assertThat(message.getMessageKeyOrDefault()).isEqualTo(MESSAGE_KEY);
	}

	@Test
	void test_byDefaultMessageKeyIsResourceId_for_ResourceDeliveryJsonMessage() {
		ResourceDeliveryJsonMessage message = new ResourceDeliveryJsonMessage();
		IBaseResource patient = buildPatient();
		ResourceDeliveryMessage payload = new ResourceDeliveryMessage();
		payload.setPayload(ourFhirContext, patient, EncodingEnum.JSON);
		message.setPayload(payload);
		assertThat(message.getMessageKey()).isNull();
		assertThat(message.getMessageKeyOrDefault()).isEqualTo(RESOURCE_ID);
	}

	@Test
	void test_messageKeyIsMessageKey_whenSpecificallySet_MdmResourceDeliveryJsonMessage() {
		ResourceDeliveryJsonMessage message = new ResourceDeliveryJsonMessage();
		IBaseResource patient = buildPatient();
		ResourceDeliveryMessage payload = new ResourceDeliveryMessage();
		payload.setPayload(ourFhirContext, patient, EncodingEnum.JSON);
		payload.setMessageKey(MESSAGE_KEY);
		message.setPayload(payload);
		assertThat(message.getMessageKey()).isEqualTo(MESSAGE_KEY);
		assertThat(message.getMessageKeyOrDefault()).isEqualTo(MESSAGE_KEY);
	}

	@Test
	void test_byDefaultMessageKeyIsResourceId_for_ResourceModifiedJsonMessage() {
		ResourceModifiedJsonMessage message = new ResourceModifiedJsonMessage();
		IBaseResource patient = buildPatient();
		ResourceModifiedMessage payload = new ResourceModifiedMessage(ourFhirContext, patient, BaseResourceMessage.OperationTypeEnum.CREATE);
		message.setPayload(payload);
		assertThat(message.getMessageKey()).isNull();
		assertThat(message.getMessageKeyOrDefault()).isEqualTo(RESOURCE_ID);
	}

	@Test
	void test_messageKeyIsMessageKey_whenSpecificallySet_for_ResourceModifiedJsonMessage() {
		ResourceModifiedJsonMessage message = new ResourceModifiedJsonMessage();
		IBaseResource patient = buildPatient();
		ResourceModifiedMessage payload = new ResourceModifiedMessage(ourFhirContext, patient, BaseResourceMessage.OperationTypeEnum.CREATE);
		payload.setMessageKey(MESSAGE_KEY);
		message.setPayload(payload);
		assertThat(message.getMessageKey()).isEqualTo(MESSAGE_KEY);
		assertThat(message.getMessageKeyOrDefault()).isEqualTo(MESSAGE_KEY);
	}

	@Test
	void test_resourceModifiedJsonMessage_getRetryCountOnNullHeaders_willReturnZero() {
		// Given
		ResourceModifiedJsonMessage message = new ResourceModifiedJsonMessage();
		message.setHeaders(null);
		// When
		HapiMessageHeaders headers = message.getHapiHeaders();
		// Then
		assertThat(headers.getRetryCount()).isEqualTo(0);
	}

	@Test
	void test_getMessageKey_whenSetMessageKeyIsNotInvoked_willReturnNull(){
		// given
		IBaseResource patient = buildPatient();
		// when
		ResourceModifiedMessage payload = new ResourceModifiedMessage(ourFhirContext, patient, BaseResourceMessage.OperationTypeEnum.CREATE);
		// then
		assertThat(payload.getMessageKey()).isNull();
	}

	@Nonnull
	private static IBaseResource buildPatient() {
		IBaseResource patient = new Patient();
		patient.setId(new IdDt("Patient", RESOURCE_ID, "1"));
		return patient;
	}
}
