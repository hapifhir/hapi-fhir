package ca.uhn.fhir.jpa.topic;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.searchparam.matcher.SearchParamMatcher;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import org.hl7.fhir.r5.model.Encounter;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.SubscriptionTopic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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

	MemoryCacheService myMemoryCacheService;

	private SubscriptionTopicSupport mySubscriptionTopicSupport;
	private Encounter myEncounter;

	@BeforeEach
	public void before() {
		myMemoryCacheService = new MemoryCacheService(new JpaStorageSettings());
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
		SubscriptionTriggerMatcher svc = new SubscriptionTriggerMatcher(mySubscriptionTopicSupport, msg, trigger, myMemoryCacheService);
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
		SubscriptionTriggerMatcher svc = new SubscriptionTriggerMatcher(mySubscriptionTopicSupport, msg, trigger, myMemoryCacheService);
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
		SubscriptionTriggerMatcher svc = new SubscriptionTriggerMatcher(mySubscriptionTopicSupport, msg, trigger, myMemoryCacheService);
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
		SubscriptionTriggerMatcher svc = new SubscriptionTriggerMatcher(mySubscriptionTopicSupport, msg, trigger, myMemoryCacheService);
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
		SubscriptionTriggerMatcher svc = new SubscriptionTriggerMatcher(mySubscriptionTopicSupport, msg, trigger, myMemoryCacheService);
		InMemoryMatchResult result = svc.match();

		// verify
		assertTrue(result.matched());
	}

	@Test
	public void testFalseFhirPathCriteriaEvaluation() {
		ResourceModifiedMessage msg = new ResourceModifiedMessage(ourFhirContext, myEncounter, ResourceModifiedMessage.OperationTypeEnum.UPDATE);

		// setup
		SubscriptionTopic.SubscriptionTopicResourceTriggerComponent trigger = new SubscriptionTopic.SubscriptionTopicResourceTriggerComponent();
		trigger.setResource("Encounter");
		trigger.addSupportedInteraction(SubscriptionTopic.InteractionTrigger.UPDATE);
		trigger.setFhirPathCriteria("false");

		// run
		SubscriptionTriggerMatcher svc = new SubscriptionTriggerMatcher(mySubscriptionTopicSupport, msg, trigger, myMemoryCacheService);
		InMemoryMatchResult result = svc.match();

		// verify
		assertFalse(result.matched());
	}

	@Test
	public void testInvalidFhirPathCriteriaEvaluation() {
		ResourceModifiedMessage msg = new ResourceModifiedMessage(ourFhirContext, myEncounter, ResourceModifiedMessage.OperationTypeEnum.UPDATE);

		// setup
		SubscriptionTopic.SubscriptionTopicResourceTriggerComponent trigger = new SubscriptionTopic.SubscriptionTopicResourceTriggerComponent();
		trigger.setResource("Encounter");
		trigger.addSupportedInteraction(SubscriptionTopic.InteractionTrigger.UPDATE);
		trigger.setFhirPathCriteria("random text");

		// run
		SubscriptionTriggerMatcher svc = new SubscriptionTriggerMatcher(mySubscriptionTopicSupport, msg, trigger, myMemoryCacheService);
		InMemoryMatchResult result = svc.match();

		// verify
		assertFalse(result.matched());
		assertEquals("Error @1, 2: Premature ExpressionNode termination at unexpected token \"text\"", result.getUnsupportedReason());
	}

	@Test
	public void testInvalidBooleanOutcomeOfFhirPathCriteriaEvaluation() {
		ResourceModifiedMessage msg = new ResourceModifiedMessage(ourFhirContext, myEncounter, ResourceModifiedMessage.OperationTypeEnum.UPDATE);

		// setup
		SubscriptionTopic.SubscriptionTopicResourceTriggerComponent trigger = new SubscriptionTopic.SubscriptionTopicResourceTriggerComponent();
		trigger.setResource("Encounter");
		trigger.addSupportedInteraction(SubscriptionTopic.InteractionTrigger.UPDATE);
		trigger.setFhirPathCriteria("id");

		// run
		SubscriptionTriggerMatcher svc = new SubscriptionTriggerMatcher(mySubscriptionTopicSupport, msg, trigger, myMemoryCacheService);
		InMemoryMatchResult result = svc.match();

		// verify
		assertFalse(result.matched());
	}

	@Test
	public void testValidFhirPathCriteriaEvaluation() {
		ResourceModifiedMessage msg = new ResourceModifiedMessage(ourFhirContext, myEncounter, ResourceModifiedMessage.OperationTypeEnum.UPDATE);

		// setup
		SubscriptionTopic.SubscriptionTopicResourceTriggerComponent trigger = new SubscriptionTopic.SubscriptionTopicResourceTriggerComponent();
		trigger.setResource("Encounter");
		trigger.addSupportedInteraction(SubscriptionTopic.InteractionTrigger.UPDATE);
		trigger.setFhirPathCriteria("id = " + myEncounter.getIdElement().getIdPart());

		// run
		SubscriptionTriggerMatcher svc = new SubscriptionTriggerMatcher(mySubscriptionTopicSupport, msg, trigger, myMemoryCacheService);
		InMemoryMatchResult result = svc.match();

		// verify
		assertTrue(result.matched());
	}

	@Test
	public void testValidFhirPathCriteriaEvaluationUsingCurrent() {
		ResourceModifiedMessage msg = new ResourceModifiedMessage(ourFhirContext, myEncounter, ResourceModifiedMessage.OperationTypeEnum.UPDATE);

		// setup
		SubscriptionTopic.SubscriptionTopicResourceTriggerComponent trigger = new SubscriptionTopic.SubscriptionTopicResourceTriggerComponent();
		trigger.setResource("Encounter");
		trigger.addSupportedInteraction(SubscriptionTopic.InteractionTrigger.UPDATE);
		trigger.setFhirPathCriteria("%current.id = " + myEncounter.getIdElement().getIdPart());

		// run
		SubscriptionTriggerMatcher svc = new SubscriptionTriggerMatcher(mySubscriptionTopicSupport, msg, trigger, myMemoryCacheService);
		InMemoryMatchResult result = svc.match();

		// verify
		assertTrue(result.matched());
	}

	@Test
	public void testValidFhirPathCriteriaEvaluationReturningNonBoolean() {
		ResourceModifiedMessage msg = new ResourceModifiedMessage(ourFhirContext, myEncounter, ResourceModifiedMessage.OperationTypeEnum.UPDATE);

		// setup
		SubscriptionTopic.SubscriptionTopicResourceTriggerComponent trigger = new SubscriptionTopic.SubscriptionTopicResourceTriggerComponent();
		trigger.setId("1");
		trigger.setResource("Encounter");
		trigger.addSupportedInteraction(SubscriptionTopic.InteractionTrigger.UPDATE);
		trigger.setFhirPathCriteria("%current.id");

		// run
		SubscriptionTriggerMatcher svc = new SubscriptionTriggerMatcher(mySubscriptionTopicSupport, msg, trigger, myMemoryCacheService);
		InMemoryMatchResult result = svc.match();

		// verify
		assertFalse(result.matched());
		assertEquals("FhirPath evaluation criteria '%current.id' from Subscription topic: '1' resulted in a non-boolean result: 'org.hl7.fhir.r5.model.IdType'", result.getUnsupportedReason());
	}

	@Test
	public void testValidFhirPathReturningCollection() {
		ResourceModifiedMessage msg = new ResourceModifiedMessage(ourFhirContext, myEncounter, ResourceModifiedMessage.OperationTypeEnum.UPDATE);

		// setup
		SubscriptionTopic.SubscriptionTopicResourceTriggerComponent trigger = new SubscriptionTopic.SubscriptionTopicResourceTriggerComponent();
		trigger.setId("1");
		trigger.setResource("Encounter");
		trigger.addSupportedInteraction(SubscriptionTopic.InteractionTrigger.UPDATE);
		trigger.setFhirPathCriteria("%current | %previous");

		IFhirResourceDao mockEncounterDao = mock(IFhirResourceDao.class);
		when(myDaoRegistry.getResourceDao("Encounter")).thenReturn(mockEncounterDao);
		Encounter encounterPreviousVersion = new Encounter();
		when(mockEncounterDao.read(any(), any(), eq(false))).thenReturn(encounterPreviousVersion);

		// run
		SubscriptionTriggerMatcher svc = new SubscriptionTriggerMatcher(mySubscriptionTopicSupport, msg, trigger, myMemoryCacheService);
		InMemoryMatchResult result = svc.match();

		// verify
		assertFalse(result.matched());
		assertEquals("FhirPath evaluation criteria '%current | %previous' from Subscription topic: '1' resulted in '2' results. Expected 1.", result.getUnsupportedReason());
	}

	@Test
	public void testUpdateWithPrevCriteriaMatchAndFailingFhirPathCriteria() {
		ResourceModifiedMessage msg = new ResourceModifiedMessage(ourFhirContext, myEncounter, ResourceModifiedMessage.OperationTypeEnum.UPDATE);

		// setup
		SubscriptionTopic.SubscriptionTopicResourceTriggerComponent trigger = new SubscriptionTopic.SubscriptionTopicResourceTriggerComponent();
		trigger.setResource("Encounter");
		trigger.addSupportedInteraction(SubscriptionTopic.InteractionTrigger.UPDATE);
		trigger.getQueryCriteria().setPrevious("Encounter?status=in-progress");
		trigger.setFhirPathCriteria("random text");


		IFhirResourceDao mockEncounterDao = mock(IFhirResourceDao.class);
		when(myDaoRegistry.getResourceDao("Encounter")).thenReturn(mockEncounterDao);
		Encounter encounterPreviousVersion = new Encounter();
		when(mockEncounterDao.read(any(), any(), eq(false))).thenReturn(encounterPreviousVersion);
		when(mySearchParamMatcher.match(any(), any(), any())).thenReturn(InMemoryMatchResult.successfulMatch());

		// run
		SubscriptionTriggerMatcher svc = new SubscriptionTriggerMatcher(mySubscriptionTopicSupport, msg, trigger, myMemoryCacheService);
		InMemoryMatchResult result = svc.match();

		// verify
		assertFalse(result.matched());
		assertEquals("Error @1, 2: Premature ExpressionNode termination at unexpected token \"text\"", result.getUnsupportedReason());
	}

	@Test
	public void testUpdateWithPrevCriteriaMatchAndFhirPathCriteriaUsingPreviousVersion() {
		myEncounter.setStatus(Enumerations.EncounterStatus.INPROGRESS);
		ResourceModifiedMessage msg = new ResourceModifiedMessage(ourFhirContext, myEncounter, ResourceModifiedMessage.OperationTypeEnum.UPDATE);

		// setup
		SubscriptionTopic.SubscriptionTopicResourceTriggerComponent trigger = new SubscriptionTopic.SubscriptionTopicResourceTriggerComponent();
		trigger.setResource("Encounter");
		trigger.addSupportedInteraction(SubscriptionTopic.InteractionTrigger.UPDATE);
		trigger.getQueryCriteria().setPrevious("Encounter?status=in-progress");
		trigger.setFhirPathCriteria("%current.status='in-progress' and %previous.status.exists().not()");


		IFhirResourceDao mockEncounterDao = mock(IFhirResourceDao.class);
		when(myDaoRegistry.getResourceDao("Encounter")).thenReturn(mockEncounterDao);
		Encounter encounterPreviousVersion = new Encounter();
		when(mockEncounterDao.read(any(), any(), eq(false))).thenReturn(encounterPreviousVersion);
		when(mySearchParamMatcher.match(any(), any(), any())).thenReturn(InMemoryMatchResult.successfulMatch());

		// run
		SubscriptionTriggerMatcher svc = new SubscriptionTriggerMatcher(mySubscriptionTopicSupport, msg, trigger, myMemoryCacheService);
		InMemoryMatchResult result = svc.match();

		// verify
		assertTrue(result.matched());
	}

	@Test
	public void testUpdateOnlyFhirPathCriteriaUsingPreviousVersion() {
		myEncounter.setStatus(Enumerations.EncounterStatus.INPROGRESS);
		ResourceModifiedMessage msg = new ResourceModifiedMessage(ourFhirContext, myEncounter, ResourceModifiedMessage.OperationTypeEnum.UPDATE);

		// setup
		SubscriptionTopic.SubscriptionTopicResourceTriggerComponent trigger = new SubscriptionTopic.SubscriptionTopicResourceTriggerComponent();
		trigger.setResource("Encounter");
		trigger.addSupportedInteraction(SubscriptionTopic.InteractionTrigger.UPDATE);
		trigger.setFhirPathCriteria("%current.status='in-progress' and %previous.status.exists().not()");


		IFhirResourceDao mockEncounterDao = mock(IFhirResourceDao.class);
		when(myDaoRegistry.getResourceDao("Encounter")).thenReturn(mockEncounterDao);
		Encounter encounterPreviousVersion = new Encounter();
		when(mockEncounterDao.read(any(), any(), eq(false))).thenReturn(encounterPreviousVersion);

		// run
		SubscriptionTriggerMatcher svc = new SubscriptionTriggerMatcher(mySubscriptionTopicSupport, msg, trigger, myMemoryCacheService);
		InMemoryMatchResult result = svc.match();

		// verify
		assertTrue(result.matched());
	}

	@Test
	public void testCacheUsage() {
		myEncounter.setStatus(Enumerations.EncounterStatus.INPROGRESS);
		ResourceModifiedMessage msg = new ResourceModifiedMessage(ourFhirContext, myEncounter, ResourceModifiedMessage.OperationTypeEnum.UPDATE);

		// setup
		SubscriptionTopic.SubscriptionTopicResourceTriggerComponent trigger = new SubscriptionTopic.SubscriptionTopicResourceTriggerComponent();
		trigger.setResource("Encounter");
		trigger.addSupportedInteraction(SubscriptionTopic.InteractionTrigger.UPDATE);
		String fhirPathCriteria = "%current.status='in-progress'";
		trigger.setFhirPathCriteria(fhirPathCriteria);


		IFhirResourceDao mockEncounterDao = mock(IFhirResourceDao.class);
		when(myDaoRegistry.getResourceDao("Encounter")).thenReturn(mockEncounterDao);

		assertNull(myMemoryCacheService.getIfPresent(MemoryCacheService.CacheEnum.FHIRPATH_EXPRESSION, fhirPathCriteria));

		// run
		SubscriptionTriggerMatcher svc = new SubscriptionTriggerMatcher(mySubscriptionTopicSupport, msg, trigger, myMemoryCacheService);
		InMemoryMatchResult result = svc.match();


		// verify
		assertTrue(result.matched());
		assertNotNull(myMemoryCacheService.getIfPresent(MemoryCacheService.CacheEnum.FHIRPATH_EXPRESSION, fhirPathCriteria));
	}
}
