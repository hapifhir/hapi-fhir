package ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepExecutionServices;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.svc.MockHapiTransactionService;
import ca.uhn.fhir.jpa.term.UploadStatistics;
import ca.uhn.fhir.jpa.term.api.ITermValueSetStorageSvc;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.system.HapiSystemProperties;
import ca.uhn.fhir.util.IntCounter;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand.PreExpandValueSetJobAppCtx.STEP_ID_WRITE_CONCEPTS_EXCLUDE;
import static ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand.PreExpandValueSetJobAppCtx.STEP_ID_WRITE_CONCEPTS_INCLUDE;
import static ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand.Step1InitiateJobTest.THE_VS_URL;
import static ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand.Step1InitiateJobTest.THE_VS_VERSION;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class Step4And5WriteConceptsStepTest {
	static {
		HapiSystemProperties.enableUnitTestMode();
	}

	@Spy
	IHapiTransactionService myTransactionService = new MockHapiTransactionService();
	@Spy
	FhirContext myFhirContext = FhirContext.forR5Cached();
	@Spy
	VersionCanonicalizer myVersionCanonicalizer = new VersionCanonicalizer(myFhirContext);
	@Mock
	private ITermValueSetStorageSvc myValueSetStorageSvc;
	@Mock
	private IJobStepExecutionServices myJobStepExecutionServices;
	@Mock
	private JobDefinition<PreExpandValueSetParameters> myJobDefinition;
	@Mock
	private IJobDataSink<ExpandConceptsWorkChunkJson> myDataSink;
	@InjectMocks
	private Step4And5WriteConceptsStep<ExpandConceptsWorkChunkJson> mySvc = new Step4And5WriteConceptsStep<>(true);

	@Test
	void testInclude_Retry() {

		// Setup

		WriteConceptsWorkChunkJson data = new WriteConceptsWorkChunkJson();
		data.setValueSet(new ValueSet());
		data.setStartingOrderOffset(1000);

		IntCounter errorCounter = new IntCounter();
		when(myValueSetStorageSvc.addConceptsToExpansion(any(ValueSet.class), anyInt())).thenAnswer(t->{
			if (errorCounter.getAndIncrement() == 3) {
				UploadStatistics statistics = new UploadStatistics(new IdType("ValueSet/A"));
				statistics.incrementConceptsAddedCount();
				return statistics;
			}
			throw new ResourceVersionConflictException("version conflict");
		});

		// Test

		PreExpandValueSetParameters params = new PreExpandValueSetParameters();
		params.setUrl(THE_VS_URL);
		params.setVersion(THE_VS_VERSION);
		mySvc.run(new StepExecutionDetails<>(params, data, new JobInstance(), new WorkChunk(), myJobStepExecutionServices, myJobDefinition, STEP_ID_WRITE_CONCEPTS_INCLUDE, STEP_ID_WRITE_CONCEPTS_EXCLUDE), myDataSink);

		// Verify

		verify(myValueSetStorageSvc, times(4)).addConceptsToExpansion(any(ValueSet.class), eq(1000));
	}
}
