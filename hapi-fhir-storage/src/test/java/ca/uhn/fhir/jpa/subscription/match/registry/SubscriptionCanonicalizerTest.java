package ca.uhn.fhir.jpa.subscription.match.registry;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscriptionChannelType;
import ca.uhn.fhir.jpa.subscription.model.CanonicalTopicSubscriptionFilter;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.subscription.SubscriptionTestDataHelper;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Extension;
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
		subscription.setTopic(SubscriptionTestDataHelper.TEST_TOPIC);
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

	@Test
	void testR4Backport() {
		// Example drawn from http://build.fhir.org/ig/HL7/fhir-subscription-backport-ig/Subscription-subscription-zulip.json.html

		// setup
		SubscriptionCanonicalizer r4Canonicalizer = new SubscriptionCanonicalizer(FhirContext.forR4Cached());

		// execute

		CanonicalSubscription canonical = r4Canonicalizer.canonicalize(SubscriptionTestDataHelper.buildR4TopicSubscription());

		// verify

		// Standard R4 stuff
		assertEquals(2, canonical.getTags().size());
		assertEquals("b", canonical.getTags().get("http://a"));
		assertEquals("e", canonical.getTags().get("http://d"));
		assertEquals("testId", canonical.getIdPart());
		assertEquals("testId", canonical.getIdElementString());
		assertEquals(SubscriptionTestDataHelper.TEST_ENDPOINT, canonical.getEndpointUrl());
		assertEquals(CT_FHIR_JSON_NEW, canonical.getContentType());
		assertThat(canonical.getHeaders(), hasSize(2));
		assertEquals(SubscriptionTestDataHelper.TEST_HEADER1, canonical.getHeaders().get(0));
		assertEquals(SubscriptionTestDataHelper.TEST_HEADER2, canonical.getHeaders().get(1));
		assertEquals(Subscription.SubscriptionStatus.ACTIVE, canonical.getStatus());

		assertEquals(CT_FHIR_JSON_NEW, canonical.getContentType());
		assertEquals(org.hl7.fhir.r5.model.Subscription.SubscriptionPayloadContent.FULLRESOURCE, canonical.getContent());
		assertEquals(SubscriptionTestDataHelper.TEST_ENDPOINT, canonical.getEndpointUrl());
		assertEquals(SubscriptionTestDataHelper.TEST_TOPIC, canonical.getTopic());
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
		filter.setComparator(Enumerations.SearchComparator.EQ);
		filter.setValue(theValue);
		return filter;
	}
}
