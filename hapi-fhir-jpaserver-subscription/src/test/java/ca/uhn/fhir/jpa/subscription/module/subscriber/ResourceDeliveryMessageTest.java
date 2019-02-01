package ca.uhn.fhir.jpa.subscription.module.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ResourceDeliveryMessageTest {

	@Test
	public void testAdditionalProperties() throws IOException {
		ResourceDeliveryMessage msg = new ResourceDeliveryMessage();
		msg.setAdditionalProperty("foo1", "bar");
		msg.setAdditionalProperty("foo2", "baz");
		String encoded = new ObjectMapper().writeValueAsString(msg);

		msg = new ObjectMapper().readValue(encoded, ResourceDeliveryMessage.class);
		assertEquals("bar", msg.getAdditionalProperty("foo1").get());
		assertEquals("baz", msg.getAdditionalProperty("foo2").get());
		assertEquals(false, msg.getAdditionalProperty("foo3").isPresent());
	}

}
