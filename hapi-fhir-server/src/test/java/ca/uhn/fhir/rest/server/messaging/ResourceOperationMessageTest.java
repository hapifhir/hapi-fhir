package ca.uhn.fhir.rest.server.messaging;

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.messaging.json.ResourceOperationJsonMessage;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;

import static ca.uhn.fhir.rest.server.messaging.json.HapiMessageHeaders.FIRST_FAILURE_KEY;
import static ca.uhn.fhir.rest.server.messaging.json.HapiMessageHeaders.LAST_FAILURE_KEY;
import static ca.uhn.fhir.rest.server.messaging.json.HapiMessageHeaders.RETRY_COUNT_KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ResourceOperationMessageTest {

	@Test
	public void testSerializationAndDeserializationOfResourceModifiedMessage() throws JacksonException {
		ResourceOperationJsonMessage jsonMessage = new ResourceOperationJsonMessage();
		ResourceOperationMessage payload = new ResourceOperationMessage();
		payload.setMediaType(Constants.CT_FHIR_JSON_NEW);
		jsonMessage.setPayload(payload);
		JsonMapper mapper = new JsonMapper();
		String serialized = mapper.writeValueAsString(jsonMessage);
		jsonMessage = mapper.readValue(serialized, ResourceOperationJsonMessage.class);

		assertEquals(0, jsonMessage.getHapiHeaders().getRetryCount());
		assertNull(jsonMessage.getHapiHeaders().getFirstFailureTimestamp());
		assertNull(jsonMessage.getHapiHeaders().getLastFailureTimestamp());

		assertEquals(0, jsonMessage.getHeaders().get(RETRY_COUNT_KEY));
		assertNull(jsonMessage.getHeaders().get(FIRST_FAILURE_KEY));
		assertNull(jsonMessage.getHeaders().get(LAST_FAILURE_KEY));
	}
}
