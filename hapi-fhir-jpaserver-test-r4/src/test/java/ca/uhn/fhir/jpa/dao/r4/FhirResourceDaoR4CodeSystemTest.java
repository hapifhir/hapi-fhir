package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.term.models.TermCodeSystemDeleteJobParameters;
import ca.uhn.fhir.jpa.term.models.TermCodeSystemDeleteVersionJobParameters;
import ca.uhn.fhir.jpa.term.TermReindexingSvcImpl;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static ca.uhn.fhir.batch2.jobs.termcodesystem.TermCodeSystemJobConfig.TERM_CODE_SYSTEM_DELETE_JOB_NAME;
import static ca.uhn.fhir.batch2.jobs.termcodesystem.TermCodeSystemJobConfig.TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

public class FhirResourceDaoR4CodeSystemTest extends BaseJpaR4Test {

	@Autowired private Batch2JobHelper myBatchJobHelper;

	@Test
	public void testIndexContained() throws Exception {
		TermReindexingSvcImpl.setForceSaveDeferredAlwaysForUnitTest(true);

		String input = IOUtils.toString(getClass().getResource("/r4/codesystem_complete.json"), StandardCharsets.UTF_8);
		CodeSystem cs = myFhirContext.newJsonParser().parseResource(CodeSystem.class, input);
		myCodeSystemDao.create(cs, mySrd);

		myResourceReindexingSvc.markAllResourcesForReindexing();
		int outcome = myResourceReindexingSvc.forceReindexingPass();
		assertThat(outcome).isNotEqualTo(-1); // -1 means there was a failure

		myTerminologyDeferredStorageSvc.saveDeferred();

	}

	@Test
	public void testDeleteLargeCompleteCodeSystem() {
		IIdType id = createLargeCodeSystem(null);

		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertNotNull(myTermCodeSystemDao.findByCodeSystemUri("http://foo"));
			assertEquals(1, myTermCodeSystemVersionDao.count());
			assertEquals(222, myTermConceptDao.count());
		});

		myCodeSystemDao.delete(id);

		// Nothing is deleted initially but the URI is changed so it can't be found
		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertNull(myTermCodeSystemDao.findByCodeSystemUri("http://foo"));
			assertEquals(1, myTermCodeSystemVersionDao.count());
			assertEquals(222, myTermConceptDao.count());
		});

		// Now the background scheduler will do its thing
		myTerminologyDeferredStorageSvc.saveDeferred();
		myBatchJobHelper.awaitAllJobsOfJobDefinitionIdToComplete(TERM_CODE_SYSTEM_DELETE_JOB_NAME);
		runInTransaction(() -> {
			assertEquals(0, myTermCodeSystemDao.count());
			assertEquals(0, myTermCodeSystemVersionDao.count());
			assertEquals(0, myTermConceptDao.count());
		});

	}

	@Test
	public void testDeleteCodeSystemVersion() {

		// Create code system with two versions.
		IIdType id_first = createLargeCodeSystem("1");

		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertNotNull(myTermCodeSystemDao.findByCodeSystemUri("http://foo"));
			assertEquals(1, myTermCodeSystemVersionDao.count());
			List<ResourceTable> resourceList = myResourceTableDao.findAll();
			assertEquals(222, myTermConceptDao.count());
			assertEquals(1, resourceList.size());
			assertNull(resourceList.get(0).getDeleted());
		});

		IIdType id_second = createLargeCodeSystem("2");

		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertNotNull(myTermCodeSystemDao.findByCodeSystemUri("http://foo"));
			assertEquals(2, myTermCodeSystemVersionDao.count());
			assertEquals(444, myTermConceptDao.count());
			List<ResourceTable> resourceList = myResourceTableDao.findAll();
			assertEquals(2, resourceList.size());
			long active = resourceList
				.stream()
				.filter(t -> t.getDeleted() == null).count();
			assertEquals(2, active);
		});

		// Attempt to delete first version
		myCodeSystemDao.delete(id_first, mySrd);

		// Only the resource will be deleted initially
		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertNotNull(myTermCodeSystemDao.findByCodeSystemUri("http://foo"));
			assertEquals(2, myTermCodeSystemVersionDao.count());
			assertEquals(444, myTermConceptDao.count());
			List<ResourceTable> resourceList = myResourceTableDao.findAll();
			assertEquals(2, resourceList.size());
			long active = resourceList
				.stream()
				.filter(t -> t.getDeleted() == null).count();
			assertEquals(1, active);
		});

		// Now the background scheduler will do its thing
		myTerminologyDeferredStorageSvc.saveDeferred();
		myBatchJobHelper.awaitAllJobsOfJobDefinitionIdToComplete(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME);

		// Entities for first resource should be gone now.
		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertNotNull(myTermCodeSystemDao.findByCodeSystemUri("http://foo"));
			assertEquals(1, myTermCodeSystemVersionDao.count());
			assertEquals(222, myTermConceptDao.count());
			List<ResourceTable> resourceList = myResourceTableDao.findAll();
			assertEquals(2, resourceList.size());
			long active = resourceList
				.stream()
				.filter(t -> t.getDeleted() == null).count();
			assertEquals(1, active);
		});

		// Attempt to delete second version
		myCodeSystemDao.delete(id_second, mySrd);

		// Only the resource will be deleted initially, but the URL for the TermCodeSystem will be cleared and not searchable.
		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertNull(myTermCodeSystemDao.findByCodeSystemUri("http://foo"));
			assertEquals(1, myTermCodeSystemVersionDao.count());
			assertEquals(222,  myTermConceptDao.count());
			List<ResourceTable> resourceList = myResourceTableDao.findAll();
			assertEquals(2, resourceList.size());
			long active = resourceList
				.stream()
				.filter(t -> t.getDeleted() == null).count();
			assertEquals(0, active);
		});

		// Now the background scheduler will do its thing
		myTerminologyDeferredStorageSvc.saveDeferred();
		myBatchJobHelper.awaitAllJobsOfJobDefinitionIdToComplete(TERM_CODE_SYSTEM_DELETE_JOB_NAME);

		// The remaining versions and Code System entities should be gone now.
		runInTransaction(() -> {
			assertEquals(0, myTermCodeSystemDao.count());
			assertNull(myTermCodeSystemDao.findByCodeSystemUri("http://foo"));
			assertEquals(0, myTermCodeSystemVersionDao.count());
			List<ResourceTable> resourceList = myResourceTableDao.findAll();
			assertEquals(2, resourceList.size());
			long active = resourceList
				.stream()
				.filter(t -> t.getDeleted() == null).count();
			assertEquals(0, active);
		});

	}

	@Test
	void update_transactionRollsBack_createsNoVersionDeleteJobAndKeepsTheConcepts() {
		IIdType id = createLargeCodeSystem(null);
		CodeSystem replacement = newCodeSystemWithSingleConcept(id);

		new TransactionTemplate(myTxManager).executeWithoutResult(tx -> {
			myCodeSystemDao.update(replacement, mySrd);
			tx.setRollbackOnly();
		});

		// the scheduled task must find nothing to do for the update that never happened
		myTerminologyDeferredStorageSvc.saveDeferred();
		assertThat(myJobCoordinator.getJobInstancesByJobDefinitionId(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME, 10, 0)).isEmpty();
		myBatchJobHelper.awaitNoJobsRunning();
		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemVersionDao.count());
			assertEquals(222, myTermConceptDao.count());
			assertNotNull(myTermCodeSystemDao.findByCodeSystemUri("http://foo").getCurrentVersion());
		});
	}

	@Test
	void update_commits_versionDeleteJobExistsWithoutTheScheduledTask() {
		IIdType id = createLargeCodeSystem(null);

		myCodeSystemDao.update(newCodeSystemWithSingleConcept(id), mySrd);

		// saveDeferred() is deliberately not called: the update itself stores the job
		assertThat(myJobCoordinator.getJobInstancesByJobDefinitionId(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME, 10, 0)).hasSize(1);
		myBatchJobHelper.awaitAllJobsOfJobDefinitionIdToComplete(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME);
		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemVersionDao.count());
			assertEquals(1, myTermConceptDao.count());
			assertNotNull(myTermCodeSystemDao.findByCodeSystemUri("http://foo").getCurrentVersion());
		});
	}

	@Test
	void delete_transactionRollsBack_createsNoDeleteJobAndKeepsTheCodeSystem() {
		IIdType id = createLargeCodeSystem(null);

		new TransactionTemplate(myTxManager).executeWithoutResult(tx -> {
			myCodeSystemDao.delete(id, mySrd);
			tx.setRollbackOnly();
		});

		myTerminologyDeferredStorageSvc.saveDeferred();
		assertThat(myJobCoordinator.getJobInstancesByJobDefinitionId(TERM_CODE_SYSTEM_DELETE_JOB_NAME, 10, 0)).isEmpty();
		myBatchJobHelper.awaitNoJobsRunning();
		runInTransaction(() -> {
			assertNotNull(myTermCodeSystemDao.findByCodeSystemUri("http://foo"));
			assertEquals(1, myTermCodeSystemVersionDao.count());
			assertEquals(222, myTermConceptDao.count());
		});
	}

	@Test
	void delete_commits_deleteJobExistsWithoutTheScheduledTask() {
		IIdType id = createLargeCodeSystem(null);

		myCodeSystemDao.delete(id, mySrd);

		assertThat(myJobCoordinator.getJobInstancesByJobDefinitionId(TERM_CODE_SYSTEM_DELETE_JOB_NAME, 10, 0)).hasSize(1);
		myBatchJobHelper.awaitAllJobsOfJobDefinitionIdToComplete(TERM_CODE_SYSTEM_DELETE_JOB_NAME);
		runInTransaction(() -> {
			assertEquals(0, myTermCodeSystemDao.count());
			assertEquals(0, myTermCodeSystemVersionDao.count());
			assertEquals(0, myTermConceptDao.count());
		});
	}

	@Test
	void isStorageQueueEmpty_deleteJobStartedElsewhereHasNotEnded_reportsNotEmptyUntilItEnds() {
		// a delete job this node's deferred storage service never saw, as when another node stored the CodeSystem
		TermCodeSystemDeleteVersionJobParameters parameters = new TermCodeSystemDeleteVersionJobParameters();
		parameters.setCodeSystemVersionPid(999_999_999L);
		JobInstanceStartRequest request = new JobInstanceStartRequest();
		request.setJobDefinitionId(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME);
		request.setParameters(parameters);
		myJobCoordinator.startInstance(mySrd, request);

		assertThat(myTerminologyDeferredStorageSvc.isStorageQueueEmpty(true)).isFalse();
		assertThat(myTerminologyDeferredStorageSvc.isStorageQueueEmpty(false)).isTrue();

		myBatchJobHelper.awaitAllJobsOfJobDefinitionIdToComplete(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME);
		assertThat(myTerminologyDeferredStorageSvc.isStorageQueueEmpty(true)).isTrue();
	}

	@Test
	void create_versionHasTheFormOfAVersionMarkedForDeletion_isRejectedAndStoresNothing() {
		CodeSystem cs = new CodeSystem();
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		cs.setUrl("http://foo");
		cs.setVersion("DELETED_" + UUID.randomUUID());
		cs.addConcept().setCode("CODE");

		assertThatThrownBy(() -> myCodeSystemDao.create(cs, mySrd))
				.isInstanceOf(UnprocessableEntityException.class)
				.hasMessageContaining(Msg.code(3083));

		runInTransaction(() -> {
			assertThat(myResourceTableDao.count()).isZero();
			assertThat(myTermCodeSystemDao.count()).isZero();
			assertThat(myTermCodeSystemVersionDao.count()).isZero();
			assertThat(myTermConceptDao.count()).isZero();
		});
	}

	@Test
	void update_versionChangesToTheFormOfAVersionMarkedForDeletion_isRejectedAndKeepsTheStoredVersion() {
		IIdType id = createLargeCodeSystem("1");
		CodeSystem update = newCodeSystemWithSingleConcept(id);
		update.setVersion("DELETED_" + UUID.randomUUID());

		assertThatThrownBy(() -> myCodeSystemDao.update(update, mySrd))
				.isInstanceOf(UnprocessableEntityException.class)
				.hasMessageContaining(Msg.code(3083));

		assertThat(findJobs(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME)).isEmpty();
		runInTransaction(() -> {
			assertThat(myTermCodeSystemVersionDao.findAll())
					.extracting(TermCodeSystemVersion::getCodeSystemVersionId)
					.containsExactly("1");
			assertThat(myTermConceptDao.count()).isEqualTo(222);
		});
	}

	@Test
	void resumeCodeSystemDeletions_versionRenamedForDeletionHasNoJob_removesTheVersionAndItsConcepts() {
		createLargeCodeSystem(null);
		renameVersionsForDeletionWithoutJob();

		myTerminologyDeferredStorageSvc.resumeCodeSystemDeletions();

		assertThat(findJobs(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME)).hasSize(1);
		myBatchJobHelper.awaitAllJobsOfJobDefinitionIdToComplete(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME);
		runInTransaction(() -> {
			assertThat(myTermCodeSystemVersionDao.count()).isZero();
			assertThat(myTermConceptDao.count()).isZero();
		});
	}

	@Test
	void resumeCodeSystemDeletions_versionOfDeletedCodeSystemWasNotRenamedAndItsJobWasCancelled_removesTheVersion() {
		IIdType firstId = createLargeCodeSystem("1");
		createLargeCodeSystem("2");
		// the TermCodeSystem row belongs to the second resource, so this delete does not rename the first version
		myCodeSystemDao.delete(firstId, mySrd);
		cancelTheOnlyJob(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME);
		runInTransaction(() -> assertThat(myTermCodeSystemVersionDao.count()).isEqualTo(2));

		myTerminologyDeferredStorageSvc.resumeCodeSystemDeletions();

		assertThat(findJobs(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME)).hasSize(2);
		myBatchJobHelper.awaitAllJobsOfJobDefinitionIdToComplete(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME);
		runInTransaction(() -> {
			assertThat(myTermCodeSystemDao.findByCodeSystemUri("http://foo")).isNotNull();
			assertThat(myTermCodeSystemVersionDao.count()).isEqualTo(1);
			assertThat(myTermConceptDao.count()).isEqualTo(222);
		});
	}

	@Test
	void resumeCodeSystemDeletions_deletedCodeSystemWhoseJobWasCancelled_removesItWithACodeSystemJobOnly() {
		IIdType id = createLargeCodeSystem(null);
		myCodeSystemDao.delete(id, mySrd);
		cancelTheOnlyJob(TERM_CODE_SYSTEM_DELETE_JOB_NAME);
		runInTransaction(() -> assertThat(myTermCodeSystemDao.count()).isEqualTo(1));

		myTerminologyDeferredStorageSvc.resumeCodeSystemDeletions();

		// the version belongs to a deleted CodeSystem as well, but the code system job is the one that removes it
		assertThat(findJobs(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME)).isEmpty();
		assertThat(findJobs(TERM_CODE_SYSTEM_DELETE_JOB_NAME)).hasSize(2);
		myBatchJobHelper.awaitAllJobsOfJobDefinitionIdToComplete(TERM_CODE_SYSTEM_DELETE_JOB_NAME);
		runInTransaction(() -> {
			assertThat(myTermCodeSystemDao.count()).isZero();
			assertThat(myTermCodeSystemVersionDao.count()).isZero();
			assertThat(myTermConceptDao.count()).isZero();
		});
	}

	@Test
	void resumeCodeSystemDeletions_deleteJobOfTheDeletedCodeSystemHasNotEnded_startsNoSecondJob() {
		IIdType id = createLargeCodeSystem(null);
		// the job of this delete has not ended: it waits for a maintenance pass, and none has run
		myCodeSystemDao.delete(id, mySrd);

		myTerminologyDeferredStorageSvc.resumeCodeSystemDeletions();

		assertThat(findJobs(TERM_CODE_SYSTEM_DELETE_JOB_NAME)).hasSize(1);
		assertThat(findJobs(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME)).isEmpty();
		myBatchJobHelper.awaitAllJobsOfJobDefinitionIdToComplete(TERM_CODE_SYSTEM_DELETE_JOB_NAME);
		runInTransaction(() -> {
			assertThat(myTermCodeSystemDao.count()).isZero();
			assertThat(myTermCodeSystemVersionDao.count()).isZero();
			assertThat(myTermConceptDao.count()).isZero();
		});
	}

	@Test
	void resumeCodeSystemDeletions_deleteJobOfAnotherDeletedCodeSystemHasNotEnded_startsAJobForTheOneWithoutAJob() {
		IIdType idWithoutJob = createLargeCodeSystem("http://bar", null);
		long pidWithoutJob = runInTransaction(
				() -> myTermCodeSystemDao.findByCodeSystemUri("http://bar").getPid());
		myCodeSystemDao.delete(idWithoutJob, mySrd);
		cancelTheOnlyJob(TERM_CODE_SYSTEM_DELETE_JOB_NAME);
		IIdType idWithJob = createLargeCodeSystem(null);
		long pidWithJob = runInTransaction(
				() -> myTermCodeSystemDao.findByCodeSystemUri("http://foo").getPid());
		// the job of this delete has not ended: it waits for a maintenance pass, and none has run
		myCodeSystemDao.delete(idWithJob, mySrd);

		myTerminologyDeferredStorageSvc.resumeCodeSystemDeletions();

		// the cancelled job and the new job of the first code system, and the job of the second that has not ended
		assertThat(findJobs(TERM_CODE_SYSTEM_DELETE_JOB_NAME))
				.extracting(job -> job.getParameters(TermCodeSystemDeleteJobParameters.class)
						.getTermPid())
				.containsExactlyInAnyOrder(pidWithoutJob, pidWithoutJob, pidWithJob);
		assertThat(findJobs(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME)).isEmpty();
		myBatchJobHelper.awaitAllJobsOfJobDefinitionIdToComplete(TERM_CODE_SYSTEM_DELETE_JOB_NAME);
		runInTransaction(() -> {
			assertThat(myTermCodeSystemDao.count()).isZero();
			assertThat(myTermCodeSystemVersionDao.count()).isZero();
			assertThat(myTermConceptDao.count()).isZero();
		});
	}

	@Test
	void resumeCodeSystemDeletions_liveCodeSystemLooksLikeARenamedOne_startsNoJob() {
		// urn:uuid: is a legal CodeSystem URL, and a version may start with DELETED_
		CodeSystem cs = new CodeSystem();
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		cs.setUrl("urn:uuid:" + UUID.randomUUID());
		cs.setVersion("DELETED_SCENES");
		cs.addConcept().setCode("CODE");
		myCodeSystemDao.create(cs, mySrd);

		myTerminologyDeferredStorageSvc.resumeCodeSystemDeletions();

		assertThat(findJobs(TERM_CODE_SYSTEM_DELETE_JOB_NAME)).isEmpty();
		assertThat(findJobs(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME)).isEmpty();
		runInTransaction(() -> {
			assertThat(myTermCodeSystemDao.count()).isEqualTo(1);
			assertThat(myTermCodeSystemVersionDao.count()).isEqualTo(1);
			assertThat(myTermConceptDao.count()).isEqualTo(1);
		});
	}

	@Test
	void resumeCodeSystemDeletions_deleteJobOfTheVersionHasNotEnded_startsNoSecondJob() {
		createLargeCodeSystem(null);
		renameVersionsForDeletionWithoutJob();
		myTerminologyDeferredStorageSvc.resumeCodeSystemDeletions();

		// the job of the first call has not ended: it waits for a maintenance pass, and none has run
		myTerminologyDeferredStorageSvc.resumeCodeSystemDeletions();

		assertThat(findJobs(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME)).hasSize(1);
		myBatchJobHelper.awaitAllJobsOfJobDefinitionIdToComplete(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME);
		runInTransaction(() -> assertThat(myTermCodeSystemVersionDao.count()).isZero());
	}

	@Test
	void resumeCodeSystemDeletions_twoVersionsRenamedForDeletionHaveNoJob_startsAJobForEach() {
		createLargeCodeSystem("1");
		createLargeCodeSystem("2");
		renameVersionsForDeletionWithoutJob();
		List<Long> versionPids = runInTransaction(() -> myTermCodeSystemVersionDao.findAll().stream()
				.map(TermCodeSystemVersion::getPid)
				.toList());

		myTerminologyDeferredStorageSvc.resumeCodeSystemDeletions();

		// the job of the first version has not ended when the second version is handled
		assertThat(findJobs(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME))
				.extracting(job -> job.getParameters(TermCodeSystemDeleteVersionJobParameters.class)
						.getCodeSystemVersionPid())
				.containsExactlyInAnyOrderElementsOf(versionPids);
		myBatchJobHelper.awaitAllJobsOfJobDefinitionIdToComplete(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME);
		runInTransaction(() -> {
			assertThat(myTermCodeSystemVersionDao.count()).isZero();
			assertThat(myTermConceptDao.count()).isZero();
		});
	}

	@Test
	void resumeCodeSystemDeletions_anotherTransactionHoldsTheVersionAndHasNotCommittedItsJob_startsNoSecondJob()
			throws Exception {
		createLargeCodeSystem(null);
		renameVersionsForDeletionWithoutJob();
		long versionPid = runInTransaction(() -> myTermCodeSystemVersionDao.findAll().get(0).getPid());

		ExecutorService executor = Executors.newSingleThreadExecutor();
		try {
			Future<?> resumeOnOtherNode = new TransactionTemplate(myTxManager).execute(tx -> {
				// what a node does for the version: it locks the row, then stores the delete job
				TermCodeSystemVersion version =
						myTermCodeSystemVersionDao.findByPidForUpdate(versionPid).orElseThrow();
				myTerminologyDeferredStorageSvc.deleteCodeSystemVersion(version);

				Future<?> future = executor.submit(() -> myTerminologyDeferredStorageSvc.resumeCodeSystemDeletions());
				try {
					// without the row lock, the call ends here with a second job stored
					future.get(3, TimeUnit.SECONDS);
				} catch (TimeoutException e) {
					// it is waiting for the row
				} catch (Exception e) {
					throw new AssertionError(e);
				}
				return future;
			});
			resumeOnOtherNode.get(30, TimeUnit.SECONDS);
		} finally {
			executor.shutdownNow();
		}

		assertThat(findJobs(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME)).hasSize(1);
		myBatchJobHelper.awaitAllJobsOfJobDefinitionIdToComplete(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME);
		runInTransaction(() -> assertThat(myTermCodeSystemVersionDao.count()).isZero());
	}

	/**
	 * Renames every stored version to {@code DELETED_<uuid>} and starts no delete job. In earlier releases, a
	 * CodeSystem update left the version it replaced in this state when the server stopped before the delete job
	 * was created.
	 */
	private void renameVersionsForDeletionWithoutJob() {
		runInTransaction(() -> myTermCodeSystemVersionDao.findAll().forEach(version -> {
			version.setCodeSystemVersionId("DELETED_" + UUID.randomUUID());
			myTermCodeSystemVersionDao.save(version);
		}));
	}

	/** No maintenance pass runs between the creation of the job and its cancellation, so the job deletes nothing. */
	private void cancelTheOnlyJob(String theJobDefinitionId) {
		List<JobInstance> jobs = findJobs(theJobDefinitionId);
		assertThat(jobs).hasSize(1);
		String instanceId = jobs.get(0).getInstanceId();
		myJobCoordinator.cancelInstance(instanceId);
		myBatchJobHelper.awaitJobCancelled(instanceId);
	}

	private List<JobInstance> findJobs(String theJobDefinitionId) {
		return myJobCoordinator.getJobInstancesByJobDefinitionId(theJobDefinitionId, 10, 0);
	}

	private CodeSystem newCodeSystemWithSingleConcept(IIdType theId) {
		CodeSystem cs = new CodeSystem();
		cs.setId(theId);
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		cs.setUrl("http://foo");
		cs.addConcept().setCode("REPLACEMENT");
		return cs;
	}

	private IIdType createLargeCodeSystem(String theVersion) {
		return createLargeCodeSystem("http://foo", theVersion);
	}

	private IIdType createLargeCodeSystem(String theUrl, String theVersion) {
		CodeSystem cs = new CodeSystem();
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		cs.setUrl(theUrl);
		if (theVersion != null) {
			cs.setVersion(theVersion);
		}
		for (int i = 0; i < 222; i++) {
			cs.addConcept().setCode("CODE" + i);
		}
		IIdType id = myCodeSystemDao.create(cs).getId().toUnqualifiedVersionless();
		myTerminologyDeferredStorageSvc.saveDeferred();
		myTerminologyDeferredStorageSvc.saveDeferred();
		return id;
	}

	@Test
	public void testCodeSystemWithDuplicateCode() {
		CodeSystem cs = new CodeSystem();
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		cs.setUrl("http://foo");
		cs.setVersion("1.0");
		cs.addConcept().setCode("CODE0").setDisplay("Code0");
		cs.addConcept().setCode("CODE1").setDisplay("Code1");
		cs.addConcept().setCode("CODE1").setDisplay("Code1");
		cs.addConcept().setCode("CODE2").setDisplay("Code2");

		try {
			myCodeSystemDao.create(cs, mySrd);
			fail();
		} catch (PreconditionFailedException e) {
			assertThat(e.getMessage()).contains("Duplicate concept detected in CodeSystem: CODE1");
		}
	}

	@Test
	public void testCodeSystemWithDuplicateCodeInChild() {
		CodeSystem cs = new CodeSystem();
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		cs.setUrl("http://foo");
		cs.setVersion("1.0");

		CodeSystem.ConceptDefinitionComponent parent = cs.addConcept().setCode("CODE0").setDisplay("Code0");
		parent.addConcept().setCode("CODE1").setDisplay("Code1");
		parent.addConcept().setCode("CODE1").setDisplay("Code1");
		cs.addConcept().setCode("CODE2").setDisplay("Code2");

		try {
			myCodeSystemDao.create(cs, mySrd);
			fail();
		} catch (PreconditionFailedException e) {
			assertThat(e.getMessage()).contains("Duplicate concept detected in CodeSystem: CODE1");
		}
	}


	@AfterAll
	public static void afterClassClearContext() {
		TermReindexingSvcImpl.setForceSaveDeferredAlwaysForUnitTest(false);
	}


}
