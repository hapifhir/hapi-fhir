package ca.uhn.fhir.jpa.subscription.match.registry;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscriptionChannelType;
import ca.uhn.fhir.jpa.subscription.model.CanonicalTopicSubscriptionFilter;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IFhirVersion;
import ca.uhn.fhir.model.dstu2.FhirDstu2;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.subscription.SubscriptionConstants;
import ca.uhn.fhir.subscription.SubscriptionTestDataHelper;
import org.hl7.fhir.dstu3.hapi.ctx.FhirDstu3;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.hapi.ctx.FhirR4;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Subscription;
import org.hl7.fhir.r4b.hapi.ctx.FhirR4B;
import org.hl7.fhir.r5.hapi.ctx.FhirR5;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.Enumerations;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static ca.uhn.fhir.rest.api.Constants.CT_FHIR_JSON_NEW;
import static ca.uhn.fhir.util.HapiExtensions.EX_SEND_DELETE_MESSAGES;
<<<<<<< HEAD
import static org.assertj.core.api.Assertions.assertThat;
=======
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;
>>>>>>> master

class SubscriptionCanonicalizerTest {

	FhirContext r4Context = FhirContext.forR4();

	private final SubscriptionCanonicalizer testedSC = new SubscriptionCanonicalizer(r4Context, new StorageSettings());

	@Test
	void testCanonicalizeR4SendDeleteMessagesSetsExtensionValueNotPresent() {
		Subscription subscription = new Subscription();

		CanonicalSubscription canonicalSubscription = testedSC.canonicalize(subscription);

		assertThat(canonicalSubscription.getSendDeleteMessages()).isFalse();
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
		assertThat(canonicalSubscription.getSendDeleteMessages()).isTrue();
	}

	@Test
	public void testCanonicalizeDstu2SendDeleteMessages() {
		//setup
		SubscriptionCanonicalizer dstu2Canonicalizer = new SubscriptionCanonicalizer(FhirContext.forDstu2Cached(), new StorageSettings());
		ca.uhn.fhir.model.dstu2.resource.Subscription dstu2Sub = new ca.uhn.fhir.model.dstu2.resource.Subscription();
		ExtensionDt extensionDt = new ExtensionDt();
		extensionDt.setUrl(EX_SEND_DELETE_MESSAGES);
		extensionDt.setValue(new BooleanDt(true));
		dstu2Sub.getChannel().addUndeclaredExtension(extensionDt);

		// execute
		CanonicalSubscription canonicalize = dstu2Canonicalizer.canonicalize(dstu2Sub);

		// verify
		assertThat(canonicalize.getSendDeleteMessages()).isTrue();
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
		SubscriptionCanonicalizer r5Canonicalizer = new SubscriptionCanonicalizer(FhirContext.forR5Cached(), new StorageSettings());
		org.hl7.fhir.r5.model.Subscription.SubscriptionPayloadContent payloadContent =
				org.hl7.fhir.r5.model.Subscription.SubscriptionPayloadContent.fromCode(thePayloadContent);
		org.hl7.fhir.r5.model.Subscription subscription = buildR5Subscription(payloadContent);

		// execute
		CanonicalSubscription canonical = r5Canonicalizer.canonicalize(subscription);

		// verify
		assertThat(canonical.getStatus()).isEqualTo(Subscription.SubscriptionStatus.ACTIVE);
		assertThat(canonical.getContentType()).isEqualTo(CT_FHIR_JSON_NEW);
		assertThat(canonical.getContent()).isEqualTo(payloadContent);
		assertThat(canonical.getEndpointUrl()).isEqualTo("http://foo");
		assertThat(canonical.getTopic()).isEqualTo(SubscriptionTestDataHelper.TEST_TOPIC);
		assertThat(canonical.getChannelType()).isEqualTo(CanonicalSubscriptionChannelType.RESTHOOK);
		assertThat(canonical.getFilters()).hasSize(2);

		CanonicalTopicSubscriptionFilter filter1 = canonical.getFilters().get(0);
		assertThat(filter1.getResourceType()).isEqualTo("Observation");
		assertThat(filter1.getFilterParameter()).isEqualTo("param1");
		assertThat(filter1.getComparator()).isEqualTo(Enumerations.SearchComparator.EQ);
		assertThat(filter1.getModifier()).isEqualTo(Enumerations.SearchModifierCode.EXACT);
		assertThat(filter1.getValue()).isEqualTo("value1");

		CanonicalTopicSubscriptionFilter filter2 = canonical.getFilters().get(1);
		assertThat(filter2.getResourceType()).isEqualTo("CarePlan");
		assertThat(filter2.getFilterParameter()).isEqualTo("param2");
		assertThat(filter2.getComparator()).isEqualTo(Enumerations.SearchComparator.EQ);
		assertThat(filter2.getModifier()).isEqualTo(Enumerations.SearchModifierCode.EXACT);
		assertThat(filter2.getValue()).isEqualTo("value2");
		assertThat(canonical.getHeartbeatPeriod()).isEqualTo(123);
		assertThat(canonical.getMaxCount()).isEqualTo(456);
	}

	@ParameterizedTest
	@ValueSource(strings = {"full-resource", "id-only", "empty"})
	void testR4BCanonicalize_returnsCorrectCanonicalSubscription(String thePayloadContent) {
		// Example drawn from http://build.fhir.org/ig/HL7/fhir-subscription-backport-ig/Subscription-subscription-zulip.json.html

		// setup
		SubscriptionCanonicalizer r4bCanonicalizer = new SubscriptionCanonicalizer(FhirContext.forR4BCached(), new StorageSettings());
		org.hl7.fhir.r4b.model.Subscription subscription = buildR4BSubscription(thePayloadContent);

		// execute
		CanonicalSubscription canonical = r4bCanonicalizer.canonicalize(subscription);

		// verify
		assertThat(canonical.getStatus()).isEqualTo(Subscription.SubscriptionStatus.ACTIVE);
		verifyStandardSubscriptionParameters(canonical);
		verifyChannelParameters(canonical, thePayloadContent);
	}


	private static Stream<Arguments> crossPartitionParams() {
		return Stream.of(
			arguments(true, FhirContext.forDstu2Cached()),
			arguments(false, FhirContext.forDstu2Cached()),
			arguments(true, FhirContext.forDstu3Cached()),
			arguments(false, FhirContext.forDstu3Cached()),
			arguments(true, FhirContext.forR4Cached()),
			arguments(false, FhirContext.forR4Cached()),
			arguments(true, FhirContext.forR4BCached()),
			arguments(false, FhirContext.forR4BCached()),
			arguments(true, FhirContext.forR5Cached()),
			arguments(false, FhirContext.forR5Cached())
		);
	}
	@ParameterizedTest
	@MethodSource("crossPartitionParams")
	void testCrossPartition(boolean theCrossPartitionSubscriptionEnabled, FhirContext theFhirContext) {
		final IFhirVersion version = theFhirContext.getVersion();
		IBaseResource subscription = null;
		if (version instanceof FhirDstu2){
			subscription = new ca.uhn.fhir.model.dstu2.resource.Subscription();
		} else if (version instanceof FhirDstu3){
			subscription = new org.hl7.fhir.dstu3.model.Subscription();
		} else if (version instanceof FhirR4){
			subscription = new Subscription();
		} else if (version instanceof FhirR4B){
			subscription = new org.hl7.fhir.r4b.model.Subscription();
		} else if (version instanceof FhirR5){
			subscription = new org.hl7.fhir.r5.model.Subscription();
		}

		final StorageSettings storageSettings = new StorageSettings();
		storageSettings.setCrossPartitionSubscriptionEnabled(theCrossPartitionSubscriptionEnabled);
		final SubscriptionCanonicalizer subscriptionCanonicalizer = new SubscriptionCanonicalizer(theFhirContext, storageSettings);
		final CanonicalSubscription canonicalSubscription = subscriptionCanonicalizer.canonicalize(subscription);

		assertEquals(theCrossPartitionSubscriptionEnabled, canonicalSubscription.getCrossPartitionEnabled());
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
		SubscriptionCanonicalizer r4Canonicalizer = new SubscriptionCanonicalizer(FhirContext.forR4Cached(), new StorageSettings());

		// execute
		Subscription subscription = SubscriptionTestDataHelper.buildR4TopicSubscriptionWithContent(thePayloadContent);
		CanonicalSubscription canonical = r4Canonicalizer.canonicalize(subscription);

		// verify

		// Standard R4 stuff
		verifyStandardSubscriptionParameters(canonical);
		assertThat(canonical.getStatus()).isEqualTo(Subscription.SubscriptionStatus.ACTIVE);
		verifyChannelParameters(canonical, thePayloadContent);

		assertThat(canonical.getFilters()).hasSize(2);

		CanonicalTopicSubscriptionFilter filter1 = canonical.getFilters().get(0);
		assertThat(filter1.getResourceType()).isEqualTo("Encounter");
		assertThat(filter1.getFilterParameter()).isEqualTo("patient");
		assertThat(filter1.getComparator()).isEqualTo(Enumerations.SearchComparator.EQ);
		assertThat(filter1.getModifier()).isNull();
		assertThat(filter1.getValue()).isEqualTo("Patient/123");

		CanonicalTopicSubscriptionFilter filter2 = canonical.getFilters().get(1);
		assertThat(filter2.getResourceType()).isEqualTo("Encounter");
		assertThat(filter2.getFilterParameter()).isEqualTo("status");
		assertThat(filter2.getComparator()).isEqualTo(Enumerations.SearchComparator.EQ);
		assertThat(filter2.getModifier()).isNull();
		assertThat(filter2.getValue()).isEqualTo("finished");

		assertThat(canonical.getHeartbeatPeriod()).isEqualTo(86400);
		assertThat(canonical.getTimeout()).isEqualTo(60);
		assertThat(canonical.getMaxCount()).isEqualTo(20);
	}

	private void verifyChannelParameters(CanonicalSubscription theCanonicalSubscriptions, String thePayloadContent) {
		assertThat(theCanonicalSubscriptions.getHeaders()).hasSize(2);
		assertThat(theCanonicalSubscriptions.getHeaders().get(0)).isEqualTo(SubscriptionTestDataHelper.TEST_HEADER1);
		assertThat(theCanonicalSubscriptions.getHeaders().get(1)).isEqualTo(SubscriptionTestDataHelper.TEST_HEADER2);

		assertThat(theCanonicalSubscriptions.getContentType()).isEqualTo(CT_FHIR_JSON_NEW);
		assertThat(theCanonicalSubscriptions.getContent().toCode()).isEqualTo(thePayloadContent);
		assertThat(theCanonicalSubscriptions.getEndpointUrl()).isEqualTo(SubscriptionTestDataHelper.TEST_ENDPOINT);
		assertThat(theCanonicalSubscriptions.getTopic()).isEqualTo(SubscriptionTestDataHelper.TEST_TOPIC);
		assertThat(theCanonicalSubscriptions.getChannelType()).isEqualTo(CanonicalSubscriptionChannelType.RESTHOOK);
	}

	private void verifyStandardSubscriptionParameters(CanonicalSubscription theCanonicalSubscription) {
		assertThat(theCanonicalSubscription.getTags()).hasSize(2);
		assertThat(theCanonicalSubscription.getTags()).containsEntry("http://a", "b");
		assertThat(theCanonicalSubscription.getTags()).containsEntry("http://d", "e");
		assertThat(theCanonicalSubscription.getIdPart()).isEqualTo("testId");
		assertThat(theCanonicalSubscription.getIdElementString()).isEqualTo("testId");
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
