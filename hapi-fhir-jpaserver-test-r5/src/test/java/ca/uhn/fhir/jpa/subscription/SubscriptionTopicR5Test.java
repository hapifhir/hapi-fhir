package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionConstants;
import ca.uhn.fhir.jpa.topic.SubscriptionTopicLoader;
import ca.uhn.fhir.jpa.topic.SubscriptionTopicRegistry;
import ca.uhn.fhir.rest.annotation.Transaction;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.util.BundleUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.Encounter;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.Subscription;
import org.hl7.fhir.r5.model.SubscriptionStatus;
import org.hl7.fhir.r5.model.SubscriptionTopic;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SubscriptionTopicR5Test extends BaseSubscriptionsR5Test {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionTopicR5Test.class);
	public static final String SUBSCRIPTION_TOPIC_TEST_URL = "http://example.com/topic/test";

	@Autowired
	protected SubscriptionTopicRegistry mySubscriptionTopicRegistry;
	@Autowired
	protected SubscriptionTopicLoader mySubscriptionTopicLoader;
	protected IFhirResourceDao<SubscriptionTopic> mySubscriptionTopicDao;
	private static final TestSystemProvider ourTestSystemProvider = new TestSystemProvider();

	@Override
	@BeforeEach
	protected void before() throws Exception {
		super.before();
		ourListenerRestServer.unregisterProvider(mySystemProvider);
		ourListenerRestServer.registerProvider(ourTestSystemProvider);
		mySubscriptionTopicDao = myDaoRegistry.getResourceDao(SubscriptionTopic.class);
	}

	@Override
	@AfterEach
	public void after() throws Exception {
		ourListenerRestServer.unregisterProvider(ourTestSystemProvider);
		ourListenerRestServer.registerProvider(mySystemProvider);
		super.after();
	}

	@Test
	public void testSubscriptionTopicRegistryBean() {
		assertNotNull(mySubscriptionTopicRegistry);
	}

	@Test
	public void testRestHookSubscriptionTopicApplicationFhirJson() throws Exception {
		// WIP SR4B test update, delete, etc
		createEncounterSubscriptionTopic(Encounter.EncounterStatus.PLANNED, Encounter.EncounterStatus.COMPLETED, SubscriptionTopic.InteractionTrigger.CREATE);
		waitForRegisteredSubscriptionTopicCount(1);

		Subscription subscription = createTopicSubscription(SUBSCRIPTION_TOPIC_TEST_URL);
		waitForActivatedSubscriptionCount(1);

		assertEquals(0, ourTestSystemProvider.getCount());
		Encounter sentEncounter = sendEncounterWithStatus(Encounter.EncounterStatus.COMPLETED);

		// Should see 1 subscription notification
		waitForQueueToDrain();

		await().until(() -> ourTestSystemProvider.getCount() > 0);

		Bundle receivedBundle = ourTestSystemProvider.getLastInput();
		List<IBaseResource> resources = BundleUtil.toListOfResources(myFhirCtx, receivedBundle);
		assertEquals(2, resources.size());

		SubscriptionStatus ss = (SubscriptionStatus) resources.get(0);
		validateSubscriptionStatus(subscription, sentEncounter, ss);

		Encounter encounter = (Encounter) resources.get(1);
		assertEquals(Encounter.EncounterStatus.COMPLETED, encounter.getStatus());
		assertEquals(sentEncounter.getIdElement(), encounter.getIdElement());
	}

	private static void validateSubscriptionStatus(Subscription subscription, Encounter sentEncounter, SubscriptionStatus ss) {
		assertEquals(Enumerations.SubscriptionStatusCodes.ACTIVE, ss.getStatus());
		assertEquals(SubscriptionStatus.SubscriptionNotificationType.EVENTNOTIFICATION, ss.getType());
		assertEquals("1", ss.getEventsSinceSubscriptionStartElement().getValueAsString());

		List<SubscriptionStatus.SubscriptionStatusNotificationEventComponent> notificationEvents = ss.getNotificationEvent();
		assertEquals(1, notificationEvents.size());
		SubscriptionStatus.SubscriptionStatusNotificationEventComponent notificationEvent = notificationEvents.get(0);
		assertEquals(1, notificationEvent.getEventNumber());
		assertEquals(sentEncounter.getIdElement().toUnqualifiedVersionless(), notificationEvent.getFocus().getReferenceElement());

		assertEquals(subscription.getIdElement().toUnqualifiedVersionless(), ss.getSubscription().getReferenceElement());
		assertEquals(SUBSCRIPTION_TOPIC_TEST_URL, ss.getTopic());
	}

	private Subscription createTopicSubscription(String theTopicUrl) {
		Subscription subscription = newSubscription(theTopicUrl, Constants.CT_FHIR_JSON_NEW);
		subscription.getMeta().addProfile(SubscriptionConstants.SUBSCRIPTION_TOPIC_PROFILE_URL);
		return postSubscription(subscription);
	}

	private void waitForRegisteredSubscriptionTopicCount(int theTarget) throws Exception {
		await().until(() -> subscriptionTopicRegistryHasSize(theTarget));
	}

	private boolean subscriptionTopicRegistryHasSize(int theTarget) {
		int size = mySubscriptionTopicRegistry.size();
		if (size == theTarget) {
			return true;
		}
		mySubscriptionTopicLoader.doSyncResourcessForUnitTest();
		return mySubscriptionTopicRegistry.size() == theTarget;
	}

	private SubscriptionTopic createEncounterSubscriptionTopic(Encounter.EncounterStatus theFrom, Encounter.EncounterStatus theCurrent, SubscriptionTopic.InteractionTrigger... theInteractionTriggers) {
		SubscriptionTopic retval = new SubscriptionTopic();
		retval.setUrl(SUBSCRIPTION_TOPIC_TEST_URL);
		retval.setStatus(Enumerations.PublicationStatus.ACTIVE);
		SubscriptionTopic.SubscriptionTopicResourceTriggerComponent trigger = retval.addResourceTrigger();
		trigger.setResource("Encounter");
		for (SubscriptionTopic.InteractionTrigger interactionTrigger : theInteractionTriggers) {
			trigger.addSupportedInteraction(interactionTrigger);
		}
		SubscriptionTopic.SubscriptionTopicResourceTriggerQueryCriteriaComponent queryCriteria = trigger.getQueryCriteria();
		queryCriteria.setPrevious("Encounter?status=" + theFrom.toCode());
		queryCriteria.setCurrent("Encounter?status=" + theCurrent.toCode());
		queryCriteria.setRequireBoth(true);
		queryCriteria.setRequireBoth(true);
		mySubscriptionTopicDao.create(retval, mySrd);
		return retval;
	}

	private Encounter sendEncounterWithStatus(Encounter.EncounterStatus theStatus) {
		Encounter encounter = new Encounter();
		encounter.setStatus(theStatus);

		IIdType id = myEncounterDao.create(encounter, mySrd).getId();
		encounter.setId(id);
		return encounter;
	}

	static class TestSystemProvider {
		AtomicInteger myCount = new AtomicInteger(0);
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
	}
}
