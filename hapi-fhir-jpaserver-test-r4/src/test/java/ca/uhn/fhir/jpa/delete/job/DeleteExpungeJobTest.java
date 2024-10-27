package ca.uhn.fhir.jpa.delete.job;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.jobs.expunge.DeleteExpungeAppCtx;
import ca.uhn.fhir.batch2.jobs.expunge.DeleteExpungeJobParameters;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class DeleteExpungeJobTest extends BaseJpaR4Test {
	@Autowired
	private IJobCoordinator myJobCoordinator;
	@Autowired
	private Batch2JobHelper myBatch2JobHelper;

	@Test
	public void testDeleteExpunge() {
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
		jobParameters.addUrl("Observation?subject.active=false");
		jobParameters.addUrl("DiagnosticReport?subject.active=false");

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

	@Test
	public void testCascade_FailIfNotEnabled() {
		IIdType p1 = createPatient(withActiveTrue());
		IIdType o1 = createObservation(withSubject(p1));
		IIdType p2 = createPatient(withActiveTrue());
		IIdType o2 = createObservation(withSubject(p2));

		// validate precondition
		assertEquals(2, myPatientDao.search(SearchParameterMap.newSynchronous()).size());
		assertEquals(2, myObservationDao.search(SearchParameterMap.newSynchronous()).size());

		DeleteExpungeJobParameters jobParameters = new DeleteExpungeJobParameters();
		jobParameters.addUrl("Patient?_id=" + p1.getIdPart());

		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setParameters(jobParameters);
		startRequest.setJobDefinitionId(DeleteExpungeAppCtx.JOB_DELETE_EXPUNGE);

		// execute
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(startRequest);

		// Validate
		JobInstance failure = myBatch2JobHelper.awaitJobFailure(startResponse);
		assertThat(failure.getErrorMessage()).contains("Unable to delete " + p1.getValue() + " because " + o1.getValue() + " refers to it");
	}

	@Test
	public void testCascade() {
		// Setup

		IIdType p1 = createPatient(withActiveTrue());
		IIdType o1 = createObservation(withSubject(p1));
		IIdType p2 = createPatient(withActiveTrue());
		IIdType o2 = createObservation(withSubject(p2));

		// validate precondition
		assertEquals(2, myPatientDao.search(SearchParameterMap.newSynchronous()).size());
		assertEquals(2, myObservationDao.search(SearchParameterMap.newSynchronous()).size());

		DeleteExpungeJobParameters jobParameters = new DeleteExpungeJobParameters();
		jobParameters.addUrl("Patient?_id=" + p1.getIdPart());
		jobParameters.setCascade(true);

		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setParameters(jobParameters);
		startRequest.setJobDefinitionId(DeleteExpungeAppCtx.JOB_DELETE_EXPUNGE);

		// execute
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(startRequest);

		// Validate
		JobInstance outcome = myBatch2JobHelper.awaitJobCompletion(startResponse);
		assertEquals(2, outcome.getCombinedRecordsProcessed());
		assertDoesntExist(p1);
		assertDoesntExist(o1);
		assertNotGone(p2);
		assertNotGone(o2);
	}


	@Test
	public void testCascade_MultiLevel_Success() {
		// Setup

		// Create a chain of dependent references
		IIdType p1 = createPatient(withActiveTrue());
		IIdType o1 = createObservation(withSubject(p1));
		IIdType o1b = createObservation(withReference("hasMember", o1));
		IIdType o1c = createObservation(withReference("hasMember", o1b));

		// validate precondition
		assertEquals(1, myPatientDao.search(SearchParameterMap.newSynchronous()).size());
		assertEquals(3, myObservationDao.search(SearchParameterMap.newSynchronous()).size());

		DeleteExpungeJobParameters jobParameters = new DeleteExpungeJobParameters();
		jobParameters.addUrl("Patient?_id=" + p1.getIdPart());
		jobParameters.setCascade(true);
		jobParameters.setCascadeMaxRounds(4);

		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setParameters(jobParameters);
		startRequest.setJobDefinitionId(DeleteExpungeAppCtx.JOB_DELETE_EXPUNGE);

		// execute
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(startRequest);

		// Validate
		JobInstance outcome = myBatch2JobHelper.awaitJobCompletion(startResponse);
		assertEquals(4, outcome.getCombinedRecordsProcessed());
		assertDoesntExist(p1);
		assertDoesntExist(o1);
		assertDoesntExist(o1b);
		assertDoesntExist(o1c);
	}

	@Test
	public void testCascade_MultiLevel_NotEnoughRounds() {
		// Setup

		// Create a chain of dependent references
		IIdType p1 = createPatient(withActiveTrue());
		IIdType o1 = createObservation(withSubject(p1));
		IIdType o1b = createObservation(withReference("hasMember", o1));
		IIdType o1c = createObservation(withReference("hasMember", o1b));

		// validate precondition
		assertEquals(1, myPatientDao.search(SearchParameterMap.newSynchronous()).size());
		assertEquals(3, myObservationDao.search(SearchParameterMap.newSynchronous()).size());

		DeleteExpungeJobParameters jobParameters = new DeleteExpungeJobParameters();
		jobParameters.addUrl("Patient?_id=" + p1.getIdPart());
		jobParameters.setCascade(true);
		jobParameters.setCascadeMaxRounds(1);

		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setParameters(jobParameters);
		startRequest.setJobDefinitionId(DeleteExpungeAppCtx.JOB_DELETE_EXPUNGE);

		// execute
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(startRequest);

		// Validate
		JobInstance outcome = myBatch2JobHelper.awaitJobFailure(startResponse);
		assertThat(outcome.getErrorMessage()).contains("refers to it via the path");
		assertNotGone(p1);
		assertNotGone(o1);
		assertNotGone(o1b);
		assertNotGone(o1c);
	}

	@Test
	public void testInvalidParams_NoSearchParams() {
		// Setup
		DeleteExpungeJobParameters jobParameters = new DeleteExpungeJobParameters();
		jobParameters.addUrl("Patient/123");

		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setParameters(jobParameters);
		startRequest.setJobDefinitionId(DeleteExpungeAppCtx.JOB_DELETE_EXPUNGE);

		// execute
		try {
			myJobCoordinator.startInstance(startRequest);
			fail();
		} catch (InvalidRequestException e) {

			// validate
			assertThat(e.getMessage()).contains("Delete expunge URLs must be in the format");
		}

	}


	public void assertDocumentCountMatchesResourceCount(IFhirResourceDao dao) {
		String resourceType = myFhirContext.getResourceType(dao.getResourceType());
		long resourceCount = dao.search(new SearchParameterMap().setLoadSynchronous(true)).size();
		runInTransaction(() -> {
			assertEquals(resourceCount, myFulltestSearchSvc.count(resourceType, new SearchParameterMap()));
		});

	}
}
