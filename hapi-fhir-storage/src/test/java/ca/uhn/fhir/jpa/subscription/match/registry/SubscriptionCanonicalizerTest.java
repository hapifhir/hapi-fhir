package ca.uhn.fhir.jpa.subscription.match.registry;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscriptionChannelType;
import ca.uhn.fhir.jpa.subscription.model.CanonicalTopicSubscriptionFilter;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.subscription.SubscriptionConstants;
import ca.uhn.fhir.subscription.SubscriptionTestDataHelper;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Subscription;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.Enumerations;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static ca.uhn.fhir.rest.api.Constants.CT_FHIR_JSON_NEW;
import static ca.uhn.fhir.util.HapiExtensions.EX_SEND_DELETE_MESSAGES;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SubscriptionCanonicalizerTest {

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

	private org.hl7.fhir.r5.model.Subscription buildR5Subscription(org.hl7.fhir.r5.model.Subscription.SubscriptionPayloadContent thePayloadContent) {
		org.hl7.fhir.r5.model.Subscription subscription = new org.hl7.fhir.r5.model.Subscription();

		subscription.setStatus(Enumerations.SubscriptionStatusCodes.ACTIVE);
		subscription.setContentType(CT_FHIR_JSON_NEW);
		subscription.setContent(thePayloadContent);
		subscription.setEndpoint("http://foo");
		subscription.setTopic(SubscriptionTestDataHelper.TEST_TOPIC);
		Coding channelType = new Coding().setSystem("http://terminology.hl7.org/CodeSystem/subscription-channel-type").setCode("rest-hook");
		subscription.setChannelType(channelType);
		subscription.addFilterBy(buildFilter("Observation", "param1", "value1"));
		subscription.addFilterBy(buildFilter("CarePlan", "param2", "value2"));
		subscription.setHeartbeatPeriod(123);
		subscription.setMaxCount(456);

		return subscription;
	}

	@ParameterizedTest
	@ValueSource(strings = {"full-resource", "id-only", "empty"})
	public void testR5Canonicalize_returnsCorrectCanonicalSubscription(String thePayloadContent) {
		// setup
		SubscriptionCanonicalizer r5Canonicalizer = new SubscriptionCanonicalizer(FhirContext.forR5Cached());
		org.hl7.fhir.r5.model.Subscription.SubscriptionPayloadContent payloadContent =
				org.hl7.fhir.r5.model.Subscription.SubscriptionPayloadContent.fromCode(thePayloadContent);
		org.hl7.fhir.r5.model.Subscription subscription = buildR5Subscription(payloadContent);

		// execute
		CanonicalSubscription canonical = r5Canonicalizer.canonicalize(subscription);

		// verify
		assertEquals(Subscription.SubscriptionStatus.ACTIVE, canonical.getStatus());
		assertEquals(CT_FHIR_JSON_NEW, canonical.getContentType());
		assertEquals(payloadContent, canonical.getContent());
		assertEquals("http://foo", canonical.getEndpointUrl());
		assertEquals(SubscriptionTestDataHelper.TEST_TOPIC, canonical.getTopic());
		assertEquals(CanonicalSubscriptionChannelType.RESTHOOK, canonical.getChannelType());
		assertThat(canonical.getFilters(), hasSize(2));

		CanonicalTopicSubscriptionFilter filter1 = canonical.getFilters().get(0);
		assertEquals("Observation", filter1.getResourceType());
		assertEquals("param1", filter1.getFilterParameter());
		assertEquals(Enumerations.SearchComparator.EQ, filter1.getComparator());
		assertEquals(Enumerations.SearchModifierCode.EXACT, filter1.getModifier());
		assertEquals("value1", filter1.getValue());

		CanonicalTopicSubscriptionFilter filter2 = canonical.getFilters().get(1);
		assertEquals("CarePlan", filter2.getResourceType());
		assertEquals("param2", filter2.getFilterParameter());
		assertEquals(Enumerations.SearchComparator.EQ, filter2.getComparator());
		assertEquals(Enumerations.SearchModifierCode.EXACT, filter2.getModifier());
		assertEquals("value2", filter2.getValue());
		assertEquals(123, canonical.getHeartbeatPeriod());
		assertEquals(456, canonical.getMaxCount());
	}

	@ParameterizedTest
	@ValueSource(strings = {"full-resource", "id-only", "empty"})
	void testR4BCanonicalize_returnsCorrectCanonicalSubscription(String thePayloadContent) {
		// Example drawn from http://build.fhir.org/ig/HL7/fhir-subscription-backport-ig/Subscription-subscription-zulip.json.html

		// setup
		SubscriptionCanonicalizer r4bCanonicalizer = new SubscriptionCanonicalizer(FhirContext.forR4BCached());
		org.hl7.fhir.r4b.model.Subscription subscription = buildR4BSubscription(thePayloadContent);

		// execute
		CanonicalSubscription canonical = r4bCanonicalizer.canonicalize(subscription);

		// verify
		assertEquals(Subscription.SubscriptionStatus.ACTIVE, canonical.getStatus());
		verifyStandardSubscriptionParameters(canonical);
		verifyChannelParameters(canonical, thePayloadContent);
	}

	private org.hl7.fhir.r4b.model.Subscription buildR4BSubscription(String thePayloadContent) {
		org.hl7.fhir.r4b.model.Subscription subscription = new org.hl7.fhir.r4b.model.Subscription();

		subscription.setId("testId");
		subscription.getMeta().addTag("http://a", "b", "c");
		subscription.getMeta().addTag("http://d", "e", "f");
		subscription.setStatus(org.hl7.fhir.r4b.model.Enumerations.SubscriptionStatus.ACTIVE);
		subscription.getChannel().setPayload(CT_FHIR_JSON_NEW);
		subscription.getChannel().setType(org.hl7.fhir.r4b.model.Subscription.SubscriptionChannelType.RESTHOOK);
		subscription.getChannel().setEndpoint(SubscriptionTestDataHelper.TEST_ENDPOINT);

		subscription.getMeta().addProfile(SubscriptionConstants.SUBSCRIPTION_TOPIC_PROFILE_URL);
		subscription.setCriteria(SubscriptionTestDataHelper.TEST_TOPIC);

		subscription.getChannel().setPayload(CT_FHIR_JSON_NEW);
		subscription.getChannel().addHeader(SubscriptionTestDataHelper.TEST_HEADER1);
		subscription.getChannel().addHeader(SubscriptionTestDataHelper.TEST_HEADER2);
		subscription.setStatus(org.hl7.fhir.r4b.model.Enumerations.SubscriptionStatus.ACTIVE);

		subscription
				.getChannel()
				.getPayloadElement()
				.addExtension(
						SubscriptionConstants.SUBSCRIPTION_TOPIC_CHANNEL_PAYLOAD_CONTENT,
						new org.hl7.fhir.r4b.model.CodeType(thePayloadContent));

		return subscription;
	}

	@ParameterizedTest
	@ValueSource(strings = {"full-resource", "id-only", "empty"})
	void testR4canonicalize_withBackPortedSubscription_returnsCorrectCanonicalSubscription(String thePayloadContent) {
		// Example drawn from http://build.fhir.org/ig/HL7/fhir-subscription-backport-ig/Subscription-subscription-zulip.json.html

		// setup
		SubscriptionCanonicalizer r4Canonicalizer = new SubscriptionCanonicalizer(FhirContext.forR4Cached());

		// execute
		Subscription subscription = SubscriptionTestDataHelper.buildR4TopicSubscriptionWithContent(thePayloadContent);
		CanonicalSubscription canonical = r4Canonicalizer.canonicalize(subscription);

		// verify

		// Standard R4 stuff
		verifyStandardSubscriptionParameters(canonical);
		assertEquals(Subscription.SubscriptionStatus.ACTIVE, canonical.getStatus());
		verifyChannelParameters(canonical, thePayloadContent);

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

	private void verifyChannelParameters(CanonicalSubscription theCanonicalSubscriptions, String thePayloadContent) {
		assertThat(theCanonicalSubscriptions.getHeaders(), hasSize(2));
		assertEquals(SubscriptionTestDataHelper.TEST_HEADER1, theCanonicalSubscriptions.getHeaders().get(0));
		assertEquals(SubscriptionTestDataHelper.TEST_HEADER2, theCanonicalSubscriptions.getHeaders().get(1));

		assertEquals(CT_FHIR_JSON_NEW, theCanonicalSubscriptions.getContentType());
		assertEquals(thePayloadContent, theCanonicalSubscriptions.getContent().toCode());
		assertEquals(SubscriptionTestDataHelper.TEST_ENDPOINT, theCanonicalSubscriptions.getEndpointUrl());
		assertEquals(SubscriptionTestDataHelper.TEST_TOPIC, theCanonicalSubscriptions.getTopic());
		assertEquals(CanonicalSubscriptionChannelType.RESTHOOK, theCanonicalSubscriptions.getChannelType());
	}

	private void verifyStandardSubscriptionParameters(CanonicalSubscription theCanonicalSubscription) {
		assertEquals(2, theCanonicalSubscription.getTags().size());
		assertEquals("b", theCanonicalSubscription.getTags().get("http://a"));
		assertEquals("e", theCanonicalSubscription.getTags().get("http://d"));
		assertEquals("testId", theCanonicalSubscription.getIdPart());
		assertEquals("testId", theCanonicalSubscription.getIdElementString());
	}

	@NotNull
	private static org.hl7.fhir.r5.model.Subscription.SubscriptionFilterByComponent buildFilter(String theResourceType, String theParam, String theValue) {
		org.hl7.fhir.r5.model.Subscription.SubscriptionFilterByComponent filter = new org.hl7.fhir.r5.model.Subscription.SubscriptionFilterByComponent();
		filter.setResourceType(theResourceType);
		filter.setFilterParameter(theParam);
		filter.setModifier(Enumerations.SearchModifierCode.EXACT);
		filter.setComparator(Enumerations.SearchComparator.EQ);
		filter.setValue(theValue);
		return filter;
	}
}
