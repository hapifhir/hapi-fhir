package ca.uhn.fhir.jpa.subscription.match.registry;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscriptionChannelType;
import ca.uhn.fhir.jpa.subscription.model.CanonicalTopicSubscriptionFilter;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.primitive.BooleanDt;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Subscription;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

class SubscriptionCanonicalizerTest {

	private static final String TEST_TOPIC = "http://test.topic";
	private static final String TEST_FILTER = "Encounter?patient=Patient/123";
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
		Subscription subscription = new Subscription();
		Extension sendDeleteMessagesExtension = new Extension()
			.setUrl(EX_SEND_DELETE_MESSAGES)
			.setValue(new BooleanType(true));
		subscription.getChannel().addExtension(sendDeleteMessagesExtension);

		CanonicalSubscription canonicalSubscription = testedSC.canonicalize(subscription);

		assertTrue(canonicalSubscription.getSendDeleteMessages());
	}

	@Test
	public void testCanonicalizeDstu2SendDeleteMessages() {
		SubscriptionCanonicalizer dstu2Canonicalizer = new SubscriptionCanonicalizer(FhirContext.forDstu2Cached());
		ca.uhn.fhir.model.dstu2.resource.Subscription dstu2Sub = new ca.uhn.fhir.model.dstu2.resource.Subscription();
		ExtensionDt extensionDt = new ExtensionDt();
		extensionDt.setUrl(EX_SEND_DELETE_MESSAGES);
		extensionDt.setValue(new BooleanDt(true));
		dstu2Sub.getChannel().addUndeclaredExtension(extensionDt);
		CanonicalSubscription canonicalize = dstu2Canonicalizer.canonicalize(dstu2Sub);
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
		CanonicalSubscription canonicalize = r5Canonicalizer.canonicalize(subscription);

		// verify
		assertEquals(Subscription.SubscriptionStatus.ACTIVE, canonicalize.getStatus());
		assertEquals(CT_FHIR_JSON_NEW, canonicalize.getContentType());
		assertEquals(org.hl7.fhir.r5.model.Subscription.SubscriptionPayloadContent.FULLRESOURCE, canonicalize.getContent());
		assertEquals("http://foo", canonicalize.getEndpointUrl());
		assertEquals(TEST_TOPIC, canonicalize.getTopic());
		assertEquals(CanonicalSubscriptionChannelType.RESTHOOK, canonicalize.getChannelType());
		assertThat(canonicalize.getFilters(), hasSize(2));

		CanonicalTopicSubscriptionFilter filter1 = canonicalize.getFilters().get(0);
		assertEquals("Observation", filter1.getResourceType());
		assertEquals("param1", filter1.getFilterParameter());
		// WIP STR5 assert comparator once core libs are updated
		assertEquals(Enumerations.SearchModifierCode.EXACT, filter1.getModifier());
		assertEquals("value1", filter1.getValue());

		CanonicalTopicSubscriptionFilter filter2 = canonicalize.getFilters().get(1);
		assertEquals("CarePlan", filter2.getResourceType());
		assertEquals("param2", filter2.getFilterParameter());
		// WIP STR5 assert comparator once core libs are updated
		assertEquals(Enumerations.SearchModifierCode.EXACT, filter1.getModifier());
		assertEquals("value2", filter2.getValue());
		assertEquals(123, canonicalize.getHeartbeatPeriod());
		assertEquals(456, canonicalize.getMaxCount());
	}

	@Test
	void testR4Backport() {
		// setup
		SubscriptionCanonicalizer r4Canonicalizer = new SubscriptionCanonicalizer(FhirContext.forR4Cached());
		Subscription subscription = new Subscription();
		subscription.getMeta().addProfile(SubscriptionConstants.SUBSCRIPTION_TOPIC_PROFILE_URL);
		subscription.setId("testId");
		subscription.setCriteria(TEST_TOPIC);
		subscription.getCriteriaElement().addExtension(SubscriptionConstants.SUBSCRIPTION_TOPIC_FILTER_URL, new StringType(TEST_FILTER));
		// FIXME KHS continue with http://build.fhir.org/ig/HL7/fhir-subscription-backport-ig/Subscription-subscription-zulip.json.html

		subscription.setStatus(Subscription.SubscriptionStatus.ACTIVE);

		// execute
		CanonicalSubscription canonicalize = r4Canonicalizer.canonicalize(subscription);

		// verify
		assertEquals(Subscription.SubscriptionStatus.ACTIVE, canonicalize.getStatus());
		assertEquals(CT_FHIR_JSON_NEW, canonicalize.getContentType());
		assertEquals(org.hl7.fhir.r5.model.Subscription.SubscriptionPayloadContent.FULLRESOURCE, canonicalize.getContent());
		assertEquals("http://foo", canonicalize.getEndpointUrl());
		assertEquals(TEST_TOPIC, canonicalize.getTopic());
		assertEquals(CanonicalSubscriptionChannelType.RESTHOOK, canonicalize.getChannelType());
		assertThat(canonicalize.getFilters(), hasSize(2));

		CanonicalTopicSubscriptionFilter filter1 = canonicalize.getFilters().get(0);
		assertEquals("Observation", filter1.getResourceType());
		assertEquals("param1", filter1.getFilterParameter());
		// WIP STR5 assert comparator once core libs are updated
		assertEquals(Enumerations.SearchModifierCode.EXACT, filter1.getModifier());
		assertEquals("value1", filter1.getValue());

		CanonicalTopicSubscriptionFilter filter2 = canonicalize.getFilters().get(1);
		assertEquals("CarePlan", filter2.getResourceType());
		assertEquals("param2", filter2.getFilterParameter());
		// WIP STR5 assert comparator once core libs are updated
		assertEquals(Enumerations.SearchModifierCode.EXACT, filter1.getModifier());
		assertEquals("value2", filter2.getValue());
		assertEquals(123, canonicalize.getHeartbeatPeriod());
		assertEquals(456, canonicalize.getMaxCount());
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
