package ca.uhn.fhir.jpa.subscription.module.subscriber;

import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResourceDeliveryMessageTest {

	@Test
	public void testAdditionalProperties() throws IOException {
		ResourceDeliveryMessage msg = new ResourceDeliveryMessage();
		msg.setAttribute("foo1", "bar");
		msg.setAttribute("foo2", "baz");
		String encoded = new ObjectMapper().writeValueAsString(msg);

		msg = new ObjectMapper().readValue(encoded, ResourceDeliveryMessage.class);
		assertThat(msg.getAttribute("foo1")).contains("bar");
		assertThat(msg.getAttribute("foo2")).contains("baz");
		assertEquals(false, msg.getAttribute("foo3").isPresent());
	}

}
