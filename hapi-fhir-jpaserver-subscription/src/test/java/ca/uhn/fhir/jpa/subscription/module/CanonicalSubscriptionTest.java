package ca.uhn.fhir.jpa.subscription.module;

import ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceDeliveryJsonMessage;
import ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceDeliveryMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class CanonicalSubscriptionTest {

	@Test
	public void testGetChannelExtension() throws IOException {

		HashMap<String, List<String>> inMap = new HashMap<>();
		inMap.put("key1", Lists.newArrayList("VALUE1"));
		inMap.put("key2", Lists.newArrayList("VALUE2a", "VALUE2b"));

		CanonicalSubscription s = new CanonicalSubscription();
		s.setChannelExtensions(inMap);

		CanonicalSubscription s2 = serializeAndDeserialize(s);

		assertThat(s2.getChannelExtension("key1"), Matchers.equalTo("VALUE1"));
		assertThat(s2.getChannelExtension("key2"), Matchers.equalTo("VALUE2a"));
		assertThat(s2.getChannelExtension("key3"), Matchers.nullValue());
		assertTrue(s2.equals(s));
	}

	@Test
	public void testGetChannelExtensions() throws IOException {

		HashMap<String, List<String>> inMap = new HashMap<>();
		inMap.put("key1", Lists.newArrayList("VALUE1"));
		inMap.put("key2", Lists.newArrayList("VALUE2a", "VALUE2b"));

		CanonicalSubscription s = new CanonicalSubscription();
		s.setChannelExtensions(inMap);

		CanonicalSubscription s2 = serializeAndDeserialize(s);

		assertThat(s2.getChannelExtensions("key1"), Matchers.contains("VALUE1"));
		assertThat(s2.getChannelExtensions("key2"), Matchers.contains("VALUE2a", "VALUE2b"));
		assertThat(s2.getChannelExtensions("key3"), Matchers.empty());
		assertTrue(s2.equals(s));
	}

	private CanonicalSubscription serializeAndDeserialize(CanonicalSubscription theSubscription) throws IOException {

		ResourceDeliveryJsonMessage resourceDeliveryMessage = new ResourceDeliveryJsonMessage();
		resourceDeliveryMessage.setPayload(new ResourceDeliveryMessage());
		resourceDeliveryMessage.getPayload().setSubscription(theSubscription);

		ObjectMapper mapper = new ObjectMapper();
		String serialized = mapper.writeValueAsString(resourceDeliveryMessage);
		resourceDeliveryMessage = mapper.readValue(serialized, ResourceDeliveryJsonMessage.class);

		ResourceDeliveryMessage payload = resourceDeliveryMessage.getPayload();
		return payload.getSubscription();
	}
}
