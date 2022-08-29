package ca.uhn.fhir.jpa.delete.job;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.jobs.expunge.DeleteExpungeAppCtx;
import ca.uhn.fhir.batch2.jobs.expunge.DeleteExpungeJobParameters;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeleteExpungeJobTest extends BaseJpaR4Test {
	@Autowired
	private IJobCoordinator myJobCoordinator;
	@Autowired
	private Batch2JobHelper myBatch2JobHelper;

	@Test
	public void testDeleteExpunge() throws Exception {
		// setup
		Patient patientActive = new Patient();
		patientActive.setActive(true);
		IIdType pKeepId = myPatientDao.create(patientActive).getId().toUnqualifiedVersionless();

		Patient patientInactive = new Patient();
		patientInactive.setActive(false);
		IIdType pDelId = myPatientDao.create(patientInactive).getId().toUnqualifiedVersionless();

		Observation obsActive = new Observation();
		obsActive.setSubject(new Reference(pKeepId));
		IIdType oKeepId = myObservationDao.create(obsActive).getId().toUnqualifiedVersionless();

		Observation obsInactive = new Observation();
		obsInactive.setSubject(new Reference(pDelId));
		IIdType oDelId = myObservationDao.create(obsInactive).getId().toUnqualifiedVersionless();

		DiagnosticReport diagActive = new DiagnosticReport();
		diagActive.setSubject(new Reference(pKeepId));
		IIdType dKeepId = myDiagnosticReportDao.create(diagActive).getId().toUnqualifiedVersionless();

		DiagnosticReport diagInactive = new DiagnosticReport();
		diagInactive.setSubject(new Reference(pDelId));
		IIdType dDelId = myDiagnosticReportDao.create(diagInactive).getId().toUnqualifiedVersionless();

		// validate precondition
		assertEquals(2, myPatientDao.search(SearchParameterMap.newSynchronous()).size());
		assertEquals(2, myObservationDao.search(SearchParameterMap.newSynchronous()).size());
		assertEquals(2, myDiagnosticReportDao.search(SearchParameterMap.newSynchronous()).size());

		DeleteExpungeJobParameters jobParameters = new DeleteExpungeJobParameters();
		jobParameters.addUrl("Observation?subject.active=false").addUrl("DiagnosticReport?subject.active=false");

		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setParameters(jobParameters);
		startRequest.setJobDefinitionId(DeleteExpungeAppCtx.JOB_DELETE_EXPUNGE);

		// execute
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(startRequest);
		myBatch2JobHelper.awaitJobCompletion(startResponse);

		// validate
		assertEquals(1, myObservationDao.search(SearchParameterMap.newSynchronous()).size());
		assertDocumentCountMatchesResourceCount(myObservationDao);

		assertEquals(1, myDiagnosticReportDao.search(SearchParameterMap.newSynchronous()).size());
		assertDocumentCountMatchesResourceCount(myDiagnosticReportDao);

		assertEquals(2, myPatientDao.search(SearchParameterMap.newSynchronous()).size());
		assertDocumentCountMatchesResourceCount(myPatientDao);
	}

	public void assertDocumentCountMatchesResourceCount(IFhirResourceDao dao) {
		String resourceType = myFhirContext.getResourceType(dao.getResourceType());
		long resourceCount = dao.search(new SearchParameterMap().setLoadSynchronous(true)).size();
		runInTransaction(() -> {
			assertEquals(resourceCount, myFulltestSearchSvc.count(resourceType, new SearchParameterMap()));
		});

	}
}
