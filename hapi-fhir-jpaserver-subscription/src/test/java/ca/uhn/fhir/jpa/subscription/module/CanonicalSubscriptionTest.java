package ca.uhn.fhir.jpa.subscription.module;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.SubscriptionSettings;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionCanonicalizer;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage;
import ca.uhn.fhir.util.HapiExtensions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import org.assertj.core.util.Lists;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Subscription;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import static ca.uhn.fhir.rest.api.Constants.RESOURCE_PARTITION_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
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

		assertEquals("VALUE1", s.getChannelExtension("key1"));
		assertEquals("VALUE2a", s.getChannelExtension("key2"));
		assertNull(s.getChannelExtension("key3"));
	}

	@Test
	public void testGetChannelExtensions() throws IOException {

		HashMap<String, List<String>> inMap = new HashMap<>();
		inMap.put("key1", Lists.newArrayList("VALUE1"));
		inMap.put("key2", Lists.newArrayList("VALUE2a", "VALUE2b"));

		CanonicalSubscription s = new CanonicalSubscription();
		s.setChannelExtensions(inMap);

		s = serializeAndDeserialize(s);

		assertThat(s.getChannelExtensions("key1")).containsExactly("VALUE1");
		assertThat(s.getChannelExtensions("key2")).containsExactly("VALUE2a", "VALUE2b");
		assertThat(s.getChannelExtensions("key3")).isEmpty();
	}

	@Test
	public void testCanonicalSubscriptionRetainsMetaTags() {
		SubscriptionCanonicalizer canonicalizer = new SubscriptionCanonicalizer(FhirContext.forR4(), new SubscriptionSettings());
		CanonicalSubscription sub1 = canonicalizer.canonicalize(makeMdmSubscription());
		assertThat(sub1.getTags()).containsKey(TAG_SYSTEM);
		assertEquals(sub1.getTags().get(TAG_SYSTEM), TAG_VALUE);
   }

	@Test
	public void emailDetailsEquals() {
		SubscriptionCanonicalizer canonicalizer = new SubscriptionCanonicalizer(FhirContext.forR4(), new SubscriptionSettings());
		CanonicalSubscription sub1 = canonicalizer.canonicalize(makeEmailSubscription());
		CanonicalSubscription sub2 = canonicalizer.canonicalize(makeEmailSubscription());
		assertTrue(sub1.equals(sub2));
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testSubscriptionCrossPartitionEnabled_basedOnGlobalFlagAndExtensionFalse(boolean theIsCrossPartitionEnabled){
		final SubscriptionSettings subscriptionSettings = buildSubscriptionSettings(theIsCrossPartitionEnabled);
		SubscriptionCanonicalizer canonicalizer = new SubscriptionCanonicalizer(FhirContext.forR4(), subscriptionSettings);

		Subscription subscriptionWithExtensionSetToBooleanFalse = makeEmailSubscription();
		subscriptionWithExtensionSetToBooleanFalse.addExtension(HapiExtensions.EXTENSION_SUBSCRIPTION_CROSS_PARTITION, new BooleanType().setValue(false));

		CanonicalSubscription canonicalSubscriptionExtensionSetToBooleanFalse = canonicalizer.canonicalize(subscriptionWithExtensionSetToBooleanFalse);

		assertEquals(canonicalSubscriptionExtensionSetToBooleanFalse.isCrossPartitionEnabled(), false);
	}

	static Stream<Arguments> requestPartitionIds() {
		return Stream.of(
			Arguments.of(null, false),
			Arguments.of(RequestPartitionId.allPartitions(), false),
			Arguments.of(RequestPartitionId.fromPartitionIds(1), false),
			Arguments.of(RequestPartitionId.defaultPartition(), true)
			);
	}

	@ParameterizedTest
	@MethodSource("requestPartitionIds")
	public void testSubscriptionCrossPartitionEnabled_basedOnGlobalFlagAndExtensionAndPartitionId(RequestPartitionId theRequestPartitionId, boolean theExpectedIsCrossPartitionEnabled){
		final boolean globalIsCrossPartitionEnabled = true;   // not required but to help understand what the test is doing.
		SubscriptionCanonicalizer canonicalizer = new SubscriptionCanonicalizer(FhirContext.forR4(), buildSubscriptionSettings(globalIsCrossPartitionEnabled));

		Subscription subscription = makeEmailSubscription();
		subscription.setUserData(RESOURCE_PARTITION_ID,theRequestPartitionId);
		subscription.addExtension(HapiExtensions.EXTENSION_SUBSCRIPTION_CROSS_PARTITION, new BooleanType().setValue(true));

		CanonicalSubscription canonicalSubscription = canonicalizer.canonicalize(subscription);

		System.out.print(canonicalSubscription);

		// for a Subscription to be a cross-partition subscription, 3 things are required:
		// - The Subs need to be created on the default partition
		// - The Subs need to have extension EXTENSION_SUBSCRIPTION_CROSS_PARTITION set to true
		// - Global flag CrossPartitionSubscriptionEnabled needs to be true
		assertThat(canonicalSubscription.isCrossPartitionEnabled()).isEqualTo(theExpectedIsCrossPartitionEnabled);
	}

	@Test
	public void testLegacyCanonicalSubscription() throws JsonProcessingException {
		String legacyCanonical = "{\"headers\":{\"retryCount\":0,\"customHeaders\":{}},\"payload\":{\"canonicalSubscription\":{\"extensions\":{\"key1\":[\"VALUE1\"],\"key2\":[\"VALUE2a\",\"VALUE2b\"]},\"sendDeleteMessages\":false},\"partitionId\":{\"allPartitions\":false,\"partitionIds\":[null]}}}";
		ObjectMapper mapper = new ObjectMapper();
		ResourceDeliveryJsonMessage resourceDeliveryMessage = mapper.readValue(legacyCanonical, ResourceDeliveryJsonMessage.class);

		CanonicalSubscription payload = resourceDeliveryMessage.getPayload().getSubscription();

		assertFalse(payload.isCrossPartitionEnabled());
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

	@Nonnull
	private static SubscriptionSettings buildSubscriptionSettings(boolean theIsCrossPartitionEnabled) {
		final SubscriptionSettings subscriptionSettings = new SubscriptionSettings();
		subscriptionSettings.setCrossPartitionSubscriptionEnabled(theIsCrossPartitionEnabled);
		return subscriptionSettings;
	}
}
