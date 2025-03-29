package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.topic.R4SubscriptionTopicBuilder;
import ca.uhn.fhir.jpa.topic.R4SubscriptionTopicLoader;
import ca.uhn.fhir.jpa.topic.SubscriptionTopicRegistry;
import ca.uhn.fhir.rest.annotation.Transaction;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.subscription.SubscriptionConstants;
import ca.uhn.fhir.util.BundleUtil;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Basic;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Subscription;
import org.hl7.fhir.r5.model.SubscriptionStatus.SubscriptionNotificationType;
import org.hl7.fhir.r5.model.SubscriptionTopic;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SubscriptionTopicR4Test extends BaseSubscriptionsR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionTopicR4Test.class);
	public static final String SUBSCRIPTION_TOPIC_TEST_URL = "https://example.com/topic/test";

	@Autowired
	protected SubscriptionTopicRegistry mySubscriptionTopicRegistry;
	@Autowired
	protected R4SubscriptionTopicLoader mySubscriptionTopicLoader;
	@Autowired
	private IInterceptorService myInterceptorService;
	private static final TestSystemProvider ourTestSystemProvider = new TestSystemProvider();

	@Override
	@BeforeEach
	protected void before() throws Exception {
		super.before();
		ourRestfulServer.unregisterProvider(mySystemProvider);
		ourRestfulServer.registerProvider(ourTestSystemProvider);
		myInterceptorService.registerAnonymousInterceptor(Pointcut.SUBSCRIPTION_TOPIC_AFTER_PERSISTED_RESOURCE_CHECKED, mySubscriptionTopicsCheckedLatch);
		myInterceptorService.registerAnonymousInterceptor(Pointcut.SUBSCRIPTION_AFTER_REST_HOOK_DELIVERY, mySubscriptionDeliveredLatch);
	}

	@Override
	@AfterEach
	public void after() throws Exception {
		ourRestfulServer.unregisterProvider(ourTestSystemProvider);
		ourRestfulServer.registerProvider(mySystemProvider);
		myInterceptorService.unregisterAllAnonymousInterceptors();
		mySubscriptionTopicsCheckedLatch.clear();
		mySubscriptionDeliveredLatch.clear();
		ourTestSystemProvider.clear();
		super.after();
	}

	@Test
	void testSubscriptionTopicRegistryBean() {
		assertNotNull(mySubscriptionTopicRegistry);
	}

	@Test
	void testCreate() throws Exception {
		// WIP R4 test update, delete, etc
		createEncounterSubscriptionTopic(SubscriptionTopic.InteractionTrigger.CREATE);
		mySubscriptionTopicLoader.doSyncResourcesForUnitTest();
		waitForRegisteredSubscriptionTopicCount();

		Subscription subscription = createTopicSubscription("Encounter?participant-type=PRPF");
		waitForActivatedSubscriptionCount(1);

		assertEquals(0, ourTestSystemProvider.getCount());
		Encounter sentEncounter = buildEncounter(Encounter.EncounterStatus.FINISHED, "PRPF");

		IIdType sentEncounterId = createResource(sentEncounter, true);

		assertEquals(1, ourTestSystemProvider.getCount());

		Bundle receivedBundle = ourTestSystemProvider.getLastInput();
		List<IBaseResource> resources = BundleUtil.toListOfResources(myFhirContext, receivedBundle);
		assertThat(resources).hasSize(2);

		Parameters parameters = (Parameters) resources.get(0);
		validateSubscriptionStatus(subscription, sentEncounter, parameters);

		Encounter encounter = (Encounter) resources.get(1);
		assertEquals(Encounter.EncounterStatus.FINISHED, encounter.getStatus());
		assertEquals(sentEncounterId, encounter.getIdElement());
	}

	@Test
	void testUpdate() throws Exception {
		createEncounterSubscriptionTopic(SubscriptionTopic.InteractionTrigger.CREATE, SubscriptionTopic.InteractionTrigger.UPDATE);
		mySubscriptionTopicLoader.doSyncResourcesForUnitTest();
		waitForRegisteredSubscriptionTopicCount();

		Subscription subscription = createTopicSubscription("Encounter?participant-type=PRPF");
		waitForActivatedSubscriptionCount(1);

		assertEquals(0, ourTestSystemProvider.getCount());
		Encounter sentEncounter = buildEncounter(Encounter.EncounterStatus.PLANNED, "PRPF");

		createResource(sentEncounter, false);
		assertEquals(0, ourTestSystemProvider.getCount());

		sentEncounter.setStatus(Encounter.EncounterStatus.FINISHED);
		updateEncounter(sentEncounter);

		assertEquals(1, ourTestSystemProvider.getCount());

		Bundle receivedBundle = ourTestSystemProvider.getLastInput();
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(receivedBundle));
		List<IBaseResource> resources = BundleUtil.toListOfResources(myFhirContext, receivedBundle);
		assertThat(resources).hasSize(2);

		Parameters parameters = (Parameters) resources.get(0);
		validateSubscriptionStatus(subscription, sentEncounter, parameters);

		Encounter encounter = (Encounter) resources.get(1);
		assertEquals(Encounter.EncounterStatus.FINISHED, encounter.getStatus());
		assertEquals(sentEncounter.getIdElement(), encounter.getIdElement());
	}
	
	@Test
	void testSubscriptionTopicShapeWithIncludeAndRevInclude() throws Exception {
		// Create organization, patient, and observations
		Organization organization = new Organization();
		organization.setName("Test Organization");
		IIdType orgId = createResource(organization, false);
		
		Patient patient = new Patient();
		patient.setManagingOrganization(new Reference(orgId));
		IIdType patientId = createResource(patient, false);
		
		// Create multiple observations linked to the patient
		Observation obs1 = new Observation();
		obs1.setSubject(new Reference(patientId));
		obs1.setStatus(Observation.ObservationStatus.FINAL);
		obs1.getCode().setText("Observation 1");
		IIdType obs1Id = createResource(obs1, false);
		
		Observation obs2 = new Observation();
		obs2.setSubject(new Reference(patientId));
		obs2.setStatus(Observation.ObservationStatus.FINAL);
		obs2.getCode().setText("Observation 2");
		IIdType obs2Id = createResource(obs2, false);
		
		// Create subscription topic that triggers on Patient updates
		R4SubscriptionTopicBuilder builder = new R4SubscriptionTopicBuilder()
			.setUrl(SUBSCRIPTION_TOPIC_TEST_URL)
			.setStatus(Enumerations.PublicationStatus.ACTIVE)
			.addResourceTrigger()
			.setResourceTriggerResource("Patient")
			.addResourceTriggerSupportedInteraction(SubscriptionTopic.InteractionTrigger.UPDATE.toCode());
		
		// Add notification shape to include Organization and revInclude Observations
		builder.addNotificationShape()
			.setNotificationShapeResource("Patient")
			.addNotificationShapeInclude("Patient:organization")
			.addNotificationShapeRevInclude("Observation:subject");
		
		// Create the topic
		Basic topicResource = builder.build();
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(topicResource));
		createResource(topicResource, false);

		// Sync the topic
		mySubscriptionTopicLoader.doSyncResourcesForUnitTest();
		waitForRegisteredSubscriptionTopicCount();
		
		// Create the subscription
		Subscription subscription = createTopicSubscription("Patient?_id=" + patientId.getIdPart());
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(subscription));
		waitForActivatedSubscriptionCount(1);
		
		// Update the patient to trigger the subscription
		patient.setId(patientId);
		patient.addName().setFamily("Updated");
		updateResource(patient, true);

		// Verify the subscription bundle
		Bundle receivedBundle = ourTestSystemProvider.getLastInput();
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(receivedBundle));
		List<IBaseResource> resources = BundleUtil.toListOfResources(myFhirContext, receivedBundle);
		
		// Should have 5 resources: Parameters + Patient + Organization + 2 Observations
		assertThat(resources).hasSize(5);
		
		// First resource should be the Parameters
		Parameters parameters = (Parameters) resources.get(0);
		validateSubscriptionStatus(subscription, patient, parameters);
		
		// Verify that the bundle contains the patient, organization, and observations
		Set<String> resourceTypes = resources.stream()
			.skip(1) // Skip the Parameters
			.map(r -> r.getClass().getSimpleName())
			.collect(Collectors.toSet());
		
		assertThat(resourceTypes).contains("Patient", "Organization", "Observation");
		
		// Verify the specific resources
		assertThat(resources.stream().filter(r -> r instanceof Patient).count()).isEqualTo(1);
		assertThat(resources.stream().filter(r -> r instanceof Organization).count()).isEqualTo(1);
		assertThat(resources.stream().filter(r -> r instanceof Observation).count()).isEqualTo(2);
		
		// Verify the references match expected values
		Patient bundlePatient = (Patient) resources.stream().filter(r -> r instanceof Patient).findFirst().orElse(null);
		assertThat(bundlePatient).isNotNull();
		assertThat(bundlePatient.getIdElement().getIdPart()).isEqualTo(patientId.getIdPart());
		
		Organization bundleOrg = (Organization) resources.stream().filter(r -> r instanceof Organization).findFirst().orElse(null);
		assertThat(bundleOrg).isNotNull();
		assertThat(bundleOrg.getIdElement().getIdPart()).isEqualTo(orgId.getIdPart());
		
		// Check that the observations in the bundle have the correct subject references
		List<Observation> bundleObservations = resources.stream()
			.filter(r -> r instanceof Observation)
			.map(r -> (Observation)r)
			.collect(Collectors.toList());
		
		assertThat(bundleObservations).hasSize(2);
		for (Observation bundleObs : bundleObservations) {
			assertThat(bundleObs.getSubject().getReferenceElement().getIdPart()).isEqualTo(patientId.getIdPart());
		}
	}

	private static void validateSubscriptionStatus(Subscription subscription, IBaseResource sentResource, Parameters parameters) {
		// Get status parameter
		CodeType status = (CodeType) parameters.getParameter("status").getValue();
		assertEquals(Subscription.SubscriptionStatus.ACTIVE.toCode(), status.getCode());
		
		// Get type parameter
		CodeType type = (CodeType) parameters.getParameter("type").getValue();
		assertEquals(SubscriptionNotificationType.EVENTNOTIFICATION.toCode(), type.getCode());
		
		// Get events-since-subscription-start parameter
		String eventsSinceStart = parameters.getParameter("events-since-subscription-start").getValue().toString();
		assertEquals("1", eventsSinceStart);

		// Get notification-event parameter
		Parameters.ParametersParameterComponent notificationEvent = parameters.getParameter("notification-event");
		assertThat(notificationEvent).isNotNull();
		
		// Navigate through notification-event parts
		String eventNumber = null;
		Reference focus = null;
		for (Parameters.ParametersParameterComponent part : notificationEvent.getPart()) {
			if ("event-number".equals(part.getName())) {
				eventNumber = part.getValue().toString();
			} else if ("focus".equals(part.getName())) {
				focus = (Reference) part.getValue();
			}
		}
		
		assertEquals("1", eventNumber);
		assertThat(focus).isNotNull();
		assertEquals(sentResource.getIdElement().toUnqualifiedVersionless(), focus.getReferenceElement());

		// Check subscription reference
		Reference subscriptionRef = (Reference) parameters.getParameter("subscription").getValue();
		assertEquals(subscription.getId(), subscriptionRef.getReferenceElement().getIdPart());
		
		// Check topic URL
		CanonicalType topic = (CanonicalType) parameters.getParameter("topic").getValue();
		assertEquals(SUBSCRIPTION_TOPIC_TEST_URL, topic.getValue());
	}

	private Subscription createTopicSubscription(String theFilter) throws InterruptedException {
		Subscription subscription = newSubscription(SubscriptionTopicR4Test.SUBSCRIPTION_TOPIC_TEST_URL, Constants.CT_FHIR_JSON_NEW);
		subscription.getMeta().addProfile(SubscriptionConstants.SUBSCRIPTION_TOPIC_PROFILE_URL);
		subscription.getCriteriaElement().addExtension(SubscriptionConstants.SUBSCRIPTION_TOPIC_FILTER_URL, new StringType(theFilter));

		mySubscriptionTopicsCheckedLatch.setExpectedCount(2);
		Subscription retval = postOrPutSubscription(subscription);
		mySubscriptionTopicsCheckedLatch.awaitExpected();

		return retval;
	}

	private void waitForRegisteredSubscriptionTopicCount() {
		await().until(this::subscriptionTopicRegistryHasOneEntry);
	}

	private boolean subscriptionTopicRegistryHasOneEntry() {
		int size = mySubscriptionTopicRegistry.size();
		if (size == 1) {
			return true;
		}
		mySubscriptionTopicLoader.doSyncResourcesForUnitTest();
		return mySubscriptionTopicRegistry.size() == 1;
	}

	private void createEncounterSubscriptionTopic(SubscriptionTopic.InteractionTrigger... theInteractionTriggers) throws InterruptedException {
		R4SubscriptionTopicBuilder builder = new R4SubscriptionTopicBuilder()
			.setUrl(SUBSCRIPTION_TOPIC_TEST_URL)
			.setStatus(Enumerations.PublicationStatus.ACTIVE)
			.addResourceTrigger()
			.setResourceTriggerResource("Encounter");
			
		// Add the interaction triggers
		for (SubscriptionTopic.InteractionTrigger interactionTrigger : theInteractionTriggers) {
			builder.addResourceTriggerSupportedInteraction(interactionTrigger.toCode());
		}
		
		// Set up the query criteria
		builder.addResourceTriggerQueryCriteria()
			.setResourceTriggerQueryCriteriaPrevious("Encounter?status=" + Encounter.EncounterStatus.PLANNED.toCode())
			.setResourceTriggerQueryCriteriaCurrent("Encounter?status=" + Encounter.EncounterStatus.FINISHED.toCode())
			.setResourceTriggerQueryCriteriaRequireBoth(true);
		
		// Create the topic
		Basic topicResource = builder.build();
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(topicResource));
		createResource(topicResource, false);
	}

	@Nonnull
	private static Encounter buildEncounter(Encounter.EncounterStatus theStatus, String theParticipantType) {
		Encounter encounter = new Encounter();
		encounter.setStatus(theStatus);
		encounter.addParticipant().addType().addCoding().setCode(theParticipantType);
		return encounter;
	}

	private void updateEncounter(Encounter theEncounter) throws InterruptedException {
		mySubscriptionDeliveredLatch.setExpectedCount(1);
		mySubscriptionTopicsCheckedLatch.setExpectedCount(1);
		myEncounterDao.update(theEncounter, mySrd);
		mySubscriptionTopicsCheckedLatch.awaitExpected();
		mySubscriptionDeliveredLatch.awaitExpected();
	}

	static class TestSystemProvider {
		final AtomicInteger myCount = new AtomicInteger(0);
		Bundle myLastInput;

		@Transaction
		public Bundle transaction(@TransactionParam Bundle theInput) {
			myCount.incrementAndGet();
			myLastInput = theInput;
			return theInput;
		}

		public int getCount() {
			return myCount.get();
		}

		public Bundle getLastInput() {
			return myLastInput;
		}

		public void clear() {
			myCount.set(0);
			myLastInput = null;
		}
	}
}
