package ca.uhn.fhir.jpa.subscription;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.topic.SubscriptionTopicLoader;
import ca.uhn.fhir.jpa.topic.SubscriptionTopicRegistry;
import ca.uhn.fhir.rest.annotation.Transaction;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.subscription.SubscriptionConstants;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.test.concurrency.PointcutLatch;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4b.model.Bundle;
import org.hl7.fhir.r4b.model.Encounter;
import org.hl7.fhir.r4b.model.Enumerations;
import org.hl7.fhir.r4b.model.Subscription;
import org.hl7.fhir.r4b.model.SubscriptionStatus;
import org.hl7.fhir.r4b.model.SubscriptionTopic;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

public class SubscriptionTopicR4BTest extends BaseSubscriptionsR4BTest {
	public static final String SUBSCRIPTION_TOPIC_TEST_URL = "https://example.com/topic/test";

	@Autowired
	protected SubscriptionTopicRegistry mySubscriptionTopicRegistry;
	@Autowired
	protected SubscriptionTopicLoader mySubscriptionTopicLoader;
	@Autowired
	private IInterceptorService myInterceptorService;
	protected IFhirResourceDao<SubscriptionTopic> mySubscriptionTopicDao;
	private static final TestSystemProvider ourTestSystemProvider = new TestSystemProvider();

	private final PointcutLatch mySubscriptionTopicsCheckedLatch = new PointcutLatch(Pointcut.SUBSCRIPTION_TOPIC_AFTER_PERSISTED_RESOURCE_CHECKED);
	private final PointcutLatch mySubscriptionDeliveredLatch = new PointcutLatch(Pointcut.SUBSCRIPTION_AFTER_REST_HOOK_DELIVERY);

	@Override
	@BeforeEach
	protected void before() throws Exception {
		super.before();
		ourRestfulServer.unregisterProvider(mySystemProvider);
		ourRestfulServer.registerProvider(ourTestSystemProvider);
		mySubscriptionTopicDao = myDaoRegistry.getResourceDao(SubscriptionTopic.class);
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
		// WIP SR4B test update, delete, etc
		createEncounterSubscriptionTopic(SubscriptionTopic.InteractionTrigger.CREATE);
		mySubscriptionTopicLoader.doSyncResourcesForUnitTest();
		waitForRegisteredSubscriptionTopicCount();

		Subscription subscription = createTopicSubscription();
		waitForActivatedSubscriptionCount(1);

		assertEquals(0, ourTestSystemProvider.getCount());
		Encounter sentEncounter = sendEncounterWithStatus(Encounter.EncounterStatus.FINISHED, true);

		assertEquals(1, ourTestSystemProvider.getCount());

		Bundle receivedBundle = ourTestSystemProvider.getLastInput();
		List<IBaseResource> resources = BundleUtil.toListOfResources(myFhirCtx, receivedBundle);
		assertThat(resources).hasSize(2);

		SubscriptionStatus ss = (SubscriptionStatus) resources.get(0);
		validateSubscriptionStatus(subscription, sentEncounter, ss);

		Encounter encounter = (Encounter) resources.get(1);
		assertEquals(Encounter.EncounterStatus.FINISHED, encounter.getStatus());
		assertEquals(sentEncounter.getIdElement(), encounter.getIdElement());
	}

	@Test
	public void testUpdate() throws Exception {
		// WIP SR4B test update, delete, etc
		createEncounterSubscriptionTopic(SubscriptionTopic.InteractionTrigger.CREATE, SubscriptionTopic.InteractionTrigger.UPDATE);
		mySubscriptionTopicLoader.doSyncResourcesForUnitTest();
		waitForRegisteredSubscriptionTopicCount();

		Subscription subscription = createTopicSubscription();
		waitForActivatedSubscriptionCount(1);

		assertEquals(0, ourTestSystemProvider.getCount());
		Encounter sentEncounter = sendEncounterWithStatus(Encounter.EncounterStatus.PLANNED, false);
		assertEquals(0, ourTestSystemProvider.getCount());

		sentEncounter.setStatus(Encounter.EncounterStatus.FINISHED);
		updateEncounter(sentEncounter);

		assertEquals(1, ourTestSystemProvider.getCount());

		Bundle receivedBundle = ourTestSystemProvider.getLastInput();
		List<IBaseResource> resources = BundleUtil.toListOfResources(myFhirCtx, receivedBundle);
		assertThat(resources).hasSize(2);

		SubscriptionStatus ss = (SubscriptionStatus) resources.get(0);
		validateSubscriptionStatus(subscription, sentEncounter, ss);

		Encounter encounter = (Encounter) resources.get(1);
		assertEquals(Encounter.EncounterStatus.FINISHED, encounter.getStatus());
		assertEquals(sentEncounter.getIdElement(), encounter.getIdElement());
	}


	private static void validateSubscriptionStatus(Subscription subscription, Encounter sentEncounter, SubscriptionStatus ss) {
		assertEquals(Enumerations.SubscriptionStatus.ACTIVE, ss.getStatus());
		assertEquals(SubscriptionStatus.SubscriptionNotificationType.EVENTNOTIFICATION, ss.getType());
		assertEquals("1", ss.getEventsSinceSubscriptionStartElement().getValueAsString());

		List<SubscriptionStatus.SubscriptionStatusNotificationEventComponent> notificationEvents = ss.getNotificationEvent();
		assertThat(notificationEvents).hasSize(1);
		SubscriptionStatus.SubscriptionStatusNotificationEventComponent notificationEvent = notificationEvents.get(0);
		assertEquals("1", notificationEvent.getEventNumber());
		assertEquals(sentEncounter.getIdElement().toUnqualifiedVersionless(), notificationEvent.getFocus().getReferenceElement());

		assertEquals(subscription.getIdElement().toUnqualifiedVersionless(), ss.getSubscription().getReferenceElement());
		assertEquals(SUBSCRIPTION_TOPIC_TEST_URL, ss.getTopic());
	}

	private Subscription createTopicSubscription() throws InterruptedException {
		Subscription subscription = newSubscription(SubscriptionTopicR4BTest.SUBSCRIPTION_TOPIC_TEST_URL, Constants.CT_FHIR_JSON_NEW);
		subscription.getMeta().addProfile(SubscriptionConstants.SUBSCRIPTION_TOPIC_PROFILE_URL);

		mySubscriptionTopicsCheckedLatch.setExpectedCount(1);
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
		SubscriptionTopic retval = new SubscriptionTopic();
		retval.setUrl(SUBSCRIPTION_TOPIC_TEST_URL);
		retval.setStatus(Enumerations.PublicationStatus.ACTIVE);
		SubscriptionTopic.SubscriptionTopicResourceTriggerComponent trigger = retval.addResourceTrigger();
		trigger.setResource("Encounter");
		for (SubscriptionTopic.InteractionTrigger interactionTrigger : theInteractionTriggers) {
			trigger.addSupportedInteraction(interactionTrigger);
		}
		SubscriptionTopic.SubscriptionTopicResourceTriggerQueryCriteriaComponent queryCriteria = trigger.getQueryCriteria();
		queryCriteria.setPrevious("Encounter?status=" + Encounter.EncounterStatus.PLANNED.toCode());
		queryCriteria.setCurrent("Encounter?status=" + Encounter.EncounterStatus.FINISHED.toCode());
		queryCriteria.setRequireBoth(true);
		mySubscriptionTopicsCheckedLatch.setExpectedCount(1);
		mySubscriptionTopicDao.create(retval, mySrd);
		mySubscriptionTopicsCheckedLatch.awaitExpected();
	}

	private Encounter sendEncounterWithStatus(Encounter.EncounterStatus theStatus, boolean theExpectDelivery) throws InterruptedException {
		Encounter encounter = new Encounter();
		encounter.setStatus(theStatus);

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
