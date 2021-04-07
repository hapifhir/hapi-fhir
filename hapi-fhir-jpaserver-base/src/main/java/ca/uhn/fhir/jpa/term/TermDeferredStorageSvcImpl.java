package ca.uhn.fhir.jpa.term;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemDao;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemVersionDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDesignationDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptParentChildLinkDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptPropertyDao;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermVersionAdapterSvc;
import ca.uhn.fhir.util.StopWatch;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ValueSet;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class TermDeferredStorageSvcImpl implements ITermDeferredStorageSvc {

	private static final Logger ourLog = LoggerFactory.getLogger(TermDeferredStorageSvcImpl.class);
	final private List<TermCodeSystem> myDeferredCodeSystemsDeletions = Collections.synchronizedList(new ArrayList<>());
	final private List<TermCodeSystemVersion> myDeferredCodeSystemVersionsDeletions = Collections.synchronizedList(new ArrayList<>());
	final private List<TermConcept> myDeferredConcepts = Collections.synchronizedList(new ArrayList<>());
	final private List<ValueSet> myDeferredValueSets = Collections.synchronizedList(new ArrayList<>());
	final private List<ConceptMap> myDeferredConceptMaps = Collections.synchronizedList(new ArrayList<>());
	final private List<TermConceptParentChildLink> myConceptLinksToSaveLater = Collections.synchronizedList(new ArrayList<>());
	@Autowired
	protected ITermConceptDao myConceptDao;
	@Autowired
	protected ITermCodeSystemDao myCodeSystemDao;
	@Autowired
	protected ITermCodeSystemVersionDao myCodeSystemVersionDao;
	@Autowired
	protected PlatformTransactionManager myTransactionMgr;
	@Autowired
	protected ITermConceptPropertyDao myConceptPropertyDao;
	@Autowired
	protected ITermConceptDesignationDao myConceptDesignationDao;
	private boolean myProcessDeferred = true;
	@Autowired
	private ITermConceptParentChildLinkDao myConceptParentChildLinkDao;
	@Autowired
	private ISchedulerService mySchedulerService;
	@Autowired
	private ITermVersionAdapterSvc myTerminologyVersionAdapterSvc;
	@Autowired
	private ITermCodeSystemStorageSvc myCodeSystemStorageSvc;

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
	@Transactional
	public void deleteCodeSystem(TermCodeSystem theCodeSystem) {
		theCodeSystem.setCodeSystemUri("urn:uuid:" + UUID.randomUUID().toString());
		myCodeSystemDao.save(theCodeSystem);
		myDeferredCodeSystemsDeletions.add(theCodeSystem);
	}

	@Override
	@Transactional
	public void deleteCodeSystemForResource(ResourceTable theCodeSystemToDelete) {
		List<TermCodeSystemVersion> codeSystemVersionsToDelete = myCodeSystemVersionDao.findByCodeSystemResourcePid(theCodeSystemToDelete.getResourceId());
		for (TermCodeSystemVersion codeSystemVersionToDelete : codeSystemVersionsToDelete) {
			if (codeSystemVersionToDelete != null) {
				myDeferredCodeSystemVersionsDeletions.add(codeSystemVersionToDelete);
			}
		}
		TermCodeSystem codeSystemToDelete = myCodeSystemDao.findByResourcePid(theCodeSystemToDelete.getResourceId());
		if (codeSystemToDelete != null) {
			deleteCodeSystem(codeSystemToDelete);
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
					codeCount += myCodeSystemStorageSvc.saveConcept(next);
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
	}

	private void runInTransaction(Runnable theRunnable) {
		assert !TransactionSynchronizationManager.isActualTransactionActive();

		new TransactionTemplate(myTransactionMgr).executeWithoutResult(tx -> theRunnable.run());
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
			myCodeSystemStorageSvc.deleteCodeSystem(next);
		}
		myDeferredCodeSystemsDeletions.clear();
	}

	private void processDeferredCodeSystemVersionDeletions() {
		for (TermCodeSystemVersion next : myDeferredCodeSystemVersionsDeletions) {
			processDeferredCodeSystemVersionDeletions(next.getPid());
		}

		myDeferredCodeSystemVersionsDeletions.clear();
	}

	private void processDeferredCodeSystemVersionDeletions(long theCodeSystemVersionPid) {
		assert !TransactionSynchronizationManager.isActualTransactionActive();
		ourLog.info(" * Deleting CodeSystemVersion[id={}]", theCodeSystemVersionPid);

		PageRequest page1000 = PageRequest.of(0, 1000);

		// Parent/Child links
		{
			String descriptor = "parent/child links";
			Supplier<Slice<Long>> loader = () -> myConceptParentChildLinkDao.findIdsByCodeSystemVersion(page1000, theCodeSystemVersionPid);
			Supplier<Integer> counter = () -> myConceptParentChildLinkDao.countByCodeSystemVersion(theCodeSystemVersionPid);
			doDelete(descriptor, loader, counter, myConceptParentChildLinkDao);
		}

		// Properties
		{
			String descriptor = "concept properties";
			Supplier<Slice<Long>> loader = () -> myConceptPropertyDao.findIdsByCodeSystemVersion(page1000, theCodeSystemVersionPid);
			Supplier<Integer> counter = () -> myConceptPropertyDao.countByCodeSystemVersion(theCodeSystemVersionPid);
			doDelete(descriptor, loader, counter, myConceptPropertyDao);
		}

		// Designations
		{
			String descriptor = "concept designations";
			Supplier<Slice<Long>> loader = () -> myConceptDesignationDao.findIdsByCodeSystemVersion(page1000, theCodeSystemVersionPid);
			Supplier<Integer> counter = () -> myConceptDesignationDao.countByCodeSystemVersion(theCodeSystemVersionPid);
			doDelete(descriptor, loader, counter, myConceptDesignationDao);
		}

		// Concepts
		{
			String descriptor = "concepts";
			// For some reason, concepts are much slower to delete, so use a smaller batch size
			PageRequest page100 = PageRequest.of(0, 100);
			Supplier<Slice<Long>> loader = () -> myConceptDao.findIdsByCodeSystemVersion(page100, theCodeSystemVersionPid);
			Supplier<Integer> counter = () -> myConceptDao.countByCodeSystemVersion(theCodeSystemVersionPid);
			doDelete(descriptor, loader, counter, myConceptDao);
		}

		runInTransaction(() -> {
			Optional<TermCodeSystem> codeSystemOpt = myCodeSystemDao.findWithCodeSystemVersionAsCurrentVersion(theCodeSystemVersionPid);
			if (codeSystemOpt.isPresent()) {
				TermCodeSystem codeSystem = codeSystemOpt.get();
				ourLog.info(" * Removing code system version {} as current version of code system {}", theCodeSystemVersionPid, codeSystem.getPid());
				codeSystem.setCurrentVersion(null);
				myCodeSystemDao.save(codeSystem);
			}

			ourLog.info(" * Deleting code system version");
			Optional<TermCodeSystemVersion> csv = myCodeSystemVersionDao.findById(theCodeSystemVersionPid);
			if (csv.isPresent()) {
				myCodeSystemVersionDao.delete(csv.get());
			}
		});


	}

	private <T> void doDelete(String theDescriptor, Supplier<Slice<Long>> theLoader, Supplier<Integer> theCounter, JpaRepository<T, Long> theDao) {
		assert !TransactionSynchronizationManager.isActualTransactionActive();

		int count;
		ourLog.info(" * Deleting {}", theDescriptor);
		int totalCount = runInTransaction(theCounter);
		StopWatch sw = new StopWatch();
		count = 0;
		while (true) {
			Slice<Long> link = runInTransaction(theLoader);
			if (!link.hasContent()) {
				break;
			}

			runInTransaction(() -> link.forEach(theDao::deleteById));

			count += link.getNumberOfElements();
			ourLog.info(" * {} {} deleted ({}/{}) remaining - {}/sec - ETA: {}", count, theDescriptor, count, totalCount, sw.formatThroughput(count, TimeUnit.SECONDS), sw.getEstimatedTimeRemaining(count, totalCount));
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
		return retVal;
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
	void setCodeSystemStorageSvcForUnitTest(ITermCodeSystemStorageSvc theCodeSystemStorageSvc) {
		myCodeSystemStorageSvc = theCodeSystemStorageSvc;
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
	public synchronized void deleteCodeSystemVersion(TermCodeSystemVersion theCodeSystemVersion) {
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
