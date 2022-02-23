package ca.uhn.fhir.jpa.subscription.module;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionCanonicalizer;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage;
import ca.uhn.fhir.util.HapiExtensions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Subscription;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CanonicalSubscriptionTest {
	private static final Logger ourLog = LoggerFactory.getLogger(CanonicalSubscriptionTest.class);

	private static final String TAG_SYSTEM = "https://hapifhir.org/NamingSystem/managing-mdm-system";
	private static final String TAG_VALUE = "HAPI-MDM";
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
	public void testCanonicalSubscriptionRetainsMetaTags() throws IOException {
		SubscriptionCanonicalizer canonicalizer = new SubscriptionCanonicalizer(FhirContext.forR4());
		CanonicalSubscription sub1 = canonicalizer.canonicalize(makeMdmSubscription());
		assertTrue(sub1.getTags().keySet().contains(TAG_SYSTEM));
		assertEquals(sub1.getTags().get(TAG_SYSTEM), TAG_VALUE);
   }

	@Test
	public void emailDetailsEquals() {
		SubscriptionCanonicalizer canonicalizer = new SubscriptionCanonicalizer(FhirContext.forR4());
		CanonicalSubscription sub1 = canonicalizer.canonicalize(makeEmailSubscription());
		CanonicalSubscription sub2 = canonicalizer.canonicalize(makeEmailSubscription());
		assertTrue(sub1.equals(sub2));
	}

	@Test
	public void testSerializeMultiPartitionSubscription(){
		SubscriptionCanonicalizer canonicalizer = new SubscriptionCanonicalizer(FhirContext.forR4());
		Subscription subscription = makeEmailSubscription();
		subscription.addExtension(HapiExtensions.EXTENSION_SUBSCRIPTION_CROSS_PARTITION, new BooleanType().setValue(true));
		CanonicalSubscription canonicalSubscription = canonicalizer.canonicalize(subscription);

		assertEquals(canonicalSubscription.getCrossPartitionEnabled(), true);
	}

	@Test
	public void testSerializeIncorrectMultiPartitionSubscription(){
		SubscriptionCanonicalizer canonicalizer = new SubscriptionCanonicalizer(FhirContext.forR4());
		Subscription subscription = makeEmailSubscription();
		subscription.addExtension(HapiExtensions.EXTENSION_SUBSCRIPTION_CROSS_PARTITION, new StringType().setValue("false"));
		CanonicalSubscription canonicalSubscription = canonicalizer.canonicalize(subscription);

		System.out.print(canonicalSubscription);

		assertEquals(canonicalSubscription.getCrossPartitionEnabled(), false);
	}

	@Test
	public void testSerializeNonMultiPartitionSubscription(){
		SubscriptionCanonicalizer canonicalizer = new SubscriptionCanonicalizer(FhirContext.forR4());
		Subscription subscription = makeEmailSubscription();
		subscription.addExtension(HapiExtensions.EXTENSION_SUBSCRIPTION_CROSS_PARTITION, new BooleanType().setValue(false));
		CanonicalSubscription canonicalSubscription = canonicalizer.canonicalize(subscription);

		System.out.print(canonicalSubscription);

		assertEquals(canonicalSubscription.getCrossPartitionEnabled(), false);
	}

	@Test
	public void testLegacyCanonicalSubscription() throws JsonProcessingException {
		String legacyCanonical = "{\"headers\":{\"retryCount\":0,\"customHeaders\":{}},\"payload\":{\"canonicalSubscription\":{\"extensions\":{\"key1\":[\"VALUE1\"],\"key2\":[\"VALUE2a\",\"VALUE2b\"]},\"sendDeleteMessages\":false},\"partitionId\":{\"allPartitions\":false,\"partitionIds\":[null]}}}";
		ObjectMapper mapper = new ObjectMapper();
		ResourceDeliveryJsonMessage resourceDeliveryMessage = mapper.readValue(legacyCanonical, ResourceDeliveryJsonMessage.class);

		CanonicalSubscription payload = resourceDeliveryMessage.getPayload().getSubscription();

		assertEquals(payload.getCrossPartitionEnabled(), false);
	}

	private Subscription makeEmailSubscription() {
		Subscription retVal = new Subscription();
		Subscription.SubscriptionChannelComponent channel = new Subscription.SubscriptionChannelComponent();
		channel.setType(Subscription.SubscriptionChannelType.EMAIL);
		retVal.setChannel(channel);
		return retVal;
	}
	private Subscription makeMdmSubscription() {
		Subscription retVal = new Subscription();
		Subscription.SubscriptionChannelComponent channel = new Subscription.SubscriptionChannelComponent();
		channel.setType(Subscription.SubscriptionChannelType.MESSAGE);
		retVal.setChannel(channel);
		retVal.getMeta().addTag("https://hapifhir.org/NamingSystem/managing-mdm-system", "HAPI-MDM", "managed by hapi mdm");
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
