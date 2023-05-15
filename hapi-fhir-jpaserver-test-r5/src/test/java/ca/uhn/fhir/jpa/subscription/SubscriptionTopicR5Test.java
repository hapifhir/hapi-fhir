package ca.uhn.fhir.jpa.subscription;

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
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SubscriptionTopicR5Test extends BaseSubscriptionsR5Test {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionTopicR5Test.class);

	@Test
	public void testSubscriptionTopicRegistryBean() {
		assertNotNull(mySubscriptionTopicRegistry);
	}

	@Test
	public void testRestHookSubscriptionTopicApplicationFhirJson() throws Exception {
		//setup
		// WIP SR4B test update, delete, etc
		createEncounterSubscriptionTopic(Enumerations.EncounterStatus.PLANNED, Enumerations.EncounterStatus.COMPLETED, SubscriptionTopic.InteractionTrigger.CREATE);
		waitForRegisteredSubscriptionTopicCount(1);

		Subscription subscription = createTopicSubscription(SUBSCRIPTION_TOPIC_TEST_URL);
		waitForActivatedSubscriptionCount(1);

		assertEquals(0, getSystemProviderCount());

		// execute
		Encounter sentEncounter = sendEncounterWithStatus(Enumerations.EncounterStatus.COMPLETED, true);

		// verify
		Bundle receivedBundle = getLastSystemProviderBundle();
		List<IBaseResource> resources = BundleUtil.toListOfResources(myFhirCtx, receivedBundle);
		assertEquals(2, resources.size());

		SubscriptionStatus ss = (SubscriptionStatus) resources.get(0);
		validateSubscriptionStatus(subscription, sentEncounter, ss);

		Encounter encounter = (Encounter) resources.get(1);
		assertEquals(Enumerations.EncounterStatus.COMPLETED, encounter.getStatus());
		assertEquals(sentEncounter.getIdElement(), encounter.getIdElement());
	}



	private Subscription createTopicSubscription(String theTopicUrl) throws InterruptedException {
		Subscription subscription = newTopicSubscription(theTopicUrl, Constants.CT_FHIR_JSON_NEW);
		return postSubscription(subscription);
	}

	private SubscriptionTopic createEncounterSubscriptionTopic(Enumerations.EncounterStatus theFrom, Enumerations.EncounterStatus theCurrent, SubscriptionTopic.InteractionTrigger... theInteractionTriggers) throws InterruptedException {
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
		super.createResource(retval, false);
		return retval;
	}

	private Encounter sendEncounterWithStatus(Enumerations.EncounterStatus theStatus, boolean theExpectDelivery) throws InterruptedException {
		Encounter encounter = new Encounter();
		encounter.setStatus(theStatus);

		IIdType id = createResource(encounter, theExpectDelivery);
		encounter.setId(id);
		return encounter;
	}


}
