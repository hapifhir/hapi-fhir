package ca.uhn.fhir.jpa.subscription.module.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ResourceDeliveryMessageTest {

	@Test
	public void testAdditionalProperties() throws IOException {
		ResourceDeliveryMessage msg = new ResourceDeliveryMessage();
		msg.setAttribute("foo1", "bar");
		msg.setAttribute("foo2", "baz");
		String encoded = new ObjectMapper().writeValueAsString(msg);

		msg = new ObjectMapper().readValue(encoded, ResourceDeliveryMessage.class);
		assertEquals("bar", msg.getAttribute("foo1").get());
		assertEquals("baz", msg.getAttribute("foo2").get());
		assertEquals(false, msg.getAttribute("foo3").isPresent());
	}

}
