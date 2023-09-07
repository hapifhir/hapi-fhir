package ca.uhn.fhir.jpa.topic;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.searchparam.matcher.SearchParamMatcher;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.r5.model.Encounter;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.SubscriptionTopic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubscriptionTriggerMatcherTest {
	private static final FhirContext ourFhirContext = FhirContext.forR5();
	@Mock
	DaoRegistry myDaoRegistry;
	@Mock
	SearchParamMatcher mySearchParamMatcher;

	private SubscriptionTopicSupport mySubscriptionTopicSupport;
	private Encounter myEncounter;

	@BeforeEach
	public void before() {
		mySubscriptionTopicSupport = new SubscriptionTopicSupport(ourFhirContext, myDaoRegistry, mySearchParamMatcher);
		myEncounter = new Encounter();
		myEncounter.setIdElement(new IdType("Encounter", "123", "2"));
	}

	@Test
		public void testCreateEmptryTriggerNoMatch() {
		// setup
		ResourceModifiedMessage msg = new ResourceModifiedMessage(ourFhirContext, myEncounter, ResourceModifiedMessage.OperationTypeEnum.CREATE);

		SubscriptionTopic.SubscriptionTopicResourceTriggerComponent trigger = new SubscriptionTopic.SubscriptionTopicResourceTriggerComponent();

		// run
		SubscriptionTriggerMatcher svc = new SubscriptionTriggerMatcher(mySubscriptionTopicSupport, msg, trigger);
		InMemoryMatchResult result = svc.match();

		// verify
		assertFalse(result.matched());
	}

	@Test
	public void testCreateSimpleTriggerMatches() {
		// setup
		ResourceModifiedMessage msg = new ResourceModifiedMessage(ourFhirContext, myEncounter, ResourceModifiedMessage.OperationTypeEnum.CREATE);

		SubscriptionTopic.SubscriptionTopicResourceTriggerComponent trigger = new SubscriptionTopic.SubscriptionTopicResourceTriggerComponent();
		trigger.setResource("Encounter");
		trigger.addSupportedInteraction(SubscriptionTopic.InteractionTrigger.CREATE);

		// run
		SubscriptionTriggerMatcher svc = new SubscriptionTriggerMatcher(mySubscriptionTopicSupport, msg, trigger);
		InMemoryMatchResult result = svc.match();

		// verify
		assertTrue(result.matched());
	}

	@Test
	public void testCreateWrongOpNoMatch() {
		ResourceModifiedMessage msg = new ResourceModifiedMessage(ourFhirContext, myEncounter, ResourceModifiedMessage.OperationTypeEnum.CREATE);

		// setup
		SubscriptionTopic.SubscriptionTopicResourceTriggerComponent trigger = new SubscriptionTopic.SubscriptionTopicResourceTriggerComponent();
		trigger.setResource("Encounter");
		trigger.addSupportedInteraction(SubscriptionTopic.InteractionTrigger.UPDATE);

		// run
		SubscriptionTriggerMatcher svc = new SubscriptionTriggerMatcher(mySubscriptionTopicSupport, msg, trigger);
		InMemoryMatchResult result = svc.match();

		// verify
		assertFalse(result.matched());
	}

	@Test
	public void testUpdateMatch() {
		ResourceModifiedMessage msg = new ResourceModifiedMessage(ourFhirContext, myEncounter, ResourceModifiedMessage.OperationTypeEnum.UPDATE);

		// setup
		SubscriptionTopic.SubscriptionTopicResourceTriggerComponent trigger = new SubscriptionTopic.SubscriptionTopicResourceTriggerComponent();
		trigger.setResource("Encounter");
		trigger.addSupportedInteraction(SubscriptionTopic.InteractionTrigger.UPDATE);

		// run
		SubscriptionTriggerMatcher svc = new SubscriptionTriggerMatcher(mySubscriptionTopicSupport, msg, trigger);
		InMemoryMatchResult result = svc.match();

		// verify
		assertTrue(result.matched());
	}

	@Test
	public void testUpdateWithPrevCriteriaMatch() {
		ResourceModifiedMessage msg = new ResourceModifiedMessage(ourFhirContext, myEncounter, ResourceModifiedMessage.OperationTypeEnum.UPDATE);

		// setup
		SubscriptionTopic.SubscriptionTopicResourceTriggerComponent trigger = new SubscriptionTopic.SubscriptionTopicResourceTriggerComponent();
		trigger.setResource("Encounter");
		trigger.addSupportedInteraction(SubscriptionTopic.InteractionTrigger.UPDATE);
		trigger.getQueryCriteria().setPrevious("Encounter?status=in-progress");


		IFhirResourceDao mockEncounterDao = mock(IFhirResourceDao.class);
		when(myDaoRegistry.getResourceDao("Encounter")).thenReturn(mockEncounterDao);
		Encounter encounterPreviousVersion = new Encounter();
		when(mockEncounterDao.read(any(), any(), eq(false))).thenReturn(encounterPreviousVersion);
		when(mySearchParamMatcher.match(any(), any(), any())).thenReturn(InMemoryMatchResult.successfulMatch());

		// run
		SubscriptionTriggerMatcher svc = new SubscriptionTriggerMatcher(mySubscriptionTopicSupport, msg, trigger);
		InMemoryMatchResult result = svc.match();

		// verify
		assertTrue(result.matched());
	}

}
