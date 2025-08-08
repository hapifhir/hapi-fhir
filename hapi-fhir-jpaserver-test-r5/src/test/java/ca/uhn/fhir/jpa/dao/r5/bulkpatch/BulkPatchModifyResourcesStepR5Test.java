package ca.uhn.fhir.jpa.dao.r5.bulkpatch;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common.BulkModifyResourcesChunkOutcomeJson;
import ca.uhn.fhir.batch2.jobs.bulkmodify.patch.BulkPatchJobParameters;
import ca.uhn.fhir.batch2.jobs.bulkmodify.patch.BulkPatchModifyResourcesStep;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.batch2.jobs.chunk.TypedPidAndVersionListWorkChunkJson;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.jpa.dao.r5.BaseJpaR5Test;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.Patient;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static ca.uhn.fhir.jpa.dao.r5.bulkpatch.BulkPatchJobR5Test.createPatchWithModifyPatientIdentifierSystem;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BulkPatchModifyResourcesStepR5Test extends BaseJpaR5Test {

	@Autowired
	private BulkPatchModifyResourcesStep mySvc;

	@Mock
	private IJobDataSink<BulkModifyResourcesChunkOutcomeJson> myDataSink;

	@Test
	public void testQueryCounts() {
		// Setup
		TypedPidAndVersionListWorkChunkJson data = new TypedPidAndVersionListWorkChunkJson();
		List<IIdType> ids = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			IIdType id = createPatient(withIdentifier("http://blah", "bar" + i));
			ids.add(id);
			data.addTypedPidWithNullPartitionForUnitTest("Patient", id.getIdPartAsLong());
		}

		// Test
		BulkPatchJobParameters jobParameters = new BulkPatchJobParameters();
		jobParameters.setFhirPatch(myFhirContext, createPatchWithModifyPatientIdentifierSystem());

		WorkChunk workChunk = new WorkChunk();
		JobInstance jobInstance = new JobInstance();

		StepExecutionDetails<BulkPatchJobParameters, TypedPidAndVersionListWorkChunkJson> parameters = new StepExecutionDetails<>(jobParameters, data, jobInstance, workChunk);
		myCaptureQueriesListener.clear();
		mySvc.run(parameters, myDataSink);

		// Verify

		// Select HFJ_RESOURCE+HFJ_RES_VER, Select HFJ_SPIDX_TOKEN
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
		// 10 insert new HFJ_RES_VER
		assertEquals(10, myCaptureQueriesListener.countInsertQueries());
		// 10 update HFJ_SPIDX_TOKEN, 10 update HFJ_RESOURCE
		assertEquals(20, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());

		Patient actualPatient = myPatientDao.read(ids.get(0), newSrd());
		assertEquals("2", actualPatient.getIdElement().getVersionIdPart());
		assertEquals("http://foo", actualPatient.getIdentifier().get(0).getSystem());
		assertEquals("bar0", actualPatient.getIdentifier().get(0).getValue());
	}

}
