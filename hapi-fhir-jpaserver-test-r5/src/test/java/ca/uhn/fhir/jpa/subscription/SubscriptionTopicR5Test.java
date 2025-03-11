package ca.uhn.fhir.jpa.subscription;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.Logs;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SubscriptionTopicR5Test extends BaseSubscriptionsR5Test {
	private static final Logger ourLog = Logs.getSubscriptionTopicLog();

	@Test
	public void testSubscriptionTopicRegistryBean() {
		assertNotNull(mySubscriptionTopicRegistry);
	}

	@Test
	public void testFilteredTopicSubscription() throws Exception {
		//setup
		// WIP SR4B test update, delete, etc
		createEncounterSubscriptionTopic(Enumerations.EncounterStatus.PLANNED, Enumerations.EncounterStatus.COMPLETED, SubscriptionTopic.InteractionTrigger.CREATE);
		waitForRegisteredSubscriptionTopicCount(1);

		Subscription subscription = createTopicSubscription("Encounter?participant-type=PRPF");

		waitForActivatedSubscriptionCount(1);

		assertEquals(0, getSystemProviderCount());

		// execute
		Encounter badSentEncounter = sendEncounterWithStatus(Enumerations.EncounterStatus.COMPLETED, false);
		Encounter goodSentEncounter = sendEncounterWithStatusAndParticipationType(Enumerations.EncounterStatus.COMPLETED, "PRPF", true);

		// verify
		Bundle receivedBundle = getLastSystemProviderBundle();
		List<IBaseResource> resources = BundleUtil.toListOfResources(myFhirCtx, receivedBundle);
		assertThat(resources).hasSize(2);

		SubscriptionStatus ss = (SubscriptionStatus) resources.get(0);
		validateSubscriptionStatus(subscription, goodSentEncounter, ss, 1L);

		Encounter encounter = (Encounter) resources.get(1);
		assertEquals(Enumerations.EncounterStatus.COMPLETED, encounter.getStatus());
		assertEquals(goodSentEncounter.getIdElement(), encounter.getIdElement());
	}

	private Subscription createTopicSubscription(String... theFilters) throws InterruptedException {
		Subscription subscription = newTopicSubscription(BaseSubscriptionsR5Test.SUBSCRIPTION_TOPIC_TEST_URL, Constants.CT_FHIR_JSON_NEW, theFilters);
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


	private Encounter sendEncounterWithStatusAndParticipationType(Enumerations.EncounterStatus theStatus, String theParticipantType, boolean theExpectDelivery) throws InterruptedException {
		Encounter encounter = new Encounter();
		encounter.setStatus(theStatus);
		encounter.addParticipant().addType().addCoding().setCode(theParticipantType);

		IIdType id = createResource(encounter, theExpectDelivery);
		encounter.setId(id);
		return encounter;
	}


}
