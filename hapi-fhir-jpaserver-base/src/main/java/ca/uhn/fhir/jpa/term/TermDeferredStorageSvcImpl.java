package ca.uhn.fhir.jpa.term;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptParentChildLinkDao;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TermDeferredStorageSvcImpl implements ITermDeferredStorageSvc {

	private static final Logger ourLog = LoggerFactory.getLogger(TermDeferredStorageSvcImpl.class);
	@Autowired
	protected ITermConceptDao myConceptDao;
	@Autowired
	protected PlatformTransactionManager myTransactionMgr;
	private boolean myProcessDeferred = true;
	private List<TermConcept> myDeferredConcepts = Collections.synchronizedList(new ArrayList<>());
	private List<ValueSet> myDeferredValueSets = Collections.synchronizedList(new ArrayList<>());
	private List<ConceptMap> myDeferredConceptMaps = Collections.synchronizedList(new ArrayList<>());
	private List<TermConceptParentChildLink> myConceptLinksToSaveLater = Collections.synchronizedList(new ArrayList<>());
	@Autowired
	private DaoConfig myDaoConfig;
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
	public void saveAllDeferred() {
		while (!isStorageQueueEmpty()) {
			saveDeferred();
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

		int count = Math.min(myDaoConfig.getDeferIndexingForCodesystemsOfSize(), myDeferredConcepts.size());
		ourLog.info("Saving {} deferred concepts...", count);
		while (codeCount < count && myDeferredConcepts.size() > 0) {
			TermConcept next = myDeferredConcepts.remove(0);
			codeCount += myCodeSystemStorageSvc.saveConcept(next);
		}

		if (codeCount > 0) {
			ourLog.info("Saved {} deferred concepts ({} codes remain and {} relationships remain) in {}ms ({}ms / code)",
				codeCount, myDeferredConcepts.size(), myConceptLinksToSaveLater.size(), stopwatch.getMillis(), stopwatch.getMillisPerOperation(codeCount));
		}

		if (codeCount == 0) {
			count = Math.min(myDaoConfig.getDeferIndexingForCodesystemsOfSize(), myConceptLinksToSaveLater.size());
			ourLog.info("Saving {} deferred concept relationships...", count);
			while (relCount < count && myConceptLinksToSaveLater.size() > 0) {
				TermConceptParentChildLink next = myConceptLinksToSaveLater.remove(0);

				if (!myConceptDao.findById(next.getChild().getId()).isPresent() || !myConceptDao.findById(next.getParent().getId()).isPresent()) {
					ourLog.warn("Not inserting link from child {} to parent {} because it appears to have been deleted", next.getParent().getCode(), next.getChild().getCode());
					continue;
				}

				saveConceptLink(next);
				relCount++;
			}
		}

		if (relCount > 0) {
			ourLog.info("Saved {} deferred relationships ({} remain) in {}ms ({}ms / entry)",
				relCount, myConceptLinksToSaveLater.size(), stopwatch.getMillis(), stopwatch.getMillisPerOperation(relCount));
		}

		if ((myDeferredConcepts.size() + myConceptLinksToSaveLater.size()) == 0) {
			ourLog.info("All deferred concepts and relationships have now been synchronized to the database");
		}
	}

	private void processDeferredValueSets() {
		int count = Math.min(myDeferredValueSets.size(), 20);
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
		myDeferredValueSets.clear();
		myDeferredConceptMaps.clear();
		myDeferredConcepts.clear();
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
				!isDeferredConceptMaps()) {
				return;
			}

			TransactionTemplate tt = new TransactionTemplate(myTransactionMgr);
			tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
			if (isDeferredConceptsOrConceptLinksToSaveLater()) {
				tt.execute(t -> {
					processDeferredConcepts();
					return null;
				});
			}

			if (isDeferredValueSets()) {
				tt.execute(t -> {
					processDeferredValueSets();
					return null;
				});
			}
			if (isDeferredConceptMaps()) {
				tt.execute(t -> {
					processDeferredConceptMaps();
					return null;
				});
			}

		}
	}

	@Override
	public boolean isStorageQueueEmpty() {
		boolean retVal = true;
		retVal &= !isProcessDeferredPaused();
		retVal &= !isDeferredConcepts();
		retVal &= !isConceptLinksToSaveLater();
		retVal &= !isDeferredValueSets();
		retVal &= !isDeferredConceptMaps();
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

	public static class Job implements HapiJob {
		@Autowired
		private ITermDeferredStorageSvc myTerminologySvc;

		@Override
		public void execute(JobExecutionContext theContext) {
			myTerminologySvc.saveDeferred();
		}
	}

	@VisibleForTesting
	void setTransactionManagerForUnitTest(PlatformTransactionManager theTxManager) {
		myTransactionMgr = theTxManager;
	}

	@VisibleForTesting
	void setDaoConfigForUnitTest(DaoConfig theDaoConfig) {
		myDaoConfig = theDaoConfig;
	}

	@VisibleForTesting
	void setCodeSystemStorageSvcForUnitTest(ITermCodeSystemStorageSvc theCodeSystemStorageSvc) {
		myCodeSystemStorageSvc = theCodeSystemStorageSvc;
	}

	@VisibleForTesting
	void setConceptDaoForUnitTest(ITermConceptDao theConceptDao) {
		myConceptDao = theConceptDao;
	}
}
