package ca.uhn.fhir.jpa.batch2.jobs.term.valueset;

import ca.uhn.fhir.batch2.api.IJobDataSink;import ca.uhn.fhir.batch2.api.IJobStepExecutionServices;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.model.JobDefinition;import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ca.uhn.fhir.jpa.svc.MockHapiTransactionService;
import static ca.uhn.fhir.jpa.batch2.jobs.term.valueset.PreExpandValueSetJobAppCtx.STEP_ID_CREATE_EXPANSION_WORK_CHUNKS;import static ca.uhn.fhir.jpa.batch2.jobs.term.valueset.PreExpandValueSetJobAppCtx.STEP_ID_HANDLE_COMPOSE_INCLUDE;

@ExtendWith(MockitoExtension.class)
class CreateExpansionWorkChunksStepTest {

	@Spy
	IHapiTransactionService myTransactionService = new MockHapiTransactionService();
	@Spy
	FhirContext myFhirContext = FhirContext.forR5Cached();
	@Spy
	VersionCanonicalizer myVersionCanonicalizer = new VersionCanonicalizer(myFhirContext);
	@Mock
	private IJobStepExecutionServices myJobStepExecutionServices;
	@Mock
	private JobDefinition<PreExpandValueSetParameters> jobDefinition;
	@Mock
	private IJobDataSink<ExpansionWorkChunkJson> myDataSink;
	@InjectMocks
	private CreateExpansionWorkChunksStep mySvc;

	@Test
	void testRun() {
		PreExpandValueSetParameters params = new PreExpandValueSetParameters();
		params.setUrl("http://foo");
		params.setVersion("1.0");


		mySvc.run(new StepExecutionDetails<>(params, new VoidModel(), new JobInstance(), new WorkChunk(), myJobStepExecutionServices, jobDefinition, STEP_ID_CREATE_EXPANSION_WORK_CHUNKS, STEP_ID_HANDLE_COMPOSE_INCLUDE), myDataSink);


	}


}
