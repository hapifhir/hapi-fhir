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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static ca.uhn.fhir.batch2.jobs.replacereferences.ReplaceReferencesAppCtx.JOB_REPLACE_REFERENCES;

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
		ReplaceReferencesJobParameters jobParams = new ReplaceReferencesJobParameters();
		jobParams.setSourceId(new FhirIdJson(myTestHelper.mySourcePatientId));
		jobParams.setTargetId(new FhirIdJson(myTestHelper.myTargetPatientId));

		JobInstanceStartRequest request = new JobInstanceStartRequest(JOB_REPLACE_REFERENCES, jobParams);
		Batch2JobStartResponse jobStartResponse = myJobCoordinator.startInstance(mySrd, request);
		JobInstance jobInstance = myBatch2JobHelper.awaitJobCompletion(jobStartResponse);

		// FIXME KHS assert outcome
		jobInstance.getReport();


		myTestHelper.assertAllReferencesUpdated();
	}
}
