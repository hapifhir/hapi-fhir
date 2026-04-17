package ca.uhn.fhir.jpa.subscription.resthook;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.subscription.BaseSubscriptionsR5Test;
import ca.uhn.fhir.rest.api.Constants;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.SubscriptionTopic;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Created by claude-sonnet-4-6
class SubscriptionTopicPartitionR5IT extends BaseSubscriptionsR5Test {

	private static final String PART_1 = "PART-1";
	private static final String PART_2 = "PART-2";
	private static final String PATIENT_TOPIC_URL = "http://example.com/topic/partition-patient-test";
	private static final RequestPartitionId REQ_PART_1 = RequestPartitionId.fromPartitionNames(PART_1);
	private static final RequestPartitionId REQ_PART_2 = RequestPartitionId.fromPartitionNames(PART_2);

	private MyPartitionInterceptor myPartitionInterceptor;

	@BeforeEach
	void beforePartitions() {
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionConfigSvc.createPartition(new PartitionEntity().setId(1).setName(PART_1), null);
		myPartitionConfigSvc.createPartition(new PartitionEntity().setId(2).setName(PART_2), null);
		myPartitionInterceptor = new MyPartitionInterceptor();
		myPartitionInterceptor.setPartitionId(RequestPartitionId.defaultPartition(myPartitionSettings));
		myInterceptorRegistry.registerInterceptor(myPartitionInterceptor);
	}

	@AfterEach
	@Override
	public void afterUnregisterRestHookListener() {
		myInterceptorRegistry.unregisterInterceptorsIf(t -> t instanceof MyPartitionInterceptor);
		myPartitionSettings.setPartitioningEnabled(false);

		myStorageSettings.setExpungeEnabled(true);
		myStorageSettings.setAllowMultipleDelete(true);
		myDaoRegistry.getSystemDao().expunge(new ExpungeOptions().setExpungeEverything(true), null);
		myStorageSettings.setExpungeEnabled(new JpaStorageSettings().isExpungeEnabled());
		myStorageSettings.setAllowMultipleDelete(new JpaStorageSettings().isAllowMultipleDelete());

		// prevent the parent from trying to delete already-expunged resources
		mySubscriptionIds.clear();
		super.afterUnregisterRestHookListener();
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

		// Give the async delivery thread time to complete any in-flight outbound call,
		// so the assertion below fires before teardown if the partition guard is broken.
		Thread.sleep(300);

		// Assertion: zero deliveries expected.
		assertEquals(
				0,
				getSystemProviderCount(),
				"Topic subscription in PART-2 must not fire when a Patient is created in PART-1");
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
