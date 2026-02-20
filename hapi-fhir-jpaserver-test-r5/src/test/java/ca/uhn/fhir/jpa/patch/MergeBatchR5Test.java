package ca.uhn.fhir.jpa.patch;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.jobs.merge.MergeJobParameters;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.pid.FhirIdJson;
import ca.uhn.fhir.jpa.dao.r5.BaseJpaR5Test;
import ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesLargeTestData;
import ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesTestHelper;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.Observation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static ca.uhn.fhir.batch2.jobs.merge.MergeAppCtx.JOB_MERGE;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Merge doesn't work on R5 yet - this is a test for when it does
 */
@Disabled
public class MergeBatchR5Test extends BaseJpaR5Test {

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
	}

	@Test
	void testMerge() {
		// Setup
		createPatient(withId("A"), withActiveTrue());
		createPatient(withId("B"), withActiveTrue());
		createObservation(withId("O"), withSubject("Patient/A"));

		// Test
		MergeJobParameters jobParams = new MergeJobParameters();
		jobParams.setSourceId(new FhirIdJson(null, "Patient", "A"));
		jobParams.setTargetId(new FhirIdJson(null, "Patient", "B"));
		String jobId = myJobCoordinator.startInstance(mySrd, new JobInstanceStartRequest(JOB_MERGE, jobParams)).getInstanceId();

		// Verify
		myBatch2JobHelper.awaitJobCompletion(jobId);

		Observation actual = myObservationDao.read(new IdType("Observation/O"), newSrd());
		assertEquals("Patient/B", actual.getSubject().getReferenceElement().getValue());
	}

}
