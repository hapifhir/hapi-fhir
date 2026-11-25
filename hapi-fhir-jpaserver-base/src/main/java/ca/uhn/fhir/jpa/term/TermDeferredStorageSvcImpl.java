/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemDao;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemVersionDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptParentChildLinkDao;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.IHasScheduledJobs;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermVersionAdapterSvc;
import ca.uhn.fhir.jpa.term.models.TermCodeSystemDeleteJobParameters;
import ca.uhn.fhir.jpa.term.models.TermCodeSystemDeleteVersionJobParameters;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.util.TimeoutManager;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ValueSet;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static ca.uhn.fhir.batch2.jobs.termcodesystem.TermCodeSystemJobConfig.TERM_CODE_SYSTEM_DELETE_JOB_NAME;
import static ca.uhn.fhir.batch2.jobs.termcodesystem.TermCodeSystemJobConfig.TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME;

public class TermDeferredStorageSvcImpl implements ITermDeferredStorageSvc, IHasScheduledJobs {

	private static final Logger ourLog = LoggerFactory.getLogger(TermDeferredStorageSvcImpl.class);
	private static final long SAVE_ALL_DEFERRED_WARN_MINUTES = 1;
	private static final long SAVE_ALL_DEFERRED_ERROR_MINUTES = 5;
	private boolean myAllowDeferredTasksTimeout = true;
	private static final List<String> BATCH_JOBS_TO_CARE_ABOUT =
			List.of(TERM_CODE_SYSTEM_DELETE_JOB_NAME, TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME);
	/**
	 * A code system gets a URL with this prefix when its CodeSystem resource is deleted. It is also a legal
	 * CodeSystem URL, so a code system that has it is only marked for deletion if its resource is deleted.
	 */
	private static final String URI_PREFIX_MARKED_FOR_DELETION = "urn:uuid:";

	private static final int DELETE_JOB_PAGE_SIZE = 100;
	private final List<TermConcept> myDeferredConcepts = Collections.synchronizedList(new ArrayList<>());
	private final List<ValueSet> myDeferredValueSets = Collections.synchronizedList(new ArrayList<>());
	private final List<ConceptMap> myDeferredConceptMaps = Collections.synchronizedList(new ArrayList<>());
	private final List<TermConceptParentChildLink> myConceptLinksToSaveLater =
			Collections.synchronizedList(new ArrayList<>());

	@Autowired
	protected ITermConceptDao myConceptDao;

	@Autowired
	private PartitionSettings myPartitionSettings;

	@Autowired
	protected ITermCodeSystemDao myCodeSystemDao;

	@Autowired
	protected ITermCodeSystemVersionDao myCodeSystemVersionDao;

	@Autowired
	protected PlatformTransactionManager myTransactionMgr;

	private boolean myProcessDeferred = true;

	@Autowired
	private ITermConceptParentChildLinkDao myConceptParentChildLinkDao;

	@Autowired
	private ITermVersionAdapterSvc myTerminologyVersionAdapterSvc;

	@Autowired
	private TermConceptDaoSvc myTermConceptDaoSvc;

	@Autowired
	private IJobCoordinator myJobCoordinator;

	@Deprecated(since = "9.2.0", forRemoval = true)
	@Override
	public void addConceptToStorageQueue(TermConcept theConcept) {
		Validate.notNull(theConcept);
		myDeferredConcepts.add(theConcept);
	}

	@Deprecated(since = "9.2.0", forRemoval = true)
	@Override
	public void addConceptLinkToStorageQueue(TermConceptParentChildLink theConceptLink) {
		Validate.notNull(theConceptLink);
		myConceptLinksToSaveLater.add(theConceptLink);
	}

	@Deprecated(since = "9.2.0", forRemoval = true)
	@Override
	public void addConceptMapsToStorageQueue(List<ConceptMap> theConceptMaps) {
		Validate.notNull(theConceptMaps);
		myDeferredConceptMaps.addAll(theConceptMaps);
	}

	@Deprecated(since = "9.2.0", forRemoval = true)
	@Override
	public void addValueSetsToStorageQueue(List<ValueSet> theValueSets) {
		Validate.notNull(theValueSets);
		myDeferredValueSets.addAll(theValueSets);
	}

	@Override
	public void deleteCodeSystemForResource(ResourceTable theCodeSystemToDelete) {
		// there are use cases (at least in tests) where the code system is not present for the resource but versions
		// are,
		// so, as code system deletion also deletes versions, we try the system first but if not present we also try
		// versions
		TermCodeSystem termCodeSystemToDelete = myCodeSystemDao.findByResourcePid(theCodeSystemToDelete.getId());
		if (termCodeSystemToDelete != null) {
			termCodeSystemToDelete.setCodeSystemUri(URI_PREFIX_MARKED_FOR_DELETION + UUID.randomUUID());
			myCodeSystemDao.save(termCodeSystemToDelete);
			startCodeSystemDeleteJob(termCodeSystemToDelete.getPid());
			return;
		}

		List<TermCodeSystemVersion> codeSystemVersionsToDelete =
				myCodeSystemVersionDao.findByCodeSystemResourcePid(theCodeSystemToDelete.getId());
		for (TermCodeSystemVersion codeSystemVersionToDelete : codeSystemVersionsToDelete) {
			if (codeSystemVersionToDelete != null) {
				startCodeSystemVersionDeleteJob(codeSystemVersionToDelete.getPid());
			}
		}
	}

	@Override
	public void setProcessDeferred(boolean theProcessDeferred) {
		myProcessDeferred = theProcessDeferred;
	}

	private void processDeferredConceptMaps() {
		int count = Math.min(myDeferredConceptMaps.size(), 20);
		for (ConceptMap nextConceptMap : new ArrayList<>(myDeferredConceptMaps.subList(0, count))) {
			ourLog.info("Creating ConceptMap: {}", nextConceptMap.getId());
			myTerminologyVersionAdapterSvc.createOrUpdateConceptMap(nextConceptMap);
			myDeferredConceptMaps.remove(nextConceptMap);
		}
		ourLog.info("Saved {} deferred ConceptMap resources, have {} remaining", count, myDeferredConceptMaps.size());
	}

	private void processDeferredConcepts() {
		int codeCount = 0, relCount = 0;
		StopWatch stopwatch = new StopWatch();

		int count = Math.min(1000, myDeferredConcepts.size());
		ourLog.debug("Saving {} deferred concepts...", count);
		while (codeCount < count && myDeferredConcepts.size() > 0) {
			TermConcept next = myDeferredConcepts.remove(0);
			if (myCodeSystemVersionDao
					.findById(next.getCodeSystemVersion().getId())
					.isPresent()) {
				try {
					codeCount += myTermConceptDaoSvc.saveConcept(next);
				} catch (Exception theE) {
					ourLog.error(
							"Exception thrown when attempting to save TermConcept {} in Code System {}",
							next.getCode(),
							next.getCodeSystemVersion().getCodeSystemDisplayName(),
							theE);
				}
			} else {
				ourLog.warn(
						"Unable to save deferred TermConcept {} because Code System {} version PID {} is no longer valid. Code system may have since been replaced.",
						next.getCode(),
						next.getCodeSystemVersion().getCodeSystemDisplayName(),
						next.getCodeSystemVersion().getPid());
			}
		}

		if (codeCount > 0) {
			ourLog.info(
					"Saved {} deferred concepts ({} codes remain and {} relationships remain) in {}ms ({} codes/sec)",
					codeCount,
					myDeferredConcepts.size(),
					myConceptLinksToSaveLater.size(),
					stopwatch.getMillis(),
					stopwatch.formatThroughput(codeCount, TimeUnit.SECONDS));
		}

		if (codeCount == 0) {
			count = Math.min(1000, myConceptLinksToSaveLater.size());
			ourLog.info("Saving {} deferred concept relationships...", count);
			while (relCount < count && myConceptLinksToSaveLater.size() > 0) {
				TermConceptParentChildLink next = myConceptLinksToSaveLater.remove(0);
				assert next.getChild() != null;
				assert next.getParent() != null;

				if ((next.getChild().getId() == null
								|| !myConceptDao
										.findById(next.getChild().getPid())
										.isPresent())
						|| (next.getParent().getId() == null
								|| !myConceptDao
										.findById(next.getParent().getPid())
										.isPresent())) {
					ourLog.warn(
							"Not inserting link from child {} to parent {} because it appears to have been deleted",
							next.getParent().getCode(),
							next.getChild().getCode());
					continue;
				}

				saveConceptLink(next);
				relCount++;
			}
		}

		if (relCount > 0) {
			ourLog.info(
					"Saved {} deferred relationships ({} remain) in {}ms ({} entries/sec)",
					relCount,
					myConceptLinksToSaveLater.size(),
					stopwatch.getMillis(),
					stopwatch.formatThroughput(relCount, TimeUnit.SECONDS));
		}

		if ((myDeferredConcepts.size() + myConceptLinksToSaveLater.size()) == 0) {
			ourLog.info("All deferred concepts and relationships have now been synchronized to the database");
		}
	}

	private void processDeferredValueSets() {
		int count = Math.min(myDeferredValueSets.size(), 200);
		for (ValueSet nextValueSet : new ArrayList<>(myDeferredValueSets.subList(0, count))) {
			ourLog.info("Creating ValueSet: {}", nextValueSet.getId());
			myTerminologyVersionAdapterSvc.createOrUpdateValueSet(nextValueSet);
			myDeferredValueSets.remove(nextValueSet);
		}
		ourLog.info("Saved {} deferred ValueSet resources, have {} remaining", count, myDeferredValueSets.size());
	}

	/**
	 * This method is present only for unit tests, do not call from client code
	 */
	@VisibleForTesting
	public synchronized void clearDeferred() {
		myProcessDeferred = true;
		myDeferredValueSets.clear();
		myDeferredConceptMaps.clear();
		myDeferredConcepts.clear();
		myConceptLinksToSaveLater.clear();
	}

	private <T> T runInTransaction(Supplier<T> theRunnable) {
		assert !TransactionSynchronizationManager.isActualTransactionActive();

		return new TransactionTemplate(myTransactionMgr).execute(tx -> theRunnable.get());
	}

	@Override
	public void saveAllDeferred() {
		TimeoutManager timeoutManager = null;
		if (myAllowDeferredTasksTimeout) {
			timeoutManager = new TimeoutManager(
					TermDeferredStorageSvcImpl.class.getName() + ".saveAllDeferred()",
					Duration.of(SAVE_ALL_DEFERRED_WARN_MINUTES, ChronoUnit.MINUTES),
					Duration.of(SAVE_ALL_DEFERRED_ERROR_MINUTES, ChronoUnit.MINUTES));
		}

		// Don't include executing jobs here since there's no point in thrashing over and over
		// in a busy wait while we wait for batch2 job processes to finish
		while (!isStorageQueueEmpty(false)) {
			if (isProcessDeferredPaused()) {
				ourLog.warn("Deferred storage processing is paused - not saving the deferred entities");
				return;
			}

			if (myAllowDeferredTasksTimeout && timeoutManager != null) {
				timeoutManager.checkTimeout();
			}
			saveDeferred();
		}
	}

	@Transactional(propagation = Propagation.NEVER)
	@Override
	public synchronized void saveDeferred() {
		if (isProcessDeferredPaused()) {
			return;
		}

		for (int i = 0; i < 10; i++) {
			if (!isDeferredConcepts()
					&& !isConceptLinksToSaveLater()
					&& !isDeferredValueSets()
					&& !isDeferredConceptMaps()) {
				return;
			}

			if (isDeferredConceptsOrConceptLinksToSaveLater()) {
				runInTransaction(() -> {
					processDeferredConcepts();
					return null;
				});

				continue;
			}

			if (isDeferredValueSets()) {
				runInTransaction(() -> {
					processDeferredValueSets();
					return null;
				});

				continue;
			}

			if (isDeferredConceptMaps()) {
				runInTransaction(() -> {
					processDeferredConceptMaps();
					return null;
				});
			}
		}
	}

	private void startCodeSystemVersionDeleteJob(Long theCodeSystemVersionPid) {
		JobInstanceStartRequest request = new JobInstanceStartRequest();
		request.setJobDefinitionId(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME);

		TermCodeSystemDeleteVersionJobParameters parameters = new TermCodeSystemDeleteVersionJobParameters();
		parameters.setCodeSystemVersionPid(theCodeSystemVersionPid);
		request.setParameters(parameters);

		SystemRequestDetails requestDetails =
				SystemRequestDetails.forRequestPartitionId(myPartitionSettings.getDefaultRequestPartitionId());
		myJobCoordinator.startInstanceInCurrentTransaction(requestDetails, request);
	}

	private void startCodeSystemDeleteJob(Long theCodeSystemPid) {
		TermCodeSystemDeleteJobParameters parameters = new TermCodeSystemDeleteJobParameters();
		parameters.setTermPid(theCodeSystemPid);
		JobInstanceStartRequest request = new JobInstanceStartRequest();
		request.setParameters(parameters);
		request.setJobDefinitionId(TERM_CODE_SYSTEM_DELETE_JOB_NAME);
		SystemRequestDetails details = new SystemRequestDetails();
		details.setRequestPartitionId(myPartitionSettings.getDefaultRequestPartitionId());
		myJobCoordinator.startInstanceInCurrentTransaction(details, request);
	}

	@Override
	public boolean isStorageQueueEmpty(boolean theIncludeExecutingJobs) {
		boolean retVal = !isProcessDeferredPaused();
		retVal &= !isDeferredConcepts();
		retVal &= !isConceptLinksToSaveLater();
		retVal &= !isDeferredValueSets();
		retVal &= !isDeferredConceptMaps();
		if (retVal && theIncludeExecutingJobs) {
			retVal = !isJobsExecuting();
		}
		return retVal;
	}

	@Override
	public boolean isJobsExecuting() {
		for (String jobDefinitionId : BATCH_JOBS_TO_CARE_ABOUT) {
			boolean hasJobThatHasNotEnded = !myJobCoordinator
					.getInstancesbyJobDefinitionIdAndEndedStatus(jobDefinitionId, false, 1, 0)
					.isEmpty();
			if (hasJobThatHasNotEnded) {
				return true;
			}
		}
		return false;
	}

	private void saveConceptLink(TermConceptParentChildLink next) {
		if (next.getId() == null) {
			myConceptParentChildLinkDao.save(next);
		}
	}

	private boolean isProcessDeferredPaused() {
		return !myProcessDeferred;
	}

	private boolean isDeferredConceptsOrConceptLinksToSaveLater() {
		return isDeferredConcepts() || isConceptLinksToSaveLater();
	}

	private boolean isDeferredConcepts() {
		return !myDeferredConcepts.isEmpty();
	}

	private boolean isConceptLinksToSaveLater() {
		return !myConceptLinksToSaveLater.isEmpty();
	}

	private boolean isDeferredValueSets() {
		return !myDeferredValueSets.isEmpty();
	}

	private boolean isDeferredConceptMaps() {
		return !myDeferredConceptMaps.isEmpty();
	}

	@VisibleForTesting
	void setTransactionManagerForUnitTest(PlatformTransactionManager theTxManager) {
		myTransactionMgr = theTxManager;
	}

	@VisibleForTesting
	void setTermConceptDaoSvc(TermConceptDaoSvc theTermConceptDaoSvc) {
		myTermConceptDaoSvc = theTermConceptDaoSvc;
	}

	@VisibleForTesting
	void setConceptDaoForUnitTest(ITermConceptDao theConceptDao) {
		myConceptDao = theConceptDao;
	}

	@VisibleForTesting
	void setCodeSystemVersionDaoForUnitTest(ITermCodeSystemVersionDao theCodeSystemVersionDao) {
		myCodeSystemVersionDao = theCodeSystemVersionDao;
	}

	@Override
	public void disallowDeferredTaskTimeout() {
		myAllowDeferredTasksTimeout = false;
	}

	@Override
	@VisibleForTesting
	public void logQueueForUnitTest() {
		ourLog.info("isProcessDeferredPaused: {}", isProcessDeferredPaused());
		ourLog.info("isDeferredConcepts: {}", isDeferredConcepts());
		ourLog.info("isConceptLinksToSaveLater: {}", isConceptLinksToSaveLater());
		ourLog.info("isDeferredValueSets: {}", isDeferredValueSets());
		ourLog.info("isDeferredConceptMaps: {}", isDeferredConceptMaps());
	}

	@Override
	public void deleteCodeSystemVersion(TermCodeSystemVersion theCodeSystemVersion) {
		startCodeSystemVersionDeleteJob(theCodeSystemVersion.getPid());
	}

	@Transactional(propagation = Propagation.NEVER)
	@Override
	public void resumeCodeSystemDeletions() {
		// Code systems go first: the delete job of a code system removes its versions, so a version that such a job
		// covers needs no job of its own
		List<Long> codeSystemPids = runInTransaction(() -> myCodeSystemDao.findPidsMarkedForDeletion());
		for (Long codeSystemPid : codeSystemPids) {
			resumeDeletion("code system", codeSystemPid, () -> startCodeSystemDeleteJobIfNoneIsRunning(codeSystemPid));
		}

		List<Long> versionPids = runInTransaction(() -> myCodeSystemVersionDao.findPidsMarkedForDeletion());
		for (Long versionPid : versionPids) {
			resumeDeletion(
					"code system version",
					versionPid,
					() -> startCodeSystemVersionDeleteJobIfNoneIsRunning(versionPid));
		}
	}

	/**
	 * @param theJobStarter runs in its own transaction, and returns {@literal true} if it started a delete job
	 */
	private void resumeDeletion(String theRowDescription, Long thePid, Supplier<Boolean> theJobStarter) {
		try {
			if (Boolean.TRUE.equals(runInTransaction(theJobStarter))) {
				ourLog.info(
						"Started a delete job for {} {}, which was marked for deletion and had no delete job in progress",
						theRowDescription,
						thePid);
			}
		} catch (Exception e) {
			// The next run tries this row again. It must not stop the rows after it.
			ourLog.warn(
					"Failed to start a delete job for {} {}, which is marked for deletion",
					theRowDescription,
					thePid,
					e);
		}
	}

	private boolean startCodeSystemDeleteJobIfNoneIsRunning(Long theCodeSystemPid) {
		// The row lock makes another node that handles this row wait until this transaction has stored its job
		Optional<TermCodeSystem> codeSystem = myCodeSystemDao.findByPidForUpdate(theCodeSystemPid);
		if (codeSystem.isEmpty() || !isMarkedForDeletion(codeSystem.get())) {
			return false;
		}
		if (isCodeSystemDeleteJobRunning(theCodeSystemPid)) {
			return false;
		}
		startCodeSystemDeleteJob(theCodeSystemPid);
		return true;
	}

	private boolean startCodeSystemVersionDeleteJobIfNoneIsRunning(Long theVersionPid) {
		// The row lock makes another node that handles this row wait until this transaction has stored its job
		Optional<TermCodeSystemVersion> version = myCodeSystemVersionDao.findByPidForUpdate(theVersionPid);
		if (version.isEmpty() || !isMarkedForDeletion(version.get())) {
			return false;
		}
		if (isCodeSystemVersionDeleteJobRunning(theVersionPid)) {
			return false;
		}
		Long codeSystemPid = version.get().getCodeSystemPid();
		if (codeSystemPid != null && isCodeSystemDeleteJobRunning(codeSystemPid)) {
			return false;
		}
		startCodeSystemVersionDeleteJob(theVersionPid);
		return true;
	}

	private static boolean isMarkedForDeletion(TermCodeSystem theCodeSystem) {
		return theCodeSystem.getCodeSystemUri().startsWith(URI_PREFIX_MARKED_FOR_DELETION)
				&& theCodeSystem.getResource().getDeleted() != null;
	}

	private static boolean isMarkedForDeletion(TermCodeSystemVersion theVersion) {
		boolean renamedForDeletion =
				TermCodeSystemStorageSvcImpl.isVersionIdMarkedForDeletion(theVersion.getCodeSystemVersionId());
		return renamedForDeletion || theVersion.getResource().getDeleted() != null;
	}

	private boolean isCodeSystemDeleteJobRunning(long theCodeSystemPid) {
		return isDeleteJobRunning(
				TERM_CODE_SYSTEM_DELETE_JOB_NAME,
				instance -> instance.getParameters(TermCodeSystemDeleteJobParameters.class)
								.getTermPid()
						== theCodeSystemPid);
	}

	private boolean isCodeSystemVersionDeleteJobRunning(long theVersionPid) {
		return isDeleteJobRunning(
				TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME,
				instance -> instance.getParameters(TermCodeSystemDeleteVersionJobParameters.class)
								.getCodeSystemVersionPid()
						== theVersionPid);
	}

	/**
	 * @return {@literal true} if a job of the given definition that matches the filter has not ended. A job that is
	 * retrying after an error has not ended either.
	 */
	private boolean isDeleteJobRunning(String theJobDefinitionId, Predicate<JobInstance> theFilter) {
		for (int pageIndex = 0; ; pageIndex++) {
			List<JobInstance> instances = myJobCoordinator.getInstancesbyJobDefinitionIdAndEndedStatus(
					theJobDefinitionId, false, DELETE_JOB_PAGE_SIZE, pageIndex);
			if (instances.stream().anyMatch(theFilter)) {
				return true;
			}
			if (instances.size() < DELETE_JOB_PAGE_SIZE) {
				return false;
			}
		}
	}

	@Override
	public void scheduleJobs(ISchedulerService theSchedulerService) {
		// TODO KHS what does this mean?
		// Register scheduled job to save deferred concepts
		// In the future it would be great to make this a cluster-aware task somehow
		ScheduledJobDefinition jobDefinition = new ScheduledJobDefinition();
		jobDefinition.setId(Job.class.getName());
		jobDefinition.setJobClass(Job.class);
		theSchedulerService.scheduleLocalJob(5000, jobDefinition);

		// This job fires when the scheduler starts, and then once a day
		ScheduledJobDefinition resumeDeletionsJobDefinition = new ScheduledJobDefinition();
		resumeDeletionsJobDefinition.setId(ResumeCodeSystemDeletionsJob.class.getName());
		resumeDeletionsJobDefinition.setJobClass(ResumeCodeSystemDeletionsJob.class);
		theSchedulerService.scheduleClusteredJob(DateUtils.MILLIS_PER_DAY, resumeDeletionsJobDefinition);
	}

	public static class Job implements HapiJob {
		@Autowired
		private ITermDeferredStorageSvc myTerminologySvc;

		@Override
		public void execute(JobExecutionContext theContext) {
			myTerminologySvc.saveDeferred();
		}
	}

	/**
	 * Calls {@link ITermDeferredStorageSvc#resumeCodeSystemDeletions()}.
	 */
	public static class ResumeCodeSystemDeletionsJob implements HapiJob {
		@Autowired
		private ITermDeferredStorageSvc myTerminologySvc;

		@Override
		public void execute(@Nullable JobExecutionContext theContext) {
			myTerminologySvc.resumeCodeSystemDeletions();
		}
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("myDeferredConcepts", myDeferredConcepts.size())
				.append("myDeferredValueSets", myDeferredValueSets.size())
				.append("myDeferredConceptMaps", myDeferredConceptMaps.size())
				.append("myConceptLinksToSaveLater", myConceptLinksToSaveLater.size())
				.append("myProcessDeferred", myProcessDeferred)
				.toString();
	}
}
