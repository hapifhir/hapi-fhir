package ca.uhn.fhir.jpa.replacereferences;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.jobs.chunk.FhirIdJson;
import ca.uhn.fhir.batch2.jobs.replacereferences.ReplaceReferencesJobParameters;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static ca.uhn.fhir.batch2.jobs.replacereferences.ReplaceReferencesAppCtx.JOB_REPLACE_REFERENCES;
import static org.awaitility.Awaitility.await;

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

		// keep the version on Provenance.target fields to verify that Provenance resources were saved
		// with versioned target references
		myFhirContext.getParserOptions()
			.setDontStripVersionsFromReferencesAtPaths("Provenance.target");

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

		Bundle patchResultBundle = myTestHelper.validateCompletedTask(jobInstance, taskId);
		ReplaceReferencesTestHelper.validatePatchResultBundle(patchResultBundle, ReplaceReferencesTestHelper.TOTAL_EXPECTED_PATCHES, List.of(
			"Observation", "Encounter", "CarePlan"));

		myTestHelper.assertAllReferencesUpdated();
		myTestHelper.assertReplaceReferencesProvenance();
	}


	@Test
	void testReplaceReferencesJob_JobFails_ErrorHandlerSetsAssociatedTaskStatusToFailed() {
		IIdType taskId = createReplaceReferencesTask();

		ReplaceReferencesJobParameters jobParams = new ReplaceReferencesJobParameters();
		jobParams.setSourceId(new FhirIdJson(myTestHelper.getSourcePatientId()));
		//use a target that does not exist to force the job to fail
		jobParams.setTargetId(new FhirIdJson("Patient", "doesnotexist"));
		jobParams.setTaskId(taskId);

		JobInstanceStartRequest request = new JobInstanceStartRequest(JOB_REPLACE_REFERENCES, jobParams);
		Batch2JobStartResponse jobStartResponse = myJobCoordinator.startInstance(mySrd, request);
		myBatch2JobHelper.awaitJobFailure(jobStartResponse);

		await().until(() -> {
			myBatch2JobHelper.runMaintenancePass();
			return myTaskDao.read(taskId, mySrd).getStatus().equals(Task.TaskStatus.FAILED);
		});
	}

	private IIdType createReplaceReferencesTask() {
		Task task = new Task();
		task.setStatus(Task.TaskStatus.INPROGRESS);
		return myTaskDao.create(task, mySrd).getId().toUnqualifiedVersionless();
	}
}
