package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.topic.ISubscriptionTopicLoader;
import ca.uhn.fhir.jpa.topic.R4SubscriptionTopicBuilder;
import ca.uhn.fhir.jpa.topic.SubscriptionTopicRegistry;
import ca.uhn.fhir.rest.annotation.Transaction;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.subscription.SubscriptionConstants;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.test.concurrency.PointcutLatch;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Basic;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Subscription;
import org.hl7.fhir.r5.model.SubscriptionTopic;
import org.hl7.fhir.r5.model.SubscriptionStatus.SubscriptionNotificationType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SubscriptionTopicR4Test extends BaseSubscriptionsR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionTopicR4Test.class);
	public static final String SUBSCRIPTION_TOPIC_TEST_URL = "https://example.com/topic/test";

	@Autowired
	protected SubscriptionTopicRegistry mySubscriptionTopicRegistry;
	@Autowired
	protected ISubscriptionTopicLoader mySubscriptionTopicLoader;
	@Autowired
	private IInterceptorService myInterceptorService;
	protected IFhirResourceDao<Basic> myBasicDao;
	private static final TestSystemProvider ourTestSystemProvider = new TestSystemProvider();

	private final PointcutLatch mySubscriptionTopicsCheckedLatch = new PointcutLatch(Pointcut.SUBSCRIPTION_TOPIC_AFTER_PERSISTED_RESOURCE_CHECKED);
	private final PointcutLatch mySubscriptionDeliveredLatch = new PointcutLatch(Pointcut.SUBSCRIPTION_AFTER_REST_HOOK_DELIVERY);

	@Override
	@BeforeEach
	protected void before() throws Exception {
		super.before();
		ourRestfulServer.unregisterProvider(mySystemProvider);
		ourRestfulServer.registerProvider(ourTestSystemProvider);
		myBasicDao = myDaoRegistry.getResourceDao(Basic.class);
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
	public void testSubscriptionTopicRegistryBean() {
		assertNotNull(mySubscriptionTopicRegistry);
	}

	@Test
	public void testCreate() throws Exception {
		// WIP R4 test update, delete, etc
		createEncounterSubscriptionTopic(SubscriptionTopic.InteractionTrigger.CREATE);
		mySubscriptionTopicLoader.doSyncResourcesForUnitTest();
		waitForRegisteredSubscriptionTopicCount();

		Subscription subscription = createTopicSubscription("Encounter?participant-type=PRPF");
		waitForActivatedSubscriptionCount(1);

		assertEquals(0, ourTestSystemProvider.getCount());
		Encounter sentEncounter = sendEncounterWithStatusAndParticipantType(Encounter.EncounterStatus.FINISHED, "PRPF", true);

		assertEquals(1, ourTestSystemProvider.getCount());

		Bundle receivedBundle = ourTestSystemProvider.getLastInput();
		List<IBaseResource> resources = BundleUtil.toListOfResources(myFhirContext, receivedBundle);
		assertThat(resources).hasSize(2);

		Parameters parameters = (Parameters) resources.get(0);
		validateSubscriptionStatus(subscription, sentEncounter, parameters);

		Encounter encounter = (Encounter) resources.get(1);
		assertEquals(Encounter.EncounterStatus.FINISHED, encounter.getStatus());
		assertEquals(sentEncounter.getIdElement(), encounter.getIdElement());
	}

	@Test
	public void testUpdate() throws Exception {
		createEncounterSubscriptionTopic(SubscriptionTopic.InteractionTrigger.CREATE, SubscriptionTopic.InteractionTrigger.UPDATE);
		mySubscriptionTopicLoader.doSyncResourcesForUnitTest();
		waitForRegisteredSubscriptionTopicCount();

		Subscription subscription = createTopicSubscription("Encounter?participant-type=PRPF");
		waitForActivatedSubscriptionCount(1);

		assertEquals(0, ourTestSystemProvider.getCount());
		Encounter sentEncounter = sendEncounterWithStatusAndParticipantType(Encounter.EncounterStatus.PLANNED, "PRPF", false);
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

	private static void validateSubscriptionStatus(Subscription subscription, Encounter sentEncounter, Parameters parameters) {
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
		assertEquals(sentEncounter.getIdElement().toUnqualifiedVersionless(), focus.getReferenceElement());

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
		mySubscriptionTopicsCheckedLatch.setExpectedCount(1);
		myBasicDao.create(topicResource, mySrd);
		mySubscriptionTopicsCheckedLatch.awaitExpected();
	}

	private Encounter sendEncounterWithStatusAndParticipantType(Encounter.EncounterStatus theStatus, String theParticipantType, boolean theExpectDelivery) throws InterruptedException {
		Encounter encounter = new Encounter();
		encounter.setStatus(theStatus);
		encounter.addParticipant().addType().addCoding().setCode(theParticipantType);

		if (theExpectDelivery) {
			mySubscriptionDeliveredLatch.setExpectedCount(1);
		}
		mySubscriptionTopicsCheckedLatch.setExpectedCount(1);
		IIdType id = myEncounterDao.create(encounter, mySrd).getId();
		mySubscriptionTopicsCheckedLatch.awaitExpected();
		if (theExpectDelivery) {
			mySubscriptionDeliveredLatch.awaitExpected();
		}
		encounter.setId(id);
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
