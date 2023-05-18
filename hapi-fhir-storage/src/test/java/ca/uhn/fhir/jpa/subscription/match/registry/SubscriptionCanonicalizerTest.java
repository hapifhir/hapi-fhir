package ca.uhn.fhir.jpa.subscription.match.registry;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscriptionChannelType;
import ca.uhn.fhir.jpa.subscription.model.CanonicalTopicSubscriptionFilter;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.primitive.BooleanDt;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.PositiveIntType;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Subscription;
import org.hl7.fhir.r4.model.UnsignedIntType;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.Enumerations;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static ca.uhn.fhir.rest.api.Constants.CT_FHIR_JSON_NEW;
import static ca.uhn.fhir.util.HapiExtensions.EX_SEND_DELETE_MESSAGES;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SubscriptionCanonicalizerTest {

	private static final String TEST_TOPIC = "http://test.topic";
	private static final String TEST_FILTER1 = "Encounter?patient=Patient/123";
	private static final String TEST_FILTER2 = "Encounter?status=finished";
	private static final String TEST_ENDPOINT = "http://rest.endpoint/path";
	public static final String TEST_HEADER1 = "X-Foo: FOO";
	public static final String TEST_HEADER2 = "X-Bar: BAR";
	FhirContext r4Context = FhirContext.forR4();

	private final SubscriptionCanonicalizer testedSC = new SubscriptionCanonicalizer(r4Context);

	@Test
	void testCanonicalizeR4SendDeleteMessagesSetsExtensionValueNotPresent() {
		Subscription subscription = new Subscription();

		CanonicalSubscription canonicalSubscription = testedSC.canonicalize(subscription);

		assertFalse(canonicalSubscription.getSendDeleteMessages());
	}

	@Test
	void testCanonicalizeR4SubscriptionWithMultipleTagsHavingSameSystem() {
		Subscription subscription = new Subscription();
		subscription.getMeta().addTag("http://foo", "blah", "blah");
		subscription.getMeta().addTag("http://foo", "baz", "baz");

		CanonicalSubscription canonicalSubscription = testedSC.canonicalize(subscription);

		assertEquals("baz", canonicalSubscription.getTags().get("http://foo"));
	}

	@Test
	void testCanonicalizeR4SendDeleteMessagesSetsExtensionValue() {
		// setup
		Subscription subscription = new Subscription();
		Extension sendDeleteMessagesExtension = new Extension()
			.setUrl(EX_SEND_DELETE_MESSAGES)
			.setValue(new BooleanType(true));
		subscription.getChannel().addExtension(sendDeleteMessagesExtension);

		// execute
		CanonicalSubscription canonicalSubscription = testedSC.canonicalize(subscription);

		// verify
		assertTrue(canonicalSubscription.getSendDeleteMessages());
	}

	@Test
	public void testCanonicalizeDstu2SendDeleteMessages() {
		//setup
		SubscriptionCanonicalizer dstu2Canonicalizer = new SubscriptionCanonicalizer(FhirContext.forDstu2Cached());
		ca.uhn.fhir.model.dstu2.resource.Subscription dstu2Sub = new ca.uhn.fhir.model.dstu2.resource.Subscription();
		ExtensionDt extensionDt = new ExtensionDt();
		extensionDt.setUrl(EX_SEND_DELETE_MESSAGES);
		extensionDt.setValue(new BooleanDt(true));
		dstu2Sub.getChannel().addUndeclaredExtension(extensionDt);

		// execute
		CanonicalSubscription canonicalize = dstu2Canonicalizer.canonicalize(dstu2Sub);

		// verify
		assertTrue(canonicalize.getSendDeleteMessages());
	}

	@Test
	public void testR5() {
		// setup
		SubscriptionCanonicalizer r5Canonicalizer = new SubscriptionCanonicalizer(FhirContext.forR5Cached());
		org.hl7.fhir.r5.model.Subscription subscription = new org.hl7.fhir.r5.model.Subscription();

		subscription.setStatus(Enumerations.SubscriptionStatusCodes.ACTIVE);
		subscription.setContentType(CT_FHIR_JSON_NEW);
		// WIP STR5 support different content types
		subscription.setContent(org.hl7.fhir.r5.model.Subscription.SubscriptionPayloadContent.FULLRESOURCE);
		subscription.setEndpoint("http://foo");
		subscription.setTopic(TEST_TOPIC);
		Coding channelType = new Coding().setSystem("http://terminology.hl7.org/CodeSystem/subscription-channel-type").setCode("rest-hook");
		subscription.setChannelType(channelType);
		subscription.addFilterBy(buildFilter("Observation", "param1", "value1"));
		subscription.addFilterBy(buildFilter("CarePlan", "param2", "value2"));
		subscription.setHeartbeatPeriod(123);
		subscription.setMaxCount(456);

		// execute
		CanonicalSubscription canonical = r5Canonicalizer.canonicalize(subscription);

		// verify
		assertEquals(Subscription.SubscriptionStatus.ACTIVE, canonical.getStatus());
		assertEquals(CT_FHIR_JSON_NEW, canonical.getContentType());
		assertEquals(org.hl7.fhir.r5.model.Subscription.SubscriptionPayloadContent.FULLRESOURCE, canonical.getContent());
		assertEquals("http://foo", canonical.getEndpointUrl());
		assertEquals(TEST_TOPIC, canonical.getTopic());
		assertEquals(CanonicalSubscriptionChannelType.RESTHOOK, canonical.getChannelType());
		assertThat(canonical.getFilters(), hasSize(2));

		CanonicalTopicSubscriptionFilter filter1 = canonical.getFilters().get(0);
		assertEquals("Observation", filter1.getResourceType());
		assertEquals("param1", filter1.getFilterParameter());
		// WIP STR5 assert comparator once core libs are updated
		assertEquals(Enumerations.SearchModifierCode.EXACT, filter1.getModifier());
		assertEquals("value1", filter1.getValue());

		CanonicalTopicSubscriptionFilter filter2 = canonical.getFilters().get(1);
		assertEquals("CarePlan", filter2.getResourceType());
		assertEquals("param2", filter2.getFilterParameter());
		// WIP STR5 assert comparator once core libs are updated
		assertEquals(Enumerations.SearchModifierCode.EXACT, filter1.getModifier());
		assertEquals("value2", filter2.getValue());
		assertEquals(123, canonical.getHeartbeatPeriod());
		assertEquals(456, canonical.getMaxCount());
	}

	@Test
	void testR4Backport() {
		// Example drawn from http://build.fhir.org/ig/HL7/fhir-subscription-backport-ig/Subscription-subscription-zulip.json.html

		// setup
		SubscriptionCanonicalizer r4Canonicalizer = new SubscriptionCanonicalizer(FhirContext.forR4Cached());
		Subscription subscription = new Subscription();

		// Standard R4 stuff
		subscription.getMeta().addTag("http://a", "b", "c");
		subscription.getMeta().addTag("http://d", "e", "f");
		subscription.setId("testId");
		subscription.getChannel().setType(Subscription.SubscriptionChannelType.RESTHOOK);
		subscription.getChannel().setEndpoint(TEST_ENDPOINT);
		subscription.getChannel().setPayload(CT_FHIR_JSON_NEW);
		subscription.getChannel().addHeader(TEST_HEADER1);
		subscription.getChannel().addHeader(TEST_HEADER2);
		subscription.setStatus(Subscription.SubscriptionStatus.ACTIVE);

		// Subscription Topic Extensions:

		subscription.getMeta().addProfile(SubscriptionConstants.SUBSCRIPTION_TOPIC_PROFILE_URL);
		subscription.setCriteria(TEST_TOPIC);
		subscription.getCriteriaElement().addExtension(SubscriptionConstants.SUBSCRIPTION_TOPIC_FILTER_URL, new StringType(TEST_FILTER1));
		subscription.getCriteriaElement().addExtension(SubscriptionConstants.SUBSCRIPTION_TOPIC_FILTER_URL, new StringType(TEST_FILTER2));
		subscription.getChannel().addExtension(SubscriptionConstants.SUBSCRIPTION_TOPIC_CHANNEL_HEARTBEAT_PERIOD_URL, new UnsignedIntType(86400));
		subscription.getChannel().addExtension(SubscriptionConstants.SUBSCRIPTION_TOPIC_CHANNEL_TIMEOUT_URL, new UnsignedIntType(60));
		subscription.getChannel().addExtension(SubscriptionConstants.SUBSCRIPTION_TOPIC_CHANNEL_MAX_COUNT, new PositiveIntType(20));
		subscription.getChannel().getPayloadElement().addExtension(SubscriptionConstants.SUBSCRIPTION_TOPIC_CHANNEL_PAYLOAD_CONTENT, new CodeType("full-resource"));

		// execute
		CanonicalSubscription canonical = r4Canonicalizer.canonicalize(subscription);

		// verify

		// Standard R4 stuff
		assertEquals(2, canonical.getTags().size());
		assertEquals("b", canonical.getTags().get("http://a"));
		assertEquals("e", canonical.getTags().get("http://d"));
		assertEquals("testId", canonical.getIdPart());
		assertEquals("testId", canonical.getIdElementString());
		assertEquals(TEST_ENDPOINT, canonical.getEndpointUrl());
		assertEquals(CT_FHIR_JSON_NEW, canonical.getContentType());
		assertThat(canonical.getHeaders(), hasSize(2));
		assertEquals(TEST_HEADER1, canonical.getHeaders().get(0));
		assertEquals(TEST_HEADER2, canonical.getHeaders().get(1));
		assertEquals(Subscription.SubscriptionStatus.ACTIVE, canonical.getStatus());

		assertEquals(CT_FHIR_JSON_NEW, canonical.getContentType());
		assertEquals(org.hl7.fhir.r5.model.Subscription.SubscriptionPayloadContent.FULLRESOURCE, canonical.getContent());
		assertEquals(TEST_ENDPOINT, canonical.getEndpointUrl());
		assertEquals(TEST_TOPIC, canonical.getTopic());
		assertEquals(CanonicalSubscriptionChannelType.RESTHOOK, canonical.getChannelType());
		assertThat(canonical.getFilters(), hasSize(2));

		CanonicalTopicSubscriptionFilter filter1 = canonical.getFilters().get(0);
		assertEquals("Encounter", filter1.getResourceType());
		assertEquals("patient", filter1.getFilterParameter());
		assertEquals(Enumerations.SearchComparator.EQ, filter1.getComparator());
		assertNull(filter1.getModifier());
		assertEquals("Patient/123", filter1.getValue());

		CanonicalTopicSubscriptionFilter filter2 = canonical.getFilters().get(1);
		assertEquals("Encounter", filter2.getResourceType());
		assertEquals("status", filter2.getFilterParameter());
		assertEquals(Enumerations.SearchComparator.EQ, filter2.getComparator());
		assertNull(filter2.getModifier());
		assertEquals("finished", filter2.getValue());

		assertEquals(86400, canonical.getHeartbeatPeriod());
		assertEquals(60, canonical.getTimeout());
		assertEquals(20, canonical.getMaxCount());
	}

	@NotNull
	private static org.hl7.fhir.r5.model.Subscription.SubscriptionFilterByComponent buildFilter(String theResourceType, String theParam, String theValue) {
		org.hl7.fhir.r5.model.Subscription.SubscriptionFilterByComponent filter = new org.hl7.fhir.r5.model.Subscription.SubscriptionFilterByComponent();
		filter.setResourceType(theResourceType);
		filter.setFilterParameter(theParam);
		filter.setModifier(Enumerations.SearchModifierCode.EXACT);
		// WIP STR5 add comparator once core libs are updated
		filter.setValue(theValue);
		return filter;
	}
}
