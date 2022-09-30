package ca.uhn.fhir.rest.server.messaging.json;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.messaging.BaseResourceMessage;
import ca.uhn.fhir.rest.server.messaging.ResourceOperationMessage;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BaseJsonMessageTest {
	FhirContext ourFhirContext = FhirContext.forR4Cached();
	static final String RESOURCE_ID = "Patient/123";

	@Test
	void test_messageKeyIsResourceId_ResourceOperationJsonMessage() {
		ResourceOperationJsonMessage message = new ResourceOperationJsonMessage();
		IBaseResource patient = buildPatient();
		ResourceOperationMessage payload = new ResourceOperationMessage(ourFhirContext, patient, ResourceOperationMessage.OperationTypeEnum.CREATE);
		message.setPayload(payload);
		assertEquals(RESOURCE_ID, message.getMessageKeyOrNull());
	}

	@Nonnull
	private static IBaseResource buildPatient() {
		IBaseResource patient = new Patient();
		patient.setId(RESOURCE_ID);
		return patient;
	}

	@Test
	void test_messageKeyIsResourceId_ResourceDeliveryJsonMessage() {
		ResourceDeliveryJsonMessage message = new ResourceDeliveryJsonMessage();
		IBaseResource patient = buildPatient();
		ResourceDeliveryMessage payload = new ResourceDeliveryMessage();
		payload.setPayload(ourFhirContext, patient, EncodingEnum.JSON);
		message.setPayload(payload);
		assertEquals(RESOURCE_ID, message.getMessageKeyOrNull());
	}

	@Test
	void test_messageKeyIsResourceId_ResourceModifiedJsonMessage() {
		ResourceModifiedJsonMessage message = new ResourceModifiedJsonMessage();
		IBaseResource patient = buildPatient();
		ResourceModifiedMessage payload = new ResourceModifiedMessage(ourFhirContext, patient, BaseResourceMessage.OperationTypeEnum.CREATE);
		message.setPayload(payload);
		assertEquals(RESOURCE_ID, message.getMessageKeyOrNull());
	}
}
