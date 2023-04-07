package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.topic.SubscriptionTopicRegistry;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.test.utilities.server.HashMapResourceProviderExtension;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4b.model.Encounter;
import org.hl7.fhir.r4b.model.SubscriptionTopic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubscriptionTopicR4BTest extends BaseSubscriptionsR4BTest {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionTopicR4BTest.class);
	public static final String SUBSCRIPTION_TOPIC_TEST_URL = "http://example.com/topic/test";

	@Order(1)
	@RegisterExtension
	protected static HashMapResourceProviderExtension<Encounter> ourEncounterProvider = new HashMapResourceProviderExtension<>(ourRestfulServer, Encounter.class);

	@Autowired
	protected SubscriptionTopicRegistry mySubscriptionTopicRegistry;
	protected IFhirResourceDao<SubscriptionTopic> mySubscriptionTopicDao;

	@Override
	@BeforeEach
	protected void before() throws Exception {
		super.before();

		mySubscriptionTopicDao = myDaoRegistry.getResourceDao(SubscriptionTopic.class);
	}

	@Test
	public void testRestHookSubscriptionTopicApplicationFhirJson() throws Exception {
		createEncounterSubscriptionTopic(Encounter.EncounterStatus.PLANNED, Encounter.EncounterStatus.FINISHED);
		waitForRegisteredSubscriptionTopics(1);

		sendEncounterWithStatus(Encounter.EncounterStatus.INPROGRESS);

		// Should see 1 subscription notification
		waitForQueueToDrain();
		assertEquals(0, ourEncounterProvider.getCountCreate());
		ourEncounterProvider.waitForUpdateCount(1);

		assertEquals(Constants.CT_FHIR_JSON_NEW, ourRestfulServer.getRequestContentTypes().get(0));
	}

	private void waitForRegisteredSubscriptionTopics(int theTarget) throws Exception {
		waitForSize(theTarget, () -> mySubscriptionTopicRegistry.size(), () -> "SubscriptionTopicRegistry");
	}

	private SubscriptionTopic createEncounterSubscriptionTopic(Encounter.EncounterStatus theFrom, Encounter.EncounterStatus theCurrent) {
		SubscriptionTopic retval = new SubscriptionTopic();
		retval.setUrl(SUBSCRIPTION_TOPIC_TEST_URL);
		SubscriptionTopic.SubscriptionTopicResourceTriggerComponent trigger = retval.addResourceTrigger();
		trigger.setResource("Encounter");
		trigger.addSupportedInteraction(SubscriptionTopic.InteractionTrigger.UPDATE);
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
}
