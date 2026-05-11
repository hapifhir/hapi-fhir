package ca.uhn.fhir.jpa.subscription.resthook;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.subscription.BaseSubscriptionsR5Test;
import ca.uhn.fhir.jpa.topic.SubscriptionTopicDispatchRequest;
import ca.uhn.fhir.jpa.topic.SubscriptionTopicDispatcher;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.util.HapiExtensions;
import org.hl7.fhir.r5.model.BooleanType;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.Subscription;
import org.hl7.fhir.r5.model.SubscriptionTopic;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

// Created by claude-sonnet-4-6
class SubscriptionTopicPartitionR5IT extends BaseSubscriptionsR5Test {

	private static final String PART_1 = "PART-1";
	private static final String PART_2 = "PART-2";
	private static final int PART_1_ID = 1;
	private static final int PART_2_ID = 2;
	private static final String PATIENT_TOPIC_URL = "http://example.com/topic/partition-patient-test";
	private static final RequestPartitionId REQ_PART_1 = RequestPartitionId.fromPartitionNames(PART_1);
	private static final RequestPartitionId REQ_PART_2 = RequestPartitionId.fromPartitionNames(PART_2);

	@Autowired
	private SubscriptionTopicDispatcher mySubscriptionTopicDispatcher;

	private MyPartitionInterceptor myPartitionInterceptor;

	@BeforeEach
	void beforePartitions() {
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionConfigSvc.createPartition(new PartitionEntity().setId(PART_1_ID).setName(PART_1), null);
		myPartitionConfigSvc.createPartition(new PartitionEntity().setId(PART_2_ID).setName(PART_2), null);
		myPartitionInterceptor = new MyPartitionInterceptor();
		myPartitionInterceptor.setPartitionId(RequestPartitionId.defaultPartition(myPartitionSettings));
		myInterceptorRegistry.registerInterceptor(myPartitionInterceptor);
	}

	@AfterEach
	@Override
	public void afterUnregisterRestHookListener() {
		myInterceptorRegistry.unregisterInterceptorsIf(t -> t instanceof MyPartitionInterceptor);
		// Disable partitioning before super runs so the base-class delete loop can find
		// resources regardless of which partition they were created in.
		myPartitionSettings.setPartitioningEnabled(false);
		super.afterUnregisterRestHookListener();

		myStorageSettings.setExpungeEnabled(true);
		myStorageSettings.setAllowMultipleDelete(true);
		myDaoRegistry.getSystemDao().expunge(new ExpungeOptions().setExpungeEverything(true), null);
		myStorageSettings.setExpungeEnabled(new JpaStorageSettings().isExpungeEnabled());
		myStorageSettings.setAllowMultipleDelete(new JpaStorageSettings().isAllowMultipleDelete());
	}

	/**
	 * Regression test for: Topic Subscription Partition Escape (issue #8611)
	 *
	 * <p>A Subscription stored in PART-2 must not receive delivery notifications for
	 * resources created in PART-1. Before the fix,
	 * {@code SubscriptionTopicDispatcher} retrieved all subscriptions for a topic
	 * from the global cache without filtering by partition, causing every partition's
	 * subscriptions to fire regardless of where the triggering resource lived.
	 */
	@Test
	void testTopicSubscriptionDoesNotFireAcrossPartitions() throws Exception {
		// Setup: SubscriptionTopic and Subscription both live in PART-2.
		// The SubscriptionTopic must be in the same partition as the Subscription so that
		// SubscriptionValidatingInterceptor can find it during Subscription creation.
		myPartitionInterceptor.setPartitionId(REQ_PART_2);
		createSubscriptionTopic(buildPatientSubscriptionTopic());
		waitForRegisteredSubscriptionTopicCount(1);

		// Setup: Subscription registered only in PART-2
		postSubscription(newTopicSubscription(PATIENT_TOPIC_URL, Constants.CT_FHIR_JSON_NEW));
		waitForActivatedSubscriptionCount(1);

		// Action: create a Patient in PART-1 — must NOT trigger the PART-2 subscription.
		// createResource() waits for SUBSCRIPTION_TOPIC_AFTER_PERSISTED_RESOURCE_CHECKED, which
		// fires after dispatch() returns. If the partition guard blocks delivery, nothing is
		// ever queued on the delivery channel, so the assertion is safe immediately.
		myPartitionInterceptor.setPartitionId(REQ_PART_1);
		Patient patient = new Patient();
		patient.setActive(true);
		createResource(patient, false);

		// Hold the assertion window open long enough for any in-flight delivery to land on a
		// slow/busy CI runner. If the partition guard regresses, the count will increment within
		// the atMost budget and the assertion will still fail — the wider window only removes
		// timing-induced false failures.
		await().during(1, TimeUnit.SECONDS)
				.atMost(5, TimeUnit.SECONDS)
				.untilAsserted(() -> assertThat(getSystemProviderCount())
						.as("Topic subscription in PART-2 must not fire when a Patient is created in PART-1")
						.isZero());
	}

	@Test
	void testTopicSubscriptionFiresForSamePartition() throws Exception {
		myPartitionInterceptor.setPartitionId(REQ_PART_2);
		createSubscriptionTopic(buildPatientSubscriptionTopic());
		waitForRegisteredSubscriptionTopicCount(1);

		postSubscription(newTopicSubscription(PATIENT_TOPIC_URL, Constants.CT_FHIR_JSON_NEW));
		waitForActivatedSubscriptionCount(1);

		// Resource in same partition as the subscription — must fire.
		Patient patient = new Patient();
		patient.setActive(true);
		createResource(patient, true);

		assertThat(getSystemProviderCount())
				.as("Topic subscription in PART-2 must fire when a Patient is created in PART-2")
				.isEqualTo(1);
	}

	@Test
	void testCrossPartitionSubscriptionFiresAcrossPartitions() throws Exception {
		// Cross-partition subscriptions must live in the default partition.
		// myPartitionSettings.isCrossPartitionSubscriptionEnabled() defaults to true.
		createSubscriptionTopic(buildPatientSubscriptionTopic());
		waitForRegisteredSubscriptionTopicCount(1);

		Subscription subscription = newTopicSubscription(PATIENT_TOPIC_URL, Constants.CT_FHIR_JSON_NEW);
		subscription.addExtension(HapiExtensions.EXTENSION_SUBSCRIPTION_CROSS_PARTITION, new BooleanType(true));
		postSubscription(subscription);
		waitForActivatedSubscriptionCount(1);

		// Resource created in PART-1 — cross-partition subscription must still fire.
		myPartitionInterceptor.setPartitionId(REQ_PART_1);
		Patient patient = new Patient();
		patient.setActive(true);
		createResource(patient, true);

		assertThat(getSystemProviderCount())
				.as("Cross-partition subscription must fire for a Patient created in any partition")
				.isEqualTo(1);
	}

	@Test
	void testSubscriptionFiresWhenDispatchedWithoutPartitionContext() throws Exception {
		// Callers that have no partition context (e.g. custom extensions) dispatch with
		// null requestPartitionId. The guard must pass when requestPartitionId is null.
		myPartitionInterceptor.setPartitionId(REQ_PART_2);
		createSubscriptionTopic(buildPatientSubscriptionTopic());
		waitForRegisteredSubscriptionTopicCount(1);

		postSubscription(newTopicSubscription(PATIENT_TOPIC_URL, Constants.CT_FHIR_JSON_NEW));
		waitForActivatedSubscriptionCount(1);

		Patient patient = new Patient();
		patient.setId("patient-no-partition");
		patient.setActive(true);

		mySubscriptionDeliveredLatch.setExpectedCount(1);
		mySubscriptionTopicDispatcher.dispatch(new SubscriptionTopicDispatchRequest(
				PATIENT_TOPIC_URL,
				List.of(patient),
				(f, r) -> InMemoryMatchResult.successfulMatch(),
				RestOperationTypeEnum.CREATE,
				InMemoryMatchResult.successfulMatch(),
				null,
				null));
		mySubscriptionDeliveredLatch.awaitExpected();

		assertThat(getSystemProviderCount())
				.as("Subscription must fire when dispatched without partition context (null requestPartitionId)")
				.isEqualTo(1);
	}

	@Test
	void testSubscriptionFiresWhenMultiPartitionIncludesSubscriptionPartition() throws Exception {
		// If the dispatch carries multiple partition IDs and one of them matches the subscription's
		// partition, the guard must pass and delivery must happen.
		myPartitionInterceptor.setPartitionId(REQ_PART_2);
		createSubscriptionTopic(buildPatientSubscriptionTopic());
		waitForRegisteredSubscriptionTopicCount(1);

		postSubscription(newTopicSubscription(PATIENT_TOPIC_URL, Constants.CT_FHIR_JSON_NEW));
		waitForActivatedSubscriptionCount(1);

		Patient patient = new Patient();
		patient.setId("patient-multi-partition");
		patient.setActive(true);

		// Dispatch with both PART-1 and PART-2 IDs; PART-2 matches the subscription.
		RequestPartitionId multiPartition = RequestPartitionId.fromPartitionIds(PART_1_ID, PART_2_ID);

		mySubscriptionDeliveredLatch.setExpectedCount(1);
		mySubscriptionTopicDispatcher.dispatch(new SubscriptionTopicDispatchRequest(
				PATIENT_TOPIC_URL,
				List.of(patient),
				(f, r) -> InMemoryMatchResult.successfulMatch(),
				RestOperationTypeEnum.CREATE,
				InMemoryMatchResult.successfulMatch(),
				multiPartition,
				null));
		mySubscriptionDeliveredLatch.awaitExpected();

		assertThat(getSystemProviderCount())
				.as("Subscription must fire when dispatched partition set includes the subscription's partition")
				.isEqualTo(1);
	}

	private static SubscriptionTopic buildPatientSubscriptionTopic() {
		SubscriptionTopic topic = new SubscriptionTopic();
		topic.setUrl(PATIENT_TOPIC_URL);
		topic.setStatus(Enumerations.PublicationStatus.ACTIVE);
		SubscriptionTopic.SubscriptionTopicResourceTriggerComponent trigger = topic.addResourceTrigger();
		trigger.setResource("Patient");
		trigger.addSupportedInteraction(SubscriptionTopic.InteractionTrigger.CREATE);
		trigger.addSupportedInteraction(SubscriptionTopic.InteractionTrigger.UPDATE);
		return topic;
	}

	@Interceptor
	static class MyPartitionInterceptor {

		private RequestPartitionId myPartitionId;

		void setPartitionId(RequestPartitionId thePartitionId) {
			myPartitionId = thePartitionId;
		}

		@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_ANY)
		public RequestPartitionId identify() {
			return myPartitionId;
		}
	}
}
