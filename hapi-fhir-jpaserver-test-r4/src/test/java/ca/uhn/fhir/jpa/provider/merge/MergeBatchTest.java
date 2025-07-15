package ca.uhn.fhir.jpa.provider.merge;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.jobs.chunk.FhirIdJson;
import ca.uhn.fhir.batch2.jobs.merge.MergeJobParameters;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesLargeTestData;
import ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesTestHelper;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import net.sf.saxon.functions.Replace;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static ca.uhn.fhir.batch2.jobs.merge.MergeAppCtx.JOB_MERGE;
import static ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesLargeTestData.RESOURCE_TYPES_EXPECTED_TO_BE_PATCHED;
import static ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesLargeTestData.TOTAL_EXPECTED_PATCHES;
import static org.awaitility.Awaitility.await;

public class MergeBatchTest extends BaseJpaR4Test {

	@Autowired
	private IJobCoordinator myJobCoordinator;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private Batch2JobHelper myBatch2JobHelper;

	SystemRequestDetails mySrd = new SystemRequestDetails();

	private ReplaceReferencesTestHelper myTestHelper;
	private ReplaceReferencesLargeTestData myTestData;


	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();

		myTestHelper = new ReplaceReferencesTestHelper(myFhirContext, myDaoRegistry);
		myTestData = new ReplaceReferencesLargeTestData(myDaoRegistry);
		myTestData.createTestResources();
		// keep the version on Provenance.target fields to verify that Provenance resources were saved
		// with versioned target references
/*		myFhirContext.getParserOptions()
			.setDontStripVersionsFromReferencesAtPaths("Provenance.target");*/
		myFhirContext.getParserOptions().setStripVersionsFromReferences(false);

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

		ReplaceReferencesTestHelper.PatientMergeInputParameters inputParams = new ReplaceReferencesTestHelper.PatientMergeInputParameters();
		inputParams.sourcePatient = myTestHelper.idAsReference(myTestData.getSourcePatientId());
		inputParams.targetPatient = myTestHelper.idAsReference(myTestData.getTargetPatientId());
		if (theDeleteSource) {
			inputParams.deleteSource = theDeleteSource;
		}
		if(theWithResultResource) {
			inputParams.resultPatient= myTestData.createResultPatientInput(theDeleteSource);
		}

		MergeJobParameters jobParams = inputParams.asMergeJobParameters(myFhirContext);
		jobParams.setTaskId(taskId);
		jobParams.setCreateProvenance(true);

		JobInstanceStartRequest request = new JobInstanceStartRequest(JOB_MERGE, jobParams);
		Batch2JobStartResponse jobStartResponse = myJobCoordinator.startInstance(mySrd, request);
		JobInstance jobInstance = myBatch2JobHelper.awaitJobCompletion(jobStartResponse);

		Bundle patchResultBundle = myTestHelper.validateCompletedTask(jobInstance, taskId);
		ReplaceReferencesTestHelper.validatePatchResultBundle(patchResultBundle, TOTAL_EXPECTED_PATCHES, RESOURCE_TYPES_EXPECTED_TO_BE_PATCHED);


		myTestHelper.assertAllReferencesUpdated(true, theDeleteSource, myTestData);
		myTestHelper.assertSourcePatientUpdatedOrDeletedAfterMerge(myTestData.getSourcePatientId(), myTestData.getTargetPatientId(), theDeleteSource);
		myTestHelper.assertTargetPatientUpdatedAfterMerge(
			myTestData.getTargetPatientId(),
			myTestData.getSourcePatientId(),
			theDeleteSource,
			myTestData.getExpectedIdentifiersForTargetAfterMerge(theWithResultResource));

		myTestHelper.assertMergeProvenance(inputParams.asParametersResource(), myTestData, null);
	}

	@Test
	void testMergeJob_JobFails_ErrorHandlerSetsAssociatedTaskStatusToFailed() {
		IIdType taskId = createTask();

		MergeJobParameters jobParams = new MergeJobParameters();
		//use a source that does not exist to force the job to fail
		jobParams.setSourceId(new FhirIdJson("Patient", "doesnotexist"));
		jobParams.setTargetId(new FhirIdJson(myTestData.getTargetPatientId()));
		jobParams.setTaskId(taskId);

		JobInstanceStartRequest request = new JobInstanceStartRequest(JOB_MERGE, jobParams);
		Batch2JobStartResponse jobStartResponse = myJobCoordinator.startInstance(mySrd, request);
		myBatch2JobHelper.awaitJobFailure(jobStartResponse);

		await().until(() -> {
			myBatch2JobHelper.runMaintenancePass();
			return myTaskDao.read(taskId, mySrd).getStatus().equals(Task.TaskStatus.FAILED);
		});
	}

	private IIdType createTask() {
		Task task = new Task();
		task.setStatus(Task.TaskStatus.INPROGRESS);
		return myTaskDao.create(task, mySrd).getId().toUnqualifiedVersionless();
	}
}
