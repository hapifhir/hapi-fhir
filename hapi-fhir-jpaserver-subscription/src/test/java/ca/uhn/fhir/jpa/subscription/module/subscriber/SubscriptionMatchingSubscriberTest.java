package ca.uhn.fhir.jpa.subscription.module.subscriber;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.subscription.module.standalone.BaseBlockingQueueSubscribableChannelDstu3Test;
import ca.uhn.fhir.rest.api.Constants;
import com.google.common.collect.Lists;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Subscription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests copied from jpa.subscription.resthook.RestHookTestDstu3Test
 */
public class SubscriptionMatchingSubscriberTest extends BaseBlockingQueueSubscribableChannelDstu3Test {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionMatchingSubscriberTest.class);
	private final IFhirResourceDao<Subscription> myMockSubscriptionDao = Mockito.mock(IFhirResourceDao.class);

	@BeforeEach
	public void beforeEach() {
		Mockito.when(myMockSubscriptionDao.getResourceType()).thenReturn(Subscription.class);
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


	private void mockSubscriptionRead(RequestPartitionId theRequestPartitionId, Subscription subscription) {
		Subscription modifiedSubscription = subscription.copy();
		// the original partition info was the request info, but we need the actual storage partition.
		modifiedSubscription.setUserData(Constants.RESOURCE_PARTITION_ID, theRequestPartitionId);
		Mockito.when(myMockSubscriptionDao.read(subscription.getIdElement())).thenReturn(modifiedSubscription);
	}
}
