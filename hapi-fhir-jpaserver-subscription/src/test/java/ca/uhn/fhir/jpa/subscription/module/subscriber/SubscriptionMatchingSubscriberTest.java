package ca.uhn.fhir.jpa.subscription.module.subscriber;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.subscription.match.matcher.subscriber.SubscriptionCriteriaParser;
import ca.uhn.fhir.jpa.subscription.match.matcher.subscriber.SubscriptionMatchingSubscriber;
import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionRegistry;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.subscription.module.standalone.BaseBlockingQueueSubscribableChannelDstu3Test;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.messaging.BaseResourceModifiedMessage;
import ca.uhn.fhir.util.HapiExtensions;
import com.google.common.collect.Lists;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Subscription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static ca.uhn.fhir.jpa.subscription.match.matcher.subscriber.SubscriptionCriteriaParser.TypeEnum.STARTYPE_EXPRESSION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests copied from jpa.subscription.resthook.RestHookTestDstu3Test
 */
public class SubscriptionMatchingSubscriberTest extends BaseBlockingQueueSubscribableChannelDstu3Test {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionMatchingSubscriberTest.class);
	private final IFhirResourceDao<Subscription> myMockSubscriptionDao = Mockito.mock(IFhirResourceDao.class);

	@BeforeEach
	public void beforeEach() {
		when(myMockSubscriptionDao.getResourceType()).thenReturn(Subscription.class);
		myDaoRegistry.register(myMockSubscriptionDao);
	}

	@Test
	public void testRestHookSubscriptionApplicationFhirJson() throws Exception {
		String payload = "application/fhir+json";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
		String criteria2 = "Observation?code=SNOMED-CT|" + code + "111&_format=xml";

		Subscription subscription1 = makeActiveSubscription(criteria1, payload, ourListenerServerBase);
		sendSubscription(subscription1, null, false);
		Subscription subscription2 = makeActiveSubscription(criteria2, payload, ourListenerServerBase);
		sendSubscription(subscription2, null, false);

		assertEquals(2, mySubscriptionRegistry.size());

		ourObservationListener.setExpectedCount(1);
		sendObservation(code, "SNOMED-CT");
		ourObservationListener.awaitExpected();

		assertEquals(1, ourContentTypes.size());
		assertEquals(Constants.CT_FHIR_JSON_NEW, ourContentTypes.get(0));
	}

	@Test
	public void testRestHookSubscriptionApplicationXmlJson() throws Exception {
		String payload = "application/fhir+xml";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
		String criteria2 = "Observation?code=SNOMED-CT|" + code + "111&_format=xml";

		Subscription subscription1 = makeActiveSubscription(criteria1, payload, ourListenerServerBase);
		sendSubscription(subscription1, null, false);
		Subscription subscription2 = makeActiveSubscription(criteria2, payload, ourListenerServerBase);
		sendSubscription(subscription2, null, false);

		assertEquals(2, mySubscriptionRegistry.size());

		ourObservationListener.setExpectedCount(1);
		sendObservation(code, "SNOMED-CT");
		ourObservationListener.awaitExpected();

		assertEquals(1, ourContentTypes.size());
		assertEquals(Constants.CT_FHIR_XML_NEW, ourContentTypes.get(0));
	}

	@Test
	public void testRestHookSubscription_NoResourceTypeInPayloadId() throws Exception {
		Observation observation = new Observation();
		observation.setId("OBS");
		observation.setStatus(Observation.ObservationStatus.CORRECTED);

		Subscription subscription = makeActiveSubscription("Observation?", "application/fhir+xml", ourListenerServerBase);
		sendSubscription(subscription, null, false);

		assertEquals(1, mySubscriptionRegistry.size());
		ourObservationListener.setExpectedCount(1);
		sendResource(observation);
		ourObservationListener.awaitExpected();

		assertEquals(1, ourContentTypes.size());
		assertEquals(Constants.CT_FHIR_XML_NEW, ourContentTypes.get(0));
	}

	@Test
	public void testRestHookSubscriptionWithoutPayload() throws Exception {
		String payload = "";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code;
		String criteria2 = "Observation?code=SNOMED-CT|" + code + "111";

		Subscription subscription1 = makeActiveSubscription(criteria1, payload, ourListenerServerBase);
		sendSubscription(subscription1, null, false);
		Subscription subscription2 = makeActiveSubscription(criteria2, payload, ourListenerServerBase);
		sendSubscription(subscription2, null, false);

		assertEquals(2, mySubscriptionRegistry.size());

		mySubscriptionAfterDelivery.setExpectedCount(1);
		ourObservationListener.setExpectedCount(0);
		sendObservation(code, "SNOMED-CT");
		ourObservationListener.clear();
		mySubscriptionAfterDelivery.awaitExpected();

		assertEquals(0, ourContentTypes.size());
	}


	@Test
	public void testCriteriaStarOnly() throws InterruptedException {
		String payload = "application/fhir+xml";

		String code = "1000000050";
		String criteria1 = "[*]";
		String criteria2 = "[*]";
		String criteria3 = "Observation?code=FOO"; // won't match

		Subscription subscription1 = makeActiveSubscription(criteria1, payload, ourListenerServerBase);
		sendSubscription(subscription1, null, false);
		Subscription subscription2 = makeActiveSubscription(criteria2, payload, ourListenerServerBase);
		sendSubscription(subscription2, null, false);
		Subscription subscription3 = makeActiveSubscription(criteria3, payload, ourListenerServerBase);
		sendSubscription(subscription3, null, false);

		assertEquals(3, mySubscriptionRegistry.size());

		ourObservationListener.setExpectedCount(2);
		sendObservation(code, "SNOMED-CT");
		ourObservationListener.awaitExpected();

		assertEquals(2, ourContentTypes.size());
		assertEquals(Constants.CT_FHIR_XML_NEW, ourContentTypes.get(0));
	}

	@Test
	public void testSubscriptionAndResourceOnTheSamePartitionMatch() throws InterruptedException {
		myPartitionSettings.setPartitioningEnabled(true);
		String payload = "application/fhir+json";

		String code = "1000000050";
		String criteria = "Observation?code=SNOMED-CT|" + code + "&_format=xml";

		RequestPartitionId requestPartitionId = RequestPartitionId.fromPartitionId(0);
		Subscription subscription = makeActiveSubscription(criteria, payload, ourListenerServerBase);
		mockSubscriptionRead(requestPartitionId, subscription);
		sendSubscription(subscription, requestPartitionId, true);

		ourObservationListener.setExpectedCount(1);
		mySubscriptionResourceMatched.setExpectedCount(1);
		sendObservation(code, "SNOMED-CT", requestPartitionId);
		mySubscriptionResourceMatched.awaitExpected();
		ourObservationListener.awaitExpected();
	}

	@Test
	public void testSubscriptionAndResourceOnTheSamePartitionMatchPart2() throws InterruptedException {
		myPartitionSettings.setPartitioningEnabled(true);
		String payload = "application/fhir+json";

		String code = "1000000050";
		String criteria = "Observation?code=SNOMED-CT|" + code + "&_format=xml";

		RequestPartitionId requestPartitionId = RequestPartitionId.fromPartitionId(1);
		Subscription subscription = makeActiveSubscription(criteria, payload, ourListenerServerBase);
		mockSubscriptionRead(requestPartitionId, subscription);
		sendSubscription(subscription, requestPartitionId, true);

		ourObservationListener.setExpectedCount(1);
		mySubscriptionResourceMatched.setExpectedCount(1);
		sendObservation(code, "SNOMED-CT", requestPartitionId);
		mySubscriptionResourceMatched.awaitExpected();
		ourObservationListener.awaitExpected();
	}

	@Test
	public void testSubscriptionAndResourceOnDiffPartitionNotMatch() throws InterruptedException {
		myPartitionSettings.setPartitioningEnabled(true);
		String payload = "application/fhir+json";

		String code = "1000000050";
		String criteria = "Observation?code=SNOMED-CT|" + code + "&_format=xml";

		RequestPartitionId requestPartitionId = RequestPartitionId.fromPartitionId(1);
		Subscription subscription = makeActiveSubscription(criteria, payload, ourListenerServerBase);
		mockSubscriptionRead(requestPartitionId, subscription);
		sendSubscription(subscription, requestPartitionId, true);

		mySubscriptionResourceNotMatched.setExpectedCount(1);
		sendObservation(code, "SNOMED-CT", RequestPartitionId.fromPartitionId(0));
		mySubscriptionResourceNotMatched.awaitExpected();
	}

	@Test
	public void testSubscriptionAndResourceOnDiffPartitionNotMatchPart2() throws InterruptedException {
		myPartitionSettings.setPartitioningEnabled(true);
		String payload = "application/fhir+json";

		String code = "1000000050";
		String criteria = "Observation?code=SNOMED-CT|" + code + "&_format=xml";

		RequestPartitionId requestPartitionId = RequestPartitionId.fromPartitionId(0);
		Subscription subscription = makeActiveSubscription(criteria, payload, ourListenerServerBase);
		mockSubscriptionRead(requestPartitionId, subscription);
		sendSubscription(subscription, requestPartitionId, true);

		mySubscriptionResourceNotMatched.setExpectedCount(1);
		sendObservation(code, "SNOMED-CT", RequestPartitionId.fromPartitionId(1));
		mySubscriptionResourceNotMatched.awaitExpected();
	}

	@Test
	public void testSubscriptionOnOnePartitionMatchResourceOnMultiplePartitions() throws InterruptedException {
		myPartitionSettings.setPartitioningEnabled(true);
		String payload = "application/fhir+json";

		String code = "1000000050";
		String criteria = "Observation?code=SNOMED-CT|" + code + "&_format=xml";

		RequestPartitionId requestPartitionId = RequestPartitionId.fromPartitionId(1);
		Subscription subscription = makeActiveSubscription(criteria, payload, ourListenerServerBase);
		mockSubscriptionRead(requestPartitionId, subscription);
		sendSubscription(subscription, requestPartitionId, true);

		ourObservationListener.setExpectedCount(1);
		mySubscriptionResourceMatched.setExpectedCount(1);
		List<Integer> partitionId = Collections.synchronizedList(Lists.newArrayList(0, 1, 2));
		sendObservation(code, "SNOMED-CT", RequestPartitionId.fromPartitionIds(partitionId));
		mySubscriptionResourceMatched.awaitExpected();
		ourObservationListener.awaitExpected();
	}

	@Test
	public void testSubscriptionOnOnePartitionDoNotMatchResourceOnMultiplePartitions() throws InterruptedException {
		myPartitionSettings.setPartitioningEnabled(true);
		String payload = "application/fhir+json";

		String code = "1000000050";
		String criteria = "Observation?code=SNOMED-CT|" + code + "&_format=xml";

		RequestPartitionId requestPartitionId = RequestPartitionId.fromPartitionId(1);
		Subscription subscription = makeActiveSubscription(criteria, payload, ourListenerServerBase);
		mockSubscriptionRead(requestPartitionId, subscription);
		sendSubscription(subscription, requestPartitionId, true);

		mySubscriptionResourceNotMatched.setExpectedCount(1);
		List<Integer> partitionId = Collections.synchronizedList(Lists.newArrayList(0, 2));
		sendObservation(code, "SNOMED-CT", RequestPartitionId.fromPartitionIds(partitionId));
		mySubscriptionResourceNotMatched.awaitExpected();
	}

	@Test
	public void testCrossPartitionSubscriptionForResourceOnTheSamePartitionMatch() throws InterruptedException {
		myPartitionSettings.setPartitioningEnabled(true);
		myDaoConfig.setCrossPartitionSubscription(true);
		String payload = "application/fhir+json";

		String code = "1000000050";
		String criteria = "Observation?code=SNOMED-CT|" + code + "&_format=xml";

		RequestPartitionId subscriptionPartitionId = RequestPartitionId.defaultPartition();
		Subscription subscription = makeActiveSubscription(criteria, payload, ourListenerServerBase);
		subscription.addExtension().setUrl(HapiExtensions.EXTENSION_SUBSCRIPTION_CROSS_PARTITION).setValue(new BooleanType(true));
		mockSubscriptionRead(subscriptionPartitionId, subscription);
		sendSubscription(subscription, subscriptionPartitionId, true);

		ourObservationListener.setExpectedCount(1);
		mySubscriptionResourceMatched.setExpectedCount(1);
		sendObservation(code, "SNOMED-CT", subscriptionPartitionId);
		mySubscriptionResourceMatched.awaitExpected();
		ourObservationListener.awaitExpected();
	}

	@Test
	public void testCrossPartitionSubscriptionForResourceOnDifferentPartitionMatch() throws InterruptedException {
		myPartitionSettings.setPartitioningEnabled(true);
		myDaoConfig.setCrossPartitionSubscription(true);
		String payload = "application/fhir+json";

		String code = "1000000050";
		String criteria = "Observation?code=SNOMED-CT|" + code + "&_format=xml";

		RequestPartitionId subscriptionPartitionId = RequestPartitionId.defaultPartition();
		RequestPartitionId requestPartitionId = RequestPartitionId.fromPartitionId(1);
		Subscription subscription = makeActiveSubscription(criteria, payload, ourListenerServerBase);
		subscription.addExtension().setUrl(HapiExtensions.EXTENSION_SUBSCRIPTION_CROSS_PARTITION).setValue(new BooleanType(true));
		mockSubscriptionRead(subscriptionPartitionId, subscription);
		sendSubscription(subscription, subscriptionPartitionId, true);

		ourObservationListener.setExpectedCount(1);
		mySubscriptionResourceMatched.setExpectedCount(1);
		sendObservation(code, "SNOMED-CT", requestPartitionId);
		mySubscriptionResourceMatched.awaitExpected();
		ourObservationListener.awaitExpected();
	}

	@Test
	public void testCrossPartitionSubscriptionForMultipleResourceOnDifferentPartitionMatch() throws InterruptedException {
		myPartitionSettings.setPartitioningEnabled(true);
		myDaoConfig.setCrossPartitionSubscription(true);
		String payload = "application/fhir+json";

		String code = "1000000050";
		String criteria = "Observation?code=SNOMED-CT|" + code + "&_format=xml";

		RequestPartitionId subscriptionPartitionId = RequestPartitionId.defaultPartition();
		RequestPartitionId requestPartitionId = RequestPartitionId.fromPartitionId(1);
		RequestPartitionId requestPartitionId2 = RequestPartitionId.fromPartitionId(2);
		Subscription subscription = makeActiveSubscription(criteria, payload, ourListenerServerBase);
		subscription.addExtension().setUrl(HapiExtensions.EXTENSION_SUBSCRIPTION_CROSS_PARTITION).setValue(new BooleanType(true));
		mockSubscriptionRead(subscriptionPartitionId, subscription);
		sendSubscription(subscription, subscriptionPartitionId, true);

		ourObservationListener.setExpectedCount(2);
		mySubscriptionResourceMatched.setExpectedCount(2);
		sendObservation(code, "SNOMED-CT", requestPartitionId);
		sendObservation(code, "SNOMED-CT", requestPartitionId2);
		mySubscriptionResourceMatched.awaitExpected();
		ourObservationListener.awaitExpected();
	}

	@Nested
	public class TestDeleteMessages {
		private final SubscriptionMatchingSubscriber subscriber = new SubscriptionMatchingSubscriber();
		@Mock
		ResourceModifiedMessage message;
		@Mock
		IInterceptorBroadcaster myInterceptorBroadcaster;
		@Mock
		SubscriptionRegistry mySubscriptionRegistry;
		@Mock(answer = Answers.RETURNS_DEEP_STUBS)
		ActiveSubscription myActiveSubscription;
		@Mock
		CanonicalSubscription myCanonicalSubscription;
		@Mock
		SubscriptionCriteriaParser.SubscriptionCriteria mySubscriptionCriteria;

		@Test
		public void testAreNotIgnored() {
			ReflectionTestUtils.setField(subscriber, "myInterceptorBroadcaster", myInterceptorBroadcaster);
			ReflectionTestUtils.setField(subscriber, "mySubscriptionRegistry", mySubscriptionRegistry);

			when(message.getOperationType()).thenReturn(BaseResourceModifiedMessage.OperationTypeEnum.DELETE);
			when(myInterceptorBroadcaster.callHooks(
				eq(Pointcut.SUBSCRIPTION_BEFORE_PERSISTED_RESOURCE_CHECKED), any(HookParams.class))).thenReturn(true);
			when(mySubscriptionRegistry.getAll()).thenReturn(Collections.emptyList());

			subscriber.matchActiveSubscriptionsAndDeliver(message);

			verify(myInterceptorBroadcaster).callHooks(
				eq(Pointcut.SUBSCRIPTION_BEFORE_PERSISTED_RESOURCE_CHECKED), any(HookParams.class));
			verify(myInterceptorBroadcaster).callHooks(
				eq(Pointcut.SUBSCRIPTION_AFTER_PERSISTED_RESOURCE_CHECKED), any(HookParams.class));
		}

		@Test
		public void matchActiveSubscriptionsChecksSendDeleteMessagesExtensionFlag() {
			ReflectionTestUtils.setField(subscriber, "myInterceptorBroadcaster", myInterceptorBroadcaster);
			ReflectionTestUtils.setField(subscriber, "mySubscriptionRegistry", mySubscriptionRegistry);

			when(message.getOperationType()).thenReturn(BaseResourceModifiedMessage.OperationTypeEnum.DELETE);
			when(myInterceptorBroadcaster.callHooks(
				eq(Pointcut.SUBSCRIPTION_BEFORE_PERSISTED_RESOURCE_CHECKED), any(HookParams.class))).thenReturn(true);
			when(message.getPayloadId(null)).thenReturn(new IdDt("Patient", 123L));
			when(mySubscriptionRegistry.getAll()).thenReturn(Collections.singletonList(myActiveSubscription));
			when(myActiveSubscription.getSubscription()).thenReturn(myCanonicalSubscription);
			when(myActiveSubscription.getCriteria()).thenReturn(mySubscriptionCriteria);
			when(myActiveSubscription.getId()).thenReturn("Patient/123");
			when(mySubscriptionCriteria.getType()).thenReturn(STARTYPE_EXPRESSION);

			subscriber.matchActiveSubscriptionsAndDeliver(message);

			verify(myCanonicalSubscription, atLeastOnce()).getSendDeleteMessages();
		}

		@Test
		public void matchActiveSubscriptionsAndDeliverSetsPartitionId() {
			ReflectionTestUtils.setField(subscriber, "myInterceptorBroadcaster", myInterceptorBroadcaster);
			ReflectionTestUtils.setField(subscriber, "mySubscriptionRegistry", mySubscriptionRegistry);

			when(message.getOperationType()).thenReturn(BaseResourceModifiedMessage.OperationTypeEnum.DELETE);
			when(myInterceptorBroadcaster.callHooks(
				eq(Pointcut.SUBSCRIPTION_BEFORE_PERSISTED_RESOURCE_CHECKED), any(HookParams.class))).thenReturn(true);
			when(message.getPayloadId(null)).thenReturn(new IdDt("Patient", 123L));
			when(mySubscriptionRegistry.getAll()).thenReturn(Collections.singletonList(myActiveSubscription));
			when(myActiveSubscription.getSubscription()).thenReturn(myCanonicalSubscription);
			when(myActiveSubscription.getCriteria()).thenReturn(mySubscriptionCriteria);
			when(myActiveSubscription.getId()).thenReturn("Patient/123");
			when(mySubscriptionCriteria.getType()).thenReturn(STARTYPE_EXPRESSION);
			when(myCanonicalSubscription.getSendDeleteMessages()).thenReturn(true);

			subscriber.matchActiveSubscriptionsAndDeliver(message);

			verify(message, atLeastOnce()).getPayloadId(null);
		}
	}


	private void mockSubscriptionRead(RequestPartitionId theRequestPartitionId, Subscription subscription) {
		Subscription modifiedSubscription = subscription.copy();
		// the original partition info was the request info, but we need the actual storage partition.
		modifiedSubscription.setUserData(Constants.RESOURCE_PARTITION_ID, theRequestPartitionId);
		when(myMockSubscriptionDao.read(eq(subscription.getIdElement()), any())).thenReturn(modifiedSubscription);
	}
}
