package ca.uhn.fhir.jpa.subscription.module;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionCanonicalizer;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.hl7.fhir.r4.model.Subscription;
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

		s = serializeAndDeserialize(s);

		assertThat(s.getChannelExtension("key1"), Matchers.equalTo("VALUE1"));
		assertThat(s.getChannelExtension("key2"), Matchers.equalTo("VALUE2a"));
		assertThat(s.getChannelExtension("key3"), Matchers.nullValue());
	}

	@Test
	public void testGetChannelExtensions() throws IOException {

		HashMap<String, List<String>> inMap = new HashMap<>();
		inMap.put("key1", Lists.newArrayList("VALUE1"));
		inMap.put("key2", Lists.newArrayList("VALUE2a", "VALUE2b"));

		CanonicalSubscription s = new CanonicalSubscription();
		s.setChannelExtensions(inMap);

		s = serializeAndDeserialize(s);

		assertThat(s.getChannelExtensions("key1"), Matchers.contains("VALUE1"));
		assertThat(s.getChannelExtensions("key2"), Matchers.contains("VALUE2a", "VALUE2b"));
		assertThat(s.getChannelExtensions("key3"), Matchers.empty());
	}

	@Test
	public void emailDetailsEquals() {
		SubscriptionCanonicalizer canonicalizer = new SubscriptionCanonicalizer(FhirContext.forR4());
		CanonicalSubscription sub1 = canonicalizer.canonicalize(makeEmailSubscription());
		CanonicalSubscription sub2 = canonicalizer.canonicalize(makeEmailSubscription());
		assertTrue(sub1.equals(sub2));
	}

	private Subscription makeEmailSubscription() {
		Subscription retVal = new Subscription();
		Subscription.SubscriptionChannelComponent channel = new Subscription.SubscriptionChannelComponent();
		channel.setType(Subscription.SubscriptionChannelType.EMAIL);
		retVal.setChannel(channel);
		return retVal;
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
