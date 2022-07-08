package ca.uhn.fhir.jpa.term;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.batch.api.IBatchJobSubmitter;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemDao;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemVersionDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptParentChildLinkDao;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermVersionAdapterSvc;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.StopWatch;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ValueSet;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static ca.uhn.fhir.jpa.batch.config.BatchConstants.JOB_PARAM_CODE_SYSTEM_ID;
import static ca.uhn.fhir.jpa.batch.config.BatchConstants.JOB_PARAM_CODE_SYSTEM_VERSION_ID;
import static ca.uhn.fhir.jpa.batch.config.BatchConstants.TERM_CODE_SYSTEM_DELETE_JOB_NAME;
import static ca.uhn.fhir.jpa.batch.config.BatchConstants.TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME;

public class TermDeferredStorageSvcImpl implements ITermDeferredStorageSvc {

	private static final Logger ourLog = LoggerFactory.getLogger(TermDeferredStorageSvcImpl.class);
	final private List<TermCodeSystem> myDeferredCodeSystemsDeletions = Collections.synchronizedList(new ArrayList<>());
	final private Queue<TermCodeSystemVersion> myDeferredCodeSystemVersionsDeletions = new ConcurrentLinkedQueue<>();
	final private List<TermConcept> myDeferredConcepts = Collections.synchronizedList(new ArrayList<>());
	final private List<ValueSet> myDeferredValueSets = Collections.synchronizedList(new ArrayList<>());
	final private List<ConceptMap> myDeferredConceptMaps = Collections.synchronizedList(new ArrayList<>());
	final private List<TermConceptParentChildLink> myConceptLinksToSaveLater = Collections.synchronizedList(new ArrayList<>());
	final private List<JobExecution> myCurrentJobExecutions = Collections.synchronizedList(new ArrayList<>());


	@Autowired
	protected ITermConceptDao myConceptDao;
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
	private ISchedulerService mySchedulerService;
	@Autowired
	private ITermVersionAdapterSvc myTerminologyVersionAdapterSvc;

	@Autowired
	private TermConceptDaoSvc myTermConceptDaoSvc;

	@Autowired
	private IBatchJobSubmitter myJobSubmitter;

	@Autowired
	private JobOperator myJobOperator;

	@Autowired
	@Qualifier(TERM_CODE_SYSTEM_DELETE_JOB_NAME)
	private org.springframework.batch.core.Job myTermCodeSystemDeleteJob;

	@Autowired
	@Qualifier(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME)
	private org.springframework.batch.core.Job myTermCodeSystemVersionDeleteJob;


	@Override
	public void addConceptToStorageQueue(TermConcept theConcept) {
		Validate.notNull(theConcept);
		myDeferredConcepts.add(theConcept);
	}

	@Override
	public void addConceptLinkToStorageQueue(TermConceptParentChildLink theConceptLink) {
		Validate.notNull(theConceptLink);
		myConceptLinksToSaveLater.add(theConceptLink);
	}

	@Override
	public void addConceptMapsToStorageQueue(List<ConceptMap> theConceptMaps) {
		Validate.notNull(theConceptMaps);
		myDeferredConceptMaps.addAll(theConceptMaps);
	}

	@Override
	public void addValueSetsToStorageQueue(List<ValueSet> theValueSets) {
		Validate.notNull(theValueSets);
		myDeferredValueSets.addAll(theValueSets);
	}

	@Override
	public void deleteCodeSystemForResource(ResourceTable theCodeSystemToDelete) {
		// there are use cases (at least in tests) where the code system is not present for the resource but versions are,
		// so, as code system deletion also deletes versions, we try the system first but if not present we also try versions
		TermCodeSystem termCodeSystemToDelete = myCodeSystemDao.findByResourcePid(theCodeSystemToDelete.getResourceId());
		if (termCodeSystemToDelete != null) {
			termCodeSystemToDelete.setCodeSystemUri("urn:uuid:" + UUID.randomUUID());
			myCodeSystemDao.save(termCodeSystemToDelete);
			myDeferredCodeSystemsDeletions.add(termCodeSystemToDelete);
			return;
		}

		List<TermCodeSystemVersion> codeSystemVersionsToDelete = myCodeSystemVersionDao.findByCodeSystemResourcePid(theCodeSystemToDelete.getResourceId());
		for (TermCodeSystemVersion codeSystemVersionToDelete : codeSystemVersionsToDelete) {
			if (codeSystemVersionToDelete != null) {
				myDeferredCodeSystemVersionsDeletions.add(codeSystemVersionToDelete);
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
			if (myCodeSystemVersionDao.findById(next.getCodeSystemVersion().getPid()).isPresent()) {
				try {
					codeCount += myTermConceptDaoSvc.saveConcept(next);
				} catch (Exception theE) {
					ourLog.error("Exception thrown when attempting to save TermConcept {} in Code System {}",
						next.getCode(), next.getCodeSystemVersion().getCodeSystemDisplayName(), theE);
				}
			} else {
				ourLog.warn("Unable to save deferred TermConcept {} because Code System {} version PID {} is no longer valid. Code system may have since been replaced.",
					next.getCode(), next.getCodeSystemVersion().getCodeSystemDisplayName(), next.getCodeSystemVersion().getPid());
			}
		}

		if (codeCount > 0) {
			ourLog.info("Saved {} deferred concepts ({} codes remain and {} relationships remain) in {}ms ({} codes/sec)",
				codeCount, myDeferredConcepts.size(), myConceptLinksToSaveLater.size(), stopwatch.getMillis(), stopwatch.formatThroughput(codeCount, TimeUnit.SECONDS));
		}

		if (codeCount == 0) {
			count = Math.min(1000, myConceptLinksToSaveLater.size());
			ourLog.info("Saving {} deferred concept relationships...", count);
			while (relCount < count && myConceptLinksToSaveLater.size() > 0) {
				TermConceptParentChildLink next = myConceptLinksToSaveLater.remove(0);
				assert next.getChild() != null;
				assert next.getParent() != null;

				if ((next.getChild().getId() == null || !myConceptDao.findById(next.getChild().getId()).isPresent())
					|| (next.getParent().getId() == null || !myConceptDao.findById(next.getParent().getId()).isPresent())) {
					ourLog.warn("Not inserting link from child {} to parent {} because it appears to have been deleted", next.getParent().getCode(), next.getChild().getCode());
					continue;
				}

				saveConceptLink(next);
				relCount++;
			}
		}

		if (relCount > 0) {
			ourLog.info("Saved {} deferred relationships ({} remain) in {}ms ({} entries/sec)",
				relCount, myConceptLinksToSaveLater.size(), stopwatch.getMillis(), stopwatch.formatThroughput(relCount, TimeUnit.SECONDS));
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
		myDeferredCodeSystemsDeletions.clear();
		myConceptLinksToSaveLater.clear();
		myDeferredCodeSystemVersionsDeletions.clear();
		clearJobExecutions();
	}


	private void clearJobExecutions() {
		for (JobExecution jobExecution : myCurrentJobExecutions) {
			if (!jobExecution.isRunning()) {
				continue;
			}

			try {
				myJobOperator.stop(jobExecution.getId());
			} catch (Exception e) {
				ourLog.error("Couldn't stop job execution {}: {}", jobExecution.getId(), e);
			}
		}

		myCurrentJobExecutions.clear();
	}


	private <T> T runInTransaction(Supplier<T> theRunnable) {
		assert !TransactionSynchronizationManager.isActualTransactionActive();

		return new TransactionTemplate(myTransactionMgr).execute(tx -> theRunnable.get());
	}

	@Override
	public void saveAllDeferred() {
		while (!isStorageQueueEmpty()) {
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

			if (!isDeferredConcepts() &&
				!isConceptLinksToSaveLater() &&
				!isDeferredValueSets() &&
				!isDeferredConceptMaps() &&
				!isDeferredCodeSystemDeletions()) {
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

				continue;
			}

			if (isDeferredCodeSystemVersionDeletions()) {
				processDeferredCodeSystemVersionDeletions();
			}

			if (isDeferredCodeSystemDeletions()) {
				processDeferredCodeSystemDeletions();
			}
		}
	}

	private boolean isDeferredCodeSystemVersionDeletions() {
		return !myDeferredCodeSystemVersionsDeletions.isEmpty();
	}


	private void processDeferredCodeSystemDeletions() {
		for (TermCodeSystem next : myDeferredCodeSystemsDeletions) {
			deleteTermCodeSystemOffline(next.getPid());
		}
		myDeferredCodeSystemsDeletions.clear();
	}


	private void processDeferredCodeSystemVersionDeletions() {
		for (TermCodeSystemVersion next : myDeferredCodeSystemVersionsDeletions) {
			deleteTermCodeSystemVersionOffline(next.getPid());
		}
		myDeferredCodeSystemVersionsDeletions.clear();
	}


	private void deleteTermCodeSystemVersionOffline(Long theCodeSystemVersionPid) {
		JobParameters jobParameters = new JobParameters(
			Collections.singletonMap(
				JOB_PARAM_CODE_SYSTEM_VERSION_ID, new JobParameter(theCodeSystemVersionPid, true)));

		try {

			JobExecution jobExecution = myJobSubmitter.runJob(myTermCodeSystemVersionDeleteJob, jobParameters);
			myCurrentJobExecutions.add(jobExecution);

		} catch (JobParametersInvalidException theE) {
			throw new InternalErrorException(Msg.code(850) + "Offline job submission for TermCodeSystemVersion: " +
				theCodeSystemVersionPid + " failed: " + theE);
		}
	}


	private void deleteTermCodeSystemOffline(Long theCodeSystemPid) {
		JobParameters jobParameters = new JobParameters(
			Collections.singletonMap(
				JOB_PARAM_CODE_SYSTEM_ID, new JobParameter(theCodeSystemPid, true)));

		try {

			JobExecution jobExecution = myJobSubmitter.runJob(myTermCodeSystemDeleteJob, jobParameters);
			myCurrentJobExecutions.add(jobExecution);

		} catch (JobParametersInvalidException theE) {
			throw new InternalErrorException(Msg.code(851) + "Offline job submission for TermCodeSystem: " +
				theCodeSystemPid + " failed: " + theE);
		}
	}


	@Override
	public boolean isStorageQueueEmpty() {
		boolean retVal = !isProcessDeferredPaused();
		retVal &= !isDeferredConcepts();
		retVal &= !isConceptLinksToSaveLater();
		retVal &= !isDeferredValueSets();
		retVal &= !isDeferredConceptMaps();
		retVal &= !isDeferredCodeSystemDeletions();
		retVal &= !isJobsExecuting();
		return retVal;
	}

	private boolean isJobsExecuting() {
		return myCurrentJobExecutions.stream().anyMatch(JobExecution::isRunning);
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

	private boolean isDeferredCodeSystemDeletions() {
		return !myDeferredCodeSystemsDeletions.isEmpty() || !myDeferredCodeSystemVersionsDeletions.isEmpty();
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

	@PostConstruct
	public void scheduleJob() {
		// TODO KHS what does this mean?
		// Register scheduled job to save deferred concepts
		// In the future it would be great to make this a cluster-aware task somehow
		ScheduledJobDefinition jobDefinition = new ScheduledJobDefinition();
		jobDefinition.setId(Job.class.getName());
		jobDefinition.setJobClass(Job.class);
		mySchedulerService.scheduleLocalJob(5000, jobDefinition);
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
	@VisibleForTesting
	public void logQueueForUnitTest() {
		ourLog.info("isProcessDeferredPaused: {}", isProcessDeferredPaused());
		ourLog.info("isDeferredConcepts: {}", isDeferredConcepts());
		ourLog.info("isConceptLinksToSaveLater: {}", isConceptLinksToSaveLater());
		ourLog.info("isDeferredValueSets: {}", isDeferredValueSets());
		ourLog.info("isDeferredConceptMaps: {}", isDeferredConceptMaps());
		ourLog.info("isDeferredCodeSystemDeletions: {}", isDeferredCodeSystemDeletions());
	}

	@Override
	public void deleteCodeSystemVersion(TermCodeSystemVersion theCodeSystemVersion) {
		myDeferredCodeSystemVersionsDeletions.add(theCodeSystemVersion);
	}

	public static class Job implements HapiJob {
		@Autowired
		private ITermDeferredStorageSvc myTerminologySvc;

		@Override
		public void execute(JobExecutionContext theContext) {
			myTerminologySvc.saveDeferred();
		}
	}


}
