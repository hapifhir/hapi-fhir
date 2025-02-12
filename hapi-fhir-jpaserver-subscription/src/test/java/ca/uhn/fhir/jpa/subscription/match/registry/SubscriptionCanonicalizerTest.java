package ca.uhn.fhir.jpa.subscription.match.registry;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.SubscriptionSettings;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscriptionChannelType;
import ca.uhn.fhir.jpa.subscription.model.CanonicalTopicSubscriptionFilter;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.subscription.SubscriptionConstants;
import ca.uhn.fhir.subscription.SubscriptionTestDataHelper;
import ca.uhn.fhir.util.HapiExtensions;
import jakarta.annotation.Nonnull;
import org.assertj.core.api.AssertionsForClassTypes;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Subscription;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.Enumerations;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;
import java.util.stream.Stream;

import static ca.uhn.fhir.rest.api.Constants.CT_FHIR_JSON_NEW;
import static ca.uhn.fhir.rest.api.Constants.RESOURCE_PARTITION_ID;
import static ca.uhn.fhir.util.HapiExtensions.EX_SEND_DELETE_MESSAGES;
import static java.util.Objects.isNull;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SubscriptionCanonicalizerTest {

	private static final Integer NON_NULL_DEFAULT_PARTITION_ID = 666;

	FhirContext r4Context = FhirContext.forR4();

	private final SubscriptionCanonicalizer testedSC = new SubscriptionCanonicalizer(r4Context, new SubscriptionSettings());

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

		assertThat(canonicalSubscription.getTags()).containsEntry("http://foo", "baz");
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
		SubscriptionCanonicalizer dstu2Canonicalizer = new SubscriptionCanonicalizer(FhirContext.forDstu2Cached(), new SubscriptionSettings());
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
		SubscriptionCanonicalizer r5Canonicalizer = new SubscriptionCanonicalizer(FhirContext.forR5Cached(), new SubscriptionSettings());
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
		assertThat(canonical.getFilters()).hasSize(2);

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
		SubscriptionCanonicalizer r4bCanonicalizer = new SubscriptionCanonicalizer(FhirContext.forR4BCached(), new SubscriptionSettings());
		org.hl7.fhir.r4b.model.Subscription subscription = buildR4BSubscription(thePayloadContent);

		// execute
		CanonicalSubscription canonical = r4bCanonicalizer.canonicalize(subscription);

		// verify
		assertEquals(Subscription.SubscriptionStatus.ACTIVE, canonical.getStatus());
		verifyStandardSubscriptionParameters(canonical);
		verifyChannelParameters(canonical, thePayloadContent);
	}

	private static Stream<RequestPartitionId> crossPartitionParams() {
		return Stream.of(null, RequestPartitionId.fromPartitionId(1), RequestPartitionId.defaultPartition()) ;
	}

	private class FakeNonNullDefaultPartitionIDHelper implements IRequestPartitionHelperSvc {

		@Override
		public @Nullable Integer getDefaultPartitionId() {
			return NON_NULL_DEFAULT_PARTITION_ID;
		}

		@Override
		public boolean isDefaultPartition(@NotNull RequestPartitionId theRequestPartitionId) {
			return  theRequestPartitionId.getPartitionIds().get(0).equals(NON_NULL_DEFAULT_PARTITION_ID);
		}

		@Override
		public boolean hasDefaultPartitionId(@NotNull RequestPartitionId theRequestPartitionId) {
			return theRequestPartitionId.getPartitionIds().stream().anyMatch(part -> part.equals(NON_NULL_DEFAULT_PARTITION_ID));
		}

		@Override
		public RequestPartitionId determineReadPartitionForRequest(@Nullable RequestDetails theRequest, @NotNull ReadPartitionIdRequestDetails theDetails) {
			return null;
		}

		@Override
		public RequestPartitionId determineGenericPartitionForRequest(RequestDetails theRequestDetails) {
			return null;
		}

		@Override
		public @NotNull RequestPartitionId determineCreatePartitionForRequest(@Nullable RequestDetails theRequest, @NotNull IBaseResource theResource, @NotNull String theResourceType) {
			return null;
		}

		@Override
		public @NotNull Set<Integer> toReadPartitions(@NotNull RequestPartitionId theRequestPartitionId) {
			return Set.of();
		}

		@Override
		public boolean isResourcePartitionable(String theResourceType) {
			return false;
		}

		@Override
		public RequestPartitionId validateAndNormalizePartitionIds(RequestPartitionId theRequestPartitionId) {
			return null;
		}

		@Override
		public RequestPartitionId validateAndNormalizePartitionNames(RequestPartitionId theRequestPartitionId) {
			return null;
		}
	}
	@Test
	public void testNonNullDefaultPartitionIDCanonicalizesToCrossPartition() {
		IRequestPartitionHelperSvc myHelperSvc = new FakeNonNullDefaultPartitionIDHelper();
		final SubscriptionSettings subscriptionSettings = new SubscriptionSettings();

		subscriptionSettings.setCrossPartitionSubscriptionEnabled(true);
		final SubscriptionCanonicalizer subscriptionCanonicalizer = new SubscriptionCanonicalizer(FhirContext.forR4(), subscriptionSettings);
		subscriptionCanonicalizer.setPartitionHelperSvc(myHelperSvc);
		Subscription subscription = buildMdmSubscriptionR4("test-subscription", "Patient?");
		CanonicalSubscription canonicalize = subscriptionCanonicalizer.canonicalize(subscription);

		assertThat(canonicalize.isCrossPartitionEnabled()).isTrue();

	}
	private Subscription buildMdmSubscriptionR4(String theId, String theCriteria) {
		Subscription retval = new Subscription();
		retval.setId(theId);
		retval.setReason("MDM Simulacrum Subscription");
		retval.setStatus(Subscription.SubscriptionStatus.REQUESTED);
		retval.setCriteria(theCriteria);
		retval.addExtension()
			.setUrl(HapiExtensions.EXTENSION_SUBSCRIPTION_CROSS_PARTITION)
			.setValue(new BooleanType().setValue(true));
		Subscription.SubscriptionChannelComponent channel = retval.getChannel();
		channel.setType(Subscription.SubscriptionChannelType.MESSAGE);
		channel.setEndpoint("channel:test");
		channel.setPayload(Constants.CT_JSON);
		RequestPartitionId retPartId = RequestPartitionId.fromPartitionId(NON_NULL_DEFAULT_PARTITION_ID);
		retval.setUserData(Constants.RESOURCE_PARTITION_ID, retPartId);
		return retval;
	}

	@ParameterizedTest
	@MethodSource("crossPartitionParams")
	void testSubscriptionCrossPartitionEnableProperty_forDstu2WithExtensionAndPartitions(RequestPartitionId theRequestPartitionId) {
		final SubscriptionSettings subscriptionSettings = new SubscriptionSettings();
		subscriptionSettings.setCrossPartitionSubscriptionEnabled(true);
		final SubscriptionCanonicalizer subscriptionCanonicalizer = new SubscriptionCanonicalizer(FhirContext.forDstu2(), subscriptionSettings);

		ca.uhn.fhir.model.dstu2.resource.Subscription subscriptionWithoutExtension = new ca.uhn.fhir.model.dstu2.resource.Subscription();
		subscriptionWithoutExtension.setUserData(RESOURCE_PARTITION_ID, theRequestPartitionId);

		final CanonicalSubscription canonicalSubscriptionWithoutExtension = subscriptionCanonicalizer.canonicalize(subscriptionWithoutExtension);

		assertThat(canonicalSubscriptionWithoutExtension.isCrossPartitionEnabled()).isFalse();
	}

	@ParameterizedTest
	@MethodSource("crossPartitionParams")
	void testSubscriptionCrossPartitionEnableProperty_forDstu3WithExtensionAndPartitions(RequestPartitionId theRequestPartitionId) {
		final SubscriptionSettings subscriptionSettings = new SubscriptionSettings();
		subscriptionSettings.setCrossPartitionSubscriptionEnabled(true);
		final SubscriptionCanonicalizer subscriptionCanonicalizer = new SubscriptionCanonicalizer(FhirContext.forDstu3(), subscriptionSettings);

		org.hl7.fhir.dstu3.model.Subscription subscriptionWithoutExtension = new org.hl7.fhir.dstu3.model.Subscription();
		subscriptionWithoutExtension.setUserData(RESOURCE_PARTITION_ID, theRequestPartitionId);

		org.hl7.fhir.dstu3.model.Subscription subscriptionWithExtensionCrossPartitionTrue = new org.hl7.fhir.dstu3.model.Subscription();
		subscriptionWithExtensionCrossPartitionTrue.setUserData(RESOURCE_PARTITION_ID, theRequestPartitionId);
		subscriptionWithExtensionCrossPartitionTrue.addExtension(HapiExtensions.EXTENSION_SUBSCRIPTION_CROSS_PARTITION, new org.hl7.fhir.dstu3.model.BooleanType().setValue(true));

		org.hl7.fhir.dstu3.model.Subscription subscriptionWithExtensionCrossPartitionFalse = new org.hl7.fhir.dstu3.model.Subscription();
		subscriptionWithExtensionCrossPartitionFalse.setUserData(RESOURCE_PARTITION_ID, theRequestPartitionId);
		subscriptionWithExtensionCrossPartitionFalse.addExtension(HapiExtensions.EXTENSION_SUBSCRIPTION_CROSS_PARTITION, new org.hl7.fhir.dstu3.model.BooleanType().setValue(false));

		final CanonicalSubscription canonicalSubscriptionWithoutExtension = subscriptionCanonicalizer.canonicalize(subscriptionWithoutExtension);
		final CanonicalSubscription canonicalSubscriptionWithExtensionCrossPartitionTrue = subscriptionCanonicalizer.canonicalize(subscriptionWithExtensionCrossPartitionTrue);
		final CanonicalSubscription canonicalSubscriptionWithExtensionCrossPartitionFalse = subscriptionCanonicalizer.canonicalize(subscriptionWithExtensionCrossPartitionFalse);

		assertCanonicalSubscriptionCrossPropertyValue(
			canonicalSubscriptionWithoutExtension,
			canonicalSubscriptionWithExtensionCrossPartitionTrue,
			canonicalSubscriptionWithExtensionCrossPartitionFalse,
			theRequestPartitionId
		);
	}

	@ParameterizedTest
	@MethodSource("crossPartitionParams")
	void testSubscriptionCrossPartitionEnableProperty_forR4WithExtensionAndPartitions(RequestPartitionId theRequestPartitionId) {
		final SubscriptionSettings subscriptionSettings = new SubscriptionSettings();
		subscriptionSettings.setCrossPartitionSubscriptionEnabled(true);

		final SubscriptionCanonicalizer subscriptionCanonicalizer = new SubscriptionCanonicalizer(FhirContext.forR4Cached(), subscriptionSettings);

		Subscription subscriptionWithoutExtension = new Subscription();
		subscriptionWithoutExtension.setUserData(RESOURCE_PARTITION_ID, theRequestPartitionId);

		Subscription subscriptionWithExtensionCrossPartitionTrue = new Subscription();
		subscriptionWithExtensionCrossPartitionTrue.setUserData(RESOURCE_PARTITION_ID, theRequestPartitionId);
		subscriptionWithExtensionCrossPartitionTrue.addExtension(HapiExtensions.EXTENSION_SUBSCRIPTION_CROSS_PARTITION, new org.hl7.fhir.r4.model.BooleanType().setValue(true));

		Subscription subscriptionWithExtensionCrossPartitionFalse = new Subscription();
		subscriptionWithExtensionCrossPartitionFalse.setUserData(RESOURCE_PARTITION_ID, theRequestPartitionId);
		subscriptionWithExtensionCrossPartitionFalse.addExtension(HapiExtensions.EXTENSION_SUBSCRIPTION_CROSS_PARTITION, new org.hl7.fhir.r4.model.BooleanType().setValue(false));


		final CanonicalSubscription canonicalSubscriptionWithoutExtension = subscriptionCanonicalizer.canonicalize(subscriptionWithoutExtension);
		final CanonicalSubscription canonicalSubscriptionWithExtensionCrossPartitionTrue = subscriptionCanonicalizer.canonicalize(subscriptionWithExtensionCrossPartitionTrue);
		final CanonicalSubscription canonicalSubscriptionWithExtensionCrossPartitionFalse = subscriptionCanonicalizer.canonicalize(subscriptionWithExtensionCrossPartitionFalse);

		assertCanonicalSubscriptionCrossPropertyValue(
			canonicalSubscriptionWithoutExtension,
			canonicalSubscriptionWithExtensionCrossPartitionTrue,
			canonicalSubscriptionWithExtensionCrossPartitionFalse,
			theRequestPartitionId
		);
	}

	@ParameterizedTest
	@MethodSource("crossPartitionParams")
	void testSubscriptionCrossPartitionEnableProperty_forR4BWithExtensionAndPartitions(RequestPartitionId theRequestPartitionId) {
		final SubscriptionSettings subscriptionSettings = new SubscriptionSettings();
		subscriptionSettings.setCrossPartitionSubscriptionEnabled(true);
		final SubscriptionCanonicalizer subscriptionCanonicalizer = new SubscriptionCanonicalizer(FhirContext.forR4BCached(), subscriptionSettings);

		org.hl7.fhir.r4b.model.Subscription subscriptionWithoutExtension = new org.hl7.fhir.r4b.model.Subscription();
		subscriptionWithoutExtension.setUserData(RESOURCE_PARTITION_ID, theRequestPartitionId);

		org.hl7.fhir.r4b.model.Subscription subscriptionWithExtensionCrossPartitionTrue = new org.hl7.fhir.r4b.model.Subscription();
		subscriptionWithExtensionCrossPartitionTrue.setUserData(RESOURCE_PARTITION_ID, theRequestPartitionId);
		subscriptionWithExtensionCrossPartitionTrue.addExtension(HapiExtensions.EXTENSION_SUBSCRIPTION_CROSS_PARTITION, new org.hl7.fhir.r4b.model.BooleanType().setValue(true));

		org.hl7.fhir.r4b.model.Subscription subscriptionWithExtensionCrossPartitionFalse = new org.hl7.fhir.r4b.model.Subscription();
		subscriptionWithExtensionCrossPartitionFalse.setUserData(RESOURCE_PARTITION_ID, theRequestPartitionId);
		subscriptionWithExtensionCrossPartitionFalse.addExtension(HapiExtensions.EXTENSION_SUBSCRIPTION_CROSS_PARTITION, new org.hl7.fhir.r4b.model.BooleanType().setValue(false));

		final CanonicalSubscription canonicalSubscriptionWithoutExtension = subscriptionCanonicalizer.canonicalize(subscriptionWithoutExtension);
		final CanonicalSubscription canonicalSubscriptionWithExtensionCrossPartitionTrue = subscriptionCanonicalizer.canonicalize(subscriptionWithExtensionCrossPartitionTrue);
		final CanonicalSubscription canonicalSubscriptionWithExtensionCrossPartitionFalse = subscriptionCanonicalizer.canonicalize(subscriptionWithExtensionCrossPartitionFalse);

		assertCanonicalSubscriptionCrossPropertyValue(
			canonicalSubscriptionWithoutExtension,
			canonicalSubscriptionWithExtensionCrossPartitionTrue,
			canonicalSubscriptionWithExtensionCrossPartitionFalse,
			theRequestPartitionId
		);
	}

	@ParameterizedTest
	@MethodSource("crossPartitionParams")
	void testSubscriptionCrossPartitionEnableProperty_forR5WithExtensionAndPartitions(RequestPartitionId theRequestPartitionId) {
		final SubscriptionSettings subscriptionSettings = new SubscriptionSettings();
		subscriptionSettings.setCrossPartitionSubscriptionEnabled(true);
		final SubscriptionCanonicalizer subscriptionCanonicalizer = new SubscriptionCanonicalizer(FhirContext.forR5Cached(), subscriptionSettings);

		org.hl7.fhir.r5.model.Subscription subscriptionWithoutExtension = new org.hl7.fhir.r5.model.Subscription();
		subscriptionWithoutExtension.setUserData(RESOURCE_PARTITION_ID, theRequestPartitionId);

		org.hl7.fhir.r5.model.Subscription subscriptionWithExtensionCrossPartitionTrue = new org.hl7.fhir.r5.model.Subscription();
		subscriptionWithExtensionCrossPartitionTrue.setUserData(RESOURCE_PARTITION_ID, theRequestPartitionId);
		subscriptionWithExtensionCrossPartitionTrue.addExtension(HapiExtensions.EXTENSION_SUBSCRIPTION_CROSS_PARTITION, new org.hl7.fhir.r5.model.BooleanType().setValue(true));

		org.hl7.fhir.r5.model.Subscription subscriptionWithExtensionCrossPartitionFalse = new org.hl7.fhir.r5.model.Subscription();
		subscriptionWithExtensionCrossPartitionFalse.setUserData(RESOURCE_PARTITION_ID, theRequestPartitionId);
		subscriptionWithExtensionCrossPartitionFalse.addExtension(HapiExtensions.EXTENSION_SUBSCRIPTION_CROSS_PARTITION, new org.hl7.fhir.r5.model.BooleanType().setValue(false));

		final CanonicalSubscription canonicalSubscriptionWithoutExtension = subscriptionCanonicalizer.canonicalize(subscriptionWithoutExtension);
		final CanonicalSubscription canonicalSubscriptionWithExtensionCrossPartitionTrue = subscriptionCanonicalizer.canonicalize(subscriptionWithExtensionCrossPartitionTrue);
		final CanonicalSubscription canonicalSubscriptionWithExtensionCrossPartitionFalse = subscriptionCanonicalizer.canonicalize(subscriptionWithExtensionCrossPartitionFalse);

		assertCanonicalSubscriptionCrossPropertyValue(
			canonicalSubscriptionWithoutExtension,
			canonicalSubscriptionWithExtensionCrossPartitionTrue,
			canonicalSubscriptionWithExtensionCrossPartitionFalse,
			theRequestPartitionId
		);
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
		SubscriptionCanonicalizer r4Canonicalizer = new SubscriptionCanonicalizer(FhirContext.forR4Cached(), new SubscriptionSettings());

		// execute
		Subscription subscription = SubscriptionTestDataHelper.buildR4TopicSubscriptionWithContent(thePayloadContent);
		CanonicalSubscription canonical = r4Canonicalizer.canonicalize(subscription);

		// verify

		// Standard R4 stuff
		verifyStandardSubscriptionParameters(canonical);
		assertEquals(Subscription.SubscriptionStatus.ACTIVE, canonical.getStatus());
		verifyChannelParameters(canonical, thePayloadContent);

		assertThat(canonical.getFilters()).hasSize(2);

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
		assertThat(theCanonicalSubscriptions.getHeaders()).hasSize(2);
		assertEquals(SubscriptionTestDataHelper.TEST_HEADER1, theCanonicalSubscriptions.getHeaders().get(0));
		assertEquals(SubscriptionTestDataHelper.TEST_HEADER2, theCanonicalSubscriptions.getHeaders().get(1));

		assertEquals(CT_FHIR_JSON_NEW, theCanonicalSubscriptions.getContentType());
		assertEquals(thePayloadContent, theCanonicalSubscriptions.getContent().toCode());
		assertEquals(SubscriptionTestDataHelper.TEST_ENDPOINT, theCanonicalSubscriptions.getEndpointUrl());
		assertEquals(SubscriptionTestDataHelper.TEST_TOPIC, theCanonicalSubscriptions.getTopic());
		assertEquals(CanonicalSubscriptionChannelType.RESTHOOK, theCanonicalSubscriptions.getChannelType());
	}

	private void verifyStandardSubscriptionParameters(CanonicalSubscription theCanonicalSubscription) {
		assertThat(theCanonicalSubscription.getTags()).hasSize(2);
		assertThat(theCanonicalSubscription.getTags()).containsEntry("http://a", "b");
		assertThat(theCanonicalSubscription.getTags()).containsEntry("http://d", "e");
		assertEquals("testId", theCanonicalSubscription.getIdPart());
		assertEquals("testId", theCanonicalSubscription.getIdElementString());
	}

	@Nonnull
	private static org.hl7.fhir.r5.model.Subscription.SubscriptionFilterByComponent buildFilter(String theResourceType, String theParam, String theValue) {
		org.hl7.fhir.r5.model.Subscription.SubscriptionFilterByComponent filter = new org.hl7.fhir.r5.model.Subscription.SubscriptionFilterByComponent();
		filter.setResourceType(theResourceType);
		filter.setFilterParameter(theParam);
		filter.setModifier(Enumerations.SearchModifierCode.EXACT);
		filter.setComparator(Enumerations.SearchComparator.EQ);
		filter.setValue(theValue);
		return filter;
	}

	private void assertCanonicalSubscriptionCrossPropertyValue(CanonicalSubscription theCanonicalSubscriptionWithoutExtension,
															   CanonicalSubscription theCanonicalSubscriptionWithExtensionCrossPartitionTrue,
															   CanonicalSubscription theCanonicalSubscriptionWithExtensionCrossPartitionFalse,
															   RequestPartitionId theRequestPartitionId) {

		boolean isDefaultPartition = isNull(theRequestPartitionId) ? false : theRequestPartitionId.isDefaultPartition();

		AssertionsForClassTypes.assertThat(theCanonicalSubscriptionWithoutExtension.isCrossPartitionEnabled()).isFalse();
		AssertionsForClassTypes.assertThat(theCanonicalSubscriptionWithExtensionCrossPartitionFalse.isCrossPartitionEnabled()).isFalse();

		if(isDefaultPartition){
			AssertionsForClassTypes.assertThat(theCanonicalSubscriptionWithExtensionCrossPartitionTrue.isCrossPartitionEnabled()).isTrue();
		} else {
			AssertionsForClassTypes.assertThat(theCanonicalSubscriptionWithExtensionCrossPartitionTrue.isCrossPartitionEnabled()).isFalse();
		}
	}
}
