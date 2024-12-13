package ca.uhn.fhir.jpa.replacereferences;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.jobs.chunk.FhirIdJson;
import ca.uhn.fhir.batch2.jobs.replacereferences.ReplaceReferenceResultsJson;
import ca.uhn.fhir.batch2.jobs.replacereferences.ReplaceReferencesJobParameters;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.util.JsonUtil;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static ca.uhn.fhir.batch2.jobs.replacereferences.ReplaceReferencesAppCtx.JOB_REPLACE_REFERENCES;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReplaceReferencesBatchTest extends BaseJpaR4Test {

	@Autowired
	private IJobCoordinator myJobCoordinator;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private Batch2JobHelper myBatch2JobHelper;

	SystemRequestDetails mySrd = new SystemRequestDetails();

	private ReplaceReferencesTestHelper myTestHelper;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();

		myTestHelper = new ReplaceReferencesTestHelper(myFhirContext, myDaoRegistry);
		myTestHelper.beforeEach();

		mySrd.setRequestPartitionId(RequestPartitionId.allPartitions());
	}

	@Test
	public void testHappyPath() {
		IIdType taskId = createReplaceReferencesTask();

		ReplaceReferencesJobParameters jobParams = new ReplaceReferencesJobParameters();
		jobParams.setSourceId(new FhirIdJson(myTestHelper.getSourcePatientId()));
		jobParams.setTargetId(new FhirIdJson(myTestHelper.getTargetPatientId()));
		jobParams.setTaskId(taskId);

		JobInstanceStartRequest request = new JobInstanceStartRequest(JOB_REPLACE_REFERENCES, jobParams);
		Batch2JobStartResponse jobStartResponse = myJobCoordinator.startInstance(mySrd, request);
		JobInstance jobInstance = myBatch2JobHelper.awaitJobCompletion(jobStartResponse);

		// FIXME KHS assert outcome
		String report = jobInstance.getReport();
		ReplaceReferenceResultsJson replaceReferenceResultsJson = JsonUtil.deserialize(report, ReplaceReferenceResultsJson.class);
		IdDt resultTaskId = replaceReferenceResultsJson.getTaskId().asIdDt();
		assertEquals(taskId.getIdPart(), resultTaskId.getIdPart());

		Bundle patchResultBundle = myTestHelper.validateCompletedTask(taskId);
		myTestHelper.validatePatchResultBundle(patchResultBundle, ReplaceReferencesTestHelper.TOTAL_EXPECTED_PATCHES, List.of("Observation", "Encounter", "CarePlan"));

		myTestHelper.assertAllReferencesUpdated();
	}

	private IIdType createReplaceReferencesTask() {
		Task task = new Task();
		task.setStatus(Task.TaskStatus.INPROGRESS);
		return myTaskDao.create(task, mySrd).getId().toUnqualifiedVersionless();
	}
}
