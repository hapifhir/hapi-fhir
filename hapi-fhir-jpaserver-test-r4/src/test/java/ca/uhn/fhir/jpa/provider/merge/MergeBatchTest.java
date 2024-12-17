package ca.uhn.fhir.jpa.provider.merge;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.jobs.chunk.FhirIdJson;
import ca.uhn.fhir.batch2.jobs.merge.MergeJobParameters;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesTestHelper;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static ca.uhn.fhir.batch2.jobs.merge.MergeAppCtx.JOB_MERGE;

public class MergeBatchTest extends BaseJpaR4Test {

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

	@ParameterizedTest
	@CsvSource({
		"true,true",
		"false,true",
		"true,false",
		"false,false"
	})
	public void testHappyPath(boolean theDeleteSource, boolean theWithResultResource) {
		IIdType taskId = createTask();

		MergeJobParameters jobParams = new MergeJobParameters();
		jobParams.setSourceId(new FhirIdJson(myTestHelper.getSourcePatientId()));
		jobParams.setTargetId(new FhirIdJson(myTestHelper.getTargetPatientId()));
		jobParams.setTaskId(taskId);
		jobParams.setDeleteSource(theDeleteSource);
		if (theWithResultResource) {
			String encodedResultPatient = myFhirContext.newJsonParser().encodeResourceToString(myTestHelper.createResultPatient(theDeleteSource));
			jobParams.setResultResource(encodedResultPatient);
		}

		JobInstanceStartRequest request = new JobInstanceStartRequest(JOB_MERGE, jobParams);
		Batch2JobStartResponse jobStartResponse = myJobCoordinator.startInstance(mySrd, request);
		JobInstance jobInstance = myBatch2JobHelper.awaitJobCompletion(jobStartResponse);

		Bundle patchResultBundle = myTestHelper.validateCompletedTask(jobInstance, taskId);
		ReplaceReferencesTestHelper.validatePatchResultBundle(patchResultBundle, ReplaceReferencesTestHelper.TOTAL_EXPECTED_PATCHES,
			List.of(
			"Observation", "Encounter", "CarePlan"));


		myTestHelper.assertAllReferencesUpdated();
		myTestHelper.assertSourcePatientUpdatedOrDeleted(theDeleteSource);
		myTestHelper.assertTargetPatientUpdated(theDeleteSource,
			myTestHelper.getExpectedIdentifiersForTargetAfterMerge(theWithResultResource));
	}

	private IIdType createTask() {
		Task task = new Task();
		task.setStatus(Task.TaskStatus.INPROGRESS);
		return myTaskDao.create(task, mySrd).getId().toUnqualifiedVersionless();
	}
}
