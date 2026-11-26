package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemDao;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemVersionDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDao;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.term.models.TermCodeSystemDeleteVersionJobParameters;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static ca.uhn.fhir.batch2.jobs.termcodesystem.TermCodeSystemJobConfig.TERM_CODE_SYSTEM_DELETE_JOB_NAME;
import static ca.uhn.fhir.batch2.jobs.termcodesystem.TermCodeSystemJobConfig.TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TermDeferredStorageSvcImplTest {

	@Mock
	private PlatformTransactionManager myTxManager;
	@Mock
	private TermConceptDaoSvc myTermConceptDaoSvc;
	@Mock
	private ITermConceptDao myConceptDao;
	@Mock
	private ITermCodeSystemVersionDao myTermCodeSystemVersionDao;
	@Mock
	private ITermCodeSystemDao myTermCodeSystemDao;
	@Mock
	private EntityManager myEntityManager;
	@Spy
	private PartitionSettings myPartitionSettings = new PartitionSettings();

	@Mock
	private IJobCoordinator myJobCoordinator;

	@InjectMocks
	private TermDeferredStorageSvcImpl mySvc;

	@Mock
	private ITermDeferredStorageSvc myDeferredStorageSvc;

	@InjectMocks
	private TermDeferredStorageSvcImpl.ResumeCodeSystemDeletionsJob myResumeCodeSystemDeletionsJob;

	@Test
	void saveDeferred_processingIsPaused_holdsTheQueuedConceptUntilProcessingResumes() {
		TermConcept concept = new TermConcept();
		concept.setCode("CODE_A");
		TermCodeSystemVersion codeSystemVersion = new TermCodeSystemVersion();
		codeSystemVersion.setId(1L);
		concept.setCodeSystemVersion(codeSystemVersion);

		TermDeferredStorageSvcImpl svc = new TermDeferredStorageSvcImpl();
		svc.setTransactionManagerForUnitTest(myTxManager);
		svc.setTermConceptDaoSvc(myTermConceptDaoSvc);
		svc.setCodeSystemVersionDaoForUnitTest(myTermCodeSystemVersionDao);
		svc.addConceptToStorageQueue(concept);

		svc.setProcessDeferred(false);
		svc.saveDeferred();
		verifyNoInteractions(myTermConceptDaoSvc);

		// the queued concept must survive the pause, not be dropped by it
		when(myTermCodeSystemVersionDao.findById(any())).thenReturn(Optional.of(codeSystemVersion));
		svc.setProcessDeferred(true);
		svc.saveDeferred();
		verify(myTermConceptDaoSvc).saveConcept(same(concept));
	}

	@Test
	void saveAllDeferred_processingIsPaused_returnsInsteadOfLoopingForever() {
		TermDeferredStorageSvcImpl svc = new TermDeferredStorageSvcImpl();
		svc.setProcessDeferred(false);

		// a paused queue reports itself as non-empty, so the save loop has no exit condition of its own
		assertFalse(svc.isStorageQueueEmpty(false));

		assertTimeoutPreemptively(Duration.ofSeconds(5), svc::saveAllDeferred);
	}


	@Test
	void isStorageQueueEmpty_includingExecutingJobs_asksTheJobTablesForDeleteJobsThatHaveNotEnded() {
		// no job was started through this instance: the answer must not depend on what this node did
		JobInstance instance = new JobInstance();
		instance.setInstanceId("jobId");
		instance.setStatus(StatusEnum.IN_PROGRESS);

		when(myJobCoordinator.getInstancesbyJobDefinitionIdAndEndedStatus(
			eq(TERM_CODE_SYSTEM_DELETE_JOB_NAME), eq(false), anyInt(), eq(0)))
			.thenReturn(List.of());
		when(myJobCoordinator.getInstancesbyJobDefinitionIdAndEndedStatus(
			eq(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME), eq(false), anyInt(), eq(0)))
			.thenReturn(List.of(instance)) // first the job has not ended
			.thenReturn(List.of()); // then it has

		assertFalse(mySvc.isStorageQueueEmpty(true));
		assertTrue(mySvc.isStorageQueueEmpty(true));
	}

	@Test
	void resumeCodeSystemDeletions_startingTheJobOfOneVersionFails_stillStartsTheJobOfTheNext() {
		TermCodeSystemVersion firstVersion = new TermCodeSystemVersion();
		firstVersion.setId(1L);
		firstVersion.setCodeSystemVersionId("DELETED_" + UUID.randomUUID());
		TermCodeSystemVersion secondVersion = new TermCodeSystemVersion();
		secondVersion.setId(2L);
		secondVersion.setCodeSystemVersionId("DELETED_" + UUID.randomUUID());

		when(myTermCodeSystemDao.findPidsMarkedForDeletion()).thenReturn(List.of());
		when(myTermCodeSystemVersionDao.findPidsMarkedForDeletion()).thenReturn(List.of(1L, 2L));
		when(myTermCodeSystemVersionDao.findByPid(1L)).thenReturn(Optional.of(firstVersion));
		when(myTermCodeSystemVersionDao.findByPid(2L)).thenReturn(Optional.of(secondVersion));
		doThrow(new PessimisticLockingFailureException("the row is locked"))
			.when(myEntityManager).refresh(firstVersion, LockModeType.PESSIMISTIC_WRITE);
		when(myJobCoordinator.getInstancesbyJobDefinitionIdAndEndedStatus(
			eq(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME), eq(false), anyInt(), eq(0)))
			.thenReturn(List.of());

		mySvc.resumeCodeSystemDeletions();

		verify(myEntityManager).refresh(secondVersion, LockModeType.PESSIMISTIC_WRITE);
		ArgumentCaptor<JobInstanceStartRequest> request = ArgumentCaptor.forClass(JobInstanceStartRequest.class);
		verify(myJobCoordinator).startInstanceInCurrentTransaction(any(), request.capture());
		assertThat(request.getValue().getJobDefinitionId()).isEqualTo(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME);
		assertThat(request.getValue()
			.getParameters(TermCodeSystemDeleteVersionJobParameters.class)
			.getCodeSystemVersionPid()).isEqualTo(2L);
	}

	@Test
	void scheduleJobs_schedulesTheResumeOfCodeSystemDeletionsOnceADayOnOneNodeOfTheCluster() {
		ISchedulerService schedulerService = mock(ISchedulerService.class);

		mySvc.scheduleJobs(schedulerService);

		ArgumentCaptor<ScheduledJobDefinition> jobDefinition = ArgumentCaptor.forClass(ScheduledJobDefinition.class);
		verify(schedulerService).scheduleClusteredJob(eq(DateUtils.MILLIS_PER_DAY), jobDefinition.capture());
		assertThat(jobDefinition.getValue().getJobClass())
			.isEqualTo(TermDeferredStorageSvcImpl.ResumeCodeSystemDeletionsJob.class);
	}

	@Test
	void resumeCodeSystemDeletionsJob_execute_resumesTheCodeSystemDeletions() {
		myResumeCodeSystemDeletionsJob.execute(null);

		verify(myDeferredStorageSvc).resumeCodeSystemDeletions();
	}

	@Test
	void testSaveDeferred_Concept() {
		TermConcept concept = new TermConcept();
		concept.setCode("CODE_A");

		TermCodeSystemVersion myTermCodeSystemVersion = new TermCodeSystemVersion();
		myTermCodeSystemVersion.setId(1L);
		concept.setCodeSystemVersion(myTermCodeSystemVersion);

		TermDeferredStorageSvcImpl svc = new TermDeferredStorageSvcImpl();
		svc.setTransactionManagerForUnitTest(myTxManager);
		svc.setTermConceptDaoSvc(myTermConceptDaoSvc);

		when(myTermCodeSystemVersionDao.findById(any())).thenReturn(Optional.of(myTermCodeSystemVersion));
		svc.setCodeSystemVersionDaoForUnitTest(myTermCodeSystemVersionDao);
		svc.setProcessDeferred(true);
		svc.addConceptToStorageQueue(concept);
		svc.saveDeferred();
		verify(myTermConceptDaoSvc, times(1)).saveConcept(same(concept));
		verifyNoMoreInteractions(myTermConceptDaoSvc);

	}

	@Test
	void testSaveDeferred_Concept_StaleCodeSystemVersion() {
		TermConcept concept = new TermConcept();
		concept.setCode("CODE_A");

		TermCodeSystemVersion myTermCodeSystemVersion = new TermCodeSystemVersion();
		myTermCodeSystemVersion.setId(1L);
		concept.setCodeSystemVersion(myTermCodeSystemVersion);

		TermDeferredStorageSvcImpl svc = new TermDeferredStorageSvcImpl();
		svc.setTransactionManagerForUnitTest(myTxManager);
		svc.setTermConceptDaoSvc(myTermConceptDaoSvc);

		when(myTermCodeSystemVersionDao.findById(any())).thenReturn(Optional.empty());
		svc.setCodeSystemVersionDaoForUnitTest(myTermCodeSystemVersionDao);
		svc.setProcessDeferred(true);
		svc.addConceptToStorageQueue(concept);
		svc.saveDeferred();

		verify(myTermConceptDaoSvc, times(0)).saveConcept(same(concept));
		verifyNoMoreInteractions(myTermConceptDaoSvc);

	}

	@Test
	void testSaveDeferred_Concept_Exception() {
		// There is a small
		TermConcept concept = new TermConcept();
		concept.setCode("CODE_A");

		TermCodeSystemVersion myTermCodeSystemVersion = new TermCodeSystemVersion();
		myTermCodeSystemVersion.setId(1L);
		concept.setCodeSystemVersion(myTermCodeSystemVersion);

		TermDeferredStorageSvcImpl svc = new TermDeferredStorageSvcImpl();
		svc.setTransactionManagerForUnitTest(myTxManager);
		svc.setTermConceptDaoSvc(myTermConceptDaoSvc);

		// Simulate the case where an exception is thrown despite a valid code system version.
		when(myTermCodeSystemVersionDao.findById(any())).thenReturn(Optional.of(myTermCodeSystemVersion));
		when(myTermConceptDaoSvc.saveConcept(concept)).thenThrow(new RuntimeException("Foreign Constraint Violation"));
		svc.setCodeSystemVersionDaoForUnitTest(myTermCodeSystemVersionDao);
		svc.setProcessDeferred(true);
		svc.addConceptToStorageQueue(concept);
		svc.saveDeferred();

		verify(myTermConceptDaoSvc, times(1)).saveConcept(same(concept));
		verifyNoMoreInteractions(myTermConceptDaoSvc);

	}

	@Test
	void testSaveDeferred_ConceptParentChildLink_ConceptsMissing() {
		TermConceptParentChildLink conceptLink = new TermConceptParentChildLink();
		conceptLink.setChild(new TermConcept().setId(111L));
		conceptLink.setParent(new TermConcept().setId(222L));

		TermDeferredStorageSvcImpl svc = new TermDeferredStorageSvcImpl();
		svc.setTransactionManagerForUnitTest(myTxManager);
		svc.setTermConceptDaoSvc(myTermConceptDaoSvc);
		svc.setConceptDaoForUnitTest(myConceptDao);
		svc.setProcessDeferred(true);
		svc.addConceptLinkToStorageQueue(conceptLink);
		svc.saveDeferred();

		verifyNoMoreInteractions(myTermConceptDaoSvc);
	}

}
