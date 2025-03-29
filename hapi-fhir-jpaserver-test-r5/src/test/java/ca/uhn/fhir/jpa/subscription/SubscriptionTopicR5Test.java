package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.Logs;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.Encounter;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.Observation;
import org.hl7.fhir.r5.model.Organization;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.Reference;
import org.hl7.fhir.r5.model.Subscription;
import org.hl7.fhir.r5.model.SubscriptionStatus;
import org.hl7.fhir.r5.model.SubscriptionTopic;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SubscriptionTopicR5Test extends BaseSubscriptionsR5Test {
	private static final Logger ourLog = Logs.getSubscriptionTopicLog();

	@Test
	void testSubscriptionTopicRegistryBean() {
		assertNotNull(mySubscriptionTopicRegistry);
	}

	@Test
	void testFilteredTopicSubscription() throws Exception {
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
		obs1.setStatus(Enumerations.ObservationStatus.FINAL);
		obs1.getCode().setText("Observation 1");
		IIdType obs1Id = createResource(obs1, false);
		
		Observation obs2 = new Observation();
		obs2.setSubject(new Reference(patientId));
		obs2.setStatus(Enumerations.ObservationStatus.FINAL);
		obs2.getCode().setText("Observation 2");
		IIdType obs2Id = createResource(obs2, false);
		
		// Create subscription topic that triggers on Patient updates
		SubscriptionTopic topic = new SubscriptionTopic();
		topic.setUrl(SUBSCRIPTION_TOPIC_TEST_URL);
		topic.setStatus(Enumerations.PublicationStatus.ACTIVE);
		
		// Add resource trigger for Patient
		SubscriptionTopic.SubscriptionTopicResourceTriggerComponent trigger = topic.addResourceTrigger();
		trigger.setResource("Patient");
		trigger.addSupportedInteraction(SubscriptionTopic.InteractionTrigger.UPDATE);
		
		// Add notification shape to include Organization and revInclude Observations
		SubscriptionTopic.SubscriptionTopicNotificationShapeComponent shape = topic.addNotificationShape();
		shape.setResource("Patient");
		shape.addInclude("Patient:organization");
		shape.addRevInclude("Observation:subject");
		
		createResource(topic, false);
		waitForRegisteredSubscriptionTopicCount(1);
		
		// Create the subscription
		Subscription subscription = createTopicSubscription("Patient?_id=" + patientId.getIdPart());
		waitForActivatedSubscriptionCount(1);
		
		// Update the patient to trigger the subscription
		patient.setId(patientId);
		patient.getNameFirstRep().setFamily("Updated");
		updateResource(patient, true);
		
		// Verify the subscription bundle
		Bundle receivedBundle = getLastSystemProviderBundle();
		List<IBaseResource> resources = BundleUtil.toListOfResources(myFhirCtx, receivedBundle);
		
		// Should have 4 resources: SubscriptionStatus + Patient + Organization + 2 Observations
		assertThat(resources).hasSize(5);
		
		// First resource should be the SubscriptionStatus
		SubscriptionStatus ss = (SubscriptionStatus) resources.get(0);
		validateSubscriptionStatus(subscription, patient, ss, 1L);
		
		// Verify that the bundle contains the patient, organization, and observations
		Set<String> resourceTypes = resources.stream()
			.skip(1) // Skip the SubscriptionStatus
			.map(r -> r.getClass().getSimpleName())
			.collect(java.util.stream.Collectors.toSet());
		
		assertThat(resourceTypes).contains("Patient", "Organization", "Observation");
		
		// Verify the specific resources
		assertThat(resources.stream().filter(Patient.class::isInstance).count()).isEqualTo(1);
		assertThat(resources.stream().filter(Organization.class::isInstance).count()).isEqualTo(1);
		assertThat(resources.stream().filter(Observation.class::isInstance).count()).isEqualTo(2);
		
		// Verify the references match expected values
		Patient bundlePatient = (Patient) resources.stream().filter(Patient.class::isInstance).findFirst().orElse(null);
		assertThat(bundlePatient).isNotNull();
		assertThat(bundlePatient.getIdElement().getIdPart()).isEqualTo(patientId.getIdPart());
		
		Organization bundleOrg = (Organization) resources.stream().filter(Organization.class::isInstance).findFirst().orElse(null);
		assertThat(bundleOrg).isNotNull();
		assertThat(bundleOrg.getIdElement().getIdPart()).isEqualTo(orgId.getIdPart());
		
		// Check that the observations in the bundle have the correct subject references
		Set<Observation> bundleObservations = resources.stream()
			.filter(Observation.class::isInstance)
			.map(r -> (Observation)r)
			.collect(java.util.stream.Collectors.toSet());
		
		assertThat(bundleObservations).hasSize(2);
		for (Observation bundleObs : bundleObservations) {
			assertThat(bundleObs.getSubject().getReferenceElement().getIdPart()).isEqualTo(patientId.getIdPart());
		}
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
