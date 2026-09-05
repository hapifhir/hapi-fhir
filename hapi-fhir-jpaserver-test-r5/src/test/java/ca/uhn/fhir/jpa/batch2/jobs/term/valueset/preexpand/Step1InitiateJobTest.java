package ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepExecutionServices;
import ca.uhn.fhir.batch2.api.RetryChunkLaterException;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.svc.MockHapiTransactionService;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermValueSetStorageSvc;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import org.hl7.fhir.r5.model.ValueSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand.PreExpandValueSetJobAppCtx.STEP_ID_EXPAND_CONCEPTS_EXCLUDE;
import static ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand.PreExpandValueSetJobAppCtx.STEP_ID_EXPAND_CONCEPTS_INCLUDE;
import static ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand.PreExpandValueSetJobAppCtx.STEP_ID_INITIATE_JOB;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class Step1InitiateJobTest {

	private static final String THE_STAGING_VERSION = "the-staging-version";
	private static final Duration RETRY_DELAY = Duration.of(3, ChronoUnit.SECONDS);
	static final String THE_VS_URL = "http://foo";
	static final String THE_VS_VERSION = "1.0";

	@Spy
	IHapiTransactionService myTransactionService = new MockHapiTransactionService();
	@Spy
	FhirContext myFhirContext = FhirContext.forR5Cached();
	@Spy
	VersionCanonicalizer myVersionCanonicalizer = new VersionCanonicalizer(myFhirContext);
	@Mock
	private ITermValueSetStorageSvc myValueSetStorageSvc;
	@Mock
	private ITermDeferredStorageSvc myDeferredStorageSvc;
	@Mock
	private IValidationSupport myValidationSupport;
	@Mock
	private IJobStepExecutionServices myJobStepExecutionServices;
	@Mock
	private JobDefinition<PreExpandValueSetParameters> myJobDefinition;
	@Mock
	private IJobDataSink<ExpandConceptsWorkChunkJson> myDataSink;
	@InjectMocks
	private Step1InitiateJob mySvc;
	@Captor
	private ArgumentCaptor<IModelJson> myWorkChunkCaptor;

	@AfterEach
	void afterResetRetryDelay() {
		Step1InitiateJob.setRetryDelay(null);
	}

	@Test
	void testRun() {
		// Setup
		ValueSet inputVs = new ValueSet();
		inputVs.getCompose()
			.addInclude()
			.setSystem("http://system-0");
		inputVs.getCompose()
			.addInclude()
			.setSystem("http://system-1");
		inputVs.getCompose()
			.addExclude()
			.setSystem("http://system-2");
		when(myDeferredStorageSvc.isStorageQueueEmpty(false)).thenReturn(true);
		when(myValidationSupport.fetchValueSet(eq("http://foo|1.0"))).thenReturn(inputVs);
		when(myValueSetStorageSvc.startStagingVersion(eq(THE_VS_URL), eq(THE_VS_VERSION))).thenReturn(THE_STAGING_VERSION);

		// Test

		PreExpandValueSetParameters params = new PreExpandValueSetParameters();
		params.setUrl(THE_VS_URL);
		params.setVersion(THE_VS_VERSION);
		mySvc.run(new StepExecutionDetails<>(params, new VoidModel(), new JobInstance(), new WorkChunk(), myJobStepExecutionServices, myJobDefinition, STEP_ID_INITIATE_JOB, STEP_ID_EXPAND_CONCEPTS_INCLUDE), myDataSink);

		// Verify

		verify(myValueSetStorageSvc, times(1)).startStagingVersion(eq(THE_VS_URL), eq(THE_VS_VERSION));

		verify(myDataSink, times(2)).acceptForFutureStep(eq(STEP_ID_EXPAND_CONCEPTS_INCLUDE), myWorkChunkCaptor.capture());
		assertEquals(THE_VS_URL, ((ExpandConceptsWorkChunkJson)myWorkChunkCaptor.getAllValues().get(0)).getStagingUrl());
		assertEquals(THE_STAGING_VERSION, ((ExpandConceptsWorkChunkJson)myWorkChunkCaptor.getAllValues().get(0)).getStagingVersion());
		assertEquals("{\"system\":\"http://system-0\"}", ((ExpandConceptsWorkChunkJson)myWorkChunkCaptor.getAllValues().get(0)).getComposeAsJson());
		assertEquals(0, ((ExpandConceptsWorkChunkJson)myWorkChunkCaptor.getAllValues().get(0)).getStartingOrder());
		assertEquals(THE_VS_URL, ((ExpandConceptsWorkChunkJson)myWorkChunkCaptor.getAllValues().get(1)).getStagingUrl());
		assertEquals(THE_STAGING_VERSION, ((ExpandConceptsWorkChunkJson)myWorkChunkCaptor.getAllValues().get(1)).getStagingVersion());
		assertEquals("{\"system\":\"http://system-1\"}", ((ExpandConceptsWorkChunkJson)myWorkChunkCaptor.getAllValues().get(1)).getComposeAsJson());
		assertEquals(1_000_000, ((ExpandConceptsWorkChunkJson)myWorkChunkCaptor.getAllValues().get(1)).getStartingOrder());

		verify(myDataSink, times(1)).acceptForFutureStep(eq(STEP_ID_EXPAND_CONCEPTS_EXCLUDE), myWorkChunkCaptor.capture());
		assertEquals(THE_VS_URL, ((ExpandConceptsWorkChunkJson)myWorkChunkCaptor.getAllValues().get(2)).getStagingUrl());
		assertEquals(THE_STAGING_VERSION, ((ExpandConceptsWorkChunkJson)myWorkChunkCaptor.getAllValues().get(2)).getStagingVersion());
		assertEquals("{\"system\":\"http://system-2\"}", ((ExpandConceptsWorkChunkJson)myWorkChunkCaptor.getAllValues().get(2)).getComposeAsJson());
		assertEquals(2_000_000, ((ExpandConceptsWorkChunkJson)myWorkChunkCaptor.getAllValues().get(2)).getStartingOrder());

		verify(myDataSink, times(1)).acceptForFutureStep(
			eq(PreExpandValueSetJobAppCtx.STEP_ID_LOAD_ALL_CONCEPT_IDS), any(LoadAllConceptIdsWorkChunkJson.class));

		verify(myDataSink, times(1)).acceptForFutureStep(
			eq(PreExpandValueSetJobAppCtx.STEP_ID_GENERATE_REPORT), any(ExpandValueSetStepOutcomeJson.class));

		verifyNoMoreInteractions(myDataSink);
	}

	@Test
	void run_deferredStorageQueueNotEmpty_retriesChunkLaterWithoutStagingAnything() {
		// Setup
		when(myDeferredStorageSvc.isStorageQueueEmpty(false)).thenReturn(false);
		Step1InitiateJob.setRetryDelay(RETRY_DELAY);

		PreExpandValueSetParameters params = new PreExpandValueSetParameters();
		params.setUrl(THE_VS_URL);
		params.setVersion(THE_VS_VERSION);

		// Test & Verify
		assertThatThrownBy(() -> mySvc.run(
			new StepExecutionDetails<>(params, new VoidModel(), new JobInstance(), new WorkChunk(), myJobStepExecutionServices, myJobDefinition, STEP_ID_INITIATE_JOB, STEP_ID_EXPAND_CONCEPTS_INCLUDE),
			myDataSink))
			.isInstanceOfSatisfying(RetryChunkLaterException.class, e ->
				assertThat(e.getNextPollDuration()).isEqualTo(RETRY_DELAY))
			.hasMessage(Msg.code(3047));

		verify(myDeferredStorageSvc).saveDeferred();
		verify(myValueSetStorageSvc, never()).startStagingVersion(any(), any());
		verifyNoMoreInteractions(myDataSink);
	}

	@Test
	void run_deferredEntitiesProcessedOnDemand_expandsWithoutRetrying() {
		// Setup - deferred entities on entry, none left once they are processed
		when(myDeferredStorageSvc.isStorageQueueEmpty(false)).thenReturn(false, true);

		ValueSet inputVs = new ValueSet();
		inputVs.getCompose()
			.addInclude()
			.setSystem("http://system-0");
		when(myValidationSupport.fetchValueSet(eq("http://foo|1.0"))).thenReturn(inputVs);
		when(myValueSetStorageSvc.startStagingVersion(eq(THE_VS_URL), eq(THE_VS_VERSION))).thenReturn(THE_STAGING_VERSION);

		PreExpandValueSetParameters params = new PreExpandValueSetParameters();
		params.setUrl(THE_VS_URL);
		params.setVersion(THE_VS_VERSION);

		// Test
		mySvc.run(new StepExecutionDetails<>(params, new VoidModel(), new JobInstance(), new WorkChunk(), myJobStepExecutionServices, myJobDefinition, STEP_ID_INITIATE_JOB, STEP_ID_EXPAND_CONCEPTS_INCLUDE), myDataSink);

		// Verify
		verify(myDeferredStorageSvc).saveDeferred();
		verify(myValueSetStorageSvc).startStagingVersion(eq(THE_VS_URL), eq(THE_VS_VERSION));
		verify(myDataSink).acceptForFutureStep(eq(STEP_ID_EXPAND_CONCEPTS_INCLUDE), any(ExpandConceptsWorkChunkJson.class));
	}

}
